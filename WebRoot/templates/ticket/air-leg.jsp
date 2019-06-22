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

.tab {
	float: left;
	padding-top: 20px;
}

.tab label {
	font-size: 18px;
	font-weight: 500 !important;
	line-height: 1.2;
}
</style>
<jsp:include page="../layout.jsp" />
</head>
<body>
	<div class="main-body">
		<div class="subtitle" style="float: left">
			<h2 style="width: 30%; float: left">票务航段</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {createLeg() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {editLeg() }">编辑</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {deleteLeg() }">删除</button>
							<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {onlyTicket() }">单售票</button> -->
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">出发城市</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" placeholder="出发城市" name="leg.from_city" />
							</div>
							<label class="col-md-1 control-label">抵达城市</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" placeholder="抵达城市" name="leg.to_city" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" st="btn-search" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" style="width: 50%">
						<thead>
							<tr role="row">
								<th></th>
								<th>序号</th>
								<th>出发城市</th>
								<th>抵达城市</th>
								<th>常用标识</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: legs">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenLegs" /></td>
								<td data-bind="text: $index()+1"></td>
								<td data-bind="text: $data.from_city"></td>
								<td data-bind="text: $data.to_city"></td>
								<td><select
									data-bind="options: $root.hots,value:$data.hot_flg, optionsText:function(hot){return $root.hotMapping[hot]},event: {change:$root.switchHot}"></select></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat" style="width: 50%">
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

	<div id="air-leg" style="display: none; width: 800px; height: 260px; padding-top: 30px; overflow: auto">
		<form id="leg-form">
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">出发城市</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="leg.from_city" class="form-control" data-bind="value:leg().from_city" maxlength="20"
							placeholder="出发城市" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">抵达城市</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="leg.to_city" class="form-control" data-bind="value:leg().to_city" maxlength="20"
							placeholder="抵达城市" required="required" />
					</div>
				</div>
			</div>
		</form>
		<div class="input-row clearfloat">
			<div style="padding-top: 3px; float: right">
				<button type="submit" class="btn btn-green" data-bind="click: doCreate">保存</button>
				<button type="submit" class="btn btn-green " data-bind="click: cancelCreate">取消</button>
			</div>
		</div>
	</div>
	<div id="air-leg-edit" style="display: none; width: 800px; height: 260px; padding-top: 30px; overflow: auto">
		<form id="leg-edit-form">
			<input type="hidden" data-bind="value:leg().pk" name="leg.pk" />
			<div class="input-row clearfloat">
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">出发城市</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="leg.from_city" class="form-control" data-bind="value:leg().from_city" maxlength="20"
							placeholder="出发城市" required="required" />
					</div>
				</div>
				<div class="col-md-6 required">
					<label class="l" style="width: 30%">抵达城市</label>
					<div class="ip" style="width: 70%">
						<input type="text" name="leg.to_city" class="form-control" data-bind="value:leg().to_city" maxlength="20"
							placeholder="抵达城市" required="required" />
					</div>
				</div>
			</div>
		</form>
		<div class="input-row clearfloat">
			<div style="padding-top: 3px; float: right">
				<button type="submit" class="btn btn-green" data-bind="click: doUpdate">更新</button>
				<button type="submit" class="btn btn-green " data-bind="click: cancelEdit">取消</button>
			</div>
		</div>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/air-leg.js"></script>
</body>
</html>