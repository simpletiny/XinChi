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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />

		<div class="subtitle">
			<s:if test='#parameters.key[0]=="R"'>
				<h2>
					新建收入<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
						class="ic-cancel"></i>取消</a>
				</h2>
			</s:if>
			<s:else>
				<h2>
					新建支出<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
						class="ic-cancel"></i>取消</a>
				</h2>
			</s:else>
		</div>

		<div class="main-container">
			<div class="main-box">
				<input type="hidden" id="key" value="<%=key%>" />
				<form class="form-box info-form">

					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">账户</label>
							<div class="ip">
								<select class="form-control"
									data-bind="options: cards, optionsCaption: '-- 请选择 --',optionsText:'account',optionsValue:'account', value: detail().account"
									name="payment_detail.account" required="required"></select>
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">发生时间</label>
							<div class="ip">
								<input type="text" id="name" class="ip- datesecond-picker" placeholder="发生时间" name="payment_detail.time"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<s:if test='#parameters.key[0]=="R"'>
							<div class="col-md-6 required">
								<label class="l">收入金额</label>
								<div class="ip">
									<input type="number" id="txt-money" class="ip-" placeholder="收入金额" name="payment_detail.money"
										required="required" />
								</div>
							</div>
						</s:if>
						<s:else>
							<div class="col-md-6 required">
								<label class="l">支出金额</label>
								<div class="ip">
									<input type="number" id="txt-money" class="ip-" placeholder="支出金额" name="payment_detail.money"
										required="required" />
								</div>
							</div>
						</s:else>
						<s:if test='#parameters.key[0]=="R"'>
							<div class="col-md-6 required">
								<label class="l">付款方</label>
								<div class="ip">
									<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="supplier-employee-name"
										required="required" /> <input type="text" class="ip-" data-bind="click:choseFinancial" style="display: none"
										name="payment_detail.receiver_pk" id="supplier-employee-pk" required="required" />
								</div>
							</div>
						</s:if>
						<s:else>
							<div class="col-md-6 required">
								<label class="l">收款方</label>
								<div class="ip">
									<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="supplier-employee-name"
										required="required" /> <input type="text" class="ip-" data-bind="click:choseFinancial" style="display: none"
										name="payment_detail.receiver_pk" id="supplier-employee-pk" required="required" />
								</div>
							</div>
						</s:else>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传凭证<input type="file" required="required"
								accept="image/jpeg,image/png" name="file2" /></a> <input type="hidden" name="payment_detail.voucher_file_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" name="payment_detail.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>

				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createDetail">提交</a>
				</div>
			</div>
		</div>
	</div>
	<!-- 选择供应商 -->
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
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/ticket/ticket-detail-creation.js"></script>
</body>
</html>