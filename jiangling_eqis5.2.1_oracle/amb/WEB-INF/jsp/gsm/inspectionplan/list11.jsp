 <%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %>
 	<script type="text/javascript">
/*  	function clickInput(cellvalue, options, rowObject){
		if(cellvalue){
			return  "<div style='text-align:center;'><a title='查看校验记录' onclick=\"lookInput('"+cellvalue+"');\" href=\"#\">"+cellvalue+"</a></div>";
		}else{
			return '';
		}
	}
 	function lookInput(no){
		var url="${gsmctx}/inspectionplan/view-info.htm?no="+no;
		$.colorbox({href:encodeURI(url),iframe:true, innerWidth:900, innerHeight:600,
 			overlayClose:false,
 			title:"检定记录"
 		});
	} */
 	
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
		var inspectionPlanDate=rowObject.inspectionPlanDate;//约定送货日期
		var time = (new Date).getTime();//当前时间内
		var newDate=time-new Date(inspectionPlanDate).getTime();
		var  tempTime=7*24 * 60 * 60 *1000;
 		if(newDate<0){//(24 * 60 * 60 * 1000)  一天时间
 			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
 		}else {
	 		if(newDate>0&&newDate<tempTime){
	 			return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
	 		}else{
	 			return '';
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
		for (var i = 0; i < ids.length; i++) {
			var rowData = $("#dynamicMeasurementInspectionPlan").jqGrid("getRowData",ids[i]);
			if("待校验" != rowData.inspectionState){
				flag = true;
			}
		}
		if(flag){
			alert("<s:text name='送校状态不是【待校验】不能送校'/>");
			return false;
		}
		$.post("${gsmctx}/inspectionplan/test.htm?ids="+ids,null,function(data){ 
			if(data.error){
				alert(data.message);
				return false;
			}
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
	//设置邮件提醒
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
	function $gridComplete(){
		if(!window._initGroupHeader){
			window._initGroupHeader = true;
			$("#dynamicMeasurementInspectionPlan").jqGrid('setGroupHeaders',{
				  useColSpanStyle: true, 
				  groupHeaders:${groupNames}
			});
		};
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
						<button class="btn" onclick="test()"><span><span><b class="btn-icons btn-icons-paste"></b><s:text name='送校'/></span></span></button> 
					</security:authorize>
					<%-- <security:authorize ifAnyGranted="gsm_inspectionplan-mail">
						<button class="btn" onclick="mailSettings('inspectionplan-mail');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='提前邮件提醒设置'/></span></span></button>
					</security:authorize> --%>
					<security:authorize ifAnyGranted="gsm_inspectionplan-mail">
						<button class="btn" onclick="mailSettingsOver('inspectionplan-mail-over');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='邮件提醒设置'/></span></span></button>
					</security:authorize>
						<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/> </span></span></button>	
					<security:authorize ifAnyGranted="gsm_inspectionplan_export">
						<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/inspectionplan/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
					</security:authorize>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="dynamicMeasurementInspectionPlan" url="${gsmctx}/inspectionplan/list-datas.htm" code="MEASUREMENT_INSPECTION_PLAN" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div> 
</body> 
</html>