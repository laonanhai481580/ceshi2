<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>导入供应商</title>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
		isUsingComonLayout=false;
			function submitForm(){
				if($("#myFile").val()){
					return true;
				}else{
					alert("文件不能为空!");
					return false;
				}
			}
		</script>
	</head>
	
	<body>
		<div class="opt-body">
			<form  id="supplierForm" name="supplierForm" method="post" action="${supplierctx}/archives/update-supplier-code.htm" enctype ="multipart/form-data" onsubmit="return submitForm()">
				<div class="opt-btn">
					<button class='btn' type="submit"><span><span>提交</span></span></button>
				</div>
				<div id="opt-content">
						<table>
							<tr>
								<td align=right>选择文件:</td>
								<td>
									<s:file name="myFile" theme="simple"></s:file>
								</td>
							</tr>
						</table>
				</div>
			</form>
		</div>
	</body>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>