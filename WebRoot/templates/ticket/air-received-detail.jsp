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
</head>

<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>押金退还记录</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<label class="col-md-1 control-label">单号</label>
						<div class="col-md-2">
							<input type="text" class="form-control" name="detail.business_number" />
						</div>
						<label class="col-md-1 control-label">收入账户</label>
						<div class="col-md-2">
							<select class="form-control" name="detail.card_account"
								data-bind="options: accounts,optionsCaption: '-- 请选择 --',event:{change:refresh}"></select>
						</div>
						<label class="col-md-1 control-label">收入月份</label>
						<div class="col-md-2">
							<input type="text" class="form-control month-picker-st" name="detail.received_month" placeholder="收入月份" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">供应商</label>
						<div class="col-md-2">
							<input type="text" class="form-control" name="detail.supplier_name" />

						</div>
						<label class="col-md-1 control-label">收入时间</label>
						<div class="col-md-2">
							<input type="text" class="form-control date-picker" placeholder="from" name="detail.date_from" />
						</div>
						<div class="col-md-2">
							<input type="text" class="form-control date-picker" placeholder="to" name="detail.date_to" />
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">备注</label>
						<div class="col-md-2">
							<input type="text" class="form-control" name="detail.comment" placeholder="填写部分信息即可" />
						</div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>单号</th>
								<th>供应商</th>
								<th>收款账户</th>
								<th>分配金额</th>
								<th>总金额</th>
								<th>收入时间</th>
								<th>类型</th>
								<th>状态</th>
								<th>确认时间</th>
								<th>确认人</th>
								<th>备注</th>
								<th>收款凭证</th>
								<th>申请人</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach:details">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenReceiveds" /></td>
								<td data-bind="text: $data.business_number"></td>
								<td data-bind="text: $data.supplier_name"></td> 
								<td data-bind="text: $data.card_account"></td>
								<td data-bind="text: $data.received"></td>
								<td data-bind="text: $data.sum_received" class="rmb"></td>
								<td data-bind="text: $data.received_time"></td>
								<td data-bind="text: $root.typeMapping[$data.received_type]"></td>
								<td data-bind="text:$root.statusMapping[$data.status]"></td>
								<td data-bind="text:$data.confirm_time" class="rmb"></td>
								<td data-bind="text: $data.confirm_user"></td>
								<td data-bind="text: $data.comment"></td>
								<td><a href="javascript:;"
									data-bind="click: function() {$root.checkIdPic($data.received_time,$data.voucher_file)}">查看</a></td>
								<td data-bind="text: $data.apply_user"></td> 
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
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".ticket-finance").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/air-received-detail.js"></script>
</body>
</html>