  <%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title><s:text name='main.title'/></title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
<script type="text/javascript">
	var businessCode = '<%=request.getParameter("businessCode")%>';
	$(document).ready(function(){
		$.spin.imageBasePath='${ctx}/widgets/spin/img/spin1/';
		$("#number1").spin({
			max:100,
			min:1
		});  
		$("#contentForm").validate({}); 
		//绑定是否启用提前提醒
		$(":input[name=enabled]").click(function(){
			changeAdvanceRemindSwitch();
		});
		changeAdvanceRemindSwitch();
		$(":input[name=businessCode]").val(businessCode);
		createPersonGrid();
	});
	//提前提醒
	function changeAdvanceRemindSwitch(){
		var checked = $(":input[name=enabled]").attr("checked");
		if(checked){
			$("#advanceRemindTable").find(":input").attr("disabled","");
		}else{
			$("#advanceRemindTable").find(":input").attr("disabled","disabled");
		}
	}
	
	function submitForm(url){ 
		if($("#contentForm").valid()){
			var params = {};
			var rows = jQuery("#person_table").jqGrid('getRowData');
			var paras = new Array();
			if(rows.length<1){
				alert("<s:text name='提醒人员不能为空！'/>");
				return;
			}
			for(var i=0;i<rows.length;i++){
			    var row = rows[i];
			    paras.push('{"userName":"'+row.userName+'","userLoginName":"'+row.userLoginName+'"}');
			}
			params.usesStr = "[" + paras.toString() + "]";
			var enabled = $(":input[name=enabled]").attr("checked");
			params.enabled=enabled; 
			if(enabled){
				params.days = $(":input[name=days]").val();
			}
			params.businessCode = businessCode;
			
			$(".opt-btn .btn").attr("disabled","disabled");
			$("#message").html("<s:text name='正在保存配置,请稍候... ...'/>");
			$.post(url,params,function(result){
				$(".opt-btn .btn").attr("disabled","");
				$("#message").html(result.message); 
			},'json');
		}
	}
	
	//选择提醒人员
	function addPerson(){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='选择提醒人员'/>",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'loginName',
			leafPage:'false',
			multiple:'true',
			hiddenInputId:'personId',
			showInputId:'personName',
			acsSystemUrl:acsSystemUrl,
			callBack:function(){
				setPerson();
			}
		});
	}
	
	function setPerson(){
		var personIds = $("#personId").val().split(",");
		var personName = $("#personName").val().split(",");
		var rows = jQuery("#person_table").jqGrid('getRowData'); 
		var mydata = [];
		var isExit;
		for(var i=0;i<personIds.length;i++){
			for(var y=0;y<rows.length;y++){
				if(personIds[i]==rows[y].userLoginName){
					isExit = true;
					break;
				}
			}
			if(!isExit){
				mydata.push({userName:personName[i],userLoginName:personIds[i],targetId:personIds[i]});
			}
			isExit = false;
		}
		for(var i=0;i<=mydata.length;i++){
			jQuery("#person_table").jqGrid('addRowData',i+1,mydata[i]);
		}
	}
	
	//删除提醒人员
	function delPerson() {
		var rows = jQuery("#person_table").getGridParam('selarrrow');
		if(rows.length==0){
			alert("<s:text name='选择提'/>");
			return;
		}
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#person_table").jqGrid("delRowData",rows[i]);
		}
	}
	
	function createPersonGrid(){
		$("#person_table").jqGrid({
			url : '${gsmctx}/inspectionplan/warn-user-datas.htm',
			postData : {businessCode:businessCode},
			height : 240,
			width : 490,
			datatype : "json",
			rownumbers : true,
			colNames : ${personGridColumnInfo.colNames},
			colModel : ${personGridColumnInfo.colModel},
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<aa:zone name = "main">
				<div class="opt-btn">
					<button class='btn' onclick="submitForm('${gsmctx}/equipment/save-mail-settings.htm');"><span><span><b class="btn-icons btn-icons-save"></b><s:text name="保存"/></span></span></button>
					<button  class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b><s:text name="取消"/></span></span></button>
					<span id="message" style="color:red;"></span>
				</div>
				<div style="margin-top:15px; margin-left: 10px;margin-right:10px">
					<form id="contentForm" name="contentForm" method="post" >
						<input type="hidden" id="businessCode" name="businessCode" />
						<fieldSet>
							<legend>&nbsp;&nbsp;<s:checkbox name="enabled" id="enabled" value="%{enabled}"></s:checkbox><label for="enabled"><s:text name="启用发送邮件提醒"/>&nbsp;&nbsp;<label/></legend>
							<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="advanceRemindTable">
					    		<tr>
					    			<td>&nbsp;&nbsp;&nbsp;<s:text name="提前"/><input type="text"  style="width:60px;" id="days" name="days" value="${days}" class="{required:true,number:true,min:0,messages:{number:请填写有效数字,min:最小为0}}"/>&nbsp;<s:text name="天提醒。"/></td>
					    		</tr>
					    		<tr>
					    			<td>
					    				&nbsp;&nbsp;&nbsp;<s:text name="数字类型，小于一天用小数表示，如0.5表示提前半天提醒 。"/>
					    			</td>
					    		</tr>
					    	</table>
					    	<br/>
						</fieldSet>	
						<fieldSet style="margin-top:6px;">
							<legend>&nbsp;&nbsp;<label for="setPerson"><s:text name="设置提醒人员"/>&nbsp;&nbsp;<label/></legend>
								<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="delayRemindTable">
					    		<tr>
									<td style="padding-bottom: 4px">
										<a class="small-button-bg" style="vertical-align: middle;" href="javascript:addPerson();" title="<s:text name='添加提醒人员'/>"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
										<a class="small-button-bg" style="vertical-align: middle;" href="javascript:delPerson();" title="<s:text name='删除提醒人员'/>"><span class="ui-icon ui-icon-minusthick" style='cursor:pointer;'></span></a>
									</td>
								</tr>
								<tr>
									<td >
										<input id="personId" name="personId" type="hidden"/>
										<input id="personName" name="personName" type="hidden"/>
										<table id="person_table"></table>
									</td>
								</tr>
					    	</table>
						</fieldSet>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>