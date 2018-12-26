<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
		
		function selectInspectionPoint(obj){
			if (editing) {
				alert("请先完成编辑！");
				return;
			}
			var workshop = $("#workshop").val();
			var url = encodeURI('${mfgctx}/inspection/inprocess-inspection/list.htm?workshop='+workshop);
			aa.submit('contentForm', url,'main'); 
			
		}
		
		//重写(单行保存前处理行数据)
		function $processRowData(data){
			data.inspectionPoint = $("#inspectionPoint").val();
			return data;
		}
		
		//重写(给单元格绑定事件)
		var myId = null;
		function $oneditfunc(rowid){
			myId = rowid;
			jQuery('#'+rowid+'_inspectionAmount','#inprocessInspectionList').change(function(){
				caclute(rowid);
			});
			jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').change(function(){
				caclute(rowid);
			});
		
		}
		
		var modelSpecificationId = null;
		function modelSpecificationClick(obj){
			modelSpecificationId = obj.currentInputId;
			$.colorbox({href:"${mfgctx}/common/product-bom-model-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择产品型号"
			});
		}
		
		//设置产品型号的值
		function setBomModelValue(data){
			$("#" + modelSpecificationId).val(data[0].value);
			//自动生成产品类型和系列
			setModelAndSeries(data[0].value);
		}
		
		//根据产品型号设置产品类型和产品系列
		function setModelAndSeries(value){
			$.post('${mfgctx}/inspection/daliy-report/model-series.htm',{model:value},function(data){
				if(data.error){
					alert(data.message);
				}else{
					jQuery('#'+myId+'_productModel','#inprocessInspectionList').val(data.type);
					jQuery('#'+myId+'_productSeries','#inprocessInspectionList').val(data.series);
				}
			},'json');
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
		
		//重写调整高度
		function contentResize(){
			var tableHeight=$('.ui-layout-center').height()-210;
			var tableWidth=_getTableWidth();
			jQuery("#inprocessInspectionList").jqGrid('setGridHeight',tableHeight);
			jQuery("#inprocessInspectionList").jqGrid('setGridWidth',tableWidth);
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
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_acquisition';
		var thirdMenu="checkRecord";
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
			<aa:zone name="main">
			<s:if test="hasRight=='yes'">
				<div class="opt-btn">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				<button class='btn' onclick="delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</div>
			</s:if>
			<s:else>
				<div class="opt-btn"><span><span style="font-size: 14px;line-height:35PX">请确认${workshop}检查点检验台帐和检验权限是否已正确配置，如有问题请联系系统管理员！</span></span></div>
			</s:else>
					<div id="opt-content">
					
					<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
					<form id="contentForm" name="contentForm" method="post"  action="">
							<table class="form-table-without-border" style="width: 100%;">
							<caption style="text-align:center;height: 35px"><h2>${inspectionPoint}记录表</h2></caption>
								<tr>
									<td style="width:8%">采集点：</td>
									<td style="width:17%">
									
									<s:select list="inspectionPointList" 
											  theme="simple"
											  onchange="selectInspectionPoint(this)"
											  listKey="inspectionPointName" 
											  listValue="inspectionPointName" 
											  id="inspectionPoint"
											  name="inspectionPoint"
											  value="inspectionPoint"
											  labelSeparator=""
											  emptyOption="false"
											  cssStyle="width:150px;"></s:select></td>
									<td style="width:8%">检验日期：</td>
									<td style="width:17%"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%></td>
									<td style="width:8%">检验员：</td>
									<td style="width:17%">${name}</td>
									<td style="width:8%">生产线：</td>
									<td style="width:17%">${productionLine}</td>
									
								</tr>
								<tr>
									<td >工&nbsp;&nbsp;厂：</td>
									<td >${factory}</td>
									<td >车&nbsp;&nbsp;间：</td>
									<td >${workshop}</td>
									<td >班&nbsp;&nbsp;别：</td>
									<td >${workGroupType}</td>
									<td >工&nbsp;&nbsp;序：</td>
									<td >${workProcedure}</td>
								</tr>
								
							</table>
							<s:if test="listCode!=null">
							<grid:jqGrid gridId="inprocessInspectionList" url="${mfgctx}/inspection/inprocess-inspection/list-datas.htm" code="${listCode }" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
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
							</s:if>
					 </form>
					</div>
					</aa:zone>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>