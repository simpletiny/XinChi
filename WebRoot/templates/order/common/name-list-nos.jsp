<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<div class="input-row clearfloat">
	<div class="col-md-12">
		<table style="width: 100%" id="name-table" class="table table-striped table-hover">
			<thead>
				<tr>
					<th style="width: 4%">团长</th>
					<th style="width: 4%">序号</th>
					<th style="width: 18%">姓名</th>
					<th style="width: 7%">性别</th>
					<th style="width: 7%" title="只按年份计算">年龄</th>
					<th style="width: 12%">手机号A</th>
					<th style="width: 12%">手机号B</th>
					<th style="width: 8%">证件类型</th>
					<th style="width: 22%">证件号码</th>
					<th style="width: 6%"></th>
				</tr>
			</thead>
			<tbody data-bind="foreach: passengers">
				<tr>
					<input type="hidden" data-bind="value:$data.pk" st="name-pk" />
					<input type="hidden" data-bind="value:$data.lock_flg" st="name-lock" />
					<td><input type="radio" data-bind="value:$data.chairman,checked:'Y'" name="team_chairman" /></td>
					<td st="name-index" data-bind="text:$data.name_index"></td>
					<td><input type="text" class="ip-" data-bind="value:$data.name" style="width: 90%" st="name" /></td>
					<td><select class="form-control" data-bind="value:$data.sex" style="height: 34px" st="sex">
							<option value="">选择</option>
							<option value="M">男</option>
							<option value="F">女</option>
					</select></td>
					<td><input type="number" class="ip-" data-bind="value:$data.age" oninput="inputAge()" style="width: 90%"
						st="age" /></td>
					<td><input type="text" class="ip-" data-bind="value:$data.cellphone_A" style="width: 90%" st="cellphone_A" /></td>
					<td><input type="text" class="ip-" data-bind="value:$data.cellphone_B" style="width: 90%" st="cellphone_B" /></td>
					<td><select class="form-control" style="height: 34px" st="type">
							<option value="I">身份证</option>
							<option value="P">护照</option>
					</select></td>
					<td><input type="text" class="ip-" data-bind="value:$data.id" maxlength="18" oninput="inputId()"
						style="width: 90%" st="id" /></td>
					<td><input type="button" style="width: 60%" onclick="removeName(this)" title="删除名单" value="—" /></td>
				</tr>
			</tbody>
		</table>
	</div>
</div>
<div align="right">
	<a type="submit" class="btn btn-green btn-r" data-bind="click: batName">批量导入</a> <a type="submit"
		class="btn btn-green btn-r" onclick="addName()">添加名单</a>
</div>