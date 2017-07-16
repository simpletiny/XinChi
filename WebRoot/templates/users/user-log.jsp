<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
 <link rel="stylesheet" type="text/css" href="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.css"/>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>用户操作日志</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
			<form class="form-horizontal search-panel">
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">日期</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control date-picker" data-bind="value: today" placeholder="日期" name="userLog.time" />
							</div>
						</div>
			
							<div class="span6">
								<label class="col-md-1 control-label">用户</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" data-bind="options: user_names, optionsCaption: '全部'" name="userLog.user_name"></select>
								</div>
							</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>操作</th>
								<th>业务层</th>
								<th>时间</th>
								<th>用户</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: logs">
							<tr>
								<td data-bind="text: $data.method_des"></td>
								<td data-bind="text: $data.class_des"></td>
								<td data-bind="text: $data.time"></td>
								<td data-bind="text: $data.user_name"></td>
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
		$(".manage").addClass("current").children("ol").css("display", "block");
	</script>
	   <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	    <script src="<%=basePath %>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/users/user-log.js"></script>
</body>
</html>