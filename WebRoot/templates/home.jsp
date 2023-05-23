<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
<jsp:include page="layout.jsp" />
<title>欣驰国际</title>
<script>
	
</script>
</head>
<body>

	<div class="main-body">


		<div class="main-container">
			<canvas id="userChart" width="1000" height="400"></canvas>
		</div>
	</div>

</body>
</html>