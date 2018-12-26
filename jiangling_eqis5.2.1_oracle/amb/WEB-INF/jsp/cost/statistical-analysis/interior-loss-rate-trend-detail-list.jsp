<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		$(document).ready(function(){
			var colNames = $("#colNames").val().split(',');
			var colModelValue = $("#colModel").val();
			var colCodes = $("#colCode").val().split(',');
			var firstCol = colCodes[0];
			var colNumbers = colCodes.length-1;
			//转换为对象数组
			//alert(colModelValue);
			//alert(('['+colModelValue+']'));
			var colModel=eval('['+colModelValue+']');
			var params = ${params};	
			var postData = {};
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
			jQuery("#interiorLossRateTrendDetailList").jqGrid({
				datatype: "json",
				jsonReader:{
					repeatitems:false
				},
				height : $(window).height()-$("#titleDiv").height()-90,
				width : $(window).width()-10,				
				prmNames:{
					rows:'page.pageSize',
					page:'page.pageNo',
					sort:'page.orderBy',
					order:'page.order'
				}, 
				postData : postData,
				rrowNum: 15,
				rownumbers: true,
				autowidth:true,
				shrinkToFit:false,
				url:'${costctx}/statistical-analysis/interior-loss-rate-trend-detail-list-datas.htm',
                colNames: colNames,
		        colModel: colModel,
//                 editurl: "${cargoctx}/inforshow/save.htm",
				gridComplete: function(){},
				serializeRowData:function(data){
					//不要把id=0传回去，避免后台判断id=0或null
					if(data.id==0){
						data.id="";
					}
					return data;
				}
			});
			$("#interiorLossRateTrendDetailList").jqGrid('setGroupHeaders', {
				useColSpanStyle: true, 
				groupHeaders:[{startColumnName: firstCol, numberOfColumns: colNumbers, titleText: '不良细项'}]
			});
       	});
// 		function click(cellvalue, options, rowObject){	
// 			return "<a href='#' onclick='javascript:goTo(\""+rowObject.id+"\")'>"+cellvalue+"</a>";
// 		}
		function goTo(id){
			 if($.isFunction(window.parent.goToNewLocationById)){
				window.parent.goToNewLocationById(id);
			} 
		}
		function click(cellvalue, options, rowObject){
			if(rowObject.formType=="领料单")
			{
		 	return cellvalue;
		   }
           if(rowObject.formType=="不合格处理"){
				return "<a href='${mfgctx}/defective-goods/ledger/view-info.htm?id="+rowObject.formId+"'>"+cellvalue+"</a>";
			}else{
				return cellvalue;
			}
			
		}
		
		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="colNames" value="${colNames}"/>
<input type="hidden" id="colModel" value="${colModel}"/>
<input  type="hidden" id="colCode"  value="${colCode}"/>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<table style="width:100%;" id="interiorLossRateTrendDetailList"></table>
				<div id="pager"></div>
			</div>
		</div>
	</div>
</body>
</html>