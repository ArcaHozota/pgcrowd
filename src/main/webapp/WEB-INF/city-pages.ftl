<!DOCTYPE html>
<html lang="ja-JP">
<#include "include-header.ftl">
<body>
	<#include "include-navibar.ftl">
	<div class="container-fluid">
		<div class="row">
			<#include "include-sidebar.ftl">
			<div class="col-sm-9 offset-sm-3 col-md-10 offset-md-2 main">
				<div class="card border-klein mb-3">
					<div class="card-header text-bg-klein mb-3">
						<h5 class="card-title" style="padding-top: 8px;">
							<i class="fa-solid fa-bars-staggered"></i> 都市情報メンテナンス
						</h5>
					</div>
					<div class="card-body">
						<div class="row">
							<form class="form-inline col-md-5" role="form">
								<div class="input-group col-md-5">
									<input id="keywordInput" class="form-control" type="text" placeholder="検索条件を入力してください">
									<button id="searchBtn2" class="btn btn-secondary my-2 my-sm-0" type="button">
										<i class="fa-solid fa-magnifying-glass"></i> 検索
									</button>
								</div>
							</form>
							<div class="col-md-2 offset-md-5">
								<button class="btn btn-primary my-2 my-sm-0" id="addCityBtn">
									<i class="fa-solid fa-city"></i> 都市情報追加
								</button>
							</div>
						</div>
						<table class="table table-sm table-hover">
							<caption style="font-size: 10px;">都市情報一覧</caption>
							<thead>
								<tr class="table-primary">
									<th scope="col" class="text-center" style="width: 150px;">ID</th>
									<th scope="col" class="text-center" style="width: 70px;">名称</th>
									<th scope="col" class="text-center" style="width: 100px;">読み方</th>
									<th scope="col" class="text-center" style="width: 70px;">都道府県</th>
									<th scope="col" class="text-center" style="width: 50px;">人口数量</th>
									<th scope="col" class="text-center" style="width: 50px;">市町村旗</th>
									<th scope="col" class="text-center" style="width: 100px;">操作</th>
								</tr>
							</thead>
							<tbody id="tableBody" class="table-group-divider"></tbody>
						</table>
						<div class="row">
							<div id="pageInfos" class="col-md-5" style="font-size: 12px;"></div>
							<div id="pageNavi" class="col-md-7 d-flex justify-content-end"></div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="cityAddModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">都市情報追加</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group row">
							<label for="nameInput" class="col-sm-3 col-form-label text-end">都市名</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="nameInput" placeholder="都市の名称"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
						<div class="form-group row">
							<label for="poInput" class="col-sm-3 col-form-label text-end">読み方</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="poInput" placeholder="都市名の読み方"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
						<div class="form-group row">
							<label for="districtInput" class="col-sm-3 col-form-label text-end">都道府県</label>
							<div class="col-sm-9" style="height: 60px;">
								<select id="districtInput" class="form-select"></select>
							</div>
						</div>
						<div class="form-group row">
							<label for="populationInput"
								class="col-sm-3 col-form-label text-end">人口数量</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="populationInput" placeholder="都市の人口数量"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
						<div class="form-group row">
							<label for="cityFlagInput"
								class="col-sm-3 col-form-label text-end">市町村旗</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="cityFlagInput" placeholder="市町村旗"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
						<i class="fa-solid fa-xmark"></i> 閉じる
					</button>
					<button type="button" class="btn btn-primary" id="cityInfoSaveBtn">
						<i class="fa-solid fa-inbox"></i> 保存
					</button>
				</div>
			</div>
		</div>
	</div>
	<div class="modal fade" id="cityEditModal" tabindex="-1" role="dialog"
		aria-labelledby="myModalLabel">
		<div class="modal-dialog" role="document">
			<div class="modal-content">
				<div class="modal-header">
					<h4 class="modal-title">都市情報変更</h4>
				</div>
				<div class="modal-body">
					<form class="form-horizontal">
						<div class="form-group row">
							<label for="nameEdit" class="col-sm-3 col-form-label text-end">都市名</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="nameEdit" placeholder="都市の名称"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
						<div class="form-group row">
							<label for="poEdit" class="col-sm-3 col-form-label text-end">読み方</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="poEdit" placeholder="都市名の読み方"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
						<div class="form-group row">
							<label for="districtEdit" class="col-sm-3 col-form-label text-end">都道府県</label>
							<div class="col-sm-9" style="height: 60px;">
								<select id="districtEdit" class="form-select"></select>
							</div>
						</div>
						<div class="form-group row">
							<label for="populationEdit" class="col-sm-3 col-form-label text-end">人口数量</label>
							<div class="col-sm-9" style="height: 60px;">
								<input type="text" class="form-control" id="populationEdit" placeholder="都市の人口数量"> 
								<span class="form-text" style="font-size: 12px;"></span>
							</div>
						</div>
					</form>
				</div>
				<div class="modal-footer">
					<button type="button" class="btn btn-secondary" data-bs-dismiss="modal">
						<i class="fa-solid fa-xmark"></i> 閉じる
					</button>
					<button type="button" class="btn btn-success" id="cityInfoChangeBtn">
						<i class="fa-solid fa-leaf"></i> 更新
					</button>
				</div>
			</div>
		</div>
	</div>
	<input type="hidden" value="/static/customizes/city-pages.js" id="jsContainer">
</body>
</html>