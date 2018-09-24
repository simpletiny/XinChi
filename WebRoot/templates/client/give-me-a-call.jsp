<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
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
		<input type="hidden" id="client_key" value="<%=key%>" />
		<div class="subtitle">
			<h2>
				新增电联<a  href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">客户</label>
							
							<div class="ip">
								<p class="ip-default"  data-bind="text: mobile().client_employee_name"></p>
								<input type="text" class="ip-" data-bind="value: mobile().client_employee_pk" style="display: none"
									name="mobile.client_employee_pk" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<div class="ip required" style="width: 30%">
								<label class="l">日期</label> <input type="text" style="width: 50%" class="ip-default date-picker" data-bind="value: mobile().date" placeholder="2013-10-19" name="mobile.date"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">电联目的</label>
							<div class="ip">
								<input type="text" class="ip-default" data-bind="value: mobile().touch_target" maxlength="10" placeholder="10个字以内" name="mobile.touch_target" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">电联效果</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="500" data-bind="value: mobile().summary" name="mobile.summary" placeholder="电联效果" required="required"></textarea>
							</div>
						</div>
					</div>
				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" id="btn_save" data-bind="click: saveMobileTouch">保存</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/give-me-a-call.js"></script>
</body>
</html>