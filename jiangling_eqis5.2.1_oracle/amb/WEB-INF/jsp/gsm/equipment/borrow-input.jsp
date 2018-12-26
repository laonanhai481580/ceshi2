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
	var rowId;
	$(document).ready(function(){
		$('#datepicker1','#measurementForm').datepicker({changeMonth:true,changeYear:true});
		gridHeight = $(document).height() - 300;
		gridWidth = $("#btmDiv").width() - 10;
		$("#gsm_table").jqGrid({
			height : gridHeight,
			width : gridWidth,
			datatype: "json",
			rowNum: 10,
			rownumbers:true,
			gridEdit: true,
			colNames:['',
			          "器具编号",
			          "器具名称",,
			          "型号/规格"], 
			colModel:[{name:'id',index:'id',width:1,hidden:true,editable:false},
			          {name:'measurementNo',index:'measurementNo',align:'left',width:200,editable:false}, 
					  {name:'measurementName',index:'measurementName',align:'left',width:200,editable:false},
					  {name:'measurementSpecification',index:'measurementSpecification',align:'left',width:200,editable:false}
					  ], 
			multiselect : false,
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
			}
		});
		makeEditable(false);
	}

	function addGsm(){
		$.colorbox({
			href:'${gsmctx}/common/gsm-bom-select.htm',
			iframe:true, 
			innerWidth:700, 
			innerHeight:500,
			overlayClose:false,
			title:"计量器具"
		});
	}
	function setGsmInput(data){
		var rows = jQuery("#gsm_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit = false;
		
		for(var i=0;i<data.length;i++){
			for(var y=0;y<rows.length;y++){
				if(data[i].id==rows[y].id){
					isExit = true;
					break;
				}
			}
			if(!isExit){
				mydata.push({id:data[i].id,measurementNo:data[i].measurementNo,measurementName:data[i].measurementName,measurementSpecification:data[i].measurementSpecification,returnDate:''});
			}
			isExit = false;
		}
		
		for(var i=0;i<=mydata.length;i++){
			jQuery("#gsm_table").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
	function submitForm(url){
		if($("#useDept").val()==''){
			alert("领用部门必填！");
			return;
		}
		
		if($("#borrower").val()==''){
			alert("领用人必填！");
			return;
		}
		
		if($("#datepicker1").val()==''){
			alert("领用日期必填！");
			return;
		}
		
		var borrowDate=new Date($("#datepicker1").val());
		if(Date.parse(borrowDate)-Date.parse("<%=dateStr%>")<0){
			alert("领用日期不能早于当前日期！");
			return;
		}
		
		var rows = jQuery("#gsm_table").jqGrid('getRowData');
		if(rows.length<1){
			alert("请选择要领用的计量器具！");
			return;
		}
		var paras = new Array();
		for(var i=0;i<rows.length;i++){
		    var row=rows[i];
		    paras.push('{"id":"'+row.id+'","measurementNo":"'+row.measurementNo+'","measurementName":"'+row.measurementName+'","measurementSpecification":"'+row.measurementSpecification+'"}');
		}
		var params = '['+ paras.toString() + ']';
		$('#measurementForm').attr('action',url+'?params='+params);
		$('#measurementForm').submit();
		closeInput();
	}
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#opt-content form select").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
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
		popTree({ title :"领用人",
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
	function closeInput(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btmDiv">
				<button class='btn' onclick="addGsm();"><span><span><b class="btn-icons btn-icons-find"></b>选择器具</span></span></button>
				<button class='btn' onclick="submitForm('${gsmctx}/equipment/save-record.htm')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button class='btn' onclick="closeInput();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>				
			</div>
			<div style="display:block;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm">
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="height: 50px;font-size: 24px;font-weight: bold;">借用登记</caption>
						<tr>
							<td><span style="color:red">*</span>借用部门</td>
							<td><s:select theme="simple" id="useDept"
										  name="useDept" 
										  list="useDepts"  
										  listValue="value" 
										  listKey="name" 
										  emptyOption="true" 
										  cssStyle="width:195px;margin: auto;">
								</s:select>
							</td>
							<td><span style="color:red">*</span>借用人</td>
							<td>
								<input name="borrower" style="width:195px;margin: auto;" readonly="readonly" id="borrower" onclick="selectStaff(this);"/>
							</td>
							<td><span style="color:red">*</span>借用日期</td>
							<td>
								<input name="borrowDate" id="datepicker1" readonly="readonly" class="line" style="width:195px;margin: auto;" value="<%=dateStr%>"></input>
							</td>
						</tr>
						<tr>
							<td>备注</td>
							<td colspan="5"><textarea rows="5" name="remark"></textarea></td>
						</tr>
					</table>
					<div id="tabs">
						<span style="font-size: 16px;">借用器具</span>
						<div id="tabs-1">
							<table id="gsm_table"></table>
						</div>
					</div>
					<script type="text/javascript">
					function $oneditfunc(rowId){
						jQuery('#'+rowId+'_returnDate','#gsm_table').attr("readonly","readonly");
						jQuery('#'+rowId+'_returnDate','#gsm_table').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
						enterKeyToNext("gsm_table",rowId,function(){},true);
					}
					</script>
				</form>
			</div>
		</div>
	</div>
	
</body>
</html>