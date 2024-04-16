<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/static/css/style.css">
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<script type="text/javascript" src="/static/jquery/jquery-3.7.1.min.js"></script>
<script type="text/javascript" src="/static/layer/layer.js"></script>
</head>
<body>
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
		
		.input-box #eyeIcons {
			position: absolute;
			cursor: pointer;
			right: 30px;
			color: #fff;
			font-size: 1.2em;
			top: 20px;
		}
	</style>
	<div class="container">
		<div class="login-box">
			<!--Login Form-->
			<form action="/pgcrowd/employee/doLogin" method="post"
				class="form-signin" role="form" id="loginForm">
				<h2>ログイン</h2>
				<div class="input-box">
					<ion-icon name="mail-outline"></ion-icon>
					<#if registeredEmail?exists>
						<input type="text" value="${registeredEmail}" name="loginAcct"
							id="accountIpt" placeholder="アカウント" required>
					</#if>
					<input type="text" name="loginAcct" id="accountIpt" placeholder="アカウント" required>
				</div>
				<div class="input-box">
					<ion-icon name="lock-closed-outline"></ion-icon>
					<ion-icon name="eye-outline" id="eyeIcons"></ion-icon>
					<input type="password" name="userPswd" id="passwordIpt"
						placeholder="パスワード" required>
				</div>
				<div class="remember-forget">
					<label><input type="checkbox" name="remember-me">Remember
						Me</label> <a href="#">Forgot Password?</a>
				</div>
				<button type="button" id="loginBtn">ログイン</button>
				<div class="register-link">
					<p>
						アカウント無し？<a href="/pgcrowd/employee/toSignUp">登録しましょう！</a>
					</p>
				</div>
			</form>
		</div>
		<script type="module"
			src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"></script>
		<script nomodule
			src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
	</div>
	<script type="text/javascript" src="/static/customizes/logintoroku.js"></script>
</body>
</html>