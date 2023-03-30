<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css" />
<link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css" />


<style type="text/css">
.delete {
	position: absolute;
	width: 100px;
	height: 50px;
	text-decoration: none;
	background: transparent;
	font-size: 16px;
	font-family: 微软雅黑, 宋体, Arial, Helvetica, Verdana, sans-serif;
	font-weight: bold;
	border: solid 2px black;
	-webkit-transition: all linear 0.30s;
	-moz-transition: all linear 0.30s;
	transition: all linear 0.30s;
	line-height: 50px;
	text-align: center;
	z-index: 999;
}

.delete:hover {
	background: #2f435e;
	color: #f2f2f2;
	opacity: 1;
	border: dashed 2px white;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="main-body">
		<!-- head start -->
		<input type="hidden" id="hidden_apiurl" value="<%=basePath%>" />
		<div class="main-header">
			<div class="header-min-width" style="text-align: center">
				<h2>新用户注册</h2>
			</div>
		</div>
		<!-- head end -->

		<!-- login box start -->
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">登录账号</label>
							<div class="ip">
								<input type="text" id="login_name" maxlength="15" autocomplete="off" placeholder="5-15位" class="ip-default"
									name="ubb.login_name" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">密码</label>
							<div class="ip">
								<input type="password" id="password1" maxlength="30" placeholder="至少6位!" class="ip-default" name="ubb.password"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">密码确认</label>
							<div class="ip">
								<input type="password" maxlength="30" id="password2" placeholder="再次输入密码" class="ip-default"
									name="ubb.password2" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">姓名</label>
							<div class="ip">
								<input type="text" id="name" placeholder="姓名" autocomplete="off" maxlength="8" class="ip-default"
									name="ubb.user_name" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" id="id" placeholder="身份证号" autocomplete="off" maxlength="18" class="ip-default" name="ubb.id"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">昵称</label>
							<div class="ip">
								<input type="text" id="nick_name" placeholder="昵称" autocomplete="off" maxlength="10" class="ip-default"
									name="uib.nick_name" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">电话</label>
							<div class="ip">
								<input type="text" id="cellphone" placeholder="电话" autocomplete="off" maxlength="11" class="ip-default"
									name="uib.cellphone" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" name="ubb.sex">
									<option value="F">女</option>
									<option value="M">男</option>
								</select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传身份证证件照<input type="file" name="file" required="required"
								id="upload" /></a> <input type="hidden" accept=".jpg,.png" name="uib.id_file_name" id="file-name" />
						</div>
						<div class="col-md-6" id="progress"></div>
					</div>
					<div class="input-row clearfloat" id="img-container"></div>
					<div class="input-row clearfloat" style="float: right">
						<div style="padding-top: 15px;">
							<button type="submit" data-bind="click: function() { register() }" class="btn btn-green">提交</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<!-- login box end -->
	<script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/utils.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/file-upload2.js"></script>
	<script src="<%=basePath%>static/js/users/register.js"></script>
</body>
</html>
