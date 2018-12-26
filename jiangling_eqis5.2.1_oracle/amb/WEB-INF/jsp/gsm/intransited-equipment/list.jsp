<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='main.title'/></title>
	<%@include file="/common/meta.jsp" %> 
	<script type="text/javascript">
	//复制
	function from(){	
		var ids = $("#dynamicIntransitedEquipment").getGridParam('selarrrow');
		if(ids.length == 0){
			alert("<s:text name='请先选择数据'/>");
			return;
		}
		$.post("${gsmctx}/intransited-equipment/frow.htm?ids="+ids,null, function(data){$("#dynamicIntransitedEquipment").trigger("reloadGrid");},'json');
	}
	//重写方法（用于处理服务器返回的值）
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	//红警灯
	function createColorLight(cellvalue, options, rowObject){
		var agreedDeliveryDate=rowObject.agreedDeliveryDate;//约定送货日期
		var time = (new Date).getTime();//当前时间内
		var newDate=time-new Date(agreedDeliveryDate).getTime();
		var  tempTime=7*24 * 60 * 60 *1000;
 		if(newDate<0){//(24 * 60 * 60 * 1000)  一天时间
 			return '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>'; 			
 		}else {
       	  // if(time>new Date(agreedDeliveryDate).getTime()){//过期提示
	       	if(newDate>0&&newDate<tempTime){
		   	    return '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';
	       	}else{
		   	    return "";
	       	}
       	} 
 	}
	
	function confirmDelivery(){	
		var ids = $("#dynamicIntransitedEquipment").getGridParam('selarrrow');
		if(ids.length == 0){
			alert("<s:text name='请先选择数据'/>");
			return;
		}
 		$.post("${gsmctx}/intransited-equipment/confirm-delivery.htm?ids="+ids,null, function(data){
			if(data.error){
				alert(data.message);
				return false;
			}
			$("#dynamicIntransitedEquipment").trigger("reloadGrid");
		},"json"); 
	} 
	//新建
	function createIntransitedEquipment(){
		window.location="${gsmctx}/intransited-equipment/input.htm";	
	}
	function click(cellvalue, options, rowObject){
		if(cellvalue != null && cellvalue != ""){
			return "<a href='${gsmctx}/intransited-equipment/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
		}
		return "";
	}
	// 邮件
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
			callBack:function(){}});
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
	function mailSettings(flag,title){
		var url = webRoot+'/gsm/intransited-equipment/mail-settings.htm?businessCode='+flag;
 		$.colorbox({href:url,iframe:true,
 			width:$(window).width()<567?$(window).width()-50:567,
 			height:$(window).height()<540?$(window).height()-50:540,
 			overlayClose:false,
 			title:"<s:text name='设置超期提醒'/>",
 			onClosed:function(){
 			}
 		});
	}
	function mailSettingsOver(flag){
		var url = webRoot+'/gsm/intransited-equipment/mail-settings-over.htm?businessCode='+flag;
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
			params.ids = $("#dynamicIntransitedEquipment").jqGrid("getGridParam","selarrrow").join(",");
		}else{
			var postData = $("#dynamicIntransitedEquipment").jqGrid("getGridParam","postData");
			for(var pro in postData){
				params[pro] = postData[pro];
			}
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="intransited";
		var thirdMenu="_myIntransitedEquipment";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/gsm-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/gsm-intransited-equipment-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
			<div class="opt-btn">
				<security:authorize ifAnyGranted="gsm_intransited-equipment_save">
					<button class="btn" onclick="createIntransitedEquipment();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="gsm_intransited-equipment_delete">
					<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b><s:text name="删除"/></span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="gsm_intransited-equipment_frow">
					<button class="btn" onclick="from()"><span><span><b class="btn-icons btn-icons-copy"></b><s:text name="复制"/></span></span></button> 
				</security:authorize>
				<security:authorize ifAnyGranted="gsm_confirm-delivery_input">
					<button class="btn" onclick="confirmDelivery();"><span><span><b class="btn-icons btn-icons-ok"></b><s:text name="确认收货"/></span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="gsm_intransited-equipment_mail">
					<button class="btn" onclick="mailSettings('intransited-equipment');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='提前邮件提醒设置'/></span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="gsm_intransited-equipment_mail">
					<button class="btn" onclick="mailSettingsOver('intransited-equipment-over');"><span><span><b class="btn-icons btn-icons-settings"></b><s:text name='超期邮件提醒设置'/></span></span></button>
				</security:authorize>
					<button class="btn" onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b><s:text name="查询"/></span></span></button>	
				<security:authorize ifAnyGranted="gsm_intransited-equipment_export">
					<button class="btn" onclick="iMatrix.export_Data('${gsmctx}/intransited-equipment/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b><s:text name="导出"/></span></span></button>
				</security:authorize>
				<span style="color: red;" id="message"></span>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="dynamicIntransitedEquipment" url="${gsmctx}/intransited-equipment/list-datas.htm" code="MEASUREMENT_INTRANSITED_EQUIPMENT" pageName="page" ></grid:jqGrid>
				</form>
			</div>
			</aa:zone>
		</div>
	</div> 
</body>  
</html>