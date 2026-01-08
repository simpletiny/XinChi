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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
#table-ticket th, #table-ticket td {
	text-align: center;
}

.table-ticket tr td input[type="text"] {
	width: 90% !important;
}

.table-ticket {
	border-collapse: separate;
	border-spacing: 0px 10px;
	width: 100%;
}

.required th[class="r"]:after {
	content: " *";
	color: red;
	font-weight: bold;
}

.deleteDiv {
	position: relative;
	display: block;
	width: 12px;
	height: 12px;
	background: url(../../static/img/mc-icon-cancel.png) no-repeat;
	background-size: cover;
	margin-right: 25px;
	padding-top: 2px;
	z-index: 999;
	float: right;
	cursor: pointer;
}

.delete {
	position: absolute;
	width: 100px;
	height: 50px;
	text-decoration: none;
	background: transparent;
	font-size: 16px;
	font-family: 微软雅黑, 宋体, Arial, Helvetica, Verdana, sans-serif;
	font-weight: bold;
	border: solid 2px black;
	-webkit-transition: all linear 0.30s;
	-moz-transition: all linear 0.30s;
	transition: all linear 0.30s;
	line-height: 50px;
	text-align: center;
	z-index: 999;
}

.delete:hover {
	background: #2f435e;
	color: #f2f2f2;
	opacity: 1;
	border: dashed 2px white;
	cursor: pointer;
}

.every-count {
	border: solid 1px #C1CDCD;
	margin-top: 5px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				详情分配<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" style="border: none">
					<s:set name="list" value="ticketAllots"></s:set>
					<s:iterator value="#list" id="ta" status="status">
						<h3 style="margin-top: 40px">
							票源
							<s:property value="#status.index+1" />
						</h3>
						<div st="every-count" class="every-count">
							<div class="input-row clearfloat">
								<div class="col-md-3 required">
									<label class="l" style="width: 60px">票源方</label>
									<div class="ip">
										<input type="text" class="ip-" maxlength="50" st="ticket-source" placeholder="票源方"
											value='<s:property value="#ta.ticket_source"/>' required="required" /> <input style="display: none"
											value='<s:property value="#ta.ticket_source_pk"/>' st="ticket-source-pk" />
									</div>
								</div>
								<div class="col-md-2">
									<label class="l" style="width: 30%">总杂费</label>
									<div class="ip" style="width: 70%">
										<p class="ip-" st="sum-other">0</p>
									</div>
								</div>
								<div class="col-md-2 required">
									<label class="l" style="width: 30%">总票款</label>
									<div class="ip" style="width: 70%">
										<p class="ip-" st="sum-cost">0</p>
									</div>
								</div>
								<div class="col-md-3">
									<label class="l" style="width: 60px">PNR码</label>
									<div class="ip">
										<input type="text" class="ip-" maxlength="50" st="ticket-PNR" placeholder="PNR码" />
									</div>
								</div>
							</div>
							<div class="input-row clearfloat">
								<table class="table-ticket">
									<thead>
										<tr class="required">

											<th style="width: 10%">航班日期</th>
											<th style="width: 10%; color: red">航班号</th>
											<th style="width: 16%; color: red">起降时刻</th>
											<th style="width: 4%"></th>
											<th style="width: 20%">起降城市</th>
											<th style="width: 18%">起飞地</th>
											<th style="width: 18%">降落地</th>
											<th style="width: 4%"></th>
										</tr>
									</thead>
									<tbody>
										<s:set name="legs" value='#ta.airLegs'></s:set>
										<s:iterator value="#legs" id="leg">
											<tr>
												<td><s:property value="#leg.date" /><input type="hidden" st="ticket-date"
													value='<s:property value="#leg.date"/>' /></td>
												<td><input st="ticket-number" maxlength="10" type="text"
													value='<s:property value="#leg.ticket_number"/>' /></td>
												<td><input st="from-to-time" maxlength="20" type="text"
													value='<s:property value="#leg.start_time"/>--<s:property value="#leg.end_time"/>' /></td>
													<td><input type="checkbox"  ${leg.add_day_flg == 'Y' ? 'checked' : ''} st="add-day-flg"/>+1</td>
												<td><s:property value="#leg.from_city" />--<s:property value="#leg.to_city" /><input
													st="from-to-city" type="hidden"
													value='<s:property value="#leg.from_city"/>--<s:property value="#leg.to_city"/>' /></td>
												<td><input st="start-place" maxlength="20" type="text" value='<s:property value="#leg.start_place"/>' /></td>
												<td><input st="end-place" maxlength="20" type="text" value='<s:property value="#leg.end_place"/>' /></td>
												<td><input type="button" value="&nbsp;&nbsp;-&nbsp;&nbsp;" onclick="deleteRow(this)"></input></td>
											</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>

							<div class="input-row clearfloat">
								<div class="col-md-12">
									<table style="width: 100%" st="name-table" class="table table-striped table-hover">
										<thead>
											<tr>
												<th style="width: 4%">序号</th>
												<th style="width: 10%">姓名</th>
												<th style="width: 17%">证件号码</th>
												<th style="width: 7%; color: red">票价&nbsp;<input type="checkbox" checked title="选中修改所有价格"
													st="ticket-cost" /></th>
												<th style="width: 7%; color: red">税费&nbsp;<input type="checkbox" checked title="选中修改所有价格" st="taxation" /></th>
												<th style="width: 7%">杂费&nbsp;<input type="checkbox" checked title="选中修改所有价格" st="other-cost" /></th>
												<th style="width: 7%;">合计</th>
											</tr>
										</thead>
										<tbody>
											<s:set name="passengers" value='#ta.passengers'></s:set>
											<s:iterator value="#passengers" id="passenger" status="stat">
												<tr>
													<input type="hidden" st="passenger-pk" value='<s:property value="#passenger.pk"/>' />
													<td st="name-index"><s:property value="#stat.index+1" /></td>
													<td><s:property value="#passenger.name" /></td>
													<td><s:property value="#passenger.id" /></td>
													<td><input type="number" class="ip-" placeholder="机票价格" oninput="fixBind()" style="width: 90%"
														st="ticket-cost" /></td>
													<td><input type="number" class="ip-" placeholder="机票税费" oninput="fixBind()" style="width: 90%"
														data-bind="value:air_ticket_charge_config().ext1" st="taxation" /></td>
													<td><input type="number" class="ip-" placeholder="机票杂费" oninput="fixBind()" style="width: 90%"
														st="other-cost" /></td>
													<td style="vertical-align: middle;" st="sum">0</td>
												</tr>
											</s:iterator>
										</tbody>
									</table>
								</div>
							</div>
						</div>
					</s:iterator>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: finish">提交</a>
				</div>
			</div>
		</div>
	</div>

	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script>
		$(".ticket-operation").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/operate-name-list.js?v=1.003"></script>
</body>
</html>