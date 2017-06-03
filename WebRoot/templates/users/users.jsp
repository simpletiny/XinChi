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
        <h2>用户管理</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                            <th>登录名</th>
                            <th>姓名</th>
                            <th>昵称</th>
                            <th>员工号</th>
                            <th>身份证号</th>
                            <th>性别</th>
                            <th>手机号</th>
                            <th>身份证照片</th>
                			<th>角色</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: users">
                        <tr>
                        	 <td data-bind="text: $data.login_name"></td>
                            <td data-bind="text: $data.user_name"></td>
                            <td data-bind="text: $data.nick_name"></td>
                            <td data-bind="text: $data.user_number"></td>
                            <td data-bind="text: $data.id"></td>
                            <td data-bind="text: $root.sexMapping[$data.sex]"></td>
                            <td data-bind="text: $data.cellphone"></td>
                            <td><a href="javascript:void(0)" data-bind="click: function() {$parent.checkIdPic($data.id_file_name)} ">查看</a></td>
                              <td data-bind="text: $data.user_roles"></td>
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
 <div id="pic-check" style="display:none">
 	<jsp:include page="../common/check-picture.jsp" />
 </div>
  <script>
    $(".user").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/users/users.js"></script>
</body>
</html>