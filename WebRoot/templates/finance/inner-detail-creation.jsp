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
     <link rel="stylesheet" type="text/css" href="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.css"/>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>内转<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form">
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">转出账户</label>
                        <div class="ip"><select class="form-control" id="from_account" data-bind="options: accounts, optionsCaption: '-- 请选择 --', value: detail().from_account" name="innerTransfer.from_account" required="required"></select></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">转入账户</label>
                        <div class="ip"><select class="form-control"  id="to_account" data-bind="options: accounts, optionsCaption: '-- 请选择 --', value: detail().to_account" name="innerTransfer.to_account" required="required"></select></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                      <div class="col-md-6 required">
                        <label class="l">转出时间</label>
                        <div class="ip"><input type="text" class="ip- datetime-picker-same-1"  data-bind="value: detail().from_time" placeholder="转出时间" name="innerTransfer.from_time" required="required"/></div>
                    </div>
                        <div class="col-md-6 required">
                        <label class="l">转入时间</label>
                        <div class="ip"><input type="text" class="ip- datetime-picker-same-2"  data-bind="value: detail().to_time" placeholder="转入时间" name="innerTransfer.to_time" required="required"/></div>
                    </div>
               </div>
               <div class="input-row clearfloat">
					 <div class="col-md-6 required">
                        <label class="l">转出金额</label>
                        <div class="ip"><input type="number" class="ip-" data-bind="value: detail().from_money"  onkeyup="setInMoney(this)" placeholder="转出金额" name="innerTransfer.from_money" required="required"/></div>
                    </div>
					 <div class="col-md-6 required">
                        <label class="l">转入金额</label>
                        <div class="ip"><input type="number" class="ip-" data-bind="value: detail().to_money" id="in-money" placeholder="转入金额" name="innerTransfer.to_money" required="required"/></div>
                    </div>
                </div>
                 <div class="input-row clearfloat">
                   <div class="col-md-6">
                        <label class="l">汇兑<input type="checkbox" id="check-exchange" onclick="checkExchange()"/></label>
                      <div class="ip">
                        <em class="small-box" style="vertical-align:middle">
                     	 <label>转出账户</label>
                       	 <input type="radio" id="radio-out" disabled="disabled" name="innerTransfer.exchange_account" value="out"/>
                        </em>
                         <em class="small-box" style="vertical-align:middle">
                       <label>转入账户</label>
                        <input type="radio" id="radio-in" disabled="disabled" name="innerTransfer.exchange_account" value="in" />
                       </em>	
                        <input style="width:100px" id="exchange-money" disabled="disabled" type="number" max="100" min="0" class="ip-" placeholder="汇兑金额" name="innerTransfer.exchange_money" required="required"/>
              		</div>
              		</div>
                </div>
               <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" class="ip-default" rows="15" data-bind="value: detail().comment" name ="innerTransfer.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" class="btn btn-green btn-r" data-bind="click: createDetail">提交</a></div>
        </div>
    </div>
  </div>
  
    <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
  <script>
    $(".accounting").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
     <script src="<%=basePath %>static/js/datepicker.js"></script>
  <script src="<%=basePath %>static/js/finance/inner-detail-creation.js"></script>
</body>
</html>