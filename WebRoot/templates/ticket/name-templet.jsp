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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/upload.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css?v1.001" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.confirmed {
	font-weight: bold;
}

h2 {
	padding-left: 20px !important;
}
</style>

</head>

<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>名单模板</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: create">新建</button>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 control-label">名称</label>
						<div class="col-md-3" style="float: left">
							<input type="number" class="form-control" placeholder="模板名称" name="templet.templet_name" />
						</div>
						<button type="submit" style="float: right" class="btn btn-green" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover" id="table-main">
						<thead>
							<tr role="row">
								<th>名称</th>
								<th>创建人</th>
								<th>创建时间</th>
								<th>使用次数</th>
								<th>最近使用</th>
								<th>更新人</th>
								<th>上次更新</th>
								<th>模板</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach:templets">
							<tr>
								<td data-bind="text: $data.templet_name"></td>
								<td data-bind="text: $data.create_user_name"></td>
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD HH:mm')"></td>
								<td data-bind="text: $data.used_count"></td>
								<td data-bind="text: $data.last_used_time"></td>
								<td data-bind="text: $data.update_user_name"></td>
								<td data-bind="text: moment($data.update_time-0).format('YYYY-MM-DD HH:mm')"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.viewTemplet">预览</a>&nbsp;<a
									href="javascript:void(0)" data-bind="click:$root.download">下载</a></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.editTemplet">编辑</a>&nbsp;<a
									href="javascript:void(0)" data-bind="click:$root.deleteTemplet">删除</a></td>
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
	<div id="div-templet" style="display: none; width: 600px;">
		<form class="form-box info-form" id="form-templet">
			<input type="hidden" id="templet-pk" name="templet.pk" data-bind="value:templet().pk"/>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l">模板名称</label>
					<div class="ip">
						<input type="text" class="ip-default" id="templet-name" placeholder="模板名称" data-bind="value:templet().templet_name"
							name="templet.templet_name" maxlength="10" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l">上传模板</label> <a href="javascript:;" class="a-upload">选择模板<input type="file" required
						class="file-office" id="file-templet" accept="application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"
						id="file-upload" /></a> <input type="hidden" data-bind="value:templet().file_name" name="templet.file_name"
						id="office-file" />
				</div>
			</div>
			<div class="input-row clearfloat">
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:cancelTemplet">取消</button>
				<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:doTemplet">保存</button>
			</div>
		</form>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/file-upload.js"></script>
	<script src="<%=basePath%>static/js/file-upload-office.js"></script>
	<script src="<%=basePath%>static/js/ticket/name-templet.js"></script>
</body>
</html>