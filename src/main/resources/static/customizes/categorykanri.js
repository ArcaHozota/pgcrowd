$(document).ready(function() {
	$("#toCategory").css('color', '#7F0020');
	let treeData = [
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
	$('#categroyTreeView').bstreeview({
		data: treeData,
		expandIcon: 'fa fa-angle-down fa-fw',
		collapseIcon: 'fa fa-angle-right fa-fw',
		indent: 2,
		parentsMarginLeft: '1.25rem',
		openNodeLinkOnNewTab: true
	});
	$("#districtQueryTree").on('click', function() {
		let url = '/pgcrowd/category/to/districtPages';
		checkPermissionAndTransfer(url);
	});
	$("#cityQueryTree").on('click', function() {
		let url = '/pgcrowd/category/to/cityPages';
		checkPermissionAndTransfer(url);
	});
});