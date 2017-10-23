<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
			+ request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
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
				机票供应商财务主体新建<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form">
					<h3>基本信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">主体全称</label>
							<div class="ip">
								<input type="text" id="name" class="ip-default" data-bind="value: supplier().supplier_name" placeholder="主体名称" name="supplier.supplier_name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">主体简称</label>
							<div class="ip">
								<input type="text" id="name" maxlength="8" class="ip-default" data-bind="value: supplier().supplier_short_name" placeholder="主体简称（8个字以内）" name="supplier.supplier_short_name"
									required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">信用代码</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: supplier().credit_code" placeholder="信用代码" name="supplier.credit_code" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">地区</label>
							<div class="ip" style="width: 35%">
								<select class="form-control" data-bind="options: provices, optionsCaption: '-- 省份--',value: supplier().supplier_provice,event:{change:ter}" name="supplier.supplier_provice" required="required"></select>
							</div>
							<div class="ip" style="width: 35%">
								<select class="form-control" id="city" name="supplier.supplier_city" required="required">
									<option value>-- 市--</option>
								</select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">详细地址</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: supplier().address" placeholder="详细地址" name="supplier.address" />
								</textarea>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
						<label class="l">结款方式</label>
						<div class="ip">
							<select class="form-control" data-bind="options: paymentTypes,value: supplier().payment_type" name="supplier.payment_type" required="required"></select>
						</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">是否合作</label>
							<div class="ip">
								<em class="small-box"> <input type="radio" name="supplier.is_cooperate" checked="checked" value="Y" /><label>合作</label>
								</em> <em class="small-box"> <input type="radio" name="supplier.is_cooperate" value="N" /><label>终止</label>
								</em>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: supplier().comment" name="supplier.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
					<hr noshade color="#0066cc" />
					<h3>法人信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">法人姓名</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" data-bind="value: supplier().corporate_name" maxlength="10" placeholder="法人姓名" name="supplier.corporate_name" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" data-bind="options: genders,optionsCaption: '-- 性别--',value: supplier().corporate_sex" name="supplier.corporate_sex"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: supplier().corporate_id" placeholder="身份证号" name="supplier.corporate_id" />
							</div>
						</div>
					</div>
					<hr noshade color="#0066cc" />
					<h3>负责人信息</h3>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">姓名</label>
							<div class="ip">
								<input type="text" class="ip- date-picker" data-bind="value: supplier().body_name" placeholder="姓名" name="supplier.body_name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">性别</label>
							<div class="ip">
								<select class="form-control" data-bind="options: genders,value: supplier().body_sex" name="supplier.body_sex" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">身份证号</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: supplier().body_id" placeholder="身份证号" name="supplier.body_id" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">微信</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: supplier().body_wechat" placeholder="微信" name="supplier.body_wechat" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">手机号</label>
							<div class="ip">
								<input type="text" class="ip- cellphone" maxlength="11" data-bind="value: supplier().body_cellphone" placeholder="手机号" name="supplier.body_cellphone" required="required" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">出生年</label>
							<div class="ip">
								<input type="text" class="ip- date_year" maxlength="4" data-bind="value: supplier().body_birth_year" placeholder="出生年" name="supplier.body_birth_year" />
							</div>
						</div>
					</div>

					<hr noshade color="#0066cc" />
					<h3>相关文件</h3>
					<h4>营业执照</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传营业执照<input type="file" name="file1" /></a> <input type="hidden" name="supplierFile.licence_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>经营许可</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传经营许可<input type="file" name="file2" /></a> <input type="hidden" name="supplierFile.permit_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>责任险</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传责任险<input type="file" name="file3" /></a> <input type="hidden" name="supplierFile.liability_insurance_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>法人身份证</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传法人身份证<input type="file" name="file4" /></a> <input type="hidden" name="supplierFile.corporate_name" />

						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>负责人身份证</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传负责人身份证<input type="file" name="file5" /></a> <input type="hidden" name="supplierFile.chief_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>
					<hr noshade color="#0066cc" />
					<h4>其他</h4>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<a href="javascript:;" class="a-upload">上传其他文件<input type="file" name="file6" /></a> <input type="hidden" name="supplierFile.other_name" />
						</div>
						<div class="col-md-6"></div>
					</div>
					<div class="input-row clearfloat"></div>

				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createSupplier">保存</a>
				</div>
			</div>
		</div>
	</div>

	<script>
		$(".ticket").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/ticket/supplier-creation.js"></script>
</body>
</html>