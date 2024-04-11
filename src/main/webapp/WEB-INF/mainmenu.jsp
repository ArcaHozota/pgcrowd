<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja-JP">
<%@include file="/WEB-INF/include-header.jsp"%>
<body>
	<style type="text/css">
		#adminKanriMainmenu:hover h2 {
			color: #7f0020;
		}
		
		#roleKanriMainmenu:hover h2 {
			color: #002fa7;
		}
		
		#categoryKanriMainmenu:hover h2 {
			color: #006400;
		}
	</style>
	<%@include file="/WEB-INF/include-navibar.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/include-sidebar.jsp"%>
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<h1 class="page-header" style="color: #7F0020;">メインメニュー</h1>
				<div class="row">
					<div class="col" id="adminKanriMainmenu" role="button">
						<img src="/static/image/icons/castilia.svg"
							style="height: 80%; width: 100%;" class="rounded" alt="img01">
						<h2 class="text-center">社員管理</h2>
					</div>
					<div class="col" id="roleKanriMainmenu" role="button">
						<img src="/static/image/icons/burgundy.svg"
							style="height: 80%; width: 100%;" class="rounded" alt="img02">
						<h2 class="text-center">役割管理</h2>
					</div>
					<div class="col" id="categoryKanriMainmenu" role="button">
						<img src="/static/image/icons/bourbon.svg"
							style="height: 80%; width: 100%;" class="rounded" alt="img03">
						<h2 class="text-center">分類管理</h2>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript" src="/static/customizes/mainmenu.js"></script>
</body>
</html>