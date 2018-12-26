<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<%@ page import="com.opensymphony.xwork2.ActionContext"%>
	<%@ page import="com.norteksoft.product.api.entity.Option"%>
<%@ page import="com.norteksoft.product.api.ApiFactory"%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
 	   <style type="text/css">
    	.ui-jqgrid .ui-jqgrid-htable th div {
		    height:auto;
		    overflow:hidden;
		    padding-right:2px;
		    padding-top:2px;
		    position:relative;
		    vertical-align:text-top;
		    white-space:normal !important;
		}
    </style> 
	<script type="text/javascript">

 	function $getExtraParams(){
		var businessUnit =  $("#businessUnit").val();
		return {businessUnit:businessUnit.toString()};
	}
	function loadDetails(){
		var businessUnit = $("#businessUnit").val();
		$("#inspectionList").jqGrid("setGridParam",{postData:{'businessUnit':businessUnit.toString()}}).trigger("reloadGrid");
	}
	//确定
	function realSelect(id){
		var ids = [];
		if(id){
			ids.push(id);
		}else{
			ids = $("#inspectionList").jqGrid("getGridParam","selarrrow");
			if(id){
				ids.push(id);
			}
		}
		if(ids.length == 0){
			alert("请选择!");
			return;
		}
		if($.isFunction(window.parent.setOqcProductValues)){//获取全部的值
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#inspectionList").jqGrid('getRowData',ids[i]);
				if(data){
					if(data.name){
						data.name = data.name.replace(/<[^<]*>/g,'');
					}
					objs.push(data);
				}
			}
			if(objs.length>0){
				window.parent.setOqcProductValues(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else if($.isFunction(window.parent.setOqcProductValues)){//获取简洁的值
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#inspectionList").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push({key:data.code,value:data.name?data.name.replace(/<[^<]*>/g,''):"",model:data.model?data.model.replace(/<[^<]*>/g,''):data.model,remadePrice:data.remadePrice,dumpingPrice:data.dumpingPrice});
				}
			}
			if(objs.length>0){
				window.parent.setOqcProductValues(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else{
			alert("页面还没有 setOqcProductValues 方法!");
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
			<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				生产事业部：
								 <s:select list="businessUnits" 
									theme="simple"
									listKey="value" 
									listValue="name" 
									id="businessUnit"
									name="businessUnit"
									onchange="loadDetails(this)"
									cssStyle="width:80px"
									emptyOption="false"
									labelSeparator="">
								</s:select> 
			</div>
			<div id="opt-content">
				<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
				<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="inspectionList" url="${mfgctx}/oqc/list-select-datas.htm" code="MFG_OQC_INSPECTION_SELECT" pageName="page" ></grid:jqGrid>
				</form>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>