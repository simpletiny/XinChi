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
				支付<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<div class="form-box info-form">
					<h3>支付信息</h3>
					<div class="input-row clearfloat">

						<div class="col-md-3">
							<label class="l">支付单号</label>
							<div class="ip" style="width: 55%">
								<p class="ip-default" data-bind="text: wfp().pay_number"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">项目</label>
							<div class="ip" style="width: 55%">
								<p class="ip-default" data-bind="text: itemMapping[wfp().item]"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">收款方</label>
							<div class="ip" style="width: 55%">
								<p class="ip-default" data-bind="text: wfp().receiver"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">总金额</label>
							<div class="ip" style="width: 55%">
								<p class="ip-default" data-bind="text: wfp().money"></p>
							</div>
						</div>
					</div>
					<!-- ko if: wfp().item == "D" -->
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l">公账账户名</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().corporate_account_name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">账户账号</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().corporate_account_number"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">开户行</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().corporate_account_bank"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l">私账账户名</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().personal_account_name"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">账户账号</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().personal_account_number"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">开户行</label>
							<div class="ip" style="width: 65%">
								<p class="ip-" data-bind="text: supplier().personal_account_bank"></p>
							</div>
						</div>
					</div>
					<!-- /ko -->
				</div>
				<form class="form-box info-form"> <!-- id="test" -->
					<input type="hidden" id="wfp_pk" value="<%=key%>" />
					<div>
						<div class="input-row clearfloat">

							<div class="col-md-6 required">
								<label class="l">支出账户</label>
								<div class="ip">
									<select class="form-control" name="account1"
										data-bind="options: accounts, optionsCaption: '-- 请选择 --',value: chosenAccount" required="required"></select>
								</div>
							</div>
							<div class="col-md-6 required">
								<label class="l">支出时间</label>
								<div class="ip">
									<input type="text" name="time1" data-bind="value:current_min" class="ip- datesecond-picker"  name="xx" placeholder="支出时间" required="required" />
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">

							<div class="col-md-6 required">
								<label class="l">收款方账户名</label>
								<div class="ip">
									<input type="text" name="receiver1" data-bind="value:supplier().personal_account_name" class="ip-" placeholder="收款方账户名" required="required" />
								</div>
							</div>
							<div class="col-md-6 required">
								<label class="l">支付金额</label>
								<div class="ip">
									<input type="number" style="width:40%" data-bind="value: defaultMoney()" name="money1" id="txt-money1" class="ip-" placeholder="支付金额"
										required="required" /> 
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-6">
								<a href="javascript:;" class="a-upload">上传凭证<input type="file" accept=".jpg,.png" required="required" name="file1" /></a> <input
									type="hidden" name="voucherFile1" />
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
							data-bind="options: accounts, optionsCaption: '-- 请选择 --'" required="required"></select>
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
						<input type="number" style="width:40%" name="money" data-bind="value: defaultMoney()"  class="ip-" id="txt-money" placeholder="支付金额" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<a href="javascript:;" class="a-upload">上传凭证<input type="file" accept=".jpg,.png" required="required" name="file" /></a> <input
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
		$(".finance").addClass("current").children("ol")
				.css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/chinese_number.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/accounting/paid.js"></script>
</body>
</html>