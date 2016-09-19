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
        <h2>公司管理</h2>
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
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); searchResumes() }">编辑</button>
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
                            <input type="text" class="form-control" data-bind="value: query().resumeContent" placeholder="公司关键字">
                        </div>
                    </div>
                    <div>
                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { resetPage(); searchResumes() }">搜索</button>
                    </div>
                </div>
            </form>
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                            <th>公司名称</th>
                            <th>简称</th>
                            <th>公司类型</th>
                            <th>财务主体</th>
                            <th>性别</th>
                            <th>手机号</th>
                            <th>身份证照片</th>
                            <th>审批</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: users">
                        <tr>
                            <td data-bind="text: $data.user_name"></td>
                            <td data-bind="text: $data.nick_name"></td>
                            <td data-bind="text: $data.user_number"></td>
                            <td data-bind="text: $data.id"></td>
                            <td data-bind="text: $data.sex"></td>
                            <td data-bind="text: $data.cellphone"></td>
                            <td><a href="javascript:void(0)" data-bind="click: function() {$parent.checkIdPic($data.id_file_name)} ">查看</a></td>
                            <td><a href="javascript:void(0)" data-bind="click: function() {$parent.agreeUser($data.pk)} ">同意</a>&nbsp;<a href="javascript:void(0)" data-bind="click: function() {$parent.rejectUser($data.pk)} ">拒绝</a></td>
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