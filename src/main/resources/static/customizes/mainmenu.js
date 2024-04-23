$(document).ready(function() {
	$("#toMainmenu").css('color', '#7F0020');
	$("#adminKanriMainmenu").find('img').attr('src', require['/static/image/icons/castilia.svg']);
	$("#roleKanriMainmenu").find('img').attr('src', require['/static/image/icons/burgundy.svg']);
	$("#categoryKanriMainmenu").find('img').attr('src', require['/static/image/icons/bourbon.svg']);
});
$("#categoryKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/category/initial';
	checkPermissionAndTransfer(url);
});
$("#roleKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/role/toPages?pageNum=1';
	checkPermissionAndTransfer(url);
});
$("#adminKanriMainmenu").on('click', function() {
	let url = '/pgcrowd/employee/toPages?pageNum=1';
	checkPermissionAndTransfer(url);
});