<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function mustNum(obj){
		var id=obj.id;
		var code=id.split("_")[0];
		var value=obj.value;
		if(value&&isNaN(value)){
			$("#"+code+"_span").html("*必须为数字!");	
		}else{
			$("#"+code+"_span").html("");
		}
	}
 	//保存
 	function saveForm() {
		var value="";
		var error=false;
		$("input[vlrr=true]").each(function(index,obj){
			iobj = $(obj);
			value += ",\""+iobj.attr("name").split("_")[0]+"\":\""+iobj.val()+"\"";
			if(iobj.val()&&isNaN(iobj.val())){
				alert("数据格式有误！");
				error=true;
			}
		});
		var items ="[{"+value.substring(1)+"}]";
		if(error){
			return ;
		}
 		$("#vlrrItems").val(items);
 		$("#applicationForm").attr("action","${aftersalesctx}/oba/edit-save.htm");
 		$("#applicationForm").submit();
 	}
	function customerNameChange(obj){
		var customerName=$("#customerName").val();
		var url = "${aftersalesctx}/base-info/customer/place-select.htm?customerName="+customerName;
		$.post(encodeURI(url),{},function(result){
 			if(result.error){
 				alert(result.message);
 			}else{
				var places = result.places;
				var placeArr = places.split(",");
				var place = document.getElementById("place");
				place.options.length=0;
				var opp1 = new Option("","");
				place.add(opp1);
 				for(var i=0;i<placeArr.length;i++){
 					var opp = new Option(placeArr[i],placeArr[i]);
 					place.add(opp);
 				}
 			}
 		},'json');
	}	 	
	function caculateBadRate(){
		var inputCount = $("#inputCount").val();
		var unqualifiedCount = $("#unqualifiedCount").val();
		if(isNaN(inputCount)){
			alert("投入数必须为整数！");
			$("#inputCount").focus();
			$("#inputCount").val("");
			$("#unqualifiedRate").val("");
			return;
		}
		if(isNaN(unqualifiedCount)){
			alert("不良数必须为整数！");
			$("#unqualifiedCount").focus();
			$("#unqualifiedCount").val("");
			$("#unqualifiedRate").val("");
			return;
		}

		if((inputCount-0)<(unqualifiedCount-0)){
			alert("不良数不能大于投入数！");
			$("#unqualifiedCount").val("");
			$("#unqualifiedRate").val("");
			return;
		}
		var rate=unqualifiedCount*100/inputCount;
		$("#unqualifiedRate").val(rate.toFixed(2)+"%");
	} 	
</script>