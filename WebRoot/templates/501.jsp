<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head profile="http://gmpg.org/xfn/11">
<title>欣驰国际</title>
<style type="text/css" id="syntaxhighlighteranchor"></style>
</head>
<body>
	<jsp:include page="layout.jsp" />
	<div class="main-body">
		<h1>您暂时还不够级别</h1>
	</div>
</body>
</html>
