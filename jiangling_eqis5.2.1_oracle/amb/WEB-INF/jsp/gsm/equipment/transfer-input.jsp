<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String ids=(String)ActionContext.getContext().get("ids");
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	var gridHeight = 0,gridWidth=0;
	var editing = false;
	var rowId,originalIndex,newIndex;
	var lastSelection;
	$(document).ready(function(){
		$('#datepicker1','#measurementForm').datepicker({changeMonth:true,changeYear:true});
		$("#measurementForm").validate({
			rules: {
				useDept:{required: true},
				borrower:{required: true},
				borrowDate:{required: true}
			}
		});
		gridHeight = $(document).height() - 300;
		gridWidth = $("#btmDiv").width() - 10;
		$("#gsm_table").jqGrid({
			url : '${gsmctx}/equipment/transfer-input-datas.htm?ids=<%=ids%>',
			height : gridHeight,
			width : gridWidth,
			prmNames:{
				rows:'page.pageSize',
				page:'page.pageNo',
				sort:'page.orderBy',
				order:'page.order'
			},
			datatype: "json",
			rowNum: 10,
			rownumbers:true,
			gridEdit: true,
			pager : '#pager',
			colNames:['',"仪器管理编号","仪器名称","规格/型号","责任人","目标责任人","转移时间"], 
			colModel:[{name:'id',index:'id',width:1,hidden:true,editable:false},
					  {name:'managerAssets',index:'managerAssets',align:'center',width:120,editable:false},
					  {name:'equipmentName',index:'equipmentName',align:'center',width:100,editable:false},
					  {name:'equipmentModel',index:'equipmentModel',align:'center',width:100,editable:false},
					  {name:'dutyMan',index:'dutyMan',align:'center',width:100,editable:false},
					  {name:'goalDutyMan',index:'goalDutyMan',align:'center',width:100,editable:false},
					  {name:'transferTime',index:'transferTime',align:'center',width:100,editable:false},
					  ], 
			multiselect : true,
		   	autowidth: true,
			viewrecords: true, 
			sortorder: "desc",
			ondblClickRow: function(rowId){
				editRow(rowId);
			},
			gridComplete : function(){}
		});
	});
	function makeEditable(editable){
		if(editable){
			editing = false;
			jQuery("#gsm_table tbody").sortable('enable');
		}else{
			editing = true;
			jQuery("#gsm_table tbody").sortable('disable');
		}			
	}
	function editRow(rowId){
		jQuery("#gsm_table").jqGrid("editRow",rowId,{
			keys:true,
			aftersavefunc : function(rowId, data) {
				afterSaveRow(rowId,data);
			},
			successfunc: function( response ) {
				var result = eval("(" + response.responseText	+ ")");
				if(result.error){
					alert(result.message);
					return false;
				}else{
					return true;
				}
		    },
			afterrestorefunc :function(rowId){
				makeEditable(true);
			},
			url : 'clientArray'
		});
		makeEditable(false);
	}	
	function afterSaveRow(rowId,data){
		//必须加括号才能转换为对象
		var jsonData = eval("(" + data.responseText	+ ")");
		editNextRow(jsonData.id);
	}
	function editNextRow(rowId){
		var ids = jQuery("#gsm_table").jqGrid("getDataIDs");
		var index = jQuery("#gsm_table").jqGrid("getInd",rowId);
		index++;
		editRow(ids[index-1]);
	}
	//获取表单的值
	function getParams(){
		var params = {};
		$("#opt-content form input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
	//选择人员
	function selectStaff(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='领用人'/>",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.id,
			showInputId:obj.id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	function submitForm(url){
		var rows = jQuery("#gsm_table").getGridParam('selarrrow');
		if(rows.length<1){
			alert("请选择要确认的计量器具！");
			return;
		}
		var auditTransferMan=$("#auditTransferMan").val();
		var auditTransferTime=$("#auditTransferTime").val();
		var remark=$("#transferRemark").val();
		var paras = new Array();
		for(var i=0;i<rows.length;i++){
		    var row=rows[i];
		    var rowData = $("#gsm_table").getRowData(row); 
		    paras.push('{"id":"'+rowData.id+'","auditTransferMan":"'+auditTransferMan+'","transferRemark":"'+remark+'","auditTransferTime":"'+auditTransferTime+'"}');
		}
		var params = '['+ paras.toString() + ']';
		$('#measurementForm').attr('action',url+'?inventoryStr='+params);
		$('#measurementForm').submit();
		$.post(url,{inventoryStr:params},function(result){
			if(result){
				var obj=eval('('+result+')');
				alert(obj.message);
				closeInput();
			}
		});
	}
	var gsmTimeout = null;
	function findGsm(obj){
		if(gsmTimeout != null){
			clearTimeout(gsmTimeout);
		}
		gsmTimeout = setTimeout(function(){
			$("#gsm_table").setGridParam({postData:{"dept":obj.value}});
			$("#gsm_table").trigger("reloadGrid");
			gsmTimeout = null;
		},300);
	}
	function click(cellvalue, options, rowObject){	
		return cellvalue;
	}
	function closeInput(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btmDiv">
<!-- 				<button class='btn' onclick="addGsm();"><span><span><b class="btn-icons btn-icons-find"></b>选择器具</span></span></button> -->
				<button class='btn' onclick="submitForm('${gsmctx}/equipment/save-transfer-input.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="closeInput();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>				
			</div>
			<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			<div style="margin-left:5px;overflow-y:auto;"  id="opt-content">
				<form action="" method="post" id="measurementForm" name="measurementForm" >
					<fieldSet  style="padding:6px;">
						<legend style="font-size: 18px;"><b>&nbsp;&nbsp;基础信息&nbsp;&nbsp;</b></legend>
						<table class="form-table-border-left" style="width:100%;margin: auto;">
							<tr>
								<td style="width:15%;">确认人</td>
								<td style="width:15%;">
									<input type="hidden" id="stocktakingMan" name="stocktakingMan" value="<%=ContextUtils.getUserName() %>"/>
									<%=ContextUtils.getUserName() %>
								</td>
								<td style="width:15%;">确认时间</td>
								<td style="width:20%;">
									
									<input type="hidden" id="auditTransferTime" name="auditTransferTime" value="<%=dateStr %>"/>
									<%=dateStr %>
								</td>
							</tr>
							<tr>
								<td>备注</td>
								<td colspan="3"><textarea rows="5" name="transferRemark" id="transferRemark"></textarea></td>
							</tr>
						</table>
					</fieldSet>
					<fieldSet  style="padding:6px;">
						<legend style="font-size: 18px;"><b>&nbsp;&nbsp;正在确认器具&nbsp;&nbsp;</b></legend>
						<div id="tabs-1" style="width: 100%;">
							<table id="gsm_table" ></table>
							<div id="pager"></div>
						</div>
					</fieldset>
				</form>
			</div>
		</div>
	</div>
</body>
</html>