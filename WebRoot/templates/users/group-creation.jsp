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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				新建用户组<a href="<%=basePath%>/templates/users/user-group.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">小组名</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: group().group_name" placeholder="小组名" name="group.group_name" required="required" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">组长</label>
							<div class="ip">
								<select class="form-control" style="height: 34px" data-bind="value:group().group_leader,options: users_name, optionsCaption: '--请选择--'" name="group.group_leader"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">上级领导</label>
							<div class="ip">
								<select class="form-control" style="height: 34px" data-bind="value:group().higher_ups,options: users_name, optionsCaption: '--请选择--'" name="group.higher_ups"></select>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">主要职责</label>
							<div class="ip">
								<input type="text" class="ip-default" data-bind="value: group().group_duty" placeholder="主要职责" name="group.group_duty" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">包含员工</label>
							<div class="ip">
								<div data-bind="foreach: users">
									<em class="small-box"> <input type="checkbox" name="choosenUsers" required="required" data-bind="attr: {'value': $data.pk}, checked: $root.chosenUsers" /><label
										data-bind="text: $data.user_name"></label>
									</em>
								</div>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: group().comment" name="group.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createGroup">保存</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".user").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/users/group-creation.js"></script>
</body>
</html>