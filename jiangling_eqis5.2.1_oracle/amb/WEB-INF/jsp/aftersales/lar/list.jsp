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
		window.location.href = encodeURI('${aftersalesctx}/lar/list.htm?businessUnit='+ obj.value);
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
		$("#" + rowId + "_inputBatchNum").keyup(caculateBadRate1);//入料批数
		$("#" + rowId + "_qualifiedBatchNum").keyup(caculateBadRate1);//合格批数
		$("#" + rowId + "_batchQualificationRate").attr("disabled","disabled");//批合格率
		$("#" + rowId + "_inputCount").keyup("disabled","disabled");//入料数
		$("#" + rowId + "_inspectionCount").keyup(caculateBadRate);//抽检数
		$("#" + rowId + "_unqualifiedCount").keyup(caculateBadRate);//不良数
		$("#" + rowId + "_unqualifiedRate").attr("disabled","disabled");//不良率
		$("#" + rowId + "_customer").change(function(){
			customerNameChange(rowId);
		});
	}
	function caculateBadRate1(){
		var inputBatchNum=$("#" + selRowId + "_inputBatchNum").val();
		
		var qualifiedBatchNum=$("#" + selRowId + "_qualifiedBatchNum").val();
		if(isNaN(inputBatchNum)){
			alert("入料批数为整数！");
			$("#" + selRowId + "_inputBatchNum").focus();
			$("#" + selRowId + "_batchQualificationRate").val("");
			return;
		}
		if(isNaN(qualifiedBatchNum)){
			alert("合格批数必须为整数！");
			$("#" + selRowId + "_inputBatchNum").focus();
			$("#" + selRowId + "_batchQualificationRate").val("");
			return;
		}
		if((inputBatchNum-0)<(qualifiedBatchNum-0)){
			alert("合格批数不能大于入料批数！");
			$("#" + selRowId + "_batchQualificationRate").val("");
			return;
		}

		var rate=qualifiedBatchNum*100/inputBatchNum;
		$("#" + selRowId + "_batchQualificationRate").val(rate.toFixed(2)+"%");
		
	}
	function caculateBadRate(){
		var inputCount = $("#" + selRowId + "_inputCount").val();
		var inspectionCount = $("#" + selRowId + "_inspectionCount").val();
		var unqualifiedCount = $("#" + selRowId + "_unqualifiedCount").val();
		if(isNaN(inspectionCount)){
			alert("投入数必须为整数！");
			$("#" + selRowId + "_inspectionCount").focus();
			$("#" + selRowId + "_unqualifiedRate").val("");
			return;
		}
		if(isNaN(unqualifiedCount)){
			alert("不良数必须为整数！");
			$("#" + selRowId + "_unqualifiedCount").focus();
			$("#" + selRowId + "_unqualifiedRate").val("");
			return;
		}

		if((inspectionCount-0)<(unqualifiedCount-0)){
			alert("不良数不能大于检验数！");
			$("#" + selRowId + "_unqualifiedRate").val("");
			return;
		}
		if((inputCount-0)<(inspectionCount-0)){
			alert("检验数不能大于入料数！");
			$("#" + selRowId + "_unqualifiedRate").val("");
			return;
		}
		var rate=unqualifiedCount*100/inspectionCount;
		$("#" + selRowId + "_unqualifiedRate").val(rate.toFixed(2)+"%");
	}
	function customerNameChange(rowid){
		selRowId=rowid;	
		var customerName=$("#"+selRowId+"_customer").val();
		var url = "${aftersalesctx}/base-info/customer/place-select.htm?customerName="+customerName;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var places = result.places;
				var placeArr = places.split(",");              
				var place = document.getElementById(selRowId+"_customerFactory");
				place.options.length=0;
				var opp1 = new Option("","");
				place.add(opp1);
 				for(var i=0;i<placeArr.length;i++){
 					var opp = new Option(placeArr[i],placeArr[i]);
 					place.add(opp);
 				}
 			}
 		},'json');
	}	

	function customerModelClick(obj){
		selRowId=obj.rowid;	
		modelClick();
	}
	function ofilmModelClick(obj){
		selRowId=obj.rowid;	
		modelClick();
	}
 	function modelClick(){
		var customerName=$("#"+selRowId+"_customer").val();
 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
 		$.colorbox({href:url,iframe:true, 
 			innerWidth:700, 
			innerHeight:500,
 			overlayClose:false,
 			title:"选择机型"
 		});
 	}
 	
	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setProblemValue(datas){
 		$("#"+selRowId+"_customerModel").val(datas[0].value);
 		$("#"+selRowId+"_ofilmModel").val(datas[0].key);
 	}
	function edit(){
		var rowIds = $("#inspectionList").jqGrid("getGridParam","selarrrow");
		var editId=rowIds[0];
		if(rowIds.length==0){
			alert("请选择需要编辑的数据!");
			return false;
		}
		if(rowIds.length>1){
			alert("只能同时编辑一条数据!");
			return false;
		}		
		var businessUnit=$("#businessUnit").val();
		window.location="${aftersalesctx}/lar/input.htm?businessUnit="+businessUnit;
	}
	function editAdd(){
		var businessUnit=$("#businessUnit").val();
		window.location="${aftersalesctx}/lar/input.htm?businessUnit="+businessUnit;
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
	
	 	//导入台账数据
		function importDatas(){
	 		var businessUnit=$("#businessUnit").val();
			var url = encodeURI('${aftersalesctx}/lar/import.htm?businessUnit='+businessUnit);
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#inspectionList").trigger("reloadGrid");
				}
			});
		}
	 	
		function downloadTemplate(){
			window.location = '${aftersalesctx}/lar/download-template.htm';
		}
		function inputNew(obj){
			var businessUnit=$("#businessUnit").val();
			window.location.href = encodeURI('${aftersalesctx}/lar/input-new.htm?businessUnit='+ businessUnit);
		}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="businessUnit"  value="${businessUnit}"/>
	<script type="text/javascript">
	   var secMenu="lar";
	   var thirdMenu="lar_data";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-lar-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
			<div class="opt-btn">
			<security:authorize ifAnyGranted="AFS_LAR_DATA_SAVE">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
			   <!--  <button class='btn' onclick="inputNew();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>表单新建</span></span></button> -->
			</security:authorize>
			<security:authorize ifAnyGranted="AFS_LAR_DATA_DELETE">
				<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="AFS_LAR_DATA_DELETE">
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="AFS_LAR_DATA_EXPORT">
				<button class="btn" onclick="iMatrix.export_Data('${aftersalesctx}/lar/export.htm?businessUnit=${businessUnit }');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
			</security:authorize>			
			<security:authorize ifAnyGranted="AFS_LAR_DATA_SAVE">
				<button class='btn' onclick="customSave('inspectionList');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</security:authorize>	
				<security:authorize ifAnyGranted="AFS_LAR_DATA_IMPORT">
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
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
								<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content">
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="inspectionList" url="${aftersalesctx}/lar/list-datas.htm" code="AFS_LAR_DATA" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
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