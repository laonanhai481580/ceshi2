<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
 	   <style type="text/css">
    	.ui-jqgrid .ui-jqgrid-htable th div {
		    height:auto;
		    overflow:hidden;
		    padding-right:2px;
		    padding-top:2px;
		    position:relative;
		    vertical-align:text-top;
		    white-space:normal !important;
		}
    </style> 
	<script type="text/javascript">
	function selectBusinessUnit(obj){
		var businessUnit=$("#businessUnit").val();
		window.location.href = encodeURI('${mfgctx}/oqc/list.htm?businessUnit='+ businessUnit);
	}
	
	//重写调整高度
/*  	function contentResize(){
		var tableHeight=$('.ui-layout-center').height()-210;
		var tableWidth=_getTableWidth();
		jQuery("#inspectionList").jqGrid('setGridHeight',tableHeight);
		jQuery("#inspectionList").jqGrid('setGridWidth',tableWidth);
	
	}  */
	//重写(单行保存前处理行数据)
	function $processRowData(data){
		data.businessUnit = $("#businessUnit").val();
		return data;
	}
	//后台返回错误信息
	function $successfunc(response){
		var jsonData = eval("(" + response.responseText+ ")");
		if(jsonData.error){
			alert(jsonData.message);
		}else{
			return true;
		}
	}
	function $addGridOption(jqGridOption){
		jqGridOption.postData.businessUnit=$("#businessUnit").val();
	}
	var selRowId = null;
	function $oneditfunc(rowId){
		selRowId = rowId;
		$("#" + rowId + "_count").keyup(caculateBadRate);
		$("#" + rowId + "_samplingCount").keyup(caculateBadRate);
		$("#" + rowId + "_unQualityCount").keyup(caculateBadRate);
		$("#" + rowId + "_unQualityRate").attr("disabled","disabled");
		$("#" + rowId + "_qeManLogin").attr("disabled","disabled");
		$("#" + rowId + "_dutyManLogin").attr("disabled","disabled");
	}
	function caculateBadRate(){
		var count = $("#" + selRowId + "_count").val();
		var samplingCount = $("#" + selRowId + "_samplingCount").val();
		var unQualityCount = $("#" + selRowId + "_unQualityCount").val();
		if(isNaN(count)){
			alert("检验数必须为整数！");
			$("#" + selRowId + "_count").val("");
			$("#" + selRowId + "_count").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if(isNaN(unQualityCount)){
			alert("不良数必须为整数！");
			$("#" + selRowId + "_unQualityCount").val("");
			$("#" + selRowId + "_unQualityCount").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if(isNaN(samplingCount)){
			alert("抽检数必须为整数！");
			$("#" + selRowId + "_samplingCount").val("");
			$("#" + selRowId + "_samplingCount").focus();
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if((count-0)<(samplingCount-0)){
			alert("抽检数不能大于检验数量！");
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		if((samplingCount-0)<(unQualityCount-0)){
			alert("不良数不能大于抽检数量！");
			$("#" + selRowId + "_unQualityRate").val("");
			return;
		}
		var rate=unQualityCount*100/samplingCount;
		$("#" + selRowId + "_unQualityRate").val(rate.toFixed(2)+"%");
	}
	function partsCodeClick(){
 		var url = '${carmfgctx}/common/product-bom-select-for-improve.htm';
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择"
 		});
 	}
 	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setBomValue(datas){
 		$('#'+selRowId+'_partCode').val(datas[0].key);
 		$('#'+selRowId+'_partName').val(datas[0].value);
 	}
 	
 	//导入台账数据
	function importDatas(){
 		var businessUnit=$("#businessUnit").val();
		var url = encodeURI('${mfgctx}/oqc/import.htm?businessUnit='+businessUnit);
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入台账数据",
			onClosed:function(){
				$("#inspectionList").trigger("reloadGrid");
			}
		});
	}
	//选择提醒人员
	var selRowId = null;
	function dutyManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setDutyMan();
			}
		});			
	}
	function qeManClick(obj){	
		selRowId=obj.rowid;
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setqeMan();
			}
		});			
	}
	
	function setDutyMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_dutyMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_dutyManLogin").val(warmingManLogin);
	}
	function setqeMan(){
		var warmingMan=$("#personName").val();
		$("#"+selRowId+"_qeMan").val(warmingMan);
		var warmingManLogin=$("#personId").val();
		$("#"+selRowId+"_qeManLogin").val(warmingManLogin);
	}
	function downloadTemplate(){
		window.location = '${mfgctx}/oqc/download-template.htm';
	}	

	function copy(){
		var rowIds = $("#inspectionList").jqGrid("getGridParam","selarrrow");
		var copyId=rowIds[0];
		if(rowIds.length==0){
			alert("请选择需要复制的数据!");
			return false;
		}
		if(rowIds.length>1){
			alert("只能选择一条数据!");
			return false;
		}		
		$.post("${mfgctx}/oqc/copy.htm?id="+ copyId, null,function(data) {
			if (data.error) {
				alert(data.message);
				return false;
			}
			alert(data.message);
			$("#inspectionList").trigger("reloadGrid");
		}, "json");
	}		
	/*---------------------------------------------------------
	函数名称:customSave
	参          数:
		gridId	表格的ID
	功          能:自定义保存方法
	------------------------------------------------------------*/
	function customSave(gridId){		
		if(lastsel==undefined||lastsel==null){
			alert("当前没有可编辑的行!");
			return;
		}
		var $grid = $("#" + gridId);
		var o = getGridSaveParams(gridId);
		if ($.isFunction(gridBeforeSaveFunc)) {
			gridBeforeSaveFunc.call($grid);
		}
		$grid.jqGrid("saveRow",lastsel,o);
	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<script type="text/javascript">
		var secMenu="oqc_list";
		var thirdMenu="_OQC_INSPECTION_LIST";  
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-oqc-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="mfg_oqc_save">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="mfg_oqc_delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_OQC_EXPORT">
				<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/oqc/export.htm?businessUnit=${businessUnit }');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
				<button class="btn" onclick="iMatrix.export_Data('${mfgctx}/oqc/export-most.htm?businessUnit=${businessUnit }');"><span><span><b class="btn-icons btn-icons-export"></b>合并导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_OQC_IMPORT">
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_OQC_COPY">
				<button class="btn" onclick="copy();"><span><span><b class="btn-icons btn-icons-import"></b>复制</span></span></button>
				</security:authorize>	
				<security:authorize ifAnyGranted="mfg_oqc_save">
				<button class='btn' onclick="customSave('inspectionList');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				</security:authorize>			
				生产事业部：
								 <s:select list="businessUnits" 
									theme="simple"
									listKey="value" 
									listValue="name" 
									id="businessUnit"
									name="businessUnit"
									onchange="selectBusinessUnit(this)"
									cssStyle="width:80px"
									emptyOption="false"
									labelSeparator="">
								</s:select> 
				<input type="hidden" name="personName"  id="personName" value=""/>
				<input type="hidden" name="personId"  id="personId" value=""/>				
			<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content">
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="inspectionList" url="${mfgctx}/oqc/list-datas.htm" code="MFG_OQC_INSPECTION" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
						<script type="text/javascript">
							$(document).ready(function(){
								$("#inspectionList").jqGrid('setGroupHeaders', {
									  useColSpanStyle: true, 
									  groupHeaders:${groupHeaders}
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