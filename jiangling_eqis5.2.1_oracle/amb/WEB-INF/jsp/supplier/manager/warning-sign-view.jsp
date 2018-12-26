<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	//格式化
	function operateFormater(cellValue,options,rowObj){
		if(rowObj.degree=='C'){
			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
		}else if(rowObj.degree=='D'){
			return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
		}else{
			return '';
		}
	}
	function contentResize(){
		$("#suppliers").jqGrid("setGridHeight",$(document).height()-100);
	}
	var topMenu='';
	jQuery.extend($.jgrid.defaults,{
		prmNames:{
			rows:'signPage.pageSize',
			page:'signPage.pageNo',
			sort:'signPage.orderBy',
			order:'signPage.order'
		}
	});
	 
	function $addGridOption(jqGridOption){
		var params = ${params};	
		jqGridOption.postData.year=params.evaluateYear;
		jqGridOption.postData.month=params.evaluateMonth;
		jqGridOption.postData.importance=params.importance;
		for(var pro in params){
			jqGridOption.postData['params.' + pro] = params[pro];
		};
	}
	
	//导出
	function exportSuppliers(){
		var state = '';
		var evaluateYear = '${evaluateYear}';
		var evaluateMonth = '${evaluateMonth}';
		var importance = '${importance}';
		iMatrix.export_Data("${supplierctx}/manager/exports-supplier-detail.htm?state="
				+state+"&params.evaluateYear="+evaluateYear+"&params.importance="
				+importance+"&params.evaluateMonth="+evaluateMonth+"&_list_code=SUPPLIER_MONTH_CARTYPE_REPORT");
	}
</script>

</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_warning_sign";
	</script>


	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<button class="btn" onclick="exportSuppliers();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="suppliers"
					  url="${supplierctx}/report/monthCartypeReport-red-yellow-datas.htm" submitForm="defaultForm" code="SUPPLIER_MONTH_CARTYPE_REPORT" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>