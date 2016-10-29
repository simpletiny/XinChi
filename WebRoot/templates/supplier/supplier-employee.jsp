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
                    <div class="span8">
                        <label class="col-md-1 control-label">关键字</label>
                        <div class="col-md-6">
                            <input type="text" class="form-control"  placeholder="公司关键字">
                        </div>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); search() }">搜索</button>
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
                            <th>地区</th>
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
                            <td data-bind="text: $data.area"></td>
                            <td data-bind="text: $data.financial_body_name"></td>
                            <td data-bind="text: $data.ellphone"></td>
                            <td data-bind="text: $data.qq"></td>
                            <td data-bind="text: $data.wechat"></td>    
                            <td data-bind="text: $data.sales"></td>          
                        </tr>
                    </tbody>
                </table>
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