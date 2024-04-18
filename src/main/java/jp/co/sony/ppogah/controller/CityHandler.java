package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.dto.CityDto;
import jp.co.sony.ppogah.dto.DistrictDto;
import jp.co.sony.ppogah.service.ICityService;
import jp.co.sony.ppogah.service.IDistrictService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 分類管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.10
 */
@Getter
@Setter
@Namespace(PgCrowd2URLConstants.URL_CITY_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/WEB-INF/city-pages.ftl"),
		@Result(name = ERROR, location = "/WEB-INF/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/WEB-INF/admin-login.ftl") })
@ParentPackage("json-default")
@Controller
public class CityHandler extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = -6017782752547971104L;

	/**
	 * 都市サービスインターフェス
	 */
	@Resource
	private ICityService iCityService;

	/**
	 * 地域サービスインターフェス
	 */
	@Resource
	private IDistrictService iDistrictService;

	/**
	 * リクエスト
	 */
	private transient HttpServletRequest request;

	/**
	 * JSONリスポンス
	 */
	private transient ResultDto<? extends Object> responsedJsondata;

	/**
	 * 情報転送クラス
	 */
	private final CityDto cityDto = new CityDto();

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 都道府県ID
	 */
	private Long districtId;

	/**
	 * 読み方
	 */
	private String pronunciation;

	/**
	 * 都道府県名称
	 */
	private String districtName;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 市町村旗
	 */
	private String cityFlag;

	/**
	 * 名称重複チェック
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_CHECK_NAME)
	public String checkDuplicated() {
		final String nameVal = this.getRequest().getParameter("nameVal");
		final String districtId2 = this.getRequest().getParameter("districtId");
		final ResultDto<String> checkDuplicated = this.iCityService.checkDuplicated(nameVal,
				Long.parseLong(districtId2));
		this.setResponsedJsondata(checkDuplicated);
		return NONE;
	}

	/**
	 * getter for cityDto
	 *
	 * @return CityDto
	 */
	public CityDto getCityDto() {
		this.cityDto.setId(this.getId());
		this.cityDto.setName(this.getName());
		this.cityDto.setPopulation(this.getPopulation());
		this.cityDto.setPronunciation(this.getPronunciation());
		this.cityDto.setDistrictId(this.getDistrictId());
		this.cityDto.setDistrictName(this.getDistrictName());
		this.cityDto.setCityFlag(this.getCityFlag());
		return this.cityDto;
	}

	/**
	 * 地域一覧を取得する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_DISTRICT_LIST)
	public String getDistricts() {
		final String cityId = this.getRequest().getParameter("cityId");
		final List<DistrictDto> districtsByCityId = this.iDistrictService.getDistrictsByCityId(cityId);
		this.setResponsedJsondata(ResultDto.successWithData(districtsByCityId));
		return NONE;
	}

	/**
	 * 情報一覧画面初期表示する
	 *
	 * @return String
	 */
	@Action(PgCrowd2URLConstants.URL_PAGINATION)
	public String pagination() {
		final String pageNum = this.getRequest().getParameter("pageNum");
		final String keyword = this.getRequest().getParameter("keyword");
		final Pagination<CityDto> cities = this.iCityService.getCitiesByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(cities));
		return NONE;
	}

	/**
	 * @see org.apache.struts2.interceptor.ServletRequestAware.setServletRequest();
	 */
	@Override
	public void setServletRequest(final HttpServletRequest request) {
		this.request = request;
	}
}
