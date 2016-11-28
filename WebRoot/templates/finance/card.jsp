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
        <h2>账户管理</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
         <form class="form-horizontal search-panel">
                <style>
                    .form-group { margin-bottom: 5px; }
                    .form-control{ height: 30px; }
                </style>
                    <div class="form-group" >
	                    <div style="width:10%;float:right">
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createCard() }">新建</button>
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
                        	<!-- <th></th> -->
                            <th>账户</th>
                            <th>账户id/卡号</th>
                            <th>户名</th>
                            <th>开户行</th>
                            <th>初始金额</th>
                            <th>余额</th>
                            <th>种类</th>
							<th>备注</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: cards">
                        <tr style="overflow:hidden">
                        	 <!-- <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenCards"/></td> -->
                        	 <td data-bind="text: $data.account" style="overflow:hidden"></td>
                        	  <td data-bind="text: $data.number" style="overflow:hidden"></td>
                            <td data-bind="text: $data.name" style="overflow:hidden"></td>
                            <td data-bind="text: $data.bank" style="overflow:hidden"></td>
                            <td data-bind="text: $data.init_money" class="rmb" style="overflow:hidden"></td>
                            <td data-bind="text: $data.balance" class="rmb"  style="overflow:hidden"></td>
                            <td data-bind="text: $data.type" style="overflow:hidden"></td>
                            <td data-bind="text: $data.comment" title="test" style="overflow:hidden"></td>      
                        </tr>
                    </tbody>
                </table>
            </div>
       </div>
    </div>
  </div>
  <script>
    $(".finance").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/finance/card.js"></script>
</body>
</html>