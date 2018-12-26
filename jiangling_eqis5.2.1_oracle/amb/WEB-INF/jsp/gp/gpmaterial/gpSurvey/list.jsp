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
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		//**预处理数据
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			//编辑权限
			<security:authorize ifAnyGranted="averageMaterial_edit">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		//重写保存
		function customSave(gridId) {
			if (lastsel == undefined || lastsel == null) {
				alert("当前没有可编辑的行!");
				return;
			}
			var $grid = $("#" + gridId);
			var o = getGridSaveParams(gridId);
			if ($.isFunction(gridBeforeSaveFunc)) {
				gridBeforeSaveFunc.call($grid);
			}
			$grid.jqGrid("saveRow", lastsel, o);
		}
		//上传附件
		function showPicture(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function showPicture2(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick2('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles2' value='"+value+"'/><span id='"+obj.id+"_uploadBtn2' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles2'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function showPicture3(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick3('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles3' value='"+value+"'/><span id='"+obj.id+"_uploadBtn3' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles3'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function attachmentFilesClick(rowId){
			//上传附件 
			$.upload({   
				showInputId : rowId + "_showFiles",
				hiddenInputId : rowId + "_hiddenFiles",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
				}
			}); 
		}
		function attachmentFilesClick2(rowId){
			//上传附件 
			$.upload({   
				showInputId : rowId + "_showFiles2",
				hiddenInputId : rowId + "_hiddenFiles2",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles2 = $("#" + rowId + "_hiddenFiles2").val();
				}
			}); 
		}
		function attachmentFilesClick3(rowId){
			//上传附件 
			$.upload({   
				showInputId : rowId + "_showFiles3",
				hiddenInputId : rowId + "_hiddenFiles3",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles3 = $("#" + rowId + "_hiddenFiles3").val();
				}
			}); 
		}
		function addNew(){
			iMatrix.addRow();
			$("#undefined_uploadBtn").show();
			$("#undefined_uploadBtn2").show();
			$("#undefined_uploadBtn3").show();
		}
		var params = {};
		function $oneditfunc(rowId){
			selRowId = rowId;
			$("#" + rowId + "_substanceName").bind("click",function(){
				supplierClick(selRowId);
			});
			params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			params.attachmentFiles2 = $("#" + rowId + "_hiddenFiles2").val();
			params.attachmentFiles3 = $("#" + rowId + "_hiddenFiles3").val();
			params.hisAttachmentFiles = params.attachmentFiles;
			params.hisAttachmentFiles2 = params.attachmentFiles2;
			params.hisAttachmentFiles3 = params.attachmentFiles3;
			$(".uploadBtn").hide();
			$("#undefined_uploadBtn").show();
			$("#" + rowId + "_uploadBtn").show();
			$(".uploadBtn2").hide();
			$("#undefined_uploadBtn2").show();
			$("#" + rowId + "_uploadBtn2").show();
			$(".uploadBtn3").hide();
			$("#undefined_uploadBtn3").show();
			$("#" + rowId + "_uploadBtn3").show();
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
			$("#" + rowId + "_uploadBtn2").hide();
			$("#" + rowId + "_uploadBtn3").hide();
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			return data;
		}
		
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${gpctx}/gpmaterial/gpSurvey/import.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}
		function downloadTemplate(){
			window.location = '${gpctx}/gpmaterial/gpSurvey/download-template.htm';
		}
		var orderId="";
		function supplierClick(obj){
			orderId = obj;
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
	
		function setSupplierValue(objs){
			for(var i=0;i<objs.length;i++){
				var obj = objs[0];
				$("#"+orderId+"_substanceName").val(obj.name);
				$("#"+orderId+"_substanceCode").val(obj.code);
				$("#"+orderId+"_supplyMaterial").val(obj.supplyMaterial);
				$("#"+orderId+"_materialType").val(obj.materialType);
			}
		} 
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="environmental";
		var thirdMenu="gpSurveyList";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gp-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gp-environmental-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gpSurvey_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gpSurvey_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="gpSurvey_export">
						<button class="btn" onclick="iMatrix.export_Data('${gpctx}/gpmaterial/gpSurvey/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="gpSurvey_import">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
					</security:authorize>	
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${gpctx}/gpmaterial/gpSurvey/list-datas.htm" submitForm="defaultForm" code="GP_SURVEY" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>