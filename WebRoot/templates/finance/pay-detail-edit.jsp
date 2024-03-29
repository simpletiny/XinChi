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
     <link rel="stylesheet" type="text/css" href="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.css"/>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>财务支出<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form">
             <input type="hidden" id="detail_key" name="detail.pk"  value="<%=key%>" />
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">账户</label>
                        <div class="ip"><select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --', value: detail().account" name="detail.account" required="required"></select></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">发生时间</label>
                        <div class="ip"><input type="text" id="name" class="ip- datesecond-picker"  data-bind="value: detail().time" placeholder="发生时间" name="detail.time" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
					 <div class="col-md-6 required">
                        <label class="l">支出金额</label>
                        <div class="ip"><input type="number" id="txt-money" class="ip-" data-bind="value: detail().money" placeholder="支出金额" name="detail.money" required="required"/></div>
                    </div>
                </div>
               <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" class="ip-default" rows="15" data-bind="value: detail().comment" name ="detail.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" class="btn btn-green btn-r" data-bind="click: updateDetail">提交</a></div>
        </div>
    </div>
  </div>
  
    <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
  <script>
    $(".finance").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
     <script src="<%=basePath %>static/js/datepicker.js"></script>
  <script src="<%=basePath %>static/js/finance/pay-detail-edit.js"></script>
</body>
</html>