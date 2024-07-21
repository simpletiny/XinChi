<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort()
			+ path + "/";
	String key = request.getParameter("key");
	String independent_flg = request.getParameter("independent_flg");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title>欣驰国际</title>
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/vendor/datetimepicker/jquery.datetimepicker.css" />
<link rel="stylesheet" type="text/css" href="<%=basePath%>static/js/order/upload.css" />
<style>
#name-table tr th, td {
	text-align: center;
	vertical-align: middle !important
}

.fix-width {
	width: 40% !important;
}

table {
	width: 100%;
	border-collapse: collapse;
}

th, td {
	border: 1px solid #000;
	padding: 8px;
	text-align: left;
}

th {
	background-color: #f2f2f2;
}

.flg {
	position: absolute;
	top: 50px;
	right: 150px;
	display: block;
	color: red;
	z-index: 1;
	width: 150px;
	height: 50px;
}
</style>
</head>
<body>
	<div class="main-body">
		<jsp:include page="../layout.jsp" />
		<div class="subtitle">
			<h2>
				失信被执行人查询<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i
					class="ic-cancel"></i>取消</a>
			</h2>
		</div>
		<input type="hidden" id="key" value="<%=key%>"></input> <input type="hidden" id="independent_flg"
			value="<%=independent_flg%>"></input>
		<div class="main-container">
			<div class="main-box">
				<form class="form-box info-form" id="form-name">
					<div style="display: none; width: 600px" id="bat-passenger">
						<div class="input-row clearfloat">
							<div class="col-md-12">
								<textarea type="text" class="ip-default" id="txt-name-list" rows="10" placeholder="姓名+身份证号。"></textarea>
							</div>
							<div class="col-md-12" style="text-align: right; margin-top: 10px">
								<a type="submit" class="btn btn-green btn-r" onclick="cancelBat()">取消</a> <a type="submit"
									class="btn btn-green btn-r" onclick="doBat()">写入</a>
							</div>
						</div>
					</div>
					<div id="div-name-list">
						<div class="input-row clearfloat">
							<input type="hidden" name="ok_id" /> <input type="hidden" name="ok_name" /> <input type="hidden" name='is_ok' />
							<div class="col-md-3 required">
								<label class="l" style="width: 50px !important">姓名:</label>
								<div class="ip fix-width">
									<input type="text" name='name' required class="ip-" oninput="whenChangeName(this)" maxlength="10"
										placeholder="姓名" />
								</div>
							</div>
							<div class="col-md-4 required">
								<label class="l" style="width: 75px !important">身份证号:</label>
								<div class="ip">
									<input type="text" name='id' required class="ip-" oninput="whenChangeId(this)" maxlength="18"
										placeholder="身份证号" />
								</div>
							</div>
							<div class="col-md-1">
								<img src="" alt="" />
							</div>
							<div class="col-md-1">
								<a href="javascript:void(0)" onclick="removeName(this)" style="line-height: 40px">删除</a>
							</div>
						</div>
					</div>
					<div align="right">
						<a type="submit" class="btn btn-green btn-r" data-bind="click: batName">批量导入</a> <a type="submit"
							class="btn btn-green btn-r" data-bind="click: addName">添加名单</a>
					</div>
				</form>
				<div align="right">
					<a type="submit" class="btn btn-green btn-r" data-bind="click: createOrder" id="a-create-order"
						style="display: none">生成订单</a> <a type="submit" class="btn btn-green btn-r" data-bind="click: check">查询</a>
				</div>
				<div id="div-result" style="display: none">
					<hr />
					<h2 style="color: red">查询结果</h2>
					<table id="table-result" style="margin-top: 20px">
						<thead>
							<tr>
								<th>序号</th>
								<th>姓名</th>
								<th>查询状态</th>
								<th>是否失信人</th>
								<th>查询结果</th>
								<th>是否下架</th>
							</tr>
						</thead>
						<tbody>

						</tbody>
					</table>
				</div>
			</div>
		</div>
	</div>
	<script>
		$(".order-box").addClass("current").children("ol").css("display", "block");
	</script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/jquery.validate.min.js"></script>
	<script type="text/javascript" src="<%=basePath%>static/vendor/messages_zh.min.js"></script>
	<script src="<%=basePath%>static/js/validation.js"></script>
	<script src="<%=basePath%>static/js/system/dishonest.js?v=1.001"></script>
</body>
</html>