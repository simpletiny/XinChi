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
<style type="text/css">
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
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				旅游公司编辑<a href="<%=basePath%>/templates/client/agency.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<input type="hidden" id="agency_key" name="agency.pk" value="<%=key%>" />
					<h3>基本信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">公司全称</label>
							<div class="ip">
								<input type="text" id="name" class="ip-default" data-bind="value: agency().agency_name" placeholder="公司全称" onblur="checkSame(this,'NAME')" name="agency.agency_name" required="required" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">信用代码</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: agency().credit_code" placeholder="信用代码" onblur="checkSame(this,'CODE')" name="agency.credit_code" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">地区</label>
							<div class="ip" style="width: 35%">
								<select class="form-control" data-bind="options: provices, optionsCaption: '-- 省份--',value: agency().agency_provice,event:{change:ter}" name="agency.agency_provice" required="required"></select>
							</div>
							<div class="ip" style="width: 35%">
								<select class="form-control" id="city" name="agency.agency_city" required="required">
									<option value>-- 市--</option>
								</select>
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">出境资质</label>
							<div class="ip">
								<em class="small-box"> <input type="radio" name="agency.is_exit" value="Y" /><label>有</label>
								</em> <em class="small-box"> <input type="radio" name="agency.is_exit" checked="checked" value="N" /><label>无</label>
								</em>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">主营业务</label>
							<div class="ip">
								<select class="form-control" data-bind="options: mainBus,value: agency().main_bussines" name="agency.main_bussines" required="required"></select>
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">公司类型</label>
							<div class="ip">
								<select class="form-control" data-bind="options: agencyType,value: agency().agency_type" name="agency.agency_type" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: agency().comment" name="agency.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
					<hr noshade color="#0066cc" />
					<h3>法人信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">法人姓名</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" data-bind="value: agency().corporate_name" maxlength="10" placeholder="法人姓名" name="agency.corporate_name" required=“required” />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" data-bind="options: genders,optionsCaption: '-- 性别--',value: agency().corporate_sex" name="agency.corporate_sex"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">手机号</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: agency().corporate_cellphone" placeholder="手机号" name="agency.corporate_cellphone" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: agency().corporate_id" placeholder="身份证号" name="agency.corporate_id" />
							</div>
						</div>
					</div>
					<hr noshade color="#0066cc" />
					<h3>相关文件</h3>
					<h4>营业执照</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传营业执照<input type="file" name="file1" /></a> <input st="st-file-name" type="hidden" id="txt-licence" name="agencyFile.licence_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>经营许可</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传经营许可<input type="file" name="file2" /></a> <input st="st-file-name" type="hidden" id="txt-permit" name="agencyFile.permit_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>责任险</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传责任险<input type="file" name="file3" /></a> <input st="st-file-name" type="hidden" id="txt-insurance" name="agencyFile.liability_insurance_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>法人身份证</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传法人身份证<input type="file" name="file4" /></a> <input st="st-file-name" type="hidden" id="txt-corporate" name="agencyFile.corporate_name" />

						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>负责人身份证</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传负责人身份证<input type="file" name="file5" /></a> <input st="st-file-name" type="hidden" id="txt-chief" name="agencyFile.chief_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>其他</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传其他文件<input type="file" name="file6" /></a> <input st="st-file-name" type="hidden" id="txt-other" name="agencyFile.other_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
				</form>
				<div align="right">
					<a type="submit" id="btn_save" class="btn btn-green btn-r" data-bind="click: saveAgency">保存</a>
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
	<script src="<%=basePath%>static/js/client/agency-edit.js"></script>
</body>
</html>