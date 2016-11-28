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
		                        <button type="submit" class="btn btn-green col-md-1" disabled="disabled" data-bind="click: function() { createOrder() }">打回从报</button>
		                    </div>
		                 </div>
	                </div>
                <div class="form-group">
   					   <div class="ip">
	                       <div data-bind="foreach: allStatus" style="padding-top: 4px;">
	                           <em class="small-box">
	                                 <input type="checkbox" data-bind="attr: {'value': $data}, checked: $root.chosenStatus"/><label data-bind="text: $root.statusMapping[$data]"></label>
	                            </em>
	                        </div>
	                   </div>
                    </div>
                   <div class="form-group">
                      <div align="left">
                        <label class="col-md-1 control-label">收入日期</label>
                        <div class="col-md-2" style="float:left">
                            <input type="text" class="form-control date-picker" data-bind="value: dateFrom" placeholder="from"
                                  name="detail.date_from" />
                        </div>
                       </div>
                     <div align="left">
                        <div class="col-md-2" style="float:left">
                          <input type="text" class="form-control date-picker" data-bind="value: dateTo" placeholder="to"
                                  name="detail.date_to" />
                        </div>
                    </div>
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
                            <th>金额</th>
                            <th>收入时间</th>
                            <th>类型</th>
                            <th>摘要</th>
                            <th>填报日期</th>
                            <th>入账日期</th>
                            <th>状态</th>
                        </tr>
                    </thead>
                    <tbody id="tbody-data" data-bind="foreach: receiveds">
                        <tr>
                        	 <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenOrders"/></td>
                            <td data-bind="text: $data.received" class="rmb"> </td>
                            <td data-bind="text: $data.received_time"></td>
                            <td data-bind="text: $root.typeMapping[$data.type]"></td>
                            <td data-bind="text: $data.comment"></td>
                            <td data-bind="text: moment($data.create_time-0).format('YYYY-MM-DD')"></td>
                            <td data-bind="text: $data.confirm_time"></td> 
                             <td data-bind="text: $root.statusMapping[$data.status]"></td>    
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
  <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
   <script src="<%=basePath %>static/js/datepicker.js"></script>
    <script src="<%=basePath%>static/js/sale/received.js"></script>
</body>
</html>