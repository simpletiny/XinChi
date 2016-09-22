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
        <h2>财务主体管理</h2>
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
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createCompany() }">新建</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editCompany() }">编辑</button>
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
                            <input type="text" class="form-control"  placeholder="关键字">
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
                            <th>财务主体简称</th>
                            <th>类型</th>
                            <th>地区</th>
                            <th>负责人</th>
                            <th>手机号</th>
                            <th>电话</th>
                            <th>传真</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: clients">
                        <tr>
                        	 <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenCompanies"/></td>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.client_short_name,attr: {href: 'company-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.client_type"></td>
                            <td data-bind="text: $data.client_area"></td>
                            <td data-bind="text: $data.body_name"></td>
                            <td data-bind="text: $data.body_cellphone"></td>
                            <td data-bind="text: $data.telephone"></td>
                             <td data-bind="text: $data.fax"></td>            
                        </tr>
                    </tbody>
                </table>
            </div>
       </div>
    </div>
  </div>
  <script>
    $(".client").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/client/company.js"></script>
</body>
</html>