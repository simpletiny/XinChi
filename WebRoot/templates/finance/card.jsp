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
			<h2>账户管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">总余额</label>
							<div class="col-md-2">
								<p class="ip-default rmb" data-bind="text:sumBalance()"></p>
							</div>
						</div>
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createCard() }">新建</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { signPurpose() }">指定用途</button>
								<s:if test="#session.user.user_roles.contains('ADMIN')">
									<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { stopUse() }">停用</button>
								</s:if>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span8">
							<label class="col-md-1 control-label">关键字</label>
							<div class="col-md-6">
								<input type="text" class="form-control" placeholder="关键字">
							</div>
						</div>
						<div>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); search() }">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>账户</th>
								<th>账户id/卡号</th>
								<th>户名</th>
								<th>开户行</th>
								<th>初始金额</th>
								<th>余额</th>
								<th>种类</th>
								<th>用途</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: cards">
							<tr style="overflow: hidden">
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenCards" /></td>
								<!-- ko if: $data.delete_flg=="N"-->
								<td data-bind="text: $data.account" style="overflow: hidden"></td>
								<!--/ko  -->
								<!-- ko if: $data.delete_flg=="Y"-->
								<td data-bind="text: $data.account" style="overflow: hidden; color: red"></td>
								<!--/ko  -->
								<td data-bind="text: $data.number" style="overflow: hidden"></td>
								<td data-bind="text: $data.name" style="overflow: hidden"></td>
								<td data-bind="text: $data.bank" style="overflow: hidden"></td>
								<td data-bind="text: $data.init_money" class="rmb" style="overflow: hidden"></td>
								<td data-bind="text: $data.balance" class="rmb" style="overflow: hidden"></td>
								<td data-bind="text: $data.type" style="overflow: hidden"></td>
								<td data-bind="text: $root.cardPurposeMapping[$data.purpose]" style="overflow: hidden"></td>
								<td data-bind="text: $data.comment" title="test" style="overflow: hidden"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<div id="card-purpose" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<label class="col-md-2 control-label" style="color: red">用途</label>
			<div class="col-md-4">
				<select class="form-control" style="height: 34px" id="purpose"
					data-bind="options: cardPurposes,optionsText: 'data_value', optionsValue: 'data_key'"></select>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { doSign() }">确认</button>
		</div>
	</div>
	<script>
		$(".manager").addClass("current").children("ol")
				.css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/finance/card.js"></script>
</body>
</html>