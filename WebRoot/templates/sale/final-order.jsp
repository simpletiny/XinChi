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
        <h2>决算单管理</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
         <form class="form-horizontal search-panel">
                <style>
                    .form-group { margin-bottom: 5px; }
                    .form-control{ height: 30px; }
                </style>
                    <div class="form-group" >
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
                            <th>团号</th>
                            <th>客户</th>
                            <th>出团日期</th>
                            <th>回团日期</th>
                            <th>人数</th>
                            <th>总团款</th>
                            <th>总成本</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: orders">
                        <tr>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.team_number,attr: {href: 'final-order-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.client_employee_name"></td>
                            <td data-bind="text: $data.departure_date"></td>
                            <td data-bind="text: $data.return_date"></td>
                            <td data-bind="text: $data.people_count"></td> 
                             <td data-bind="text: $data.receivable"></td>    
                             <td data-bind="text: $data.payable"></td>     
                        </tr>
                    </tbody>
                </table>
            </div>
       </div>
    </div>
  </div>
  <script>
    $(".sale").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/sale/final-order.js"></script>
</body>
</html>