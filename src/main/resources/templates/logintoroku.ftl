<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8" />
<meta http-equiv="X-UA-Compatible" content="IE=edge">
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/static/css/style.css">
<link href='https://unpkg.com/boxicons@2.1.4/css/boxicons.min.css' rel='stylesheet'>
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<style type="text/css">
	input[type=password]::-ms-reveal {
		display: none;
	}
	
	input[type=password]::-ms-clear {
		display: none;
	}
	
	input[type=password]::-o-clear {
		display: none;
	}
</style>
<script type="text/javascript" src="/static/jquery/jquery-3.7.1.min.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
<script type="text/javascript" src="/static/layer/layer.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
</head>
<body>
    <div class="container">
    	<#if torokuMsg?exists!''>
			<input type="hidden" value="${torokuMsg}" id="torokuMsgContainer">
		</#if>
        <div class="login-box">
        	<h2 class="login-title">
                <span>すでにアカウント持ち？</span>ログイン
            </h2>
            <div class="input-box" action="/pgcrowd/employee/doLogin" method="post" role="form" id="loginForm">
            	<#if registeredEmail?exists>
					<input type="text" value="${registeredEmail}" name="loginAcct"
						id="accountIpt" placeholder="アカウント">
					<#else>
					<input type="text" name="loginAcct" id="accountIpt" placeholder="アカウント">
				</#if>
                <input type="password" name="userPswd" id="passwordIpt" placeholder="パスワード">
            </div>
            <button type="button" id="loginBtn">ログイン</button>
        </div>
        <div class="toroku-box slide-up">
            <div class="center">
                <h2 class="toroku-title">
	                <span>アカウント無し？</span>登録
	            </h2>
	            <div class="input-box" action="/pgcrowd/employee/toroku" method="post" role="form" id="torokuForm">
	                <input type="text" placeholder="メール">
	                <input type="password" placeholder="パスワード">
	                <input type="password" placeholder="パス再入力">
	            </div>
	            <button type="button" id="torokuBtn">登録</button>
            </div>
        </div>
    </div>
    <script type="text/javascript" src="/static/customizes/logintoroku.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
</body>
</html>