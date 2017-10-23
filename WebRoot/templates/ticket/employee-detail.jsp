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
 <input type="hidden" id="employee_key" value="<%=key%>">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>机票供应商员工信息<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create">返回</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <hr>
            <div class="form-box info-form">
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">姓名</label>
                        <div class="ip"><p class="ip-default" data-bind="text: employee().name"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">性别</label>
                        <div class="ip"><p class="ip-default" data-bind="text: employee().sex"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">地区</label>
                        <div class="ip"><p class="ip-default" data-bind="text: employee().area"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">财务主体</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().financial_body_name"></p></div>
                    </div>
                </div>
               <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">身份证号</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().id"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">出生年</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().birth_year"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">手机号</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().cellphone"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">微信</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().wechat"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">电话</label>
                        <div class="ip"><p class="ip- date-picker" data-bind="text: employee().telephone"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">传真</label>
                        <div class="ip"><p class="ip-" data-bind="text: employee().fax"></p></div>
                    </div>
                </div>
              <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">QQ</label>
                        <div class="ip"><p class="ip- date-picker" data-bind="text: employee().qq"></p></div>
                    </div>
                </div>
               <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">所属销售</label>
                        <div class="ip"><p class="ip- date-picker" data-bind="text: employee().sales"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="ll">备注</label>
                        <div class="ip"><p class="ip-default" data-bind="text: employee().comment"></p></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
  <script>
      $(".ticket").addClass("current").children("ol").css("display", "block");
  </script>
  <script src="<%=basePath%>static/js/ticket/employee-detail.js"></script>
</body>
</html>