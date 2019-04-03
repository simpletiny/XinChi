<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";

	String key = request.getParameter("key");
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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				地接维护<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="product-pk" value="<%=key%>" />

		<div class="main-container">
			<div class="main-box">

				<div class="form-box info-form">

					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">产品名称</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">产品线</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().location"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">型号</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().product_model"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">编号</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().product_number"></p>
							</div>
						</div>
					</div>
				</div>
				<div id="div-supplier">
					<div>
						<div class="input-row clearfloat">
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">地接社</label>
								<div class="fix-width1">
									<input type="text" class="ip- required" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input type="text"
										class="need" st="supplier-pk" style="display: none" />
								</div>

							</div>
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">产品名称</label>
								<div class="fix-width1">
									<input type="text" class="ip- required" st="supplier-product-name" />
								</div>
							</div>
							<div class="col-md-2 required">
								<label class="l" style="width: 70px !important">天数</label>
								<div class="ip" style="width: 50% !important">
									<input type="number" class="ip- required" st="supplier-product-days" />
								</div>
							</div>
							<div class="col-md-2 required">
								<label class="l" style="width: 70px !important">成人</label>
								<div class="ip" style="width: 50% !important">
									<input type="number" class="ip- required" st="adult-cost" />
								</div>
							</div>
							<div class="col-md-2 required">
								<label class="l" style="width: 70px !important">儿童</label>
								<div class="ip" style="width: 50% !important">
									<input type="number" class="ip- required" st="child-cost" />
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
								<tbody st="t-body">
									<tr>
										<td><input type="button" value="-" onclick="deleteRow(this)" /></td>
										<td class="r">接：</td>
										<td><input name="radio-jie-0" st="radio-jie-0" checked="checked" type="radio" value="0"
											onclick="changeJieSongType(this)"/>航段</td>
										<td><input class="required" type="text" maxlength="10" st="txt-jie-type-0" /></td>
										<td><input name="radio-jie-0" st="radio-jie-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-jie-type-1" disabled="disabled" /></td>
										<td><input class="required" type="number" st="day" /></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
									<tr>
										<td><input type="button" value="+" onclick="addRow(this)" /></td>
										<td class="r">送：</td>
										<td><input name="radio-song-0" st="radio-song-0" checked="checked" type="radio" value="0"
											onclick="changeJieSongType(this)" />航段</td>
										<td><input class="required" type="text" maxlength="10" st="txt-song-type-0" /></td>
										<td><input name="radio-song-0" st="radio-song-1" type="radio" value="1" onclick="changeJieSongType(this)" />其他</td>
										<td><input type="text" maxlength="10" st="txt-song-type-1" disabled="disabled" /></td>
										<td><input class="required" type="number" st="day" /></td>
										<td><input class="required" type="text" maxlength="10" st="traffic-tool" /></td>
										<td><input class="required" type="text" maxlength="15" st="time" /></td>
										<td><input class="required" type="text" maxlength="15" st="city" /></td>
										<td><input class="required" type="text" maxlength="30" st="place" /></td>
									</tr>
								</tbody>
							</table>
						</div>

						<div class="input-row clearfloat">
							<div class="col-md-6">
								<label class="l" style="width: 70px !important">游客信息：</label>
								<div class="ip">
									<div style="padding-top: 4px;">
										<em class="small-box"> <input type="checkbox" checked="checked" name="chk_tourist" value="name"/><label>姓名</label> <input
											type="checkbox" checked="checked" name="chk_tourist" value="sex"/><label>性别</label> <input type="checkbox"
											name="chk_tourist" value="age"/><label>年龄</label> <input type="checkbox" name="chk_tourist" value="id"/><label>身份证号码</label> <input
											type="checkbox" checked="checked" name="chk_tourist" value="tel"/><label>联系方式</label> <input type="checkbox"
											name="chk_tourist" value="room_group"/><label>分房组</label>
										</em>
									</div>
								</div>
							</div>
							<div class="col-md-3">
								<div class="ip">
									<a href="javascript:;" class="a-upload">上传确认件<input type="file" onchange="changeFile(this)" /></a> <input
										type="hidden" st="confirm-file-templet" />
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
					<a type="submit" class="btn btn-green btn-r" data-bind="click: saveProductSupplier">保存</a> <a type="submit"
						class="btn btn-green btn-r" data-bind="click: createProduct">更新</a> <a type="submit" class="btn btn-green btn-r"
						onclick="javascript:history.go(-1);return false;">放弃</a>
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

	<script>
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/supplier-management.js"></script>
</body>
</html>