<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#produceDate").datepicker();
		});
		
		//重写(单行保存前处理行数据)
		function $processRowData(data){
			data.inspectionPoint = $("#inspectionPoint").val();
			data.reportId=$("#reportId").val();
			data.produceDate=$("#produceDate").val();
			return data;
		}
		
		//重写(给单元格绑定事件)
		var myId = null;
		function $oneditfunc(rowid){
			myId = rowid;
			var readOnlys = ['materialName','materialModel','unqualifiedRate'];
			for(var i=0;i<readOnlys.length;i++){
				$("#" + rowid + "_" + readOnlys[i]).attr("readOnly","readOnly")
					.css({
						'background-color':'rgb(251, 236, 136)',
						'border':'0px'
					});
			}
			jQuery('#'+rowid+'_inspectionAmount','#inprocessInspectionList').change(function(){
				caclute(rowid);
			});
			jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').change(function(){
				caclute(rowid);
			});
			jQuery('#'+rowid+'_materialCode','#inprocessInspectionList').click(materialCodeClick);
			/**jQuery('#'+rowid+'_produceTaskCode','#inprocessInspectionList').click(function(){
				selectTask(rowid);
			});*/
			jQuery('#'+rowid+'_name','#inprocessInspectionList').attr("readOnly","readOnly").click(function(){
				selectObj("选择检验员",rowid + "_name",rowid + "_name");
			});
			var inspector = $("#" + rowid + "_name").val();
			if(!inspector){
				$("#" + rowid + "_name").val('<%=ContextUtils.getUserName()%>');
			}
		}
		function materialCodeClick(){
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择成品"
			});
		}
		function setFullBomValue(datas){
			//产品代码
			$('#'+myId+'_materialCode','#inprocessInspectionList').val(datas[0].code);
			//产品名称
			$('#'+myId+'_materialName','#inprocessInspectionList').val(datas[0].name);
			//规格型号
			$('#'+myId+'_materialModel','#inprocessInspectionList').val(datas[0].model);
			
			var $inspectionAmount = $('#'+myId+'_inspectionAmount','#inprocessInspectionList');
			if($inspectionAmount.length>0){
				$inspectionAmount.val(datas[0].amount);
				$inspectionAmount.focus()[0].select();	
			}
		}
		function selectTask(rowid){
			$.colorbox({href:"${mfgctx}/base-info/work-task/select-task.htm",
				iframe:true, 
				innerWidth:800, 
				innerHeight:550,	
				overlayClose:false,	
				title:"选择生产工单"
			});
		}
		
		function setTaskValue(datas){
			var taskNumber = datas[0].taskNumber;
			jQuery('#'+myId+'_produceTaskCode','#inprocessInspectionList').val(taskNumber);
			//产品代码
			jQuery('#'+myId+'_materialCode','#inprocessInspectionList').val(datas[0].productCode);
			//产品名称
			jQuery('#'+myId+'_materialName','#inprocessInspectionList').val(datas[0].productName);
			//规格型号
			jQuery('#'+myId+'_materialModel','#inprocessInspectionList').val(datas[0].productModel);
			//
			var $inspectionAmount = $('#'+myId+'_inspectionAmount','#inprocessInspectionList');
			if($inspectionAmount.length>0){
				$inspectionAmount.val(datas[0].amount);
				$inspectionAmount.focus()[0].select();	
			}
			//计算合格数量
			caclute(myId);
		}
		
		//选择检验人员
	 	function selectObj(title,hiddenInputId,showInputId){
			var acsSystemUrl = "${ctx}";
			popTree({ title : title,
				innerWidth:'400',
				treeType:"MAN_DEPARTMENT_TREE",
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId :hiddenInputId,
				showInputId : showInputId,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
		
		//计划本周生产量
		function caluateWeekPoductionAmount(rowid){
			var val = 0;
			$(":input","#"+rowid).each(function(index,obj){
				if(obj.name.indexOf("A")==0){
					var value = $(obj).val();
					if(!isNaN(value)&&!isNaN(parseInt(value))){
						val += parseInt(value);
					}
				}
			});
			jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').val(val);
			caclute(rowid);
		}
		
		//系统计算合格率和不良值
		function caclute(rowid){
			var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#inprocessInspectionList').val();
			var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#inprocessInspectionList').val();
			var qualifiedAmount = inspectionAmount-unqualifiedAmount;
			var unqualifiedRate = (unqualifiedAmount/inspectionAmount)*100;
			if(unqualifiedRate>100){
				unqualifiedRate = 100;
			}
			jQuery('#'+rowid+'_qualifiedAmount','#inprocessInspectionList').val(qualifiedAmount);
			jQuery('#'+rowid+'_unqualifiedRate','#inprocessInspectionList').val(unqualifiedRate.toFixed(2));
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
				$("#reportId").val(jsonData.reportId);
				$("#reportNO").html(jsonData.reportNO);
				return true;
			}
		}
		
		function $addGridOption(jqGridOption){
			jqGridOption.postData.inspectionPoint=$("#inspectionPoint").val();
			jqGridOption.postData.reportId=$("#reportId").val();
		}
		
		function selectInspectionPoint(obj){
			var produceDate = $("#produceDate").val();
			var inspectionPoint = $("#inspectionPoint").val();
			var url = encodeURI('${mfgctx}/inspection/daliy-report/list.htm?produceDate='+produceDate+'&inspectionPoint='+inspectionPoint);
			window.location.href = url;
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_INSPECTION_DALIY-REPORT_LIST_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="data_acquisition";
		var thirdMenu="daliyPoduceReport";
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
			<s:if test="hasRight=='yes'">
				<div class="opt-btn">
					<table cellpadding="0" cellspacing="0" width="100%">
						<tr>
							<td valign="middle" style="padding: 0px;width:30%; margin: 0px;" id="btnTd">
							<security:authorize ifAnyGranted="MFG_INSPECTION_DALIY-REPORT_LIST_SAVE">
							<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
							</security:authorize>
							<security:authorize ifAnyGranted="MFG_INSPECTION_DALIY-REPORT_LIST_DELETE">
							<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
							</security:authorize>
							</td>
							<td align="right" style="padding-right: 6px;width:70%;" valign="middle">
								<span>&nbsp;&nbsp;采集点：<s:select list="productDaliyPointList" 
											  theme="simple"
											  onchange="selectInspectionPoint(this)"
											  listKey="inspectionPointName" 
											  listValue="inspectionPointName" 
											  id="inspectionPoint"
											  name="inspectionPoint"
											  value="inspectionPoint"
											  labelSeparator=""
											  emptyOption="false"
											  cssStyle="width:160px;"></s:select></span>&nbsp;&nbsp; 
								<span>生产日期：<input type="text" id="produceDate" style="width:100px" readonly="readonly" value="<s:date name="produceDate" format="yyyy-MM-dd"/>" onchange="selectInspectionPoint()"/></span>&nbsp;&nbsp;&nbsp;&nbsp;
							</td>
						</tr>
					</table>
				</div>
			</s:if>
			<s:else>
				<div class="opt-btn"><span><span style="font-size: 14px;line-height:35PX">请确认${workshop}检查点检验台帐和检验权限是否已正确配置，如有问题请联系系统管理员！</span></span></div>
			</s:else>
			<aa:zone name="main">
					<div id="opt-content">
						<input type="hidden" id="inspectionPoint"  value="${inspectionPoint}"/>
						<input type="hidden" id="reportId" value="${reportId}"/>
						<input type="hidden" id="colCode"  name="colCode" value="${colCode}"/>
					<form id="contentForm"  name="contentForm"  method="post"   action="">
							
							<table class="form-table-without-border" style="width: 100%;">
							<caption style="text-align:center;height: 35px"><h2>${inspectionPoint}</h2></caption>
								<tr>
									<td style="width:7%">编号：</td>
									<td style="width:18%;text-align: left" ><span id="reportNO">${reportNO}</span></td>
									<td style="width:7%">事业部：</td>
									<td style="width:18%;text-align: left">${businessUnitName}</td>
									<td style="width:7%">工段：</td>
									<td style="width:18%;text-align: left">${section}</td>
									<td style="width:7%">生产线：</td>
									<td style="width:18%;text-align: left">${productionLine}</td>
								</tr>
							</table>
							 </form>
							<grid:jqGrid gridId="inprocessInspectionList"  url="${mfgctx}/inspection/daliy-report/info-datas.htm" code="MFG_INSPECTION_RECORDS_IMW" pageName="dynamicPage" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
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
					</div>
					</aa:zone>
			</div>
	</div>
	
</body>
</html>