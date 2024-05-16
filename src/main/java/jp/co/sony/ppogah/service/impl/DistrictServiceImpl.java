package jp.co.sony.ppogah.service.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

import jp.co.sony.ppogah.common.PgCrowdConstants;
import jp.co.sony.ppogah.dto.DistrictDto;
import jp.co.sony.ppogah.entity.Chiho;
import jp.co.sony.ppogah.entity.City;
import jp.co.sony.ppogah.entity.District;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.ChihoRepository;
import jp.co.sony.ppogah.repository.CityRepository;
import jp.co.sony.ppogah.repository.DistrictRepository;
import jp.co.sony.ppogah.service.IDistrictService;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import jp.co.sony.ppogah.utils.SecondBeanUtils;
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
	public List<String> getDistrictChihos(final String chihoName) {
		final List<String> chihos = new ArrayList<>();
		final List<String> chihoList = this.chihoRepository.findAll(Sort.by(Direction.ASC, "id")).stream()
				.map(Chiho::getName).filter(a -> CommonProjectUtils.isNotEqual(a, chihoName))
				.collect(Collectors.toList());
		chihos.add(chihoName);
		chihos.addAll(chihoList);
		return chihos;
	}

	@Override
	public List<DistrictDto> getDistrictsByCityId(final String cityId) {
		final List<DistrictDto> districtDtos = new ArrayList<>();
		final List<District> districts = this.districtRepository.findAll(Sort.by(Direction.ASC, "id"));
		final List<DistrictDto> districtDtos1 = districts.stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			districtDto.setId(item.getId().toString());
			districtDto.setChiho(item.getChiho().getName());
			return districtDto;
		}).collect(Collectors.toList());
		if (!CommonProjectUtils.isDigital(cityId)) {
			final DistrictDto districtDto = new DistrictDto();
			districtDto.setId("0");
			districtDto.setName(PgCrowdConstants.DEFAULT_ROLE_NAME);
			districtDto.setChiho(CommonProjectUtils.EMPTY_STRING);
			districtDtos.add(districtDto);
		} else {
			final City city = this.cityRepository.findById(Long.parseLong(cityId)).orElseThrow(() -> {
				throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
			});
			final DistrictDto selecteDistrictDto = districtDtos1.stream()
					.filter(a -> CommonProjectUtils.isEqual(Long.parseLong(a.getId()), city.getDistrictId()))
					.collect(Collectors.toList()).get(0);
			districtDtos.add(selecteDistrictDto);
		}
		districtDtos.addAll(districtDtos1);
		return districtDtos;
	}

	@Override
	public Pagination<DistrictDto> getDistrictsByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<District> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Specification<District> specification = Specification.where(where1);
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Page<District> pages = this.districtRepository.findAll(specification, pageRequest);
			final List<DistrictDto> districtDtos = pages.stream().map(item -> {
				final DistrictDto districtDto = new DistrictDto();
				SecondBeanUtils.copyNullableProperties(item, districtDto);
				districtDto.setId(item.getId().toString());
				districtDto.setChiho(item.getChiho().getName());
				districtDto.setShutoName(
						item.getCities().stream().filter(a -> Objects.equals(a.getId(), item.getShutoId()))
								.collect(Collectors.toList()).get(0).getName());
				districtDto.setPopulation(
						item.getCities().stream().map(City::getPopulation).reduce((a, v) -> a + v).get());
				return districtDto;
			}).collect(Collectors.toList());
			return Pagination.of(districtDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Page<District> pages = this.districtRepository.findByShutoLike(searchStr, pageRequest);
		final List<DistrictDto> districtDtos = pages.stream().map(item -> {
			final DistrictDto districtDto = new DistrictDto();
			SecondBeanUtils.copyNullableProperties(item, districtDto);
			districtDto.setId(item.getId().toString());
			districtDto.setChiho(item.getChiho().getName());
			districtDto.setShutoName(item.getCities().stream().filter(a -> Objects.equals(a.getId(), item.getShutoId()))
					.collect(Collectors.toList()).get(0).getName());
			districtDto.setPopulation(item.getCities().stream().map(City::getPopulation).reduce((a, v) -> a + v).get());
			return districtDto;
		}).collect(Collectors.toList());
		return Pagination.of(districtDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> update(final DistrictDto districtDto) {
		final District originalEntity = new District();
		originalEntity.setId(Long.parseLong(districtDto.getId()));
		originalEntity.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<District> example = Example.of(originalEntity, ExampleMatcher.matching());
		final District district = this.districtRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		SecondBeanUtils.copyNullableProperties(district, originalEntity);
		SecondBeanUtils.copyNullableProperties(districtDto, district);
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
