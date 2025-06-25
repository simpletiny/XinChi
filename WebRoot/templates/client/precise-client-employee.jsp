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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.table-container {
	display: flex;
	align-items: flex-start;
}

/* 左边表格区域 */
.left-table {
	flex: 1;
	min-width: 0; /* 防止超宽时溢出 */
}

/* 右边“已选择”区域 */
.right-selected {
	width: 200px;
	margin-left: 20px;
	background-color: #f8f8f8;
	border: 1px solid #ddd;
	padding: 10px;
	box-sizing: border-box;
	border-radius: 4px;
}

.right-selected .selected-title {
	font-weight: bold;
	margin-bottom: 10px;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>精准客户</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form id="form-search" class="form-horizontal search-panel">

					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createPreciseEmployee() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editPreciseEmployee() }">编辑</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { bindingEmployee() }">关联</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteEmployee()  }">删除</button>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">姓名</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="姓名" maxlength="20" name="employee.name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">地区</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: employeeArea, optionsCaption: '-- 请选择 --'" name="employee.employee_area"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">手机号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="手机号" maxlength="20" name="employee.cellphone" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">微信号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="微信号" maxlength="20" name="employee.wechat" />
							</div>
						</div>
						<div class="span6">
							<div style="padding-top: 3px;">
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>头像</th>
								<th>姓名</th>
								<th>实名认证</th>
								<th>性别</th>
								<th>类型</th>
								<th>地区</th>
								<th>财务主体简称</th>
								<th>手机号</th>
								<th>微信号</th>
								<th>备注</th>
								<th>本人照片</th>
								<th>其他图像</th>
								<th>关联状态</th>
								<th>关联个数</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: employees">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data, checked: $root.chosenEmployees" /></td>
								<td><img style="width: 25px; height: 25px" data-bind="click: function() {$parent.checkHeadPhoto($data.head_photo)}"
									src="<%=basePath%>static/img/head.jpg" /><input type="hidden" st="st-file-name" data-bind="value:$data.head_photo" /></td>
								<td><a href="javascript:void(0)" data-bind="text: $data.name,attr: {href: 'employee-detail.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.is_verified==='Y'? '已认证' : '未认证',style:{color:$data.is_verified==='Y'?'green':'grey'}"></td>
								<td data-bind="text: $data.gender==='M'?'男':'女'"></td>
								<td data-bind="text: $data.type"></td>
								<td data-bind="text: $data.employee_area"></td>

								<td><a href="javascript:void(0)"
									data-bind="text: $data.financial_body_name,click:function(){ $root.checkFinancialBody($data.financial_body_pk);}"></a></td>
								<td data-bind="text: $data.cellphone"></td>
								<td data-bind="text: $data.wechat"></td>
								<!-- ko if: $data.comment==null || $data.comment==''-->
								<td><a href="javascript:void(0)" data-bind="click:function() {$root.editComment($data.pk)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.comment!=null && $data.comment!=''-->
								<td data-bind="attr:{title:$data.comment}"><a href="javascript:void(0)"
									data-bind="text: $data.comment,click:function() {$root.editComment($data.pk)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.self_photo==null || $data.self_photo==''-->
								<td>无</td>
								<!-- /ko -->
								<!-- ko if: $data.self_photo!=null && $data.self_photo!=''-->
								<td><a href="javascript:void(0)" data-bind=" click:function() {$root.checkPhoto($data.self_photo,'self')}">查看</a></td>
								<!-- /ko -->
								<!-- ko if: $data.other_photo==null || $data.other_photo==''-->
								<td>无</td>
								<!-- /ko -->
								<!-- ko if: $data.other_photo!=null && $data.other_photo!=''-->
								<td><a href="javascript:void(0)" data-bind=" click:function() {$root.checkPhoto($data.other_photo,'other')}">查看</a></td>
								<!-- /ko -->
								<td data-bind="text: $data.binding_status==='Y'? '已关联' : '未关联',style:{color:$data.binding_status==='Y'?'green':'red'}"></td>
								<td data-bind="text: $data.binding_count"></td>
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
	<div id="comment-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div>
				<label class="l">备注</label>
				<div class="ip">
					<textarea type="text" class="ip-default" rows="10" maxlength="100" id="txt-comment" data-bind="value: clientEmployee().comment" placeholder="备注"></textarea>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: cancelEditComment">取消</a> <a type="submit" class="btn btn-green btn-r"
					data-bind="click: updateComment">保存</a>
			</div>
		</div>
	</div>
	<div id="check-financial" style="display: none">
		<div class="clearfloat" style="width: 100%">
			<div class="col-md-12">
				<label class="l">财务主体</label>
				<div class="ip">
					<p class="ip-default" data-bind="text: financial().client_short_name"></p>
				</div>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">全称</label>
				<p class="ip-default" data-bind="text: financial().client_name"></p>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">负责人</label>
				<p class="ip-default" data-bind="text: financial().body_name"></p>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">地址</label>
				<p class="ip-default" data-bind="text: financial().address"></p>
			</div>
		</div>
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<div id="client-employee-pick" style="display: none; width: 1250px; height: 800px; overflow-y: auto">
		<form id="form-search-employee" class="form-horizontal search-panel">
			<div class="form-group">
				<div class="input-row clearfloat">
					<label class="col-md-1 control-label">姓名</label>
					<div class="col-md-4">
						<input type="text" name="employee.name" class="form-control" placeholder="姓名" />
					</div>
					<label class="col-md-1 control-label">昵称</label>
					<div class="col-md-4">
						<input type="text" name="employee.nick_name" class="form-control" placeholder="姓名" />
					</div>
				</div>
				<div class="input-row clearfloat">
					<label class="col-md-1 control-label">地区</label>
					<div class="col-md-4">
						<select class="form-control" data-bind="options: employeeArea, optionsCaption: '-- 请选择 --'" name="employee.employee_area"></select>
					</div>
				</div>
				<div class="input-row clearfloat">
					<div data-bind="foreach: filters">
						<em class="small-box "> <input type="checkbox" data-bind="checkedValue:$data.key,checked:$root.chosenFilters" /><label
							data-bind="text: $data.value"></label>
						</em>
					</div>
				</div>
				<div class="input-row clearfloat">
					<button type="submit" class="btn btn-green col-md-1" style="float: right" data-bind="event:{click:searchClientEmployee }">搜索</button>
				</div>
			</div>
		</form>
		<div class="table-container">
			<div class="left-table">
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>昵称</th>
								<th>姓名</th>
								<th>性别</th>
								<th>类型</th>
								<th>状态</th>
								<th>市</th>
								<th>区</th>
								<th>财务主体简称</th>
								<th>手机号</th>
								<th>微信号</th>
								<th>备注</th>
								<th>所属销售</th>
								<th>是否关联</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: clientEmployees">
							<tr>
								<td><input type="checkbox" data-bind="checkedValue:$data,checked: $root.chosenClientEmployees" /></td>
								<td data-bind="text: $data.nick_name"></td>
								<td><a href="javascript:void(0)" data-bind="text: $data.name,attr: {href: 'employee-detail.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.sex"></td>
								<td data-bind="text: $data.type"></td>
								<td data-bind="text:$data.delete_flg==='Y'?'停用':'正常',style:{color:$data.delete_flg==='Y'?'red':'green'}"></td>
								<td data-bind="text: $data.employee_area"></td>
								<td data-bind="text: $data.employee_county"></td>

								<!-- ko if:$data.body_public_flg=='Y' -->
								<td><a style="color: red" href="javascript:void(0)"
									data-bind="text: $data.financial_body_name,click:function(){ $root.checkFinancialBody($data.financial_body_pk);}"></a></td>
								<!-- /ko -->
								<!-- ko if:$data.body_public_flg!='Y' -->
								<td><a href="javascript:void(0)"
									data-bind="text: $data.financial_body_name,click:function(){ $root.checkFinancialBody($data.financial_body_pk);}"></a></td>
								<!-- /ko -->
								<td data-bind="text: $data.cellphone"></td>
								<td data-bind="text: $data.wechat"></td>
								<td data-bind="text: $data.comment,attr:{title:$data.comment}"></td>
								<!-- ko if:$data.public_flg =='Y' -->
								<td data-bind="text: $data.sales_name" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.public_flg =='N' -->
								<td data-bind="text: $data.sales_name"></td>
								<!-- /ko -->
								<td data-bind="text:$data.binding_status==='Y'?'已关联':'未关联',style:{color:$data.binding_status==='Y'?'green':'red'}"></td>
							</tr>
						</tbody>
					</table>

					<div class="pagination clearfloat" style="text-align: left !important">
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
			<div class="right-selected">
				<div class="selected-title">已选择</div>
				<ul data-bind="foreach:chosenClientEmployees">
					<!-- 你动态添加的选中项 -->
					<li><span data-bind="text: $data.name"></span> <a title="删除" href="javascript:void(0)" style="cursor: pointer;"
						data-bind="click: $parent.removeClientEmployee" style="margin-left: 10px;">×</a></li>
				</ul>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<button type="submit" class="btn btn-green" data-bind="event:{click:cancelBinding }">取消</button>
			<button type="submit" class="btn btn-green" data-bind="event:{click:doBinding }">确定</button>
		</div>
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/precise-client-employee.js"></script>
</body>
</html>