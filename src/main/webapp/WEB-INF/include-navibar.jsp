<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<nav class="navbar navbar-expand-lg bg-body-tertiary fixed-top"
	data-bs-theme="dark">
	<div class="container-fluid">
		<a class="navbar-brand" style="font-size: 24px;"
			href="/pgcrowd/to/mainmenu">PGアプリケーション</a>
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
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle btn btn-success me-2" href="#"
					role="button" data-bs-toggle="dropdown" aria-expanded="false"
					style="height: 37.6px;"> <i class="fa-solid fa-user-circle"></i></a>
					<ul class="dropdown-menu dropdown-menu-end" role="menu">
						<li><a class="dropdown-item" href="#" id="toPersonal"> <i
								class="fa-solid fa-user-gear"></i> 個人設定
						</a></li>
						<li><a class="dropdown-item" href="#"> <i
								class="fa-solid fa-comments"></i> メッセージ
						</a></li>
						<li>
							<hr class="dropdown-divider">
						</li>
						<li><a id="logoutLink" class="dropdown-item" href="#"> <i
								class="fa-solid fa-right-from-bracket"></i> ログアウト
						</a></li>
						<form id="logoutForm" method="post"
							action="/pgcrowd/employee/logout" style="display: none;"></form>
					</ul></li>
			</ul>
			<div class="d-flex">
				<button id="logoutBtn" type="button" class="btn btn-danger me-2">
					<i class="fa-brands fa-ideal"></i>
				</button>
			</div>
		</div>
	</div>
</nav>