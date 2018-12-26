<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
	
	function goBack(){
		window.location='${mfgctx}/base-info/procedure-defection/list.htm';
	}
	function saveDefection(){
		var productionLineValue = $('#productionLine option:selected').val();
		var workProcedureValue = $('#workProcedure option:selected').val();
		var rows = jQuery("#defectionList").jqGrid('getRowData');
		
		var paras=new Array();
		  for(var i=0;i<rows.length;i++){
		      var row=rows[i];
		      paras.push('{"defectiveItemCode":"'+row.defectiveItemCode+'","defectiveItem":"'+row.defectiveItem+'","defectionTypeNo":"'+row.defectionTypeNo+'","defectionTypeName":"'+row.defectionTypeName+'"}');
		  }
		$.post("${mfgctx}/base-info/procedure-defection/save-all.htm",{
			params : '['+ paras.toString() + ']',productionLine:productionLineValue,workProcedure:workProcedureValue
		},function(data){
			showMsg();
			jQuery("#defectionList").jqGrid('clearGridData');
		},'json');
	}
	function selectDefection(){
		$.colorbox({href:'${mfgctx}/common/code-bom-multi-select.htm',iframe:true, innerWidth:750, innerHeight:500,overlayClose:false,title:"选择不良代码"});
	}
	function setDefectionValue(data){
		var mydata = [];
		for(var i=0;i<data.length;i++){
			mydata.push({defectiveItemCode:data[i].key,defectiveItem:data[i].value,defectionTypeNo:data[i].defectionTypeNo,defectionTypeName:data[i].defectionTypeName});
		}
		for(var i=0;i<=mydata.length;i++){
			jQuery("#defectionList").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="inspectionPoint";
		var treeMenu="code";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
					<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_BOM-MULTI-SELECT">
					<button class="btn" onclick="selectDefection();" ><span><span><b class="btn-icons btn-icons-add"></b>添加不良</span></span></button>
					</security:authorize>
					<%-- <security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_DELETE">
					<button class="btn" onclick="delRow();" ><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> 
					</security:authorize> --%>
					<security:authorize ifAnyGranted="MFG_BASE-INFO_PROCEDURE-DEFECTION_SAVE-ALL">
					<button class="btn" onclick="saveDefection();" ><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
					<button class="btn" onclick="goBack();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
					</div>
					<div id="opt-content">
						<div style="display: none;" id="message"><font class=onSuccess><nobr>保存成功！如需继续请选择添加，添加完成请点击返回！</nobr></font></div>
						<form id="contentForm" name="contentForm" method="post"  action="" onsubmit="return false;">
							<table class="form-table-without-border" style="width: 55%;">
								<tr><td style="width:10%">生产线</td>
									<td style="width:40%"><s:select list="productionLineList" 
											  theme="simple"
											  listKey="name" 
											  listValue="name" 
											  id="productionLine"
											  name="productionLine"
											  value=""
											  labelSeparator=""
											  emptyOption="false"
											  cssStyle="width:150px;"></s:select></td>
									<td style="width:10%">工序</td>
									<td style="width:40%"><s:select list="workProcedureList" 
											  theme="simple"
											  listKey="name" 
											  listValue="name"
											  value="" 
											  id="workProcedure"
											  name="workProcedure"
											  labelSeparator=""
											  emptyOption="false"
											  cssStyle="width:150px;"></s:select></td>
								</tr>
							</table>
							<table id="defectionList"></table>
							<div id="pager"></div> 
							<script type="text/javascript">
								
								$(document).ready(function(){
									jQuery("#defectionList").jqGrid({
										datatype: "local",
										rownumbers:true,
										colNames:['不良编码','不良名称','',''],
										colModel:[
										          {name:'defectiveItemCode', index:'defectiveItemCode',width:240},
										          {name:'defectiveItem', index:'defectiveItem',width:240},
										          {name:'defectionTypeNo', index:'defectionTypeNo',width:240,hidden:true},
										          {name:'defectionTypeName', index:'defectionTypeName',width:240,hidden:true}
										          ],
										
										gridComplete: function(){
											contentResize();
										},
										serializeRowData:function(data){
											//不要把id=0传回去，避免后台判断id=0或null
											if(data.id==0){
												data.id="";
												}
											return data;
										}
									});
									
						       	});
								
								
							</script>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>