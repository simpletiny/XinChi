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
       <form class="form-horizontal search-panel">
			<div class="form-group">
				<div style="width: 30%; float: right">
					<div>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { editUserRole() }">修改角色</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { stop() }">停用</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { reuse() }">启用</button>
						<button type="submit" class="btn btn-green col-md-1" data-bind="click: function() { changePassword() }">修改密码</button>
					</div>
				</div>
			</div>
		</form>
       <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                        	<th></th>
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
                  
                    		<!-- ko if:$data.delete_flg=='Y' -->
              			<tr style="color:red">
                        	<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenUsers" /></td>
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
							<!-- /ko -->
                     		<!-- ko if:$data.delete_flg=='N' -->
 								<tr>
                        	<td><input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.chosenUsers" /></td>
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
							<!-- /ko -->
				
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
  <div id="edit-role" style="display:none">
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
	       <a type="submit" class="btn btn-green btn-r" data-bind="click: doSave">保存</a>
	       <a type="submit" class="btn btn-green btn-r" data-bind="click: doCancel">取消</a>
     </div>
  </div>
  <div style="display: none;width:600px" id="password-new" >
		<div class="input-row clearfloat">
			<div class="col-md-12 required">
				<label class="l">新密码</label>
				<div class="ip">
				 <div class="ip"><input type="password" id="password_new" class="ip-default" placeholder="新密码"/></div>
				</div>
			</div>
		</div>
		<div class="input-row clearfloat">
			<button style="float: right; margin-top: 10px" type="submit" class="btn btn-green col-md-1" data-bind="click:doChangePassword">完成</button>
		</div>
	</div>
 <div id="pic-check" style="display:none">
 	<jsp:include page="../common/check-picture.jsp" />
 </div>
  <script>
    $(".user").addClass("current").children("ol").css("display", "block");
  </script>
    <script src="<%=basePath%>static/js/users/users.js?v=1.0"></script>
</body>
</html>