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

</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>销售信用额度</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green" data-bind="click: function() {editCreditLimit() }">修改信用额度</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th><input type="checkbox" id="chk-all" onclick="checkAll(this)" /></th>
								<th>姓名</th>
								<th>昵称</th>
								<th>员工号</th>
								<th>性别</th>
								<th>手机号</th>
								<th>角色</th>
								<th>信用额度</th>
								<th>信用余额</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: users">

							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+','+$data.user_name}, checked: $root.chosenUsers" /></td>
								<td data-bind="text: $data.user_name"></td>
								<td data-bind="text: $data.nick_name"></td>
								<td data-bind="text: $data.user_number"></td>
								<td data-bind="text: $data.id"></td>
								<td data-bind="text: $data.cellphone"></td>
								<td data-bind="text: $data.user_roles"></td>
								<td data-bind="text: $data.credit_limit" class="rmb"></td>
								<td data-bind="text: $data.credit_balance" class="rmb"></td>
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
	<div style="display: none; width: 750px" id="edit-credit">
		<div class="input-row clearfloat" data-bind="foreach:chosenUserNames()">
		<label class="l" data-bind="text:$data"></label>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12 required">
				<label class="l">信用额度</label>
				<div class="ip">
					<div class="ip">
						<input type="number" id="credit-limit" class="ip-default" placeholder="信用额度" />
					</div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button style="margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:doSaveCreditLimit">保存</button>
			<button style="margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:cancelSaveCreditLimit">取消</button>
		</div>
	</div>
	<script>
		$(".user").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/users/sale-credit.js"></script>
</body>
</html>