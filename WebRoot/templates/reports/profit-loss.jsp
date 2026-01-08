<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
String path = request.getContextPath();
String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
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

.fix_width {
	width: 50% !important
}

.col-md-2 {
	width: 20% !important
}

.info-form {
	position: relative !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>损益表</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: statuses" style="padding-top: 4px;">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}" name="statuses" /><label
									data-bind="text: $root.approvedMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">月份</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" data-bind="value:month" placeholder="起始月" name="date_from" />
							</div>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" data-bind="value:month" placeholder="截止月" name="date_to" />
							</div>
						</div>
						<div style="padding-top: 3px; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<hr />
				<h4 style="margin-bottom: 10px">汇总</h4>
				<div class="form-box info-form" id="div-summary">
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l" title="净利=毛利-票务-人力成本-费用申请+其他利润">净利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().profit"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">单数：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().order_count"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">人数：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().people_count"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">人均：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().per_profit"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">毛利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().gross_profit"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">票务：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().ticket"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">人力：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text:summary().human"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">费用申请：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: summary().reimbursement"></p>
							</div>
						</div>
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">主营</h4>
				<div class="form-box info-form" id="div-main">
					<div class="input-row clearfloat">
						<div class="col-md-2">
							<label class="l">团款：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().receivable"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">立款：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().discount_receivable"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">98清尾：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().tail98"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">单数：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().order_count"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">人数：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().people_count"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-2">
							<label class="l">机票：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().air_ticket_cost"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">地接：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().product_cost"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">fy：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().fy"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l" title="毛利=实际团款-机票-地接-fy">毛利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: main().gross_profit"></p>
							</div>
						</div>
						<div class="col-md-2">
							<label class="l">人均毛利：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text:main().per_gross_profit"></p>
							</div>
						</div>
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">票务</h4>
				<div class="form-box info-form" id="div-ticket">
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">票务合计：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: ticket().sum"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">押金扣款：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: ticket().deduct"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">票务额外支出：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: ticket().payment"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">票务费用申请：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: ticket().reimbursement"></p>
							</div>
						</div>
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">人力</h4>
				<div class="form-box info-form" id="div-human">
					<div class="input-row clearfloat">
						<!-- ko foreach: human_items -->
						<div class="col-md-3">
							<label class="l" data-bind="text:$data === 'SUM' ? '人力成本' : payTypeMapping[$data]"></label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: $root.human().get($data)"></p>
							</div>
						</div>
						<!-- /ko -->
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">费用申请汇总</h4>
				<div class="form-box info-form" id="div-reimbursement">
					<div class="input-row clearfloat">
						<!-- ko foreach: reimbursement_items -->
						<div class="col-md-2">
							<label class="l" data-bind="text:$data === 'SUM' ? '费用' : payTypeMapping[$data]"></label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: $root.reimbursement().get($data)"></p>
							</div>
						</div>
						<!-- /ko -->
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">其他</h4>
				<div class="form-box info-form" id="div-other">
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">其它利润：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: other().profit"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">其它营收：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: other().receive"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">其它成本：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: other().pay"></p>
							</div>
						</div>
						<div class="col-md-3"></div>
					</div>
				</div>
				<hr />
				<h4 style="margin-bottom: 10px">内部划拨</h4>
				<div class="form-box info-form" id="div-inner">
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">后台划拨：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: inner().sys_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">销售划拨：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text: inner().sale_cost"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">提成立奖：</label>
							<div class="ip fix_width">
								<p class="ip-default rmb" data-bind="text:'无'"></p>
							</div>
						</div>
						<div class="col-md-3"></div>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".data").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/accounting-constant.js?v=1.001"></script>
	<script src="<%=basePath%>static/js/reports/profit-loss.js?v=1.000"></script>
</body>
</html>