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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
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
			<h2>航司押金</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: create">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: receive">退还</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: delete_deposit">删除</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="deposit.statuses" /><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div>
							<label class="col-md-1 control-label">供应商</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" name="deposit.supplier_name" />
							</div>
						</div>
						<div>
							<label class="col-md-1 control-label">支出账户</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control" name="deposit.account"
									data-bind="options: accounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
							</div>
						</div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
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
								<th>剩余</th>
								<th>到期时间</th>
								<th>状态</th>
								<th>退还途径</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach:deposits">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenDeposits" /></td>
								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.supplier_name"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.received" class="rmb"></td>
								<td data-bind="text: $data.balance" class="rmb"></td>
								<td data-bind="text: $data.return_date"></td>
								<!-- ko if:$data.status=='N' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.status=='Y' -->
								<td data-bind="text: $root.statusMapping[$data.status]" style="color: green"></td>
								<!-- /ko -->
								<td><a href="javascript:void(0)"
									data-bind="text: $root.wayMapping[$data.return_way],click:$root.viewDetail"></a></td>
								<td data-bind="text: $data.comment"></td>
								<!-- ko if:$data.status=='I' -->
								<!-- <td data-bind="text: $root.statusMapping[$data.status]"></td> -->
								<!-- /ko -->
								<!-- <td><a href="javascript:void(0)" data-bind="click:$root.viewDetail">查看</a></td> -->
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
	<div id="div-create" style="display: none; width: 1200px; height: 750px; overflow-y: auto; padding-top: 30px;">
		<form class="form-box info-form" id="form-create">
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">支出账户</label>
					<div class="ip">
						<select class="form-control" name="deposit.account"
							data-bind="options: ticketAccounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --'"
							required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">供应商</label>
					<div class="ip">
						<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="financial-body-name"
							required="required" /> <input type="text" class="ip-" data-bind="click:choseFinancial" style="display: none"
							name="deposit.supplier_pk" id="financial-body-pk" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">金额</label>
					<div class="ip">
						<input type="number" class="ip-default" placeholder="押金金额" name="deposit.money" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">入账时间</label>
					<div class="ip">
						<input type="text" name="deposit.time" placeholder="请准确填写避免冲突" class="form-control datetime-picker"
							required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l">退还日期</label>
					<div class="ip">
						<input type="text" maxlength="10" class="ip-default date-picker" placeholder="归还日期" name="deposit.return_date"
							required="required" />
					</div>
				</div>
				<div class="col-md-6">
					<label class="l">备注</label>
					<div class="ip">
						<textarea type="text" class="ip-default" rows="4" maxlength="200" name="deposit.comment" placeholder="需要备注说明的信息"></textarea>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required"
						accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="deposit.voucher_file_name" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
		</form>
		<div class="form-group">
			<div style="float: right">
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: doCreate">提交</button>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: cancelCreate">取消</button>
			</div>
		</div>
	</div>
	<div id="div-receive" style="display: none; width: 1200px; height: 750px; overflow-y: auto; padding-top: 30px;">
		<form class="form-box info-form" id="form-receive">
			<div class="input-row clearfloat">
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">收款账户</label>
					<div class="ip" style="width: 70%">
						<select class="form-control" name="deposit.account" data-bind="options: accounts, optionsCaption: '-- 请选择 --'"
							required="required"></select>
					</div>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">退还总金额</label>
					<div class="ip" style="width: 70%">
						<input type="number" class="ip-default" placeholder="退还总金额" name="deposit.money" id="sum-money" required="required" />
					</div>
				</div>
				<div class="col-md-4 required">
					<label class="l" style="width: 30%">入账时间</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="deposit.time" placeholder="请准确填写避免冲突" class="form-control datetime-picker"
							required="required" />
					</div>
				</div>
			</div>
			<hr />
			<div data-bind="foreach: chosenDeposits" id="div-allot">
				<div>
					<input type="hidden" data-bind="value:$data.pk" st="deposit-pk" />
					<input type="hidden" data-bind="value:$data.balance" st="deposit-balance" />
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l" style="width: 30%">供应商</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text: $data.supplier_name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l" style="width: 30%">剩余押金</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.balance"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l" style="width: 30%">押金备注</label>
							<div class="ip" style="width: 70%">
								<p class="ip-default" data-bind="text:$data.comment"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">分配金额</label>
							<div class="ip">
								<input type="number" class="ip-default" placeholder="押金金额"
									data-bind="attr:{'name':'money_'+$index()}" st="money" min="1" required="required" />
							</div>
						</div>
					</div>
				</div>
				<hr />
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required"
						accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="deposit.voucher_file_name" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
		</form>
		<div class="form-group">
			<div style="float: right">
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: doReceive">提交</button>
				<button type="submit" class="btn btn-green col-md-1" data-bind="click: cancelReceive">取消</button>
			</div>
		</div>
	</div>
	<!-- 选择供应商 -->
	<div id="financial-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">主体简称</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="主体简称" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchFinancial }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>财务主体简称</th>
								<th>负责人</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: suppliers">
							<tr data-bind="event: {click: function(){ $parent.pickFinancial($data.supplier_short_name,$data.pk)}}">
								<td data-bind="text: $data.supplier_short_name"></td>
								<td data-bind="text: $data.body_name"></td>
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
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/ticket/deposit.js"></script>
</body>
</html>