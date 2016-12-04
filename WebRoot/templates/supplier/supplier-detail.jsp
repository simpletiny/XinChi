<%@ page language="java" pageEncoding="UTF-8"%>
 <%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
String key = request.getParameter("key");
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>欣驰国际</title>

</head>
<body>
<div class="main-body">
 <input type="hidden" id="supplier_key" value="<%=key%>">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>查看财务主体信息<a  href="<%=basePath %>/templates/supplier/supplier.jsp"  class="cancel-create">返回</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <hr>
            <div class="form-box info-form">
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">主体名称</label>
                        <div class="ip"><p class="ip-default" data-bind="text: supplier().supplier_name"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">主体简称</label>
                        <div class="ip"><p class="ip-default" data-bind="text: supplier().supplier_short_name"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">信用代码</label>
                        <div class="ip"><p class="ip-default" data-bind="text: supplier().credit_code"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">地区</label>
                        <div class="ip"><p class="ip-default" data-bind="text: supplier().supplier_area"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">电话</label>
                        <div class="ip"><p class="ip- date-picker" data-bind="text: supplier().telephone"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">传真</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().fax"></p></div>
                    </div>
                </div>
                <hr/>
                <h3>负责人信息</h3>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">姓名</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_name"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">性别</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_sex"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">身份证号</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_id"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">微信</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_wechat"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">手机号</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_cellphone"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="ll">出生年</label>
                        <div class="ip"><p class="ip-" data-bind="text: supplier().body_birth_year"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="ll">备注</label>
                        <div class="ip"><p class="ip-default" data-bind="text: supplier().comment"></p></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
  <script>
      $(".supplier").addClass("current").children("ol").css("display", "block");
  </script>
  <script src="<%=basePath%>static/js/supplier/supplier-detail.js"></script>
</body>
</html>