package jp.co.toshiba.ppocph.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.toshiba.ppocph.common.PgCrowdConstants;
import jp.co.toshiba.ppocph.dto.ChihoDto;
import jp.co.toshiba.ppocph.dto.CityDto;
import jp.co.toshiba.ppocph.dto.DistrictDto;
import jp.co.toshiba.ppocph.entity.City;
import jp.co.toshiba.ppocph.entity.District;
import jp.co.toshiba.ppocph.exception.PgCrowdException;
import jp.co.toshiba.ppocph.repository.ChihoRepository;
import jp.co.toshiba.ppocph.repository.CityRepository;
import jp.co.toshiba.ppocph.repository.DistrictRepository;
import jp.co.toshiba.ppocph.service.IDistrictService;
import jp.co.toshiba.ppocph.utils.CommonProjectUtils;
import jp.co.toshiba.ppocph.utils.Pagination;
import jp.co.toshiba.ppocph.utils.ResultDto;
import jp.co.toshiba.ppocph.utils.SecondBeanUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 地域サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class DistrictServiceImpl implements IDistrictService {

	/**
	 * 地方管理リポジトリ
	 */
	private final ChihoRepository chihoRepository;

	/**
	 * 地域管理リポジトリ
	 */
	private final DistrictRepository districtRepository;

	/**
	 * 都市管理リポジトリ
	 */
	private final CityRepository cityRepository;

	@Override
	public List<ChihoDto> getDistrictChihos(final String chihoName) {
		final List<ChihoDto> chihos = new ArrayList<>();
		final List<ChihoDto> chihoList = this.chihoRepository.findAll(Sort.by(Direction.ASC, "id")).stream()
				.map(item -> {
					final ChihoDto chihoDto = new ChihoDto();
					SecondBeanUtils.copyNullableProperties(item, chihoDto);
					chihoDto.setId(item.getId().toString());
					return chihoDto;
				}).collect(Collectors.toList());
		chihos.addAll(chihoList.stream().filter(a -> CommonProjectUtils.isEqual(a.getName(), chihoName))
				.collect(Collectors.toList()));
		chihos.addAll(chihoList);
		return chihos.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<CityDto> getDistrictCities(final DistrictDto districtDto) {
		final List<CityDto> cityDtos = new ArrayList<>();
		final City aCity = new City();
		aCity.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		aCity.setDistrictId(Long.parseLong(districtDto.getId()));
		final Example<City> example = Example.of(aCity, ExampleMatcher.matching());
		final List<CityDto> cities = this.cityRepository.findAll(example, Sort.by(Direction.ASC, "id")).stream()
				.map(item -> {
					final CityDto cityDto = new CityDto();
					cityDto.setId(item.getId().toString());
					cityDto.setName(item.getName());
					return cityDto;
				}).collect(Collectors.toList());
		final CityDto cityDto = cities.stream()
				.filter(a -> CommonProjectUtils.isEqual(a.getId(), Long.parseLong(districtDto.getShutoId())))
				.findFirst().get();
		cityDtos.add(cityDto);
		cityDtos.addAll(cities);
		return cityDtos.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public List<DistrictDto> getDistrictsByCityId(final String cityId) {
		final List<DistrictDto> districtDtos = new ArrayList<>();
		final List<District> districts = this.districtRepository.findAll(Sort.by(Direction.ASC, "id"));
		final List<DistrictDto> districtDtos1 = districts.stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			districtDto.setId(item.getId().toString());
			districtDto.setChihoName(item.getChiho().getName());
			return districtDto;
		}).collect(Collectors.toList());
		if (!CommonProjectUtils.isDigital(cityId)) {
			final DistrictDto districtDto = new DistrictDto();
			districtDto.setId("0");
			districtDto.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
			districtDto.setChihoName(CommonProjectUtils.EMPTY_STRING);
			districtDtos.add(districtDto);
		} else {
			final City city = this.cityRepository.findById(Long.parseLong(cityId)).orElseThrow(() -> {
				throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
			});
			final DistrictDto selecteDistrictDto = districtDtos1.stream()
					.filter(a -> CommonProjectUtils.isEqual(Long.parseLong(a.getId()), city.getDistrictId()))
					.findFirst().get();
			districtDtos.add(selecteDistrictDto);
		}
		districtDtos.addAll(districtDtos1);
		return districtDtos.stream().distinct().collect(Collectors.toList());
	}

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<District> specification = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Page<District> pages = this.districtRepository.findAll(specification, pageRequest);
			final List<DistrictDto> districtDtos = pages.stream().map(item -> {
				final DistrictDto districtDto = new DistrictDto();
				SecondBeanUtils.copyNullableProperties(item, districtDto);
				districtDto.setId(item.getId().toString());
				districtDto.setChihoName(item.getChiho().getName());
				districtDto.setShutoName(item.getShutoCity().getName());
				districtDto.setPopulation(
						item.getCities().stream().map(City::getPopulation).reduce((a, v) -> a + v).get());
				return districtDto;
			}).collect(Collectors.toList());
			return Pagination.of(districtDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Specification<District> specification2 = (root, query, criteriaBuilder) -> {
			criteriaBuilder.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
			return criteriaBuilder.or(criteriaBuilder.like(root.get("name"), searchStr),
					criteriaBuilder.like(root.get("chiho").get("name"), searchStr),
					criteriaBuilder.like(root.get("shutoCity").get("name"), searchStr));
		};
		final Page<District> pages = this.districtRepository.findAll(specification2, pageRequest);
		final List<DistrictDto> districtDtos = pages.stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			districtDto.setId(item.getId().toString());
			districtDto.setChihoName(item.getChiho().getName());
			districtDto.setShutoName(item.getShutoCity().getName());
			districtDto.setPopulation(item.getCities().stream().map(City::getPopulation).reduce((a, v) -> a + v).get());
			return districtDto;
		}).collect(Collectors.toList());
		return Pagination.of(districtDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> update(final DistrictDto districtDto) {
		final District originalEntity = new District();
		originalEntity.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		originalEntity.setId(Long.parseLong(districtDto.getId()));
		final Example<District> example = Example.of(originalEntity, ExampleMatcher.matching());
		final District district = this.districtRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		SecondBeanUtils.copyNullableProperties(district, originalEntity);
		SecondBeanUtils.copyNullableProperties(districtDto, district);
		district.setChihoId(Long.parseLong(districtDto.getChihoId()));
		district.setShutoId(Long.parseLong(districtDto.getShutoId()));
		if (CommonProjectUtils.isEqual(originalEntity, district)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.districtRepository.saveAndFlush(district);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_DISTRICT_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
