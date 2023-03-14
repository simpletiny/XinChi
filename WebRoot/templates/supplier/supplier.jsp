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
  <style>
                    .form-group { margin-bottom: 5px; }
                    .form-control{ height: 30px; }
                </style>
</head>
<body>
<div class="main-body">
<jsp:include page="../layout.jsp" />
    <div class="subtitle">
        <h2>供应商财务主体管理</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
         <form class="form-horizontal search-panel">
              
                    <div class="form-group" >
	                    <div style="width:30%;float:right">
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { createSupplier() }">新建</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editSupplier() }">编辑</button>
		                    </div>
		                    <div>
		                        <button type="submit" class="btn btn-green col-md-1" data-bind="click: function() {blockSupplier() }">终止合作</button>
		                    </div>
		                 </div>
	                </div>
                <div class="form-group">
                    <div class="span6">
                        <label class="col-md-1 control-label">简称</label>
                        <div class="col-md-2">
                            <input type="text" class="form-control" placeholder="简称"
                                  name="supplier.supplier_short_name" />
                        </div>
                    </div>
                    <div class="span6">
                        <label class="col-md-1 control-label">地区</label>
                        <div class="col-md-2">
                            <select class="form-control" data-bind="options: provices, optionsCaption: '-- 省份--',value: supplier().supplier_provice,event:{change:ter}" name="supplier.supplier_provice"></select>
                        </div>
                    </div>
                    <div class="span6">
                        <div class="col-md-2">
                            <select class="form-control" id="city" name="supplier.supplier_city" ></select>
                        </div>
                    </div>
                    <div style="padding-top: 3px;">
                        <button type="submit" st="btn-search" class="btn btn-green col-md-1" data-bind="click: refresh">搜索</button>
                    </div>
                </div>
            </form>
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                        	<th></th>
                            <th>供应商简称</th>
                            <th>城市</th>
                            <th>负责人</th>
                            <th>手机号</th>
                            <th>结款方式</th>
                            <th>合作状态</th>
                            <th>备注</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: suppliers">
                        <tr>
                        	 <td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenCompanies"/></td>
                            <td ><a href="javascript:void(0)" data-bind="text: $data.supplier_short_name,attr: {href: 'supplier-detail.jsp?key='+$data.pk}"></a> </td>
                            <td data-bind="text: $data.supplier_city"></td>
                            <td data-bind="text: $data.body_name"></td>
                            <td data-bind="text: $data.body_cellphone"></td>
                            <td data-bind="text: $data.payment_type"></td>
                             <td data-bind="text:$root.isMapping[$data.is_cooperate]"></td>    
                             <td data-bind="text: $data.comment"></td>            
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
    <script src="<%=basePath%>static/js/supplier/supplier.js"></script>
</body>
</html>