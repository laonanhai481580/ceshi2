<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">

function Audit(){
	$.colorbox({href:"${qsmctx}/inner-audit/auditor-library/list-view.htm",iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择内审员"
	});
}
function setAuditValue(objs){
	var a="";
	var b="";
	for(var i=0;i<objs.length;i++){
		var obj = objs[i];
		a=obj.name;
		if(i==0){
			b=a;
		}else{
			b=b+","+a;
		}
	}
	$("#auditor").val(b);
}
</script>