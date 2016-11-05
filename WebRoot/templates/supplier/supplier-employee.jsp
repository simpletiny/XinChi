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
        <h2>供应商员工管理</h2>
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
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createSupplierEmployee() }">新建</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editEmployee() }">编辑</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); searchResumes() }">删除</button>
		                    </div>
		                 </div>
	                </div>
              <div class="form-group">
                    <div class="span6">
                        <label class="col-md-1 control-label">姓名</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="姓名"
                                  name="employee.name" />
                        </div>
                    </div>
                    <div class="span6">
                        <label class="col-md-1 control-label">财务主体</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="财务主体"
                                  name="employee.financial_body_name" />
                        </div>
                    </div>

                </div>
                 <div class="form-group">
                   <div class="span6">
                        <label class="col-md-1 control-label">地区</label>
                        <div class="col-md-2">
                            <select style="height:32px" class="form-control" data-bind="options: provices, optionsCaption: '-- 省份--',value: supplier().supplier_provice,event:{change:ter}" name="employee.employee_provice"></select>
                        </div>
                        <div class="col-md-2">
                            <select style="height:32px" class="form-control" id="city" name="employee.employee_city" ></select>
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
                            <th>姓名</th>
                            <th>性别</th>
                            <th>城市</th>
                            <th>财务主体</th>
                            <th>手机号</th>
                            <th>QQ</th>
                            <th>微信</th>
                            <th>所属销售</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: employees">
                        <tr>
                        	<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenEmployees"/></td>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.name,attr: {href: 'employee-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.sex"></td>
                            <td data-bind="text: $data.employee_city"></td>
                            <td data-bind="text: $data.financial_body_name"></td>
                            <td data-bind="text: $data.ellphone"></td>
                            <td data-bind="text: $data.qq"></td>
                            <td data-bind="text: $data.wechat"></td>    
                            <td data-bind="text: $data.sales"></td>          
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
    $(".supplier").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/supplier/supplier-employee.js"></script>
</body>
</html>