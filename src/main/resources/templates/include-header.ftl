<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8">
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="_csrf_header" content="${_csrf.headerName}">
<meta name="_csrf_token" content="${_csrf.token}">
<link rel="stylesheet" type="text/css" href="/static/bootstrap/css/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/static/css/main.css">
<link rel="stylesheet" type="text/css" href="/static/ztree/metroStyle.css">
<link rel="stylesheet" type="text/css" href="/static/bstreeview/css/bstreeview.min.css">
<link rel="stylesheet" type="text/css" href="/static/css/customize.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css">
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<style type="text/css">
	a {
		text-decoration: none;
	}
	
	.bg-dark {
		background-color: #000000 !important;
	}
	
	.effect-shine:hover {
		mask-image: linear-gradient(-75deg, rgba(0, 0, 0, 0.6) 30%, #000 50%, rgba(0, 0, 0, 0.6) 70%);
		mask-size: 200%;
		animation: shine 2500ms infinite;
	}

	@keyframes shine {
		from {
			-webkit-mask-position: 150%;
		}

		to {
			-webkit-mask-position: -50%;
		}
	}
</style>
<script type="text/javascript" src="https://cdn.jsdelivr.net/npm/sweetalert2@11" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
<script data-main="/static/requirejs/main" src="/static/requirejs/require.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn" strict-dynamic></script>
</head>
</html>