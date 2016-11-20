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
	<link rel="stylesheet" type="text/css" href="<%=basePath %>static/vendor/datetimepicker/MonthPicker.min.css"/>
	<link rel="stylesheet" type="text/css" href="<%=basePath %>static/css/jquery-ui.css"/>
     <style>
         .form-group { margin-bottom: 5px; }
         .form-control{ height: 30px; }
         .fixed{
         	font-size:12px;
			display:block;
			position:fixed;
			right:0px;
			top:200px;
			margin-left:10px;
			z-index:100;
			}
     </style>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>应收款</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
         <form class="form-horizontal search-panel" id="form-search">
                <div class="form-group">
                    <div class="span6">
                        <label class="col-md-1 control-label">客户</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="客户"
                                  name="receivable.client_employee_name"/>
                        </div>
                    </div>

                    <div class="span6">
                        <label class="col-md-1 control-label">出团月份</label>
                        <div class="col-md-2">
                             <input type="text" class="form-control month-picker-st" placeholder="出团月份"
                                  name="receivable.departure_month"/>
                        </div>
                    </div>
                    <div class="span6">
                        <label class="col-md-1 control-label">状态</label>
                        <div class="col-md-2">
                        	 <select class="form-control" style="height:34px" data-bind="options: teamStatus, optionsCaption: '全部'" name="receivable.team_status"></select>
                        </div>
                    </div>
                    <div class="span6">
                 	    <div data-bind="foreach: types">
                            <em class="small-box">
                                <input type="checkbox" required="required" data-bind="attr: {'value': $data}, checked: $root.chosenTypes,event:{click:$root.changeType}"/><label data-bind="text: $data"></label>
                            </em>
                        </div>
                    </div>
                    </div>
                   <div class="form-group">
                   <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                     <div class="span6">
                        <label class="col-md-1 control-label">销售</label>
                        <div class="col-md-2">
                        	 <select class="form-control" style="height:34px" id="select-sales" data-bind="options: sales_name, optionsCaption: '全部',value:chosenSales,event:{change:fetchSummary}" name="receivable.sales_name"></select>
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
	                            <th>全部</th>
	                            <th>不足1月</th>
	                            <th>1个月~2个月</th>
	                            <th>2个月~6个月</th>
	                            <th>坏账</th>
	                        </tr>
	                    </thead>
	                     <tbody id="tbody-data">
	                        <tr>
								<td>单数</td>
								<td data-bind="text:recsum().all_count"></td>
								<td data-bind="text:recsum().one_month_count"></td>
								<td data-bind="text:recsum().two_month_count"></td>
								<td data-bind="text:recsum().six_month_count"></td>
								<td data-bind="text:recsum().bad_month_count"></td>
	                        </tr>
	                        <tr>
								<td>尾款</td>
								<td data-bind="text:recsum().all_balance"></td>
								<td st="all" data-bind="text:recsum().one_month_balance"></td>
								<td st="all" data-bind="text:recsum().two_month_balance"></td>
								<td st="all" data-bind="text:recsum().six_month_balance"></td>
								<td st="all" data-bind="text:recsum().bad_month_balance"></td>
								<td st="budget" style="display:none" data-bind="text:recsum().one_month_budget_balance"></td>
								<td st="budget" style="display:none" data-bind="text:recsum().two_month_budget_balance"></td>
								<td st="budget" style="display:none" data-bind="text:recsum().six_month_budget_balance"></td>
								<td st="budget" style="display:none" data-bind="text:recsum().bad_month_budget_balance"></td>
								<td st="final" style="display:none" data-bind="text:recsum().one_month_final_balance"></td>
								<td st="final" style="display:none" data-bind="text:recsum().two_month_final_balance"></td>
								<td st="final" style="display:none" data-bind="text:recsum().six_month_final_balance"></td>
								<td st="final" style="display:none" data-bind="text:recsum().bad_month_final_balance"></td>
	                        </tr>
	                    </tbody>
	           </table>
           	</div>
      		 <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                        	<th></th>
                            <th>团号</th>
                            <th>回团天数</th>
                            <th>决否</th>
                            <th>客户</th>
                            <th>出团日期</th>
                            <th>产品</th>
                            <th>人数</th>
                            <th>总团款</th>
                            <th>已收款</th>
                            <th>尾款</th>
                            <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                            <th>销售</th>
                            </s:if>
                        </tr>
                    </thead>
                    <tbody id="tbody-data" data-bind="foreach: receivables">
                        <tr>
                        	 <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenOrders"/></td>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.team_number,attr: {href: 'order-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.back_days"></td>
                            <td data-bind="text: $data.final_flg"></td>
                            <td data-bind="text: $data.client_employee_name"></td>
                            <td data-bind="text: $data.departure_date"></td>
                            <td data-bind="text: $data.product"></td> 
                             <td data-bind="text: $data.people_count"></td>
                             
                             <td st="budget" style="display:none" data-bind="text:$data.budget_receivable"></td>
                             
                             <!-- ko if: $data.final_flg=="Y" -->  
                             <td st="final" style="display:none" data-bind="text:$data.final_receivable"></td>
                              <!-- /ko -->
                               <!-- ko if: $data.final_flg=="N" -->      
                             <td st="final" style="display:none">未决算</td>
                             <!-- /ko -->
                             
                              <!-- ko if: $data.final_flg=="Y" -->      
                             <td st="all" data-bind="text:$data.final_receivable"></td>
                             <!-- /ko -->
                              <!-- ko if: $data.final_flg=="N" -->      
                             <td st="all" data-bind="text:$data.budget_receivable"></td>
                             <!-- /ko -->
                             
                             <td data-bind="text: $data.received"></td>   
                             
                             <td st="budget" style="display:none" data-bind="text:$data.budget_balance"></td>
                             
                             <!-- ko if: $data.final_flg=="Y" -->  
                             <td st="final" style="display:none" data-bind="text:$data.final_balance"></td>
                              <!-- /ko -->
                               <!-- ko if: $data.final_flg=="N" -->      
                             <td st="final" style="display:none">未决算</td>
                             <!-- /ko -->
                              
                             <!-- ko if: $data.final_flg=="Y" -->      
                             <td st="all" data-bind="text:$data.final_balance"></td>
                             <!-- /ko -->
                              <!-- ko if: $data.final_flg=="N" -->      
                             <td st="all" data-bind="text:$data.budget_balance"></td>
                             <!-- /ko -->
                             <s:if test="#session.user.user_roles.contains('ADMIN')||#session.user.user_roles.contains('MANAGER')">
                             <td data-bind="text: $data.sales_name"></td>  
                              </s:if>  
                        </tr>
                    </tbody>
                    <tr id="total-row">
                    		<td></td>
                    	    <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td></td>
                            <td>合计</td>
                            <td data-bind="text:totalPeople"></td>
                            <td st="all" data-bind="text:totalReceivable"></td>
                            <td st="budget" style="display:none" data-bind="text:totalBudgetReceivable"></td>
                            <td st="final" style="display:none" data-bind="text:totalFinalReceivable"></td>
                            <td data-bind="text:totalReceived"></td>
                            <td st="all" data-bind="text:totalBalance"></td>
                            <td st="budget" style="display:none" data-bind="text:totalBudgetBalance"></td>
                            <td st="final" style="display:none" data-bind="text:totalFinalBalance"></td>
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
       
	                    <div class="fixed">
		                    <div style="margin-top:5px">
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { ridTail() }">抹零申请</button>
		                    </div>
		                    <div style="margin-top:5px">
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { sumOrder() }">合账申请</button>
		                    </div>
		                    <div style="margin-top:5px">
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { strike() }">冲账申请</button>
		                    </div>
		                    <div style="margin-top:5px">
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { receive() }">收入</button>
		                    </div>
		                 </div>
    </div>
    
  </div>
</div>
    <div id="tail_submit" style="display:none;width:1100px; padding-top: 30px;">
   	 <form id="form-tail">
	          <div class="input-row clearfloat">
		           <div class="col-md-4">
		               <label class="l" style="width:30%">抹零金额</label>
		               <div class="ip" style="width:70%">
		                  <p class="ip-default" data-bind="text:tailMoney()"></p>
		                  <input name="detail.received" type="hidden" data-bind="value:tailMoney()"/>
		               </div>
		           </div>
		           <div class="col-md-4">
		               <label class="l" style="width:30%">团号</label>
		               <div class="ip" style="width:70%">
		                   <p class="ip-default" data-bind="text:team_number()"></p>
		                   <input name="detail.team_number" type="hidden" data-bind="value:team_number()"/>
		               </div>
		           </div>
		         <div class="col-md-4">
		               <label class="l" style="width:30%">客户</label>
		               <div class="ip" style="width:70%">
		                   <p class="ip-default" data-bind="text:client_employee_name()"></p>
		               </div>
		           </div>
	      	 </div>
	      	 <div class="input-row clearfloat">
		     	 <div class="col-md-12 required">
                       <label class="l" style="width:10%">说明</label>
                       <div class="ip" style="width:90%"><input type="text" maxlength="200" class="ip-" placeholder="说明" name="detail.comment" required="required"/></div>
                   </div>
	      	 </div>
	      	 <div class="input-row clearfloat">
		     	<div class="col-md-12" style="margin-top:10px">
					<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: applyRidTail">申请</a></div>
				</div>
	      	 </div>
      	 </form>
  	 </div>
  	 
  <div id="sum_submit" style="display:none;width:800px; padding-top: 30px;">
   	 <form id="form-sum">
	          <div class="input-row clearfloat">
		           <div class="col-md-6 required">
		               <label class="l" style="width:30%">账户</label>
		               <div class="ip" style="width:70%">
		                   <select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'" name="detail.card_account" required="required"></select>
		               </div>
		           </div>
		           <div class="col-md-6 required">
		               <label class="l" style="width:30%">入账总金额</label>
		               <div class="ip" style="width:70%">
		                   <input type="number" name="detail.sum_received" class="ip- amountRangeStart" required="required"/>
		               </div>
		           </div>
	      	 </div>
	      	 <div class="input-row clearfloat">
		         <div class="col-md-6 required">
		               <label class="l" style="width:30%">入账时间</label>
		               <div class="ip" style="width:70%">
		                   <input type="text" name="detail.received_time" class="form-control datetime-picker" required="required"/>
		               </div>
		           </div>
		         <div class="col-md-6 required">
		               <label class="l" style="width:30%">我组金额</label>
		               <div class="ip" style="width:70%">
		                   <input type="number" name="detail.allot_received" class="ip- amountRangeEnd" required="required"/>
		               </div>
		           </div>
	      	  </div>
	      	  <div class="input-row clearfloat">
		         <div class="col-md-3">
		               <label class="l" style="width:100%">团号</label>
		           </div>
		         <div class="col-md-3">
		               <label class="l" style="width:100%">客户</label>
		           </div>
		          <div class="col-md-3">
		               <label class="l" style="width:100%">尾款</label>
		           </div>
		            <div class="col-md-3 required">
		               <label class="l" style="width:100%">分配金额</label>
		           </div>
	      	 </div>
	      	 <!-- ko foreach:chosenReceivables -->
	      	  <div class="input-row clearfloat" st="allot">
		         <div class="col-md-3">
		               <div class="ip">
		               	   <p class="ip-default" data-bind="text:$data.team_number"></p>
		                   <input type="hidden" data-bind="value:$data.team_number" st="team_number"/>
		               </div>
		           </div>
		         <div class="col-md-3">
		               <div class="ip">
		                   	<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
		               </div>
		           </div>
		         <div class="col-md-3">
		               <div class="ip">
		               		<!-- ko if:$data.final_flg=="Y" -->
		                   	<p class="ip-default" data-bind="text:$data.final_balance"></p>
		                   	<!-- /ko -->
		                   	<!-- ko if:$data.final_flg=="N" -->
		                   	<p class="ip-default" data-bind="text:$data.budget_balance"></p>
		                   	<!-- /ko -->
		               </div>
		           </div>
		           <div class="col-md-3">
		               <div class="ip">
		                   <input type="number" class="form-control" st="received" data-bind="attr:{'name':'name-'+$data.pk}" required="required"/>
		               </div>
		           </div>
	      	  </div>
	      	 <!-- /ko -->
	      	 <div class="input-row clearfloat">
		     	<div class="col-md-12" style="margin-top:10px">
					<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: applySum">申请</a></div>
				</div>
	      	 </div>
      	 </form>
  	 </div>
  	 
  	<div id="strike_submit" style="display:none;width:800px; padding-top: 30px;">
   	 <form id="form-strike">
	          <div class="input-row clearfloat">
		           <div class="col-md-6 required">
		               <label class="l" style="width:30%">冲账金额</label>
		               <div class="ip" style="width:70%">
		                   <input type="text" name="detail.allot_received" class="form-control" id="strike-money" required="required"/>
		               </div>
		           </div>
	      	 </div>
	      	  <div class="input-row clearfloat">
		         <div class="col-md-3">
		               <label class="l" style="width:100%">团号</label>
		           </div>
		         <div class="col-md-3">
		               <label class="l" style="width:100%">客户</label>
		           </div>
		             <div class="col-md-3">
		               <label class="l" style="width:100%">尾款</label>
		           </div>
		            <div class="col-md-3 required">
		               <label class="l" style="width:100%">分配金额</label>
		           </div>
	      	 </div>
	      	   <!-- ko foreach:chosenReceivables -->
	      	  <div class="input-row clearfloat" st="strike-allot">
		         <div class="col-md-3">
		               <div class="ip">
		               	   <p class="ip-default" data-bind="text:$data.team_number"></p>
		                   <input type="hidden" data-bind="value:$data.team_number" st="strike-team_number"/>
		               </div>
		           </div>
		         <div class="col-md-3">
		               <div class="ip">
		                   	<p class="ip-default" data-bind="text:$data.client_employee_name"></p>
		               </div>
		           </div>
		         <div class="col-md-3">
		               <div class="ip">
		               		<!-- ko if:$data.final_flg=="Y" -->
		                   	<p class="ip-default" data-bind="text:$data.final_balance"></p>
		                   	<!-- /ko -->
		                   	<!-- ko if:$data.final_flg=="N" -->
		                   	<p class="ip-default" data-bind="text:$data.budget_balance"></p>
		                   	<!-- /ko -->
		               </div>
		           </div>
		           <div class="col-md-3">
		               <div class="ip">
		                   <input type="number" class="form-control" st="strike-received" data-bind="attr:{'name':'name-'+$data.pk}" required="required"/>
		               </div>
		           </div>
	      	  </div>
	      	 <!-- /ko -->
	      	  <div class="input-row clearfloat">
		         <div class="col-md-12 required">
		         	  <label class="l" style="width:10%">说明</label>
		               <div class="ip">
		                   <textarea type="text" class="ip-default" rows="15" name ="detail.comment" placeholder="需要说明的信息" required="required"></textarea>
		               </div>
		           </div>
	      	  </div>
	      	 <div class="input-row clearfloat">
		     	<div class="col-md-12" style="margin-top:10px">
					<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: applyStrike">申请</a></div>
				</div>
	      	 </div>
      	 </form>
  	 </div>
  	 
  <div id="receive_submit" style="display:none;width:800px; padding-top: 30px;">
   	 <form id="form-receive">
   	 		 <div class="input-row clearfloat">
		         <div class="col-md-6">
		               <label class="l" style="width:30%">团号</label>
		               <div class="ip" style="width:70%">
		                    <p class="ip-default" data-bind="text:team_number()"></p>
		                   <input name="detail.team_number" type="hidden" data-bind="value:team_number()"/>
		               </div>
		           </div>
		           <div class="col-md-6">
		               <label class="l" style="width:30%">客户</label>
		               <div class="ip" style="width:70%">
		                    <p class="ip-default" data-bind="text:client_employee_name()"></p>
		               </div>
		           </div>
	      	 </div>
	          <div class="input-row clearfloat">
		           <div class="col-md-6 required">
		               <label class="l" style="width:30%">账户</label>
		               <div class="ip" style="width:70%">
		                  <select class="form-control" data-bind="options: accounts, optionsCaption: '-- 请选择 --'" name="detail.card_account" required="required"></select>
		               </div>
		           </div>
		           <div class="col-md-6 required">
		               <label class="l" style="width:30%">金额</label>
		               <div class="ip" style="width:70%">
		                   <input type="number"  name="detail.received" class="form-control" required="required"/>
		               </div>
		           </div>
	      	 </div>
	      	 <div class="input-row clearfloat">
		         <div class="col-md-6 required">
		               <label class="l" style="width:30%">入账时间</label>
		               <div class="ip" style="width:70%">
		                   <input type="text" name="detail.received_time" class="form-control datetime-picker" required="required"/>
		               </div>
		           </div>
	      	  </div>
	      	 <div class="input-row clearfloat">
		     	<div class="col-md-12" style="margin-top:10px">
					<div align="right"><a type="button" class="btn btn-green btn-r" data-bind="click: applyReceive">申请</a></div>
				</div>
	      	 </div>
      	 </form>
  	 </div>
  <script>
    $(".sale").addClass("current").children("ol").css("display", "block");
  </script>
   <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
   <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
   <script src="<%=basePath %>static/js/validation.js"></script>
   <script src="<%=basePath %>static/vendor/jquery-ui.min.js"></script>
  <script src="<%=basePath %>static/vendor/datetimepicker/jquery.datetimepicker.js"></script>
  <script src="<%=basePath %>static/vendor/datetimepicker/MonthPicker.min.js"></script>
   <script src="<%=basePath %>static/js/datepicker.js"></script>
    <script src="<%=basePath%>static/js/sale/receivable.js"></script>
</body>
</html>