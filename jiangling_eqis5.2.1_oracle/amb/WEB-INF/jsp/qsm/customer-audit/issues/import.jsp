<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>导入台帐数据</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
			var isUsingComonLayout=false;
			function submitForm(){
				if($("#myFile").val()){
					var i=0;
					setInterval(function(){
						i++;
						var k = i%7;
						if(k==0){
							i++;
							k=1;
						}
						var message = "正在导入,请稍候";
						for(var j=0;j<k;j++){
							message += ".";
						}
						$("#importForm .mess").html(message);
					},500);
					setTimeout(function(){
						$("#submit").attr("disabled",true);
						$("#importForm :input").attr("disabled",true);
					},1000);
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
		<form id="importForm" method="post"
			action="${qsmctx}/customer-audit/issues/import-datas.htm"
			enctype="multipart/form-data" onsubmit="return submitForm();">
			<div class="opt-btn">
				<button class='btn' type="submit" id="submit">
					<span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span>
				</button>
				<div
					style="float: right; margin-left: 4px; color: red; line-height: 30px;"
					class="mess"></div>
			</div>
			<div id="opt-content">
				<table>
					<tr>
						<td align="right">选择文件:</td>
						<td><s:file name="myFile" theme="simple"></s:file>
						</td>
					</tr>
					<tr>
						<td colspan="2" style="color: red;"><s:actionmessage /></td>
					</tr>
				</table>
			</div>
		</form>
	</div>
</body>
</html>