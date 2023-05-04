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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/css/jquery-ui.css" />
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.detail {
	color: blue;
}

.warning {
	color: red;
}

#table-relations tr td {
	text-overflow: ellipsis; /* for IE */
	-moz-text-overflow: ellipsis; /* for Firefox,mozilla */
	overflow: hidden;
	white-space: nowrap;
	text-align: left
}

.fixed {
	font-size: 12px;
	display: block;
	position: fixed;
	right: 0px;
	top: 200px;
	margin-left: 10px;
	z-index: 100;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>客户关系管理</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel" id="form-search">
					<div class="form-group">
						<button type="submit" class="btn btn-green" data-bind="click: function() { createVisit() }">登门拜访</button>
						<button type="submit" class="btn btn-green" data-bind="click: function() { createMobileTouch() }">主动电联</button>
						<button type="submit" class="btn btn-green " data-bind="click: function() { createIncomingCall() }">被动咨询</button>
						<button type="submit" class="btn btn-green " data-bind="click: function() { setClientLevel() }">销售力调整</button>
						<button type="submit" class="btn btn-green " data-bind="click: function() { reimbursement() }">费用申请</button>
						<button type="submit" class="btn btn-green " data-bind="click: function() { quitConnect() }">放弃维护</button>
						<button type="submit" class="btn btn-green " data-bind="click: function() {createEmployee() }">新增客户</button>
						<button type="submit" class="btn btn-yellow " data-bind="click: function() {querySysClient() }">系统客户查询</button>
						<!-- ko if: canEdit()-->
						<button type="submit" class="btn btn-green " data-bind="click: function() {editEmployee()}">维护</button>
						<!-- /ko -->
						<label class="col-md-1 control-label">当月分值</label>
						<div class="col-md-1">
							<p class="ip-default" data-bind="text: saleScore()"></p>
						</div>
						<label class="col-md-1 control-label">当日勤点</label>
						<div class="col-md-1">
							<p class="ip-default" data-bind="text: todayPoint()"></p>
						</div>
					</div>

					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">客户</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="客户" maxlength="20" name="relation.client_employee_name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">关系度</label>
							<div class="col-md-2">
								<select class="form-control" style="height: 34px"
									data-bind="options: level, optionsCaption: '全部',value:chosenLevel,event:{change:refresh}"
									name="relation.relation_level"></select>
							</div>
						</div>
						<div class="span6">
							<div class="col-md-1">
								<em class="small-box "> <input type="checkbox" value="Y" checked="checked" name="relation.new_class_status"
									data-bind="event:{click:function(){refresh();return true;}}" /><label>新增</label>
								</em>
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'pk', optionsCaption: '--全部--',event:{change:function(){fetchSummary()}}"
										name="relation.sales"></select>
								</div>
							</div>
						</s:if>

					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">排序方式</label>
							<div data-bind="foreach: sortTypes" class="col-md-4">
								<em class="small-box "> <input name="relation.sort_type" type="radio"
									data-bind="attr: {value: $data}, checked: $root.chosenSortType,event:{click:$root.changeSortType}" /><label
									data-bind="text: $root.sortTypeMapping[$data]"></label>
								</em>
							</div>
						</div>
						<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: function() { refresh() }">搜索</button>
					</div>
				</form>


				<div class="list-result">
					<table id="table-relations" class="table table-striped table-hover">
						<thead>
							<tr>
								<th></th>
								<th colspan="4" style="border-right: solid 1px #ff0000; text-align: center"><h3>潜力值</h3></th>
								<th colspan="4" style="border-right: solid 1px #ff0000; text-align: center"><h3>努力度</h3></th>
								<th colspan="7" style="text-align: center"><h3>仪表盘</h3></th>
							</tr>
							<tr>
								<th></th>
								<th>全部</th>
								<th>电话</th>
								<th>微信</th>
								<th style="border-right: solid 1px #ff0000;">广告</th>
								<th>订单</th>
								<th>拜访</th>
								<th>电话</th>
								<th style="border-right: solid 1px #ff0000;">微信</th>
								<!-- <th style="border-right: solid 1px #ff0000;">忽略</th> -->
								<th>日均占款</th>
								<th>资金费用</th>
								<th>应收款</th>
								<th>预警</th>
								<th>本月呆账</th>
								<th>本月罚息</th>
							</tr>
						</thead>
						<tbody id="tbody-data">
							<tr>
								<td>全部</td>
								<td data-bind="text:potential().all_all"></td>
								<td data-bind="text:potential().all_tel"></td>
								<td data-bind="text:potential().all_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().all_ad"></td>
								<td data-bind="text:workOrder().all_order"></td>
								<td data-bind="text:accurateSale().all_accurate"></td>
								<td data-bind="text:incomingCount().all_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().all_wechat"></td>
								<td data-bind="text:meter().day_hold" class="rmb"></td>
								<td></td>
								<td data-bind="text:meter().receivable" class="rmb"></td>
								<td data-bind="text:meter().warning" class="rmb warning"></td>
								<td data-bind="text:meter().bad" class="rmb warning"></td>
								<td data-bind="text:meter().bad_interest" class="rmb warning"></td>
							</tr>
							<tr>
								<td>主力</td>
								<td data-bind="text:potential().main_all"></td>
								<td data-bind="text:potential().main_tel"></td>
								<td data-bind="text:potential().main_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().main_ad"></td>
								<td data-bind="text:workOrder().main_order"></td>
								<td data-bind="text:accurateSale().main_accurate"></td>
								<td data-bind="text:incomingCount().main_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().main_wechat"></td>
								<th>发展基金</th>
								<th>新增费用</th>
								<th>新增基金</th>
								<th>累计费用</th>
								<th>勤点扣款</th>
								<th></th>
							</tr>
							<tr>
								<td>市场</td>
								<td data-bind="text:potential().market_all"></td>
								<td data-bind="text:potential().market_tel"></td>
								<td data-bind="text:potential().market_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().market_ad"></td>
								<td data-bind="text:workOrder().market_order"></td>
								<td data-bind="text:accurateSale().market_accurate"></td>
								<td data-bind="text:incomingCount().market_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().market_wechat"></td>
								<td data-bind="text:meter().score * 1.07
					- meter().sum_reimbursement" class="rmb"></td>
								<td data-bind="text:meter().month_reimbursement" class="rmb"></td>
								<td data-bind="text:meter().month_score * 1.07" class="rmb"></td>
								<td data-bind="text:meter().sum_reimbursement" class="rmb"></td>
								<td data-bind="text:meter().point_money_deduct" class="rmb"></td>
								<td></td>
							</tr>

							<tr>
								<td>尝试</td>
								<td data-bind="text:potential().try_all"></td>
								<td data-bind="text:potential().try_tel"></td>
								<td data-bind="text:potential().try_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().try_ad"></td>
								<td data-bind="text:workOrder().try_order"></td>
								<td data-bind="text:accurateSale().try_accurate"></td>
								<td data-bind="text:incomingCount().try_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().try_wechat"></td>
								<th>收客分值</th>
								<th>回款分值</th>
								<th>当月分值</th>
								<th>本月坏账</th>
								<th>累计坏账</th>
								<th></th>
							</tr>
							<tr>
								<td>忽略</td>
								<td data-bind="text:potential().ignore_all"></td>
								<td data-bind="text:potential().ignore_tel"></td>
								<td data-bind="text:potential().ignore_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().ignore_ad"></td>
								<td data-bind="text:workOrder().ignore_order"></td>
								<td data-bind="text:accurateSale().ignore_accurate"></td>
								<td data-bind="text:incomingCount().ignore_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().ignore_wechat"></td>
								<td data-bind="text:meter().month_score"></td>
								<td data-bind="text:meter().back_score"></td>
								<td data-bind="text:meter().month_score+meter().back_score"></td>
								<td data-bind="text:meter().month_dead" class="rmb warning"></td>
								<td data-bind="text:meter().sum_dead" class="rmb warning"></td>
								<td></td>
							</tr>
							<tr>
								<td>新增</td>
								<td data-bind="text:potential().new_all"></td>
								<td data-bind="text:potential().new_tel"></td>
								<td data-bind="text:potential().new_wechat"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:potential().new_ad"></td>
								<td data-bind="text:workOrder().new_order"></td>
								<td data-bind="text:accurateSale().new_accurate"></td>
								<td data-bind="text:incomingCount().new_tel"></td>
								<td style="border-right: solid 1px #ff0000;" data-bind="text:incomingCount().new_wechat"></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
								<td></td>
							</tr>
						</tbody>

					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>头像</th>
								<th>客户姓名</th>
								<th>昵称</th>
								<th>年单</th>
								<th>关系度</th>
								<th>回款誉</th>
								<th>销售力</th>
								<th>签单期间</th>
								<th>最近交流日期</th>
								<th>交流方式</th>
								<th>交流信息</th>
								<th>应收款总计</th>
								<th>最长账期</th>
								<th>备注</th>
								<th>销售</th>
							</tr>
						</thead>
						<tbody id="tbody-data" data-bind="foreach: relations">
							<tr>
								<td><input type="checkbox"
									data-bind="attr: {'value': $data.client_employee_pk+';'+$data.client_employee_name}, checked: $root.chosenEmployee" /></td>
								<td><img style="width: 25px; height: 25px" src="<%=basePath%>static/img/head.jpg"
									data-bind="click: function() {$parent.checkHeadPhoto($data.head_photo)}" /><input type="hidden"
									st="st-file-name" data-bind="value:$data.head_photo" /></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.client_employee_name,attr: {href: 'employee-detail.jsp?key='+$data.client_employee_pk}"></a></td>

								<td data-bind="text: $data.nick_name"></td>
								<td data-bind="text: $data.year_order_count"></td>
								<td data-bind="text: $data.relation_level"></td>
								<td data-bind="text: $data.back_level"></td>
								<td data-bind="text: $data.market_level"></td>

								<td data-bind="text: $data.last_order_period"></td>
								<td data-bind="text: $data.connect_date"></td>
								<td data-bind="text: $root.connectTypeMapping[$data.type]"></td>
								<td><a
									data-bind="text: $data.extra_info,click:function(){$root.checkConnectInfo($data.client_employee_pk);}"></a></td>
								<td class="rmb" data-bind="text: $data.receivable"></td>
								<td data-bind="text: $data.last_receivable_period"></td>
								<!-- ko if: $data.comment==null || $data.comment==''-->
								<td><a href="javascript:void(0)" data-bind="click:function() {$root.editComment($data.client_employee_pk)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.comment!=null && $data.comment!=''-->
								<td data-bind="attr:{title:$data.comment}"><a href="javascript:void(0)"
									data-bind="text: $data.comment,click:function() {$root.editComment($data.client_employee_pk)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.sales=='public'-->
								<td style="color: red" data-bind="text: $data.sales_name"></td>
								<!-- /ko -->
								<!-- ko if: $data.sales!='public'-->
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
	<div id="connect-info" style="display: none;">
		<table class="table table-striped table-hover">
			<thead>
				<tr role="row">
					<th style="width: 15%">日期</th>
					<th style="width: 15%">方式</th>
					<th style="width: 70%">效果</th>
				</tr>
			</thead>
			<tbody id="tbody-data" data-bind="foreach: connects">
				<tr>
					<td data-bind="text: $data.connect_date"></td>
					<td data-bind="text: $root.connectTypeMapping[$data.type]"></td>
					<td data-bind="text: $data.extra_info"></td>
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

	<div id="todo-create" style="display: none">
		<div class="input-row clearfloat" style="width: 400px;">
			<div class="ip">
				<input type="text" id="todo_content" class="form-control" maxlength="10" placeholder="十个字以内" /> <input
					type="hidden" id="client_employee_pk" />
			</div>
		</div>
		<div class="input-row clearfloat" style="width: 400px;">
			<div class="ip" style="float: right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doCreateToDo}" value="保存"></input> <input
					type="button" data-bind="event:{click:cancelCreateToDo}" class="btn btn-green col-md-1" value="取消"></input>
			</div>
		</div>
	</div>
	<div id="client-market-level" style="display: none; width: 300px">
		<div class="form-horizontal search-panel">
			<div class="form-group" style="width: 300px">
				<div class="span6">
					<label class="col-md-3 control-label">销售力</label>
					<div class="col-md-9">
						<select class="form-control" data-bind="options: marketLevel,value:chosenMarketLevel" name="relation.level"></select>
					</div>
				</div>
			</div>
			<div class="form-group" style="float: right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doSetClientLevel}" value="保存"></input> <input
					type="button" data-bind="event:{click:cancelSetClientLevel}" class="btn btn-green col-md-1" value="取消"></input>
			</div>
		</div>
	</div>
	<div id="query-sys-client" style="display: none; width: 600px">
		<div class="form-horizontal search-panel">
			<div class="form-group col-md-6">
				<div class="span6">
					<label class="col-md-3 control-label">微信号</label>
					<div class="col-md-9">
						<input type="text" class="form-control" maxlength="20" id="txt-wechat" placeholder="微信号（区分大小写）" />
					</div>
				</div>
			</div>
			<div class="form-group col-md-6">
				<div class="span6">
					<label class="col-md-3 control-label">手机</label>
					<div class="col-md-9">
						<input type="text" class="form-control" maxlength="11" id="txt-cellphone" placeholder="手机号" />
					</div>
				</div>
			</div>
			<div class="form-group col-md-6" style="float: right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doQuery}" value="查询"></input> <input
					type="button" data-bind="event:{click:cancelQuery}" class="btn btn-green col-md-1" value="取消"></input>
			</div>
		</div>
	</div>
	<%-- 	<div id="client-level" style="display: none; width: 800px">
		<div class="form-horizontal search-panel">
			<div class="form-group" style="width: 800px">
				<div class="span6">
					<label class="col-md-1 control-label">关系度</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: relationLevel, value:chosenRelationLevel" name="relation.level"></select>
					</div>
				</div>
				<div class="span6">
					<label class="col-md-1 control-label">市场力</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: marketLevel,value:chosenMarketLevel" name="relation.level"></select>
					</div>
				</div>
				<div class="span6">
					<label class="col-md-1 control-label">回款誉</label>
					<div class="col-md-3">
						<select class="form-control" data-bind="options: backLevel,value:chosenBackLevel" name="relation.level"></select>
					</div>
				</div>
			</div>
			<div class="form-group" style="float: right">
				<input type="button" class="btn btn-green col-md-1" data-bind="event:{click:doSetClientLevel}" value="保存"></input> <input
					type="button" data-bind="event:{click:cancelSetClientLevel}" class="btn btn-green col-md-1" value="取消"></input>
			</div>
		</div>
	</div> --%>
	<div id="quit-connect" style="display: none">
		<form id="form-quit">
			<input type="hidden" data-bind="value:quit().client_employee_pk" name="quit.client_employee_pk" />
			<div class="input-row clearfloat">
				<div class="col-md-12">
					<label class="l" style="width: 20%">客户:</label>
					<div class="ip">
						<p class="ip-default" data-bind="text:quit().client_employee_name"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 30%">放弃原因：</label>
					<div class="ip" style="width: 60%">
						<select class="form-control" data-bind="options: reasons" name="quit.reason" required="required"></select>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat" style="width: 400px;">
				<div class="ip" style="float: right">
					<input type="button" class="btn btn-green col-md-1" data-bind="click:doQuit" value="确定"></input> <input
						type="button" data-bind="click:cancelQuit" class="btn btn-green col-md-1" value="取消"></input>
				</div>
			</div>
		</form>
	</div>
	<div id="comment-edit" style="display: none; width: 500px">
		<div class="input-row clearfloat">
			<div>
				<label class="l">备注</label>
				<div class="ip">
					<textarea type="text" class="ip-default" rows="10" maxlength="100" id="txt-comment"
						data-bind="value: clientEmployee().comment" placeholder="备注"></textarea>
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
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/client-relation.js"></script>
</body>
</html>