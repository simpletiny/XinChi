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
        <h2>新建预算订单<a href="<%=basePath %>/templates/sale/order.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form" id="form_container">
               <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">客户</label>
                        <div class="ip"><input type="text" id="txt-client-employee-name" class="ip-" data-bind="value: order().client_employee_name,event:{click:choseClientEmployee}" placeholder="客户" name="order.client_employee_name" required="required"/></div>
                    	<input type="text" class="ip-" id="txt-client-employee-pk" data-bind="value: order().client_employee_pk" style="display:none" name="order.client_employee_pk" id="client-employee-pk" required="required"/>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">总团款</label>
                        <div class="ip"><input type="number"  class="ip-" data-bind="value: order().receivable" placeholder="总团款" name="order.receivable" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12 required">
                        <label class="l">产品</label>
                        <div class="ip"><input type="text" id="name" class="ip-default" data-bind="value: order().product" placeholder="产品" name="order.product" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat ">
                    <div class="col-md-6 required">
                        <label class="l">出团日期</label>
                        <div class="ip"><input type="text" id="departure" class="ip- date-picker"  data-bind="value: order().departure_date" placeholder="出团日期" name="order.departure_date" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">天数</label>
                        <div class="ip"><input type="number"  class="ip-" data-bind="value: order().days" placeholder="天数" name="order.days" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">人数</label>
                        <div class="ip" style="width:30%"><input type="number" class="ip-" id="people-count" data-bind="value:order().people_count" placeholder="人数" name="order.people_count"/></div>
                   		<div class="ip" style="width:30%"><a type="submit" style="display:none" class="btn btn-green btn-r" data-bind="click: recordNameList">名单</a></div>
                    </div>
                    <div class="col-md-6 ">
                        <label class="l">大交通费用</label>
                        <div class="ip"><input type="number"  class="ip-" id="air-pay" data-bind="value: order().traffic_payment" placeholder="大交通费用" name="order.traffic_payment" /></div>
                    </div>
                </div>
                 
                <div class="input-row clearfloat" st="supplier">
                    <div class="col-md-6">
                        <label class="l">供应商</label>
                        <div class="ip"><input type="text" class="ip-" st="supplierEmployeeName" data-bind="event:{click:choseSupplierEmployee}" placeholder="供应商"/></div>
                  		<input type="text" class="ip-" st="supplierEmployeePk" style="display:none"/>
                    </div>
                    <div class="col-md-6">
                        <label class="l">应付款</label>
                        <div class="ip"><input type="number" st="payable"  class="ip-"  placeholder="应付款"/></div>
                    </div>
                </div>

                
                <div class="input-row clearfloat">
                <div class="ip"><a type="button" class="btn btn-green btn-r" data-bind="click: addSupplier">添加供应商</a></div>
                </div>
                 <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">其他费用</label>
                        <div class="ip"><input type="number"  data-bind="value:order().other_payment" name="order.other_payment"  class="ip-"  placeholder="其他费用"/></div>
                    </div>
                     <div class="col-md-6">
                        <label class="l">费用说明</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: order().payment_comment" name="order.payment_comment" placeholder="费用说明"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" class="ip-default" rows="15" data-bind="value: order().comment" name ="order.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" class="btn btn-green btn-r" data-bind="click: createOrder">保存</a></div>
        </div>
    </div>
  </div>
   <div id="name-list" style="display:none;">
      <div class="main-container">
       <div class="main-box" style="width:600px">
       	<div class="col-md-12">
                 <label class="l">名单（规则：姓名：身份证号。多个名单用"；"分隔）</label>
                 <div class="ip" ><textarea style="width:100%" type="text" class="ip-default" rows="15" id="txt-name-list" placeholder="张三：12345；李四：123154"></textarea></div>
          </div>
          	<div class="col-md-12" style="margin-top:10px">
			<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: saveNameList">保存</a></div>
			</div>
       </div>
       </div>
      <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
  </div>

   <div id="client-pick" style="display:none;">
      <div class="main-container">
       <div class="main-box"style="width:600px">
         <div class="form-group">
           <div class="span8">
               <label class="col-md-2 control-label">姓名</label>
               <div class="col-md-6">
                   <input type="text" id="client_name" class="form-control"  placeholder="姓名" />
               </div>
           </div>
           <div>
               <button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchClientEmployee }">搜索</button>
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
                    <tbody data-bind="foreach: clientEmployees">
                        <tr data-bind="event: {dblclick: function(){ $parent.pickClientEmployee($data.name,$data.pk)}}">
                            <td data-bind="text: $data.name"> </td>
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
  
   <div id="supplier-pick" style="display:none;">
      <div class="main-container">
       <div class="main-box"style="width:600px">
          <div class="form-group">
           <div class="span8">
               <label class="col-md-2 control-label">姓名</label>
               <div class="col-md-6">
                   <input type="text" id="supplier_name" class="form-control"  placeholder="姓名" />
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
                        <tr data-bind="event: {dblclick: function(){ $parent.pickSupplierEmployee($data.name,$data.pk)}}">
                            <td data-bind="text: $data.name"> </td>
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
    $(".sale").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
    <script src="<%=basePath %>static/js/datepicker.js"></script>
 
  <script src="<%=basePath %>static/js/sale/order-creation.js"></script>
</body>
</html>