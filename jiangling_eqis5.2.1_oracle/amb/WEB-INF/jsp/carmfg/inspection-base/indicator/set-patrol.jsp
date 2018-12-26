<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@page import="com.ambition.spc.entity.BsRules"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/widgets/spin/jquery-spin.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	jQuery.validator.addMethod("stringCheck",function(value,element){
		return this.optional(element)||/^\d{2}:\d{2}(,\d{2}:\d{2})*$/.test(value);
	});
	var selFlag = '<%=request.getParameter("selFlag")%>';
	$(document).ready(function(){
		$("#opt-content").height($(window).height()-55);
		$.spin.imageBasePath='${ctx}/widgets/spin/img/spin1/';
		$("#number1").spin({
			max:100,
			min:1
		});
		createPersonGrid();
		//初始化值
		initValue();
		
		$("#contentForm").validate({});
		//绑定是否启用到期未巡检提醒
		$(":input[name=remindSwitch]").click(function(){
			changeRemindSwitch();
		});
		changeRemindSwitch();
		
		//绑定巡检周期改变事件
		$(":input[name=timeIntervalType]").click(function(){
			changeTimeInterval();
		});
		changeTimeInterval();
		
		//绑定提醒时间改变事件
		$(":input[name=triggerType]").click(function(){
			triggerTypeTime();
		});
		
	});
	function initValue(){
		var id = '';
		if(selFlag=='select'){
			id = window.parent.$("#evaluatingIndicatorList").jqGrid("getGridParam","selarrrow")[0];
		}else{
			id = window.parent.$("#evaluatingIndicatorList").jqGrid("getDataIDs")[0];
		}
		var obj = window.parent.$("#evaluatingIndicatorList").jqGrid("getRowData",id);
		var params = {};
		for(var pro in obj){
			if(pro.indexOf("patrolSettings")==0){
				params[pro.split(".")[1]] = obj[pro];
			}
		}
		var $obj = $(":input[name=timeIntervalType][value="+params.timeIntervalType+"]").attr("checked","checked");
		$obj.closest("tr").find(":input[type=text]").val(params.timeIntervalValue);
		if(!params.remindSwitch||'true' != params.remindSwitch){
			$(":input[name=remindSwitch]").attr("checked","");
		}else{
			$(":input[name=remindSwitch]").attr("checked","checked");
			
			$obj = $(":input[name=remindTimeType][value="+params.remindTimeType+"]").attr("checked","checked");
			$obj.closest("tr").find(":input[type=text]").val(params.remindTimeValue);
			//触发时间	
			$obj = $(":input[name=triggerType][value="+params.triggerType+"]").attr("checked","checked");
			$obj.closest("tr").find(":input[type=text]").val(params.triggerValue);
			
			$(":input[name=recieveType]").attr("checked","");
			var types = [];
			if(params.receiveTypes){
				types = params.receiveTypes.split(",");
			}
			for(var i=0;i<types.length;i++){
				$(":input[name=recieveType][value="+types[i]+"]").attr("checked","checked");
			}
			if(params.receiveUserIds&&params.receiveUserNames){
				addPersonDatas(params.receiveUserIds.split(","), params.receiveUserNames.split(","));
			}
		}
	}
	//到期未巡检提醒绑定
	function changeRemindSwitch(){
		var checked = $(":input[name=remindSwitch]").attr("checked");
		if(checked){
			$("#remindTable").find(":input").attr("disabled","");
			$("#receiveTable").find(":input").attr("disabled","");
			$("#receiveTable").find("a").attr("disabled","");
			$("#triggerTable").find(":input").attr("disabled","");
			triggerTypeTime();
		}else{
			$("#remindTable").find(":input").attr("disabled","disabled");
			$("#receiveTable").find(":input").attr("disabled","disabled");
			$("#receiveTable").find("a").attr("disabled","disabled");
			$("#triggerTable").find(":input").attr("disabled","disabled");
		}
	}
	function changeTimeInterval(){
		$(":input[name=timeIntervalType]").closest("table").find("tr").each(function(){
			var checked = $(this).find(":input[name=timeIntervalType]").attr("checked");
			if(checked){
				$(this).find(":input[type=text]").attr("disabled","").focus();
			}else{
				$(this).find(":input[type=text]").attr("disabled","disabled")
					.removeClass("error").parent().find("label.error").remove();
			}
		});
	}
	function triggerTypeTime(){
		$(":input[name=triggerType]").closest("table").find("tr").each(function(){
			var checked = $(this).find(":input[name=triggerType]").attr("checked");
			if(checked){
				$(this).find(":input[type=text]").attr("disabled","").focus();
			}else{
				$(this).find(":input[type=text]").attr("disabled","disabled")
					.removeClass("error").parent().find("label.error").remove();
			}
		});
	}
	function submitForm(url){
		if($("#contentForm").valid()){
			var params = {};
			var $obj = $(":input[name=timeIntervalType]:checked");
			params.timeIntervalType = $obj.val();
			params.timeIntervalValue = $obj.closest("tr").find(":input[type=text]").val();
			var isChecked = $(":input[name=remindSwitch]").attr("checked");
			if(!isChecked){
				params.remindSwitch = false;
			}else{
				params.remindSwitch = true;
				$obj = $(":input[name=remindTimeType]:checked");
				params.remindTimeType = $obj.val();
				params.remindTimeValue = $obj.closest("tr").find(":input[type=text]").val();
				var types = [];
				$(":input[name=recieveType]:checked").each(function(index,obj){
					types.push(obj.value);
				});
				params.receiveType = types.join(",");
				var ids = $("#person_table").jqGrid("getDataIDs");
				var names = [];
				for(var i=0;i<ids.length;i++){
					var obj = $("#person_table").jqGrid("getRowData",ids[i]);
					names.push(obj.name);
				}
				params.receiveUserIds = ids.join(",");
				params.receiveUserNames = names.join(",");
				$obj = $(":input[name=triggerType]:checked");
				params.triggerType = $obj.val();
				params.triggerValue = $obj.closest("tr").find(":input[type=text]").val();
				if(!params.triggerValue){
					params.triggerValue = 0;
				}
			}
			params.selFlag = selFlag;
			window.parent.getIndicators(selFlag,params);
			$(".opt-btn .btn").attr("disabled","disabled");
			$("#message").html("正在保存配置,请稍候... ...");
			$.post(url,params,function(result){
				$(".opt-btn .btn").attr("disabled","");
				$("#message").html(result.message);
				if(result.error){
					alert(result.message);
				}else{
					window.parent.$.colorbox.close();
				}
			},'json');
		}
	}
	
	//接收人员
	function createPersonGrid(){
		var height = $(window).height()-420;
		$("#person_table").jqGrid({
			height : height>100?height:100,
			width : $(window).width()-140,
			datatype : "locale",
			rownumbers : true,
			colModel : [
				{name:'id',index:'id',width:50,align:"center",hidden:true}, 
	            {label:'提醒接收人员',name:'name',index:'name',width:140,hidden:false},
			],
			forceFit : true,
		   	shrinkToFit : true,
		   	autowidth : false,
			viewrecords : true, 
			sortorder : "desc",
			gridComplete : function(){}
		});
	}
	//选择预警人员
	function addPerson(){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择提醒人员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
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
		addPersonDatas(personIds,personName);
	}
	
	function addPersonDatas(personIds,personNames){
		var mydata = [];
		for(var i=0;i<personIds.length;i++){
			var hisData = $("#person_table").jqGrid("getRowData",personIds[i]);
			if(!hisData||!hisData.id){
				mydata.push({name:personNames[i],id:personIds[i]});
			}
		}
		for(var i=0;i<mydata.length;i++){
			jQuery("#person_table").jqGrid('addRowData',mydata[i].id,mydata[i]);
		}
	}
	//删除预警人员
	function delPerson() {
		var rows = jQuery("#person_table").getGridParam('selarrrow');
		if(rows.length==0){
			alert("请选择所要删除的人员！");
			return;
		}
		rows = rows.join(",").split(",");
		for(var i=0;i<rows.length;i++){
			jQuery("#person_table").jqGrid("delRowData",rows[i]);
		}
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="submitForm('${mfgctx}/inspection-base/indicator/save-patrol-settings.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				<button  class='btn' type="button" onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
				<span id="message" style="color:red;"></span>
			</div>
			<div style="margin-left: 10px;overflow-y:auto;" id="opt-content">
				<form id="contentForm" name="contentForm" method="post" >
				<fieldSet>
					<legend>&nbsp;&nbsp;巡检周期&nbsp;&nbsp;</legend>
					<table style="width: 100%;">
			    		<tr>
			    			<td style="width:160px;">
			    				<input type="radio" name="timeIntervalType" checked="checked" value="fixed"/>固定时间间隔(小时)</td>
			    			<td>
			    				<input type="text" style="width:60px;" name="timeInterval1" value="3" class="{required:true,number:true,min:0,messages:{number:'必须为数字格式!',min:'不能为负数!'}}"></input>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td></td>
			    			<td>
			    				数字类型,可用小数表示,如3.5表示每隔三个半小时执行一次巡检.
			    			</td>
			    		</tr>
			    		<tr>
			    			<td>
			    				<input type="radio" name="timeIntervalType" value="custom"/>自定义时间
			    			</td>
			    			<td>
			    				<input type="text" style="width:85%;" name="timeInterval2" value="" class="{required:true,stringCheck:true,messages:{required:'必填!',stringCheck:'格式不正确!'}}"></input>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td></td>
			    			<td>
			    				多个时间用逗号隔开,如:12:00,18:00,21:00
			    			</td>
			    		</tr>
			    	</table>
				</fieldSet>
				<fieldSet style="margin-top:6px;">
					<legend>&nbsp;&nbsp;<input type="checkbox" name="remindSwitch">启用到期未巡检提醒&nbsp;&nbsp;</legend>
					<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="remindTable">
			    		<tr>
			    			<td style="width:160px;">
			    				<input type="radio" name="remindTimeType" value="fixed" checked="checked"/>超期
			    			</td>
			    			<td><input type="text"  style="width:60px;" name="remindTime1" value="0.5" class="{required:true,number:true,min:0,messages:{number:'必须为数字格式!',min:'不能为负数!'}}"/>&nbsp;小时未巡检提醒.</td>
			    		</tr>
			    		<tr>
			    			<td></td>
			    			<td>
			    				数字类型,小于一个小时用小数表示,如0.5表示超过半个小时提醒.
			    			</td>
			    		</tr>
			    	</table>
				</fieldSet>
				<fieldSet style="margin-top:6px;">
					<legend>&nbsp;&nbsp;触发方式&nbsp;&nbsp;</legend>
					<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="triggerTable">
			    		<tr>
			    			<td style="width:160px;">
			    				<input type="radio" name="triggerType" value="fixed" checked="checked"/>立即触发.
			    			</td>
			    			<td><input type="hidden" name="triggerValue1" value="0"/></td>
			    		</tr>
			    		<tr>
			    			<td>
			    				<input type="radio" name="triggerType" value="custom"/>自定义时间段
			    			</td>
			    			<td><input type="text" style="width:90%;" name="triggerValue2" class="{required:true,stringCheck:true,messages:{required:'必填!',stringCheck:'格式不正确!'}}"/></td>
			    		</tr>
			    		<tr>
			    			<td></td>
			    			<td>
			    				服务器先缓存提醒消息,到设定的时间后统一触发,多个时间段用逗号隔开,如:12:00,18:00,21:00
			    			</td>
			    		</tr>
			    	</table>
				</fieldSet>
				<fieldSet style="margin-top:6px;">
					<legend>&nbsp;&nbsp;接收人员&nbsp;&nbsp;</legend>
					<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="receiveTable">
			    		<tr>
			    			<td style="width:220px;">
			    				提醒方式
			    			</td>
			    			<td>
			    				<input name="recieveType" type="checkbox" value="news">消息
								<input name="recieveType" type="checkbox" value="mail">邮件
								<input name="recieveType" type="checkbox" value="message">短信
			    			</td>
			    		</tr>
			    		<tr height=30>
			    			<td>接收人员</td>
			    			<td>
			    				<a class="small-button-bg" style="vertical-align: middle;" href="javascript:addPerson();" title="添加接收人员"><span class="ui-icon ui-icon-plusthick" style='cursor:pointer;'></span></a>
								<a class="small-button-bg" style="vertical-align: middle;" href="#" onclick="delPerson();" title="删除接收人员"><span class="ui-icon ui-icon-minusthick" style='cursor:pointer;'></span></a>
			    			</td>
			    		</tr>
			    		<tr>
			    			<td></td>
							<td>
								<input id="personId" name="personId" type="hidden"/>
								<input id="personName" name="personName" type="hidden"/>
								<table id="person_table"></table>
							</td>
						</tr>
			    	</table>
				</fieldSet>
				</form>
			</div>
		</div>
	</div>
</body>
</html>