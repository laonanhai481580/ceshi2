<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function subControl(){
	var str = $("#currentActivityName").val();
	if(!str){
		$("input:[stage='two']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
	}else if(str!=null&&str=="实验室确认"){
		$("input:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
	}else{
		$("input:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("input:[stage='two']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});			
	}
}
	
</script>