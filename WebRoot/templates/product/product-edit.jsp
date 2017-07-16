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
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				修改产品<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a>
			</h2>
		</div>

		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form_container">
				 <input type="hidden" id="key" value="<%=key%>" name="product.pk"></input>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">产品名称</label>
							<div class="ip">
								<input type="text" class="ip-" data-bind="value: product().name" placeholder="8字以内" maxlength="8" name="product.name" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">产品线</label>
							<div class="ip">
								<select class="form-control" style="height: 34px" data-bind="options: locations,value:product().location, optionsCaption: '--请选择--'" name="product.location" required="required"></select>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6 required">
							<label class="l">天数</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: product().days" placeholder="天数" name="product.days" required="required" />
							</div>
						</div>
						<div class="col-md-6 required">
							<label class="l">同业价格</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: product().business_price" placeholder="同业价格" name="product.business_price" required="required" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">销售利润</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: product().profit_space" placeholder="销售利润" name="product.profit_space" />
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">最大让利</label>
							<div class="ip">
								<input type="number" class="ip-" data-bind="value: product().max_profit_substract" placeholder="最大让利" name="product.max_profit_substract" />
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-12">
							<label class="l">备注</label>
							<div class="ip">
								<textarea type="text" class="ip-default" rows="15" data-bind="value: product().comment" name="product.comment" placeholder="需要备注说明的信息"></textarea>
							</div>
						</div>
					</div>
				</form>

				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: updateProduct">保存</a>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".product-manager").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/datepicker.js"></script>

	<script src="<%=basePath%>static/js/product/product-edit.js"></script>
</body>
</html>