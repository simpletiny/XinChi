<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String product_pk = request.getParameter("product_pk");
	String order_number = request.getParameter("order_number");
	String standard_flg = request.getParameter("standard_flg");
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

.intdtext {
	width: 100px !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				订单地接信息<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="product_pk" value="<%=product_pk%>" /> <input type="hidden" id="order_number"
			value="<%=order_number%>" /> <input type="hidden" id="standard_flg" value="<%=standard_flg%>" />
		<div class="main-container">
			<div class="main-box">

				<div class="form-box info-form">
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<table style="width: 100%" class="table table-striped table-hover" id="table-order">
								<thead>
									<tr>
										<th style="width: 8%">团号</th>
										<th style="width: 8%">销售</th>
										<th style="width: 5%">人数</th>
										<th style="width: 10%">游客</th>
										<!-- ko foreach:productSuppliers -->
										<th data-bind='text:"地接"+($index()+1)'></th>
										<!-- /ko -->
									</tr>
								</thead>
								<tbody data-bind="foreach:orders">
									<tr>
										<td data-bind="text:$data.team_number"></td>
										<td data-bind="text:$data.create_user"></td>
										<td data-bind="text:$data.adult_count+($data.special_count==null?0:$data.special_count)"></td>
										<td><a href="javascript:void(0)" style="color: blue"
											data-bind="click:function(){$root.checkPassengers($data.team_number);},text: $data.passenger_captain"></a></td>
										<!-- ko foreach:{data:$root.productSuppliers,as:'supplier'} -->
										<td><input type="number" class="required intdtext"
											data-bind="value:supplier.adult_cost*$parent.adult_count+supplier.child_cost*($parent.special_count==null?0:$parent.special_count)"
											st="supplier-cost" /></td>
										<!-- /ko -->
									</tr>
								</tbody>
							</table>
						</div>
					</div>
				</div>
				<div id="div-supplier" data-bind="foreach:{data:productSuppliers,as:'supplier'}">
					<div>
						<div class="input-row clearfloat">
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">地接社</label>
								<div class="fix-width1">
									<input type="text" class="ip- required" st="supplier-name" data-bind="value:$data.supplier_employee_name"
										onclick="choseSupplierEmployee(event)" /> <input type="text" class="need"
										data-bind="value:$data.supplier_employee_pk" st="supplier-pk" style="display: none" />
								</div>

							</div>
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">产品名称</label>
								<div class="fix-width1">
									<input type="text" class="ip- required" data-bind="value:$data.supplier_product_name"
										st="supplier-product-name" />
								</div>
							</div>
							<div class="col-md-2 required">
								<label class="l" style="width: 70px !important">天数</label>
								<div class="ip" style="width: 50% !important">
									<input type="number" class="ip- required" data-bind="value:$data.days" st="supplier-product-days" />
								</div>
							</div>
							<div class="col-md-2 required">
								<label class="l" style="width: 70px !important">结算价格</label>
								<div class="ip" style="width: 50% !important">
									<input type="number" class="ip- required"
										data-bind="value:$data.adult_cost*$root.adult_count()+$data.child_cost*$root.special_count()"
										st="supplier-cost" />
								</div>
							</div>
						</div>
						<div style="margin-top: 20px; padding-left: 70px">
							<table style="width: 90%" class="table-supplier">
								<thead>
									<tr class="required">
										<th style="width: 5%"></th>
										<th style="width: 5%"></th>
										<th style="width: 5%"></th>
										<th style="width: 10%"></th>
										<th style="width: 5%"></th>
										<th style="width: 10%"></th>
										<th class="r" style="width: 10%">天次</th>
										<th class="r" style="width: 10%">交通工具</th>
										<th class="r" style="width: 10%">抵离时间</th>
										<th class="r" style="width: 10%">抵离城市</th>
										<th class="r" style="width: 10%">抵离地点</th>
									</tr>
								</thead>
								<!-- ko if:supplier.infos.length!=0 -->
								<tbody st="t-body" data-bind="foreach:$data.infos">
									<tr>
										<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
										<td class="r">接：</td>
										<td><input
											data-bind="checked:$data.pick_type,attr:{name:'radio-jie-'+ $parentContext.$index()+'-'+$index()}"
											st="radio-jie-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-0"
											data-bind="value:$data.pick_leg,enable:$root.isD($data.pick_type),css:{required:$root.isD($data.pick_type)}" /></td>
										<td><input
											data-bind="checked:$data.pick_type,attr:{name:'radio-jie-'+ $parentContext.$index()+'-'+$index()}"
											st="radio-jie-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-1"
											data-bind="value:$data.pick_other,enable:!$root.isD($data.pick_type),css:{required:!$root.isD($data.pick_type)}" /></td>
										<td><input class="required" type="number" maxlength="2" st="day" data-bind="value:$data.pick_day" /></td>
										<td><input class="required" type="text" data-bind="value:$data.pick_traffic" maxlength="10"
											st="traffic-tool" /></td>
										<td><input class="required" type="text" data-bind="value:$data.pick_time" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" data-bind="value:$data.pick_city" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" data-bind="value:$data.pick_place" maxlength="30" st="place" /></td>
									</tr>
									<tr>
										<td><input type="button" value="+" onclick="addRow(this)" /></td>
										<td class="r">送：</td>
										<td><input
											data-bind="checked:$data.send_type,attr:{name:'radio-song-'+ $parentContext.$index()+'-'+$index()}"
											st="radio-song-0" type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="10" st="txt-song-type-0"
											data-bind="value:$data.send_leg,enable:$root.isD($data.send_type),css:{required:$root.isD($data.send_type)}" /></td>
										<td><input
											data-bind="checked:$data.send_type,attr:{name:'radio-song-'+ $parentContext.$index()+'-'+$index()}"
											st="radio-song-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-song-type-1"
											data-bind="value:$data.send_other,enable:!$root.isD($data.send_type),css:{required:!$root.isD($data.send_type)}" /></td>
										<td><input class="required" type="number" maxlength="2" st="day" data-bind="value:$data.send_day" /></td>
										<td><input class="required" type="text" data-bind="value:$data.send_traffic" maxlength="10"
											st="traffic-tool" /></td>
										<td><input class="required" type="text" data-bind="value:$data.send_time" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" data-bind="value:$data.send_city" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" data-bind="value:$data.send_place" maxlength="30" st="place" /></td>
									</tr>
									<!-- ko if:($index()+1) < supplier.infos.length -->
									<tr>
										<td colspan="11"><hr style="width: 100%; text-align: center; vertical-align: middle" /></td>
									</tr>
									<!-- /ko -->
								</tbody>
								<!-- /ko -->
								<!-- ko if:supplier.infos.length==0 -->
								<tbody st="t-body">
									<tr>
										<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
										<td class="r">接：</td>
										<td><input checked="checked" data-bind="attr:{name:'radio-jie-'+ $index()+'-0'}" st="radio-jie-0"
											type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-0" /></td>
										<td><input data-bind="attr:{name:'radio-jie-'+ $index()+'-0'}" st="radio-jie-1" type="radio" value="1"
											onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-1" disabled="disabled" /></td>
										<td><input class="required" maxlength="2" type="number" st="day" /></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
									<tr>
										<td><input type="button" value="+" onclick="addRow(this)" /></td>
										<td class="r">送：</td>
										<td><input data-bind="attr:{name:'radio-song-'+ $index()+'-0'}" checked="checked" st="radio-song-0"
											type="radio" value="0" onclick="changeJieSongType(this)" />航段</td>
										<td><input type="text" maxlength="10" st="txt-song-type-0" /></td>
										<td><input data-bind="attr:{name:'radio-song-'+ $index()+'-0'}" st="radio-song-1" type="radio" value="1"
											onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-song-type-1" disabled="disabled" /></td>
										<td><input class="required" maxlength="2" type="number" st="day" /></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
								</tbody>
								<!-- /ko -->
							</table>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-6">
								<label class="l" style="width: 70px !important">游客信息：</label>
								<div class="ip">
									<div style="padding-top: 4px;">
										<em class="small-box"> <input type="checkbox" data-bind="checked:$data.tourist_info.indexOf('name')>=0"
											name="chk_tourist" value="name" /><label>姓名</label> <input type="checkbox"
											data-bind="checked:$data.tourist_info.indexOf('sex')>=0" name="chk_tourist" value="sex" /><label>性别</label>
											<input type="checkbox" data-bind="checked:$data.tourist_info.indexOf('age')>=0" name="chk_tourist"
											value="age" /><label>年龄</label> <input type="checkbox" name="chk_tourist" value="id"
											data-bind="checked:$data.tourist_info.indexOf('id')>=0" /><label>身份证号码</label> <input type="checkbox"
											name="chk_tourist" data-bind="checked:$data.tourist_info.indexOf('tel')>=0" value="tel" /><label>联系方式</label>
											<input type="checkbox" data-bind="checked:$data.tourist_info.indexOf('room_group')>=0" name="chk_tourist"
											value="room_group" /><label>分房组</label>
										</em>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="ip">
									<a href="javascript:;" class="a-upload">上传确认件<input type="file" onchange="changeFile(this)" /></a> <input
										type="hidden" data-bind="value:$data.confirm_file_templet" st="confirm-file-templet" />
									<!-- ko if: $data.confirm_file_templet == "default" -->
									<span style="color: blue">默认模板</span>
									<!-- /ko -->
									<!-- ko if: $data.confirm_file_templet != "default" && $data.confirm_file_templet!="" -->
									<span style="color: green">已上传&nbsp;&nbsp;<a href="javascript:void(0)"
										data-bind="click:function(){viewTemplet('sc',$data.confirm_file_templet);}">预览</a></span>
									<!-- /ko -->
								</div>
							</div>
						</div>
						<hr />
					</div>
				</div>
				<div class="input-row clearfloat" style="text-align: right">
					<div class="col-md-12">
						<div class="ip">
							<a type="submit" class="btn btn-r" href="javacript:void(0);" onclick="deleteSupplier()">-地接社</a> <a type="submit"
								class="btn btn-r" href="javacript:void(0);" onclick="addSupplier()">+地接社</a>
						</div>
					</div>
				</div>
				<hr class="hr-big" />
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createOrderOperation">保存</a> <a type="submit"
						class="btn btn-green btn-r" onclick="javascript:history.go(-1);return false;">放弃</a>
				</div>
			</div>
		</div>
	</div>

	<div id="supplier-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchSupplierEmployee }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>财务主体</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: supplierEmployees">
							<tr data-bind="event: {click: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
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
	<!-- 查看乘客信息 -->
	<div id="passengers-check" style="display: none; width: 800px">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px; overflow-y: auto">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 10%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 10%">身份证号</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	<script>
		$(".order-operate").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/product-upload.js"></script>
	<script src="<%=basePath%>static/js/product/order-operate-creation.js"></script>
</body>
</html>