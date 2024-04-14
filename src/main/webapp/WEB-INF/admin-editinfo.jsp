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
				<nav style="-bs-breadcrumb-divider: '&gt;';" aria-label="breadcrumb">
					<ol class="breadcrumb">
						<li class="breadcrumb-item"><a href="#"
							style="text-decoration: none;" id="toMainmenu2">メインメニュー</a></li>
						<li class="breadcrumb-item"><a href="#"
							style="text-decoration: none;" id="toPages">データリスト</a></li>
						<li class="breadcrumb-item active" aria-current="page">データ更新</li>
					</ol>
				</nav>
				<div class="card border-klein mb-3">
					<div class="card-header text-bg-klein mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-grip"></i> 社員情報変更
						</h5>
					</div>
					<div class="card-body">
						<form role="form" id="editForm">
							<c:set var="arawaseta" value="${employeeInfo}" />
							<input type="hidden" name="${arawaseta.id}" id="editId">
							<div class="form-group row">
								<label for="loginAccountEdit"
									class="col-sm-3 col-form-label text-end">ログインアカウント</label>
								<div class="col-sm-7" style="height: 60px;">
									<p class="form-control" id="loginAccountEdit">${arawaseta.loginAccount}</p>
								</div>
							</div>
							<div class="form-group row">
								<label for="usernameEdit"
									class="col-sm-3 col-form-label text-end">ユーザ名</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="text" class="form-control" id="usernameEdit"
										placeholder="ユーザ名を入力してください" value="${arawaseta.username}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="passwordEdit"
									class="col-sm-3 col-form-label text-end">パスワード</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="password" class="form-control" id="passwordEdit"
										placeholder="パスワードを入力してください" value="${arawaseta.password}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="emailEdit" class="col-sm-3 col-form-label text-end">メールアドレス</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="email" class="form-control" id="emailEdit"
										placeholder="メールアドレスを入力してください" value="${arawaseta.email}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="dateEdit" class="col-sm-3 col-form-label text-end">生年月日</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="date" class="form-control" id="dateEdit"
										value="${arawaseta.dateOfBirth}"> <span
										class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="roleEdit" class="col-sm-3 col-form-label text-end">役割</label>
								<div class="col-sm-7" style="height: 45px;">
									<select id="roleEdit" class="form-select">
										<c:forEach items="${employeeRoles}" var="employeeRole">
											<option value="${employeeRole.id}">${employeeRole.name}</option>
										</c:forEach>
									</select> <span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
						</form>
					</div>
					<div class="card-footer">
						<button type="button" class="btn btn-primary my-2 my-sm-0"
							id="editInfoBtn">
							<i class="fa-solid fa-hourglass-half"></i> 変更
						</button>
						<button type="button" class="btn btn-secondary my-2 my-sm-0"
							id="restoreBtn">
							<i class="fa-solid fa-trash-can"></i> 廃棄
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