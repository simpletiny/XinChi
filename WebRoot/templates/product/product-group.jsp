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

</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品小组</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { create() }">新建</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { edit() }">编辑</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteGroup() }">删除</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>名称</th>
								<th>产品经理</th>
								<th>供应商</th>
								<th>包含员工</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: groups">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenGroups" /></td>
								<td data-bind="text: $data.group_name"></td>
								<td data-bind="text: $data.group_leader"></td>
								<td><a href="javascript:void(0)" data-bind="click: function() {$root.checkSuppliers($data.pk)} ">查看详情</a></td>
								<td data-bind="text: $root.getUsers($data.pk)"></td>
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
	<div id="supplier-panel" style="display:none;width:700px">
      <div class="input-row clearfloat"  style="width:100%">
	        <div class="col-md-12" style="width:100%">
	             <label class="l">供应商</label>
	                 <div class="ip">
	                       <div data-bind="foreach: suppliers" style="padding-top: 4px;">
	                           <em class="small-box">
	                                <label data-bind="text: $data.supplier_short_name"></label>
	                            </em>
	                        </div>
	                   </div>
	       </div>
       </div>
  </div>
	<script>
		$(".manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/product/product-group.js"></script>
</body>
</html>