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
	 		var url = '${gsmctx}/common/product-bom-select.htm';
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
				alert("请选择要删除的检验项目!");
				return;
			}
			if(confirm("确定要删除选择的检验项目吗?")){
				$.post('${gsmctx}/base/check-standard/delete.htm',{deleteIds:ids.join(",")},function(result){
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
			var url='${gsmctx}/base/check-standard/edit-indicator.htm?indicatorId=' + id;
			$.colorbox({href:url,iframe:true,
				width:$(window).width()-100,
				height:$(window).height()-20,
				overlayClose:false,
				title:"设置检验项目"
			});
		}
		//导入
		function imports(){
			var url = '${gsmctx}/base/check-standard/import-form.htm';
			$.colorbox({href:url,iframe:true, innerWidth:380, innerHeight:200,
				overlayClose:false,
				title:"导入检验标准",
				onClosed:function(){
					$("#evaluatingIndicatorList").trigger("reloadGrid");
				}
			});
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		
		function standardAttachFormater(value,options,rowObj){
			if(rowObj.standardAttachId&&rowObj.standardAttachId!='&nbsp;'){
				return "<div style='width:100%;text-align:center;' title='下载检验标准' ><a style='margin-top:2px;margin-bottom:-3px;' class=\"small-button-bg\" onclick=\"downloadAttach("+rowObj.standardAttachId+");\" href=\"#\"><span class='ui-icon ui-icon-arrowthickstop-1-s' style='cursor:pointer;'></span></a><div>";
			}else{
				return "";
			}
		}
		
		function downloadAttach(id){
			window.location.href = '${gsmctx}/base/check-standard/download-attach.htm?id=' + id;
		}
		function downloadTemplate(){
			window.location = '${gsmctx}/base/check-standard/download-template.htm';
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

	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="checkStandard";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
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
				<div class="opt-btn" style="line-height:30px;">
				<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_SAVE">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_DELETE">
				<button class='btn' onclick="deleteEvaluatingIndicator();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_IMPORT_FORM">
				<button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入检验标准</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="GSM_CHECK_STANDARD_EXPORT">
				<button  class='btn' type="button" onclick="iMatrix.export_Data('${gsmctx}/base/check-standard/export.htm');"><span><span><b class="btn-icons btn-icons-import"></b>导出</span></span></button>
				</security:authorize>
					<span id="message" style="color:red;">
					</span>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="" >
						<grid:jqGrid gridId="evaluatingIndicatorList" url="${gsmctx}/base/check-standard/list-datas.htm" code="GSM_CHECK_STANDARD" pageName="page"></grid:jqGrid>
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