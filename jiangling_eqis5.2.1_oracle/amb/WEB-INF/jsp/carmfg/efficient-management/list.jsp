<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.norteksoft.product.web.struts2.Struts2Utils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%
String selStructureId=Struts2Utils.getParameter("selStructureId");
ActionContext.getContext().put("selStructureId", selStructureId);
%>
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${ctx}/widgets/tablednd/jquery.tablednd.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
<style >
<!--
	checkbox{
		border:0px;
		background:red;
	}
-->
</style>
<script type="text/javascript">
	function contentResize(){
		jQuery("#list1").jqGrid('setGridHeight',$(".ui-layout-center").height()-70);
		jQuery("#list1").jqGrid('setGridWidth',_getTableWidth() - 280);
		$("#checkGradeTypeTree").height($(".ui-layout-center").height()-48);
	}
	$(document).ready(function(){
		//jqGrid
		$("#list1").jqGrid({ 
			rownumbers:true,
			shrinkToFit:true,
			sortorder: "asc",
			sortname: "orderNum",
			colModel:[ 
	           	{name:'id',index:'id',width:50,align:"center",hidden:true}, 
	            {label:'工序',name:'processName',index:'processName',width:300,editable:true,editrules:{required:true},sortable:false,edittype:'select',formatter:'select',editoptions:{${processNameStr}}}, 
	            {label:'工时',name:'hour',index:'hour',width:70,editable:true,editrules:{required:true,number:true,min:0},sortable:false} 
	        ], 
           	multiselect: true,
		   	gridComplete: function(){
		   		$("#list1").tableDnDUpdate();
		   	},
		   	ondblClickRow: editRow,
			editurl: "${mfgctx}/efficient-management/save-processhour.htm",
			serializeRowData:function(data){
				if(data.id==0){
					data.id="";
					data.checkGradeTypeId = selCheckGradeTypeId;
				}
				data.productLineId = selCheckGradeTypeId;
				return data;
			}
		}); 
		$(".btn","#checkGradeToolbar").attr("disabled","disabled");
		contentResize();
	});
	
	var selCheckGradeTypeId = null,selCheckGradeTypeName=null;
	function loadCheckGradeByCheckGradeTypeId(checkGradeTypeId,checkGradeTypeName){
		if(checkGradeTypeId == selCheckGradeTypeId){
			return;
		}
		selCheckGradeTypeId = checkGradeTypeId;
		selCheckGradeTypeName = checkGradeTypeName;
		var params = {
			url : '${mfgctx}/efficient-management/list-processhour-datas.htm',
			postData : {productLineId:selCheckGradeTypeId}
		};
		$("#list1").jqGrid("setGridParam",params).trigger("reloadGrid");
		makeEditable(true);
		$("#checkGradeToolbar .btn").attr("disabled","");
	}
	var editParams={
		keys : true,
		oneditfunc : function(rowId){
		},
		successfunc: function( response ) {
			var jsonData = eval("(" + response.responseText + ")");
			if(jsonData.error){
				alert(jsonData.message);
				return false;
			}else{
				return true;
			}
	    },
		aftersavefunc : function(rowId, data) {
			var json = eval("(" + data.responseText + ")");
			var obj = json.obj;
			if(rowId == 0){
				$("#list1").jqGrid("setRowData",rowId,{id:obj.id,orderNum:obj.orderNum});
				$("#" + rowId,"#list1").attr("id",obj.id);//修改这一行的编号
				$("#"+$.jgrid.jqID(obj.id), "#list1").removeClass("jqgrid-new-row");//修改新建的标志
				$("#jqg_list1_" + rowId).attr("id","jqg_list1_" + obj.id);//修改Checkbox的编号
				var selarrrow = $("#list1")[0].p.selarrrow;//更新多选的
				for(var i=0;i<selarrrow.length;i++){
					if(selarrrow[i]==0){
						selarrrow.splice(i,1,obj.id);
						break;
					}
				}
			}
			makeEditable(true);
			editNextRow(obj.id);
		},
		afterrestorefunc : function(rowId) {
			if(rowId==0){
				$("#list1").jqGrid("delRowData",0);
				var selarrrow = $("#list1")[0].p.selarrrow;//更新多选的
				for(var i=0;i<selarrrow.length;i++){
					if(selarrrow[i]==0){
						selarrrow.splice(i,1);
						break;
					}
				}
			}
			makeEditable(true);
		},
		restoreAfterError:false
	};

	var lastsel = null,editing=false;
	function makeEditable(editable) {
		if (editable) {
			editing = false;
			lastsel=null;
		} else {
			editing = true;
		}

	}
	/**
	 * 新建一行
	 * @param byEnter
	 * @return
	 */
	function addRow(byEnter) {
		if(!selCheckGradeTypeId){
			alert("请先选择项目!");
			return;
		}
		if (byEnter == undefined) {
			byEnter = false;
		}
		var editableGrid = $("#list1");
		if(editing){
			if(lastsel != null){
				editableGrid.jqGrid("restoreRow", lastsel,editParams.afterrestorefunc);
			}else{
				alert("请先完成编辑！");
				return;
			}
		}
		editableGrid.jqGrid('addRowData',0,{
			checkGradeTypeId:selCheckGradeTypeId,
			checkGradeTypeName : selCheckGradeTypeName
		});
		editRow(0);
	}

	/**
	 * 编辑行
	 * @param rowId
	 * @return
	 */
	function editRow(rowId,iRow,iCol,e) {
		var editableGrid = $("#list1");
		if(rowId==lastsel){
//			双击当前正在编辑的行，不用做任何特殊处理
			return;
		}else{
			editableGrid.jqGrid("restoreRow", lastsel,editParams.afterrestorefunc);
		}
		editableGrid.resetSelection();
		editableGrid.setSelection(rowId);
		lastsel = rowId;
		editableGrid.jqGrid("editRow", rowId, editParams);
		makeEditable(false);
	}
	/**
	 * 编辑下一行
	 * @param rowId
	 * @return
	 */
	function editNextRow(rowId) {
		var editableGrid = $("#list1");
		var ids = editableGrid.jqGrid("getDataIDs");
		var index = editableGrid.jqGrid("getInd", rowId);
		index++;
		if (index > ids.length) {// 当前编辑行是最后一行
			addRow(true);
		} else {
			editRow(ids[index - 1]);
		}
		
	}
	
	/**
	 * 删除
	 * @param rowId
	 * @return
	 */
	function delRow() {
		var editableGrid = $("#list1");
		if (editing) {
			if(lastsel != null){
				editableGrid.jqGrid("restoreRow",lastsel,editParams.afterrestorefunc);
			}else{
				alert("请先完成编辑！");
				return;
			}
		}
		var ids = editableGrid.getGridParam('selarrrow');
		if (ids.length < 1) {
			alert("请选中需要删除的记录！");
			return;
		}
		if(confirm("确定要删除吗?")){
			var params={
				deleteIds : ids.join(',')
			};
			var url = "${mfgctx}/efficient-management/delete-processhour.htm";
			$.post(url, params, function(data) {
				if(data.error){
					alert(data.message);
				}else{
					if (ids.length>0) {
						editableGrid.jqGrid().trigger("reloadGrid"); 
					}
				}
			},'json');
		}
	}

</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="efficientManagement";
		var thirdMenu="bom";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-efficient-management-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
			<div class="opt-body">
				<table style="width:100%;height:100%;" cellpadding="0" cellspacing="0">
					<tr>
						<td valign="top">
							<div class="opt-body" id="checkGradeTypeTree" style="padding:6px;width:270px;overflow:auto;">
							</div>
						</td>
						<td style="width:6px;border-left:1px solid #99bbe8;border-right:1px solid #99bbe8;">
							&nbsp;
						</td>
						<td valign="top">
							<div id="checkGradeToolbar" class="opt-btn" style="line-height:33px;">
								<button class='btn' type="button" onclick="addRow()"><span><span>新建</span></span></button>
								<button class='btn' type="button" onclick="delRow()"><span><span>删除</span></span></button>
							</div>
							<div class="opt-body" style="padding:6px;border:0px;">
								<form>
								<table id="list1"></table>
								</form>
							</div>
						</td>
					</tr>
				</table>
			</div>
		</div>
</body>
</html>