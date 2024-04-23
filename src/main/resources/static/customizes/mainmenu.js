$(document).ready(function() {
	$("#toMainmenu").css('color', '#7F0020');
	let adminKanriImg = $("#adminKanriMainmenu").find('img');
	$(adminKanriImg).attr('src', require['/static/image/icons/castilia.svg']);
	let roleKanriImg = $("#roleKanriMainmenu").find('img');
	$(roleKanriImg).attr('src', require['/static/image/icons/burgundy.svg']);
	let categoryKanriImg = $("#categoryKanriMainmenu").find('img');
	$(categoryKanriImg).attr('src', require['/static/image/icons/bourbon.svg']);
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