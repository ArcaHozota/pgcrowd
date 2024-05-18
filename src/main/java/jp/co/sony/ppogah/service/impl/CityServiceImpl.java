package jp.co.sony.ppogah.service.impl;

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
import jp.co.sony.ppogah.dto.CityDto;
import jp.co.sony.ppogah.entity.City;
import jp.co.sony.ppogah.entity.District;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.CityRepository;
import jp.co.sony.ppogah.repository.DistrictRepository;
import jp.co.sony.ppogah.service.ICityService;
import jp.co.sony.ppogah.utils.CommonProjectUtils;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import jp.co.sony.ppogah.utils.SecondBeanUtils;
import jp.co.sony.ppogah.utils.SnowflakeUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;

/**
 * 都市サービス実装クラス
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Service
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class CityServiceImpl implements ICityService {

	/**
	 * 都市管理リポジトリ
	 */
	private final CityRepository cityRepository;

	/**
	 * 地域管理リポジトリ
	 */
	private final DistrictRepository districtRepository;

	@Override
	public ResultDto<String> checkDuplicated(final String name, final Long districtId) {
		final District aDistrict = new District();
		aDistrict.setId(districtId);
		aDistrict.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<District> example = Example.of(aDistrict, ExampleMatcher.matching());
		final District district = this.districtRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final List<String> cityNameList = district.getCities().stream().map(City::getName).collect(Collectors.toList());
		if (cityNameList.contains(name)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public Pagination<CityDto> getCitiesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowdConstants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final City aCity = new City();
		aCity.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<City> example = Example.of(aCity, ExampleMatcher.matching());
		if (CommonProjectUtils.isEmpty(keyword)) {
			final Page<City> pages = this.cityRepository.findAll(example, pageRequest);
			final List<CityDto> cityDtos = pages.stream().map(item -> {
				final CityDto cityDto = new CityDto();
				SecondBeanUtils.copyNullableProperties(item, cityDto);
				cityDto.setId(item.getId().toString());
				cityDto.setDistrictName(item.getDistrict().getName());
				return cityDto;
			}).collect(Collectors.toList());
			return Pagination.of(cityDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = CommonProjectUtils.getDetailKeyword(keyword);
		final Specification<City> specification = (root, query, criteriaBuilder) -> {
			criteriaBuilder.equal(root.get("deleteFlg"), PgCrowdConstants.LOGIC_DELETE_INITIAL);
			return criteriaBuilder.or(criteriaBuilder.like(root.get("name"), searchStr),
					criteriaBuilder.like(root.get("pronunciation"), searchStr),
					criteriaBuilder.like(root.get("district").get("name"), searchStr));
		};
		final Page<City> pages = this.cityRepository.findAll(specification, pageRequest);
		final List<CityDto> cityDtos = pages.stream().map(item -> {
			final CityDto cityDto = new CityDto();
			SecondBeanUtils.copyNullableProperties(item, cityDto);
			cityDto.setId(item.getId().toString());
			cityDto.setDistrictName(item.getDistrict().getName());
			return cityDto;
		}).collect(Collectors.toList());
		return Pagination.of(cityDtos, pages.getTotalElements(), pageNum, PgCrowdConstants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final City aCity = new City();
		aCity.setId(id);
		aCity.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		final Example<City> example = Example.of(aCity, ExampleMatcher.matching());
		final City city = this.cityRepository.findOne(example).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		if (Objects.equals(id, city.getDistrict().getShutoId())) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_FORBIDDEN3);
		}
		city.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_FLG);
		this.cityRepository.saveAndFlush(city);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final CityDto cityDto) {
		final City city = new City();
		SecondBeanUtils.copyNullableProperties(cityDto, city);
		city.setId(SnowflakeUtils.snowflakeId());
		city.setDeleteFlg(PgCrowdConstants.LOGIC_DELETE_INITIAL);
		this.cityRepository.saveAndFlush(city);
	}

	@Override
	public ResultDto<String> update(final CityDto cityDto) {
		final City city = this.cityRepository.findById(Long.parseLong(cityDto.getId())).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowdConstants.MESSAGE_STRING_FATAL_ERROR);
		});
		final City originalEntity = new City();
		SecondBeanUtils.copyNullableProperties(city, originalEntity);
		SecondBeanUtils.copyNullableProperties(cityDto, city);
		city.setDistrictId(Long.parseLong(cityDto.getDistrictId()));
		if (CommonProjectUtils.isEqual(originalEntity, city)) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.cityRepository.saveAndFlush(city);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowdConstants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
