<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
.deleteDiv {
	position: relative;
	display: block;
	width: 12px;
	height: 12px;
	background: url(../../static/img/mc-icon-cancel.png) no-repeat;
	background-size: cover;
	margin-right: 25px;
	padding-top: 2px;
	z-index: 999;
	float: right;
	cursor: pointer;
}

.delete {
	position: absolute;
	width: 100px;
	height: 50px;
	text-decoration: none;
	background: transparent;
	font-size: 16px;
	font-family: 微软雅黑, 宋体, Arial, Helvetica, Verdana, sans-serif;
	font-weight: bold;
	border: solid 2px black;
	-webkit-transition: all linear 0.30s;
	-moz-transition: all linear 0.30s;
	transition: all linear 0.30s;
	line-height: 50px;
	text-align: center;
	z-index: 999;
}

.delete:hover {
	background: #2f435e;
	color: #f2f2f2;
	opacity: 1;
	border: dashed 2px white;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				票款支付<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<h3>票源信息</h3>
				<div class="form-box info-form" id="div-payable">
					<!-- ko foreach: payables -->
					<div class="input-row clearfloat">
						<input type="hidden" data-bind="value:$data.pk" st="payable-pk" />
						<div class="col-md-4">
							<label class="l">票源</label>
							<p class="ip-default" data-bind="text:$data.supplier_employee_name"></p>
						</div>
						<div class="col-md-4">
							<label class="l">应付款</label>
							<p class="ip-default" data-bind="text: $data.budget_balance"></p>
						</div>
						<div class="col-md-4 required">
							<label class="l">此次付款</label> <input type="number" st="this-paid" data-bind="value:$data.budget_balance"
								onkeyup="caculateSumMoney()" style="width: 50%" class="ip-" placeholder="此次付款" required="required" />
						</div>
					</div>
					<!-- /ko -->
				</div>
				<h3>支付信息</h3>
				<div class="form-box info-form">

					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l">收款方</label>
							<p class="ip-default" data-bind="text: receiver"></p>
						</div>
						<div class="col-md-4">
							<label class="l">总金额</label>
							<p class="ip-default" id="sum-money"></p>
						</div>
					</div>
				</div>
				<form class="form-box info-form">
					<input type="hidden" id="payable_pks" value="<%=key%>" />
					<div>
						<div class="input-row clearfloat">

							<div class="col-md-6 required">
								<label class="l">支出账户</label>
								<div class="ip">
									<select class="form-control" name="account"
										data-bind="options: accounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --'"
										required="required"></select>
								</div>
							</div>
							<div class="col-md-6 required">
								<label class="l">支出时间</label>
								<div class="ip">
									<input type="text" name="time" class="ip- datetime-picker" name="xx" placeholder="支出时间" required="required" />
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">

							<div class="col-md-6 required">
								<label class="l">收款方账户名</label>
								<div class="ip">
									<input type="text" name="receiver" class="ip-" placeholder="收款方账户名" required="required" />
								</div>
							</div>
							<div class="col-md-6 required">
								<label class="l">支付金额</label>
								<div class="ip">
									<input type="number" name="money" class="ip-" placeholder="支付金额" required="required" />
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-6">
								<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required" name="file" /></a> <input
									type="hidden" name="voucherFile" />
							</div>
							<div class="col-md-6"></div>
						</div>
						<div class="input-row clearfloat"></div>
						<hr />
					</div>
					<div align="right" id="div_add">
						<a type="submit" class="btn btn-green btn-r" data-bind="click: add">添加</a>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: finish">支付完成</a>
				</div>
			</div>
		</div>
	</div>

	<div id="div_mod" style="display: none">
		<div>
			<div class="deleteDiv" title="删除" onclick="remove(this)"></div>
			<div class="input-row clearfloat">

				<div class="col-md-6 required">
					<label class="l">支出账户</label>
					<div class="ip">
						<select class="form-control" name="account"
							data-bind="options: accounts,optionsText:'account',optionsValue:'account', optionsCaption: '-- 请选择 --'"
							required="required"></select>
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">支出时间</label>
					<div class="ip">
						<input type="text" name="time" class="ip- datetime-picker" placeholder="支出时间" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">

				<div class="col-md-6 required">
					<label class="l">收款方账户名</label>
					<div class="ip">
						<input type="text" name="receiver" class="ip-" placeholder="收款方账户名" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l">支付金额</label>
					<div class="ip">
						<input type="number" name="money" class="ip-" placeholder="支付金额" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required" name="file" /></a> <input
						type="hidden" name="voucherFile" />
				</div>
				<div class="col-md-6"></div>
			</div>
			<div class="input-row clearfloat"></div>
			<hr />
		</div>
	</div>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/air-ticket-paid.js"></script>
</body>
</html>