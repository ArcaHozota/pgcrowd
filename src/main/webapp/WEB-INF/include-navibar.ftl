<!DOCTYPE html>
<html lang="ja-JP">
<body>
	<#if Session.SPRING_SECURITY_CONTEXT?exists>    
	    <#assign principalAdmin = Session.SPRING_SECURITY_CONTEXT.authentication.principal
	        userAdminName = principalAdmin.getOriginalAdmin().getUsername()
	        personalId = principalAdmin.getOriginalAdmin().getId()>
	<#else>
	    <#assign userAdminName="unknown" personalId="0L">
	</#if>
	<nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top"
		data-bs-theme="dark">
		<div class="container-fluid">
			<a class="navbar-brand" style="font-size: 24px;"
				href="/pgcrowd/employee/toMainmenu">PGアプリケーション</a>
			<div class="collapse navbar-collapse">
				<div class="input-group d-flex justify-content-end">
					<input type="text" class="form-control" id="searchInput"
						placeholder="検索" style="max-width: 180px;">
					<button class="input-group-text bg-transparent p-0 px-2"
						id="searchBtn">
						<i class="fa-solid fa-magnifying-glass"></i>
					</button>
				</div>
				<ul class="navbar-nav mb-2 mb-lg-0 d-flex" id="dropdown-info">
					<li class="nav-item dropdown">
						<a class="nav-link dropdown-toggle btn btn-success me-2" href="#" role="button" 
							data-bs-toggle="dropdown" aria-expanded="false" style="height: 37.6px;"> 
							<i class="fa-solid fa-user-circle">${userAdminName}</i>
						</a>
						<ul class="dropdown-menu dropdown-menu-end" role="menu">
							<li>
								<a class="dropdown-item" href="#" id="toPersonal"> 
									<i class="fa-solid fa-user-gear"></i> 個人設定
									<input type="hidden" value="${personalId}">
								</a>
							</li>
							<li>
								<a class="dropdown-item" href="#"> 
									<i class="fa-solid fa-comments"></i> メッセージ
								</a>
							</li>
							<li>
								<hr class="dropdown-divider">
							</li>
							<li>
								<a id="logoutLink" class="dropdown-item" href="#"> 
									<i class="fa-solid fa-right-from-bracket"></i> ログアウト
								</a>
							</li>
							<form id="logoutForm" method="post" action="/pgcrowd/employee/logout" 
								style="display: none;"></form>
							<#assign authNames = statics["jp.co.sony.ppogah.utils.CommonProjectUtils"].getAuthNames(principalAdmin.authorities.stream())>
							<#if authNames.contains('employee%edition') || authNames.contains('employee%delete')>
								<input type="hidden" value="true" id="authChkFlgContainer">
								<#else>
								<input type="hidden" value="false" id="authChkFlgContainer">
							</#if>
						</ul>
					</li>
				</ul>
				<div class="d-flex">
					<button id="logoutBtn" type="button" class="btn btn-danger me-2">
						<i class="fa-brands fa-ideal"></i>
					</button>
				</div>
			</div>
		</div>
	</nav>
</body>
</html>