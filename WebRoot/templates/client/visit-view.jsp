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
                <style>
                    .form-group { margin-bottom: 5px; }
                    .form-control{ height: 30px; }
                </style>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>拜访记录<a href="javascript:void(0)" onclick="javascript:history.go(-1);return false;"  class="cancel-create">返回</a></h2>
    </div>
    <form class="form-horizontal">
		<input type="hidden" id="employee_key" name="visit.client_employee_pk" value="<%=key%>" />
	</form>
    <div class="main-container">
       <div class="main-box">
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                        	<th></th>
                        	<th>时间</th>
                            <th>目的</th>
                            <th>辅助目的</th>
                            <th>效果</th>                    
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: visits">
                        <tr>
                        	<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenVisits"/></td>
                            <td data-bind="text: $data.date"></td>
                            <td data-bind="text: $data.target"></td>
                            <td data-bind="text: $data.sub_target"></td>
                            <td data-bind="text: $data.summary"></td>
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
    $(".client").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/client/visit-view.js"></script>
</body>
</html>