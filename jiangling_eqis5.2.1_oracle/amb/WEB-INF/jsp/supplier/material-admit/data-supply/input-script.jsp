<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
		function supplierClick(){
			$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		function setSupplierValue(objs){
			var obj = objs[0];
			$("#supplierName").val(obj.name);
			$("#supplierLoginName").val(obj.code);
			$("#supplierMails").val(obj.supplierEmail);
			//联系人
		} 

		function clearValue(showInputId,hiddenInputId){
			$("#"+showInputId).val("");
			$("#"+hiddenInputId).val("");
		}
		function copyValue(obj){
			$("#unwithholdValue").val($("#"+obj.id).val());
		}
		function changeFlow(obj){
			alert();
			if(obj.value.length!=0){
				alert();
			}
		}
		function findKey(obj){
			var value = obj.value;
			var url = "${supplierctx}/change/select-level.htm?valueStr=" + value;
			$.post(encodeURI(url),{},function(result){
				if(result.error){
					alert(result.message);
				}else{
					$("#changeLevel").val(result.level);
				}
			},'json');
		}
		function rateChange(){
			var rate = 0;
			$("tr[zbtr1=true]").find("input[fieldname=partRate]").each(function(index,obj){
				var ra = obj.value;
				if(ra){
					rate = parseFloat(ra)+ rate;
				}
			});
			$("#rateAll").val(rate);
		}
		function checkRate(){
			var rate = $("#rateAll").val();
			if('${nowTaskName}'=='供应商办理'){
				if(rate<99.99){
					alert("总比例不符合要求");
					return false;
				}else if(rate>=99.99&&rate<=100){
					return true;
				}else{
					alert("总比例不符合要求");
					return false;
				}
			}
			return true;
		}
		var weights=0;
		function setweigh(obj){
		 	var m=0;
		    var n=0;
			var controls = $("tr[zbtr1=true]").find('input[fieldname=materialWeight]');
			for(var i=0; i<controls.length; i++){
		       m=parseInt(controls[i].value);
		       if(isNaN(m)){
		    	   m=0;
		       }
		       n+=parseFloat(m);
		    }
			weights=parseFloat(n);
			$("#weights").html(weights);
		};
</script>