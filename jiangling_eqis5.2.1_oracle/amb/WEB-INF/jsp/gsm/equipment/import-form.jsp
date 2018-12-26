<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<%@include file="/common/iframe-meta.jsp" %>
	<title><s:text name='gsm.title-2'/></title>
	<script type="text/javascript">
	function submitImport(){
		var url="";
			url='${gsmctx}/equipment/imports.htm';
		if($("#file").val()==''){
			alert("<s:text name='请选择导入的文件!'/>");
			return;
		}
		var filestr = $("#file").val().split("\\");
		$("#fileName").val(filestr[filestr.length-1]);
		$("#submitImportForm").attr("action",url);
		$("#submitImportForm").ajaxSubmit(function (id){
			id=id.replace("<pre>","").replace("</pre>","");
			id=id.replace("<PRE>","").replace("</PRE>","");
			id=id.replace("<pre style=\"word-wrap: break-word; white-space: pre-wrap;\">","");
			alert(id);
			window.parent.location="${gsmctx}/equipment/list.htm";
		});
	}
	//下载模板
	function downloadModel(){
		var fileName = "";
		if($("#type").val()=='noInject'|| $("#type").val()=='inject'){//导入1和导入2时
			fileName = "order-orderItem.xls";
		}else{//导入3时
			fileName = "importThree";
		}
		window.open(webRoot+"/order/download-model.htm?fileName="+fileName);
	}
	
	</script>
</head>
<body onload="">
<div class="ui-layout-center">
<div class="opt-body">
	<div class="opt-btn">
		<button class="btn" type="button" onclick="submitImport();"><span><span><s:text name="导入"/></span></span></button>
		<s:if test='#versionType=="online"'>
			<button class="btn" type="button" onclick="downloadModel();"><span><span><s:text name="下载模板"/></span></span></button>
		</s:if>
	</div>
	<div id="opt-content" >
		<form id="submitImportForm" name="submitImportForm" action="" method="post" enctype="multipart/form-data">
			<div>
				<input type="hidden" name="type" id="type" value="${type}"/>
				<input type="hidden" name="fileName" id="fileName" value=""/>
				<input type="file" name="file" id="file"/>
			</div>
		</form>
	</div>
</div>
</div>
</body>
</html>