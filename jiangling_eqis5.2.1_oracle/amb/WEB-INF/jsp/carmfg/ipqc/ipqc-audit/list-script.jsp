<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
//格式化
function operateFormater(cellValue,options,rowObj){
	if(rowObj.id){
		var operations = "<div style='text-align:center;' title='管控物质'><a class=\"small-button-bg\" onclick=\"editInfo("+cellValue+");\" href=\"#\"><span class=\"ui-icon ui-icon-info\" style='cursor:pointer;'></span></a></div>";
		return operations;
	}else{
		return '';
	}
}
function editInfo(id){
	$.colorbox({
		href:'${gpctx}/averageMaterial/input.htm?id='+id,
		iframe:true, 
		width:$(window).width()<1500?$(window).width()-100:1000, 
		height:$(window).height()<1380?$(window).height()-100:600,
		overlayClose:false,
		title:"管控物质信息",
// 		cbox_open:function(){
// 			$.post('${gpctx}/averageMaterial/gpSubstance-datas.htm?id='+id,
// 			 		function(data){
// 			 		});
// 		},
		onClosed:function(){
			$("#list").trigger("reloadGrid");
			makeEditable(true);
		}
	});
}
function createColorLight(cellvalue, options, rowObject){
	var isWarm=rowObject.taskProgress;
	if(isWarm=='合格'){
		str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
	}else if(isWarm=='待审核'){
		str= '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
	}else{
		str= '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';	
	}
	return str;
}
function viewChildTableInputForm(value,o,obj){
	var strs = "";
	strs = "<div style='width:100%;text-align:center;' title='查看详情' ><a class=\"small-button-bg\"  onclick=\"_viewChildTableInputForm("+obj.id+");\" href=\"#\"><span class='ui-icon ui-icon-info' style='cursor:pointer;text-align:right;'></span></a><div>";
	return strs;
}
function _viewChildTableInputForm(formId){
	$.colorbox({href:'${gpctx}/gpmaterial/sub/select-input.htm?id='+formId,iframe:true,
		innerWidth:800, 
		innerHeight:580,
		overlayClose:false,
		title:"项目详情"
	});
}
</script>