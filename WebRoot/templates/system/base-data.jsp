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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.grid-square {
	height: 100%;
	display: inline-block;
	border: dashed 2px rgb(0, 0, 0, 0.2);
	padding: 5px;
	margin: 7px;
	cursor: pointer;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>系统基础数据</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<h3>产品线</h3>
					（双击进行删除更新操作，拖动排序）
					<div class="form-group" style="padding-top: 20px">
						<div class="span6">
							<div class="col-md-2">
								<input type="text" class="form-control" maxlength="10" placeholder="产品线" id="txt-line" /> <input type="hidden"
									id="txt-pk" /> <input type="hidden" id="txt-edit" /> <input type="hidden" id="txt-old-name" />
							</div>
						</div>
						<div class="span6">
							<div style="padding-top: 3px;">
								<button type="submit" class="btn btn-green create" data-bind="click: function() {createLine() }">添加</button>
								<button type="submit" class="btn btn-green update" style="display: none"
									data-bind="click: function() {updateLine() }">更新</button>
								<button type="submit" class="btn btn-green update" style="display: none"
									data-bind="click: function() {deleteLine() }">删除</button>
								<button type="submit" class="btn btn-green update" style="display: none"
									data-bind="click: function() {cancelUpdate() }">取消</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div id="lineGrid" class="col" data-bind="foreach: locations">
							<div class="grid-square" style="">
								<span
									data-bind="text:$data.name,event:{dblclick:function(){$root.setLineName($data.name,$data.pk,$data.can_edit)}}"></span>
								<input type="hidden" data-bind="value:$data.pk" />
							</div>
						</div>
					</div>
				</form>
				<hr />
				<form class="form-horizontal search-panel" id="form-bad">
					<h3>呆账设置</h3>
					<div class="form-group" style="padding-top: 20px">
						<div class="span6 col-md-6">
							<label class="col-md-2 control-label">罚息系数</label>
							<div class="col-md-3">
								<select class="form-control" style="height: 34px"
									data-bind="options: badDenominator,  optionsText: 'text', optionsValue: 'value',value:chosenDenominator, event:{change:function(){changeDenominator()}}"></select>
							</div>
							<div class="col-md-2">
								<input type="number" class="form-control"
									data-bind="value:badNumerator(),event:{keyup:function(){changeDenominator()}}" maxlength="6"
									id="txt-bad-numerator" />
							</div>
							<label class="col-md-2" id="l-bad-rate"></label>
						</div>
					</div>
					<div class="form-group">
						<div class="span6 col-md-6">
							<label class="col-md-3 fix-width1 control-label"><input type="checkbox" value="AUTO" id="chk-isauto"
								data-bind="checked:isAuto()" />自动清零，每月</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: days,  optionsText: 'text', optionsValue: 'value',value:chosenDay"></select>
							</div>
							<div class="col-md-2">
								<button type="submit" class="btn btn-green create" data-bind="click: function() {cleanBadPunishment() }">立即清除</button>
							</div>
						</div>
						<div class="span6">
							<div style="padding-top: 3px;">
								<button type="submit" class="btn btn-green create" data-bind="click: function() {saveBadConfig() }">保存</button>
							</div>
						</div>
					</div>
				</form>
				<hr />
				<form class="form-horizontal search-panel" id="form-team">
					<h3>单团核算相关</h3>
					<div class="form-group" style="padding-top: 20px">
						<div class="span6 col-md-6">
							<label class="col-md-3 control-label">销售费用（元/分）</label>
							<div class="col-md-2">
								<input type="number" class="form-control" data-bind="value:saleCost()" maxlength="6" id="txt-sale-cost" />
							</div>
							<label class="col-md-3 control-label">后台费用（元/人）</label>
							<div class="col-md-2">
								<input type="number" class="form-control" data-bind="value:sysCost()" maxlength="6" id="txt-sys-cost" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6  col-md-6">
							<div style="padding-top: 3px; float: right">
								<button type="submit" class="btn btn-green create" data-bind="click: function() {saveTeamConfig() }">保存</button>
							</div>
						</div>
					</div>
				</form>
				<hr />
				<form class="form-horizontal search-panel" id="form-team">
					<h3>销售信用额度</h3>
					<div class="form-group" style="padding-top: 20px">
						<div class="span6 col-md-3">
							<input name="relation.sort_type" type="radio" value="Y" data-bind="checked:saleCreditFlg(),event:{click:function(){changeSaleCredit('Y');return true;}}" /><label
								class="control-label">启用</label>
						</div>
						<div class="span6 col-md-3">
							<input name="relation.sort_type" type="radio" value="N" data-bind="checked:saleCreditFlg(),event:{click:function(){changeSaleCredit('N');return true;}}" /><label
								class="control-label">停用</label>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<div id="div-clear" style="display: none; width: 400px;">
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<div class="ip">
					<p class="ip-default">选择清除日，清除此日（含）之前的罚息累计。</p>
				</div>
			</div>
			<div class="col-md-12" style="margin-top: 20px">
				<label class="l" style="width: 30%">清除日</label>
				<div class="ip" style="width: 70%">
					<input type="text" maxlength="10" data-bind="value:today()" id="txt-clean-date" class="form-control date-picker"
						required="required" />
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-12" style="margin-top: 10px">
				<div align="right">
					<a type="button" class="btn btn-green btn-r" data-bind="click: confirmCleanBadInterest">确认</a> <a type="button"
						class="btn btn-green btn-r" data-bind="click: cancelCleanBadInterest">取消</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".system").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/system/base-data.js?v=1.0"></script>
</body>
</html>