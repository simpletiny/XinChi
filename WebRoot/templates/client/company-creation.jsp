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
        <h2>客户财务主体新建<a href="<%=basePath %>/templates/client/company.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form">
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">主体名称</label>
                        <div class="ip"><input type="text" id="name" class="ip-default" data-bind="value: client().client_name" placeholder="主体名称" name="client.client_name" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">主体简称</label>
                        <div class="ip"><input type="text" maxlength="8" id="name" class="ip-default" data-bind="value: client().client_short_name" placeholder="主体简称" name="client.client_short_name" required="required"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">电话</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: client().telephone" placeholder="电话" name="client.telephone"/></div>
                    </div>
                    <div class="col-md-6 ">
                        <label class="l">传真</label>
                        <div class="ip"><input type="number" class="ip-" data-bind="value: client().fax" placeholder="传真" name="client.fax" /></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                     <div class="col-md-6 required">
                        <label class="l">地区</label>
                        <div class="ip"><select class="form-control" data-bind="options: clientArea, optionsCaption: '-- 请选择 --', value: client().client_area" name="client.client_area" required="required"></select></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">公司类型</label>
                        <div class="ip"><select class="form-control" data-bind="options: clientType, optionsCaption: '-- 请选择 --', value: client().client_type" name="client.client_type" required="required"></select></div>
                    </div>
                </div>
                 <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">地址</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: client().address" placeholder="地址" name="client.address"/></textarea></div>
                    </div>
                </div>
                <div class="input-row clearfloat required">
                    <div class="col-md-12">
                        <label class="l">所属销售</label>
                        <div class="ip">
                            <div data-bind="foreach: sales">
                                <em class="small-box">
                                    <input type="checkbox" st="sales" name="choosenSales" required="required" data-bind="attr: {'value': $data.pk}, checked: $root.choosenSales"/><label data-bind="text: $data.user_name"></label>
                                </em>
                            </div>
                              <em class="small-box">
                                    <input type="checkbox" id="check-public" name="choosenSales" required="required" data-bind="click:publicClient"/><label style="color:red;">公开</label>
                               </em>
                        </div>
                    </div>
                </div>
                <hr noshade color="#0066cc">
                 <h3>负责人信息</h3>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">姓名</label>
                        <div class="ip"><input type="text" class="ip- date-picker" data-bind="value: client().body_name" placeholder="姓名" name="client.body_name" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">性别</label>
                        <div class="ip"><select class="form-control" data-bind="options: genders,value: client().body_sex" name="client.body_sex" required="required"></select></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">身份证号</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: client().body_id" placeholder="身份证号" name="client.body_id"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">微信</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: client().body_wechat" placeholder="微信" name="client.body_wechat"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">手机号</label>
                        <div class="ip"><input type="text" maxlength="11" class="ip- cellphone" data-bind="value: client().body_cellphone" placeholder="手机号" name="client.body_cellphone" required="required"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">出生年</label>
                        <div class="ip"><input type="text" maxlength="4" class="ip- date_year" data-bind="value: client().body_birth_year" placeholder="出生年" name="client.body_birth_year" /></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" class="ip-default" rows="15" data-bind="value: client().comment" name ="client.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" class="btn btn-green btn-r" data-bind="click: createCompany">保存</a></div>
        </div>
    </div>
  </div>
  <script>
    $(".client").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
  <script src="<%=basePath %>static/js/client/company-creation.js"></script>
</body>
</html>