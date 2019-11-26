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
#table-ticket th, #table-ticket td {
	text-align: center;
}

#table-ticket tr td input {
	width: 90%;
}

#table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
}

#air-ticket input[type="button"] {
	width: 30px;
	font-weight: bold;
	font-size: 20px;
}

.required th[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
}

tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>产品管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<div style="width: 50%; float: right">
							<button type="submit" class="btn btn-green col-md-1"
								data-bind="click: function() { uploadClientConfirmTemplet() }">组团确认</button>
							<button type="submit" class="btn btn-green col-md-1"
								data-bind="click: function() { uploadOutNoticeConfirmTemplet() }">出团通知</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { flightManagement() }">机票维护</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { supplierManagement() }">地接维护</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { localManagement() }">本地维护</button>
						</div>
					</div>
					<div class="form-group">
						<div class="col-md-6">
							<div data-bind="foreach: status" style="padding-top: 4px; display: none">
								<em class="small-box"> <input type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatuses,click:function(){$root.refresh();return true;}"
									name="product.statuses" /><label data-bind="text: $root.saleMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">产品编号</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品编号" name="product.product_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="product.name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">产品线</label>
							<div class="col-md-2" style="float: left">
								<select class="form-control" style="height: 34px"
									data-bind="options: locations,optionsText:'name',optionsValue:'name',value:product().location, optionsCaption: '--请选择--',event:{change:refresh}"
									name="product.location"></select>
							</div>
						</div>
					</div>
					<div class="form-group">
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">产品经理</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: users,  optionsText: 'user_name', optionsValue: 'user_number',, optionsCaption: '--全部--'"
										name="product.product_manager"></select>
								</div>
							</div>
						</s:if>
						<div style="width: 30%; float: right">
							<div>
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
							</div>
						</div>
					</div>
				</form>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>产品编号</th>
								<th>名称</th>
								<th>型号</th>
								<th>天数</th>
								<th>组团确认</th>
								<th>出团通知</th>
								<th>机票维护</th>
								<th>地接维护</th>
								<th>本地维护</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>产品经理</th>
								</s:if>
							</tr>
						</thead>
						<tbody data-bind="foreach: products">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenProducts" /></td>
								<td data-bind="text: $data.product_number"></td>
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.product_model"></td>
								<td data-bind="text: $data.days"></td>
								<!-- ko if: $data.client_confirm_templet == "default"-->
								<td style="color: red">未维护</td>
								<!-- /ko -->
								<!-- ko if: $data.client_confirm_templet == "no"-->
								<td>无确认</td>
								<!-- /ko -->
								<!-- ko if: $data.client_confirm_templet != "no" && $data.client_confirm_templet != "default"-->
								<td style="color: green">已维护</td>
								<!-- /ko -->

								<!-- ko if: $data.out_notice_templet == "default"-->
								<td style="color: red">未维护</td>
								<!-- /ko -->
								<!-- ko if: $data.out_notice_templet == "no"-->
								<td>无通知</td>
								<!-- /ko -->
								<!-- ko if: $data.out_notice_templet != "no" && $data.out_notice_templet != "default"-->
								<td style="color: green">已维护</td>
								<!-- /ko -->

								<!-- ko if: $data.air_ticket_upkeep_flg == "N"-->
								<td style="color: red">未维护</td>
								<!-- /ko -->
								<!-- ko if: $data.air_ticket_upkeep_flg == "D"-->
								<td>无机票</td>
								<!-- /ko -->
								<!-- ko if: $data.air_ticket_upkeep_flg == "Y"-->
								<td style="color: green">已维护</td>
								<!-- /ko -->

								<!-- ko if: $data.supplier_upkeep_flg == "N"-->
								<td style="color: red">未维护</td>
								<!-- /ko -->
								<!-- ko if: $data.supplier_upkeep_flg == "D"-->
								<td>无地接</td>
								<!-- /ko -->
								<!-- ko if: $data.supplier_upkeep_flg == "Y"-->
								<td style="color: green">已维护</td>
								<!-- /ko -->

								<!-- ko if: $data.local_upkeep_flg == "N"-->
								<td style="color: red">未维护</td>
								<!-- /ko -->
								<!-- ko if: $data.local_upkeep_flg == "D"-->
								<td>无本地</td>
								<!-- /ko -->
								<!-- ko if: $data.local_upkeep_flg == "Y"-->
								<td style="color: green">已维护</td>
								<!-- /ko -->

								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text: $data.product_manager"></td>
								</s:if>
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
	<div id="c-c-t" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<em class="small-box"> <input type="radio" name="cctradio" data-bind="checked:clientConfirmType()" value="D"
					onclick="changeCctRadio(this)" /> <label>无模板</label>
				</em>
			</div>
		</div>
		<div class="input-row clearfloat" style="height: 40px">
			<div class="col-md-12">
				<em class="small-box"> <input type="radio" name="cctradio" data-bind="checked:clientConfirmType()" value="Y"
					onclick="changeCctRadio(this)" /> <label>上传</label>
				</em>
				<a href="javascript:;" id="c-c-t-a" style="display: none" class="a-upload">上传模板<input type="file" name="cct" /></a>
				<input type="hidden" id="cct_file" />
			<!-- ko if: clientConfirmTemplet() == "default" || clientConfirmTemplet()=="no" -->
			<span style="color: blue">默认模板</span>
			<!-- /ko -->
			<!-- ko if: clientConfirmTemplet() != "default" && clientConfirmTemplet()!="" &&clientConfirmTemplet() != "no" -->
			<span style="color: green">已上传&nbsp;&nbsp;<a href="javascript:void(0)"
				data-bind="click:function(){viewTemplet('cc',clientConfirmTemplet());}">预览</a></span>
			<!-- /ko -->
			</div>
		</div>
		<div class="input-row clearfloat">
			<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:cancelCCT">取消</button>
			<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:saveCCT">保存</button>
		</div>
	</div>
	<div id="o-n-t" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div class="col-md-12">
				<em class="small-box"> <input type="radio" name="ontradio" data-bind="checked:outNoticeType()" value="D"
					onclick="changeOntRadio(this)" /> <label>无模板</label>
				</em>
			</div>
		</div>
		<div class="input-row clearfloat" style="height: 40px">
			<div class="col-md-12">
				<em class="small-box"> <input type="radio" name="ontradio" value="Y" data-bind="checked:outNoticeType()"
					onclick="changeOntRadio(this)" /> <label>上传</label>
				</em> <a id="o-n-t-a" style="display: none" href="javascript:;" class="a-upload">上传模板<input type="file" name="ont" /></a>
				<input type="hidden" id="ont_file" />
			<!-- ko if: outNoticeTemplet() == "default" || outNoticeTemplet()=="no" -->
			<span style="color: blue">默认模板</span>
			<!-- /ko -->
			<!-- ko if: outNoticeTemplet() != "default" && outNoticeTemplet()!="" &&outNoticeTemplet() != "no" -->
			<span style="color: green">已上传&nbsp;&nbsp;<a href="javascript:void(0)"
				data-bind="click:function(){viewTemplet('on',outNoticeTemplet());}">预览</a></span>
			<!-- /ko -->
			</div>
		</div>
		<div class="input-row clearfloat">
			<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:cancelONT">取消</button>
			<button type="submit" style="float: right" class="btn btn-green col-md-1" data-bind="click:saveONT">保存</button>
		</div>
	</div>
	<div id="air-ticket-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="width: 100%">
				<label class="l">产品名称</label> <label class="l" data-bind="text:product().name"></label> <label class="l">产品编号</label>
				<label class="l" data-bind="text:product().product_number"></label> <label class="l"
					data-bind="text:chargeMapping[product().air_ticket_charge]"></label>
			</div>
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">航段</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">起飞城市</th>
							<th style="width: 10%">天次</th>
							<th style="width: 30%">抵达城市</th>
							<th style="width: 20%">航班号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:airTickets">
						<tr>
							<td data-bind="text:$data.ticket_index"></td>
							<td data-bind="text:$data.start_day"></td>
							<td data-bind="text:$data.start_city"></td>
							<td data-bind="text:$data.end_day"></td>
							<td data-bind="text:$data.end_city"></td>
							<td data-bind="text:$data.ticket_number"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/product-upload.js"></script>
	<script src="<%=basePath%>static/js/product/product-upkeep.js"></script>
</body>
</html>