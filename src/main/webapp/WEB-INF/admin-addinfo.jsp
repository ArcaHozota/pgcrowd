<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ja-JP">
<%@include file="/WEB-INF/include-header.jsp"%>
<body>
	<%@include file="/WEB-INF/include-navibar.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/include-sidebar.jsp"%>
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<nav
					style="-bs-breadcrumb-divider: url(&amp; amp; amp; amp; amp; amp; amp; amp; amp; amp; amp; amp; amp; #34; data: image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='8' height='8'%3E%3Cpath d='M2.5 0L1 1.5 3.5 4 1 6.5 2.5 8l4-4-4-4z' fill='%236c757d'/%3E%3C/svg%3E&amp;amp;"
					aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#"
							style="text-decoration: none;" id="toMainmenu2">メインメニュー</a></li>
						<li class="breadcrumb-item"><a href="#"
							style="text-decoration: none;" id="toPages">データリスト</a></li>
						<li class="breadcrumb-item active" aria-current="page">データ追加</li>
					</ol>
				</nav>
				<div class="card border-darkgreen mb-3">
					<div class="card-header text-bg-darkgreen mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-grip"></i> 社員情報追加
						</h5>
					</div>
					<div class="card-body">
						<form role="form" id="inputForm">
							<div class="form-group row">
								<label for="loginAccountInput"
									class="col-sm-3 col-form-label text-end">ログインアカウント</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="text" class="form-control" id="loginAccountInput"
										placeholder="ログインアカウントを入力してください"> <span
										class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="usernameInput"
									class="col-sm-3 col-form-label text-end">ユーザ名</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="text" class="form-control" id="usernameInput"
										placeholder="ユーザ名を入力してください"> <span class="form-text"
										style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="passwordInput"
									class="col-sm-3 col-form-label text-end">パスワード</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="text" class="form-control" id="passwordInput"
										placeholder="パスワードを入力してください"> <span class="form-text"
										style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="emailInput" class="col-sm-3 col-form-label text-end">メールアドレス</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="email" class="form-control" id="emailInput"
										placeholder="メールアドレスを入力してください"> <span
										class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="dateInput" class="col-sm-3 col-form-label text-end">生年月日</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="date" class="form-control" id="dateInput">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="roleInput" class="col-sm-3 col-form-label text-end">役割</label>
								<div class="col-sm-7" style="height: 45px;">
									<select id="roleInput" class="form-select">
										<c:forEach items="${employeeRoles}" var="employeeRole">
											<option value="${employeeRole.id}">${employeeRole.name}</option>
										</c:forEach>
									</select> <span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
						</form>
					</div>
					<div class="card-footer">
						<button type="button" class="btn btn-success my-2 my-sm-0"
							id="saveInfoBtn">
							<i class="fa-solid fa-user-plus"></i> 追加
						</button>
						<button type="button" class="btn btn-secondary my-2 my-sm-0"
							id="resetBtn">
							<i class="fa-solid fa-arrow-rotate-right"></i> リセット
						</button>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="/static/customizes/employee-pages.js"></script>
</body>
</html>