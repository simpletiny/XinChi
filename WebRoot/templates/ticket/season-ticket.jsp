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

.tab {
	float: left;
	padding-top: 20px;
}

.tab label {
	font-size: 18px;
	font-weight: 500 !important;
	line-height: 1.2;
}
</style>
<jsp:include page="../layout.jsp" />
</head>
<body>
	<div class="main-body">
		<div class="subtitle" style="float: left">
			<h2 style="width: 30%; float: left">套票管理</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="width: 30%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {createSeasonTicket() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {editLeg() }">编辑</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {deleteLeg() }">删除</button>
							<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {onlyTicket() }">单售票</button> -->
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">名称</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" placeholder="名称" name="base.name" />
							</div>
							<label class="col-md-1 control-label">城市</label>
							<div class="col-md-2" style="float: left">
								<input type="text" class="form-control" placeholder="城市" name="base.city" />
							</div>
						</div>
						<div style="padding-top: 3px;">
							<button type="submit" class="btn btn-green col-md-1" st="btn-search" data-bind="click: refresh">搜索</button>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>名称</th>
								<th>编号</th>
								<th>票源</th>
								<th>成人/儿童</th>
								<th>备注</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: bases">
							<tr>
								<th><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenBases" /></th>
								<th data-bind="text: $data.name"></th>
								<th data-bind="text: $data.model"></th>
								<th data-bind="text: $data.model"></th>
								<th data-bind="text: $data.price+'/'+$data.special_price"></th> 
								<th data-bind="text: $data.comment"></th>
							</tr>
							<tr>
								<td colspan="6">
									<table class="table"> 
										<thead>
											<td>&nbsp;</td>
											<td>&nbsp;</td>
											<td>序号</td>
											<td>航段</td>
											<td>天次</td>
											<td>航班号</td>
											<td>票务航段</td>
											<td>起飞时间</td>
											<td>降落时间</td>
											<td>起飞地</td>
											<td>降落地</td>
										</thead>
										<tbody data-bind="foreach: {data:$data.infos, as: 'info'}">
											<tr>
												<td>&nbsp;</td>
												<td>&nbsp;</td>
												<td data-bind="text: info.ticket_index"></td>
												<td data-bind="text: info.index_leg"></td>
												<td data-bind="text: info.start_day"></td>
												<td data-bind="text: info.ticket_number"></td>
												<td data-bind="text: info.ticket_leg"></td>
												<td data-bind="text: info.start_time"></td>
												<td data-bind="text: info.end_time"></td>
												<td data-bind="text: info.start_place"></td>
												<td data-bind="text: info.end_place"></td>
											</tr>
										</tbody>
									</table>
								</td>
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
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/ticket/season-ticket.js"></script>
</body>
</html>