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
        <h2>新用户审批</h2>
    </div>

    <div class="main-container">
       <div class="main-box">
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                            <th>姓名</th>
                            <th>昵称</th>
                            <th>员工号</th>
                            <th>身份证号</th>
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
  <div id="agree-panel" style="display:none">
  	<input type="hidden" id="current-pk" />
      <div class="input-row clearfloat">
	        <div class="col-md-12">
	             <label class="l">员工角色</label>
	                 <div class="ip">
	                       <div data-bind="foreach: allRoles" style="padding-top: 4px;">
	                           <em class="small-box">
	                                 <input type="checkbox" data-bind="attr: {'value': $data}, checked: $root.chosenUserRoles"/><label data-bind="text: $root.roleMapping[$data]"></label>
	                            </em>
	                        </div>
	                   </div>
	       </div>
       </div>
       <div class="input-row clearfloat" style="float:right">
	       <a type="submit" class="btn btn-green btn-r" data-bind="click: doAgree">确定</a>
	       <a type="submit" class="btn btn-green btn-r" data-bind="click: doCancel">取消</a>
     </div>
  </div>
 
 <div id="pic-check" style="display:none">
 	<jsp:include page="../common/check-picture.jsp" />
 </div>
  <script>
    $(".user").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/users/user-approve.js?v=1.0"></script>
</body>
</html>