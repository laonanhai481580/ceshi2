<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function $oneditfunc(){
	$("#quantity").keyup(caculateBadRate);
	$("#grantNumber").keyup(caculateBadRate);
	$("#sampleSum").keyup(caculateBadRate);
	$("#sampletype").keyup(caculateBadRate);
}

function caculateBadRate(){
	var quantity = $("#quantity").val();
	var grantNumber = $("#grantNumber").val();
	var sampleSum = $("#sampleSum").val();
	if(isNaN(quantity)){
		alert("样品数量必须为整数！");
		$("#quantity").val("");
		return;
	}
	if(isNaN(grantNumber)){
		alert("颁发数量必须为整数！");
		$("#grantNumber").val("");
		return;
	}
// 	if(isNaN(sampleSum)||sampleSum==""){
// 		alert("归还数量必须为整数！");
// 		$("#sampleSum").val("");
// 		return;
// 	}
	if((quantity-0)<(grantNumber-0)){
		alert("颁发数量大于样品数量！");
		$("#grantNumber").val("");
		return;
	}
	if((quantity-0)<(sampleSum-0)){
		alert("归还数量大于样品数量！");
		$("#sampleSum").val(0);
		return;
	}
	if((quantity-0)>(sampleSum-0)){
		$("#sampletype").val('Y');
		return;
	}else{
		$("#sampletype").val('N');
	}
}
var weights=0;
function setweigh(obj){
 	var m=0;
    var n=0;
	var controls = $("tr[zbtr1=true]").find('input[fieldname=returnQuantity]');
	for(var i=0; i<controls.length; i++){
       m=parseInt(controls[i].value);
       if(isNaN(m)){
    	   m=0;
       }
       n+=parseFloat(m);
    }
	weights=parseFloat(n);
	$("#sampleSum").val(weights);
	caculateBadRate();
};

</script>