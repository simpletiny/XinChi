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
			<h2>售前必知</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {createView() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {deleteView() }">删除</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {labelManager() }">标签管理</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: labels" style="padding-top: 4px;">
								<em class="small-box"> <input type="radio"
									data-bind="attr: {'value': $data.label_name},checked:$root.chosenLabel,click:function(){$root.refresh();return true;}"
									name="view.label" /><label data-bind="text: $data.label_name"></label>
								</em>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>标题</th>
								<th>作者</th>
								<th>创建时间</th>
								<th>最后更新</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: views">
							<tr style="overflow: hidden">
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenViews" /></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.title,attr: {href: 'product-research-view-preview.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.create_user"></td>
								<td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD HH:mm')"></td>
								<td data-bind="text: moment($data.update_time-0).format('YYYY-MM-DD HH:mm')"></td>
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
	<div style="display: none; width: 500px" id="label-manager">
		<div class="form-group">
			<div id="labelGrid" class="col" data-bind="foreach: labels">
				<div class="grid-square" data-bind="event:{dblclick:function(){$root.setLabelName($data.label_name)}}">
					<span data-bind="text:$data.label_name"></span> <input type="hidden" data-bind="value:$data.pk" />
				</div>
			</div>
		</div>
		<div class="form-group">
			<div class="span6">
				<div class="col-md-6">
					<input type="text" class="form-control" maxlength="10" placeholder="标签" id="txt-label" />
				</div>
			</div>
			<div class="span6" style="float: right">
				<div style="padding-top: 3px;">
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {createLabel() }">保存</button>
					<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {deleteLabel() }">删除</button>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/sortable/Sortable.js"></script>
	<script src="<%=basePath%>static/js/culture/product-research-view.js?v=1.001"></script>

</body>
</html>