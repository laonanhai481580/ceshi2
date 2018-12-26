<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.LayerType" %>
<%@ page import="com.ambition.spc.entity.LayerDetail" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	String endDateStr = sdf.format(calendar.getTime()); 
	
	calendar.add(Calendar.DATE,-6);
	String startDateStr = sdf.format(calendar.getTime());
	String featureId=Struts2Utils.getParameter("featureId");
	String featureName=Struts2Utils.getParameter("featureName");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<%-- <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		height:28px;
		margin-left:10px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:120px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:260px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript">
	var featureId='<%=featureId==null?"":featureId%>',featureName='<%=featureName==null?"":featureName%>',gridHeight = 0,gridWidth=0;
	var leng = 0,currentPage = 0;
	$(document).ready(function(){
		setFormWidth();
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		$("#tabs").tabs({});
		$(".content").height($(document).height()-105);
		gridHeight = $(document).height() - 350;
		gridWidth = $(document).width() - 475;
		centerlayout = $('#container').layout({
			defaults:{
				applyDefaultStyles:	true,
				north__paneSelector : '#header',
				north__size : 66,
				west__size : 180,
				north__spacing_open : 8,
				north__spacing_closed : 31,
				center__minSize : 500,
				resizable : true,
				paneClass : 'ui-layout-pane',
				north__resizerClass : 'ui-layout-resizer',
				west__onresize : $.layout.callbacks.resizePaneAccordions,
				center__onresize : contentResize
			},
			south:{
				sliderTip : "Slide Open",
				minSize:70,
				spacing_closed: 4,
				spacing_open: 4
		
			},east:{
				spacing_closed: 4,
				spacing_open: 4
			}
		});
		if(featureId&&featureName){
			$("#_qualityFeature").val(featureId);
			$("#featureName").val(featureName);
			search();
		}
	});
	
	function contentResize(){
		//alert($("#tabs").width());
		setFormWidth();
	}
	
	function setImgUrl(dotWidth,offside){
		var lastAmout=$("#lastAmout").val();
		if(isNaN(lastAmout)){
			alert("最后子组数请输入正确的数字");
			return;
		}
		var featureId = $("#_qualityFeature").val();
		if(!featureId){
			alert("请选择质量特性!");
			return;
		}
		$.showMessage("正在查询,请稍候... ...","custom");
		var url = '${spcctx }/statistics-analysis/draw-control.htm?currentPage='+currentPage+'&featureId='+featureId + "&nowtime=" + (new Date()).getTime();
		var histogramurl='${spcctx}/statistics-analysis/histogram-draw.htm?featureId=' + featureId + "&nowtime=" + (new Date()).getTime();
		var cpkurl='${spcctx }/statistics-analysis/cp-moudle.htm?featureId=' + featureId + "&nowtime=" + (new Date()).getTime();
		var dataurl='${spcctx }/statistics-analysis/data-table.htm?featureId=' + featureId + "&nowtime=" + (new Date()).getTime();
		//添加日期
		url += "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		histogramurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		cpkurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		dataurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		//添加最后子组数据
		url += "&lastAmout=" + lastAmout;
		histogramurl+= "&lastAmout=" + lastAmout;
		cpkurl+= "&lastAmout=" + lastAmout;
		dataurl+= "&lastAmout=" + lastAmout;
		
		if(dotWidth != "" && offside != ""){
			var myDotWidth = dotWidth;
			var myOffSide = offside;
			url += "&dotWidth=" + myDotWidth + "&offside=" + myOffSide;
		}
		//添加是否显示规格线,最大值,最小值的标志
		if($(":input[name=showGuiGe][checked]").length>0){
			url += "&showGuiGe=1";
		}
		if($(":input[name=showMaxAndMin][checked]").length>0){
			url += "&showMaxAndMin=1";
		}
		var queryConditionStr=getQueryCondition();
		url+="&queryConditionStr="+queryConditionStr;
		histogramurl+="&queryConditionStr="+queryConditionStr;
		cpkurl+="&queryConditionStr="+queryConditionStr;
		dataurl+="&queryConditionStr="+queryConditionStr;
		$("#imgID").attr("src",encodeURI(url));
		$("#histogramImg").attr("src",encodeURI(histogramurl));
		$("#cpmoudle").load(encodeURI(cpkurl));
		$.post(encodeURI(dataurl),"",function(result){
			if(result.error){
				alert(result.message);
			}else{
				createDetailTable(result);
				setDataLength(result.size);
				cacheResult = result;
			}
		},'json');
	}
	//创建表格
	var detailTable = null;
	function createDetailTable(result){
		clearTable();
		var colModel=result.colModel,datas = result.tabledata;
		var width = $("#south").width();
		detailTable = $("#detail_table").jqGrid({
			datatype: "local",
			localReader : {
				id : 'custom_name'
			},
			data: datas,
			rowNum:15,
			pager:'#detail_tablePager',
			rownumbers : true,
			width : gridWidth,
			height: 320,
			colModel:colModel,
		    multiselect: false,
		   	autowidth: false,
			forceFit : false,
			shrinkToFit: true,
			viewrecords: true, 
			sortorder: "desc",
			gridComplete : function(){}
		});
	}
	//清除表格
	function clearTable(){
		if(detailTable != null){
			detailTable.GridDestroy();
			detailTable = null;
		}
		$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table><div id=\"detail_tablePager\"></div>");
	}
	
	function setFormWidth(){
		var width = _getTableWidth()-16;
		var total = parseInt(width/260);
		var addWidth = parseInt(width/total);
		addWidth = addWidth < 260?260:addWidth;
		$("#searchUl li").width(addWidth);
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$(":input","#contentForm").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	
	function getQueryCondition(){
		var queryCondition="";
		var a=$("#searchTable").find("tr>td>ul>li");
		$("#searchTable").find("tr>td>ul>li").each(function(index,obj){
			//隐藏不处理
			if(!$(obj).is(":hidden")){
				var tagValue = $(obj).find(":input[name=tag_value]").val();
				if(tagValue){
					if(queryCondition){
						queryCondition += ",";
					}
					var str = '';
					$(obj).find(":input").each(function(index,obj){
						if(obj.name){
							if(str){
								str += ","; 
							}
							str += "\"" + obj.name + "\":\"" + $(obj).val() + "\"";
						}
					});
					queryCondition += "{" + str + "}";
				}
			}
		});
		return "[" + queryCondition + "]";
	}
	
	function search(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			currentPage=0;
			setImgUrl();
		}
	}
	
	var defImgWidth = 536;
	var ld = new Array();
	var ln = 0;
	var dw = 32;
	//图片移动(图片大小按 400*670 设定移位)
	var dotWidth = dw;//点间距 默认为 20
	var dataLength = leng;//总数据长度
	function setDataLength(leng){
		dataLength = leng;
	}
	var pointNumOfPic = 33;//图片上可以显示的点的总数
	var offside = leng;//默认显示第一屏数据
	 
	function lastLeft(){//最左
		currentPage = 0;
	    offside = pointNumOfPic;
		setImgUrl(dotWidth,offside);
	}
	
	function moveToLeft(){//向左移屏
		currentPage--;
		offside = offside - pointNumOfPic;
		if(offside < pointNumOfPic){
			offside = pointNumOfPic;
		}
		setImgUrl(dotWidth,offside);
	}
	
	function moveToRight(){ //向右移屏
		currentPage++;
		offside = offside + pointNumOfPic;
		if(offside > dataLength){
			offside = dataLength;
		}
		setImgUrl(dotWidth,offside);
	}
	 
	function lastRight(){//最右
		currentPage = parseInt(dataLength/pointNumOfPic)-1;
		if(dataLength%pointNumOfPic>0){
			currentPage++;
		}
		offside = dataLength;
		setImgUrl(dotWidth,offside);
	}
	 
	function fangda(){//放大
		dotWidth = dotWidth*2;
		if(dotWidth>160){
			dotWidth=160;
		}
		pointNumOfPic = parseInt(defImgWidth/dotWidth);
		setImgUrl(dotWidth,offside);
	}
	 
	function suoxiao(){//缩小
	   	dotWidth = parseInt(dotWidth/2);
	   	if(dotWidth<5){
	   		dotWidth=5;
	   	}
	   	pointNumOfPic=parseInt(defImgWidth/dotWidth);
		setImgUrl(dotWidth,offside);
	}
	
	function exportImg(impID){//导出图片
		alert("对不起，没有设置该方法！");
// 		window.open($(impID).src,'','toolbar=no,resizable=yes,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,top=50,left=50,width=950,height=550') ;  
	}

	function setWidth(){
		$("#south").width($("#table1").width());
		$("#detail_table").width($("#south").width());
	}
	//重置查询条件的方法
	function reset() {
		$("#searchTable input").reset();
		$("#searchTable select").reset();
	}
	//选择质量特性
	function selectFeature(obj){
		$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择质量特性"
		});
	}
	
	function setFeatureValue(datas){
		var hisId = $("#_qualityFeature").val();
		$("#featureName").val(datas[0].value);
		$("#_qualityFeature").val(datas[0].id);
		//查询对应的层级信息
		if(hisId != datas[0].id){
			var url = "${spcctx}/statistics-analysis/get-layers-by-feature.htm";
			var params = {
				featureId : datas[0].id	
			};
			$.post(url,params,function(result){
				var codes = "," + result + ",";
				$("li[tag_code]").each(function(index,obj){
					var code = $(obj).attr("tag_code");
					if(codes.indexOf("," + code + ",")>-1){
						$(obj).show();
					}else{
						$(obj).hide();
						//清空
						$(obj).find(":input[name=tag_value]").val("");
					}
				});
			});
		}
	}
	
	function showExceptionMessages(){
	    var featureId=$("#_qualityFeature").val();
	    if(!featureId){
	    	alert("请先选择质量特性!");
	    	return;
	    }
		var url = "${spcctx}/process-monitor/feature-exception-message.htm?featureId=" + featureId;
		url += "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		//添加最后子组数据
		url += "&lastAmout=" + $("#lastAmout").val();
		$.colorbox({href:encodeURI(url),
			iframe:true, 
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayClose:false,
			title:"查看异常消息"
		});
	}
	//cpk图形变化的时候,调用此方法获取对应的点坐标
	function cpkChange(){
		$.clearMessage();
		var featureId=$("#_qualityFeature").val();
	    if(!featureId){
	    	return;
	    }
	    $("#controlTd .point").remove();
	    var url = '${spcctx }/statistics-analysis/get-control-points.htm?name=' + featureId;
	    $.post(url,{name:featureId},function(obj){
	    	if(obj.error){
	    		alert("获取坐标失败!"+obj.message);
	    	}else{
	    		var drawWidth = 1050,drawHeight = 400;
	    		var imgWidth = $("#imgID").width(),imgHeight = $("#imgID").height();
	    		var yBase = imgHeight*1.0/drawHeight;
	    		var xBase = imgWidth*1.0/drawWidth;
	    		for(var i=0;i<obj.length;i++){
	    			var p = obj[i];
	    			var x = p.x*xBase+13;
	    			var y = p.y*yBase+44;
	    			$("#controlTd").append("<div style='line-height:12px;width:12px;position:absolute;cursor:pointer;left:"+x+"px;top:"+y+"px;' class='point' title='"+p.date+"【单击查看详细】' onclick='showGroupInfo("+p.id+");'>&nbsp;&nbsp;&nbsp;</div>");
	    		}
	    	}
	    },'json');
	}
	//查看子组详细信息
	function showGroupInfo(groupId){
		$.colorbox({
			href:'${spcctx}/data-acquisition/maintenance-input.htm?isView=yes&groupId='+groupId,
			iframe:true,
			innerWidth:750,
			innerHeight:500,
			overlayColse:false,
			title:"子组信息"
		});
	}
	//查看判异准则
	function showBsRules(){
		$.colorbox({
			href:'${spcctx}/statistics-analysis/bs-rules-list.htm',
			iframe:true,
			innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
			innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
			overlayColse:false,
			title:"查看判异准则",
		});
	}
	//导出
	function exports(){
		var date1 = $("#datepicker1").val();
		var date2 = $("#datepicker2").val();
		if(date1>date2){
			alert("日期前后选择有误,请重新设置!");
			$("#datepicker1").focus();
		}else{
			currentPage=0;
			exportSearch();
		}
	}
	function exportSearch(dotWidth,offside){
		var lastAmout=$("#lastAmout").val();
		if(isNaN(lastAmout)){
			alert("最后子组数请输入正确的数字");
			return;
		}
		var featureId = $("#_qualityFeature").val();
		if(!featureId){
			alert("请选择质量特性!");
			return;
		}
		$.showMessage("正在导出,请稍候... ...","custom");
		var url = "${spcctx}/statistics-analysis/data-analysis-export.htm?currentPage="+currentPage+"&featureId="+featureId+"&nowtime="+(new Date()).getTime();
		url += "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		url += "&lastAmout=" + lastAmout;
		if(dotWidth != "" && offside != ""){
			var myDotWidth = dotWidth;
			var myOffSide = offside;
			url += "&dotWidth=" + myDotWidth + "&offside=" + myOffSide;
		}
		var queryConditionStr=getQueryCondition();
		url+="&queryConditionStr="+queryConditionStr;
		window.open(encodeURI(url), "导出数据分析");
		$.showMessage("");
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_data_analysis";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-statistics-analysis-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto">
			<form id="contentForm" name="contentForm" method="post" action="">
			   	<div class="opt-btn" id="btnDiv">
		 			<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
		 			<button class='btn' onclick="exports();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
		 			<button class='btn' onclick="reset();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
		 			<button class='btn' onclick="showBsRules();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查看判异准则</span></span></button>
					<span id="message" style="color:red;"></span>
					<div style="float:right;margin-right:6px;">
						<input type="checkbox" name="showGuiGe" value="1" id="showGuiGe" checked="checked"/><label for="showGuiGe">显示规格线</label>
						<input type="checkbox" name="showMaxAndMin" value="1" id="showMaxAndMin"/><label for="showMaxAndMin">显示最大、最小值</label>
					</div>
				</div>
				<table class="form-table-outside-border" style="width:99.8%;display:block;" id="searchTable">
					<tr>
						<td style="padding-left:6px;padding-bottom:4px;">
							<ul id="searchUl">
						 		<li>
						 			<span class="label">检验日期</span>
						 			<input id="datepicker1" type="text"  style="width:80px;border: none;" value="<%=startDateStr%>" name="startDate_ge_date"/>至<input id="datepicker2" type="text" readonly="readonly" style="width:80px;border: none;" value="<%=endDateStr%>" name="endDate_le_date"/>
						 		</li>
						 		<li>
						 			<span class="label">质量特性</span>
									<input style="border: none;" name="featureName" id="featureName" onclick="selectFeature(this);" readonly="readonly"/>
									<input name="qualityFeature" id="_qualityFeature" type="hidden"/>
						 		</li>
						 		<li>
						 			<span class="label">最后子组数</span>
						 			<input style="border: none;" type="text" name="lastAmout" id="lastAmout"></input>
						 		</li>
						 	<!-- </ul>
						 	<ul id="searchUl"> -->
						 		<%
									List<LayerType> layerTypes=(List<LayerType>)ActionContext.getContext().getValueStack().findValue("layerTypes");
									int k=0,m=1;%>
							
								<%if(layerTypes!=null){
								for(int i=0;i<layerTypes.size();i++){ 
									LayerType layerType=(LayerType)layerTypes.get(i);
									if("0".equals(layerType.getSampleMethod())){%>
							<li  tag_code="<%=layerType.getTypeCode() %>">
								<span class="label"><%=layerType.getTypeName()%></span>
								<input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/>
								<input type="text" name="tag_value" id="tag_value"/>
							</li>
									<%}else if("2".equals(layerType.getSampleMethod())||"1".equals(layerType.getSampleMethod())){%>
							<li tag_code="<%=layerType.getTypeCode() %>">
								<input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/>
								<span class="label"><%=layerType.getTypeName()%></span>
								<select name="tag_value">
									<option value=""></option>
									<%List<LayerDetail> layerDetails=layerType.getLayerDetails();
									for(int j=0;j<layerDetails.size();j++){
										LayerDetail layerDetail=layerDetails.get(j);%>
									<option value="<%=layerDetail.getDetailCode()%>"><%=layerDetail.getDetailName()%></option>
									<%}%>
								</select>
							</li>
							<%
							k++;
							if(k==1&&m==1){
								%>
								<br></br>
								<%
								m++;
								k=0;
							}
							if(k==4&&m>1){
								%>
								<br></br>
								<%
								m++;
								k=0;
							}
							%>
							<%}}}%>
					 	</ul>
					 </td>
 				 </tr>
		 		<!-- <tr>
			 		<td style="text-align:right;">
				 		<span id="message" style="color:red;float:left;"></span>
				 		<span>
				 			<button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button>
				 			<button class='btn' onclick="reset();" type="button"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
				 			<button class='btn' onclick="showBsRules();" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查看判异准则</span></span></button>
				 		</span>
			 		</td>
			 	</tr> -->
			</table>
				<div style="margin-top:0px; margin-left: 0px;height:500px;">
					<table style="width:100%;height:100%;">
						<tr>
							<td style="width:70%;height:500px;" valign="top">
								<div id="container" style="height: 500px;">  
								    <div class="pane ui-layout-center">  
			       						<div id="tabs">
											<ul>
												<li><span><a href="#tabs1">控制图</a></span></li>
												<li><span><a href="#tabs2">直方图</a></span></li>
												<li><span><a href="#tabs3" onclick="setWidth();">原始数据</a></span></li>
											</ul>
											<!--控制图 -->
											<div id="tabs1">
												<table id="table1">
													<tr>
														<td style="width: 98%" id="controlTd">
															<img id="imgID" src="${ctx}/images/stat/controlpic.png" width="100%;" height="100%;" onload="cpkChange();"/>
														</td>
														<td align="right" style="width: 98%;vertical-align: top;"> 
															<table border="0" cellpadding="0" cellspacing="0" style="height:100%;">
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/left.gif"  title="最左"  onclick="lastLeft();"/></td></tr>
												  				<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/sollleft.gif" title="向左滚动"  onclick="moveToLeft();"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/sollright.gif" title="向右滚动" onclick="moveToRight();"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/right.gif" title="最右"  onclick="lastRight()"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/fangda.bmp" title="放大"  onclick="fangda()"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/suoxiao.bmp" title="缩小"  onclick="suoxiao();"/></td></tr>
<%-- 																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/exportimage.gif" title="导出图片"  onclick="exportImg('tabs1');"/></td></tr> --%>
																<tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/innormalinfo.gif" title="查看异常信息" onclick="showExceptionMessages();"/></td></tr>
																<!-- <tr valign="middle"><td align="center" ><img style="cursor:pointer" src="${ctx}/images/icon/selectParm.gif" title="查看监控数据"/></td></tr> -->
																<tr valign="middle"><td align="center" >&nbsp;</td></tr>
															</table> 
														</td>
													</tr>
												</table>
											</div>
											<!-- 直方图 -->
											<div id="tabs2">
												<table style="width: 98.5%;" id="table2">
													<tr>
														<td height="100%" valign="top">
															<img id="histogramImg" src="${ctx}/images/stat/histogram.png" width="100%" height="100%;"/>
														</td>
													</tr>
												</table>
											</div>
											<!-- 原始数据 -->
											<div id="tabs3">
												<table style="width: 98.5%;" id="table3">
													<tr>
														<td id="detailTableDiv_parent">
															<table id="detail_table" style="table-layout: fixed;"></table>
															<div id="detail_tablePager"></div>
														</td>
													</tr>
												</table> 
											</div>
										</div>
			 						</div>  
									<!-- <div class="pane ui-layout-south" >
										<table>
											<tr>
												<td id="detailTableDiv_parent">
													<table id="detail_table"></table>
												</td>
												<td><div id="detail_tablePager"></div></td>
											</tr>
										</table> 
									 </div>   -->
									 <div class="pane ui-layout-east" >
										<table>
											<tr>
												<td style="width: 30%; height: 440px;" valign="top">
													<div id="cpmoudle" style="margin-left: 10px">
														<%@ include file="cp-moudle.jsp"%>
													</div>
												</td>
											</tr>
										</table> 
									</div>  
								</div>
							</td>
						</tr>
					</table>
				</div>
			</form>
		</div>
	</div>
</body>
</html>