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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>收入匹配</h2>
		</div>
		<div class="main-container">
			<div class="main-box">
				<form class="form-horizontal search-panel">

					<div class="form-group">
						<div style="float: right">
							<div>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: match">主营收入</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: matchOther">其他收入</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: reject">驳回</button>
								<button id="btn-cancel" style="display: none" type="submit" class="btn btn-green col-md-1"
									data-bind="click: cancelMatch">取消匹配</button>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="ip">
							<div data-bind="foreach: allStatus" style="padding-top: 4px;">
								<em class="small-box"> <input type="radio" data-bind="attr: {value: $data },checked: $root.chosenStatus "
									onclick="changeStatus(this)" name="detail.match_flg" /><label data-bind="text: $root.statusMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">收入日期</label>
							<div class="col-md-2">
								<input type="text" class="form-control date-picker" placeholder="收入日期" name="detail.time" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">账户</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --',event: {change:refresh}"
									name="detail.account" required="required"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">发生月</label>
							<div class="col-md-2">
								<input type="text" class="form-control month-picker-st" placeholder="发生月" name="detail.month" />
							</div>
						</div>
					</div>
					<div class="form-group">
						<div align="left">
							<label class="col-md-1 control-label">金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="大于等于" name="detail.money_from" />
							</div>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="小于等于" name="detail.money_to" />
							</div>
						</div>
						<div align="left">
							<label class="col-md-1 control-label">精确金额</label>
							<div class="col-md-1" style="float: left">
								<input type="number" class="form-control" placeholder="精确金额" name="detail.money" />
							</div>
						</div>
						<div align="right" style="padding-right: 200px">
							<em class="small-box"> <input type="checkbox" id="chk-data" checked="checked"
								data-bind="click:function(){searchReceiveApply();return true}" /> <label>日期</label>
							</em> <em class="small-box"> <input type="checkbox" id="chk-account" checked="checked"
								data-bind="click:function(){searchReceiveApply();return true}" /> <label>账户</label>
							</em> <em class="small-box"> <input type="checkbox" id="chk-money" checked="checked"
								data-bind="click:function(){searchReceiveApply();return true}" /> <label>金额</label>
							</em>
						</div>
						<div style="padding-top: 3px; float: right">
							<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
							<em class="small-box"><a href="javascript:void(0)" data-bind="click: showAll">显示全部</a> </em>
						</div>
					</div>
				</form>
				<table class="table">
					<thead>
						<tr>
							<th style="width: 50%; border-right: 1px solid black; text-align: center">收入信息</th>
							<th style="width: 50%; text-align: center">填报信息</th>
						</tr>

					</thead>
					<tbody>
						<tr>
							<td style="border-right: 1px solid black">
								<div class="list-result">
									<table class="table table-striped table-hover" id="left-table">
										<thead>
											<tr role="row">
												<th></th>
												<th>账户</th>
												<th>收入时间</th>
												<th>收入</th>
												<th>备注</th>
												<th>状态</th>
											</tr>
										</thead>
										<tbody data-bind="foreach: details">
											<tr>
												<td><input type="radio" name="rad-detail"
													data-bind="attr: {'value': $data.pk}, checked: $root.chosenDetails,click:$root.checkReceived" /></td>
												<td data-bind="text: $data.account"></td>
												<td data-bind="text: $data.time"></td>
												<td data-bind="text: $data.money" class="rmb"></td>
												<td data-bind="text: $data.comment"></td>
												<!-- ko if: $data.match_flg =='Y' -->
												<td ><a href="javascript:void(0)" data-bind="text: $root.statusMapping[$data.match_flg],click:$root.showDetails" /></td>
												<!-- /ko -->
												<!-- ko ifnot: $data.match_flg =='Y'  -->
												<td  data-bind="text: $root.statusMapping[$data.match_flg]"></td>
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
							</td>
							<td>
								<div class="list-result">
									<table class="table table-striped table-hover" id="right-table">
										<thead>
											<tr role="row">
												<th></th>
												<th>金额</th>
												<th>类型</th>
												<th>收入时间</th>
												<th>我方账户</th>
												<th>收入凭证</th>
												<th>付款方</th>
												<th>摘要</th>
												<th>填报人</th>
												<th>填报日期</th>
											</tr>
										</thead>
										<tbody id="tbody-data" data-bind="foreach: receiveds">
											<tr>
												<td><input type="checkbox" data-bind="checkedValue:$data,checked: $root.chosenReceiveds" /></td>
												<td data-bind="text: $data.received" class="rmb"></td>
												<td data-bind="text: $root.typeMapping[$data.from_where+$data.type]"></td>
												<td data-bind="text: $data.received_time"></td>
												<td data-bind="text: $data.card_account"></td>
												<td><a href="javascript:void(0)"
													data-bind="click: function() {$root.checkVoucherPic($data.voucher_file,$data.received_time,$data.from_where)} ">查看</a></td>
												<!-- ko if: $data.type == 'SUM' -->
												<td><a href="javascript:void(0)"
													data-bind="event:{click:function(){$root.viewDetail($data.related_pk,$data.from_where)}}">详情</a></td>
												<!-- /ko -->
												<!-- ko if: $data.type != 'SUM' -->
												<td data-bind="text: $data.pay_user"></td>
												<!-- /ko -->
												<td><a href="javascript:void(0)" data-bind="event:{click:function(){$root.viewComment($data)}}">详情</a></td>
												<td data-bind="text: $data.apply_user"></td>
												<td data-bind="text: $data.apply_date"></td>
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
							</td>
						</tr>
					</tbody>
				</table>

			</div>
		</div>
	</div>
	<div id="sum_detail" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">销售</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:received().user_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">填报日期</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text: moment(received().create_time-0).format('YYYY-MM-DD')" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">账户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().card_account"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">入账总金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().sum_received" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">入账时间</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().received_time"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">我组金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().allot_received" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">团号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">客户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">分配金额</label>
			</div>
		</div>
		<!-- ko foreach:sumDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.team_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.received" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="comment" style="display: none; width: 800px; padding-top: 30px;">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">销售</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:received().user_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">填报日期</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text: moment(received().create_time-0).format('YYYY-MM-DD')" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">团号</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().team_number"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">客户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().client_employee_name" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">产品</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().product_name"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">人数</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().adult_count+(order().special_count==null?0:order().special_count)"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">出团日期</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:order().departure_date"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">备注</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:comment()"></p>
				</div>
			</div>
		</div>
	</div>
	<div id="sum_detail1" style="display: none; width: 800px; padding-top: 30px;padding-bottom: 30px">
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">账户</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().card_account"></p>
				</div>
			</div>
			<div class="col-md-6">
				<label class="l" style="width: 30%">收入总金额</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().sum_received" class="rmb"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-6">
				<label class="l" style="width: 30%">收入时间</label>
				<div class="ip" style="width: 70%">
					<p class="ip-default" data-bind="text:sumDetail().received_time"></p>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<div class="col-md-3">
				<label class="l" style="width: 100%">业务单号</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">客户</label>
			</div>
			<div class="col-md-3">
				<label class="l" style="width: 100%">分配金额</label>
			</div>
		</div>
		<!-- ko foreach:sumDetails -->
		<div class="input-row clearfloat" st="allot">
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.business_number"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.pay_user"></p>
				</div>
			</div>
			<div class="col-md-3">
				<div class="ip">
					<p class="ip-default" data-bind="text:$data.received" class="rmb"></p>
				</div>
			</div>
		</div>
		<!-- /ko -->
	</div>
	<div id="comment1" style="display: none; width: 800px; padding-top: 30px;">
		<div  data-bind="foreach: orders">
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">团号</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.team_number"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">客户</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.client_employee_name" class="rmb"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">产品</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.product_name"></p>
					</div>
				</div>
				<div class="col-md-6">
					<label class="l" style="width: 30%">人数</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.adult_count+($data.special_count==null?0:$data.special_count)" class="rmb"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">出团日期</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.departure_date"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-6">
					<label class="l" style="width: 30%">备注</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:$data.comment"></p>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div id="pic-check" style="display: none">
		<jsp:include page="../common/check-picture.jsp" />
	</div>
	<script>
		$(".accounting").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/vendor/jquery-ui.min.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/MonthPicker.min.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/finance/received-match.js?v1.001"></script>
</body>
</html>