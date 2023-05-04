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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<style>
tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}

.download-panel {
	position: absolute;
	background: #FFEC8B;
	border: solid 1px red;
	z-index: 200;
	width: 150px;
	height: 50px;
	text-align: center;
}

.fix-width1 {
	width: 6.5% !important;
}

.fix-width2 {
	width: 10% !important;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<!-- tbc for to be confirmed -->
			<h2>已确认订单</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">
					<div class="form-group"> 
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { confirmNameList() }">名单确认</button>
								<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { writeFirstAir() }">写入首航段</button> -->
								<!-- <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editOrder() }">微调</button> -->
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { changeOrder() }">变更</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { cancelOrder() }">取消</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { finalOrder() }">决算</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 fix-width1 control-label"><input name="option.order_statuses" checked="checked"
							data-bind="click:function(){refresh();return true;}" value="no" type="checkbox" />未出团</label>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">客户</label>
							<div class="col-md-1">
								<input class="form-control" placeholder="客户" name="option.client_employee_name"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">产品名称</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品名称" name="option.product_name"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">游客姓名</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="暂不可用" name="option.passenger_name"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 fix-width1 control-label"><input type="radio" value="1" onclick="check(this)"
								checked name="radio_date" />出团日期</label>
							<div class="col-md-2 fix-width2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="from"
									name="option.departure_date_from" />
							</div>
						</div>
						<div align="left">
							<div class="col-md-2 fix-width2" style="float: left">
								<input type="text" class="form-control date-picker" st="st-date-1" placeholder="to"
									name="option.departure_date_to" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<label class="col-md-1 fix-width1 control-label"><input name="option.order_statuses" checked="checked"
							data-bind="click:function(){refresh();return true;}" value="yes" type="checkbox" />出团中</label>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">团号</label>
							<div class="col-md-1">
								<input class="form-control" placeholder="团号" name="option.team_number"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">产品经理</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="产品经理" name="option.product_manager"></input>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 fix-width1 control-label">游客手机</label>
							<div class="col-md-2">
								<input class="form-control" placeholder="暂不可用" name="option.passenger_cellphone"></input>
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 fix-width1 control-label"><input type="radio" value="2" onclick="check(this)"
								name="radio_date" />确认日期</label>
							<div class="col-md-2">
								<select class="form-control" disabled="disabled" st="st-date-2" style="height: 34px" id="select-sales"
									data-bind="options: confirmDates,  optionsText: 'cn', optionsValue: 'en'" name="option.confirm_period"></select>
							</div>
						</div>

					</div>
					<div class="form-group">
						<label class="col-md-1 fix-width1 control-label"><input name="option.order_statuses" checked="checked"
							data-bind="click:function(){refresh();return true;}" value="back" type="checkbox" />待决算</label>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 fix-width1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px" id="select-sales"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'user_number',event:{change:$root.refresh}, optionsCaption: '--全部--'"
										name="option.create_user"></select>
								</div>
							</div>
						</s:if>
						<s:else>
							<div class="col-md-3">&nbsp;</div>
						</s:else>
						<div style="float: right">
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
								<th>状态</th>
								<th>团号</th>
								<th>客户</th>
								<th>确认月</th>
								<th>出团日期</th>
								<th>天数</th>
								<th>产品名称</th>
								<th>人数</th>
								<th>游客信息</th>
								<th>回团天数</th>
								<th>总团款</th>
								<th>尾款</th>
								<th>确认件</th>
								<th>备注</th>
								<th>接待备注</th>
								<th>产品经理</th>
								<th>文件下载</th>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<th>销售</th>
								</s:if>
								<th>锁定</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: orders">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.pk+';'+$data.standard_flg+';'+$data.status+';'+$data.lock_flg+';'+$data.independent_flg}, checked: $root.chosenOrders" /></td>
								<td data-bind="text: $root.statusMapping[$data.status]"></td>
								<td data-bind="text: $data.team_number"></td>
								<td data-bind="text: $data.client_employee_name"></td>
								<td data-bind="text: moment($data.confirm_date).format('YYYY-MM')"></td>
								<td data-bind="text: $data.departure_date"></td>
								<td data-bind="text: $data.days"></td>
								<td data-bind="text: $data.product_name"></td>
								<td data-bind="text: $data.people_count"></td>
								<td><a href="javascript:void(0)" data-bind="click:$root.checkPassengers,text: $data.passenger_captain"></a></td>
								<td data-bind="text: $data.back_days"></td>

								<td data-bind="text: $data.receivable"></td>
								<td data-bind="text: $data.balance"></td>

								<td><a href="javascript:void(0)"
									data-bind="click: function() {$root.checkIdPic($data.confirm_file,$data.create_user_number)} ">查看</a></td>
								<!-- ko if: $data.comment==null || $data.comment==''-->
								<td><a href="javascript:void(0)"
									data-bind="click:function() {$root.editComment($data.pk,$data.standard_flg)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.comment!=null && $data.comment!=''-->
								<td data-bind="attr:{title:$data.comment}"><a href="javascript:void(0)"
									data-bind="text: $data.comment,click:function() {$root.editComment($data.pk,$data.standard_flg)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.treat_comment==null || $data.treat_comment==''-->
								<td data-bind="text:$data.treat_comment"></td>
								<!-- /ko -->
								<!-- ko if: $data.treat_comment!=null && $data.treat_comment!=''-->
								<td><a href="javascript:void(0)"
									data-bind="text: $data.treat_comment,click:function() {msg($data.treat_comment)}"></a></td>
								<!-- /ko -->
								<td data-bind="text:$data.product_manager"></td>
								<td><a href="javascript:void(0)" class="download" data-bind="click:$root.downloadFile">下载</a></td>
								<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
									<td data-bind="text:$data.create_user"></td>
								</s:if>
								<td data-bind="text:$root.lockMapping[$data.lock_flg]"></td>
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
	<div id="passengers-check" style="display: none; width: 800px; height: 450px; overflow-y: scroll;">
		<div class="input-row clearfloat">
			<div style="margin-top: 60px; height: 300px">
				<table style="width: 100%" class="table table-striped table-hover">
					<thead>
						<tr>
							<th style="width: 9%">序号</th>
							<th style="width: 10%">姓名</th>
							<th style="width: 35%">身份证号</th>
							<th style="width: 23%">电话1</th>
							<th style="width: 23%">电话2</th>
						</tr>
					</thead>
					<tbody data-bind="foreach:passengers">
						<tr>
							<td data-bind="text:$index()+1"></td>
							<td data-bind="text:$data.name"></td>
							<td data-bind="text:$data.id"></td>
							<td data-bind="text:$data.cellphone_A"></td>
							<td data-bind="text:$data.cellphone_B"></td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>

	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<input type="hidden" data-bind="value:order().pk" id="txt-order-pk" />
	<input type="hidden" data-bind="value:order().standard_flg" id="txt-standard-flg" />
	<div id="comment-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div>
				<label class="l">备注</label>
				<div class="ip">
					<textarea type="text" class="ip-default" rows="10" maxlength="200" id="txt-comment"
						data-bind="value: order().comment" placeholder="备注"></textarea>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: cancelEditComment">取消</a> <a type="submit"
					class="btn btn-green btn-r" data-bind="click: updateComment">保存</a>
			</div>
		</div>
	</div>
	<div id="first-air" style="display: none; width: 1300px">
		<div class="col-md-12">
			<table style="width: 100%" class="table table-striped table-hover">
				<thead>
					<tr>
						<th style="width: 7.5%"></th>
						<th style="width: 7.5%">航班号</th>
						<th style="width: 12.5%">起飞城市</th>
						<th style="width: 12.5%">机场</th>
						<th style="width: 12.5%">抵达城市</th>
						<th style="width: 12.5%">机场</th>
						<th style="width: 7.5%">起飞时间</th>
						<th style="width: 7.5%">抵达时间</th>
						<!-- ko if:order().next_day!=0 -->
						<th style="width: 10%">隔天达&nbsp;<input type="checkbox" checked="checked" id="chk-next-day"
							onclick="nextDay()" /></th>
						<!-- /ko -->
						<!-- ko if:order().next_day==0 -->
						<th style="width: 10%">隔天达&nbsp;<input type="checkbox" id="chk-next-day" onclick="nextDay()" /></th>
						<!-- /ko -->
						<th style="width: 10%">飞行时间</th>
					</tr>
				</thead>
				<tbody>
					<tr>
						<td>首航段：</td>
						<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().ticket_number"
							id="txt-ticket-number" /></td>
						<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().start_city"
							id="txt-start-city" /></td>
						<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().start_airport"
							id="txt-start-airport" /></td>
						<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().end_city" id="txt-end-city" /></td>
						<td><input type="text" style="width: 90%" maxlength="10" data-bind="value:order().end_airport"
							id="txt-end-airport" /></td>
						<td><input type="text" style="width: 90%" class="isTime" id="txt-off-time" data-bind="value:order().off_time"
							maxlength="5" onkeyup="caculate_fly_time()" /></td>
						<td><input type="text" style="width: 90%" class="isTime" data-bind="value:order().land_time"
							id="txt-land-time" maxlength="5" onkeyup="caculate_fly_time()" /></td>
						<!-- ko if:order().next_day==0 -->
						<td><input type="number" style="width: 90%" maxlength="2" min="1" id="txt-next-day" value="1"
							onkeyup="caculate_fly_time()" disabled="disabled" /></td>
						<!-- /ko -->
						<!-- ko if:order().next_day!=0 -->
						<td><input type="number" style="width: 90%" data-bind="value:order().next_day" maxlength="2" min="1"
							id="txt-next-day" onkeyup="caculate_fly_time()" /></td>
						<!-- /ko -->
						<td id="txt-fly-time"></td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="input-row clearfloat">
			<div align="right">
				<a type="submit" class="btn btn-green btn-r" data-bind="click: cancelWrite">取消</a> <a type="submit"
					class="btn btn-green btn-r" data-bind="click: confirmWrite">保存</a>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/order/c-order.js?v=1.2"></script>
</body>
</html>