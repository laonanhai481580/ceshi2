<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.ambition.gp.entity.GpAverageMaterial"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
<%-- 	<%@ include file="/common/common-js.jsp" %> --%>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<%@ include file="list-script.jsp" %>
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
				isRight =  true;
			return isRight;
		}	
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			data.isHarmful = '<%=GpAverageMaterial.STATE_SUBMIT%>';
			return data;
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
			$("#" + rowId + "_exemption").bind("click",function(){
				selectProject(selRowId);
			});
			$("#" + rowId + "_testReportExpire").attr("disabled","disabled");
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
		
		//提交成有害物
		function subHarmful(obj){
	 		var id = $("#list").jqGrid("getGridParam","selarrrow");
	 		if(id.length==0){
	 			alert("请选择一项！");
	 			return ;
	 		}
	 		
	 		$.post('${gpctx}/averageMaterial/isHarmful.htm?id='+id+"&&type=N",
	 		function(data){
	 			  window.location.reload(href='${gpctx}/averageMaterial/list.htm');
				  alert(data);
	 		});
		}
// 		function calculateDate(){
// 			var testReportDate = $("#" + selRowId + "_testReportDate").val();
// 			$("#" + selRowId + "_testReportExpire").val(testReportDate);
// 		}

		//豁免清单
		var orderId="";
		function selectProject(obj){
			orderId = obj;
			$.colorbox({href:"${gpctx}/base-info/exemption/select-list.htm",
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
					overlayClose:false,
					title:"选择科目"
			});
			
		}
		function setProjectValue(datas){
			for(var i=0;i<datas.length;i++){
				$("#" +orderId+ "_exemption").val(datas[i].exemptionCode);
			}
		}
		function startUd(){
			var id = $("#list").jqGrid("getGridParam","selarrrow");
			if(editing){
				alert("请先保存正在编辑的资料！");
				return;
			}
			if(id.length==0){
				alert("请选择一项！");
				return ;
			}
			if(id.length>1){
				alert("只能选择一项！");
				return ;
			}
			var auditMan = $("#list").jqGrid("getRowData",id).auditMan;
			if(!auditMan){
				alert("请先选择审核人！");
				return ;
			}
			 $.post('${gpctx}/averageMaterial/isHarmful.htm?id='+id+'&&type=U',
		 	function(data){
		 		window.location.reload(href='${gpctx}/averageMaterial/list-datas.htm?type=0');
				alert(data);
		 	}); 
		}
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${gpctx}/averageMaterial/import.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}
		function downloadTemplate(){
			window.location = '${gpctx}/averageMaterial/download-template.htm';
		}
		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="pending";
		var thirdMenu="averageList";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gp-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/gp-pending-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="averageMaterial_save">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b><s:text name="portal.add"/></span></span></button>
						<button class="btn" onclick="customSave('list');"><span><span><b class="btn-icons btn-icons-save"></b><s:text name="portal.save"/></span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="averageMaterial_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="portal.delete"/></span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="portal.query"/></span></span></button>
					<security:authorize ifAnyGranted="averageMaterial_import">
						<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b><s:text name="导入"/></span></span></button>
						<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b></b><s:text name="下载导入模版"/></span></span></button>
					</security:authorize>	
					<security:authorize ifAnyGranted="averageMaterial_export">
						<button class="btn" onclick="iMatrix.export_Data('${gpctx}/averageMaterial/export.htm?type=0');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
					</security:authorize>
					<button href="#" class="btn" id="refuse" onclick="startUd(this)"><span><span><s:text name="portal.submit"/></span></span></button>
					<security:authorize ifAnyGranted="GP_HIDE">
					<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
					<button class="btn" myType='settingData' onclick="iMatrix.setDataVisible(this);"><span><span>配置数据显示</span></span></button>
					</security:authorize>	
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;"><s:text name="portal.编辑提示"/></span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${gpctx}/averageMaterial/list-datas.htm?type=0" submitForm="defaultForm" code="GP_AVERAGE_MATERIAL" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>