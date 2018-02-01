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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/multiple-select/multiple-select.css" />
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
			<h2>客户财务主体管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 40%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createCompany() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editCompany() }">编辑</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); stopCompany() }">停用</button>
							<s:if test="#session.user.user_roles.contains('ADMIN')">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); deleteCompany() }">删除</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: changeSales">调整销售</button>
							</s:if>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">简称</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="简称" name="client.client_short_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">地区</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: clientArea, optionsCaption: '-- 请选择 --',event:{change:refresh}" name="client.client_area"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">门店</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: storeTypes, optionsCaption: '-- 请选择 --',event:{change:refresh}" name="client.store_type"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">主营</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: mainBusinesses, optionsCaption: '-- 请选择 --',event:{change:refresh}" name="client.main_business"></select>
							</div>
						</div>

						
					</div>
					<div class="form-group">
						<div class="span6 col-md-3">
							<label class="col-md-1 control-label">&nbsp;</label>
							<div data-bind="foreach: relates">
								<em class="small-box "> <input type="checkbox" name="client.relate_flgs"
									data-bind="attr: {'value': $data}, checked: $root.chosenRelates,event:{click:$root.changeRelate}" /><label
									data-bind="text: $root.relatesMapping[$data]"></label>
								</em>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">回款誉</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: backLevels, optionsCaption: '-- 请选择 --',event:{change:refresh}" name="client.back_level"></select>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'pk', optionsCaption: '--全部--',event:{change:refresh}"
										name="client.sales"></select>
								</div>
							</div>
							<div class="span6">
								<div class="col-md-2">
									<em class="small-box "> <input type="checkbox" value="Y" id="txt-public-flg" name="client.public_flg"
										data-bind="event:{click:function(){refresh();return true;}}" /><label>公开</label>
									</em>
								</div>
							</div>
						</s:if>
						
						
					</div>
					<div class="form-group">
					<label class="col-md-1 control-label">地址</label>
							<div class="col-md-6">
								<input type="text" class="form-control" placeholder="地址" name="client.address" />
							</div>
					<div class="span6">
							<div data-bind="foreach: status" class="col-md-2">
								<em class="small-box "> <input name="client.statuses" type="checkbox"
									data-bind="attr: {'value': $data}, checked: $root.chosenStatus,event:{click:$root.changeStatus}" /><label
									data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					<div class="span6" style="float: right">
							<div style="padding-top: 3px;">
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<td width="11.11%">总数</td>
								<td width="11.11%" data-bind="text:totalCompanies()"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
								<td width="11.11%"></td>
							</tr>
						</thead>
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>简称</th>
								<th>门店</th>
								<th>主营</th>
								<th>回款誉</th>
								<th>地区</th>
								<th>状态</th>
								<th>类型</th>
								<th>负责人</th>
								<th>关联</th>
								<th>地址</th>
								<th>客户数量</th>
								<th>应收总计</th>
								<th>年度订单</th>
								<th>最近订单</th>
								<th>所属销售</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: clients">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenCompanies" /></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.client_short_name,attr: {href: 'company-detail.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.store_type"></td>
								<td data-bind="text: $data.main_business"></td>
								<td data-bind="text: $data.back_level"></td>
								<td data-bind="text: $data.client_area"></td>
								<!-- ko if:$data.delete_flg =='Y' -->
								<td style="color: red">停用</td>
								<!-- /ko -->

								<!-- ko if:$data.delete_flg =='N' -->
								<td style="color: green">正常</td>
								<!-- /ko -->

								<td data-bind="text: $data.client_type"></td>
								<td data-bind="text: $data.body_name"></td>
								<!-- ko if:$data.relate_flg =='N' -->
								<td style="color: red">未关联</td>
								<!-- /ko -->

								<!-- ko if:$data.relate_flg =='Y' -->
								<td style="color: blue"><a href="javascript:void(0)"
									data-bind="attr: {href: 'agency-detail.jsp?key='+$data.agency_pk}">已关联</a></td>
								<!-- /ko -->

								<td data-bind="text: $data.address"></td>
								<td data-bind="text: $data.client_employee_count"></td>
								<td class="rmb" data-bind="text: $data.sum_balance"></td>
								<td data-bind="text: $data.client_year_order_count"></td>
								<td data-bind="text: $data.last_order_date"></td>

								<!-- ko if:$data.public_flg =='Y' -->
								<td data-bind="text: $data.sales_name" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.public_flg =='N' -->
								<td data-bind="text: $data.sales_name"></td>
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
	<div id="edit-sale" style="display: none">
		<input type="hidden" id="current-pk" />
		<div class="input-row clearfloat">
			<!-- <div class="col-md-12">
				说明：改变客户财务主体销售，会一并将此财务主体下的客户员工移至新分配销售下。如果新销售下存在同名财务主体，那么只会将客户员工移至新销售，此财务主体停用，并以新销售下已存在的同名财务主体为准，将涉及客户员工所属财务主体改为此财务主体。
			</div> -->
			<div class="col-md-12 required">
				<label class="l">选择销售</label>
				<div class="ip">
					<div data-bind="foreach: sales" style="padding-top: 4px;">
						<em class="small-box"> <input type="radio" name="choosenUser"
							data-bind="attr: {'value': $data.pk}, checked: $root.chosenUser" /> <!-- ko if: $data.user_name =='公开' --> <label
							style="color: red" data-bind="text: $data.user_name"></label> <!-- /ko --> <!-- ko if: $data.user_name !='公开' -->
							<label data-bind="text: $data.user_name"></label> <!-- /ko -->
						</em>
					</div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<a type="submit" class="btn btn-green btn-r" data-bind="click: doChangeSale">保存</a> <a type="submit"
				class="btn btn-green btn-r" data-bind="click: doCancelChangeSale">取消</a>
		</div>
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/multiple-select/jquery.multiple.select.js"></script>
	<script src="<%=basePath%>static/js/client/company.js"></script>
</body>
</html>