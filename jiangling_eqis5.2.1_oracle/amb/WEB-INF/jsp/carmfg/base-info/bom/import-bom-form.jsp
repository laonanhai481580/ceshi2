<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>导入物料BOM</title>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
		var isUsingComonLayout=false;
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
				<form  id="productBomForm" name="productBomForm" method="post" action="${mfgctx}/base-info/bom/import-bom.htm" enctype ="multipart/form-data" onsubmit="return submitForm()">
					<div class="opt-btn">
					<button  class='btn'  type="submit"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
					</div>
					<div id="opt-content">
<%-- 						<input type="hidden" name="selParentId" id="selParentId" value="${selParentId}"/> --%>
						<table>
<!-- 							<tr> -->
<!-- 								<td align=right>上级BOM:</td> -->
<%-- 								<td>${parent.name}</td> --%>
<!-- 							</tr> -->
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
</html>