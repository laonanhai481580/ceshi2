<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
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
			url : '${gsmctx}/equipment/return-datas.htm',
			height : gridHeight,
			width : gridWidth,
			prmNames:{
				rows:'gsmPage.pageSize',
				page:'gsmPage.pageNo',
				sort:'gsmPage.orderBy',
				order:'gsmPage.order'
			},
			datatype: "json",
			rowNum: 10,
			rownumbers:true,
			gridEdit: true,
			colNames:['',
			          "<s:text name='器具编号'/>",
			          "<s:text name='器具名称'/>",
			          "<s:text name='型号/规格'/>",
			          "<s:text name='量具状态'/>"], 
			colModel:[{name:'id',index:'id',width:1,hidden:true,editable:false},
			          {name:'measurementNo',index:'measurementNo',align:'left',width:200,editable:false}, 
					  {name:'measurementName',index:'measurementName',align:'left',width:200,editable:false},
					  {name:'measurementSpecification',index:'measurementSpecification',align:'left',width:200,editable:false},
					  {name:'measurementState',index:'measurementState',align:'left',width:200,editable:false,edittype:'select',formatter:'select',editoptions:{value:{'':'请选择','在库':'在库','在用':'在用','封存':'封存','报废':'报废','维修':'维修'}}}
					  ], 
			multiselect : true,
		   	autowidth: false,
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
		if($("#useDept").val()==''){
			alert("<s:text name='归还部门必填！'/>");
			return;
		}
		
		if($("#borrower").val()==''){
			alert("<s:text name='归还人必填！'/>");
			return;
		}
		
		if($("#datepicker1").val()==''){
			alert("<s:text name='归还日期必填！'/>");
			return;
		}
		
		var borrowDate=new Date($("#datepicker1").val());
		if(Date.parse(borrowDate)-Date.parse("<%=dateStr%>")<0){
			alert("<s:text name='归还日期不能早于当前日期！'/>");
			return;
		}
		
		var rows = jQuery("#gsm_table").getGridParam('selarrrow');
		if(rows.length<1){
			alert("<s:text name='请选择要归还的计量器具！'/>");
			return;
		}
		var paras = new Array();
		for(var i=0;i<rows.length;i++){
		    var row=rows[i];
		    var rowData = $("#gsm_table").getRowData(row); 
		    paras.push('{"id":"'+rowData.id+'","measurementNo":"'+rowData.measurementNo+'","measurementName":"'+rowData.measurementName+'","measurementSpecification":"'+rowData.measurementSpecification+'","measurementState":"'+rowData.measurementState+'"}');
		}
		var params = '['+ paras.toString() + ']';
		$('#measurementForm').attr('action',url+'?params='+params);
		$('#measurementForm').submit();
		closeInput();
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
				<button class='btn' onclick="submitForm('${gsmctx}/equipment/update-record.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="closeInput();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>				
			</div>
			<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm" name="measurementForm" >
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="height: 50px"><h2><s:text name="归还登记"/></h2></caption>
						<tr>
							<td><span style="color:red">*</span><s:text name="归还部门"/></td>
							<td><s:select theme="simple" id="useDept" onchange="findGsm(this);"
										  name="useDept" 
										  list="useDepts"  
										  listValue="value" 
										  listKey="name" 
										  emptyOption="true" 
										  cssStyle="width:195px;margin: auto;">
								</s:select>
							</td>
							<td><span style="color:red">*</span><s:text name="归还人"/></td>
							<td>
								<input name="borrower" style="width:195px;margin: auto;" readonly="readonly" id="borrower" onclick="selectStaff(this);"/>
							</td>
							<td><span style="color:red">*</span><s:text name="归还日期"/></td>
							<td>
								<input name="borrowDate" id="datepicker1" readonly="readonly" class="line" style="width:195px;margin: auto;" value="<%=dateStr%>"></input>
							</td>
						</tr>
						<tr>
							<td><s:text name="备注"/></td>
							<td colspan="5"><textarea rows="5" name="remark"></textarea></td>
						</tr>
					</table>
					<div id="tabs">
						<span><font style="text-align:left;"><h3><s:text name="归还器具"/></h3></font></span>
						<div id="tabs-1">
							<table id="gsm_table"></table>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>
</body>
</html>