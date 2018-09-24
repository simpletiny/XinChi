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
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				客户新建<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<input type="hidden" name="employee.review_flg" value="N" />
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">昵称</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" maxlength="10" placeholder="昵称" name="employee.nick_name" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">姓名</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" maxlength="10" placeholder="姓名" name="employee.name"
									required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" data-bind="options: genders" name="employee.sex" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">手机号</label>
							<div class="ip">
								<input type="text" class="ip- cellphone" maxlength="11" placeholder="手机号" name="employee.cellphone"
									required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">财务主体</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: employee().financial_body_name" placeholder="财务主体"
									name="employee.financial_body_name" maxlength="10" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="18" data-bind="value: employee().body_id" placeholder="身份证号"
									name="employee.id" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">出生年</label>
							<div class="ip">
								<input type="text" class="ip- date_year" maxlength="4" data-bind="value: employee().body_birth_year"
									placeholder="例：1988" name="employee.birth_year" />
							</div>
						</div>

					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">电话</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="15" data-bind="value: employee().telephone" placeholder="电话"
									name="employee.telephone" />
							</div>
						</div>
						<div class="col-md-6 ">
							<label class="l">传真</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="15" data-bind="value: employee().fax" placeholder="传真"
									name="employee.fax" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">微信</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="20" data-bind="value: employee().wechat" placeholder="微信"
									name="employee.wechat" />
							</div>
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
							<label class="l">QQ</label>
							<div class="ip">
								<input type="text" class="ip-" maxlength="15" data-bind="value: employee().qq" placeholder="QQ"
									name="employee.qq" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" maxlength="100" rows="15" data-bind="value: employee().comment"
									name="employee.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createEmployee">保存</a>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(".client").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/client/employee-creation.js"></script>
</body>
</html>