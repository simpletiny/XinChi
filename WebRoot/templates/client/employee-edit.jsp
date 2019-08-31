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
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/cropper/cropper.css" />
<style>
.progress {
	display: none;
	margin-bottom: 1rem;
}

.img-container img {
	max-width: 100%;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				客户资料编辑<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<input type="hidden" id="employee_key" name="employee.pk" value="<%=key%>" /> <input type="hidden"
						name="employee.public_flg" data-bind="value: employee().public_flg" />
					
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">昵称</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" data-bind="value: employee().nick_name" placeholder="昵称"
									name="employee.nick_name" />
							</div>
						</div>
						<label class="label" style="cursor: pointer" data-toggle="tooltip" title="更换头像">
						 	<img style="width: 100px; height: 100px" class="rounded" id="avatar" src="<%=basePath%>static/img/head.jpg" alt="avatar" /> 
						 	<input type="file" class="sr-only" id="input" name="image" accept="image/*" />
						 	<input type="hidden" name="employee.head_photo" data-bind="value:employee().head_photo" id="head"/>
						</label>
						<div class="modal fade" id="modal" tabindex="-1" role="dialog" aria-labelledby="modalLabel" aria-hidden="true">
							<div class="modal-dialog" role="document">
								<div class="modal-content">
									<div class="modal-header">
										<h5 class="modal-title" id="modalLabel">上传头像</h5>
										<button type="button" class="close" data-dismiss="modal" aria-label="Close">
											<span aria-hidden="true">&times;</span>
										</button>
									</div>
									<div class="modal-body">
										<div class="img-container">
											<img id="image" src="<%=basePath%>static/img/head.jpg" />
										</div>
									</div>
									<div class="modal-footer">
										<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>
										<button type="button" class="btn btn-primary" id="crop">上传</button>
									</div>
								</div>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">姓名</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" data-bind="value: employee().name" placeholder="姓名"
									name="employee.name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" data-bind="options: genders,value: employee().sex" name="employee.sex"
									required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">手机号1</label>
							<div class="ip">
								<input type="text" class="ip- cellphone" maxlength="11" data-bind="value: employee().cellphone"
									placeholder="手机号" name="employee.cellphone" required="required" onblur="checkCellphone(this)" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">手机号2</label>
							<div class="ip">
								<input type="text" class="ip- cellphone" maxlength="11" data-bind="value: employee().cellphone1"
									placeholder="手机号" name="employee.cellphone1" onblur="checkCellphone(this)" />
							</div>
						</div>

					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">微信1</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().wechat" required="required" placeholder="微信" name="employee.wechat"
									onblur="checkWechat(this)" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">微信2</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().wechat1" placeholder="微信" name="employee.wechat1"
									onblur="checkWechat(this)" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">财务主体</label>
							<!-- ko if: employee().review_flg == "Y" -->
							<div class="ip">
								<input type="text" class="ip- date-picker"
									data-bind="value: employee().financial_body_name,click:choseFinancial" placeholder="点击选择"
									name="employee.financial_body_name" id="financial_body_name" required="required" /> <input type="text"
									class="ip- date-picker" data-bind="value: employee().financial_body_pk,click:choseFinancial"
									style="display: none" name="employee.financial_body_pk" id="financial_body_pk" required="required" />
							</div>
							<!-- /ko -->
							<!-- ko if: employee().review_flg == "N" -->
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().financial_body_name" placeholder="财务主体"
									name="employee.financial_body_name" maxlength="10" required="required" />
							</div>
							<!-- /ko -->

						</div>

						<div class="col-md-6 required">
							<label class="l">类型</label>
							<div class="ip">
								<select class="form-control" data-bind="options: employeeType, value: employee().type" name="employee.type"
									required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().id" placeholder="身份证号" name="employee.id" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">出生年</label>
							<div class="ip">
								<input type="text" class="ip- date_year" maxlength="4" data-bind="value: employee().birth_year"
									placeholder="出生年" name="employee.birth_year" />
							</div>
						</div>

					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">电话</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().telephone" placeholder="电话"
									name="employee.telephone" />
							</div>
						</div>
						<div class="col-md-6 ">
							<label class="l">传真</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().fax" placeholder="传真" name="employee.fax" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">QQ</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().qq" placeholder="QQ" name="employee.qq" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: employee().comment" name="employee.comment"
									placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: saveEmployee">保存</a>
				</div>
			</div>
		</div>
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
								<th>财务主体简称</th>
								<th>负责人</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: clients">
							<tr data-bind="event: {click: function(){ $parent.pickFinancial($data.client_short_name,$data.pk)}}">
								<td data-bind="text: $data.client_short_name"></td>
								<td data-bind="text: $data.body_name"></td>
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
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/bootstrap.bundle.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/cropper/cropper.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/client/employee-edit.js"></script>
</body>
</html>