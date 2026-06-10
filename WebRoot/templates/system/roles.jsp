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

.light-blue {
	background-color: lightblue;
}

/* 页面css */
.page-container {
	width: 100%;
	display: flex;
	flex-wrap: wrap; /* 允许换行 */
	gap: 20px; /* 行间和列间的间距 */
	justify-content: flex-start; /* 确保每行从左侧开始排列 */
	margin: 30px auto;
	justify-content: center;
}

.box {
	width: 200px; /* 固定宽度 */
	border: 1px dashed black;
	min-height: 300px;
}

.box h2 {
	background-color: lightblue; /* 浅蓝色背景 */
	margin: 0;
	padding: 10px;
	font-size: 14px;
	font-weight: bold;
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
	padding-top: 15px;
	font-size: 13px;
}

.blue-background-class {
	background-color: lightblue;
	opacity: 0.5; /* 让拖动时的 ghost 元素半透明 */
}

.children {
	min-height: 200px;
}

h3 input {
	float: right;
	padding-top: 10px;
}

.fixed {
	font-size: 12px;
	display: none;
	position: fixed;
	right: 0px;
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
.a-edit {
	font-size: 12px;
	float: right;
	padding-top: 10px; 
}
small.first-page{
	font-size: 10px;
	padding-left:10px;
	color: #007B6E; /* 深蓝绿色 */

}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>角色权限管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-roles">
					<h3>角色优先级</h3>
					（多重角色用户，用户角色由优先级高的角色决定，拖动排序）
					<div class="form-group">
						<div id="roleGrid" class="col" data-bind="foreach: user_roles">
							<div class="grid-square" data-bind="css: {'light-blue': $index() === 0},attr:{'role-code':$data.code}" onclick="changeRoles(this)">
								<span data-bind="text:$data.name"></span> 
							</div>
						</div>
					</div>
				</form>
				<div class="page-container" data-bind="foreach: pages">
					<div class="box">
						<h2 data-bind="attr:{'data-pk':$data.pk}">
							<span data-bind="text: page_title" class="title"></span><small class="first-page"></small>
							<a href="javascript:void(0)" onclick="editFirstPage(this)" class="a-edit">编辑</a>
						</h2>
						<div class="children" data-bind="foreach:$data.child_pages,as:child">
							<div class="child">
								<h3 data-bind="attr:{'data-pk':pk,'order-index':order_index}">
									<span data-bind="text: page_title,attr:{'data-pk':pk}" class="title"></span> <input data-bind="attr: {'father-pk':father_pk,'page-title':page_title,'data-pk': pk }"  type="checkbox" />
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
	<div id="first-page-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<input type="hidden" class="pk" />
			<div>
				<label class="l" style="width: 30%">首页指向</label>
				<div class="ip" style="width: 70%">
					<select class="form-control" id="sel-first-page" maxlength="6" >
					
					</select>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" onclick="cancelEditFirstPage()">取消</a> <a type="submit"
					onclick="confirmFirstPage()" class="btn btn-green btn-r">确定</a>
			</div>
		</div>
	</div>
	<script>
		$(".system").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/system/roles.js?v=1.001"></script>
</body>
</html>