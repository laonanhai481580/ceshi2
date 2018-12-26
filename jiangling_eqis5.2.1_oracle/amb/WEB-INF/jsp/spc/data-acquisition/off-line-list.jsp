<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/js/highcharts.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
	var featureId='';
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
	//设置质量特性
	function setFeatureValue(datas){
		$("#featureName").val(datas[0].value);
		$("#featureId").val(datas[0].id);
		featureId = datas[0].id; 
		setImgUrl();
		//controlModel(true);
		if(!datas[0].sampleCapacity){
			$("#message").text("请先到》基础维护》过程定义下》维护完整的质量特性");
			return false;
		}
		mya.innerHTML = "<span style='text-decoration:underline'><a href='${spcctx }/data-acquisition/general-model.htm?featureId="+featureId+"' title='"+datas[0].value+".xls'>"+datas[0].value+".xls&nbsp;</a></span>";
		setTimeout(function(){
			$("#message").html("");
		},3000);
	}
	//设置图片路径
	function setImgUrl(){
		var url = '${spcctx }/jl-analyse/draw-control.htm?featureId='+featureId + "&nowtime=" + (new Date()).getTime();
		$("#imgID").attr("src",url);
	}
	
	var index=0,mainId=null;
	$(document).ready(function(){
		$("#featureName").focus();
		$("#tabs").tabs({});
// 		controlModel(false);
	});
	
	function controlModel(obj){
		if(obj){
			$("#modelTable").css("display","block");
		}else{
			$("#modelTable").css("display","none");
		}
	}
	
	function lanchAmbormal(num){
		$.post('${spcctx}/process-monitor/lanch-abnormal.htm',{featureId:featureId,num:num},function(result){},'json');
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
		if(featureId==""){
		    alert("对不起，请先选择质量参数!");
		    return false;
	    }
		if($("#myFile").val()==""){
			alert("请上传excel文档数据!");
			return false;
		}else{
			return true;
		}
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acq";
		var thirdMenu="_off_line";
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
			<form id="contentForm" name="contentForm" method="post" action="${spcctx}/data-acquisition/saveParamExcel.htm" enctype="multipart/form-data" onsubmit="return submitForm();">
				<div class="opt-btn">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td style="width:50%;">
								<span>
									<security:authorize ifAnyGranted="spc_common_feature-bom-select">
										<strong style="vertical-align: middle;">质量特性</strong>
										<input name="featureName" id="featureName" style="width:150px;vertical-align: middle;border: none;" readonly="readonly"/>
										<a class="small-button-bg" style="vertical-align: middle;" onclick="selectFeature(this);" href="javascript:void(0);" title="选择质量特性">
										<span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
										<input name="featureId" id="featureId" type="hidden"/>
										<span style="vertical-align: middle;">（请选择质量特性）</span>
									</security:authorize>
								</span>
								<span style="color:red;" id="message"><s:actionmessage theme="mytheme"/></span>
							</td>
							<td style="margin-right:10px;" align="right">
								<span>
									<security:authorize ifAnyGranted="spc_jl-analyse_update-cl">
										<button class='btn' type="button" onclick="updateCl(this);"><span ><span><b class="btn-icons btn-icons-settings"></b>修改控制线</span></span></button>
									</security:authorize>
								</span>
							</td>
						</tr>
					</table>
				</div>
				<div style="margin-top:5px; margin-left: 5px;margin-right: 5px;height:480px;">
					<table  style="width:100%;display:block;" class="form-table-border-left" id="modelTable">
	                	<tr>
	                    	<td style="width:30%;" title="下载模板填写数据后上传">Excel模板（下载模板填写数据后上传）</td>
		                    <td style="width:55%;"><div id="mya"><span style="color: red;">选择质量特性后，即可下载相应的导入模板</span></div></td>
		                    <td style="width:15%;text-align: center" rowspan="2">
<%-- 			                    <security:authorize ifAnyGranted="spc_data-acquisition_save-param-excel"> --%>
		                    		<span><button class='btn' type="submit"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button></span>
<%-- 		                    	</security:authorize> --%>
	                    	</td>
	                  	</tr>
	                   	<tr>
	                    	<td >上传模板数据</td>
	                    	<td >
	                    		<s:file name="myFile" id="myFile" cssStyle="width:280px;border:none;" theme="simple"></s:file>
	                    	</td>
	                  	</tr>
                  	</table>
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