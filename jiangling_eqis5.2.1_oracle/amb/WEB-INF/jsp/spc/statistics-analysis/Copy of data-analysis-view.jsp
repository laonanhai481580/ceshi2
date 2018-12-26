<%@ page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.spc.entity.LayerType" %>
<%@ page import="com.ambition.spc.entity.LayerDetail" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	calendar.set(Calendar.DAY_OF_MONTH,1);
	String startDateStr = sdf.format(calendar.getTime());
	
	calendar.add(Calendar.MONTH,1);
	calendar.add(Calendar.DATE,-1);
	String endDateStr = sdf.format(calendar.getTime());
	String featureId=Struts2Utils.getParameter("featureId");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:170px;
	}
	.input{
		width:178px;
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
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript">
	var isUsingComonLayout=false;
	var featureId=<%=featureId%>,gridHeight = 0,gridWidth=0;
	var leng = 5;
	$(document).ready(function(){
		<%if(featureId!=null){ %>
			$("#_qualityFeature").val(featureId);
			setImgUrl();
		<%}%>
		setFormWidth();
		$("#datepicker1").datepicker({changeMonth:true,changeYear:true});
		$("#datepicker2").datepicker({changeMonth:true,changeYear:true});
		$("#tabs").tabs({});
		$(".content").height($(document).height()-105);
		gridHeight = $(document).height() - 350;
		gridWidth = $(document).width() - 255;
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
				minSize:80,
				spacing_closed: 4,
				spacing_open: 4
		
			},east:{
				spacing_closed: 4,
				spacing_open: 4
			}
		});
	
	});
	
	function contentResize(){
		//alert($("#tabs").width());
		setFormWidth();
	}
	
	function setImgUrl(){
		var lastAmout=$("#lastAmout").val();
		if(isNaN(lastAmout)){
			alert("最后子组数请输入正确的数字");
			return;
		}
		var url = '${spcctx }/statistics-analysis/draw-control.htm?featureId='+$("#_qualityFeature").val() + "&nowtime=" + (new Date()).getTime();
		var histogramurl='${spcctx}/statistics-analysis/histogram-draw.htm?featureId='+$("#_qualityFeature").val() + "&nowtime=" + (new Date()).getTime();
		var cpkurl='${spcctx }/statistics-analysis/cp-moudle.htm?featureId='+$("#_qualityFeature").val() + "&nowtime=" + (new Date()).getTime();
		var dataurl='${spcctx }/statistics-analysis/data-table.htm?featureId='+$("#_qualityFeature").val() + "&nowtime=" + (new Date()).getTime();
		//添加日期
		url += "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		histogramurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		cpkurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		dataurl+= "&startDateStr=" + $("#datepicker1").val()+"&endDateStr="+$("#datepicker2").val();
		//添加最后子组数据
		url += "&lastAmout=" + $("#lastAmout").val();
		histogramurl+= "&lastAmout=" + $("#lastAmout").val();
		cpkurl+= "&lastAmout=" + $("#lastAmout").val();
		dataurl+= "&lastAmout=" + $("#lastAmout").val();
		
		if(dotWidth != "" && offside != ""){
			var myDotWidth = dotWidth;
			var myOffSide = offside;
			url += "&dotWidth=" + myDotWidth + "&offside=" + myOffSide;
		}
		
		var queryConditionStr=getQueryCondition();
		url+="&queryConditionStr="+queryConditionStr;
		histogramurl+="&queryConditionStr="+queryConditionStr;
		cpkurl+="&queryConditionStr="+queryConditionStr;
		dataurl+="&queryConditionStr="+queryConditionStr;
		$("#imgID").attr("src",url);
		$("#histogramImg").attr("src",histogramurl);
		$("#cpmoudle").load(cpkurl);
		$.post(dataurl,"",function(result){
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
		var width = $("#south").width()-20;
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
			height : 320,
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
		$("#detailTableDiv_parent").html("<table id=\"detail_table\"></table>");
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
			setImgUrl();
		}
		
	}

	var defImgWidth = 536;
	var ld = new Array();
	var ln = 0;
	var dw = 20;
	//图片移动(图片大小按 400*670 设定移位)
	var dotWidth = dw;//点间距 默认为 20
	var dataLength = leng;//总数据长度
	function setDataLength(leng){
		dataLength = leng;
	}
	var pointNumOfPic = 30;//图片上可以显示的点的总数
	var offside = leng;//默认显示第一屏数据
	 
	function lastLeft(){//最左
	    offside = pointNumOfPic;
		setImgUrl(dotWidth,offside);
	}
	
	function moveToLeft(){//向左移屏
		offside = offside - pointNumOfPic;
		if(offside < pointNumOfPic){
			offside = pointNumOfPic;
		}
		setImgUrl(dotWidth,offside);
	}
	
	function moveToRight(){ //向右移屏
		offside = offside + pointNumOfPic;
		if(offside > dataLength){
			offside = dataLength;
		}
		setImgUrl(dotWidth,offside);
	}
	 
	function lastRight(){//最右
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
		//window.open($(impID).src,'','toolbar=no,resizable=yes,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,top=50,left=50,width=950,height=550') ;  
	}
	
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="stat_analyse";
		var thirdMenu="_analysis";
	</script>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="height: 100%;" >
			<form id="contentForm" name="contentForm" method="post" action="">
				<table class="form-table-outside-border" style="width:100%;display:block;" id="searchTable">
					<tr>
						<td style="padding-left:6px;padding-bottom:4px;">
							<ul id="searchUl">
						 		<li>
						 			<span class="label">检验日期</span>
						 			<input id="datepicker1" type="text" readonly="readonly" style="width:68px" name="startDate_ge_date" value="<%=startDateStr%>"/>
						 		        至
						 		    <input id="datepicker2" type="text" readonly="readonly" style="width:68px" name="endDate_le_date" value="<%=endDateStr%>"/>
						 		</li>
						 		<li>
						 			<span class="label">质量特性</span>
						 			<s:select id="_qualityFeature" list="qualityFeatures"
									  theme="simple"
									  listKey="value" 
									  listValue="name" 
									  name="qualityFeatures">
									</s:select>
						 		</li>
						 		<li>
						 			<span class="label">最后子组数</span>
						 			<input type="text" name="lastAmout" id="lastAmout"></input>
						 		</li>
						 		<%
									ValueStack valueStack1=(ValueStack)request.getAttribute("struts.valueStack");
									List<LayerType> layerTypes=(List)valueStack1.findValue("layerTypes");
									int k=1;%>
									
								<%if(layerTypes!=null){
									for(int i=0;i<layerTypes.size();i++){ 
										LayerType layerType=(LayerType)layerTypes.get(i);
										if(layerType.getSampleMethod().equals("0")){%>
								<li>
									<span class="label"><%=layerType.getTypeName()%></span>
									<input type="hidden" name="tag_code" value="<%=layerType.getTypeCode() %>" id="tag_code"/>
									<input type="text" name="tag_value" id="tag_value"/>
								</li>
										<%}else if(layerType.getSampleMethod().equals("2")||layerType.getSampleMethod().equals("1")){%>
								<li>
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
							<%}}}%>
					 		</ul>
				 		</td>
			 		</tr>
				 	<tr>
				 		<td style="text-align:right;">
				 			<span><button class='btn' onclick="search();" type="button"><span><span><b class="btn-icons btn-icons-stata"></b>统计</span></span></button></span>
				 		</td>
				 	</tr>
				</table>
				<div style="margin-top:0px; margin-left: 0px;height:475px;">
					<table style="width:100%;height:100%;">
						<tr>
							<td style="width:75%;height:475px;" valign="top">
								<div id="container" style="height: 465px;">  
								    <div class="pane ui-layout-center">  
			       					<div id="tabs">
											<ul>
												<li><span><a href="#tabs1">控制图</a></span></li>
												<li><span><a href="#tabs2">直方图</a></span></li>
												<li><span><a href="#tabs3">原始数据</a></span></li>
											</ul>
											<!--控制图 -->
											<div id="tabs1">
												<table>
													<tr>
														<td style="width: 98%;"><img id="imgID"
															src="${ctx}/images/stat/controlpic.png" width="100%;" height="100%;" />
														</td>
														<td align="right" style="width: 98%;vertical-align: top;"> 
															<table border="0" cellpadding="0" cellspacing="0" style="height:100%;">
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/left.gif"  title="最左"  onclick="lastLeft();"/></td></tr>
												  				<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/sollleft.gif" title="向左滚动"  onclick="moveToLeft();"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/sollright.gif" title="向右滚动" onclick="moveToRight();"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/right.gif" title="最右"  onclick="lastRight()"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/fangda.bmp" title="放大"  onclick="fangda()"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/suoxiao.bmp" title="缩小"  onclick="suoxiao();"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/exportimage.gif" title="导出图片"  onclick="exportImg('imgID');"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/innormalinfo.gif" title="查看异常信息"/></td></tr>
																<tr valign="middle"><td align="center" ><img style="cursor:hand" src="${ctx}/images/icon/selectParm.gif" title="查看监控数据"/></td></tr>
																<tr valign="middle"><td align="center" >&nbsp;</td></tr>
															</table> 
														</td>
													</tr>
												</table>
											</div>
											<!-- 直方图 -->
											<div id="tabs2">
												<table style="width: 98.5%;">
													<tr>
														<td height="100%" valign="top"><img id="histogramImg"
															src="${ctx}/images/stat/histogram.png" width="100%;" height="100%;"/>
														</td>
													</tr>
												</table>
											</div>
											<!-- 原始数据 -->
											<div id="tabs3">
												<table style="width: 98.5%;" >
													<tr>
														<td id="detailTableDiv_parent">
															<table id="detail_table" style="table-layout: fixed;"></table>
														</td>
														<td><div id="detail_tablePager"></div></td>
													</tr>
												</table> 
											</div>
										</div>
			 						 </div>  
									<div class="pane ui-layout-east">
										<table>
											<tr>
												<td style="width: 25%; height: 475px;" valign="top">
													<div id="cpmoudle">
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