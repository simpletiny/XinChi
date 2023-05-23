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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css?v1.001" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />

<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}
</style>

</head>

<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>往来详表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDetail('R');}">添加收入</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDetail('P');}">添加支出</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function(){createDeduct();}">押金扣款</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: rollBack">打回重报</button>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="option.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="option.money_to" />
							</div>
						</div>

						<label class="col-md-1 control-label">首航日期</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control date-picker" placeholder="from" name="option.first_date_from" />
						</div>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control date-picker" placeholder="to" name="option.first_date_to" />
						</div>

						<div>
							<label class="col-md-1 control-label">首航段</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.from_to_city" />
							</div>
						</div>


					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">精确金额</label>
						<div class="col-md-2" style="float: left">
							<input type="number" class="form-control" style="width: 44%" placeholder="精确金额" name="option.money" />
						</div>

						<label class="col-md-1 control-label">入账日期</label>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control date-picker" id="option-data-from" data-bind="value: dateFrom" placeholder="from"
								name="option.date_from" />
						</div>
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control date-picker" id="option-data-to" data-bind="value: dateTo" placeholder="to"
								name="option.date_to" />
						</div>
						<div>
							<label class="col-md-1 control-label">收款方</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.receiver" />
							</div>
						</div>
						<!-- 						<div  align="left">
							<label class="col-md-1 control-label">产品经理</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="option.create_user" />
							</div>
						</div> -->

					</div>
					<div class="form-group">
						<div>
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control"
									data-bind="options: types, optionsText:function(item){return typeMapping[item]}, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="option.type" required="required"></select>
							</div>
						</div>
						<label class="col-md-1 control-label">归属月份</label> 
						<div class="col-md-2" style="float: left">
							<input type="text" class="form-control month-picker-st" id="option-belong-month" name="option.belong_month" />
						</div>
						<label class="col-md-1 control-label">责任产品</label>
						<div class="col-md-2" style="float: left">
							<select class="form-control" name="option.product_manager"
								data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--请选择--'"></select>
						</div>
						<button type="submit" style="float: right" class="btn btn-green" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" id="main-table">
						<thead>
							<tr role="row">
								<th></th>
								<th>金额</th>
								<th>类型</th>
								<th>首航日期</th>
								<th>首航段</th>
								<th>名单</th>
								<th>供应商员工</th>
								<th>收款方</th>
								<th>入账日期</th>
								<th>状态</th>
								<th>支付详情</th>
								<th>责任产品</th>
								<th>归属月份</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: paids">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.related_pk+';'+$data.type+';'+$data.status}, checked: $root.chosenPaids" /></td>

								<td data-bind="text: $data.money" class="rmb"></td>

								<td style="color: green" data-bind="text: $root.typeMapping[$data.type]"></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkTicketInfo($data);},text: $data.first_date"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkTicketInfo($data);},text: $data.from_to_city"></a></td>
								<td><a href="javascript:void(0)"
									data-bind="click:function(){$root.checkPassengers($data);},text: $data.passenger"></a></td>

								<td data-bind="text: $data.supplier_employee_name"></td>
								<td data-bind="text: $data.receiver"></td>
								<!-- ko if:$data.type=='BACK' || $data.type=='RECEIVE' -->
								<td data-bind="text: $data.confirm_time"></td>
								<!-- /ko -->
								<!-- ko if:$data.type!='BACK' && $data.type!='RECEIVE' -->
								<td data-bind="text: $data.time"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='I' -->
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='P' -->
								<td style="color: green" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='N' -->
								<td style="color: red" data-bind="text: $root.statusMapping[$data.status]"></td>
								<!-- /ko -->

								<td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td>
								<!-- ko if:$data.product_manager_name==null && ($data.type=='DEDUCT'|| $data.base_pk=='SIMPLE')-->
								<td><a href="javascript:void(0)" data-bind="click:$root.addProductManager">添加</a></td>
								<!-- /ko -->
								<!-- ko ifnot:$data.product_manager_name==null && ($data.type=='DEDUCT'|| $data.base_pk=='SIMPLE') -->
								<td data-bind="text:$data.product_manager_name"></td>
								<!-- /ko -->
								<!-- ko if:$data.belong_month==null && ($data.type=='DEDUCT'|| $data.base_pk=='SIMPLE') -->
								<td><a href="javascript:void(0)" data-bind="click:$root.addProductManager">添加</a></td>
								<!-- /ko -->
								<!-- ko ifnot:$data.belong_month==null && ($data.type=='DEDUCT' || $data.base_pk=='SIMPLE') -->
								<td data-bind="text:$data.belong_month"></td>
								<!-- /ko -->
								<td data-bind="text: $data.comment"></td>
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
	<div id="sum_detail" style="display: none; width: 1200px; height: 500px; overflow-y: auto; padding-top: 30px;">

		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">支付金额</label>
			</div>
		</div>
		<!-- ko foreach:details -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.financial_body_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
		<div class="input-row clearfloat">
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支账户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收支时间</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">收款方账户</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">收支金额</label>
			</div>
			<div class="col-md-2">
				<label class="l" style="width: 100%">凭证</label>
			</div>
		</div>
		<!-- ko foreach:paymentDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.account"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.time"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.receiver"></p>
				</div>
			</div>
			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.money"></p>
				</div>
			</div>

			<div class="col-md-2">
				<div class="ip">
					<p class="ip-default">
						<a href="javascript:void(0)"
							data-bind="click: function() {$root.checkVoucherPic($data.voucher_file_name,$data.account_pk)} ">查看</a>
					</p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<!-- 无业务扣款 -->
	<div id="div-deduct" style="display: none; width: 1000px; height: 600px; overflow: auto; padding-top: 30px;">
		<h3 style="margin-left: 100px; color: red">押金账</h3>
		<form id="form-deduct">
			<div style="min-height: 200px">
				<div class="input-row clearfloat">
					<div class="col-md-12">
						<a type="button" class="btn btn-green btn-r" style="float: right" data-bind="click: chooseDeposit">选择押金</a>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>付押账户</th>
								<th>押金方</th>
								<th>金额</th>
								<th>已退</th>
								<th>可用余额</th>
								<th>到期时间</th>
								<th>备注</th>
								<th style="color: red">扣款金额</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: usedDeposits" id="tbody-info">
							<tr>

								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb" style="color: red"></td>
								<td data-bind="text: $data.return_date"></td>
								<td data-bind="text: $data.comment"></td>
								<td><input type="number" min="1" class="ip-" id="deduct-money" required="required" /> <input type="hidden"
									data-bind="value:$data.pk" id="deposit-pk" /> <input type="hidden" data-bind="value:$data.balance"
									id="deposit-balance" /></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">扣款时间</label>
					<div class="ip" style="width: 40%">
						<input type="text" class="ip- date-picker date" id="deduct-date" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">责任经理</label>
					<div class="ip" style="width: 40%">
						<select class="form-control" id="product-manager" required="required"
							data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--请选择--'"></select>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">归属月份</label>
					<div class="ip" style="width: 40%">
						<input type="text" class="ip- month-picker-st" id="belong-month" value='${user.current_date.substring(0,7)}'
							placeholder="月份" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-8 required">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="7" maxlength="200" id="comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doCreateDeduct">提交</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelDeduct">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<!-- 添加责任产品-->
	<div id="product-manager-add" style="display: none; width: 400px; height: 200px;">
		<div class="input-row clearfloat">
			<div class="col-md-12 required">
				<label class="l">责任经理</label>
				<div class="ip" style="width: 60%">
					<select class="form-control" id="add-product-manager" required="required"
						data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number', optionsCaption: '--请选择--'"></select>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12 required">
				<label class="l">归属月份</label>
				<div class="ip" style="width: 60%">
					<input type="text" class="ip- month-picker-st" placeholder="月份" value='${user.current_date.substring(0,7)}'
						id="temp-belong-month" />
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12" style="margin-top: 10px">
				<div align="right">
					<a type="button" class="btn btn-green btn-r" data-bind="click: doAddProductManager">保存</a> <a type="button"
						class="btn btn-green btn-r" data-bind="click: cancelAddProductManager">取消</a>
				</div>
			</div>
		</div>
	</div>

	<!-- 选择航司押金 -->
	<div id="deposit-pick" style="display: none; width: 1110px; height: 600px; overflow: auto">
		<form class="form-horizontal search-panel" id="form-search-deposit">
			<div class="form-group">
				<div>
					<label class="col-md-1 control-label">供应商</label>
					<div class="col-md-2" style="float: left">
						<input type="text" class="form-control" name="deposit.supplier_name" />
					</div>
				</div>
				<div>
					<label class="col-md-1 control-label">支出账户</label>
					<div class="col-md-3" style="float: left">
						<select class="form-control" name="deposit.account"
							data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
					</div>
				</div>
				<div>
					<label class="col-md-1 control-label">备注</label>
					<div class="col-md-2" style="float: left">
						<input type="text" class="form-control" name="deposit.comment" placeholder="填写部分信息即可" />
					</div>
				</div>
				<div>
					<button type="submit" class="btn btn-green" data-bind="click: searchDeposit">搜索</button>
					<button type="submit" class="btn btn-green" data-bind="click: finishChoose">选择</button>
				</div>
			</div>
		</form>
		<div class="list-result">
			<table class="table table-striped table-hover">
				<thead>
					<tr role="row">
						<th></th>
						<th>支出账户</th>
						<th>收款方</th>
						<th>金额</th>
						<th>已退</th>
						<th>可用余额</th>
						<th>到期时间</th>
						<th>备注</th>
					</tr>
				</thead>
				<tbody data-bind="foreach: deposits">
					<tr>
						<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenDeposits" /></td>
						<td data-bind="text: $data.account"></td>
						<td data-bind="text: $data.supplier_name"></td>
						<td data-bind="text: $data.money" class="rmb"></td>
						<td data-bind="text: $data.received" class="rmb"></td>
						<td data-bind="text: $data.balance" class="rmb" style="color: red"></td>
						<td data-bind="text: $data.return_date"></td>
						<td data-bind="text: $data.comment"></td>
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
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
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
	<!-- 查看机票信息-->
	<div id="infos-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">日期</th>
							<th style="width: 10%">时间</th>
							<th style="width: 10%">城市对</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:infos">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.ticket_date"></td>
							<td data-bind="text:$data.from_to_time"></td>
							<td data-bind="text:$data.from_to_city"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".ticket-finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/paid.js?v=1.004"></script>
</body>
</html>