<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>选择部件</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		//确定
		function realSelect(id){
			var ids = [];
			if(id){
				ids.push(id);
			}else{
				ids = $("#problemPointList").jqGrid("getGridParam","selarrrow");	
				if(id){
					ids.push(id);
				}
			}
			if(ids.length == 0){
				alert("请选择!");
				return;
			}
			if($.isFunction(window.parent.setProblemValue)){
				var data = [];
				for(var i=0;i < ids.length;i++){
					var objs = $("#problemPointList").jqGrid('getRowData',ids[i]);					
					if(objs){
						data.push({key:objs.ofilmModel,value:objs.customerModel});
					}
				}	
				if(data.length > 0){
					window.parent.setProblemValue(data);
				}else{
					alert("选择的值不存在!");
				}
				window.parent.$.colorbox.close();
			}else{
				alert("页面还没有 setProblemValue方法!");
			}			
		}	
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn" id="btnDiv">
				<button class='btn' type="button" onclick="realSelect();"><span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
				<button class='btn' type="button" onclick="cancel();"><span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
				<input type="hidden" name="customerName" id="customerName" value="${customerName}"/>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post"  action="">
					<grid:jqGrid gridId="problemPointList"  url="${aftersalesctx}/base-info/customer/model-select-list-datas.htm?customerName=${customerName}" code="AFS_CUSTOMER_MODEL_SELECT" pageName="page"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
</html>