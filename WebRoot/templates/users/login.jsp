<%@ page language="java" pageEncoding="UTF-8"%>
 <%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>欣驰国际</title>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <script type="text/javascript" src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>static/vendor/jquery-cookie/jquery.cookie.js"></script>
    <script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
    <script src="<%=basePath%>static/js/users/login.js"></script>
</head>
<body>
<s:hidden id="login_result" value="%{#request.login_result}"></s:hidden>
<!-- head start -->
    <div class="main-header">
        <div class="header-min-width">
   
        </div>
    </div>
<!-- head end -->


    <!-- login box start -->
    <s:form cssClass="login" theme="simple" id="login-form" action="login" namespace="/user">
        <h4>用户登录</h4>
        <ul>
            <li><input type="text" id="username" placeholder="用户名" class="ip-default" name="ubb.login_name" required="required" /></li>
            <li><input type="password" id="password" placeholder="密码" class="ip-default" name="ubb.password" required="required" /></li>
            <li><input type="checkbox" id="auto-login"/><label>自动登录</label></li>
            <li><div style="padding-top: 15px;"><button type="submit" class="btn btn-green">登录</button></div></li>
            <li><div style="padding-top: 5px;"><a style="cursor: pointer" href="<%=basePath%>templates/users/register.jsp">注册</a></div></li>
        </ul>
    </s:form>
    <!-- login box end -->

</body>
</html>
