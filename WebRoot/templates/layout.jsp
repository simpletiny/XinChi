
<%@ page language="java" pageEncoding="UTF-8"%>
<%@ page trimDirectiveWhitespaces="true"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
<style>
input::-webkit-outer-spin-button, input::-webkit-inner-spin-button {
	-webkit-appearance: none !important;
}

.fa-users1:before {
	content: url("<%=basePath%>/templates/favicon.png");
}

.floatPanel {
	position: fixed;
	top: 70%;
	right: 0;
	z-index: 9999999;
}

.timer {
	text-align: center;
	border: solid 1px;
	width: 100px;
	height: 80px;
	display: block;
	background: white;
}
</style>
</head>
<body>
	<!--  	<div class="floatPanel timer">
		<label>重启倒计时</label>
		<h3><b id="reboot-timer">05:00</b></h3>
	</div> 
	 -->
	<!-- head start -->
	<input type="hidden" id="hidden_apiurl" value="<%=basePath%>" />
	<div class="main-header">

		<div class="header-min-width" style="text-align: center">
			<%-- <img src="<%=basePath%>/static/img/head.jpg"></img>  --%>
			<div style="display: block;">
				<font size="4" color="white">让组团收客多快好省。&nbsp;&nbsp;&nbsp;以价值创造为核心：开放、透明、坦诚、共赢。&nbsp;&nbsp;&nbsp;做世界一流旅游运营商。&nbsp;&nbsp;&nbsp;以奋斗者为本，与优秀者为伍。</font>
			</div>
			<div class="user-status">

				<s:property value="#session.user.nick_name" />
				（
				<s:property value="#session.user.user_number" />
				）&nbsp;&nbsp;&nbsp;&nbsp; <i class="ic-user"
					onclick="javascript:window.location.href='<%=basePath%>templates/users/user-center.jsp'"><img
					src="<%=basePath%>static/img/mc-default-userphoto.png" width="36" height="36" alt="" /></i> <span></span>&nbsp;&nbsp;&nbsp;&nbsp;<a
					href="<%=basePath%>user/logout">退出</a><i class="fa fa-lg fa-sign-out"></i>
			</div>
		</div>
	</div>
	<!-- head end -->
	`
	<!-- sidebar start -->
	<div class="main-sidebar">
		<ul class="menu-tree" style="padding-top: 30px;">
			<li class="culture" onclick="click_menu(this)"><a href="<%=basePath%>templates/culture/world-view.jsp"><i
					class="fa fa-users1 fa-users1 fa-lg fa-fw"></i>企业文化</a>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/culture/history-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>欣驰历史</a></li>
					<li><a href="<%=basePath%>templates/culture/world-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>欣驰故事</a></li>
					<li><a href="<%=basePath%>templates/culture/value-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>欣驰价值观</a></li>
					<li><a href="<%=basePath%>templates/culture/team-evolution.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>团质进化</a></li>
					<li><a href="<%=basePath%>templates/culture/rule-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>通用制度</a></li>
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('SALES')">
						<li><a href="<%=basePath%>templates/culture/sale-rule-view.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>销售制度</a></li>
					</s:if>
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
						<li><a href="<%=basePath%>templates/culture/product-rule-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>产品制度</a></li>
						<li><a href="<%=basePath%>templates/culture/product-academy-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>产品研发</a></li>

					</s:if>
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('TICKET')">
						<li><a href="<%=basePath%>templates/culture/ticket-rule-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>票务制度</a></li>
					</s:if>
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('SALES')">
						<li><a href="<%=basePath%>templates/culture/sale-academy-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>销售学院</a></li>
					</s:if>
					<s:if
						test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')||#session.user.user_roles.contains('SALES')">
						<li><a href="<%=basePath%>templates/culture/product-research-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>产品学院</a></li>
					</s:if>
					<s:if test="#session.user.user_roles.contains('ADMIN')">
						<li><a href="<%=basePath%>templates/culture/top-academy-view.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>高层学院</a></li>
					</s:if>
					<s:else>
						<li><a href="<%=basePath%>templates/501.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>高层学院</a></li>
					</s:else>
				</ol></li>
			<s:if
				test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="client"><a href="<%=basePath%>templates/client/client-relation.jsp"
					onclick="$('.client').addClass('current').children('ol').css('display', 'block')"><i
						class="fa fa-users1 fa-lg fa-fw"></i>客户管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/client/client-relation.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>客户关系</a></li>
						<li><a href="<%=basePath%>templates/client/client-employee.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>客户资料</a></li>
						<li><a href="<%=basePath%>templates/client/company.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务主体</a></li>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<li><a href="<%=basePath%>templates/client/agency.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>旅游公司</a></li>
						</s:if>
						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<li><a href="<%=basePath%>templates/sale/sale-score.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>销售仪表盘</a></li>
						</s:if>
						<s:else>
							<li><a href="<%=basePath%>templates/404.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>销售仪表盘</a></li>
						</s:else>
						<li><a href="<%=basePath%>templates/accounting/reimbursement.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>费用详情</a></li>
					</ol></li>

			</s:if>
			<%-- 			<li class="product-box"><a href="<%=basePath%>templates/product/product-box.jsp"><i
					class="fa fa-users1 fa-lg fa-fw"></i>产品架</a>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/product/product-box.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品架</a></li>
				</ol></li> --%>
			<s:if
				test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="order-box"><a href="<%=basePath%>templates/product/product-box.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>订单管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/product/product-box.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品架</a></li>
						<li><a href="<%=basePath%>templates/order/tbc-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>待确认</a></li>
						<li><a href="<%=basePath%>templates/order/c-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>已确认</a></li>
						<li><a href="<%=basePath%>templates/order/f-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>已决算</a></li>
					</ol></li>
			</s:if>
			<%-- <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
				<li class="order"><a href="<%=basePath%>templates/sale/order.jsp"><i class="fa fa-users1 fa-lg fa-fw"></i>预决算管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>预算单管理</a></li>
						<li><a href="<%=basePath%>templates/sale/final-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>决算单管理</a></li>
					</ol></li>
			</s:if> --%>
			<s:if
				test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')||#session.user.user_roles.contains('SALES')">
				<li class="sale"><a href="<%=basePath%>templates/sale/receivable.jsp"><i class="fa fa-users1 fa-lg fa-fw"></i>应收款管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/receivable.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>应收款</a></li>
						<li class="received"><a href="<%=basePath%>templates/sale/received.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>收入详表</a></li>
					</ol></li>
			</s:if>

			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
				<li class="product-manager"><a href="<%=basePath%>templates/product/product.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>产品管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/product/product.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品管理</a></li>
						<li><a href="<%=basePath%>templates/product/product-analysis.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>产品分析</a></li>
						<li><a href="<%=basePath%>templates/product/product-upkeep.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品维护</a></li>
						<li><a href="<%=basePath%>templates/product/product-report.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品报表</a></li>
						<li><a href="<%=basePath%>templates/order/order-report.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>单团核算单</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
				<li class="order-operate"><a href="<%=basePath%>templates/product/product-need.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>产品操作</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/product/product-need.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品需求</a></li>
						<li><a href="<%=basePath%>templates/product/product-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品订单</a></li>
						<li><a href="<%=basePath%>templates/product/product-order-operating.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>操作中</a></li>
						<li><a href="<%=basePath%>templates/product/product-order-operated.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>已操作</a></li>
						<li><a href="<%=basePath%>templates/product/product-order-final.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>已决算</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
				<li class="supplier"><a href="<%=basePath%>templates/supplier/supplier.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>供应商管理</a>
			</s:if>
			<s:elseif test="#session.user.user_roles.contains('PRODUCT')">
				<li class="supplier"><a href="<%=basePath%>templates/supplier/supplier-employee.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>供应商管理</a>
			</s:elseif>
			<ol style="display: none;">
				<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
					<li><a href="<%=basePath%>templates/supplier/supplier.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务主体</a></li>
				</s:if>
				<li><a href="<%=basePath%>templates/supplier/supplier-employee.jsp"><i
						class="fa fa-angle-right fa-lg fa-fw"></i>供应商员工</a></li>
			</ol>
			</li>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('PRODUCT')">
				<li class="product"><a href="<%=basePath%>templates/sale/payable.jsp"><i class="fa fa-users1 fa-lg fa-fw"></i>地接款管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/sale/payable.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>地接往来</a></li>
						<li><a href="<%=basePath%>templates/sale/paid.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>往来详表</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('TICKET')">
				<li class="ticket"><a href="<%=basePath%>templates/ticket/ticket-need.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>票务管理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/ticket/ticket-need.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>需求列表</a></li>
						<li><a href="<%=basePath%>templates/ticket/ticket-order.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>待操作订单</a></li>
						<li><a href="<%=basePath%>templates/ticket/name-list.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>待出票名单</a></li>
						<li><a href="<%=basePath%>templates/ticket/name-list-done.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>已出票名单</a></li>
						<li><a href="<%=basePath%>templates/ticket/ticket-order-done.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>已操作订单</a></li>
						<li><a href="<%=basePath%>templates/ticket/payable.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>机票往来</a></li>
						<li><a href="<%=basePath%>templates/ticket/paid.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>往来详表</a></li>
						<li><a href="<%=basePath%>templates/ticket/supplier.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>供应商财务主体</a></li>
						<li><a href="<%=basePath%>templates/ticket/supplier-employee.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>供应商员工</a></li>
						<li><a href="<%=basePath%>templates/ticket/season-ticket.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>套票管理</a></li>
						<li><a href="<%=basePath%>templates/ticket/air-leg.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>票务航段</a></li>
						<%-- 						<li><a href="<%=basePath%>templates/ticket/flight-management.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>航班维护</a></li> --%>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('FINANCE')">
				<li class="accounting"><a href="<%=basePath%>templates/finance/received-match.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>会计</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/finance/received-match.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>收入匹配</a></li>
						<li><a href="<%=basePath%>templates/finance/inner-transfer.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>内转明细</a></li>
					</ol></li>
			</s:if>
			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('FINANCE')">
				<li class="finance"><a href="<%=basePath%>templates/accounting/waiting-for-paid.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>出纳</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/finance/detail.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>银行流水</a></li>
						<li><a href="<%=basePath%>templates/accounting/waiting-for-paid.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>待支付</a></li>
						<li><a href="<%=basePath%>templates/accounting/finished-paid.jsp"><i
								class="fa fa-angle-right fa-lg fa-fw"></i>已支付</a></li>
					</ol></li>
			</s:if>

			<s:if test="#session.user.user_roles.contains('ADMIN')">
				<li class="data"><a href="<%=basePath%>templates/data/order-data.jsp"><i class="fa fa-users1 fa-lg fa-fw"></i>数据分析</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/data/order-data.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>订单数据</a></li>
						<li><a href="<%=basePath%>templates/data/finance-summary.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>财务汇总</a></li>
					</ol></li>
			</s:if>

			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
				<li class="user"><a href="<%=basePath%>templates/users/user-approve.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>用户管理</a>
			</s:if>
			<s:else>
				<li class="user"><a href="<%=basePath%>templates/users/user-center.jsp"><i class="fa fa-users1 fa-lg fa-fw"></i>用户管理</a>
			</s:else>
			<ol style="display: none;">
				<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
					<li><a href="<%=basePath%>templates/users/user-approve.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>新用户审批</a></li>
					<li><a href="<%=basePath%>templates/users/users.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>用户管理</a></li>
					<li><a href="<%=basePath%>templates/users/user-group.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>用户组</a></li>
					<li><a href="<%=basePath%>templates/users/user-online.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>在线员工</a></li>
				</s:if>
				<li><a href="<%=basePath%>templates/users/user-center.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>个人中心</a></li>
			</ol>
			</li>


			<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
				<li class="manager"><a href="<%=basePath%>templates/sale/sale-work-report.jsp"><i
						class="fa fa-users1 fa-lg fa-fw"></i>经理</a>
					<ol style="display: none;">
						<li><a href="<%=basePath%>templates/finance/card.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>账户管理</a></li>
						<li><a href="<%=basePath%>templates/accounting/pay-approve.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>支出审批</a></li>
						<li><a href="<%=basePath%>templates/users/user-log.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>用户操作日志</a></li>
						<li><a href="<%=basePath%>templates/product/product-group.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>产品小组</a></li>
						<li><a href="<%=basePath%>templates/sale/sale-work-report.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>销售工作报表</a></li>

					</ol></li>
			</s:if>
			<li class="system"><a href="<%=basePath%>templates/system/system-guide.jsp"><i
					class="fa fa-users1 fa-lg fa-fw"></i>系统相关</a>
				<ol style="display: none;">
					<li><a href="<%=basePath%>templates/system/system-guide.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>系统使用手册</a></li>
					<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
						<li><a href="<%=basePath%>templates/system/base-data.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>基础数据</a></li>
					</s:if>
					<li><a href="<%=basePath%>templates/404.jsp"><i class="fa fa-angle-right fa-lg fa-fw"></i>角色权限管理</a></li>
				</ol></li>

		</ul>
	</div>
	<!-- sidebar end -->
	<script src="<%=basePath%>static/vendor/jquery-1.11.1.min.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-formatCurrency.js"></script>
	<script src="<%=basePath%>static/vendor/knockout-3.2.0.js"></script>
	<script src="<%=basePath%>static/vendor/layer-v1.8.5/layer/layer.min.js"></script>
	<%-- <script src="<%=basePath%>static/vendor/layer-v3.1.1/layer.js"></script> --%>
	<script src="<%=basePath%>static/vendor/momentjs/moment-with-locales.min.js"></script>
	<script src="<%=basePath%>static/vendor/autocomplete/jquery.autocomplete.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/nanobar.js"></script>

	<script src="<%=basePath%>static/js/utils.js"></script>
	<script src="<%=basePath%>static/js/layout.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
</body>
</html>
