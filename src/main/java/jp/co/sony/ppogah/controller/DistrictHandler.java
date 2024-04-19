package jp.co.sony.ppogah.controller;

import static com.opensymphony.xwork2.Action.ERROR;
import static com.opensymphony.xwork2.Action.LOGIN;
import static com.opensymphony.xwork2.Action.NONE;
import static com.opensymphony.xwork2.Action.SUCCESS;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.convention.annotation.Action;
import org.apache.struts2.convention.annotation.InterceptorRef;
import org.apache.struts2.convention.annotation.Namespace;
import org.apache.struts2.convention.annotation.ParentPackage;
import org.apache.struts2.convention.annotation.Result;
import org.apache.struts2.convention.annotation.Results;
import org.apache.struts2.interceptor.ServletRequestAware;
import org.springframework.stereotype.Controller;

import com.opensymphony.xwork2.ActionSupport;

import jp.co.sony.ppogah.common.PgCrowd2URLConstants;
import jp.co.sony.ppogah.dto.DistrictDto;
import jp.co.sony.ppogah.service.IDistrictService;
import jp.co.sony.ppogah.utils.Pagination;
import jp.co.sony.ppogah.utils.ResultDto;
import lombok.Getter;
import lombok.Setter;

/**
 * 地域管理ハンドラ
 *
 * @author ArkamaHozota
 * @since 1.10
 */
@Getter
@Setter
@Namespace(PgCrowd2URLConstants.URL_DISTRICT_NAMESPACE)
@Results({ @Result(name = SUCCESS, location = "/WEB-INF/city-pages.ftl"),
		@Result(name = ERROR, location = "/WEB-INF/system-error.ftl"),
		@Result(name = NONE, type = "json", params = { "root", "responsedJsondata" }),
		@Result(name = LOGIN, location = "/WEB-INF/admin-login.ftl") })
@ParentPackage("json-default")
@Controller
public class DistrictHandler extends ActionSupport implements ServletRequestAware {

	private static final long serialVersionUID = 646905610745129665L;

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
	private final DistrictDto districtDto = new DistrictDto();

	/**
	 * ID
	 */
	private String id;

	/**
	 * 名称
	 */
	private String name;

	/**
	 * 州都ID
	 */
	private Long shutoId;

	/**
	 * 州都名称
	 */
	private String shutoName;

	/**
	 * 地方名称
	 */
	private String chiho;

	/**
	 * 人口数量
	 */
	private Long population;

	/**
	 * 都道府県旗
	 */
	private String districtFlag;

	/**
	 * getter for districtDto
	 *
	 * @return DistrictDto
	 */
	private DistrictDto getDistrictDto() {
		this.districtDto.setId(this.getId());
		this.districtDto.setName(this.getName());
		this.districtDto.setChiho(this.getChiho());
		this.districtDto.setShutoId(this.getShutoId());
		this.districtDto.setShutoName(this.getShutoName());
		this.districtDto.setPopulation(this.getPopulation());
		this.districtDto.setDistrictFlag(this.getDistrictFlag());
		return this.districtDto;
	}

	/**
	 * 地域情報を更新する
	 *
	 * @return String
	 */
	@Action(value = PgCrowd2URLConstants.URL_INFO_UPDATE, interceptorRefs = { @InterceptorRef("json") })
	public String infoUpdate() {
		final DistrictDto districtDto2 = this.getDistrictDto();
		final ResultDto<String> update = this.iDistrictService.update(districtDto2);
		this.setResponsedJsondata(update);
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
		final Pagination<DistrictDto> districtsByKeyword = this.iDistrictService
				.getDistrictsByKeyword(Integer.parseInt(pageNum), keyword);
		this.setResponsedJsondata(ResultDto.successWithData(districtsByKeyword));
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