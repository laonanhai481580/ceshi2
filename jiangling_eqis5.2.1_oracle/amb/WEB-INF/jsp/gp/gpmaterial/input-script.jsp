<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
function getScrollTop(){
	 return $("#opt-content").height()+23;
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
function contentResize(){
	initScrollDiv();
	checkScrollDiv();
}
$(document).ready(function(){
	contentResize();
	subControl();
// 	setweigh();
});


var orderId="";
var selectIndex1="";
function selectProject(obj){
	if(!obj.previousElementSibling){
		orderId=obj.id;
	}else if(!obj.previousElementSibling.id){
		orderId = obj.previousElementSibling.previousElementSibling.id;
	}else{
		orderId = obj.previousElementSibling.id;
	}
	selectIndex1=obj.parentNode;
	$.colorbox({href:"${gpctx}/averageMaterial/select-list.htm",
		iframe:true, 
		width:$(window).width()<700?$(window).width()-100:900,
		height:$(window).height()<400?$(window).height()-100:600,
			overlayClose:false,
			title:"选择材料"
	});
	
}
function setProjectValue(datas){
	var idFirst = orderId.split("_")[0];
	var a=idFirst.slice(0,2);
	var b=idFirst.slice(2);
	for(var i=0;i<datas.length;i++){
		$("#" + a + b + "_partName").val(datas[i].partName);
		$("#" + a + b +  "_averageMaterialName").val(datas[i].averageMaterialName);
		$("#" + a + b + "_averageMaterialModel").val(datas[i].averageMaterialModel);
		$("#" + a + b + "_averageMaterialWeight").val(datas[i].averageMaterialWeight);
		$("#" + a + b + "_averageMaterialAttribute").val(datas[i].averageMaterialAttribute);
		$("#" + a + b + "_unit").val(datas[i].unit);
		$("#" + a + b + "_manufacturer").val(datas[i].manufacturer);
		$("#" + a + b + "_text1").val(datas[i].text1);
		$("#" + a + b + "_text2").val(datas[i].text2);
		$("#" + a + b + "_text3").val(datas[i].text3);
		$("#" + a + b + "_text4").val(datas[i].text4);
		$("#" + a + b + "_text5").val(datas[i].text5);
		$("#" + a + b + "_text6").val(datas[i].text6);
		$("#" + a + b + "_text7").val(datas[i].text7);
		$("#" + a + b + "_text8").val(datas[i].text8);
		$("#" + a + b + "_text9").val(datas[i].text9);
		$("#" + a + b + "_text10").val(datas[i].text10);
		$("#" + a + b + "_text11").val(datas[i].text11);
		$("#" + a + b + "_text12").val(datas[i].text12);
		$("#" + a + b + "_text13").val(datas[i].text13);
		$("#" + a + b + "_text14").val(datas[i].text14);
		$("#" + a + b + "_text15").val(datas[i].text15);
		$("#" + a + b + "_text16").val(datas[i].text16);
		$("#" + a + b + "_text17").val(datas[i].text17);
		$("#" + a + b + "_testReportNo").val(datas[i].testReportNo);
		$("#" + a + b + "_testReportDate").val(datas[i].testReportDate);
		$("#" + a + b + "_testReportExpire").val(datas[i].testReportExpire);
		$("#" + a + b + "_testReportDepart").val(datas[i].testReportDepart);
		$("#" + a + b + "_exemption").val(datas[i].exemption);
		$("#" + a + b + "_testReportFile").val(datas[i].testReportFile);
		$("#" + a + b + "_msdsFile").val(datas[i].msdsFile);
		$("#" + a + b + "_taskProgress").val(datas[i].taskProgress);
		$("#" + a + b + "_averageId").val(datas[i].id);
		b++;
		if(i!=datas.length-1){
			addRowHtml(selectIndex1);
		}
	}
	setweigh();
}

function addRowHtml(aObj){
	var $tr = $(aObj).closest("tr");
	var clonetr = $tr.clone(false); 
	$tr.after(clonetr);
	var tableName = $tr.attr("tableName");
	var tableParams = _ambWorkflowFormObj.children[tableName];
	var maxIndex = tableParams['maxIndex']+1;
	tableParams['maxIndex'] = maxIndex;
	var flag = tableParams['inputNamePrefix'] + maxIndex;
	clonetr.attr("trPrefix",flag);
	//重置对象
	clonetr
		.find(":input[fieldName]")
		.unbind()
		.removeClass("hasDatepicker")
		.each(function(index ,input){
			$input = $(input);
			//清除值
			if(_ambWorkflowFormObj.childrenInitParams.addRowToClearValue){
				$input.val("");
				if($input.attr("title")!=""){
					$input.attr("title","");
				}
			}
			var id = flag + "_" + $input.attr("fieldName");
			$input.attr("name",id)
				  .attr("id",id);
			var hiddenInputName = $input.attr("hiddenInputName");
			if(hiddenInputName){
				$input.attr("hiddenInputId",flag + "_" + hiddenInputName);
			}
			var showInputName = $input.attr("showInputName");
			if(showInputName){
				$input.attr("showInputId",flag + "_" + showInputName);
			}
	});
	//移除自动添加的对象
	clonetr.find("[autoAppend]").remove();
	clonetr.find(":input[isFile]").removeAttr("showInputId");
	//初始化新增行的输入元素
	_initInputForScope(clonetr);
	//更新序号
	_updateRowNum(tableName);
	//检查是否有回调事件
	if($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterAddRow)){
		_ambWorkflowFormObj.childrenInitParams.afterAddRow(tableName,clonetr);
	}
}
//删除行
function removeRowHtml(aObj){
	var $tr = $(aObj).closest("tr");
	var tableName = $tr.attr("tableName");
	if($("tr." + tableName).length<=1){
		alert("至少保留一行!");
		return;
	}
	$tr.remove();
	//更新序号
	_updateRowNum(tableName);
	setweigh();
	//检查是否有回调事件
	if($.isFunction(_ambWorkflowFormObj.childrenInitParams.afterRemoveRow)){
		_ambWorkflowFormObj.childrenInitParams.afterRemoveRow(tableName,$tr);
	}
}
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
	$("#supplierCode").val(obj.code);
	$("#supplierLoginName").val(obj.code);
	$("#supplierEmail").val(obj.supplierEmail);
	//联系人
} 
//清除
function clearValue(showInputId,hiddenInputId){
	$("#"+showInputId).val("");
	$("#"+hiddenInputId).val("");
}
function testSelect(){
	$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",iframe:true,
		width:$(window).width()<1000?$(window).width()-100:1000,
		height:$(window).height()<600?$(window).height()-100:600,
		overlayClose:false,
		title:"选择物料"
	});
}
function setFullBomValue(objs){
	var obj = objs[0];
	$("#materialCode").val(obj.materielCode);
	$("#materialName").val(obj.materielName);
}
function inform(){
	var str = $("#currentActivityName").val();
	if(str==""){
		$("#a01_partName").val("");
	}
}
function createColorLight(cellvalue, options, rowObject){
	var isWarm=rowObject.taskProgress;
	if(isWarm=='OK'){
		str= '<div style="text-align:center;"><img src="${ctx}/images/green.gif"/></div>';
	}else if(isWarm=='待审核'){
		str= '<div style="text-align:center;"><img src="${ctx}/images/yellow.png"/></div>';
	}else{
		str= '<div style="text-align:center;"><img src="${ctx}/images/red.png"/></div>';	
	}
	return str;
}
var weights=0;
function setweigh(obj){
 	var m=0;
    var n=0;
	var controls = $("tr[zbtr2=true]").find('input[fieldname=averageMaterialWeight]');
	for(var i=0; i<controls.length; i++){
		if(controls[i].value==""){
			controls[i].value=0;
		}
       m=controls[i].value;
       if(isNaN(m)){
    	   m=0;
       }
       n+=parseFloat(m);
    }
	weights=parseFloat(n);
	$("#productWeight").val(weights);
	return true;
};

function subControl(a){
	if(a=="流程结束"){
		return;
	}
	var str = $("#currentActivityName").val();
	if(str!=null&&str=="供应商填写资料"){
		$("input:[name='partName']").each(function(index, obj) {
			$(obj).addClass("{required:true, messages:{required:'必填'}}");
		});
		$("input:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("select:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		
	}else if(str!=null&&str=="审核"){
		$("input:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("input:[stage='two']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("input:[stage='three']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("select:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("select:[stage='three']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
	}else{
		$("input:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("input:[stage='two']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("input:[stage='three']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("select:[stage='one']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
		$("select:[stage='three']").each(function(index, obj) {
			$(obj).attr("disabled","disabled");
		});
	}
}
function sendEmail(){
	var params = $("#supplierEmail").val();
	var formNo = $("#formNo").val();
	var str = $("#currentActivityName").val();
	var id = $("#id").val();
// 	var url = window.location.host+window.location.pathname+"?id="+id;
	var url = window.location.host+"/amb/gp/gpmaterial/input.htm?id="+id;
	if("确认信息"==str){
		$.post("${gpctx}/gpmaterial/release.htm",{par:params,formNo:formNo,url:url,id:id},function(data){
//				console.log(data);
		},'json');
	} 
}
</script>