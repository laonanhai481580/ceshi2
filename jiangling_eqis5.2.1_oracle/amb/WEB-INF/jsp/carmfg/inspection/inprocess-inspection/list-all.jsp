<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	
	//重写(单行保存前处理行数据)
	function $processRowData(data){
		data.inspectionPoint = $("#inspectionPoint").val();
		return data;
	}
	//重写(给单元格绑定事件)
	function $oneditfunc(rowid){
		jQuery('#'+rowid+'_inspectionAmount','#inprocessInspectionList').change(function(){
			caclute(rowid);
		});
		jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').change(function(){
			caclute(rowid);
		});
	
	}
	//系统计算合格率和不良值
	function caclute(rowid){
		var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#inprocessInspectionList').val();
		var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').val();
		var qualifiedAmount = inspectionAmount-unqualifiedAmount;
		var qualifiedRate = (qualifiedAmount/inspectionAmount)*100;
		jQuery('#'+rowid+'_qualifiedAmount','#inprocessInspectionList').val(qualifiedAmount);
		jQuery('#'+rowid+'_qualifiedRate','#inprocessInspectionList').val(qualifiedRate.toFixed(2));
	}
   
	
	//默认当前日期
	function nowDate(cellValue,options,rowObj){
		if(cellValue!=null){
			return cellValue;
		}else{
			var now = new Date();
			y=now.getFullYear();
			m=now.getMonth()+1;
			d=now.getDate();
			m=m <10?"0"+m:m;
			d=d <10?"0"+d:d;
			return  y+"-"+m+"-"+d; 
		}
		
	}
	
	function checkAmount(value,colname){
		
		var curId = $("#inprocessInspectionList").jqGrid('getGridParam','selrow');
		var deliverAmount = $('#'+curId+'_deliverAmount','#inprocessInspectionList').val();
		var unqualifiedAmount = $('#'+curId+'_unqualifiedAmount','#inprocessInspectionList').val();
		if(parseInt(value)>parseInt(deliverAmount)){
			return [false,"数据有误，检验数量不能大于送检数量！"];
		}else if(parseInt(value)<parseInt(unqualifiedAmount)){
			return [false,"数据有误，不良数量不能大于检验数量！"];
		}else{
			return [true,""];
		}
		
	}
	//后台返回错误信息
	function $successfunc(response){
		var jsonData = eval("(" + response.responseText+ ")");
		if(jsonData.error){
			alert(jsonData.message);
		}else{
			return true;
		}
	}
	
	function $addGridOption(jqGridOption){
		jqGridOption.postData.inspectionPoint=$("#inspectionPoint").val();
	}
	function nameClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			//hiddenInputId:obj.rowid+"_buyerLoginName",
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();"  >
	<input type="hidden" id="workshop" value="${workshop}"/>
	<input type="hidden" id="inspectionPoint" value="${inspectionPoint}"/>
	<script type="text/javascript">
		var secMenu=$("#workshop").val();
		var thirdMenu="productCheckAll";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inprocess-inspection-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button  class='btn' onclick="showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
				<div id="opt-content">
						<input type="hidden" id="colCode"  value="${colCode}"/>
						<s:if test="listCode!=null">
							<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="inprocessInspectionList" url="${mfgctx}/inspection/inprocess-inspection/list-inspectionpoint-datas.htm" code="${listCode}" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
							<script type="text/javascript">
								 $(document).ready(function(){
									var colCodes = $("#colCode").val().split(',');
									var firstCol = colCodes[0];
									var colNumbers = colCodes.length-1;
									$("#inprocessInspectionList").jqGrid('setGroupHeaders', {
										  useColSpanStyle: true, 
										  groupHeaders:[
											{startColumnName:firstCol , numberOfColumns: colNumbers, titleText: '不良明细'}
										  ]
										});
									$("#inprocessInspectionList").jqGrid('setGridParam',{gridComplete:contentResize});
								}); 
							</script>
							</form>
						</s:if>
				</div>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>