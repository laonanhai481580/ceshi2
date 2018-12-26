<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	$(document).ready(function(){
		var params = ${params};	
		var postData = {};
		for(var pro in params){
			postData['params.' + pro] = params[pro];
		}
		jQuery("#list").jqGrid({
			datatype: "json",
// 			jsonReader:{
// 				repeatitems:false
// 			},
			height:520,
// 			prmNames:{
// 				rows:'page.pageSize',
// 				page:'page.pageNo',
// 				sort:'page.orderBy',
// 				order:'page.order'
// 			}, 
			loadtext:"读取中....",
			postData: postData,
			rowNum: 15,
			rownumbers: true,
			multiselect: false,
			autowidth:true,
			shrinkToFit:false,
			url:'${mfgctx}/defective-goods/ledger/amount-detail-datas.htm',
			colNames: ['编号','生产日期','制令号','产品类型','产品型号','零部件名称','零部件代号','质检报告单号','产品阶段','工序','不合格类别','不良描述','不合格来源','处理方式','不良数量','备注'],
	       	colModel: [{name:'formNo', index:'formNo', width:110, editable:false, formatter:click},
            		   {name:'produceDate', index:'produceDate', width:100, editable:false},
            		   {name:'produceNO', index:'produceNO', width:100, editable:false},
                       {name:'productModel', index:'productModel', width:100,editable:false},
                       {name:'modelSpecification', index:'modelSpecification', width:100, editable:false},
                       {name:'partName', index:'partName', width:100, editable:false},
                       {name:'partCode', index:'partCode', width:100, editable:false},
                       {name:'qualityTestingReportNo', index:'qualityTestingReportNo', width:100, editable:true},
                       {name:'productPhase', index:'productPhase', width:100, editable:true},
             		   {name:'workProcedure', index:'workProcedure', width:100, editable:false},
                       {name:'unqualifiedType', index:'unqualifiedType', width:100, editable:false},
                       {name:'unqualifiedDescription', index:'unqualifiedDescription', width:200, editable:false},
                       {name:'unqualifiedSource', index:'unqualifiedSource', width:100, editable:false},
                       {name:'disposeMethod', index:'disposeMethod', width:100, editable:false},
                       {name:'unqualifiedAmount', index:'unqualifiedAmount', width:100, editable:false},
                       {name:'remark', index:'remark', width:200,editable:false}],
// 		    editurl: "${marketctx}/inspection-report/save.htm",
			gridComplete: function(){ 
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
// 	$("#list").jqGrid('setGroupHeaders', {
// 			useColSpanStyle: true, 
// 			groupHeaders:[{startColumnName: firstCol, numberOfColumns: colNumbers, titleText: '不良细项'}]
// 		});
//   	});
// 	function booleanformatter(cellvalue, options, rowObject){
// 		if(cellvalue){
// 			return "是";
// 		}else{
// 			return "否";
// 		}
// 	}
	function click(cellvalue, options, rowObject){	
		return "<a href='#' onclick='javascript:goTo(\""+rowObject.id+"\")'>"+cellvalue+"</a>";
	}
	function goTo(id){
		 if($.isFunction(window.parent.goToNewLocationById1)){
			window.parent.goToNewLocationById1(id);
		} 
	}
</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<table style="width:100%;" id="list"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>