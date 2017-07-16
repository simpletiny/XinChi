<%@ page language="java" pageEncoding="UTF-8"%>
 <%@taglib uri="/struts-tags" prefix="s" %>
<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <title>欣驰国际</title>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>新建账户<a href="<%=basePath %>/templates/finance/card.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form">
            	 <div class="input-row clearfloat">
                    <div class="col-md-8 required">
                        <label class="l">账户名称</label>
                        <div class="ip"><input maxlength="50" type="text" id="account" class="ip-default" data-bind="value: card().account,event:{blur:checkAccount}" placeholder="账户名称" name="card.account" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">卡号/账户id</label>
                        <div class="ip"><input type="text" id="number" maxlength="40" class="ip-default" data-bind="value: card().number" placeholder="卡号" name="card.number" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">户名</label>
                        <div class="ip"><input type="text" id="name" maxlength="20" class="ip-default" data-bind="value: card().name" placeholder="户名" name="card.name" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">开户行</label>
                        <div class="ip"><input type="text" class="ip-" maxlength="40" data-bind="value: card().bank" placeholder="开户行" name="card.bank" required="required"/></div>
                    </div>
                     <div class="col-md-6 required">
                        <label class="l">初始金额</label>
                        <div class="ip"><input type="number" class="ip-" data-bind="value: card().initMoney" placeholder="初始金额" name="card.init_money" required="required"/></div>
                    </div>
   
                </div>
                <div class="input-row clearfloat">
               		 <div class="col-md-6 required">
                        <label class="l">种类</label>
                        <div class="ip"><select class="form-control" data-bind="options: cardType, optionsCaption: '-- 请选择 --', value: card().type" name="card.type" required="required"></select></div>
                    </div>
               </div>
               <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" maxlength="200" class="ip-default" rows="15" data-bind="value: card().comment" name ="card.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" id="submit" class="btn btn-green btn-r" data-bind="click: createCompany">保存</a></div>
        </div>
    </div>
  </div>
  <script>
    $(".manager").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
  <script src="<%=basePath %>static/js/finance/card-creation.js"></script>
</body>
</html>