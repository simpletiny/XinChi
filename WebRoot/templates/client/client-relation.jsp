<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.fixed {
	font-size: 12px;
	display: block;
	position: fixed;
	right: 0px;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>客户关系管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
					
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createVisit() }">新增拜访</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createAccurateSale() }">新增精推</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { setClientLevel() }">客户评级</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { reimbursement() }">费用报销</button>
					</div>
		
					<div class="form-group">
						<div class="span6">
								<label class="col-md-1 control-label">客户</label>
								<div class="col-md-2">
									 <input type="text" class="form-control" placeholder="客户" maxlength="20" name="relation.client_employee_name" />
								</div>
							</div>
								<div class="span6">
								<label class="col-md-1 control-label">评级</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" data-bind="options: level, optionsCaption: '全部',value:chosenLevel,event:{change:fetchSummary}"
										name="relation.level"></select>
								</div>
							</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales" data-bind="options: sales_name, optionsCaption: '全部',value:chosenSales,event:{change:fetchSummary}"
										name="relation.sales_name"></select>
								</div>
							</div>
						</s:if>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
					</div>
				</form>
				
				
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<td width="11.11%">客户总数</td>
								<td width="11.11%" data-bind="text:clientEmployeeCount()"></td>
								<td width="11.11%">本月订单</td>
								<td width="11.11%"  data-bind="text:monthOrderCount()"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
							</tr>
					</thead>
					</table>
					<table class="table table-striped table-hover">
					<thead>
							<tr>
								<th colspan="2" style="border-right:solid 1px #ff0000;"></th>
								<th colspan="4" style="border-right:solid 1px #ff0000;">月度数据</th>
								<th colspan="4">周数据</th>
							</tr>
							<tr>
								<th>评级</th>
								<th style="border-right:solid 1px #ff0000;">客户总数</th>
								<th>升级</th>
								<th>订单</th>
								<th>拜访</th>
								<th style="border-right:solid 1px #ff0000;">精推</th>
								<th>升级</th>
								<th>订单</th>
								<th>拜访</th>
								<th>精推</th>
							</tr>
						</thead>
							<tbody id="tbody-data" data-bind="foreach: clientSummary">
							<tr>
								<td data-bind="text: $data.level"></td>
								<td style="border-right:solid 1px #ff0000;" data-bind="text: $data.client_count"></td>
								<td>-</td>
								<td data-bind="text: $data.month_visit_count"></td>
								<td data-bind="text: $data.month_visit_count"></td>
								<td style="border-right:solid 1px #ff0000;" data-bind="text: $data.month_visit_count"></td>
								<td>-</td>
								<td data-bind="text: $data.month_visit_count"></td>
								<td data-bind="text: $data.month_visit_count"></td>
								<td data-bind="text: $data.week_visit_count"></td>
							</tr>
							</tbody>
				
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>客户姓名</th>
								<th>关系度</th>
								<th>回款誉</th>
								<th>市场力</th>
								<!-- <th>年订单</th> -->
								<th>月订单</th>
								<th>签单期间</th>
								<th>拜访目的</th>
								<th>最近精推</th>
								<!-- <th>拜访期间</th> -->
								<!-- <th>有效通话</th>
								<th>通话期间</th> -->
								<th>应收款总计</th>
								<th>最长账期</th>
								<!-- <th>待办事宜</th> -->
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: relations">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.client_employee_pk+';'+$data.client_employee_name}, checked: $root.chosenEmployee" /></td>
								<td> <a href="javascript:void(0)" data-bind="text: $data.client_employee_name,attr: {href: 'employee-detail.jsp?key='+$data.client_employee_pk}"></a></td>
								<td data-bind="text: $data.relation_level"></td>
								<td data-bind="text: $data.back_level"></td>
								<td data-bind="text: $data.market_level"></td>
								<!-- <td data-bind="text: $data.year_order_count"></td> -->
								<td data-bind="text: $data.month_order_count"></td>
								<td data-bind="text: $data.last_order_period"></td>
								<td data-bind="text: $data.visit_count"></td>
								<td data-bind="text: $data.accurate_count"></td>
								<!-- <td data-bind="text: $data.last_visit_period"></td> -->
								<!-- <td data-bind="text: $data.chat_count"></td>
								<td data-bind="text: $data.last_chat_period"></td> -->
								<td data-bind="text: $data.receivable"></td>
								<td data-bind="text: $data.last_receivable_period"></td>
								<!-- <td><a href="javascript:void(0)" data-bind="event: {click:function(){$root.createToDo($data.client_employee_pk)}}">新增</a>&nbsp; <a href="javascript:void(0)"
									data-bind="event: {click:function(){$root.viewToDo($data.pk)}}">查看</a></td> -->

								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.sales_name"></td>
								</s:if>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat">
						<a data-bind="click: previousPage, enable: currentPage() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums -->
						<!-- ko if: $data == $root.currentPage() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage() -->
						<a data-bind="text: $data, click: $root.turnPage"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage, enable: currentPage() < pageNums().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="todo-create" style="display: none">
		<div class="input-row clearfloat" style="width: 400px;">
			<div class="ip">
				<input type="text" id="todo_content" class="form-control" maxlength="10" placeholder="十个字以内" />
				<input type="hidden" id="client_employee_pk" />
			</div>
		</div>
		<div class="input-row clearfloat" style="width: 400px;">
			<div class="ip" style="float: right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doCreateToDo}" value="保存"></input> <input type="button" data-bind="event:{click:cancelCreateToDo}"
					class="btn btn-green col-md-1" value="取消"></input>
			</div>
		</div>
	</div>
	<div id="client-level" style="display: none;width:800px" >
	<div class="form-horizontal search-panel">
		<div class="form-group" style="width:800px">
				<div class="span6">
					<label class="col-md-1 control-label">关系度</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: relationLevel, value:chosenRelationLevel"
							name="relation.level"></select>
					</div>
				</div>
				<div class="span6">
					<label class="col-md-1 control-label">市场力</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: marketLevel,value:chosenMarketLevel"
							name="relation.level"></select>
					</div>
				</div>
				<div class="span6">
					<label class="col-md-1 control-label">回款誉</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: backLevel,value:chosenBackLevel"
							name="relation.level"></select>
					</div>
				</div>
		</div>
		<div class="form-group" style="float:right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doSetClientLevel}" value="保存"></input>
				<input type="button" data-bind="event:{click:cancelSetClientLevel}" class="btn btn-green col-md-1" value="取消"></input>
		</div>
	</div>
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/client-relation.js"></script>
</body>
</html>