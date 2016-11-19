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
        <h2>收入详表</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
         <form class="form-horizontal search-panel">
                <style>
                    .form-group { margin-bottom: 5px; }
                    .form-control{ height: 30px; }
                </style>
                    <div class="form-group" >
	                    <div style="width:30%;float:right">
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createOrder() }">新建</button>
		                    </div>
		                     <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editOrder() }">编辑</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); searchResumes() }">删除</button>
		                    </div>
		                    </s:if>
		                 </div>
	                </div>
                <div class="form-group">
                    <div class="span6">
                        <label class="col-md-1 control-label">团号</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="团号"
                                  name="order.team_number" />
                        </div>
                    </div>
                    <div class="span6">
                        <label class="col-md-1 control-label">产品</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="产品"
                                  name="order.product" />
                        </div>
                    </div>
                    </div>
                   <div class="form-group">
                      <div align="left">
                        <label class="col-md-1 control-label">确认日期</label>
                        <div class="col-md-2" style="float:left">
                            <input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from"
                                  name="order.date_from" />
                        </div>
                       </div>
                      <div align="left">
                        <div class="col-md-2" style="float:left">
                          <input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to"
                                  name="order.date_to" />
                        </div>
                    </div>
                   <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                     <div class="span6">
                        <label class="col-md-1 control-label">销售</label>
                        <div class="col-md-2">
                        	 <select class="form-control" style="height:34px" id="select-sales" data-bind="options: sales_name, optionsCaption: '全部'" name="order.create_user_name"></select>
                        </div>
                    </div>
                    </s:if>
                    <div style="padding-top: 3px;">
                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
                    </div>
                </div>
            </form>
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                        	<th></th>
                            <th>团号</th>
                            <th>客户</th>
                            <th>确认日期</th>
                            <th>出团日期</th>
                            <th>回团日期</th>
                            <th>人数</th>
                            <th>总团款</th>
                            <th>总成本</th>
                            <th>毛利润</th>
                            <th>人均利润</th>
                            <th></th>
                            <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                            <th>销售</th>
                            </s:if>
                        </tr>
                    </thead>
                    <tbody id="tbody-data" data-bind="foreach: orders">
                        <tr>
                        	 <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenOrders"/></td>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.team_number,attr: {href: 'order-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.client_employee_name"></td>
                            <td data-bind="text: $data.confirm_date"></td>
                            <td data-bind="text: $data.departure_date"></td>
                            <td data-bind="text: $data.return_date"></td>
                            <td data-bind="text: $data.people_count"></td> 
                             <td data-bind="text: $data.receivable"></td>    
                             <td data-bind="text: $data.payable"></td>
                             <td data-bind="text: $data.gross_profit"></td>    
                             <td data-bind="text: $data.per_profit"></td>
                             <!-- ko if: $data.final_flg=='N' -->
                             <td><a href="javascript:void(0)" data-bind="click: function() {$parent.closeTeam($data.pk)} ">生成决算单</a></td> 
                             <!-- /ko -->   
                             <!-- ko if: $data.final_flg=='Y' -->
                             <td>已生成</td> 
                             <!-- /ko -->      
                             <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                             <td data-bind="text: $data.create_user_name"></td>  
                              </s:if>  
                        </tr>
                    </tbody>
                    <tr id="total-row">
                    		<td></td>
                    	    <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>合计</td>
                            <td data-bind="text:totalPeople"></td>
                            <td data-bind="text:totalReceivable"></td>
                            <td data-bind="text:totalPayable"></td>
                            <td data-bind="text:totalProfit"></td>
                            <td data-bind="text:totalPerProfit"></td>
                            <td></td>
                             <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                            <td></td>
                            </s:if>
                    </tr>
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
  <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
   <script src="<%=basePath %>static/js/datepicker.js"></script>
    <script src="<%=basePath%>static/js/sale/order.js"></script>
</body>
</html>