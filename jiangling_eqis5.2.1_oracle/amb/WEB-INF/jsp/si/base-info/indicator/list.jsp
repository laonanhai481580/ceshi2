<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<script type="text/javascript" src="${ctx}/js/CustomCombobox.js"></script>
<script type="text/javascript">
		jQuery.extend($.jgrid.defaults,{
			loadComplete : function(){
				if(scroolTop != null){
					$($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
					scroolTop = null;
				}
			}
		});
		
		function actFormat(value){
			if(value){
				return "<div style='width:100%;text-align:center;' title='编辑检验项目' ><a class=\"small-button-bg\" onclick=\"editModelIndicator("+value+");\" href=\"#\"><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a><div>";				
			}else{
				return "";
			}
		}
		
		/**
		* 刷新检验项目
		*/
		var scroolTop = null;
		function refreshEvaluatingIndicator(){
			scroolTop = $($("#evaluatingIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#evaluatingIndicatorList").trigger("reloadGrid");
		}
		
		//选择物料BOM
	 	function selectBomValue(){
	 		var url = '${sictx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,
	 			iframe:true,
	 			innerWidth:700, 
	 			innerHeight:400,
	 			overlayClose:false,
	 			title:"选择物料BOM"
	 		});
	 	}
		
	//  //选择之后的方法 data格式{key:'a',value:'a'}
		var selRowId = null;
	 	function setFullBomValueForEdit(datas){
			$("#" + selRowId + "_materielName").val(datas[0].name);
	 		$("#" + selRowId + "_materielCode").val(datas[0].code).focus();
	 	}
	 	
	 	function showPicture(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\"  onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		function attachmentFilesClick(rowId){
			//上传附件 
			$.upload({   
				//file_types:"*.jpg;*.bmp;*.png",
				showInputId : rowId + "_showFiles",
				hiddenInputId : rowId + "_hiddenFiles",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
				}
			}); 
		}
		
		var params = {};
		function $oneditfunc(rowId){
			selRowId = rowId;
	/* 		$("#" + rowId + "_materielCode").attr("readonly","readonly")
			.click(function(){
				window.edit = true;
				selectBomValue(rowId);
			});
			$("#" + rowId + "_materielName").attr("readonly","readonly")
			.click(function(){
				window.edit = true;
				selectBomValue(rowId);
			}); */
			params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			params.hisAttachmentFiles = params.attachmentFiles;
			$(".uploadBtn").hide();
			$("#" + rowId + "_uploadBtn").show();
		}
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			return data;
		}
		//删除检验项目
		function deleteEvaluatingIndicator(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(!ids){
				alert("请选择要删除的检验物料!");
				return;
			}
			if(confirm("确定要删除选择的检验物料吗?")){
				$.post('${sictx}/base-info/indicator/delete.htm',{deleteIds:ids.join(",")},function(result){
					if(result.error){
						alert(result.message);
					}else{
						refreshEvaluatingIndicator();
					}
				},'json');
			}
		}
		//编辑检验项目
		function editModelIndicator(id){
			var url='${sictx}/base-info/indicator/edit-indicator.htm?indicatorId=' + id;
			$.colorbox({href:url,iframe:true,
				width:$(window).width()-100,
				height:$(window).height()-20,
				overlayClose:false,
				title:"设置检验项目"
			});
		}
		//导入
		function imports(){
			var url = '${sictx}/base-info/indicator/import-form.htm';
			$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
				overlayClose:false,
				title:"导入抽样标准",
				onClosed:function(){
					$("#evaluatingIndicatorList").trigger("reloadGrid");
				}
			});
		}
		//导入
		function importPatrol(){
			var url = '${sictx}/base-info/indicator/import-patrol-form.htm';
			$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
				overlayClose:false,
				title:"导入工艺检验标准",
				onClosed:function(){
					$("#evaluatingIndicatorList").trigger("reloadGrid");
				}
			});
		}
		//复制抽样标准
		function copyInspectingIndicator(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请先选择选择抽样标准!");
			}else if(ids.length>1){
				alert("只能选择一条抽样标准!");
			}else{
				window.edit = false;
				var url = '${sictx}/common/product-bom-multi-select.htm';
		 		$.colorbox({href:url,iframe:true,
		 			width:$(window).width()<900?$(window).width()-50:900,
		 			height:$(window).height()<600?$(window).height()-50:600,
		 			overlayClose:false,
		 			title:"选择物料BOM"
		 		});
			}
		}
		
		function setFullBomValue(datas){
			if(window.edit){
				setFullBomValueForEdit(datas);
				return;
			}
			var indicatorId = $("#evaluatingIndicatorList").jqGrid("getGridParam","selrow");
			if(!indicatorId){
				return;
			}
			var ids = [];
			for(var i=0;i<datas.length;i++){
				ids.push(datas[i].id);
			}
			if(ids.length>0){
				$("button").attr("disabled","disabled");
				$("#message").html("正在复制抽样标准,请稍候... ...");
				$.post("${sictx}/base-info/indicator/copy-inspecting-indicator.htm",{indicatorId:indicatorId,deleteIds:ids.join(",")},function(result){
					$("button").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						$("#evaluatingIndicatorList").trigger("reloadGrid");
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="SI_BASE-INFO_INSPECTION-BASE-INDICATOR_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		
		function indicatorAttachFormater(value,options,rowObj){
			if(rowObj.indicatorAttachId&&rowObj.indicatorAttachId!='&nbsp;'){
				return "<div style='width:100%;text-align:center;' title='下载检验标准' ><a style='margin-top:2px;margin-bottom:-3px;' class=\"small-button-bg\" onclick=\"downloadAttach("+rowObj.indicatorAttachId+");\" href=\"#\"><span class='ui-icon ui-icon-arrowthickstop-1-s' style='cursor:pointer;'></span></a><div>";
			}else{
				return "";
			}
		}
		
		function downloadAttach(id){
			window.location.href = '${sictx}/base-info/indicator/download-attach.htm?id=' + id;
		}
		function downloadTemplate(){
			window.location = '${sictx}/base-info/indicator/download-template.htm';
		}
		function patrolSettings(flag){
			if('select'==flag){
				var rowIds = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
				if(rowIds.length==0){
					alert("选择的数据为空,不能设置巡检周期!");
					return false;
				}
			}else{
				var rowIds = $("#evaluatingIndicatorList").jqGrid("getDataIDs");
				if(rowIds.length==0){
					alert("表格的数据为空,不能设置巡检周期!");
					return false;
				}
			}
			var url = '${sictx}/base-info/indicator/set-patrol.htm?selFlag='+flag;
	 		$.colorbox({href:url,iframe:true,
	 			width:$(window).width()<800?$(window).width()-50:800,
	 			height:$(window).height()<700?$(window).height()-50:700,
	 			overlayClose:false,
	 			title:"设置巡检周期",
	 			onClosed:function(){
	 				$("#evaluatingIndicatorList").trigger("reloadGrid");
	 			}
	 		});
		}
		//保存周期设置时获取的参数
		function getIndicators(selFlag,params){
			if(selFlag=='select'){
				params.ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow").join(",");
			}else{
				var postData = $("#evaluatingIndicatorList").jqGrid("getGridParam","postData");
				for(var pro in postData){
					params[pro] = postData[pro];
				}
			}
		}
		/*---------------------------------------------------------
		函数名称:showIdentifiersDiv
		参          数:
		功          能:标识为（下拉选）
		------------------------------------------------------------*/
		function showIdentifiersDiv(){
			if($("#flag").css("display")=='none'){
				removeSearchBox();
				$("#flag").show();
				var position = $("#_task_button").position();
				$("#flag").css("left",position.left+15);
				$("#flag").css("top",position.top+28);
			}else{
				$("#flag").hide();
			}
		}
		
		var identifiersDiv;
		function hideIdentifiersDiv(){
			identifiersDiv = setTimeout('$("#flag").hide()',300);
		}
		
		function show_moveiIdentifiersDiv(){
			clearTimeout(identifiersDiv);
		}
		function timeIntervalTypeFormatter(value,p,obj){
			var type = obj['patrolSettings.timeIntervalType'];
			if(type){
				if("fixed"==type){
					return "间隔" + obj['patrolSettings.timeIntervalValue'] + "小时";
				}else{
					return obj['patrolSettings.timeIntervalValue'];
				}
			}else{
				return '';
			}
		}
		function remindSwitchFormatter(value,p,obj){
			var val = obj['patrolSettings.remindSwitch'];
			if('true'==val){
				return "是";
			}else{
				return '否';
			}
		}
		function remindTimeFormatter(value,p,obj){
			var type = obj['patrolSettings.remindTimeType'];
			if(type&&'true'==obj['patrolSettings.remindSwitch']){
				if("fixed"==type){
					return "超期" + obj['patrolSettings.remindTimeValue'] + "小时";
				}else{
					return obj['patrolSettings.remindTimeValue'];
				}
			}else{
				return '';
			}
		}
		function viewHistory(){
			var ids = $("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow");
			if(ids.length==0){
				alert("请先选择选择检验标准!");
			}else if(ids.length>1){
				alert("只能选择一条检验标准!");
			}else{
				var rowData = $("#evaluatingIndicatorList").jqGrid("getRowData",ids[0]);
				var url = '${sictx}/base-info/indicator/list-history.htm?materielCode=' + rowData.materielCode;
				url += "&workingProcedure=" + rowData.workingProcedure;
				window.location = url;
			}
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="inspectingIndicator";
		var treeMenu = "indicator";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/si-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/si-base-info-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" style="line-height:30px;">
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_INDICATOR_SAVE">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_INDICATOR_DELETE">
				<button class='btn' onclick="deleteEvaluatingIndicator();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
<!-- 				<button  class='btn' type="button" onclick="copyInspectingIndicator();"><span><span><b class="btn-icons btn-icons-paste"></b>复制检验标准</span></span></button> -->
<!-- 				<button class="sexybutton" onclick="editEvaluatingIndicator();"><span class="edit">编辑 </span></button>  -->
<!-- 					<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导入</span></span></button> -->
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_INDICATOR_IMPORT">
				<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入检验标准</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_INDICATOR_EXPORT">
				<button  class='btn' type="button" onclick="iMatrix.export_Data('${sictx}/base-info/indicator/export.htm');"><span><span><b class="btn-icons btn-icons-import"></b>导出</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="SI_INSPECTION_BASE_INDICATOR_SET_PATROL">
				<button  class='btn' type="button" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-settings"></b>巡检周期设置...</span></span></button>
				</security:authorize>
					<span id="message" style="color:red;">
					</span>
				<button class='btn' type="button" onclick="viewHistory();"><span><span><b class="btn-icons btn-icons-search"></b>查看所有版本</span></span></button>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="" >
						<grid:jqGrid gridId="evaluatingIndicatorList" url="${sictx}/base-info/indicator/list-datas.htm" code="SI_INSPECTING_INDICATOR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
			<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
				<ul >
				 <li onclick="patrolSettings('select');">
				 <a href="#">当前所选</a>
				 </li>
				 <li onclick="patrolSettings('all');">
				 <!-- <span><img style="border: none;" src="/imatrix/task/images/blue.gif"/></span> -->
				 <a href="#">全部</a>
				 </li>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>