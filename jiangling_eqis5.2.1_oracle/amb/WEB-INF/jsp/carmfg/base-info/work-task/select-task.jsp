<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.ambition.supplier.entity.Supplier"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript">
	//重写保存后的方法
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	function formatter(cellvalue, options, rowObject){
		return "<a class='small-button-bg' href='#' onclick='realSelect("+cellvalue+")' style='float:center;margin-left:8px;' title='选择【"+rowObject.materialName+"】'><span class='ui-icon ui-icon-circle-check'></span></a>";
	}
	//确定
	function realSelect(id){
		var ids = [];
		if(id){
			ids.push(id);
		}else{
			var id = $("#taskList").jqGrid("getGridParam","selrow");
			if(id){
				ids.push(id);
			}
		}
		if(ids.length == 0){
			alert("请选择生产工单!");
			return;
		}
		if($.isFunction(window.parent.setTaskValue)){
			var data = [];
			for(var i=0;i < ids.length;i++){
				var objs = $("#taskList").jqGrid('getRowData',ids[i]);					
				if(objs){
					data.push(objs);
				}
			}	
			if(data.length > 0){
				//选择生产任务单后调用父窗口的setTaskValue（data）方法，将任务单信息返回到打开窗口的父页面
				window.parent.setTaskValue(data);
			}else{
				alert("选择的值不存在!");
			}
			window.parent.$.colorbox.close();
		}else{
			alert("页面还没有 setTaskValue 方法!");
		}
	}
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>
				<button class='btn' onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			</div>
			<div id="opt-content" style="clear:both;">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="taskList"
						url="${mfgctx}/base-info/work-task/list-datas.htm" code="MFG_WORK_TASK" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>

</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>