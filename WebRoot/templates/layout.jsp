<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true" %>
 <%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta name="renderer" content="webkit"/>
	<link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/hint.min.css"/>
    <link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css"/>
    <link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css"/>
    <title>欣驰国际</title>
</head>
<body>
<!-- head start -->
    <input type="hidden" id="hidden_apiurl" value="<%=basePath%>"/>
    <div class="main-header">
        <div class="header-min-width">
            <div class="user-status">
            	<s:property value="#session.user.nick_name"/>（<s:property value="#session.user.user_number"/>）&nbsp;&nbsp;&nbsp;&nbsp;
                <i class="ic-user"><img src="<%=basePath%>static/img/mc-default-userphoto.png" width="36" height="36" alt=""/></i>
                <span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a href="<%=basePath%>user/logout">退出</a><i class="fa fa-lg fa-sign-out"></i>
            </div>
        </div>
    </div>
<!-- head end -->

<!-- sidebar start -->
    <div class="main-sidebar">
        <ul class="menu-tree" style="padding-top: 30px;">
        <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')"><li class="user"><a href="<%=basePath%>templates/users/user-approve.jsp"><i class="fa fa-users fa-lg fa-fw"></i>用户管理</a>
                <ol style="display: none;">
                    <li><a href="<%=basePath%>templates/users/user-approve.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>新用户审批</a></li>
                </ol>
            </li>
            </s:if>
            <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')"><li class="client"><a href="<%=basePath%>templates/client/company.jsp"><i class="fa fa-users fa-lg fa-fw"></i>客户管理</a>
                <ol style="display: none;">
                    <li><a href="<%=basePath%>templates/client/company.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务主体</a></li>
                </ol>
                 <ol style="display: none;">
                    <li><a href="<%=basePath%>templates/client/client-employee.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>客户员工</a></li>
                </ol>
            </li>
            </s:if>
             <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')"><li class="finance"><a href="<%=basePath%>templates/finance/card.jsp"><i class="fa fa-users fa-lg fa-fw"></i>财务</a>
                <ol style="display: none;">
                    <li><a href="<%=basePath%>templates/finance/card.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>账户管理</a></li>
                </ol>
                 <ol style="display: none;">
                    <li><a href="<%=basePath%>templates/finance/detail.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>明细账</a></li>
                </ol>
            </li>
            </s:if>
        </ul>
    </div>

<!-- sidebar end -->
    <script src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
    <script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
    <script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
    <script src="<%=basePath%>static/vendor/momentjs/moment-with-locales.min.js"></script>
    <script src="<%=basePath%>static/vendor/autocomplete/jquery.autocomplete.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
    <script type="text/javascript" src="<%=basePath%>static/vendor/nanobar.js"></script>

    <script src="<%=basePath%>static/js/utils.js"></script>
    <script src="<%=basePath%>static/js/validation.js"></script>
    <script src="<%=basePath%>static/js/datepicker.js"></script>
</body>
</html>
