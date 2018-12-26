<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>原因措施经验库</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${ctx}/js/search.js"></script>
	<script type="text/javascript">
	var multiselect = false;
	function contentResize(){
		$("#reasonMeasureLib").jqGrid("setGridWidth",$("#btnDiv").width()-12);
		$("#reasonMeasureLib").jqGrid('setGridHeight',$(document).height() - $("#btnDiv").height() - 40);
	}
	
	$(document).ready(function(){
		contentResize(); 
	});
	//确定
	function realSelect(){
		var ids = [];
		if(multiselect){
			$("#reasonMeasureLib input[type='checkbox']").each(function(index,obj){
	        		if(obj.checked&&obj.id&&obj.id.indexOf("_")>0){
	        			ids.push(obj.id.split('_')[1]);
	        		}
	        });
		}else{
			var id = $("#reasonMeasureLib").jqGrid("getGridParam","selrow");
			if(id){
				ids.push(id);
			}
		}
		if(ids.length == 0){
			alert("请选择原因措施!");
			return;
		}
		
		if($.isFunction(window.parent.setRAndMValue)){//获取全部的值
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#reasonMeasureLib").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push(data);
				}
			}
			if(objs.length>0){
				window.parent.setRAndMValue(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else if($.isFunction(window.parent.setBomValue)){//获取简洁的值
			var objs = [];
			for(var i=0;i<ids.length;i++){
				var data = $("#reasonMeasureLib").jqGrid('getRowData',ids[i]);
				if(data){
					objs.push({key:data.code,value:data.name});
				}
			}
			if(objs.length>0){
				window.parent.setRAndMValue(objs);
				window.parent.$.colorbox.close();
			}else{
				alert("选择的值不存在!");
			}
		}else{
			alert("页面还没有 setRAndMValue 方法!");
		}
	}
	
	
	//取消
	function cancel(){
		window.parent.$.colorbox.close();
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
	var secMenu="dataAcquisition";
	var thirdMenu="reasonAndMearsureLib";
	</script>
	
	<div class="ui-layout-center">
			<div class="opt-body">
				<aa:zone name="main">
				<div class="opt-btn">
					<a class='btn' onclick="realSelect();"><span><span>确定</span></span></a>
						<a class='btn' onclick="cancel();"><span><span>取消</span></span></a>
						<a  class='btn' onclick="showSearchDIV(this);"><span><span>查询</span></span></a>
					</div>
					<div id="opt-content">
						<form id="contentForm" name="contentForm" method="post"  action="">
							<grid:jqGrid gridId="reasonMeasureLib" url="${mfgctx}/data-acquisition/reason-measure-lib/list-datas.htm" code="MFG_REASON_AND_MEASURE_LIB" pageName="page" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
						</form>
					</div>
				</aa:zone>
			</div>
	</div>
	
</body>
</html>