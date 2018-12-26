<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>评价等级</title>
		<%@include file="/common/meta.jsp" %>
		<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
		<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>	
		<script type="text/javascript">
		    isUsingComonLayout=false;
			function degreeFormValidate(){
				$("#degreeForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('degreeForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				$('#degreeForm').attr('action',url);
				$('#degreeForm').submit();
			}
			function callback(){
				addFormValidate('${fieldPermission}','degreeForm');
				degreeFormValidate();
				showMsg();
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<s:if test="task==null">
							<security:authorize ifAnyGranted="manager-degree-save">
							<button class='btn' onclick="submitForm('${supplierctx}/manager/degree/save.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
							</security:authorize>
						</s:if><s:else>
							<button class='btn' onclick="retriveTask()">
								<span><span>取回</span>
								</span>
							</button>
						</s:else>						
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content" class="form-bg">
						<form  id="degreeForm" name="degreeForm" method="post" action="">
							<input type="hidden" name="id" value="${id}"/>
							<table>
								<tr>
									<td>评价等级</td>
									<td><input name="estimateDegree" value="${estimateDegree}" style="width:180px;" class="{required:true,messages:{required:'请输入评价等级'}}"/></td>
								</tr>
								<tr>
									<td>得分</td>
									<td>
										<s:select list="scores" name="goal1"
											theme="simple"
											listKey="value"
											listValue="name"
											cssStyle="width:76px;">
										</s:select>至
										<s:select list="scores" name="goal2"
											theme="simple"
											listKey="value"
											listValue="name"
											cssStyle="width:76px;">
										</s:select>
									</td>
								</tr>
								<tr>
									<td rowspan="2" style="vertical-align:bottom;">红黄牌</td>
								</tr>
								<tr>
									<td>
										<input type="radio" name="waringSign" style="vertical-align: middle;" value="yellow" <s:if test="waringSign=='yellow'">checked</s:if>/><img src="${ctx}/images/set-yellow.png"/><input type="radio" name="waringSign" value="red" style="vertical-align: middle;" <s:if test="waringSign=='red'">checked</s:if>/><img src="${ctx}/images/set-red.png"/>
										<input type="radio" name="waringSign" style="vertical-align: middle;" value=""/>不选
									</td>
								</tr>
							</table>						
						</form>
					</div>
					<script type="text/javascript">
						//流程环节设置的必填字段
						addFormValidate('${fieldPermission}','degreeForm');
						degreeFormValidate();
					</script>
				</aa:zone>
			</div>
	</body>
</html>