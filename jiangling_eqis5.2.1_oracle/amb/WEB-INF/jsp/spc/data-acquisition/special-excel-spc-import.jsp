<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript">
	var featureId='';
	var index=0,mainId=null;
	
	$(document).ready(function(){
		$("#featureName").focus();
		var qualityFeatures =  $("[name = 'qualityFeature']");
		if(qualityFeatures.size()>0){
			featureId = $(qualityFeatures[0]).val();
			setImgUrl();
			$(qualityFeatures[0]).attr("checked",true);
		}
	});
	
	
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
	
	function updateCl(){
		if(featureId==""){
			alert("请先选择某一质量特性！");
			return;
		}else{
			$.colorbox({href:"${spcctx}/jl-analyse/update-cl.htm?featureId="+featureId+"&date="+$("#dateValue").val(),
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
				overlayClose:false,
				title:"修改控制线",
				onClosed:function(){
					setImgUrl();
				}
			});
		}
	}	
	
	function submitForm(){
		if($("#excelType").val() == ""){
			alert("请选择EXCEL模板类型");
			return false;
		}
		if($("#myFile").val() == ""){
			alert("请上传excel文档数据!");
			return false;
		}else{
			$("#message").text("数据导入中……");
			return true;
		}
	}
	
	function qclick(obj){
		featureId = $(obj).val();
		setImgUrl();
	}
	
	
	function downloadfile(){
		if($("#excelType").val() == ""){
			alert("请选择EXCEL模板类型");
		}else{
			window.location = '${spcctx}/data-acquisition/download.htm?excelType=' + $("#excelType").val();
		}
	}
	
	//设置图片路径
	function setImgUrl(){
		var url = '${spcctx}/jl-analyse/draw-control.htm?featureId='+featureId + "&nowtime=" + (new Date()).getTime();
		$("#imgID").attr("src",url);
	}
	function selectProduct(){
		$.colorbox({href:"${spcctx}/common/process-tree-single-select.htm",
			iframe:true, 
			width:$(window).width()<400?$(window).width()-100:400,
			height:$(window).height()<500?$(window).height()-50:500,
			overlayClose:false,
			title:"选择产品"
		});
	}
	function setProcessRange(objs){
		$("#productId").val(objs[0].id);
		$("#productName").val(objs[0].name);
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_special-excel-spc-import";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/spc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/spc-data-acquisition-menu.jsp"%>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow: auto">
			<form id="contentForm" name="contentForm" method="post" action="${spcctx}/data-acquisition/import-excel-spc-datas.htm" enctype="multipart/form-data" onsubmit="return submitForm();">
				<div class="opt-btn">
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
				</div>
				<div style="margin-top:5px; margin-left: 5px;margin-right: 5px;height:600px;">
					<table  style="width:100%;display:block;" class="form-table-border-left" id="modelTable">
						<tr>
	                    	<td style="width:30%;">选择产品</td>
	                    	<td style="width:55%;">
		                    	<input type="text" name="productName" id="productName" readonly="readonly" value="${productName}"/>
		                    	<input type="hidden" name="productId" id="productId" value="${productId}"/>
		                    	<span><button class='btn' type="button" onclick="selectProduct()"><span><span><b class="btn-icons btn-icons-search"></b>选择产品</span></span></button></span>
							</td>
	                    	<td style="width:15%;text-align: center" rowspan="3">
			                    <security:authorize ifAnyGranted="spc_data-acquisition_import-datas">
		                    		<span><button class='btn' type="submit"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button></span>
		                    	</security:authorize>
	                    	</td>
	                    </tr>
	                	<tr>
	                    	<td style="width:30%;" title="下载模板填写数据后上传">选择导入的EXCEL模板类型</td>
		                    <td style="width:55%;">
		                    	<select id="excelType" name='excelType'>
		                    		<option value="">请选择</option>
		                    		<option value="冲压SPC" <s:if test="excelType==\"冲压SPC\"">selected="selected"</s:if>>冲压SPC</option>
		                    		<option value="焊装三坐标" <s:if test="excelType==\"焊装三坐标\"">selected="selected"</s:if>>焊装三坐标</option>
		                    		<option value="焊装SPC" <s:if test="excelType==\"焊装SPC\"">selected="selected"</s:if>>焊装SPC</option>
		                    		<option value="涂装SPC" <s:if test="excelType==\"涂装SPC\"">selected="selected"</s:if>>涂装SPC</option>
		                    		<option value="总装SPC" <s:if test="excelType==\"总装SPC\"">selected="selected"</s:if>>总装SPC</option>
		                    	</select>
		                    	<security:authorize ifAnyGranted="spc_data-acquisition_download">
		                    		<span><button class='btn' type="button" onclick="downloadfile()"><span><span><b class="btn-icons btn-icons-download"></b>下载模板</span></span></button></span>
		                    	</security:authorize>
							</td>
	                  	</tr>
	                   	<tr>
	                    	<td >上传模板数据</td>
	                    	<td >
	                    		<s:file name="myFile" id="myFile" cssStyle="width:280px;border:none;" theme="simple"></s:file>
	                    	</td>
	                  	</tr>
                  	</table>
                  	<fieldset style="margin-top:5px;">
						<legend>质量特性</legend>
						<div>
							<s:radio list="options" listKey="value" listValue="name" theme="simple" onclick="qclick(this)" name='qualityFeature'></s:radio>
						</div>
					</fieldset>	
					<fieldset style="margin-top:5px;height: 405px;">
						<legend>控制图</legend>
						<table>
							<tr>
								<td align="left">
									<img id="imgID" src="${ctx}/images/stat/controlpic.png" width="100%" height="380px;" style="margin-left: 3px;"/>
								</td>
							</tr>
						</table>
					</fieldset>	
				</div>
			</form>
		</div>
	</div>
</body>
</html>