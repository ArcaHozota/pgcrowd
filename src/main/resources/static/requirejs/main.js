require(['/static/jquery/jquery-3.7.1.min.js'], function() {
	require(['/static/bootstrap/js/bootstrap.min.js'], function() {
		require(['/static/layer/layer.js', '/static/ztree/jquery.ztree.all.js', '/static/treeview/js/bstreeview.js'], function() {
			require(['/static/customizes/commons.js'], function() {
				let jsUrl = document.getElementById("jsContainer").getAttribute('value');
				require([jsUrl]);
			});
		});
	});
});