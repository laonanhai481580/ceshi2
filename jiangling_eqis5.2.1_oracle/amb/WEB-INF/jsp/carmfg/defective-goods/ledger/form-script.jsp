<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<script type="text/javascript">
	function showMessage(msg){
		clearTimeout(window.msgTimeout);
		$("#message").html(msg).show();
		clearMessage();		
	}
	function clearMessage(){
		window.msgTimeout = setTimeout(function(){
			$("#message").html("");
		},3000);
	}
	function initForm(){
		$("input[name^='inspectDate']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		$("input[name='completeDate']").datepicker({changeMonth:true,changeYear:true});
		$("input[name='reportDate']").datepicker({changeMonth:true,changeYear:true});
		$("input[name='qualitySignDate']").datepicker({changeMonth:true,changeYear:true});
		
		$(":checkbox[name=defectType]").bind("click", singleCheck)
		$(":checkbox[name=qualityOpinion]").bind("click", singleCheck);
		$(":checkbox[name=concessionResult]").bind("click", singleCheck);
		$(":checkbox[name=need8d]").bind("click", singleCheck)
		.bind("click",need8DChange);
		need8DChange();
		$(":checkbox[name=concessionResult]").bind("click", singleCheck);
		$(":checkbox[name=isAgreeQO]").bind("click", singleCheck);
		$(":checkbox[name=analyseDepartment]").bind("click", singleCheck);
		$(":checkbox[name=analyseDepartmentOption]").bind("click", singleCheck)
		.bind("click",analyseDepartmentOptionChange);
		analyseDepartmentOptionChange();
		showAttachment("reviewAttachment","showAttachment");
		showAttachment("inspectorAttachment","showInspectorAttachment");
		showAttachment("qualityEngineerAttachment","showQualityEngineerAttachment");
		addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
		var attachmentMap = {
			reviewAttachment : true,
			inspectorAttachment :true,
			qualityEngineerAttachment:true,
		};
		var fieldPermission = ${fieldPermission};
		var editDetailItemReadonly =false;
		if(fieldPermission.length==1&&fieldPermission[0].controlType=='allReadolny'){
			$(".deptTab").removeAttr("onclick");
			editDetailItemReadonly = true;
			//$(".composingItemTr").find(":input").attr("disabled","disabled");
			$("a").removeAttr("onclick");
			$("button[uploadBtn=true]").hide();
			$(":input[name]").attr("disabled","disabled");
			$("button[start8D=true]").hide();
		}
		for(var i=0;i<fieldPermission.length;i++){
			var obj=fieldPermission[i];
			if(obj.readonly=='true'){
				if(obj.name == 'inspectionDept'){
					$(".deptTab").removeAttr("onclick");
					$(".selectDept").removeAttr("onclick");
				}
				var $obj = $(":input[name="+obj.name+"]");
				if($obj.attr("type") != 'hidden'){
					$obj.attr("disabled","disabled");
				}else{
					if(attachmentMap[obj.name]){
						$obj.closest("td").find("button[uploadBtn=true]").hide();
					}
				}
				if(obj.name=="editDetailItem"){
					editDetailItemReadonly = true;
				}else if(obj.name=="editIdentifier"){
					$(".composingItemTr").find(":input").attr("disabled","disabled");
					$(".composingItemTr").find("a").removeAttr("onclick");
				}
			}
		};
		
		if(editDetailItemReadonly){
			$(".detailItemTr").find(":input").attr("disabled","disabled");
			$(".detailItemTr").find("a").hide();
			$(".detailItemTr").find("input").each(function(index,obj){
				var val = obj.value;
				$(obj).hide().after(val);
			});
		};
	}
	function need8DChange(){
		if($("input[name='need8d']:checked").val()=="0"){
			$("#show8D").show();
		} else{
			$("#show8D").hide();
		}
	}
	function analyseDepartmentOptionChange(){
		if($("input[name=analyseDepartmentOption]:checked").val()=="让步接收"){
			$("#concessionOrganizeNameDiv").show();
			$("#concessionOrganizeNameDiv").find(":input").removeAttr("disabled");
		} else{
			$("#concessionOrganizeNameDiv").hide();
			$("#concessionOrganizeNameDiv").find(":input").attr("disabled","disabled");
		}
	}
	function deptTypeSelect(obj){
		var hisVal = $(obj).closest("div").find("a.deptSel").html();
		window[hisVal] = $("#inspectionDept").val();
		$(obj).closest("div").find("a").removeClass("deptSel");
		$(obj).addClass("deptSel");
		var newVal = $(obj).html();
		$("#inspectionDept").val(window[newVal]);
	}
	function selectInspectionDept(id){
		if($("a.deptSel").html()=="内部"){
			selectObj('选择单位','inspectionDept','DEPARTMENT_TREE');
		}else{
			supplierClick();
		}
	}
	function supplierClick(){
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm?state=合格",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#inspectionDept").val(obj.name);
	} 
	function selectObj(title,obj,treeType,treeValue,hiddenInputId){
		var acsSystemUrl = "${ctx}";
		inputId = obj&&obj.id?obj.id:obj;
		popTree({ title :title,
			innerWidth:'400',
			treeType:treeType,
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:hiddenInputId?hiddenInputId:inputId,
			showInputId:inputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}

	//选择质量成本
	var composingObj;
	function selectComposing(obj){
		composingObj = obj;
		var url = '${costctx}/common/composing-select.htm';
 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
 			overlayClose:false,
 			title:"选择质量成本"
 		});
	}
	function setFullComposingValue(datas){
		$(composingObj).closest("tr").find(":input[fieldName=code]").val(datas[0].code);
		$(composingObj).closest("tr").find(":input[fieldName=name]").val(datas[0].name);
	}
 	//计算费用
 	function caculateAmount(){
 		var val = 0;
 		$("input","#defective-goods-composing-table-body").each(function(index,obj){
 			if(obj.id){
 				if(!isNaN(obj.value)&&!isNaN(parseFloat(obj.value))){
					val += parseFloat(obj.value);
				}
 			}
 		});
 		$("#totalComposingFee").html(val);
 	}
 	//选择BOM零部件
 	var bomRowId;
 	var selProductObj = null;
 	function selectComponentCode(obj){
 		selProductObj = obj;
 		var url = '${mfgctx}/common/product-bom-select.htm';
 		$.colorbox({href:url,iframe:true, innerWidth:650, innerHeight:400,
 			overlayClose:false,
 			title:"选择BOM零部件"
 		});
 	}
 	function setFullBomValue(datas){
 		$(selProductObj).closest("tr").find(":input[fieldName=productCode]").val(datas[0].code);
 		$(selProductObj).closest("tr").find(":input[fieldName=productName]").val(datas[0].name);
 		$(selProductObj).closest("tr").find(":input[fieldName=modelSpecification]").val(datas[0].model);
 	}
 	
 	//获取不良项目和 成本
	function addRowHtml(totalObj,classObj,obj){
 		var tr = $(obj).closest("tr");
 		var clonetr = tr.clone(false);
 		tr.after(clonetr);
 		var total = $("#"+totalObj);
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
 			obj=$(obj);
 			var name=obj.attr("name").split("_")[0];
 			obj.attr("id",name+"_"+num).val("");
 			obj.attr("name",name+"_"+num);
 			if(name=="inspectDate"){
 			 	obj.removeAttr("class").datepicker({changeYear:'true',changeMonth:'true'});
 			 	obj.addClass("{required:true,messages:{required:'必填'}}");
 			}else if(name=="productCode"){
 				obj.click(function(){selectComponentCode(this);});
 			}else if(name=="name"){
 				obj.click(function(){selectComposing(this);});
 			}
 		});
 	  	total.val(parseInt(num)+1);
 	  	total.siblings("span").text(parseInt(num)+1);
  		$("."+classObj).each(function(index, obj){
  			$($(obj).children("td")[1]).text(parseInt(index)+1);
  		});
 	}
 	function removeRowHtml(totalObj,classObj,obj){
 		var total = $("#"+totalObj);
		var tr=$(obj).closest("tr");
		var pre=tr.prev("tr").attr("class");
		var next=tr.next("tr").attr("class");
		if(next==classObj){
		 	tr.remove();
	 	  	total.siblings("span").text(parseInt(total.val())-1);
		 	total.val(parseInt(total.val())-1);
		}else if(pre==classObj){
			tr.remove();
	 	  	total.siblings("span").text(parseInt(total.val())-1);
			total.val(parseInt(total.val())-1);
		}else{
			alert('至少要保留一行');
			total.val(1);
	 	  	total.siblings("span").text(1);
		}
  		$("."+classObj).each(function(index, obj){
  			$($(obj).children("td")[1]).text(parseInt(index)+1);
  		});
 	}
	function getDetailItems(){
		var infovalue="";
		$(".detailItemTr").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		$("#detailItems").val("["+infovalue.substring(1)+"]");
	}
	function getComposingItems(){
		var infovalue="";
		$(".composingItemTr").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		$("#composingItems").val("["+infovalue.substring(1)+"]");
	}
	function getTdItem(obj){
		var value="";
		$(obj).find(":input[fieldName]").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("fieldName")+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId
		});
	}
	function showAttachment(hiddenId,showId){
		$.parseDownloadPath({
			showInputId : showId,
			hiddenInputId : hiddenId
		});
 	}
	
	function singleCheck(event){ 
		var newobj=event.currentTarget; 
		var name=newobj.name;
		$(":checkbox[name="+name+"]").each(function(index,obj){  
		 	if(obj!=newobj){
			  	$(obj).removeAttr("checked");
		  	} 
		});
	}
	//开始8D
	function start8D(){
		var need8dNo = $(":input[name=need8dNo]").val();
		if(need8dNo){
			alert("已经触发了8D流程!");
			return;
		}
		$.colorbox({href:"${improvectx}/8d-report/launch-input.htm?taskId=${taskId}&source=defective",iframe:true,
			width:$(window).width()<1100?$(window).width()-20:1100,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"提交品质改善(8D)报告书"
		});
	}
	function completeStart8D(form8dNo){
		$.colorbox.close();
		$(":input[name=need8dNo]").val(form8dNo);
	}
</script>
