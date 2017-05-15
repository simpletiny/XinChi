<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="pragma" content="no-cache" />
<meta http-equiv="cache-control" content="no-cache" />
<meta http-equiv="expires" content="0" />
<meta name="renderer" content="webkit" />
<link href="<%=basePath%>static/img/favicon.ico" rel="icon" type="image/x-icon" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/bootstrap.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/hint.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/style.css" />
<link rel="stylesheet" href="<%=basePath%>static/vendor/font-awesome-4.2.0/css/font-awesome.min.css" />
<title>欣驰国际</title>
</head>
<body>
	<!-- head start -->
	<input type="hidden" id="hidden_apiurl" value="<%=basePath%>" />
	<div class="main-header">

		<div class="header-min-width">
			<div class="user-status">
				<font size="4" color="white">以客户为中心，以奋斗者为本，与优秀者为伍；创造价值，艰苦奋斗，共赢思维，团队精神；
					坚持自我批评，坚持自我反思。&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</font>
				<s:property value="#session.user.nick_name" />
				（
				<s:property value="#session.user.user_number" />
				）&nbsp;&nbsp;&nbsp;&nbsp; <i class="ic-user"><img src="<%=basePath%>static/img/mc-default-userphoto.png" width="36" height="36" alt="" /></i> <span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
					href="<%=basePath%>user/logout">退出</a><i class="fa fa-lg fa-sign-out"></i>
			</div>
		</div>
	</div>
	<!-- head end -->

	<!-- sidebar start -->
	<div class="main-sidebar">
		<ul class="menu-tree" style="padding-top: 30px;">
			<li class="culture"><a href="<%=basePath%>templates/culture/world-view.jsp"><i class="fa fa-users fa-lg fa-fw"></i>企业文化</a>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/world-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>欣驰世界观</a></li>
				</ol>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/value-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>欣驰价值观</a></li>
				</ol>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/rule-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>规章制度</a></li>
				</ol>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/academy-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>销售学院</a></li>
				</ol>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/product-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品学院</a></li>
				</ol>
				</li>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="client"><a href="<%=basePath%>templates/client/client-relation.jsp" onclick="$('.client').addClass('current').children('ol').css('display', 'block')"><i class="fa fa-users fa-lg fa-fw"></i>客户管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/client/client-relation.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>客户关系</a></li>
					</ol>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/client/client-employee.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>客户资料</a></li>
					</ol> 
						<ol style="display: none;">
							<li><a href="<%=basePath%>templates/client/company.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务主体</a></li>
						</ol>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
						<ol style="display: none;">
							<li><a href="<%=basePath%>templates/client/agency.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>旅游公司</a></li>
						</ol>
					</s:if></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="product-box"><a href="#" onclick="$('.product-box').addClass('current').children('ol').css('display', 'block')"><i class="fa fa-users fa-lg fa-fw"></i>产品架</a></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="order-box"><a href="#" onclick="$('.order-box').addClass('current').children('ol').css('display', 'block')"><i class="fa fa-users fa-lg fa-fw"></i>订单管理</a></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="order"><a href="<%=basePath%>templates/sale/order.jsp"><i class="fa fa-users fa-lg fa-fw"></i>预决算管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>预算单管理</a></li>
					</ol>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/final-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>决算单管理</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="sale"><a href="<%=basePath%>templates/sale/receivable.jsp"><i class="fa fa-users fa-lg fa-fw"></i>应收款管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/receivable.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>应收款</a></li>
						<li class="received"><a href="<%=basePath%>templates/sale/received.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>收入详表</a></li>
					</ol></li>
			</s:if>

			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
				<li class="product-manage"><a href="#" onclick="$('.product-manage').addClass('current').children('ol').css('display', 'block')"><i class="fa fa-users fa-lg fa-fw"></i>产品管理</a></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="supplier"><a href="#" onclick="$('.supplier').addClass('current').children('ol').css('display', 'block')"><i class="fa fa-users fa-lg fa-fw"></i>供应商管理</a> <s:if
						test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
						<ol style="display: none;">
							<li><a href="<%=basePath%>templates/supplier/supplier.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务主体</a></li>
						</ol>
					</s:if>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/supplier/supplier-employee.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>供应商员工</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
				<li class="product"><a href="<%=basePath%>templates/sale/payable.jsp"><i class="fa fa-users fa-lg fa-fw"></i>应付款管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/payable.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>应付款</a></li>
						<li><a href="<%=basePath%>templates/sale/paid.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>支出详表</a></li>
						<li><a href="<%=basePath%>templates/accounting/pay-approve.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>支出审批</a></li>
						<li><a href="<%=basePath%>templates/accounting/waiting-for-paid.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>待支付</a></li>
					</ol></li>
			</s:if>


			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('FINANCE')">
				<li class="finance"><a href="<%=basePath%>templates/finance/card.jsp"><i class="fa fa-users fa-lg fa-fw"></i>出纳</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/finance/card.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>账户管理</a></li>
					</ol>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/finance/detail.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>明细账</a></li>
					</ol></li>
			</s:if>

			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
				<li class="user"><a href="<%=basePath%>templates/users/user-approve.jsp"><i class="fa fa-users fa-lg fa-fw"></i>用户管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/users/user-approve.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>新用户审批</a></li>
						<li><a href="<%=basePath%>templates/users/user-group.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>用户组</a></li>
						<li><a href="<%=basePath%>templates/users/user-online.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>在线员工</a></li>
					</ol></li>
			</s:if>
			<li class="system"><a href="<%=basePath%>templates/system/system-guide.jsp"><i class="fa fa-users fa-lg fa-fw"></i>系统相关</a>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/system/system-guide.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>系统使用手册</a></li>
				</ol>
			</li>

		</ul>
	</div>
	<!-- sidebar end -->
	<script src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-formatCurrency.js"></script>
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
