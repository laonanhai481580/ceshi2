<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.carmfg.bom.web.BomAction"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
	<head>
		<title></title>
		<%@include file="/common/meta.jsp" %>	
		<script type="text/javascript">
		var isUsingComonLayout=false;
			function submitForm(){
				if($("#importForm input[name=myFile]").val()){
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
						$("#importForm :input").attr("disabled",true);
					},1000);
					return true;
				}else{
					alert("请选择模版!");
					return false;
				}
			}
		</script>
	</head>
	<body>
		<div class="opt-body">
			<form  id="importForm" name="importForm" method="post" action="${mfgctx}/inspection-base/indicator/imports.htm" enctype ="multipart/form-data" onsubmit="return submitForm()">
				<div class="opt-btn">
				<button  class='btn' type="submit"  style="float:left;"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
					<div style="float:left;margin-left:4px;color:red;line-height:30px;" class="mess"></div>
				</div>
				<div id="opt-content">
					<table>
						<tr>
							<td align=right style="width:110px;">选择抽样标准:</td>
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