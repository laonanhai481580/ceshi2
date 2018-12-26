<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function selectPerson1(obj,hidden) {
	show = $("#" + obj.id).val();
	hiddenNames = $("#" + hidden).val();
	var acsSystemUrl = "${ctx}";
	popTree({
		title : '选择人员',
		innerWidth : '400',
		treeType : 'MAN_DEPARTMENT_TREE',
		defaultTreeValue : 'id',
		leafPage : 'false',
		multiple : 'true',
		hiddenInputId : obj.id,
		showInputId : obj.id,
		acsSystemUrl : acsSystemUrl,
		callBack : function() {
			copyPersonLoginName(obj.id,hidden,show,hiddenNames);
		}
	});
}
function copyPersonLoginName(showId,hidden,show,hiddenNames) {
	var names = $("#" + showId).val();
	var hiddenValue = $("#"+ hidden).val();
	if(show!=''){
		var nameArry = names.split(",");
		var newNames = '';
		var hiddenArray = jstree.getLoginNames().split(",");
		var newHidden = '';
		for(var i=0;i<nameArry.length;i++){
			if(show.indexOf(nameArry[i])<0){
				if(newNames==''){
					newNames = nameArry[i];
				}else{
					newNames += "," + nameArry[i];
				}
			}
		}
		for(var i=0;i<hiddenArray.length;i++){
			if(hiddenValue.indexOf(hiddenArray[i])<0){
				if(newHidden==''){
					newHidden = hiddenArray[i];
				}else{
					newHidden += "," + hiddenArray[i];
				}
			}
		}
		$("#"+showId).val(show+","+newNames);
		$('#'+hidden).attr("value", hiddenValue+","+newHidden);
	}else{
		$('#'+hidden).attr("value", jstree.getLoginNames());
	}
}
function clearValue(showInputId,hiddenInputId){
	$("#"+showInputId).val("");
	$("#"+hiddenInputId).val("");
}

</script>