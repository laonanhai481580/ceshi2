<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		//一级代码列表
		var measurementType = '${gsmCodeRules.measurementType}';
		<%String gsmCodeRulesId = request.getParameter("gsmCodeRulesId");
		if(StringUtils.isNotEmpty(gsmCodeRulesId)){%>
			function $addGridOption(jqGridOption){
				jqGridOption.postData.gsmCodeRulesId=<%=gsmCodeRulesId%>;
			}
		<%}%>
		//格式化类别
		function click(cellvalue, options, rowObject){	
			return "<div style='text-align:center;'>"+measurementType+"</div>";
		}
		//回调信息
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		//数据查询参数
		function $processRowData(data){
			data.gsmCodeRulesId = '<%=gsmCodeRulesId%>';
			return data;
		}
		//编辑行前
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="gsm_equipment_code-sec-rules_save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="codeRules";
		var thirdMenu="MyCodeRules";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-code-rules-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_equipment_code-sec-rules_save">			
						<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gsm_equipment_code-sec-rules_delete">
						<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
						<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="gsm_equipment_code-sec-rules_export">
						<button class='btn' onclick="iMatrix.export_Data('${gsmctx}/code-sec-rules/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
					</security:authorize> 
					<button class='btn' type="button" onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>	 
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<grid:jqGrid gridId="defectionTypeList" url="${gsmctx}/code-sec-rules/list-datas.htm" code="MEASUREMENT_CODE_SECRULES" pageName="page" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>