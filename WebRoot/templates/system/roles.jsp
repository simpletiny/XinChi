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
			<h2>角色权限管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-roles">
					<h3>角色优先级</h3>
					（多重角色用户，用户角色由优先级高的角色决定，拖动排序）
					<div class="form-group">
						<div id="roleGrid" class="col" data-bind="foreach: user_roles">
							<div class="grid-square" style="">
								<span
									data-bind="text:$data.name,event:{dblclick:function(){$root.setLineName($data.name,$data.pk,$data.can_edit)}}"></span>
								<input type="hidden" data-bind="value:$data.pk" />
							</div>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
	<script>
		$(".system").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/system/roles.js"></script>
</body>
</html>