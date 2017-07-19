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

</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<!-- tbc for to be confirmed -->
			<h2>已确认订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteOrder() }">决算</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editOrder() }">备注</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<%-- <div class="span6">
							<label class="col-md-1 control-label">产品线</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: locations, optionsCaption: '-- 请选择 --',event: {change:refresh}" name="product.location"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" name="product.product_number"></input>
							</div>
						</div> --%>
					</div>
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>状态</th>
								<th>团号</th>
								<th>客户</th>
								<th>出团日期</th>
								<th>产品名称</th>
								<th>人数</th>
								<th>游客信息</th>
								<th>回团天数</th>
								<th>天数</th>
								<th>总团款</th>
								<th>产品经理</th>
								<th>确认件</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk+';'+$data.standard_flg}, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $data.status"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td></td>
								<td data-bind="text: $data.back_days"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.receivable"></td>
								<td data-bind="text: $data.product_manager"></td>
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.checkIdPic($data.confirm_file,$data.create_user_number)} ">查看</a></td>
								<td></td>
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
	<div id="pic-check" style="display:none">
 	<jsp:include page="../common/check-picture.jsp" />
 </div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/order/c-order.js"></script>
</body>
</html>