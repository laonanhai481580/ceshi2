<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>方位代码</title>
		<%@include file="/common/meta.jsp" %>	
		<script src="${ctx}/widgets/validation/jquery.validate.js" type="text/javascript"></script>
		<link rel="stylesheet" type="text/css" href="${ctx}/widgets/validation/cmxform.css"/>
		<script src="${ctx}/widgets/validation/jquery.metadata.js" type="text/javascript"></script>
		<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
		<script type="text/javascript">
			function directionCodeFormValidate(){
				$("#directionCodeForm").validate({
					submitHandler: function() {
						$(".opt_btn").find("button.btn").attr("disabled","disabled");
						aa.submit('directionCodeForm','','main',showMsg);
					}
				});
			}
			function submitForm(url){
				$('#directionCodeForm').attr('action',url);
				$('#directionCodeForm').submit();
			}
			function callback(){
				addFormValidate('${fieldPermission}','directionCodeForm');
				directionCodeFormValidate();
				showMsg();
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
						<s:if test="task==null">
							<button class='btn' onclick="submitForm('${mfgctx}/base-info/direction-code/save.htm')"><span><span>保存</span></span></button>
						</s:if><s:else>
							<button class='btn' onclick="retriveTask()">
								<span><span>取回</span>
								</span>
							</button>
						</s:else>						
					</div>
					<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
					<div id="opt-content" class="form-bg">
						<form  id="directionCodeForm" name="directionCodeForm" method="post" action="">
							<input type="hidden" name="id" value="${id}"/>
							<table>
								<tr>
									<td>方位代码</td>
									<td><input name="directionCodeNo" value="${directionCodeNo}"/></td>
								</tr>
								<tr>
									<td>方位名称</td>
									<td><input name="directionCodeName" value="${directionCodeName}" class="{required:true}"/></td>
								</tr>								
							</table>						
						</form>
					</div>
					<script type="text/javascript">
						//流程环节设置的必填字段
						addFormValidate('${fieldPermission}','directionCodeForm');
						directionCodeFormValidate();
					</script>
				</aa:zone>
			</div>
	</body>
</html>