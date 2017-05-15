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

</head>
<body>
	<div class="main-body">
		<input type="hidden" id="agency_key" value="<%=key%>" />
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				查看旅游公司信息<a  href="javascript:void(0)" onclick="javascript:history.go(-1);return false;"  class="cancel-create">返回</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<hr>
				<div class="form-box info-form">
					<h3>基本信息</h3>
					<div class="input-row clearfloat">

						<div class="col-md-6">
							<label class="l">公司名称</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().agency_name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">信用代码</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().credit_code"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">所属地区</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().agency_provice+'  '+agency().agency_city"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">出境资质</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: isMapping[agency().is_exit]"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">主营业务</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().main_bussines"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">公司类型</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().agency_type"></p>
							</div>
						</div>
					</div>

					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="ll">备注</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: agency().comment"></p>
							</div>
						</div>
					</div>
					<hr />
					<h3>法人信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">法人姓名</label>
							<div class="ip">
								<p class="ip-" data-bind="text: agency().corporate_name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">性别</label>
							<div class="ip">
								<p class="ip-" data-bind="text: agency().corporate_sex"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">手机号</label>
							<div class="ip">
								<p class="ip-" data-bind="text: agency().corporate_cellphone"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<p class="ip-" data-bind="text: agency().corporate_id"></p>
							</div>
						</div>
					</div>
					<hr />
					<h3>相关文件</h3>
					<h4>营业执照</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" type="hidden" id="txt-licence" />
					</div>
					<h4>经营许可证</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" type="hidden" id="txt-permit" />
					</div>
					<h4>责任险</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" type="hidden" id="txt-insurance" />
					</div>
					<h4>法人身份证</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" type="hidden" id="txt-corporate" />
					</div>
					<h4>负责人身份证</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" type="hidden" id="txt-chief" />
					</div>
					<h4>其他文件</h4>
					<div class="input-row clearfloat">
						<input st="st-file-name" name="file6" type="hidden" id="txt-other" />
					</div>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".agency").addClass("current").children("ol").css("display", "block");
	</script>
	<script src="<%=basePath%>static/js/client/agency-detail.js"></script>
</body>
</html>