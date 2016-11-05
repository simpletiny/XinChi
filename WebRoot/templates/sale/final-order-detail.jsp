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
<input type="hidden" id="order_key" value="<%=key%>" />
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>查看预算订单(<b data-bind="text:order().team_number"></b>)<a href="<%=basePath %>/templates/sale/order.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
               <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">客户</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().client_employee_name" name="order.client_employee_name" /></div>
                    	
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">总团款</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().receivable" placeholder="总团款" name="order.receivable"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12 required">
                        <label class="l">产品</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().product" placeholder="产品" name="order.product" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat ">
                    <div class="col-md-6">
                        <label class="l">出团日期</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().departure_date" placeholder="出团日期" name="order.departure_date" required="required"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">天数</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().days" placeholder="天数" name="order.days" required="required"/></div>
                    </div>
                </div>
               <!--  <hr/>
                 <h4>收款明细</h4>
             <div class="list-result" style="padding-left:10%;width:50%" id="receivable-detail">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                            <th>收款时间</th>
                            <th>金额</th>
                            <th colspan="2"></th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: receivableDetails">
                        <tr>
                             <td data-bind="text: $data.received_time"></td>
                             <td data-bind="text: $data.received"></td>
                             <td><a href="javascript:void(0)" data-bind="click: function() {$parent.deleteReceivableDetail($data.pk)} ">删除</a></td>                
                        </tr>
                    </tbody>
                </table>
                 <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">已收合计: <b class="ip-default" data-bind="text: order().received"></b></label>

                    </div>
                    <div class="col-md-6">
                        <label class="l">尚余: <b class="ip-default" data-bind="text: order().client_debt"></b></label>
                    </div>
                </div>
            </div>
            <div class="input-row clearfloat">
                <div class="ip"><a type="button" class="btn btn-green btn-r" data-bind="click: addReceivable">添加收款明细</a></div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">人数</label>
                        <div class="ip" style="width:30%"><p class="ip-default" data-bind="text:order().people_count" placeholder="人数" name="order.people_count"/></div>
                    </div>
                    <div class="col-md-6 ">
                        <label class="l">大交通费用</label>
                        <div class="ip"><p class="ip-default" data-bind="text: order().traffic_payment" placeholder="大交通费用" name="order.traffic_payment" /></div>
                    </div>
                </div> -->
                <!-- ko foreach: suppliers -->
                <div class="input-row clearfloat" st="supplier">
                    <div class="col-md-6">
                        <label class="l">供应商</label>
                        <div class="ip"><p class="ip-default" data-bind="text:$data.supplier_employee_name"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">应付款</label>
                        <div class="ip"><p class="ip-default"  data-bind="text:$data.payable"/></div>
                    </div>
                </div>
               <!-- /ko -->
               
                 <div class="input-row clearfloat" st="supplier">
                    <div class="col-md-6">
                        <label class="l">其他费用</label>
                        <div class="ip"><p class="ip-default" data-bind="text:order().other_payment"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><p class="ip-default" data-bind="text:order().comment" name ="order.comment"></textarea></div>
                    </div>
                </div>
     </div>
     </div>
    </div>

     <div id="receivable-add" style="display:none;">
        <form  id="form_container">
         <input type="hidden" data-bind="value: order().team_number" id="txt-team-number" name="detail.team_number"/>
            <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">时间</label>
                        <div class="ip"><input type="text" name="detail.received_time" class="ip- datetime-picker" placeholder="时间" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">金额</label>
                        <div class="ip"><input type="number"  min="0" class="ip-" name="detail.received"  placeholder="金额" required="required"/></div>
                    </div>
                </div>
          </form>
          	<div class="col-md-12" style="margin-top:10px">
			<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: saveReceivable">保存</a></div>
       </div>
      <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
  </div>
  <script>
    $(".sale").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
        <script src="<%=basePath %>static/js/datepicker.js"></script>
  <script src="<%=basePath %>static/js/sale/final-order-detail.js"></script>
</body>
</html>