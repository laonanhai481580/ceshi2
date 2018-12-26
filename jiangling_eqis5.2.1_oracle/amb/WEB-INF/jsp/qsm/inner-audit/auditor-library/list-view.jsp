 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<c:set var="actionBaseCtx" value="${qsmctx}/inner-audit/auditor-library" />
	<script type="text/javascript">
	//确定
	function realSelect(){
		var ids = $("#defaultForm").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0){
			alert("请选择设备!");
			return;
		}
// 		if(ids.length > 1){
// 			alert("只能选择一条设备信息!");
// 			return;
// 		}
		if($.isFunction(window.parent.setAuditValue)){
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#defaultForm").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push(data);
				}
			}
			if(objs.length>0){
				window.parent.setAuditValue(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else{
			alert("页面还没有 setAuditValue 方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	//格式化数据源
	function dataSourceFormatter(cellValue,options,rowObj){
		if(rowObj.dataSource=="NEW"){
			return "<div style='background:red;text-align:center;color:white;width:100%;margin-left:-6px;'>NEW</div>";
		}
		return "<div style='background:green;text-align:center;color:white;width:100%;margin-left:-6px;'>ERP</div>";
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b><s:text name='确定'/></span></span></button>
					<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b><s:text name='取消'/></span></span></button>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name='查询'/></span></span></button>
 				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="defaultForm" url="${actionBaseCtx}/list-datas.htm"
						submitForm="defaultForm" code="QSM_AUDITOR_LIBRARY" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>