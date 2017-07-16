<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
.deleteImg {
	position: relative;
	display: block;
	width: 12px;
	height: 12px;
	/* background: url(../../static/img/mc-icon-cancel.png) no-repeat; */
	background-size: cover;
	padding-top: 2px;
	z-index: 999;
	float: right;
	cursor: pointer;
}

.supplierDiv {
	width: 150px;
	height: 30px;
	float: left;
	border: solid 1px blue;
	padding: 5px 0 0 5px;
	margin-left: 10px;
	margin-top: 10px;
	font-family: 宋体
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				修改产品组<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
					<input type="hidden" id="key" value="<%=key%>" name="group.pk"></input>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">小组名</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: group().group_name" placeholder="小组名" name="group.group_name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">产品经理</label>
							<div class="ip">
								<select class="form-control" style="height: 34px" data-bind="value:group().group_leader,options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--请选择--'" name="group.group_leader" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l"><a type="submit" class="btn btn-green btn-r" data-bind="click: choseFinancial">选择供应商</a></label>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12" id="div_chosen_supplier"></div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: group().comment" name="group.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: updateGroup">保存</a>
				</div>
			</div>
		</div>
	</div>
	<div id="financial_pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 700px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">主体简称</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="主体简称" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchFinancial }">搜索</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:pickFinancial }">选择</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>财务主体简称</th>
								<th>负责人</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: suppliers">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk+';'+$data.supplier_short_name}, checked: $root.chosenSuppliers" /></td>
								<td data-bind="text: $data.supplier_short_name"></td>
								<td data-bind="text: $data.body_name"></td>
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
		$(".manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/product/group-edit.js"></script>
</body>
</html>