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

#table-ticket tr td input[type="text"], #table-ticket tr td input[type="number"]
	{
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
			<h2>
				新建套票<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">

					<div class="input-row clearfloat">
						<div class="col-md-3 required">
							<label class="l" style="width: 25%">套票名称</label>
							<div class="ip" style="width: 50%">
								<input type="text" class="ip- date-picker" name="base.name" required="required" placeholder="好区分的名称" />
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 25%">编号</label>
							<div class="ip" style="width: 50%">
								<input type="text" class="ip- date-picker" placeholder="用于区分同名称套票" maxlength="10" name="base.model"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-3 required">
							<label class="l" style="width: 25%">票源</label>
							<div class="ip" style="width: 50%">
								<input st="supplier-name" type="text" class="ip-" onclick="choseSupplierEmployee(event)" required="required"
									placeholder="票源供应商" /> <input type="hidden" st="supplier-pk" name="base.supplier_employee_pk" />
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 25%">价格</label>
							<div class="ip" style="width: 50%">
								<input type="number" class="ip- date-picker" name="base.price" required="required" placeholder="套票单价" />
							</div>
						</div>
						<div class="col-md-3 required">
							<label class="l" style="width: 25%">儿童价格</label>
							<div class="ip" style="width: 50%">
								<input type="number" class="ip- date-picker" name="base.special_price" required="required" placeholder="儿童价格" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat" id="air-ticket">
						<div class="col-md-12">
							<table style="width: 100%" id="table-ticket">
								<thead>
									<tr class="required">
										<th class="r" style="width: 5%">序号</th>
										<th class="r" style="width: 5%">天次</th>
										<th class="r" style="width: 10%">航班号</th>
										<th class="r" style="width: 15%">票务航段</th>
										<th class="r" style="width: 10%">起飞时间</th>
										<th class="r" style="width: 10%">降落时间</th>
										<th style="width: 5%"></th>
										<th style="width: 15%">起飞机场（含航站楼）</th>
										<th style="width: 15%">降落机场（含航站楼）</th>
									</tr>
								</thead>
								<tbody>
									<tr>
										<td>A<input type="hidden" st="index-leg" value="A" /></td>
										<td><input st="start-day" type="number" maxlength="2" min="1" max="20" required="required" /></td>
										<td><input st="ticket-number" type="text" maxlength="10" required="required" /></td>
										<td><input st="ticket-leg" type="text" onclick="choseAirLeg(event)" maxlength="20" required="required" /></td>
										<td><input st="start-time" type="text" maxlength="5" onkeyup="formatTime(this)" onblur="checkTime(this)"
											required="required" /></td>
										<td><input st="end-time" type="text" maxlength="5" onkeyup="formatTime(this)" onblur="checkTime(this)"
											required="required" /></td>
										<td style="float: left"><input st="is-add-day" type="checkbox" />+1</td>
										<td><input st="start-place" type="text" maxlength="20" /></td>
										<td><input st="end-place" type="text" maxlength="20" /></td>
									</tr>
								</tbody>
							</table>
							<div style="margin-top: 20px; float: right">
								<input type="button" value="-" onclick="deleteRow()"></input> <input type="button" value="+" onclick="addRow()"></input>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="5" maxlength="200" name="base.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12" style="text-align: right">
							<a type="submit" class="btn btn-green btn-r" data-bind="click: createFlight">保存</a>
						</div>
					</div>
				</form>

			</div>
		</div>
	</div>
	<!-- 供应商选择界面 -->
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
						<a data-bind="click: previousPage1, enable: currentPage1() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums1 -->
						<!-- ko if: $data == $root.currentPage1() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage1() -->
						<a data-bind="text: $data, click: $root.turnPage1"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage1, enable: currentPage1() < pageNums1().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>

	<div id="air-leg-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">城市</label>
						<div class="col-md-6">
							<input type="text" id="city" class="form-control" placeholder="城市" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchAirLeg }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>出发城市</th>
								<th>抵达城市</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: airLegs">
							<tr data-bind="event: {click: function(){ $parent.pickAirLeg($data.from_city,$data.to_city)}}">
								<td data-bind="text: $data.from_city"></td>
								<td data-bind="text: $data.to_city"></td>
							</tr>
						</tbody>
					</table>
					<div class="pagination clearfloat">
						<a data-bind="click: previousPage2, enable: currentPage2() > 1" class="prev">Prev</a>
						<!-- ko foreach: pageNums2 -->
						<!-- ko if: $data == $root.currentPage2() -->
						<span class="current" data-bind="text: $data"></span>
						<!-- /ko -->
						<!-- ko ifnot: $data == $root.currentPage2() -->
						<a data-bind="text: $data, click: $root.turnPage2"></a>
						<!-- /ko -->
						<!-- /ko -->
						<a data-bind="click: nextPage2, enable: currentPage2() < pageNums2().length" class="next">Next</a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/ticket/season-ticket-creation.js"></script>
</body>
</html>