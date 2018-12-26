<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>产品结构</title>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
			function productStructureFormValidate(){
				$("#productStructureForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('productStructureForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				$('#productStructureForm').attr('action',url);
				$('#productStructureForm').submit();
			}
			function callback(){
				addFormValidate('${fieldPermission}','productStructureForm');
				productStructureFormValidate();
				showMsg();
			}
			$(function(){
				//添加验证
				productStructureFormValidate();
			});
		</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<s:if test="task==null">
							<button class='btn' onclick="submitForm('${mfgctx}/base-info/bom/save-structure.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
						</s:if><s:else>
							<button class='btn' onclick="retriveTask()">
								<span><span>取回</span>
								</span>
							</button>
						</s:else>
						
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content" class="form-bg">
						<form  id="productStructureForm" name="productStructureForm" method="post" action="">
							<input type="hidden" name="id" id="id" value="${id }"/>
							<input type="hidden" name="parentId" id="parentId" value="${parent.id}"/>
							<table>
								<tr>
									<td align=right>父级结构:</td>
									<td>${parent.name}</td>
								</tr>
								<tr>
									<td align=right>编码:</td>
									<td><input id="code" name="code" value="${code }"  class="{required:true,messages:{required:'请输入编码!'}}"/></td>
								</tr>
								<tr>
									<td align=right>名称:</td>
									<td><input id="name" name="name" value="${name }" class="{required: true,messages:{required:'请输入名称'}}"/> </td>
								</tr>
							</table>
						</form>
					</div>
				</aa:zone>
			</div>
	</body>
</html>