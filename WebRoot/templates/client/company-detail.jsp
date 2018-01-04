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
 <input type="hidden" id="client_key" value="<%=key%>" />
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>查看财务主体信息<a  href="javascript:void(0)" onclick="javascript:history.go(-1);return false;"  class="cancel-create">返回</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
                   <!-- ko if: company().delete_flg =='Y' -->
             <div class="form-group">
             <label class="col-md-1 control-label"  style="color:red">已停用</label>
                  <div class="ip">
                        <button type="submit" class="btn btn-green col-md-1" data-bind="click:recovery">恢复</button>
                    </div>
                    </div>
           <!-- /ko -->
            <hr />
            <div class="form-box info-form">
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">主体名称</label>
                        <div class="ip"><p class="ip-default" data-bind="text: company().client_name"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">主体简称</label>
                        <div class="ip"><p class="ip-default" data-bind="text: company().client_short_name"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">地区</label>
                        <div class="ip"><p class="ip-default" data-bind="text: company().client_area"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">类型</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().client_type"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-4">
                        <label class="l">门店</label>
                        <div class="ip" style="width:60%"><p class="ip-default" data-bind="text: company().store_type"></p></div>
                    </div>
                    <div class="col-md-4">
                        <label class="l">主营</label>
                        <div class="ip" style="width:60%"><p class="ip-" data-bind="text: company().main_business"></p></div>
                    </div>
                    <div class="col-md-4">
                        <label class="l">回款誉</label>
                        <div class="ip" style="width:60%"><p class="ip-" data-bind="text: company().back_level"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">电话</label>
                        <div class="ip"><p class="ip- date-picker" data-bind="text: company().telephone"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">传真</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().fax"></p></div>
                    </div>
                </div>
                <hr/>
                <h3>负责人信息</h3>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">姓名</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_name"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">性别</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_sex"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">身份证号</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_id"></p></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">微信</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_wechat"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">手机号</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_cellphone"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="ll">出生年</label>
                        <div class="ip"><p class="ip-" data-bind="text: company().body_birth_year"></p></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="ll">备注</label>
                        <div class="ip"><p class="ip-default" data-bind="text: company().comment"></p></div>
                    </div>
                </div>
            </div>
        </div>
    </div>
  </div>
  <script>
      $(".client").addClass("current").children("ol").css("display", "block");
  </script>
  <script src="<%=basePath%>static/js/client/company-detail.js"></script>
</body>
</html>