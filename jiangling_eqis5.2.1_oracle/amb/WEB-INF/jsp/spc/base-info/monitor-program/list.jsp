<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp"%>
	<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
	<link rel="stylesheet" href="${ctx}/widgets/swfupload/css/button.css" type="text/css" />
	<script type="text/javascript" src="${ctx}/widgets/swfupload/swfupload.js"></script>
	<script type="text/javascript" src="${ctx}/widgets/swfupload/handlers.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
		
	<style type="text/css">
		.context-menu-list{
			margin:0;
			padding:0px;
			background:#EEE;
			border:1px solid #99bbe8;
			position:absolute;
			top:2px;
			left:3px;
			display:none;
			list-style-type: none;	
			z-index:10;	
		}
		.context-menu-list li{
			font-size:12px;
			line-height:24px;
			height:24px;
			cursor:pointer;
			clear:both;
		}
		.context-menu-list li .label{
			float:left;
			padding-right:8px;
		}
		.context-menu-list li .menuicon{
			height:14px;
			width:14px;
			float:left;
		}
		.add{
			margin:3px 0px 0px 3px;
			background: url(${ctx}/css/sky-blue/images/iconfile.gif) no-repeat 0px -11px;
		}
		.delete{
			margin:3px 0px 0px 3px;
			background: url(${ctx}/css/sky-blue/images/iconfile.gif) no-repeat -90px -11px;
		}
		.update{
			margin:3px 0px 0px 3px;
			background: url(${ctx}/css/sky-blue/images/iconfile.gif) no-repeat -62px -11px;
		}
		.light{
			width:16px;
			height:16px;
			position:absolute;
			z-index:0;
		}
		.green-light{
			background: url(${ctx}/images/goal/light1.gif) no-repeat ;
		}
		.red-light{
			background: url(${ctx}/images/goal/light2.gif) no-repeat;
		}
		.blue-light{
			background: url(${ctx}/images/goal/light3.gif) no-repeat;
		}
		.qlight{
			width:60px;
			height:25px;
			position:absolute;
			margin-top:-13px;
			z-index:0;
		}
		.green-qlight{
			background: green ;
		}
		.red-qlight{
			background: red;
		}
		.blue-qlight{
			background: blue;
		}
		.add11{
		margin: 3px;
		padding: 3px;
		font-size: 12PX;
		font-family:宋体;
		border-width: 1px;
		border-color:#CCCCCC;
		border-collapse :collapse;
		background-color: #efefef;
		border: 2px;
		}
		.CGaddtr{/*table tr*/
		margin: 3px;
		padding: 3px;
		text-align: left;
		font-size: 13PX;
		color: #336699;
		height:20px;
		white-space:nowrap; 
		}
	</style>

	<script type="text/javascript">
		//重写(单行保存前处理行数据)
		function $processRowData(data){
			data.monitorProgramId=$("#monitorProgramId").val();
			return data;
		}
		
		function addQualityFeature(cellValue,options,rowObj){
			return "<a  style='margin-left:40px;' class='small-button-bg' title='选择质量特性' href='#' onclick='selectQualityFeature("+cellValue+")'><span><span class='ui-icon ui-icon-plusthick'></span></span></a>";
		}
		
		function selectQualityFeature(id){
			if(id==undefined){
				alert("请先按回车保存监控点！");
				return;
			}
			$.colorbox({href:'${spcctx}/base-info/monitor-program/list-qualityfeature.htm?monitPointId='+id,iframe:true, innerWidth:1000, innerHeight:600,overlayClose:false,title:"选择质量特性"});
		}
		
		function showQualityFeature(id,myTop,myLeft,lightimageWidth,lightimageHeight){
			var url="${spcctx}/base-info/monitor-point/quality-feature-table.htm?id="+id;
			$("#qualityFeatureTable").load(url,function(){
				var $image = $("#monitorImage");
				var imageWidth = $image.width(),imageHeight = $image.height();
				var pCss = {
						'z-index' : 2,
						'display': 'block'
					};
					if(imageWidth != lightimageWidth){
						var top = myTop * imageHeight/lightimageHeight,left = myLeft * imageWidth/lightimageWidth;
						if(imageWidth > lightimageWidth){
							pCss.top = (top + 1) +"px";
							pCss.left = (left + 1) +"px";
						}else{
							pCss.top = (top - 1) +"px";
							pCss.left = (left - 1) +"px";
						}
					}else{
						pCss.top = myTop + "px";
						pCss.left = myLeft + "px";
					}
					$("#qualityFeatureTable").css(pCss);
			});
		}
		
		function dataAnalysis(id){
			$.colorbox({
				href : '${spcctx}/statistics-analysis/data-analysis-view.htm?featureId='+ id,
				iframe : true,
				innerWidth : 1000,
				innerHeight : 600,
				overlayClose : false,
				title : "质量分析"
			});
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="base_info";
		var thirdMenu="monitorTree";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-base-info-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="spc_base-info_monitor-program_list">
					<button class='btn' onclick="loadPage();"><span><span><b class="btn-icons btn-icons-reload"></b>刷新</span></span></button>
				</security:authorize>
				<!-- <button class='sexybutton' onclick="loadImage();"><span class="upload">上传监控图</span></button> -->
				<security:authorize ifAnyGranted="spc_base-info_monitor-program_list">
					<button class="btn" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传监控图</span></span></button>
				</security:authorize>
				<span style="float:left;" id="message"></span>
			</div>
			<div id="opt-content" onContextMenu="return false" style="padding:0px;margin:0px;position:absolute;overflow-y:auto;z-index:1;">
				<div id="pic"> 
					<%@ include file="monitor-program-pic.jsp" %>
				</div>
				<div id="qualityFeatureTable" style="position: absolute;z-index: 1;border: 4;display: none;" >
					<%@ include file="../monitor-point/quality-feature-table.jsp" %>
				</div>
			</div>
		</div>
	</div>
</body>
</html>