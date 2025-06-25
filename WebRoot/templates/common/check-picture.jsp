<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<script type="text/javascript">
	function checkBig(img) {
		window.open($("#hidden_apiurl").val() + "templates/common/check-picture-big.jsp?src="
				+ encodeURIComponent($(img).attr("src")));
	}
</script>
<div id="div-pic">
	<img src="" id="img-pic" alt="图片" onclick="checkBig(this)" style="width: 590px; height: 600px; cursor: pointer" />
</div>