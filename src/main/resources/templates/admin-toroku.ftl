<!DOCTYPE html>
<html lang="ja-JP">
<head>
<title>PGアプリケーション</title>
<meta charset="UTF-8" />
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" href="/static/css/style.css">
<link rel="shortcut icon" type="image/x-icon" href="/static/favicon.ico">
<script type="text/javascript" src="/static/jquery/jquery-3.7.1.min.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
<script type="text/javascript" src="/static/layer/layer.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
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
		
		.container .login-box {
			height: 550px;
		}
		
		.input-box #eyeIcons2 {
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
			<#--Login Form-->
			<form action="/pgcrowd/employee/toroku" method="post"
				class="form-signin" role="form" id="torokuForm">
				<h2>登録</h2>
				<div class="input-box">
					<ion-icon name="mail-outline"></ion-icon>
					<input type="text" name="email" id="emailIpt" placeholder="メールアドレス"
						required>
				</div>
				<div class="input-box">
					<ion-icon name="key-outline"></ion-icon>
					<ion-icon name="eye-outline" id="eyeIcons2"></ion-icon>
					<input type="password" name="password" id="passwordIpt1"
						placeholder="パスワード" required>
				</div>
				<div class="input-box">
					<ion-icon name="key-outline"></ion-icon>
					<input type="password" name="password" id="passwordIpt2"
						placeholder="パスワード再入力" required>
				</div>
				<div class="input-box">
					<ion-icon name="calendar-number-outline"></ion-icon>
					<input type="date" name="dateOfBirth" id="dateOfBirthIpt" required>
				</div>
				<button type="button" id="torokuBtn">登録</button>
				<div class="register-link">
					<p>
						すでにアカウント持ち? <a href="/pgcrowd/employee/login">ログイン!</a>
					</p>
				</div>
			</form>
		</div>
		<script type="module" src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.esm.js"　
			nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
		<script nomodule src="https://unpkg.com/ionicons@7.1.0/dist/ionicons/ionicons.js"></script>
	</div>
	<script type="text/javascript" src="/static/customizes/logintoroku.js" nonce="Ytvk0lE3pg1BL713YR9i89Kn"></script>
</body>
</html>