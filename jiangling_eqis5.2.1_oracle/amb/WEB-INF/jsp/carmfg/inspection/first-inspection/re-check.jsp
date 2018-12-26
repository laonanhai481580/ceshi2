<%@page import="com.ambition.carmfg.entity.MfgCheckInspectionReport"%>
<%@page import="com.ambition.iqc.entity.IncomingInspectionActionsReport"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
//重新检验
function reCheckList(){
	var ids = $("#dynamicInspection").jqGrid("getGridParam","selarrrow");
	if(ids.length==0){
		alert("请选择需要操作的数据!");
		return;
	}
	if(confirm("确定要重新检验吗?")){
		$("#message").html("正在执行操作,请稍候... ...");
		var url = '${mfgctx}/inspection/first-inspection/re-check.htm';
		$.post(url,{deleteIds:ids.join(",")},function(result){
			if(result.error){
				$("#message").html("<b style='color:red;'>"+result.message+"</b>");
				alert(result.message);				
			}else{
				$("#message").html("<b style='color:red;'>操作成功!</b>");
				if($("#dynamicInspection").length>0){
					$("#dynamicInspection").trigger("reloadGrid");
				}
			}
			setTimeout(function(){
				$("#message").html('');
			},3000);
		},'json');
	}
}