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
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}
</style>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>客户资料</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form id="form-search" class="form-horizontal search-panel">

					<div class="form-group">
						<div style="width: 85%; float: right">
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createClientEmployee() }">新建</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editEmployee() }">维护</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { stopEmployee() }">停用</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { publicEmployee() }">公开</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { combineEmployee() }">合并</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { jobHopping() }">跳槽</button>
							<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { dimission() }">离职</button>
							<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { changeSales()}">调整销售</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { deleteEmployee()  }">删除</button>
								<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { reviewEmployee()  }">审核</button>
							</s:if>
						</div>
					</div>
					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">姓名</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="姓名" maxlength="20" name="employee.name" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">地区</label>
							<div class="col-md-2">
								<select class="form-control" data-bind="options: employeeArea, optionsCaption: '-- 请选择 --'" name="employee.area"></select>
							</div>
						</div>
						<div class="span6">
							<label class="col-md-2 control-label">财务主体简称</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="财务主体简称" name="employee.financial_body_name" />
							</div>
						</div>
						<div class="span6">
							<div data-bind="foreach: status">
								<em class="small-box "> <input name="employee.delete_flgs" type="checkbox"
									data-bind="attr: {'value': $data},checked:$root.chosenStatus, event:{click:function(){$root.refresh();return true}}" /><label
									data-bind="text: $root.deleteMapping[$data]"></label>
								</em>
							</div>
						</div>
					</div>

					<div class="form-group">
						<div class="span6">
							<label class="col-md-1 control-label">手机号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="手机号" maxlength="20" name="employee.cellphone" />
							</div>
						</div>
						<div class="span6">
							<label class="col-md-1 control-label">微信号</label>
							<div class="col-md-2">
								<input type="text" class="form-control" placeholder="微信号" maxlength="20" name="employee.wechat" />
							</div>
						</div>
						<s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">

							<div class="span6">
								<label class="col-md-1 control-label">销售</label>
								<div class="col-md-2">
									<select class="form-control" style="height: 34px"
										data-bind="options: sales,  optionsText: 'user_name', optionsValue: 'pk', optionsCaption: '--全部--',event:{change:function(){$root.refresh();$root.refreshSumCnt()}}"
										name="employee.sales"></select>
								</div>
							</div>
						</s:if>
						<div class="span6">
							<div class="col-md-2">
								<em class="small-box "> <input type="checkbox" id="chk_public"
									data-bind="event:{click:function(){refresh();return true;}}" /><label>公开</label>
								</em>
							</div>
						</div>

						<div class="span6" style="float: right">
							<div style="padding-top: 3px;">
								<button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
							</div>
						</div>
					</div>

				</form>
				<div class="list-result" style="width: 50%">
					<table class="table table-striped table-hover">
						<thead>
							<tr>
								<td width="8.33%" style="color: green">总数</td>
								<td width="8.33%" data-bind="text:rld().sum_cnt"></td>
								<td width="8.33%" style="color: green">强</td>
								<td width="8.33%" data-bind="text:rld().strong_cnt"></td>
								<td width="8.33%" style="color: green">中</td>
								<td width="8.33%" data-bind="text:rld().middle_cnt"></td>
								<td width="8.33%" style="color: green">弱</td>
								<td width="8.33%" data-bind="text:rld().weak_cnt"></td>
								<td width="8.33%" style="color: green">差</td>
								<td width="8.33%" data-bind="text:rld().bad_cnt"></td>
								<td width="8.33%" style="color: green">未知</td>
								<td width="8.33%" data-bind="text:rld().unknow_cnt"></td>
							</tr>
						</thead>
					</table>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th></th>
								<th>头像</th>
								<th>昵称</th>
								<th>姓名</th>
								<th>性别</th>
								<th>类型</th>
								<th>状态</th>
								<th>地区</th>
								<th>财务主体简称</th>
								<th>年单</th>
								<th>回款誉</th>
								<th>签单期间</th>
								<th>公开日期</th>
								<th>手机号</th>
								<th>微信号</th>
								<th>备注</th>
								<th>所属销售</th>
								<th>审核</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: employees">
							<tr>
								<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenEmployees" /></td>
								<td><img style="width:25px;height:25px" data-bind="click: function() {$parent.checkHeadPhoto($data.head_photo)}" src="<%=basePath%>static/img/head.jpg" /><input type="hidden" st="st-file-name" data-bind="value:$data.head_photo"/></td>
								<td data-bind="text: $data.nick_name"></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.name,attr: {href: 'employee-detail.jsp?key='+$data.pk}"></a></td>
								<td data-bind="text: $data.sex"></td>
								<td data-bind="text: $data.type"></td>
								<!-- ko if:$data.delete_flg =='Y' -->
								<td style="color: red">停用</td>
								<!-- /ko -->

								<!-- ko if:$data.delete_flg =='N' -->
								<td style="color: green">正常</td>
								<!-- /ko -->

								<td data-bind="text: $data.area"></td>
								<td><a href="javascript:void(0)"
									data-bind="text: $data.financial_body_name,click:function(){ $root.checkFinancialBody($data.financial_body_pk);}"></a></td>
								<td data-bind="text: $data.year_order_count"></td>
								<td data-bind="text: $data.back_level"></td>
								<td data-bind="text: $data.last_order_period"></td>
								<!-- ko if:$data.public_flg =='Y' -->
								<td data-bind="text: moment($data.useful_time-0).format('YYYY-MM-DD')"></td>
								<!-- /ko -->
								<!-- ko if:$data.public_flg =='N' -->
								<td></td>
								<!-- /ko -->
								<td data-bind="text: $data.cellphone"></td>
								<td data-bind="text: $data.wechat"></td>
								<!-- ko if: $data.comment==null || $data.comment==''-->
								<td><a href="javascript:void(0)" data-bind="click:function() {$root.editComment($data.pk)}">添加</a></td>
								<!-- /ko -->
								<!-- ko if: $data.comment!=null && $data.comment!=''-->
								<td data-bind="attr:{title:$data.comment}"><a href="javascript:void(0)"
									data-bind="text: $data.comment,click:function() {$root.editComment($data.pk)}">添加</a></td>
								<!-- /ko -->

								<!-- ko if:$data.public_flg =='Y' -->
								<td data-bind="text: $data.sales_name" style="color: red"></td>
								<!-- /ko -->
								<!-- ko if:$data.public_flg =='N' -->
								<td data-bind="text: $data.sales_name"></td>
								<!-- /ko -->
								<!-- ko if:$data.review_flg =="Y" -->
								<td style="color: green">是</td>
								<!-- /ko -->
								<!-- ko if:$data.review_flg =="N" -->
								<td style="color: red">否</td>
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
	<div id="job-hopping" style="display: none; width: 350px">
		<form id="form-hopping">
			<input type="hidden" data-bind="value:employee().pk" name="employee.pk" />
			<div class="input-row clearfloat">
				<div class="col-md-12">
					<label class="l" style="width: 30%">原</label>
					<div class="ip" style="width: 70%">
						<p class="ip-default" data-bind="text:employee().financial_body_name"></p>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 30%">新</label>
					<div class="ip" style="width: 70%">
						<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="financial_body_name"
							required="required" /> <input type="text" class="ip-" style="display: none" name="employee.new_client_pk"
							id="financial_body_pk" required="required" />
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12">
					<label class="l" style="width: 30%">日期</label>
					<div class="ip" style="width: 70%">
						<input type="text" data-bind="" class="form-control date-picker" name="employee.hopping_date" placeholder="日期"></input>
					</div>
				</div>
			</div>
			<div class="input-row clearfloat">
				<div class="col-md-12" style="margin-top: 10px">
					<div align="right">
						<a type="button" class="btn btn-green btn-r" data-bind="click: doJobHopping">确定</a> <a type="button"
							class="btn btn-green btn-r" data-bind="click: cancelJobHopping">取消</a>
					</div>
				</div>
			</div>
		</form>
	</div>
	<div id="financial_pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">主体简称</label>
						<div class="col-md-6">
							<input type="text" id="client_name" class="form-control" placeholder="主体简称" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchFinancial }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>主体简称</th>
								<th>地区</th>
								<th>负责人</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: clients">
							<tr data-bind="event: {click: function(){ $parent.pickFinancial($data.client_short_name,$data.pk)}}">
								<td data-bind="text: $data.client_short_name"></td>
								<td data-bind="text: $data.client_area"></td>
								<td data-bind="text: $data.body_name"></td>
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
	<div id="edit-sale" style="display: none">
		<input type="hidden" id="current-pk" />
		<div class="input-row clearfloat">
			<div class="col-md-12 required">
				<label class="l">选择销售</label>
				<div class="ip">
					<div data-bind="foreach: sales" style="padding-top: 4px;">
						<em class="small-box"> <input type="checkbox"
							data-bind="attr: {'value': $data.pk}, checked: $root.chosenUser,click:$root.checkSale" /> <!-- ko if: $data.user_name =='公开' -->
							<label style="color: red" data-bind="text: $data.user_name"></label> <!-- /ko --> <!-- ko if: $data.user_name !='公开' -->
							<label data-bind="text: $data.user_name"></label> <!-- /ko -->
						</em>
					</div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat" style="float: right">
			<a type="submit" class="btn btn-green btn-r" data-bind="click: doChangeSale">保存</a> <a type="submit"
				class="btn btn-green btn-r" data-bind="click: doCancelChangeSale">取消</a>
		</div>
	</div>
	<div id="div-review" style="display: none">
		<form id="form-review">
			<input type="hidden" data-bind="value:employee().pk" name="employee.pk" /> <input type="hidden"
				data-bind="value:employee().name" name="employee.name" />
			<div class="input-row clearfloat">
				<div class="col-md-12 required">
					<label class="l" style="width: 30%">财务主体</label>
					<div class="ip" style="width: 70%">
						<input type="text" class="ip-" data-bind="click:choseFinancial" placeholder="点击选择" id="financial_body_name1"
							required="required" /> <input type="text" class="ip-" style="display: none" id="financial_body_pk1"
							name="employee.financial_body_pk" required="required" />
					</div>
				</div>
			</div>
		</form>
		<div class="input-row clearfloat" style="float: right">
			<a type="submit" class="btn btn-green btn-r" data-bind="click: doReview">保存</a> <a type="submit"
				class="btn btn-green btn-r" data-bind="click: cancelReview">取消</a>
		</div>
	</div>
	<div id="check-financial" style="display: none">
		<div class="clearfloat" style="width: 100%">
			<div class="col-md-12">
				<label class="l">财务主体</label>
				<div class="ip">
					<p class="ip-default" data-bind="text: financial().client_short_name"></p>
				</div>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">全称</label>
				<p class="ip-default" data-bind="text: financial().client_name"></p>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">负责人</label>
				<p class="ip-default" data-bind="text: financial().body_name"></p>
			</div>
		</div>
		<div class="clearfloat">
			<div class="col-md-12">
				<label class="l">地址</label>
				<p class="ip-default" data-bind="text: financial().address"></p>
			</div>
		</div>
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
	<div id="pic-check" style="display:none">
 		<jsp:include page="../common/check-picture.jsp" />
 	</div>
	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>
	<script src="<%=basePath%>static/js/client/client-employee.js"></script>
</body>
</html>