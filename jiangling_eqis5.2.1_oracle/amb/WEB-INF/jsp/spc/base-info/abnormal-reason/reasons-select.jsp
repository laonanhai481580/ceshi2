<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript">
	isUsingComonLayout=false;
	$(document).ready(function(){
		contentResize();
	});
	
	function contentResize(){
		var height = $(window).height() - 115;
		$("#gridList").jqGrid("setGridHeight",height);
	}
	//确定
	function realSelect(){
		var ids = $("#gridList").jqGrid("getGridParam","selarrrow");
		if(ids.length == 0){
			alert("请选择原因!");
			return;
		}
		if(ids.length > 1){
			alert("不能选择多条原因!");
			return;
		}
		if($.isFunction(window.parent.setReasonValue)){
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#gridList").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push(data);
				}
			}
			if(objs.length>0){
				window.parent.setReasonValue(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else{
			alert("页面还没有 setReasonValue 方法!");
		}
	}
	//关闭
	function cancel(){
		window.parent.$.colorbox.close();
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="opt-body">
		<div class="opt-btn" id="btnDiv">
			<button class='btn' onclick="realSelect();">
				<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
			</button>
			<button class='btn' onclick="cancel();">
				<span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span>
			</button>
		</div>
		<div id="opt-content" style="padding-top:6px;margin:0px;">
			<form id="contentForm" name="contentForm" method="post"  action="">
				<grid:jqGrid gridId="gridList" url="${spcctx}/base-info/abnormal-reason/list-datas.htm" code="SPC_ABNORMAL_REASON" pageName="page"></grid:jqGrid>
			</form>
		</div>
	</div>
</body>
</html>