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
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<input type="hidden" id="client_key" value="<%=key%>" />
		<div class="subtitle">
			<h2>
				新增拜访<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">客户</label>

							<div class="ip">
								<p class="ip-default" data-bind="text: visit().client_employee_name"></p>
								<input type="hidden" class="ip-default" data-bind="value: visit().client_employee_name"
									name="visit.client_employee_name" required="required" /> <input type="text" class="ip-"
									data-bind="value: visit().client_employee_pk" style="display: none" name="visit.client_employee_pk"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<div class="ip required" style="width: 30%">
								<label class="l">日期</label> <input type="text" style="width: 50%"
									class="ip-default date-picker" data-bind="value: visit().date"  name="visit.date"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">主要目的</label>
							<div class="ip">
								<select class="form-control" data-bind="options: target,optionsCaption: '-- 请选择--'" name="visit.target"
									required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">辅助目的</label>
							<div class="ip">
								<div data-bind="foreach: target" style="padding-top: 4px;">
									<em class="small-box"> <input type="checkbox"
										data-bind="attr: {'value': $data},checked: $root.chosenSubTargets" /><label data-bind="text: $data"></label>
									</em>
								</div>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12 required">
							<label class="l">效果评估</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" maxlength="500" data-bind="value: visit().summary"
									name="visit.summary" placeholder="效果评估" required="required"></textarea>
							</div>
						</div>
					</div>
				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" id="btn_save" data-bind="click: saveVisit">保存</a>
				</div>
			</div>
		</div>
	</div>
	<%-- <div id="client-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="client_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchClientEmployee }">搜索</button>
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
						<tbody data-bind="foreach: clientEmployees">
							<tr data-bind="event: {dblclick: function(){ $parent.pickClientEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
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
	</div> --%>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/visit-creation.js"></script>
</body>
</html>