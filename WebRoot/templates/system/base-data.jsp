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
					<div class="form-group">
						<div class="span6">
							<div class="col-md-2">
								<input type="text" class="form-control" maxlength="10" placeholder="产品线" id="txt-line" /> <input type="hidden"
									id="txt-pk" /> <input type="hidden" id="txt-edit" />
									 <input type="hidden" id="txt-old-name" />
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
			</div>
		</div>
	</div>
	<script>
		$(".system").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/system/base-data.js"></script>
</body>
</html>