<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function emptyText() {
		document.getElementById("inspectionResult").value = "检验结果:";
		
	}
	var orderId="";
	function Epm(obj){
		orderId = obj.id.split("_")[0];
		console.log(orderId);
		$.colorbox({href:"${gsmctx}/equipment/list-view.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择设备"
		});
	}
	function setEpmValue(objs){
		var obj = objs[0];
// 		$("#managerAssets").val(obj.managerAssets);
// 		$("#equipmentName").val(obj.equipmentName);
// 		$("#equipmentModel").val(obj.equipmentModel);
// 		$("#address").val(obj.address);
// 		$("#dutyMan").val(obj.dutyMan);
// 		$("#measuringRange").val(obj.measuringRange);
// 		$("#manufacturer").val(obj.manufacturer);
// 		$("#factoryNumber").val(obj.factoryNumber);
// 		$("#calibrationDate").val(obj.devName);//
// 		$("#checkMethod").val(obj.checkMethod);
// 		$("#parameterStandard").val(obj.devName);//
// 		$("#measurementState").val(obj.measurementState);
// 		$("#confirmor").val(obj.stocktakingMan);
		
		$("#"+orderId+"_managerAssets").val(obj.managerAssets);
		$("#"+orderId+"_equipmentName").val(obj.equipmentName);
		$("#"+orderId+"_equipmentModel").val(obj.equipmentModel);
		$("#"+orderId+"_address").val(obj.address);
		$("#"+orderId+"_dutyMan").val(obj.dutyMan);
		$("#"+orderId+"_measuringRange").val(obj.measuringRange);
		$("#"+orderId+"_manufacturer").val(obj.manufacturer);
		$("#"+orderId+"_factoryNumber").val(obj.factoryNumber);
// 		$("#calibrationDate").val(obj.devName);//
		$("#"+orderId+"_checkMethod").val(obj.checkMethod);
// 		$("#parameterStandard").val(obj.devName);//
		$("#"+orderId+"_measurementState").val(obj.measurementState);
		$("#"+orderId+"_confirmor").val(obj.stocktakingMan);
		
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkItemsParent").position().top + $("#checkItemsParent").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#checkItemsParent").find("div").width(width).offset();
		var contentWidth =  $("#checkItemsParent").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	$(document).ready(function(){
		contentResize();
	});
</script>