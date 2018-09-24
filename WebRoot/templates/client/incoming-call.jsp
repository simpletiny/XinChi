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
				被动咨询<a  href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">客户</label>
							
							<div class="ip">
								<p class="ip-default"  data-bind="text: incoming().client_employee_name"></p>
								<input type="text" class="ip-" data-bind="value: incoming().client_employee_pk" style="display: none"
									name="incoming.client_employee_pk" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<div class="ip required" style="width: 30%">
								<label class="l">日期</label> <input type="text" style="width: 50%" class="ip-default date-picker" data-bind="value: incoming().date" placeholder="2013-10-19" name="incoming.date"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">咨询方式</label>
							<div class="ip">
								<select style="width: 30%" class="form-control" data-bind="options: type" name="incoming.type" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">咨询效果</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="500" data-bind="value: incoming().summary" name="incoming.summary" placeholder="咨询效果" required="required"></textarea>
							</div>
						</div>
					</div>
				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" id="btn_save" data-bind="click: saveIncomingCall">保存</a>
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
	<script src="<%=basePath%>static/js/client/incoming-call.js"></script>
</body>
</html>