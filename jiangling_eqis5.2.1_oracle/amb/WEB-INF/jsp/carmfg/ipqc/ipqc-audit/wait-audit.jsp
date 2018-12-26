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
		function addNew(){
			iMatrix.addRow();
			$("#undefined_uploadBtn").show();
			$("#undefined_uploadBtn2").show();
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
			params.hisAttachmentFiles = params.attachmentFiles;
			params.hisAttachmentFiles2 = params.attachmentFiles2;
			$(".uploadBtn").hide();
			$("#undefined_uploadBtn").show();
			$("#" + rowId + "_uploadBtn").show();
			$(".uploadBtn2").hide();
			$("#undefined_uploadBtn2").show();
			$("#" + rowId + "_uploadBtn2").show();
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
			$("#" + rowId + "_uploadBtn2").hide();
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			data.type = '<%=GpAverageMaterial.STATE_PENDING%>';
			data.factorySupply = $("#factorySupply").val();
			return data;
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
				$("#" +orderId+ "_exemption").val(datas[i].exemptionName);
			}
		}
		function startAd(str){
			var id = $("#list").jqGrid("getGridParam","selarrrow");
// 				if(id.length>1){
// 					alert("只能选择一项！");
// 					return ;
// 				}else 
					if(id.length==0){
					alert("请选择一项！");
					return ;
				}
			$.post('${gpctx}/averageMaterial/isHarmful.htm?id='+id+'&&type='+str,
		 	function(data){
		 		window.location.reload(href='${gpctx}/averageMaterial/list-datas.htm?type=1');
				alert(data);
		 	});
		}
		function selectQualityType(obj){
			window.location.href = encodeURI('${gpctx}/averageMaterial/list-pending.htm?factorySupply='+ obj.value);
		}
		function $addGridOption(jqGridOption){
			jqGridOption.postData.factorySupply = $("#factorySupply").val();
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="pending";
		var thirdMenu="averageList_pending";
	</script>
	
	 <div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ipqc-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="averageMaterial_isHarmful">
						<button href="#" class="btn" id="approve" onclick="startAd('Y')"><span><span>同意</span></span></button>
						<button href="#" class="btn" id="refuse" onclick="startAd('N')"><span><span>不同意</span></span></button>
					</security:authorize>
						
					<security:authorize ifAnyGranted="GP_HIDE">
					<button class="btn" onclick="iMatrix.settingColumns();"><span><span>配置字段</span></span></button>
					<button class="btn" myType='settingData' onclick="iMatrix.setDataVisible(this);"><span><span>配置数据显示</span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">红旗:待提交,黄旗:待审核,绿旗:审核OK.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${gpctx}/averageMaterial/list-datas.htm?type=1" submitForm="defaultForm" code="GP_AVERAGE_MATERIAL" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>