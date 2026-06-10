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
.page-container {
	width: 1200px;
	display: flex;
	flex-wrap: wrap; /* 允许换行 */
	gap: 20px; /* 行间和列间的间距 */
	justify-content: flex-start; /* 确保每行从左侧开始排列 */
	/* 确保没有额外的 padding 或 margin */
	padding: 0;
	margin: 0 auto;
}

.box {
	width: 170px; /* 固定宽度 */
	border: 1px dashed black;
	min-height: 300px;
}

.box h2 {
	background-color: lightblue; /* 浅蓝色背景 */
	margin: 0;
	padding: 10px;
	font-size: 14px;
	font-weight:bold;
	cursor: pointer;
}
 
.child {
	height: 50px; /* 固定高度50px */
	line-height: 50px; /* 文字垂直居中 */
	padding: 0 10px;
	border-top: 1px solid #eee;
	cursor: pointer;
	min-height: 20px;
	align-items: center; 
}

.child h3 {
	margin: 0;
	padding-top:15px;
	font-size: 13px;
}

.child-title-edit{
	font-size: 11px;
	float: right;
	padding-top: 10px; 
}

.blue-background-class {
	background-color: lightblue;
	opacity: 0.5; /* 让拖动时的 ghost 元素半透明 */
}

.children {
	min-height: 200px;
}

.handle {
	cursor: move;
}

.fixed {
	font-size: 12px;
	display: none;
	position: fixed;
	right: 120px;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
	width: 100px;
}

.fixed button {
	width: 80px;
	margin-top: 5px;
	display: block;
}

.sort-num {
	padding-left: 10px;
	color: #800000
}

.a-edit {
	font-size: 12px;
	float: right;
	padding-top: 10px; 
}


</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>页面管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<div class="page-container" data-bind="foreach: pages">
					<div class="box">
						<h2 data-bind="attr:{'data-pk':$data.pk}">
							<span data-bind="text: page_title" class="title"></span> <small data-bind="text: order_index" class="sort-num"></small>
							<a href="javascript:void(0)" onclick="editFatherTitle(this)" class="a-edit">编辑</a>
						</h2>
						<div class="children" data-bind="foreach:$data.child_pages,as:child,attr: { 'data-pk': $data.pk }">
							<div class="child">
								<h3 data-bind="attr:{'data-pk':pk,'order-index':order_index}">
									<span data-bind="text: page_title" class="title"></span> <a href="javascript:void(0)"
										onclick="editChildTitle(this)" class="child-title-edit">编辑</a>
								</h3>
							</div>
						</div>
					</div>
				</div>
				<div class="fixed">
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {revoke() }">撤销</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { save() }">保存</button>
				</div>
			</div>
		</div>
	</div>

	<div id="title-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<input type="hidden" class="pk" />
			<div>
				<label class="l" style="width: 30%">名称</label>
				<div class="ip" style="width: 70%">
					<input type="text" class="form-control title" maxlength="6" />
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" onclick="cancelEditTitle()">取消</a> <a type="submit"
					onclick="confirmEditTitle()" class="btn btn-green btn-r">确定</a>
			</div>
		</div>
	</div>
	<script>
		$(".system").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/system/pages.js"></script>
</body>
</html>