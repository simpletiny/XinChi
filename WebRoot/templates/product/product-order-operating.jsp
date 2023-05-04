<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
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
#table-supplier th, #table-supplier td {
	text-align: center;
}

#table-supplier tr td input {
	width: 90%;
}

#table-supplier {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

.required th.r:after {
	content: " *";
	color: red;
	font-weight: bold;
}

tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>操作中</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green" data-bind="click: function() { editOperation() }">修改价格</button>
								<button type="submit" class="btn btn-green" data-bind="click: function() { confirmOperation() }">确认</button>
								<button type="submit" class="btn btn-green" data-bind="click: function() { deleteOperation() }">打回重新操作</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" name="operate_option.supplier_product_name" placeholder="产品名称"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">订单号</label>
							<div class="col-md-2">
								<input class="form-control" name="operate_option.team_number" placeholder="订单号"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">团号</label>
							<div class="col-md-2">
								<input class="form-control" name="operate_option.team_number1" placeholder="团号"></input>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--全部--'"
										name="operate_option.create_user"></select>
								</div>
							</div>
						</s:if>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2">
								<input class="form-control " name="operate_option.supplier_employee_name" placeholder="供应商"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">供应商主体</label>
							<div class="col-md-2">
								<input class="form-control" name="operate_option.supplier_name" placeholder="供应商主体"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">接团月份</label>
							<div class="col-md-2">
								<input class="form-control month-picker-st" name="operate_option.pick_month" placeholder="接团月份"></input>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" id="main-table">
						<thead>
							<tr role="row">
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" />全选</th>
								<th>操作单号</th>
								<th>单/合</th>
								<th>供应商</th>
								<th>主体</th>
								<th>总成本</th>
								<th>产品名称</th>
								<th>人数</th>
								<th>接团日期</th>
								<th>送团日期</th>
								<th>游客信息</th>
								<th>备注</th>
								<th>下载</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: operations">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.team_number+';'+$data.supplier_cost}, checked: $root.chosenOperations" /></td>
								<td><a
									data-bind="text: $data.team_number+'&nbsp;&nbsp;&nbsp;&nbsp;'+$data.operate_index+'/'+$data.supplier_count, click:function(){$root.checkOrders($data.team_number)}"></a></td>
								<!-- ko if: $data.single_flg == "N" -->
								<td><a href="javascript:void(0)"
									data-bind="text: $root.singleMapping[$data.single_flg], click:function(){$root.checkOrders($data.team_number)}"></a></td>
								<!-- /ko -->
								<!-- ko if: $data.single_flg == "Y" -->
								<td><a href="javascript:void(0)" style="color: red"
									data-bind="text: $root.singleMapping[$data.single_flg], click:function(){$root.checkOrders($data.team_number)}"></a></td>
								<!-- /ko -->
								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.supplier_cost"></td>
								<td data-bind="text: $data.supplier_product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td data-bind="text: $data.pick_date"></td>
								<td data-bind="text: $data.send_date"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger_captain"></a></td>
								<td></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.downloadSc($data.team_number,$data.supplier_employee_pk)}"
									style="cursor: pointer; margin-right: 10px">确认件</a></td>
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
	<div id="supplier-info" style="display: none; width: 800px;height:450px;overflow-y:auto ">
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<table style="width: 100%" class="table table-striped table-hover" id="table-order">
					<thead>
						<tr class="required">
							<th style="width: 8%">团号</th>
							<th style="width: 8%">销售</th>
							<th style="width: 5%">人数</th>
							<th style="width: 10%">游客</th>
							<th style="width: 10%" class="r">地接费用</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:orders">
						<tr> 
							<td data-bind="text:$data.team_number" class="team-number"></td>
							<td data-bind="text:$data.create_user"></td>
							<td data-bind="text:$data.adult_count+($data.special_count==null?0:$data.special_count)"></td>
							<td><a href="javascript:void(0)" style="color: blue"
								data-bind="click:$root.checkPassengers,text: $data.passenger_captain"></a></td>
							<td><input type="number" class="form-control supplier-cost" data-bind="value:$data.payable" st="supplier-cost" /></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
		<hr/>
		<div class="input-row clearfloat">
			<input type="text" data-bind="value:operation().supplier_employee_pk" class="supplier-employee-pk" st="supplier-pk" style="display: none" />
			<div style="margin-top: 20px;" id="div-ticket">
				<table style="width: 90%" id="table-supplier">
					<thead>
						<tr class="required">
							<th style="width: 15%">供应商</th>
							<th style="width: 20%">产品名称</th>
							<th style="width: 10%">原费用</th>
							<th style="width: 10%" class="r">新费用</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td data-bind="text:operation().supplier_employee_name"></td>
							<td data-bind="text:operation().supplier_product_name"></td>
							<td data-bind="text:operation().supplier_cost"></td>
							<td><input class="form-control new-cost" st="supplier-cost" type="number" /></td>
						</tr>
					</tbody>
				</table>
			</div>

			<div style="margin-top: 50px; width: 700px; float: right">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:function(){cancelEdit()}">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:function(){doEdit()}">确定</button>
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
	<div id="passengers-check" style="display: none; width: 800px; height: 650px; overflow-y: auto;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">团号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
							<th style="width: 10%">电话1</th>
							<th style="width: 10%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.team_number"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_A"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<!-- 查看订单详情 -->
	<div id="div-check-order" style="display: none; width: 1000px; height: 550px; overflow-y: auto;">
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th>团号</th>
						<th>出团日期</th>
						<th>产品名称</th>
						<th>成人</th>
						<th>儿童</th>
						<th>游客信息</th>
						<th>销售</th>
						<th>接待特情</th>
						<th>订单状态</th>
						<th>锁定状态</th>
						<th>操作</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: sale_orders">
					<tr>
						<td data-bind="text: $data.team_number"></td>
						<td data-bind="text: $data.departure_date"></td>
						<td data-bind="text: $data.product_name"></td>
						<td data-bind="text: $data.adult_count"></td>
						<td data-bind="text: $data.special_count"></td>
						<td><a href="javascript:void(0)" data-bind="click:$root.innerCheckPassengers,text: $data.passenger_captain"></a></td>
						<td data-bind="text:$data.create_user"></td>
						<td><a href="javascript:void(0)"
							data-bind="click:function(){msg($data.treat_comment)},text:$data.treat_comment"></a></td>
						<!-- ko if: $data.cancel_flg == "N" -->
						<td data-bind="text:$root.orderStatusMapping[$data.cancel_flg]"></td>
						<!-- /ko -->
						<!-- ko if: $data.cancel_flg == "Y" -->
						<td style="color:red" data-bind="text:$root.orderStatusMapping[$data.cancel_flg]"></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg.substr(0,1) == "Y" -->
						<td style="color: green" data-bind="text:$root.lockMapping[$data.lock_flg.substr(0,1)]"></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg.substr(0,1) == "N" -->
						<td style="color: red" data-bind="text:$root.lockMapping[$data.lock_flg.substr(0,1)]"></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg.substr(0,1) == "Y" -->
						<td><a href="javascript:void(0)" data-bind="click:function(){$root.lockOrder($data.team_number,'N');}">解锁</a></td>
						<!-- /ko -->
						<!-- ko if: $data.lock_flg.substr(0,1) == "N" -->
						<td><a href="javascript:void(0)" data-bind="click:function(){$root.lockOrder($data.team_number,'Y');}">锁定</a></td>
						<!-- /ko -->
					</tr>
				</tbody>
			</table>
		</div>
	</div>
	<!-- 订单详情查看乘客信息 -->
	<div id="passengers-check-inner" style="display: none; width: 800px; height: 550px; overflow-y: auto;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
							<th style="width: 10%">电话1</th>
							<th style="width: 10%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_A"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/product/product-order-operating.js?v=1.3"></script>
</body>
</html>