<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function booleanformatter(cellvalue, options, rowObject){
		if(cellvalue){
			return "是";
		}else{
			return "否";
		}
	}
	function mothformatter(cellvalue, options, rowObject){
		if(cellvalue=="one"){
			return "一月";
		}else if(cellvalue=="two"){
			return "二月";
		}else if(cellvalue=="three"){
			return "三月";
		}else if(cellvalue=="four"){
			return "四月";
		}else if(cellvalue=="five"){
			return "五月";
		}else if(cellvalue=="six"){
			return "六月";
		}else if(cellvalue=="seven"){
			return "七月";
		}else if(cellvalue=="eight"){
			return "八月";
		}else if(cellvalue=="nine"){
			return "九月";
		}else if(cellvalue=="ten"){
			return "十月";
		}else if(cellvalue=="eleven"){
			return "十一月";
		}else if(cellvalue=="tweleve"){
			return "十二月";
		}
	}
	
	$(document).ready(function(){
		var params = ${params};	
		var postData = {};
		for(var pro in params){
			postData['params.' + pro] = params[pro];
		}
		jQuery("#inspectionReportList").jqGrid({
			datatype: "json",
			jsonReader:{
				repeatitems:false
			},
			height:520,
			prmNames:{
				rows:'page.pageSize',
				page:'page.pageNo',
				sort:'page.orderBy',
				order:'page.order'
			}, 
			loadtext:"<s:text name='读取中....'/>",
			postData : postData,
			rownumbers: true,
			multiselect:false,
			autowidth:true,
			shrinkToFit:false,
			url:'${gsmctx}/inspectionMsa-chart/check-qualified-chart-detail-datas.htm',
			colNames:["<s:text name='器具编号'/>",
			          "<s:text name='器具名称'/>",
			          "<s:text name='器具型号'/>",
			          "<s:text name='出厂编号'/>", 
			          "<s:text name='使用部门'/>", 
			          "<s:text name='检定周期'/>", 
			          "<s:text name='器具类别'/>",
			          "<s:text name='计划'/>",
			          "<s:text name='实际'/>",
			          "<s:text name='是否为计划检定'/>", 
			          "<s:text name='MSA状态'/>", 
			          "<s:text name='鉴定员'/>",
			          "<s:text name='附件'/>"], 
			colModel:[
					{name:'gsmEquipment.measurementNo', index:'nspectionPlanmeasurementNo', width:100,editable:true},
					{name:'gsmEquipment.measurementName', index:'measurementName', width:100,editable:true},
					{name:'gsmEquipment.measurementSpecification', index:'measurementSpecification', width:100,editable:true}, 
					{name:'gsmEquipment.factoryNumber', index:'factoryNumber', width:160,editable:true},
					{name:'gsmEquipment.useDept', index:'useDept', width:100,editable:true},
					{name:'gsmEquipment.testCycle', index:'testCycle', width:100,editable:true},
					{name:'gsmEquipment.measurementType', index:'measurementType', width:100,editable:true},
					{name:'inspectionPlanDate', index:'inspectionPlanDate', width:100,editable:true},
					{name:'actualInspectionDate', index:'actualInspectionDate', width:100,editable:true},
					{name:'yesOrNo', index:'yesOrNo', width:180,editable:true,formatter:booleanformatter},
			        {name:'inspectionState', index:'inspectionState', width:160,editable:true},
			        {name:'modifier', index:'modifier', width:100,editable:true},
			        {name:'attachment', index:'attachment', width:100,editable:true}
			        ], 
		               editurl: "${gsmctx}/inspection-chart/save.htm",
					gridComplete: function(){ 
						//alert("aa");
						//contentResize();
					},
					serializeRowData:function(data){
						//不要把id=0传回去，避免后台判断id=0或null
						if(data.id==0){
							data.id="";
						}
						return data;
					}
		});
      	});
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<table style="width:100%;" id="inspectionReportList"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>