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
<style>
.form-group {
	margin-bottom: 5px;
}

.form-control {
	height: 30px;
}

.fix-width1 {
	display: inline-block;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				本地维护<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="product-pk" value="<%=key%>" />
		<div class="main-container">
			<div class="main-box">

				<div class="form-box info-form">

					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">产品名称</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().name"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">产品线</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().location"></p>
							</div>
						</div>
					</div>
					<div class="input-row clearfloat">
						<div class="col-md-6">
							<label class="l">型号</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().product_model"></p>
							</div>
						</div>
						<div class="col-md-6">
							<label class="l">编号</label>
							<div class="ip">
								<p class="ip-default" data-bind="text: product().product_number"></p>
							</div>
						</div>
					</div>
				</div>

				<div id="div-location">
					<div>
						<div class="input-row clearfloat">
							<div class="col-md-3">
								<div class="ip">
									<em class="small-box"> <input type="radio" name="service_type_0" checked="checked" value="0" st="chk-0" /><label>机场衔接</label>
									</em>
								</div>

							</div>
							<div class="col-md-1">
								<div class="ip">
									<em class="small-box"> <input type="radio" name="service_type_0" value="1" st="chk-1" /><label>接送机场</label>
									</em>
								</div>
							</div>
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">费用：</label>
								<div class="ip">
									<input class="ip- required" type="number" st="cost" />
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-3">
								<label class="l" style="width: 80px !important">供应商</label>
								<div class="fix-width1">
									<input type="text" class="ip-" st="supplier-name" onclick="choseSupplierEmployee(event)" /> <input type="text"
										class="need" st="supplier-pk" style="display: none" />
								</div>
							</div>
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">服务名称</label>
								<div class="ip">
									<input class="ip- required" type="text" st="service-name" />
								</div>
							</div>
							<div class="col-md-3 required">
								<label class="l" style="width: 70px !important">人均成人</label>
								<div class="ip">
									<input class="ip- required" type="number" st="adult-cost" />
								</div>
							</div>
							<div class="col-md-3">
								<label class="l" style="width: 70px !important">人均儿童</label>
								<div class="ip">
									<input class="ip- " type="number" st="child-cost" />
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-12">
								<label class="l">服务要求：</label>
								<div class="ip">
									<textarea type="text" class="ip-default" rows="3" maxlength="200" st="service-comment" placeholder="服务要求"></textarea>
								</div>
							</div>
						</div>
						<div class="input-row clearfloat">
							<div class="col-md-6">
								<label class="l" style="width: 70px !important">游客信息：</label>
								<div class="ip">
									<div style="padding-top: 4px;">
										<em class="small-box"> <input type="checkbox" name="chk_info" checked="checked" value="name" /><label>姓名</label>
											<input type="checkbox" checked="checked" name="chk_info" value="sex" /><label>性别</label> <input
											type="checkbox" name="chk_info" value="age" /><label>年龄</label> <input type="checkbox" name="chk_info"
											value="id" /><label>身份证号码</label> <input type="checkbox" checked="checked" name="chk_info" value="tel" /><label>联系方式</label>
											<input type="checkbox" name="chk_info" value="room_group" /><label>分房组</label>
										</em>
									</div>
								</div>
							</div>
						</div>
						<hr />
					</div>
				</div>
				<div class="input-row clearfloat" style="text-align: right">
					<div class="col-md-12">
						<div class="ip">
							<a type="submit" class="btn btn-r" href="javacript:void(0);" onclick="deleteLocation()">-本地</a> <a type="submit"
								class="btn btn-r" href="javacript:void(0);" onclick="addLocation()">+本地</a>
						</div>
					</div>
				</div>

				<hr class="hr-big" />
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: saveProductLocal">保存</a> <a type="submit"
						class="btn btn-green btn-r" data-bind="click: updateProductLocal">更新</a> <a type="submit"
						class="btn btn-green btn-r" onclick="javascript:history.go(-1);return false;">放弃</a>
				</div>
			</div>
		</div>
	</div>
	<div id="supplier-pick" style="display: none;">
		<div class="main-container">
			<div class="main-box" style="width: 600px">
				<div class="form-group">
					<div class="span8">
						<label class="col-md-2 control-label">姓名</label>
						<div class="col-md-6">
							<input type="text" id="supplier_name" class="form-control" placeholder="姓名" />
						</div>
					</div>
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchSupplierEmployee }">搜索</button>
					</div>
				</div>
				<div class="list-result">
					<table class="table table-striped table-hover">
						<thead>
							<tr role="row">
								<th>姓名</th>
								<th>财务主体</th>
							</tr>
						</thead>
						<tbody data-bind="foreach: supplierEmployees">
							<tr data-bind="event: {click: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
								<td data-bind="text: $data.name"></td>
								<td data-bind="text: $data.financial_body_name"></td>
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
		$(".product-manager").addClass("current").children("ol").css("display",
				"block");
	</script>
	<script src="<%=basePath%>static/js/product/local-management.js"></script>
</body>
</html>