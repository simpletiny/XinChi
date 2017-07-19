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
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createStandard() }">创建订单</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createNonStandard() }">创建非标</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createIndependent() }">独立团</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
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
								<th>销售利润</th>
								<th>最大让利</th>
								<th>现结立减</th>
								<th>产品活动</th>
								<th>注意事项</th>
								<th>产品编号</th>
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
								<td data-bind="text: $data.profit_space"></td>
								<td data-bind="text: $data.max_profit_substract"></td>
								<td></td>
								<td></td>
								<td></td>
								<td data-bind="text: $data.product_number"></td>
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
	<script>
		$(".product-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/product/product-box.js"></script>
</body>
</html>