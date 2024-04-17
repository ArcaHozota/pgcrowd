<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
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
							<input type="hidden" value="${employeeInfo.id}" id="editId">
							<div class="form-group row">
								<label for="loginAccountEdit"
									class="col-sm-3 col-form-label text-end">ログインアカウント</label>
								<div class="col-sm-7" style="height: 60px;">
									<p class="form-control" id="loginAccountEdit">${employeeInfo.loginAccount}</p>
								</div>
							</div>
							<div class="form-group row">
								<label for="usernameEdit"
									class="col-sm-3 col-form-label text-end">ユーザ名</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="text" class="form-control" id="usernameEdit"
										placeholder="ユーザ名を入力してください" value="${employeeInfo.username}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="passwordEdit"
									class="col-sm-3 col-form-label text-end">パスワード</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="password" class="form-control" id="passwordEdit"
										placeholder="パスワードを入力してください" value="${employeeInfo.password}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="emailEdit" class="col-sm-3 col-form-label text-end">メールアドレス</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="email" class="form-control" id="emailEdit"
										placeholder="メールアドレスを入力してください" value="${employeeInfo.email}">
									<span class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="dateEdit" class="col-sm-3 col-form-label text-end">生年月日</label>
								<div class="col-sm-7" style="height: 60px;">
									<input type="date" class="form-control" id="dateEdit"
										value="${employeeInfo.dateOfBirth}"> <span
										class="form-text" style="font-size: 12px;"></span>
								</div>
							</div>
							<div class="form-group row">
								<label for="roleEdit" class="col-sm-3 col-form-label text-end">役割</label>
								<div class="col-sm-7" style="height: 45px;">
									<select id="roleEdit" class="form-select">
										<#list employeeRoles as employeeRole>
											<option value="${employeeRole.id}">${employeeRole.name}</option>
										</#list>
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