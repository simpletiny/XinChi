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

</head>
<body>
	<jsp:include page="layout.jsp" />
	<div id="main" style="width: 600px; height: 400px;"></div>
	<script src="<%=basePath%>static/vendor/echart/echarts.min.js"></script>
	<script src="<%=basePath%>static/js/data/order-data.js"></script>
</body>
</html>
