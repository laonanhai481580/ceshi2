<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page contentType="text/html;charset=UTF-8" import="com.ambition.iqc.entity.SampleCodeLetter"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	String baseType = SampleCodeLetter.GB_TYPE;
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		//样本量字码台帐			
		$(function(){
			setTimeout(function(){},100);
		});
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		//修改当前的检验水平
		function updateInspectionLevel(){
			var html = '<div id="inspectionLevelBody"><div style="width:300px;background:url(/amb/css/sky-blue/images/content-btn-title-center.gif) repeat-x left top;padding: 3px 8px;height:27px;"><button class="btn" onclick="saveInspectionLevel();"><span><span>保存</span></span></button><span id="setMessage" style="color:red;padding-left:4px;"></span></div>';
			html += '<div style="padding:6px;padding-top:12px;">检验水平:<select id="inspectionLevel">${inspectionLevelOptions}</select></div>';
			html += '</div>';
			$.colorbox({
				title : '设置检验水平',
				html:html,
				height:200
			});
			$("#inspectionLevel").val($("#hiddenInspectionLevel").val());
		}
		function saveInspectionLevel(){
			$("#setMessage").html("正在保存,请稍候... ...");
			$("#inspectionLevelBody").attr("disabled",true);
			var url = "${iqcctx}/sample-standard/code-letter/save-inspection-level.htm";
			$.post(url,{inspectionLevel:$("#inspectionLevel").val()},function(result){
				if(result.error){
					$("#setMessage").html(result.message);
					$("#inspectionLevelBody").attr("disabled","");
				}else{
					$("#hiddenInspectionLevel").val(result.value);
					$("#showInspectionLevel").html(result.name);
					$.colorbox.close();
				}
			},"json");
		}
		function $processRowData(data){
			data.baseType = "<%=baseType%>";
			return data;
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="code-letter-save">
				isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="standard";
		var thirdMenu="_code_letter";
		var treeMenu="2828-code-letter";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-sample-standard-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="iqc-sample-code-letter-save">
						<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="iqc-sample-code-letter-delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="list" url="${iqcctx}/sample-standard/code-letter/list-datas.htm" code="IQC_SAMPLE_CODE_LETTER"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#list").jqGrid('setGroupHeaders', {
				  useColSpanStyle: true, 
				  groupHeaders:[
					{startColumnName: 'batchSize1', numberOfColumns: 2, titleText: '批量'},
					{startColumnName: 'special1', numberOfColumns: 4, titleText: '特殊检验水平'},
					{startColumnName: 'ordinary1', numberOfColumns: 3, titleText: '一般检验水平'}
				  ]
			});
		});
	</script>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>