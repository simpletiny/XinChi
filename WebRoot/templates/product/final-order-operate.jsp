<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String order_number = request.getParameter("order_number");
	String supplier_employee_pk = request.getParameter("supplier_employee_pk");
	String operation_pk = request.getParameter("operation_pk");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.table-supplier th, .table-supplier td {
	text-align: center;
}

.table-supplier tr td input[type="text"], input[type="number"], input[type="button"]
	{
	width: 90%;
}

.table-supplier {
	border-collapse: separate;
	border-spacing: 0px 10px;
	border-spacing: 0px 10px;
}

#table-order  tr:nth-child(odd) td {
	border-top: 1px dashed black; /* 单数行的上行线加粗 */
}

.required th[class="r"]:after, td[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
}

.fix-width {
	width: 45% !important;
}

.col-md-3 {
	width: 24% !important;
}

.col-md-1 {
	width: 4% !important;
}

.fix-width1 {
	display: inline-block;
}

.hr-big {
	border-top: 1px solid black !important;
}

.hr-big-dash {
	border-top: 1px dashed black !important;
}

.intdtext {
	width: 100px !important;
}

h3 {
	padding-left: 20px !important;
	font-weight: bold !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				决算操作单<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="txt-order-number" value="<%=order_number%>" /> <input type="hidden"
			id="txt-supplier-employee-pk" value="<%=supplier_employee_pk%>" /> <input type="hidden" id="txt-operation-pk"
			value="<%=operation_pk%>" />
		<div class="main-box">
			<div class="form-box info-form">
				<h3>订单信息</h3>
				<div class="input-row clearfloat">
					<div class="col-md-3">
						<label class="l">产品名称</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().product_name"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">型号</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().product_model"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">团期</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().departure_date"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">天数</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().days"></p>
						</div>
					</div>
				</div>
				<div class="input-row clearfloat">
					<div class="col-md-3">
						<label class="l">成人</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().adult_count+'人'"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">儿童</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().special_count+'人'"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">全陪</label>
						<div class="ip fix-width">
							<p class="ip-default"></p>
						</div>
					</div>
					<div class="col-md-3">
						<label class="l">标单</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:product_order().standard_flg=='Y'?'是':'否'"></p>
						</div>
					</div>
				</div>
				<hr />
				<h3>票务信息</h3>
				<div class="input-row clearfloat">
					<div class="col-md-3">
						<label class="l">出票状态</label>
						<div class="ip fix-width">
							<p class="ip-default" data-bind="text:ticket_status()"></p>
						</div>
					</div>
				</div>
				<div class="input-row clearfloat">
					<table style="width: 100%" class="table table-striped table-hover">
						<thead>
							<tr>
								<th>航段</th>
								<th>日期</th>
								<th>城市对</th>
								<th>航班号</th>
								<th>航班时刻</th>
								<th>起飞机场</th>
								<th>降落机场</th>
								<th>人数</th>
							</tr>
						</thead>
						<tbody data-bind="foreach:ticket_infos">
							<tr>
								<td data-bind="text:alphabetMap[$index()+1]"></td>
								<td data-bind="text:$data.ticket_date"></td>
								<td data-bind="text:$data.from_to_city"></td>
								<td data-bind="text:$data.ticket_number"></td>
								<td data-bind="text:$data.from_to_time"></td>
								<td data-bind="text:$data.from_airport"></td>
								<td data-bind="text:$data.to_airport"></td>
								<td data-bind="text:$data.people_count"></td>
							</tr>
						</tbody>
					</table>
				</div>
			</div>
			<hr />
			<div id="div-supplier">
				<div>
					<h3>地接信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-3">
							<label class="l">地接操作</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:order_supplier().supplier_employee_name"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">产品名称</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:order_supplier().supplier_product_name"></p>
							</div>
						</div>
						<div class="col-md-3">
							<label class="l">接团日期</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:order_supplier().pick_date"></p>
							</div>
						</div>
						<div class="col-md-3 ">
							<label class="l">天数</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:order_supplier().days"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">

						<div class="col-md-3 ">
							<label class="l">预算团款</label>
							<div class="ip fix-width">
								<p class="ip-default" data-bind="text:order_supplier().supplier_cost"></p>
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l">决算团款</label>
							<div class="ip fix-width">
								<input type="number" class="ip- required" id="final-cost" data-bind="value:order_supplier().supplier_cost"
									st="supplier-cost" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-4">
							<label class="l">接待说明</label>
							<div class="ip">
								<p class="ip-default" data-bind="text:order_supplier().treat_comment"></p>
							</div>
						</div>
						<div class="col-md-4">
							<label class="l">团款说明</label>
							<div class="ip">
								<p class="ip-default" data-bind="text:order_supplier().payable_comment"></p>
							</div>
						</div>
					</div>
					<div style="margin-top: 20px; padding-left: 70px">
						<table style="width: 90%" class="table-supplier">
							<thead>
								<tr>
									<th style="width: 5%">接/送</th>
									<th style="width: 5%">方式</th>
									<th style="width: 10%">天次</th>
									<th style="width: 10%">交通工具</th>
									<th style="width: 10%">抵离时间</th>
									<th style="width: 10%">抵离城市</th>
									<th style="width: 10%">抵离地点</th>
								</tr>
							</thead>
							<!-- ko if:order_supplier().infos.length!=0 -->
							<tbody st="t-body" data-bind="foreach:order_supplier().infos">
								<tr>
									<td>接：</td>
									<td data-bind="text:pick_type == 1 ? pick_other : '飞机航段'+pick_leg"></td>
									<td data-bind="text: pick_day"></td>
									<td data-bind="text: pick_traffic"></td>
									<td data-bind="text: pick_time"></td>
									<td data-bind="text: pick_city"></td>
									<td data-bind="text: pick_place"></td>
								</tr>
								<tr>
									<td>送：</td>
									<td data-bind="text:send_type == 1 ? send_other : '飞机航段'+send_leg"></td>
									<td data-bind="text: send_day"></td>
									<td data-bind="text: send_traffic"></td>
									<td data-bind="text: send_time"></td>
									<td data-bind="text: send_city"></td>
									<td data-bind="text: send_place"></td>
								</tr>
								<!-- ko if:($index()+1) < $parent.order_supplier().infos.length -->
								<tr>
									<td colspan="11"><hr style="width: 100%; text-align: center; vertical-align: middle" /></td>
								</tr>
								<!-- /ko -->
							</tbody>
							<!-- /ko -->
						</table>
					</div>
					<hr />
					<div class="input-row clearfloat table-order-here">
						<h3>名单</h3>
						<table style="width: 100%;" class="table table-order" id='table-name'>
							<thead>
								<tr>
									<th style="width: 4%;">序</th>
									<th style="width: 12%;">订单信息</th>
									<th style="width: 12%;">客人名单</th>
									<th style="width: 4%;">性别</th>
									<th style="width: 4%;">年龄</th>
									<th style="width: 6%;">预算价格</th>
									<th style="width: 8%; color: red">决算价格*&nbsp;&nbsp;<input type="checkbox" checked st="chk-edit-all" /></th>
									<th style="width: 10%;">团款备注</th>
									<th style="width: 10%;">决算团款备注</th>
									<th style="width: 10%;">联系方式</th>
									<th style="width: 6%;">用房说明</th>
									<th style="width: 6%;">销售特请</th>
									<th style="width: 8%;">决算备注</th>
								</tr>
							</thead>
							<tbody data-bind="foreach:{data:order_supplier().sale_order_infos,as:'order'}">
								<!-- ko foreach:{data:order.infos,as:'name'} -->
								<!-- ko if: $index() === 0-->
								<tr>	
									<input type="hidden" data-bind="value:order.pk" st="sale-order-pk" />
									<input type="hidden" st="team-number" data-bind="value:order.team_number" />
									<input type="hidden" st="name-pk" data-bind="value:$data.pk" />
									<td data-bind="text:$parentContext.$index()+1,attr: { rowspan: order.infos.length }"></td>
									<td
										data-bind="html:order.adult_count+'大'+order.special_count+'小'+'&lt;br&gt;销售：'+order.sale_name+'&lt;br&gt;团号：'+order.team_number,attr: { rowspan: order.infos.length }"></td>
									<td data-bind="text:$data.passenger_name+';'+$data.passenger_id"></td>
									<td data-bind="text:determineGender($data.passenger_id)==0?'女':'男'"></td>
									<td data-bind="text:calculateAge($data.passenger_id)"></td>
									<td data-bind="text: name.price"></td>
									<td><input class="required" type="text" oninput="editAll(this)" st="price" data-bind="value:$data.price" /></td>
									<td data-bind="attr: { rowspan: order.infos.length }, text: order.receivable_comment"></td>
									<td data-bind="attr: { rowspan: order.infos.length }"><textarea maxlength="200" class="td-textarea" st="final-payable-comment"></textarea></td>
									<td data-bind="attr: { rowspan: order.infos.length }, text: order.contact_way"></td>
									<td data-bind="attr: { rowspan: order.infos.length }, text: order.hotel_comment"></td>
									<td data-bind="attr: { rowspan: order.infos.length }, text: order.treat_comment"></td>
									<td data-bind="attr: { rowspan: order.infos.length }"><textarea maxlength="200" class="td-textarea" st="final-comment"></textarea></td>
								</tr>
								<!-- /ko -->
								<!-- ko if: $index() != 0-->
								<tr>
									<input type="hidden" data-bind="value:order.pk" />
									<input type="hidden" st="team-number" data-bind="value:order.team_number" />
									<input type="hidden" st="name-pk" data-bind="value:$data.pk" />
									<td data-bind="text:$data.passenger_name+';'+$data.passenger_id"></td>
									<td data-bind="text:determineGender($data.passenger_id)==0?'女':'男'"></td>
									<td data-bind="text:calculateAge($data.passenger_id)"></td>
									<td data-bind="text: name.price"></td>
									<td><input class="required" type="text" oninput="editAll(this)" st="price" data-bind="value:$data.price" /></td>
								</tr>
								<!-- /ko -->
								<!-- /ko -->
							</tbody>
						</table>
					</div>
					<hr class="hr-big-dash" />
				</div>
			</div>
			<hr class="hr-big" />
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: finalOrderOperation">决算</a> <a type="submit"
					class="btn btn-green btn-r" onclick="javascript:history.go(-1);return false;">放弃</a>
			</div>
		</div>
	</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/product/product-properties.js"></script>
	<script src="<%=basePath%>static/js/product/final-order-operate.js?v=1.001"></script>
</body>
</html>