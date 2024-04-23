require(['/static/jquery/jquery-3.7.1.min.js'], function() {
	require(['/static/bootstrap/js/bootstrap.bundle.min.js'], function() {
		require(['/static/layer/layer.js', '/static/ztree/jquery.ztree.all.js', '/static/treeview/js/bstreeview.js',
			'/static/customizes/commons.js'], function() {
				let jsUrl = document.getElementById("jsContainer").getAttribute('value');
				require([jsUrl]);
			});
	});
});