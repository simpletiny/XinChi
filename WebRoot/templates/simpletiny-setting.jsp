<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
</head>
<body>

	<div class="main-body">
		<div class="subtitle">
			<form action="<%=basePath%>simpletiny/autoFixBalance">
				<input type="text" name="account_name"></input> <input type="submit" value="修正银行流水"></input>
			</form>
			<%-- 	<a href="<%=basePath%>simpletiny/autoUpdateClientUser">修复公开客户对应关系</a> --%>
			<%-- <a href="<%=basePath%>simpletiny/autoUpdateClientRelationConnect">修复客户关系交流信息</a> --%>
			<a href="<%=basePath%>simpletiny/autoUpdateProductReport">修正单团核算表</a>
			<%-- <a href="<%=basePath%>simpletiny/fixClientRelation" >修复FLY</a> --%>
			<%-- <form action="<%=basePath%>simpletiny/updateProductDetail">
				<input type="submit" value="修正产品信息"></input>
			</form> --%>
			<%--  <a href="<%=basePath%>simpletiny/changeAllPasswordToMD5" >test</a> --%>
			<%--      <a href="<%=basePath%>simpletiny/autoGenReceivable" >生成应收款表</a>
        <a href="<%=basePath%>simpletiny/autoGenPayable" >生成应付款表</a>
        <a href="<%=basePath%>simpletiny/autoGenPayable2th" >生成应付款表2</a>
        <a href="<%=basePath%>simpletiny/autoGenReceivable2th" >生成应收款表2</a> --%>

			<%--  <a href="<%=basePath%>simpletiny/autoGenTicketOrder" >生成机票订单</a>  --%>
		</div>

		<div class="main-container">
			<canvas id="userChart" width="1000" height="400"></canvas>
		</div>

		<form action="<%=basePath%>simpletiny/rebootTimer">
			<input type="number" name="reboot_min"></input> <input type="submit" value="重启倒计时"></input>
		</form>
	</div>
</body>
</html>