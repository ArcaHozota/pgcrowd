$(document).ready(function() {
	$("#toMainmenu").css('color', '#7F0020');
	require(['/static/image/icons/castilia.svg', '/static/image/icons/burgundy.svg', '/static/image/icons/bourbon.svg'], function(moduleA, moduleB, moduleC) {
		$("#adminKanriImg").attr('src', moduleA);
		$("#roleKanriImg").attr('src', moduleB);
		$("#categoryKanriImg").attr('src', moduleC);
	});
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