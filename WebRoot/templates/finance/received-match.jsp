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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>收入匹配</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
			
					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: match">匹配</button>
								<button id = "btn-cancel" style="display:none" type="submit" class="btn btn-green col-md-1" data-bind="click: cancelMatch">取消匹配</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="ip">
							<div data-bind="foreach: allStatus" style="padding-top: 4px;">
								<em class="small-box"> <input type="radio" data-bind="attr: {value: $data },checked: $root.chosenStatus " onclick="changeStatus(this)" name="detail.match_flg"/><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">收入日期</label>
							<div class="col-md-2">
								<input type="text" data-bind="value:today" class="form-control date-picker" placeholder="收入日期" name="detail.time" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">账户</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --',event: {change:refresh}" name="detail.account" required="required"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">发生月</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="出团月份" name="detail.month" />
							</div>
						</div>
							<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>账户</th>
								<th>收入时间</th>
								<th>收入</th>
								<th>备注</th>
								<th>状态</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: details">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenDetails" /></td>
								<td data-bind="text: $data.account"></td>
								<td data-bind="text: $data.time"></td>
								<td data-bind="text: $data.money" class="rmb"></td>
								<td data-bind="text: $data.comment"></td>
								<!-- ko if: $data.match_flg =='Y' -->
								<td style="color:green">已匹配</td>
								<!-- /ko -->
								<!-- ko if: $data.match_flg =='N' -->
								<td style="color:red">未匹配</td>
								<!-- /ko -->
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
		$(".accounting").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/finance/received-match.js"></script>
</body>
</html>