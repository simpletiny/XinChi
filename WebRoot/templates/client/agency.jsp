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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>旅游公司</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createAgency() }">新建</button>
							</div>
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editAgency() }">编辑</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">公司全称</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="公司全称" name="agency.agency_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">信用代码</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="信用代码" name="agency.credit_code" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">地区</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: provices, optionsCaption: '-- 省份--',value: agency().agency_provice,event:{change:ter}" name="agency.agency_provice"></select>
							</div>
						</div>
						<div class="span6">
							<div class="col-md-2">
								<select class="form-control" id="city" name="agency.agency_city"></select>
							</div>
						</div>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">类型</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: agencyType, optionsCaption: '-- 请选择--',value: agency().agency_type" name="agency.agency_type" required="required"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">出境</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: isExit, optionsCaption: '-- 请选择 --',value: agency().is_exit" name="agency.is_exit"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">主营</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: mainBus, optionsCaption: '-- 请选择--',value: agency().main_bussines" name="agency.main_bussines" required="required"></select>
							</div>
						</div>
						<div class="span6">
							<em class="small-box"> <label>注销</label> <input type="checkbox" name="agency.is_cancel" value="Y" />
							</em>
						</div>
					</div>
					<div class="form-group">
							<button  style="float:right" type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>主营</th>
								<th>财体数量</th>
								<th>公司全称</th>
								<th>所属地区</th>
								<th>公司类型</th>
								<th>出境</th>
								<th>法人姓名</th>
								<th>核验日期</th>
								<th>操作</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: agencys">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenAgencys" /></td>
								<td data-bind="text: $data.main_bussines"></td>
								<td data-bind="text: $data.agency_client_count"></td>
								<td><a href="javascript:void(0)" data-bind="text: $data.agency_name,attr: {href: 'agency-detail.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.agency_city"></td>
								<td data-bind="text: $data.agency_type"></td>
								<td data-bind="text:$root.isMapping[$data.is_exit]"></td>
								<td data-bind="text: $data.corporate_name"></td>
								<td>- -</td>
								<!-- ko if: $data.is_cancel == 'Y' -->
								<td style="color:red">已注销</td>
								<!-- /ko -->
								<!-- ko if: $data.is_cancel == 'N' -->
								<td><a href="javascript:void(0)" data-bind="event: {click:function(){$root.cancel($data.pk)}}">注销</a></td>
								<!-- /ko -->
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
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/client/agency.js"></script>
</body>
</html>