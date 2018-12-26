<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ page import="com.norteksoft.product.util.ContextUtils" %>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function addExceptionSingle(obj){
		var str=$(obj).parent().prev().find("textarea[stage='three']");
		var name=str.attr("name");
		var prev=name.split("_")[0];
		var id=$("#"+prev+"_hsfId").val();
		if(!id){
			alert("请先保存！");
			return;
		}
// 		var no=$("#"+prev+"_exceptionNo").val();
// 		if(no){ 
// 			alert("该条信息已经发起过异常改进！");
// 			return;
// 		}
		else{
			window.open('${epmctx}/exception-single/input.htm?type=hsf&&formId='+id);
		}
	}	
	function alterException(obj){
		var str = $("#currentActivityName").val();
		if("流程结束"!=str){
			alert("该流程没结束");
			return;
		} 
		$.post('${epmctx}/entrust-hsf/input.htm?id='+obj+"&&type=hsf&&str="+str,
			function(data){
			subControl();
			window.location.href='${epmctx}/entrust-hsf/input.htm?id='+obj+"&&str="+str;
		});
	}
		 
	var orderId="";
	function supplierClick(obj){
		var aa=obj.previousElementSibling;
		orderId = aa.id.split("_")[0];
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		
		var obj = objs[0];
		$("#"+orderId+"_supplier").val(obj.name);
		console.log($("#"+orderId+"_supplier"));
		console.log(orderId);
		console.log(obj.name);
		//联系人
	} 
	var params = {};
	function setExValue(obj){
		params = {};
		orderId = obj.id.split("_")[0];
		$("#" + orderId + "_supplier");
	}
	
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkSub").position().top + $("#checkSub").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#checkSub").find("div").width(width).offset();
		var contentWidth =  $("#checkSub").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	$(document).ready(function(){
		contentResize();
	});
	function compile(taskId){
		if(taskId=="填写委托单"){
			
		}
		if(tasskId==null){
			
		}
	}
	function subControl(){
		var str = $("#currentActivityName").val();
		if(!str){
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
		}else if(str!=null&&str=="实验室排程"){
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
		
		}else if(str!=null&&str=="实验室测试"){
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[fieldname='testAfter']").each(function(index, obj) {
				$(obj).addClass("{required:true, messages:{required:'必填'}}");
			});
		}else if(str!=null&&str=="流程结束"){
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
		}else if(str!=null&&str=="填写委托单"){
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			
		}else{
			$("input:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='one']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='two']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("input:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("textarea:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});
			$("select:[stage='three']").each(function(index, obj) {
				$(obj).attr("disabled","disabled");
			});				
		}
		
	}
	var orderId="";
	function showInput(obj){
		var find=obj.nextElementSibling;
		orderId = find.id.split("_")[0];
		var chk = document.getElementById(obj.id);
		if(chk.checked == true){
			$("#"+orderId+"_else").show();
			$("select:[stage='else']").show();
		}else{
			$("#"+orderId+"_else").hide();
		}
	}
	function setTestItemValues(obj){
		var oldValue = $("#"+obj.parentElement.lastElementChild.id).val();
		if(obj.checked == true){
			if(oldValue.length==0){
				oldValue = obj.value;
			}else{
				oldValue += "," + obj.value;
			}
			$("#"+obj.parentElement.lastElementChild.id).val(oldValue);
		}else{
			oldValue=oldValue.split(obj.value);
			$("#"+obj.parentElement.lastElementChild.id).val(oldValue);
		}
		var oldValue = $("#"+obj.parentElement.lastElementChild.id).val();
		var oldArr = oldValue.split(",");
		var newValue = "";
		for(var i=0;i<oldArr.length;i++){
			if(newValue.length==0){
				newValue = oldArr[i];
			}else{
				newValue += ","+oldArr[i];
			}
		}
		$("#"+obj.parentElement.lastElementChild.id).val(newValue);
	}
	function printPage(){
		var url = '${epmctx}/entrust-hsf/print-page.htm?&formNo=${formNo}';
		window.open(url);
	}
	var weights=0;
	function setweigh(obj){
	 	var m=0;
	    var n=0;
		var controls = $("tr[zbtr1=true]").find('input[fieldname=amount]');
		for(var i=0; i<controls.length; i++){
	       m=parseInt(controls[i].value);
	       if(isNaN(m)){
	    	   m=0;
	       }
	       n+=parseFloat(m);
	    }
		weights=parseFloat(n);
		$("#quantity").val(weights);
		return true;
	};
	function isTestResult(){
		var result = $("tr[zbtr1=true]").find('select[fieldname=testAfter]');
		for(var i=0; i<result.length; i++){
			if(result[i].value==''){
				$("#textResult").val("");
			}else if(result[i].value=='NG'){
				$("#textResult").val("不合格");
				return;
			}else{
				$("#textResult").val("合格");
			}
		}
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
		$("#" +flag+ "_factoryClassify").val($("#factoryClassify").val());
	}
</script>