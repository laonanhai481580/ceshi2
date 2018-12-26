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
		var editing=false;
		var rowId,originalIndex,newIndex;
		var lastSelection;
		function makeEditable(editable){
			if(editable){
				editing=false;
				jQuery("#inrpocessCheckList tbody").sortable('enable');
			}else{
				editing=true;
				jQuery("#inrpocessCheckList tbody").sortable('disable');
			}
		}
		function editNextRow(rowId){
			var ids=jQuery("#inrpocessCheckList").jqGrid("getDataIDs");
			var index=jQuery("#inrpocessCheckList").jqGrid("getInd",rowId);
			index++;
			if(index>ids.length){//当前编辑行是最后一行
				addRow(true);
			}else{
				editRow(ids[index-1]);
			}
		}
		function afterSaveRow(rowId,data){
			//必须加括号才能转换为对象
			var jsonData = eval("(" + data.responseText
					+ ")");
			if (rowId == 0) {//新纪录删除了再增加
				jQuery("#inrpocessCheckList").jqGrid(
						'delRowData', rowId);
				jQuery("#inrpocessCheckList").jqGrid(
						'addRowData', jsonData.id,
						jsonData, "last");
			} else {//更新已有记录
				jQuery("#inrpocessCheckList").jqGrid(
						'setRowData', jsonData.id,
						jsonData);
			}
			
			editNextRow(jsonData.id);
		}
		function addRow(byEnter) {
			if(byEnter==undefined){
				byEnter=false;
			}
			if ((!editing&&!byEnter)||byEnter) {
				jQuery("#inrpocessCheckList").jqGrid(
						'addRow',
						{
							rowID : "0",
							position : "last",
							addRowParams : {
								keys:true,
								aftersavefunc : function(rowId, data) {
									afterSaveRow(rowId,data);
								},
								afterrestorefunc :function(rowId){
									makeEditable(true);
								}
							}
						});
				makeEditable(false);
			}

		}
		function editRow(rowId){
			if(rowId&&rowId!=lastSelection){
				jQuery("#inrpocessCheckList").jqGrid("saveRow",lastSelection);
			}
			lastSelection=rowId;
			jQuery("#inrpocessCheckList").jqGrid("editRow",rowId,{
				keys:true,
				aftersavefunc : function(rowId, data) {
					afterSaveRow(rowId,data);
				},
				afterrestorefunc :function(rowId){
					makeEditable(true);
				}
			});
			jQuery("#inrpocessCheckList").focus();
			makeEditable(false);
		}
		function delRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#inrpocessCheckList").getGridParam('selarrrow');
			if(ids.length<1){
				alert("请选中需要删除的记录！");
				return;
			}
			$.post("${mfgctx}/inspection/inprocess-check/delete.htm", {
				deleteIds : ids.join(',')
			}, function(data) {
				//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
				while (ids.length>0) {
					jQuery("#inrpocessCheckList").jqGrid('delRowData', ids[0]);
				}
			});

		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	
	<script type="text/javascript">
		var secMenu="inprocessCheck";
		var thirdMenu="inputRecord";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inprocess-check-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<s:if test="hasRight=='yes'">
				<div class="opt-btn">
					<button  class='btn' onclick="addRow();"><span><span>新建</span></span></button>
					<button  class='btn' onclick="delRow();"><span><span>删除</span></span></button>
					<button  class='btn' ><span><span>导出</span></span></button>
				</div>
				<div id="opt-content">
							<input type="hidden" id="colNames" value="${colNames}"/>
							<input type="hidden" id="colModel" value="${colModel}"/>
							<input type="hidden" id="colCode"  value="${colCode}"/>
							<table class="form-table-without-border" style="width: 100%;">
							<caption style="text-align:center;height: 35px"><h2>制造检查记录表</h2></caption>
								<tr>
									<td style="width:10%">检验日期：</td>
									<td style="width:15%"><%=new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date())%></td>
									<td style="width:10%">检验员：</td>
									<td style="width:15%">${name}</td>
									<td style="width:10%">生产线：</td>
									<td style="width:15%">${productionLine}</td>
									<td style="width:10%">采集点：</td>
									<td style="width:15%">${inspectionPoint}</td>
								</tr>
								<tr>
									<td >工&nbsp;&nbsp;厂：</td>
									<td >${factory}</td>
									<td >车&nbsp;&nbsp;间：</td>
									<td >${workshop}</td>
									<td >班&nbsp;&nbsp;组：</td>
									<td >${workGroup}</td>
									<td >工&nbsp;&nbsp;序：</td>
									<td >${workProcedure}</td>
								</tr>
								
							</table>
							<table id="inrpocessCheckList"></table>
							<div id="pager"></div> 
							<script type="text/javascript">
								$(document).ready(function(){
								    var colNames = $("#colNames").val().split(',');
									var colModelValue = $("#colModel").val();
									var colCodes = $("#colCode").val().split(',');
									var firstCol = colCodes[0];
									var colNumbers = colCodes.length-1;
									//转换为对象数组
									var colModel=eval('['+colModelValue+']'); 
									jQuery("#inrpocessCheckList").jqGrid({
										url:'${mfgctx}/inspection/inprocess-check/list-datas.htm',
										rownumbers:true,
										colNames:colNames,
										colModel:colModel,
										
										editurl: "${mfgctx}/inspection/inprocess-check/save.htm",
										gridComplete: function(){ 
												contentResize();
											},
										ondblClickRow: function(rowId){
												editRow(rowId);
												jQuery("#"+rowId+"_name").focus();
										},
										serializeRowData:function(data){
												//不要把id=0传回去，避免后台判断id=0或null
												if(data.id==0){
													data.id="";
													}
												return data;
										}
									});
									$("#inrpocessCheckList").jqGrid('setGroupHeaders', {
										  useColSpanStyle: true, 
										  groupHeaders:[
											{startColumnName:firstCol , numberOfColumns: colNumbers, titleText: '不良明细'}
										  ]
										});
						       	});
								
								
							</script>
					</div>
					</s:if>
					<s:else>
						<div id="opt-content"><h2>还未配置制程检查对应检查点，如有需要请联系系统管理员！</h2></div>
					</s:else>
			</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>