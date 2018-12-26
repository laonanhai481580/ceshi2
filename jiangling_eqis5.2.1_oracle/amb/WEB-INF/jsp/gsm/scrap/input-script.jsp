<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function emptyText() {
		document.getElementById("inspectionResult").value = "检验结果:";
		
	}
	function Epm(){
		$.colorbox({href:"${gsmctx}/equipment/list-view.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择设备"
		});
	}
	function setEpmValue(objs){
		var obj = objs[0];
		$("#managerAssets").val(obj.managerAssets);
		$("#equipmentName").val(obj.equipmentName);
		$("#equipmentModel").val(obj.equipmentModel);
		$("#manufacturer").val(obj.manufacturer);
		$("#factoryNumber").val(obj.factoryNumber);
		$("#devName").val(obj.devName);
		$("#responsible").val(obj.dutyMan);
		//联系人
	}
</script>