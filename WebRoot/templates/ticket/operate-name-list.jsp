<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
#table-ticket th,#table-ticket td {
	text-align: center;
}

.table-ticket tr td input {
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
				详情分配<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
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
										<input type="text" class="ip-" maxlength="50" st="ticket-source" placeholder="票源方" value='<s:property value="#ta.ticket_source"/>' required="required" />
										<input style="display:none" value='<s:property value="#ta.ticket_source_pk"/>' st="ticket-source-pk"/>
									</div>
								</div>
								<div class="col-md-3 required">
									<label class="l" style="width: 60px">单价</label>
									<div class="ip">
										<input type="number" class="ip-" st="ticket-cost" placeholder="单价" required="required" />
									</div>
								</div>
								<div class="col-md-3">
									<label class="l" style="width: 60px">PNR码</label>
									<div class="ip">
										<input type="text" class="ip-" maxlength="50" st="ticket-PNR" placeholder="PNR码"/>
									</div>
								</div>
							</div>
							<div class="input-row clearfloat">
								<table class="table-ticket">
									<thead>
										<tr class="required">
											<th></th>
											<th style="width: 10%">航班日期</th>
											<th style="width: 10%">航班号</th>
											<th style="width: 20%">起降时刻</th>
											<th style="width: 20%">起降城市</th>
											<th style="width: 10%">起飞机场</th>
											<th style="width: 10%">降落机场</th>
											<th style="width: 10%">航站楼</th>
										</tr>
									</thead>
									<tbody>
										<s:set name="legs" value='#ta.airLegs'></s:set>
										<s:iterator value="#legs" id="leg">
										<tr>
											<td><input type="button" value="-" onclick="deleteRow(this)"></input></td>
											<td><s:property value="#leg.date" /><input type="hidden" st="ticket-date" value='<s:property value="#leg.date"/>'/></td>
											<td><input st="ticket-number" maxlength="10" type="text" /></td>
											<td><input st="from-to-time" maxlength="20" type="text" /></td>
											<td><s:property value="#leg.from_city"/>--<s:property value="#leg.to_city"/><input st="from-to-city" type="hidden"  value='<s:property value="#leg.from_city"/>--<s:property value="#leg.to_city"/>'/></td>
											<td><input st="from-airport"  maxlength="10" type="text" /></td>
											<td><input st="to-airport"  maxlength="10" type="text" /></td>
											<td><input st="terminal"  maxlength="10" type="text" /></td>
										</tr>
										</s:iterator>
									</tbody>
								</table>
							</div>
							<div class="input-row clearfloat">
								<div class="col-md-12">
									<h3>名单：</h3>
									<div class="ip">
										<s:set name="passengers" value='#ta.passengers'></s:set>
										<s:iterator value="#passengers" id="passenger">
											<em class="small-box"> <a href="#"><s:property value="#passenger.name" />：<s:property value="#passenger.id" />;</a> <input type="hidden" st="passenger-pk" value='<s:property value="#passenger.pk"/>' />
											</em>
										</s:iterator>
									</div>
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
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/ticket/operate-name-list.js"></script>
</body>
</html>