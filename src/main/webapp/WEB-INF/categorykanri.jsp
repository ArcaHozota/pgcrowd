<%@page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ja-JP">
<%@include file="/WEB-INF/include-header.jsp"%>
<body>
	<%@include file="/WEB-INF/include-navibar.jsp"%>
	<div class="container-fluid">
		<div class="row">
			<%@include file="/WEB-INF/include-sidebar.jsp"%>
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<div class="card border-periwinkle mb-3">
					<div class="card-header text-bg-periwinkle mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-bars-staggered"></i> 分類管理
						</h5>
					</div>
					<div class="card-body">
						<div id="categroyTreeView"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript"
		src="../static/customizes/categorykanri.js"></script>
</body>
</html>