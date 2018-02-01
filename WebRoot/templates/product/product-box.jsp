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
			<h2>产品架</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createStandard() }">客户需求</button>
							</div>
							<!-- <div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createNonStandard() }">非标需求</button>
							</div> -->
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createIndependent() }">独立团需求</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品线</label>
							<div class="col-md-6">
		                       <div data-bind="foreach: locations" style="padding-top: 4px;">
		                           <em class="small-box">
		                                 <input type="checkbox" data-bind="attr: {'value': $data},click:function(){$root.refresh();return true;}" name="product.locations"/><label data-bind="text: $data"></label>
		                            </em>
		                        </div>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" name="product.product_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="product.name"></input>
							</div>
						</div>
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
								<th>产品线</th>
								<th>产品名称</th>
								<th>天数</th>
								<th>同业报价</th>
								<th>产品分值</th>
								<th>最大让利</th>
								<th>现结立减</th>
								<th>产品活动</th>
								<th>注意事项</th>
								<th>产品编号</th>
								<th>机票信息</th>
								<th>产品经理</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: products">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenProducts" /></td>
								<td data-bind="text: $data.location"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.business_price"></td>
								<td data-bind="text: $data.product_value"></td>
								<td data-bind="text: $data.max_profit_substract"></td>
								<td></td>
								<td></td>
								<td></td>
								<td data-bind="text: $data.product_number"></td>
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.checkAirTicket($data.pk)} ">查看</a></td>
								<td data-bind="text: $data.product_manager"></td>
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
	<div id="air-ticket-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="width: 100%">
				<label class="l">产品名称</label> <label class="l" data-bind="text:product().name"></label> <label class="l">产品编号</label> <label class="l" data-bind="text:product().product_number"></label> <label
					class="l" data-bind="text:chargeMapping[product().air_ticket_charge]"></label>
			</div>
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">起飞城市</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">抵达城市</th>
							<th style="width: 20%">航班号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.ticket_index"></td>
							<td data-bind="text:$data.start_day"></td>
							<td data-bind="text:$data.start_city"></td>
							<td data-bind="text:$data.end_day"></td>
							<td data-bind="text:$data.end_city"></td>
							<td data-bind="text:$data.ticket_number"></td>
						</tr>
					</tbody>
					<!-- /ko -->
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".product-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/product/product-box.js"></script>
</body>
</html>