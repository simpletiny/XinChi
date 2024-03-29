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
<jsp:include page="../layout.jsp" />
<script src="<%=basePath%>static/vendor/kindeditor/kindeditor.js"></script>
<script src="<%=basePath%>static/js/culture/common-editor.js"></script>
</head>
<body>
	<div class="main-body">
		<div class="subtitle">
			<h2>
				新建产品制度<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">标题</label>
							<div class="ip">
								<input type="text" maxlength="20" class="ip-" data-bind="value: view().title" placeholder="标题" name="view.title" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">内容</label>
							<div class="ip">
								<!-- <textarea type="text" class="ip-default" rows="40" data-bind="value: view().content" name="view.content" placeholder="内容"></textarea> -->
								<textarea id="content" name="view.content" style="width:800px;height:600px;visibility:hidden;"></textarea>
							
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: publish">发布</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".rule").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/vendor/kindeditor/lang/zh_CN.js"></script>
	
	<script src="<%=basePath%>static/js/culture/product-rule-view-creation.js"></script>
</body>
</html>