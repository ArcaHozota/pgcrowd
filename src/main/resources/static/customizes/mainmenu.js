$(document).ready(function() {
	$("#toMainmenu").css('color', '#7F0020');
	let height1 = $("#adminKanriMainmenu").find('svg').height();
	let height01 = height1 * 0.8;
	$("#adminKanriMainmenu").find('svg').attr('height', height01);
	let height2 = $("#roleKanriMainmenu").find('svg').height();
	let height02 = height2 * 0.8;
	$("#roleKanriMainmenu").find('svg').attr('height', height02);
	let height3 = $("#categoryKanriMainmenu").find('svg').height();
	let height03 = height3 * 0.8;
	$("#categoryKanriMainmenu").find('svg').attr('height', height03);
});
$("#categoryKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/category/initial';
	checkPermissionAndTransfer(url);
});
$("#roleKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/role/to/pages?pageNum=1';
	checkPermissionAndTransfer(url);
});
$("#adminKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/employee/to/pages?pageNum=1';
	checkPermissionAndTransfer(url);
});