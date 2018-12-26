<%@ page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title>导入数据</title>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
			var isUsingComonLayout=false;
			function submitForm(){
				if($("#myFile").val()){
					if(!confirm("导入时将覆盖相同编码的配置,确定要导入吗?")){
						return false;
					}
					$.showMessage("正在导入，请稍候... ...","custom");
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
			<form id="myForm" method="post" action="${chartdesignctx}/base-info/datasource/import-datas.htm" enctype ="multipart/form-data" onsubmit="return submitForm();">
				<div class="opt-btn">
					<button class='btn' type="submit"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
					<span style="color:red;" id="message"></span>
				</div>
				<div id="opt-content">
					<table>
						<tr>
							<td align="right">选择文件:</td>
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