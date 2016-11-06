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
    <link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css"/>
    
    <script type="text/javascript" src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
    <script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
    <script src="<%=basePath%>static/js/users/login.js"></script>
    <script>
   	 window['basePath']="<%=basePath%>";
    </script>
</head>
<body>
<!-- head start -->
    <div class="main-header">
        <div class="header-min-width">
   			 <div class="user-status">
                <span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>">返回</a></i>
            </div>
        </div>
    </div>
<!-- head end -->

    <!-- login box start -->
    <s:form theme="simple" cssClass="register" id="register-form" method="post" action="register" namespace="/user" enctype="multipart/form-data" >
        <h4>用户注册</h4>
        <ul>
            <li><input type="text" id="login_name" maxlength="15" placeholder="用户名" onblur="check_name()" class="ip-default" name="ubb.login_name" required="required" /></li>
            <li><input type="password" onblur="confirm()" id="password1" maxlength="30" placeholder="密码" class="ip-default" name="ubb.password" required="required" /></li>
            <li><input type="password" onblur="confirm()" maxlength="30" id="password2" placeholder="再次输入密码" class="ip-default" name="password2" required="required" /></li>
            <li><input type="text" id="name" placeholder="姓名" maxlength="8" class="ip-default" name="ubb.user_name" required="required" /></li>
            <li><input type="text" id="id" placeholder="身份证号" maxlength="18" class="ip-default" name="ubb.id" required="required" /></li>
             <li><input type="text" id="nick_name" placeholder="昵称" maxlength="10" class="ip-default" name="uib.nick_name" required="required" /></li>
              <li><input type="text" id="cellphone" placeholder="电话" maxlength="11" class="ip-default" name="uib.cellphone" required="required" /></li>
            <li><select class="form-control" name="ubb.sex" > <option value ="F">女</option><option value ="M">男</option></select></li>
            <li><a href="javascript:;" class="a-upload">上传身份证复印件<input type="file" name="file"  required="required" id="upload" /></a></li>
            <li><div style="padding-top: 15px;"><button id="submit" type="submit" class="btn btn-green">提交</button></div></li>
        </ul>
    </s:form>
    <!-- login box end -->

</body>
</html>
