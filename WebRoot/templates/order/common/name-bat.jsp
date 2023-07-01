<%@ page language="java" pageEncoding="UTF-8"%>
<%@taglib uri="/struts-tags" prefix="s"%>
<div style="display: none; width: 600px" id="bat-passenger">
	<div class="input-row clearfloat">
		<div class="col-md-12">
			<textarea type="text" class="ip-default" id="txt-name-list" rows="10" placeholder="姓名+身份证号。"></textarea>
		</div>
		<div class="col-md-12" style="text-align: right; margin-top: 10px">
			<a type="submit" class="btn btn-green btn-r" onclick="cancelBat()">取消</a> <a type="submit"
				class="btn btn-green btn-r" onclick="doBat()">写入</a>
		</div>
	</div>
</div>