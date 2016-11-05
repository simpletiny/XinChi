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
        <h2>供应商员工编辑<a href="<%=basePath %>/templates/supplier/supplier-employee.jsp" class="cancel-create"><i class="ic-cancel"></i>取消</a></h2>
    </div>

    <div class="main-container">
        <div class="main-box">
            <form class="form-box info-form">
            
                   <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">姓名</label>
                        <div class="ip"><input type="text" class="ip- date-picker" data-bind="value: employee().body_name" placeholder="姓名" name="employee.name" required="required"/></div>
                    </div>
                    <div class="col-md-6 required">
                        <label class="l">性别</label>
                        <div class="ip"><select class="form-control" data-bind="options: genders,value: employee().body_sex" name="employee.sex" required="required"></select></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">财务主体</label>
                        <div class="ip"><input type="text" class="ip- date-picker" data-bind="value: employee().financial_body_name,click:choseFinancial" placeholder="点击选择" name="employee.financial_body_name" id="financial_body_name" required="required"/>
                        <input type="text" class="ip- date-picker" data-bind="value: employee().financial_body_pk,click:choseFinancial" style="display:none" name="employee.financial_body_pk" id="financial_body_pk" required="required"/>
                        </div>
                    </div>
                </div>
                   <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">身份证号</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().body_id" placeholder="身份证号" name="employee.id"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">出生年</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().body_birth_year" placeholder="出生年" name="employee.birth_year" /></div>
                    </div>

                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6 required">
                        <label class="l">手机号</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().body_cellphone" placeholder="手机号" name="employee.cellphone" required="required"/></div>
                    </div>
                    <div class="col-md-6">
                        <label class="l">微信</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().body_wechat" placeholder="微信" name="employee.wechat"/></div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">电话</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().telephone" placeholder="电话" name="employee.telephone"/></div>
                    </div>
                    <div class="col-md-6 ">
                        <label class="l">传真</label>
                        <div class="ip"><input type="text" min="0" class="ip-" data-bind="value: employee().fax" placeholder="传真" name="employee.fax" /></div>
                    </div>
                </div>
               <div class="input-row clearfloat">
                    <div class="col-md-6">
                        <label class="l">QQ</label>
                        <div class="ip"><input type="text" class="ip-" data-bind="value: employee().qq" placeholder="QQ" name="employee.qq"/></div>
                    </div>
                </div>
				<div class="input-row clearfloat required">
                    <div class="col-md-12">
                        <label class="l">所属销售</label>
                        <div class="ip">
                            <div data-bind="foreach: sales">
                                <em class="small-box">
                                    <input type="checkbox" data-bind="attr: {'value': $data.pk}, checked: $root.choosenSales"/><label data-bind="text: $data.user_name"></label>
                                </em>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="input-row clearfloat">
                    <div class="col-md-12">
                        <label class="l">备注</label>
                        <div class="ip"><textarea type="text" class="ip-default" rows="15" data-bind="value: employee().comment" name ="employee.comment" placeholder="需要备注说明的信息"></textarea></div>
                    </div>
                </div>
            </form>

            <div align="right"><a type="submit" class="btn btn-green btn-r" data-bind="click: createEmployee">保存</a></div>
        </div>
    </div>
  </div>
  
  
  <div id="financial_pick" style="display:none;">
      <div class="main-container">
       <div class="main-box"style="width:600px">
         <div class="form-group">
           <div class="span8">
               <label class="col-md-2 control-label">主体名称</label>
               <div class="col-md-6">
                   <input type="text" id="supplier_name" class="form-control"  placeholder="主体名称" />
               </div>
           </div>
           <div>
               <button type="submit" class="btn btn-green col-md-1" data-bind="event:{click:searchFinancial }">搜索</button>
           </div>
       </div>
             <div class="list-result">
                <table class="table table-striped table-hover">
                    <thead>
                        <tr role="row">
                            <th>财务主体简称</th>
                            <th>负责人</th>
                        </tr>
                    </thead>
                    <tbody data-bind="foreach: suppliers">
                        <tr data-bind="event: {dblclick: function(){ $parent.pickFinancial($data.supplier_short_name,$data.pk)}}">
                            <td data-bind="text: $data.supplier_short_name"> </td>
                            <td data-bind="text: $data.body_name"></td> 
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
    $(".employee").addClass("current").children("ol").css("display", "block");
  </script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/jquery.validate.min.js"></script>
    <script type="text/javascript" src="<%=basePath %>static/vendor/messages_zh.min.js"></script>
    <script src="<%=basePath %>static/js/validation.js"></script>
  <script src="<%=basePath %>static/js/supplier/employee-creation.js"></script>
</body>
</html>