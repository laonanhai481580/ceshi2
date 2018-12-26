<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
	<title>物料BOM</title>
	<%@include file="/common/meta.jsp" %>	
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
		<script type="text/javascript">
			isUsingComonLayout=false;
			function productBomFormValidate(){
				$("#productBomForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('productBomForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				if($("#productBomForm").valid()){
					$('#productBomForm').attr('action',url);
					$('#productBomForm').submit();				
				}
			}
			function callback(){
				addFormValidate('${fieldPermission}','productBomForm');
				productBomFormValidate();
				showMsg();
			}
			$(function(){
				//添加验证
				//productBomFormValidate();
				$('#checkDepartment').click(function(){selectDepartment("checkDepartment");});
				$('#cooperateDepartment').click(function(){selectDepartment("cooperateDepartment");});
			});
			
			function selectDepartment(inputId){
				var acsSystemUrl = "${ctx}";
				popTree({title :'选择部门',
					innerWidth:'300',
					innerHeight:'100',
					treeType:'DEPARTMENT_TREE',
					defaultTreeValue:'id',
					leafPage:'false',
					multiple:'false',
					hiddenInputId:inputId,
					showInputId:inputId,
					acsSystemUrl:acsSystemUrl,
					callBack:function(){
					}
				});
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="cost_detail_input_m">
						<button class='btn' onclick="submitForm('${costctx}/composing-detail/save.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						</security:authorize>
						<span style="padding-bottom:2px;color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
					</div>
					<form  id="productBomForm" name="productBomForm" method="post" action="" style="padding:0px;margin-top:0px;">
					<div id="opt-content">
						<input type="hidden" name="id" id="id" value="${id}"/>
						<input type="hidden" name="parentId" id="parentId" value="${parent.id}"/>
						<table>
							<tr>
								<td align=right width=70><s:if test="parent.level == 1">二级科目</s:if><s:else>二级科目</s:else>:</td>
								<td>${parent.name}</td>
							</tr>
							<tr>
								<td align=right>编码:</td>
								<td><input id="code" style="width:260px;" name="code" value="${code }"  class="{required:true,messages:{required:'请输入编码!'}}"/></td>
							</tr>
							<tr>
								<td align=right>科目名称:</td>
								<td><input id="name" style="width:260px;" name="name" value="${name }" class="{required: true,messages:{required:'请输入名称'}}"/> </td>
							</tr>
							<tr>
								<td align=right>单位:</td>
								<td>
									<s:select list="units"
										listKey="value"
										listValue="name"
										theme="simple"
										value="unit"
										name="unit"
										headerKey=""
										headerValue="请选择..."
									></s:select>
							</tr>
							<tr>
								<td align=right valign="top">备注:</td>
								<td><textarea rows="10"style="width:260px;" id="remark" name="remark">${remark}</textarea> </td>
							</tr>
							<tr>
								<td align=right>序号:</td>
								<td><input style="width:130px;" name="orderNum" value="${orderNum}" class="{required: true,digits:true,messages:{required:'请输入序号!','digits':'必须为整数!'}}"/>
								</td>
							</tr>
						</table>
					</div>
					</form>
				</aa:zone>
			</div>
	</body>
</html>