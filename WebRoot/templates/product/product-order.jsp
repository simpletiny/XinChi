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
<style>
#table-supplier th,#table-supplier td {
	text-align: center;
}

#table-supplier tr td input {
	width: 90%;
}

#table-supplier {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

.required th[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="span6">
							<div class="col-md-6">
								<div data-bind="foreach: status" style="padding-top: 4px;">
									<em class="small-box"> <input type="checkbox" data-bind="attr: {'value': $data},checked: $root.chosenStatus,click:function(){$root.refresh();return true;}"
										name="order_option.operate_flgs" /><label data-bind="text: $root.statusMapping[$data]"></label>
									</em>
								</div>
							</div>
						</div>
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createOperate() }">生成操作</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.product_number" placeholder="产品编号"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.product_name" placeholder="产品名称"></input>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="order_option.product_manager_number"></select>
								</div>
							</div>
						</s:if>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" name="order_option.team_number" placeholder="团号"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">出团日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="from" name="order_option.date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" placeholder="to" name="order_option.date_to" />
							</div>
						</div>
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
								<th>出团日期</th>
								<th>产品名称</th>
								<th>成人</th>
								<th>特殊</th>
								<th>游客信息</th>
								<th>航班信息</th>
								<th>销售</th>
								<th>接待特请</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk+';'+$data.product_pk+';'+$data.team_number+';'+$data.operate_flg}, checked: $root.chosenOrders" /></td>
								<td data-bind="text:$root.statusMapping[$data.operate_flg]"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.adult_count"></td>
								<td data-bind="text: $data.special_count"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger"></a></td>
								<td data-bind="text: $data.air_info"></td>
								<td data-bind="text: $data.sale_name"></td>
								<td data-bind="text: $data.treat_comment"></td>
							</tr>
						</tbody>
						<tr id="total-row">
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td></td>
							<td>汇总</td>
							<td data-bind="text:totalAdult"></td>
							<td data-bind="text:totalSpecial"></td>
							<td></td>
							<td></td>
							<td></td>
						</tr>
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
	<div id="supplier-info" style="display: none; width: 1400px">
		<div class="input-row clearfloat">
			<div style="margin-top: 20px; height: 300px" id="div-ticket">
				<table style="width: 90%" id="table-supplier">
					<thead>
						<tr class="required">
							<th style="width: 5%">序号</th>
							<th style="width: 10%" class="r">供应商</th>
							<th style="width: 10%" class="r">产品名称</th>
							<th style="width: 5%" class="r">总价格</th>
							<th style="width: 9%" class="r">接团天次</th>
							<th style="width: 10%">接团方式</th>
							<th style="width: 10%">接团人</th>
							<th style="width: 10%">联系方式</th>
							<th style="width: 9%" class="r">送团天次</th>
							<th style="width: 10%">送团方式</th>
							<th style="width: 2%"></th>
						</tr>
					</thead>
					<!-- ko if:productSuppliers().length>0 -->
					<tbody data-bind="foreach:productSuppliers">
						<tr>
							<td st="index" data-bind="text:$data.supplier_index"></td>
							<td><input type="text" st="supplier-name" data-bind="value:$data.supplier_employee_name" onclick="choseSupplierEmployee(event)" /> <input type="text" class="need"
								data-bind="value:$data.supplier_employee_pk" st="supplier-pk" style="display: none" /></td>
							<td><input class="need" st="supplier-product-name" maxlength="10" data-bind="value:$data.supplier_product_name" type="text" /></td>
							<td><input class="need" st="supplier-cost" data-bind="value:$data.sum_cost" type="number" /></td>
							<td><input class="need" st="land-day" data-bind="value:$data.land_day" type="number" /></td>
							<td><input st="pick-type" data-bind="value:$data.pick_type" maxlength="50" type="text" /></td>
							<td><input st="picker" data-bind="value:$data.picker" maxlength="10" type="text" /></td>
							<td><input st="picker-cellphone" data-bind="value:$data.picker_cellphone" maxlength="15" type="number" /></td>
							<td><input class="need" st="off-day" type="number" data-bind="value:$data.off_day" /></td>
							<td><input st="send-type" maxlength="50" type="text" data-bind="value:$data.send_type" /></td>
							<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
						</tr>
					</tbody>
					<!-- /ko -->
					<!-- ko if:productSuppliers().length<1 -->
					<tbody>
						<tr>
							<td st="index">1</td>
							<td><input type="text" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input class="need" type="text" st="supplier-pk" style="display: none" /></td>
							<td><input class="need" st="supplier-product-name" maxlength="10" type="text" /></td>
							<td><input class="need" st="supplier-cost" type="number" /></td>
							<td><input class="need" st="land-day" type="number" /></td>
							<td><input st="pick-type" maxlength="50" type="text" /></td>
							<td><input st="picker" maxlength="10" type="text" /></td>
							<td><input st="picker-cellphone" maxlength="15" type="number" /></td>
							<td><input class="need" st="off-day" type="number" /></td>
							<td><input st="send-type" maxlength="50" type="text" /></td>
							<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
						</tr>
					</tbody>
					<!-- /ko -->
				</table>
				<div style="margin-top: 20px; float: right">
					<input type="button" value="添加供应商" onclick="addRow()"></input>
				</div>
			</div>

			<div style="margin-top: 50px; width: 700px; float: right">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:function(){cancelOperate()}">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:function(){doOperate()}">确定</button>
			</div>
		</div>

	</div>

	<div id="supplier-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchSupplierEmployee }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>财务主体</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: supplierEmployees">
							<tr data-bind="event: {click: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat">
						<a data-bind="click: previousPage1, enable: currentPage1() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums1 -->
						<!-- ko if: $data == $root.currentPage1() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage1() -->
						<a data-bind="text: $data, click: $root.turnPage1"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage1, enable: currentPage1() < pageNums1().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-order.js"></script>
</body>
</html>