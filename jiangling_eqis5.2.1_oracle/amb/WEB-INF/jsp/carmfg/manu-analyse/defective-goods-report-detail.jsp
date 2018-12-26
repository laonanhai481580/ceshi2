<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function changePosition(){}
		var params = ${params};
		$(document).ready(function(){
			createDetailGrid();
		});
		function $getExtraParams(){
			return {searchStrs:'${params}'};
		}
		function contentResize(){
			$("#defectiveGoods").jqGrid("setGridHeight",$(window).height()-74);
			$("#defectiveGoods").jqGrid("setGridWidth",$(window).width()-25);
		}
		function createDetailGrid(){
			var postData = {};
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
			$("#detailTable").jqGrid({
				datatype: "json",
				url : '${mfgctx}/manu-analyse/defective-goods-report-detail-datas.htm',
				height : $(window).height()-$("#titleDiv").height()-90,
				width : $(window).width()-10,
				postData : postData,
				pager : '#pager',
				prmNames:{
	 				rows:'defectivePage.pageSize',
	 				page:'defectivePage.pageNo',
	 				sort:'defectivePage.orderBy',
	 				order:'defectivePage.order'
	 			}, 
				rownumbers : true,
				colNames: ['','编号','质检报告单号','不合格品类别','缺陷分类','所属产业','不合格来源'],
		       	colModel: [{name:'id', index:'id', width:1, editable:false, hidden:true},
		       	           {name:'formNo', index:'formNo', width:200, editable:false, formatter:click},
	                       {name:'qualityTestingReportNo', index:'qualityTestingReportNo', width:100, editable:true},
	                       {name:'defectType', index:'defectType', width:100, editable:true},
	                       {name:'defectLevel', index:'defectLevel', width:100, editable:true},
	                       {name:'industryType', index:'industryType', width:100, editable:true},
	                       {name:'unqualifiedSource', index:'unqualifiedSource', width:100, editable:false, formatter:nameFactory}],
			    multiselect: false,
			   	autowidth: true,
			   	forceFit : true,
			   	shrinkToFit: false,
				viewrecords: true, 
				sortorder: "desc",
				gridComplete : function(){}
			});
		}
		function click(cellvalue, options, rowObject){	
			return "<a href='#' onclick='javascript:goTo(\""+rowObject.id+"\")'>"+cellvalue+"</a>";
		}
		function nameFactory(cellvalue, options, rowObject){
			if(cellvalue == 'FQC'){
				return '制程';
			}else if (cellvalue == 'IQC'){
				return '进货检验';
			}
		}
		function goTo(id){
			if($.isFunction(window.parent.goToNewLocationById)){
				window.parent.goToNewLocationById(id);
			} 
		}
		function inputLinkClick(cellvalue, options, rowObject){
			return "<a href='javascript:void(0);goToViewInfo(\""+cellvalue+"\")'>"+cellvalue+"</a>";
		}
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
		function goToViewInfo(formNo){
			window.location.href = '${mfgctx}/defective-goods/ledger/view-info.htm?formNo=' + formNo;
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div style="margin:6px auto;text-align:center;font-size:16px;font-weight:bold;" id='titleDiv'>
			</div>
			<div id="opt-content">
				<grid:jqGrid gridId="defectiveGoods" url="${mfgctx}/manu-analyse/defective-goods-report-detail-datas.htm" code="MFG_DEFECTIVE_GOODS" dynamicColumn="${dynamicColumn}" pageName="defectivePage"></grid:jqGrid>
			</div>
		</div>
	</div>
</body>
</html>