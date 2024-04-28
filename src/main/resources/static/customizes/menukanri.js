$(document).ready(function() {
	$("#toMenu").css('color', '#005300');
	let treeData = [
		{
			text: "社員管理",
			icon: "fa-solid fa-user-group",
			expanded: true,
			nodes: [
				{
					id: "employeeAddTree",
					text: "社員情報追加",
					icon: "fa-solid fa-user-plus"
				},
				{
					id: "employeeQueryTree",
					text: "社員情報一覧",
					icon: "fa-solid fa-users"
				}
			]
		},
		{
			text: "役割管理",
			icon: "fa-solid fa-user-shield",
			expanded: true,
			nodes: [
				{
					id: "roleQueryTree",
					text: "役割情報一覧",
					icon: "fa-solid fa-id-card"
				}
			]
		},
		{
			text: "分類管理",
			icon: "fa-solid fa-list",
			expanded: true,
			nodes: [
				{
					id: "districtQueryTree",
					text: "地域一覧",
					icon: "fa-solid fa-earth-americas"
				},
				{
					id: "cityQueryTree",
					text: "都市一覧",
					icon: "fa-solid fa-tree-city"
				}
			]
		}
	];
	$('#treeView').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 2,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
	$("#employeeAddTree").on('click', function() {
		let url = '/pgcrowd/employee/toAddition';
		checkPermissionAndTransfer(url);
	});
	$("#employeeQueryTree").on('click', function() {
		let url = '/pgcrowd/employee/toPages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#roleQueryTree").on('click', function() {
		let url = '/pgcrowd/role/toPages?pageNum=1';
		checkPermissionAndTransfer(url);
	});
	$("#districtQueryTree").on('click', function() {
		let url = '/pgcrowd/category/toDistricts';
		checkPermissionAndTransfer(url);
	});
	$("#cityQueryTree").on('click', function() {
		let url = '/pgcrowd/category/toCities';
		checkPermissionAndTransfer(url);
	});
});