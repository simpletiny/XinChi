<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>

</head>
<body>
	<div class="main-body">
		<input type="hidden" id="user_pk" value='<s:property value="#session.user.pk" />' />
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				个人中心<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create">返回</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<button style="float: right; margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:changePassword">修改密码</button>
				<div class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">昵称</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: user().nick_name"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">姓名</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: user().user_name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">性别</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: sexMapping[user().sex]"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: user().id"></p>
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div style="display: none;width:600px" id="password-old" >
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<label class="l">旧密码</label>
				<div class="ip">
				 <div class="ip"><input type="password" id="password_old" class="ip-default" placeholder="旧密码"/></div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<button style="float: right; margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:newPassword">下一步</button>
		</div>
	</div>
	<div style="display: none;width:600px" id="password-new" >
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<label class="l">新密码</label>
				<div class="ip">
				 <div class="ip"><input type="password" id="password_new" class="ip-default" placeholder="新密码"/></div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<label class="l">确认</label>
				<div class="ip">
				 <div class="ip"><input type="password" id="password_confirm" class="ip-default" placeholder="确认"/></div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<button style="float: right; margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:doChangePassword">完成</button>
		</div>
	</div>
	<script>
		$(".user").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/users/user-center.js"></script>
</body>
</html>