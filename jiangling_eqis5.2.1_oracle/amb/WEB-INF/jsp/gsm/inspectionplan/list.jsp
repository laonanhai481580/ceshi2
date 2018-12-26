  <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
 	<script type="text/javascript">
  	function clickInput(value,o,obj){
  		var strs = "";
  		strs=  "<div style='width:100%;text-align:center;' title=\"查看检验记录\" ><a class=\"small-button-bg\"  onclick=\"lookInput("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a></div>";
  		return strs;
	}
 	function lookInput(no){
		var url="${gsmctx}/inspectionplan/view-info.htm?id="+no;
		$.colorbox({href:encodeURI(url),iframe:true, innerWidth:900, innerHeight:600,
 			overlayClose:false,
 			title:"检定记录"
 		});
	} 
 	
	function $oneditfunc(rowid){
		params = {hisAttachmentFiles : $("#" + rowid + "_hiddenAttachmentFiles").val()};
		$("#" + rowid + " .upload").show();
	};
	function $processRowData(data){
		for(var pro in params){
			data[pro] = params[pro];
		}
		return data;
	}
	function formateAttachmentFiles(value,o,obj){
		var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='<s:text name='上传附件'/>'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
		return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
	}
	function beginUpload(rowId){
		$.upload({
			showInputId : rowId + "_showAttachmentFiles",
			hiddenInputId : rowId + "_hiddenAttachmentFiles",
			callback : function(files){
				params.attachment = $("#" + rowId + "_hiddenAttachmentFiles").val();
			}
		});
	}
	//红警灯
	function createColorLight(cellvalue, options, rowObject){
		var inspectionPlanDate=rowObject.inspectionPlanDate;//计划校验日期
		var time = (new Date).getTime();//当前时间内
		var newDate=new Date(inspectionPlanDate).getTime()-time;
		var  tenDayTime=10*24 * 60 * 60 *1000;
		var  monthTime=30*24 * 60 * 60 *1000;
		var checkMethod=rowObject.checkMethod;//计划校验日期
		if(checkMethod =="内校"){
			if(newDate>tenDayTime){
				return '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
			}else{
				return '<div style="text-align:center;"><img src="${ctx}/images/red.gif"/></div>';				
			}
		}
		if(checkMethod=="外校"){
			if(newDate>monthTime){
				return '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
			}else{
				return '<div style="text-align:center;"><img src="${ctx}/images/red.gif"/></div>';				
			}
		}		
	}
	//校验
	function test(){
		var ids = $("#dynamicMeasurementInspectionPlan").getGridParam("selarrrow");
		if(ids.length == 0){
			alert("<s:text name='请选择数据！！'/>");
			return;
		}
		var flag = false;
		var type='';
		for (var i = 0; i < ids.length; i++) {
			var rowData = $("#dynamicMeasurementInspectionPlan").jqGrid("getRowData",ids[i]);
			if("待校验" != rowData.inspectionState){
				flag = true;
			}
			if('外校'==rowData.checkMethod){
				type='Y';
			}
			if('内校'==rowData.checkMethod){
				type='N';
			}
		}
		if(flag){
			alert("<s:text name='送校状态不是【待校验】不能送校'/>");
			return false;
		}
		
		
		$.post("${gsmctx}/inspectionplan/test.htm?ids="+ids+"&&type="+type,null,function(data){ 
			if(data.error){
				alert(data.message);
				return false;
			}
			alert(data.message);
			$("#dynamicMeasurementInspectionPlan").trigger("reloadGrid");
		},"json");
	}
	//选择人员
	function storagerClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='编辑人'/>",
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.currentInputId,
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	//选择部门
	function useDeptClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :"<s:text name='选择部门'/>",
			innerWidth:'400',
			treeType:'DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.currentInputId,
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	} 
/* 	//设置邮件提醒
	function mailSettings(flag){
		var url = webRoot+'/gsm/inspectionplan/mail-settings.htm?businessCode='+flag;
 		$.colorbox({href:url,iframe:true,
 			width:$(window).width()<567?$(window).width()-50:567,
 			height:$(window).height()<540?$(window).height()-50:540,
 			overlayClose:false,
 			title:"<s:text name='设置提前提醒'/>",
 			onClosed:function(){}
 		});
	}
	function mailSettingsOver(flag){
		var url = webRoot+'/gsm/inspectionplan/mail-settings-over.htm?businessCode='+flag;
 		$.colorbox({href:url,iframe:true,
 			width:$(window).width()<567?$(window).width()-50:567,
 			height:$(window).height()<540?$(window).height()-50:540,
 			overlayClose:false,
 			title:"<s:text name='设置超期提醒'/>",
 			onClosed:function(){
 			}
 		});
	} */

	function $gridComplete(){
		if(!window._initGroupHeader){
			window._initGroupHeader = true;
			$("#dynamicMeasurementInspectionPlan").jqGrid('setGroupHeaders',{
				  useColSpanStyle: true, 
				  groupHeaders:${groupNames}
			});
		};
	}
	
	/*---------------------------------------------------------
	函数名称:showIdentifiersDiv
	参          数:
	功          能:标识为（下拉选）
	------------------------------------------------------------*/
	function showIdentifiersDiv(){
		if($("#flag").css("display")=='none'){
			removeSearchBox();
			$("#flag").show();
			var position = $("#_task_button").position();
			$("#flag").css("left",position.left+15);
			$("#flag").css("top",position.top+28);
		}else{
			$("#flag").hide();
		}
	}
	
	var identifiersDiv;
	function hideIdentifiersDiv(){
		identifiersDiv = setTimeout('$("#flag").hide()',300);
	}
	
	function show_moveiIdentifiersDiv(){
		clearTimeout(identifiersDiv);
	}
	
	/**设置邮件提醒*/
	function mailSettings(flag){
		if('select'==flag){
			var rowIds = $("#dynamicMeasurementInspectionPlan").jqGrid("getGridParam","selarrrow");
			if(rowIds.length==0){
				alert("选择的数据为空,不能进行邮件提醒设置!");
				return false;
			}
		}else{
			var rowIds = $("#dynamicMeasurementInspectionPlan").jqGrid("getDataIDs");
			if(rowIds.length==0){
				alert("表格的数据为空,不能进行邮件提醒设置!");
				return false;
			}
		}
		var url = webRoot+'/gsm/inspectionplan/mail-settings.htm?selFlag='+flag;
 		$.colorbox({href:url,iframe:true,
 			width:$(window).width()<567?$(window).width()-50:567,
 			height:$(window).height()<540?$(window).height()-50:540,
 			overlayClose:false,
 			title:"设置邮件提醒",
 			onClosed:function(){
 				$("#dynamicMeasurementInspectionPlan").trigger("reloadGrid");
 			}
 		});
	}
	//保存邮件设置时获取的参数
	function getIndicators(businessCode,params){
		if(businessCode=='select'){
			params.ids = $("#dynamicMeasurementInspectionPlan").jqGrid("getGridParam","selarrrow").join(",");
		}else{
			var postData = $("#dynamicMeasurementInspectionPlan").jqGrid("getGridParam","postData");
			for(var pro in postData){
				params[pro] = postData[pro];
			}
		}
	}	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="inspectionplan";
		var thirdMenu="_myInspectionPlan";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-inspection-plan-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="gsm_inspectionplan_delete">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="删除"/> </span></span></button>
					</security:authorize>  
					<security:authorize ifAnyGranted="gsm_intransited-equipment_test"> 
 						<button class="btn" onclick="test(this)"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name='送校'/></span></span></button>  
 					</security:authorize>

					<%-- <security:authorize ifAnyGranted="gsm_inspectionplan-mail">
						<button class="btn" onclick="mailSettings('inspectionplan-mail');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='提前邮件提醒设置'/></span></span></button>
					</security:authorize> --%>
<%-- 					<security:authorize ifAnyGranted="gsm_inspectionplan-mail"> --%>
<%-- 						<button class="btn" id="_task_button" onclick="showIdentifiersDiv();"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='邮件提醒设置'/></span></span></button> --%>
<%-- 					</security:authorize> --%>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/> </span></span></button>	
					<security:authorize ifAnyGranted="gsm_inspectionplan_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/inspectionplan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
					</security:authorize>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicMeasurementInspectionPlan" url="${gsmctx}/inspectionplan/list-state.htm?type=N" code="MEASUREMENT_INSPECTION_PLAN" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
			<div id="flag" onmouseover='show_moveiIdentifiersDiv();' onmouseout='hideIdentifiersDiv();'>
				<ul >
				 <li onclick="mailSettings('select');">
				 <a href="#">当前所选</a>
				 </li>
				 <li onclick="mailSettings('all');">
				 <a href="#">全部</a>
				 </li>
				</ul>
			</div>
		</div>
	</div> 
</body> 
</html>