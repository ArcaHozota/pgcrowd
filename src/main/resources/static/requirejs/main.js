require.config({
	paths: {
		'jquery': '/static/jquery/jquery-3.7.1.min.js',
		'bootstrap': '/static/bootstrap/js/bootstrap.bundle.min.js'
	},
	shim: {
		'jquery': { exports: 'jquery' },
		'bootstrap': {
			deps: ['jquery'], // 依赖 jQuery 或其他库
			exports: 'bootstrap' // 导出变量名
		}
	}
});
require(['jquery'], function() {
	require(['bootstrap'], function() {
		require(['/static/layer/layer.js'], function() {
			require(['/static/ztree/jquery.ztree.all.js', '/static/treeview/js/bstreeview.js'], function() {
				require(['/static/customizes/commons.js'], function() {
					let jsUrl = document.getElementById("jsContainer").getAttribute('value');
					require([jsUrl]);
				});
			});
		});
	});
});