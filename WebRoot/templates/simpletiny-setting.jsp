<%@ page language="java" pageEncoding="UTF-8"%>
 <%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>
<html xmlns="http://www.w3.org/1999/xhtml">

<head>
</head>
<body>

  <div class="main-body">
    <div class="subtitle">
        
       <a href="<%=basePath%>simpletiny/changeAllPasswordToMD5" >test</a>
    </div>

    <div class="main-container">
        <canvas id="userChart" width="1000" height="400"></canvas>
    </div>
  </div>
 </body>
</html>