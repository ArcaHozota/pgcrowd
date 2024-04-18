package jp.co.sony.ppogah.service.impl;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import jp.co.sony.ppogah.dto.CityDto;
import jp.co.sony.ppogah.entity.City;
import jp.co.sony.ppogah.entity.District;
import jp.co.sony.ppogah.exception.PgCrowdException;
import jp.co.sony.ppogah.repository.CityRepository;
import jp.co.sony.ppogah.repository.DistrictRepository;
import jp.co.sony.ppogah.service.ICityService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import jp.co.sony.ppogah.utils.SecondBeanUtils;
import jp.co.sony.ppogah.utils.SnowflakeUtils;
import jp.co.sony.ppogah.utils.StringUtils;
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
		final District district = this.districtRepository.findById(districtId).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_FATAL_ERROR);
		});
		final List<String> list = district.getCities().stream().map(City::getName).collect(Collectors.toList());
		if (list.contains(name)) {
			return ResultDto.failed(PgCrowd2Constants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}

	@Override
	public Pagination<CityDto> getCitiesByKeyword(final Integer pageNum, final String keyword) {
		final PageRequest pageRequest = PageRequest.of(pageNum - 1, PgCrowd2Constants.DEFAULT_PAGE_SIZE,
				Sort.by(Direction.ASC, "id"));
		final Specification<City> where1 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		final Specification<City> specification = Specification.where(where1);
		if (StringUtils.isEmpty(keyword)) {
			final Page<City> pages = this.cityRepository.findAll(specification, pageRequest);
			final List<CityDto> cityDtos = pages.stream().map(item -> {
				final CityDto cityDto = new CityDto();
				SecondBeanUtils.copyNullableProperties(item, cityDto);
				cityDto.setId(item.getId().toString());
				cityDto.setDistrictName(item.getDistrict().getName());
				return cityDto;
			}).collect(Collectors.toList());
			return Pagination.of(cityDtos, pages.getTotalElements(), pageNum, PgCrowd2Constants.DEFAULT_PAGE_SIZE);
		}
		final String searchStr = StringUtils.getDetailKeyword(keyword);
		final Specification<District> where2 = (root, query, criteriaBuilder) -> criteriaBuilder
				.equal(root.get("deleteFlg"), PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		final Specification<District> where3 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),
				searchStr);
		final Specification<District> specification2 = Specification.where(where2).and(where3);
		final List<Long> districtIds = this.districtRepository.findAll(specification2).stream().map(District::getId)
				.collect(Collectors.toList());
		final Specification<City> where4 = (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("name"),
				searchStr);
		final Specification<City> where5 = (root, query, criteriaBuilder) -> criteriaBuilder
				.like(root.get("pronunciation"), searchStr);
		final Specification<City> where6 = (root, query, criteriaBuilder) -> criteriaBuilder
				.and(root.get("districtId").in(districtIds));
		final Specification<City> specification3 = Specification.where(where1)
				.and(Specification.where(where4).or(where5).or(where6));
		final Page<City> pages = this.cityRepository.findAll(specification3, pageRequest);
		final List<CityDto> cityDtos = pages.stream().map(item -> {
			final CityDto cityDto = new CityDto();
			SecondBeanUtils.copyNullableProperties(item, cityDto);
			cityDto.setId(item.getId().toString());
			cityDto.setDistrictName(item.getDistrict().getName());
			return cityDto;
		}).collect(Collectors.toList());
		return Pagination.of(cityDtos, pages.getTotalElements(), pageNum, PgCrowd2Constants.DEFAULT_PAGE_SIZE);
	}

	@Override
	public ResultDto<String> remove(final Long id) {
		final City city = this.cityRepository.findById(id).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_FATAL_ERROR);
		});
		if (Objects.equals(id, city.getDistrict().getShutoId())) {
			return ResultDto.failed(PgCrowd2Constants.MESSAGE_STRING_FORBIDDEN3);
		}
		city.setDeleteFlg(PgCrowd2Constants.LOGIC_DELETE_FLG);
		this.cityRepository.saveAndFlush(city);
		return ResultDto.successWithoutData();
	}

	@Override
	public void save(final CityDto cityDto) {
		final City city = new City();
		SecondBeanUtils.copyNullableProperties(cityDto, city);
		city.setId(SnowflakeUtils.snowflakeId());
		city.setDeleteFlg(PgCrowd2Constants.LOGIC_DELETE_INITIAL);
		this.cityRepository.saveAndFlush(city);
	}

	@Override
	public ResultDto<String> update(final CityDto cityDto) {
		final City city = this.cityRepository.findById(Long.parseLong(cityDto.getId())).orElseThrow(() -> {
			throw new PgCrowdException(PgCrowd2Constants.MESSAGE_STRING_FATAL_ERROR);
		});
		final City originalEntity = new City();
		SecondBeanUtils.copyNullableProperties(city, originalEntity);
		SecondBeanUtils.copyNullableProperties(cityDto, city);
		if (originalEntity.equals(city)) {
			return ResultDto.failed(PgCrowd2Constants.MESSAGE_STRING_NOCHANGE);
		}
		try {
			this.cityRepository.saveAndFlush(city);
		} catch (final DataIntegrityViolationException e) {
			return ResultDto.failed(PgCrowd2Constants.MESSAGE_CITY_NAME_DUPLICATED);
		}
		return ResultDto.successWithoutData();
	}
}
