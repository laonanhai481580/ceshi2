<%@page import="com.ambition.si.entity.SiCheckItem"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<c:set var="actionBaseCtx" value="${sictx}/check-inspection" />
<script type="text/javascript">	
	var rowTable = 1;
	$(document).ready(function(){
		//初始化表单元素
			var len = $('td[target="rowNum"]').length;
			var rownum = $('td[target="rowNum"]').get(len - 1);
			rowTable = parseInt(rownum.innerHTML);
			$(":input[isDate=true]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
			children:{
				siCheckItems:{
					entityClass:"<%=SiCheckItem.class.getName()%>";
				}
			}
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			conclusionChange();
			var canEdit=$("#canEdit").val();
			if(canEdit=="false"){
				$("#inputform").find(":input").each(function(index, obj){
					$(obj).attr("disabled","disabled");
				});
			}
	});
	//添加行
	function addRowHtml(obj) {
		var tr = $(obj).parent("td").parent("tr");
		var clonetr = tr.clone(false);
		tr.after(clonetr);
		rowTable++;
		clonetr.find(":input").each(
				function(index, obj) {
					obj = $(obj);
					var id = obj.attr("name");
					obj.attr("id",id+ "_" + rowTable);
				}).val("");
		clonetr.find("[id^='showAttachment']").each(
			function(index, obj) {
				var str= $(obj).attr("id").split("_")[0];
				 $(obj).attr("id",str + "_" + rowTable);
				 $(obj).html("");
			}
		);
		$("input:[isDate=true]").removeClass();
		$("input:[isDate=true]").datepicker({changeYear:true,changeMonth:true,showButtonPanel: true});
		$(":input[uploadbtn=\"true\"]").each(function(index, obj){
			var index = $(obj).next("[name=attachment]").attr("id").split("_")[1];
			$(obj).removeAttr("onclick");
			$(obj).click(function(){
				uploadFiles("showAttachment_"+index,"attachment_"+index);
			});
		});
		updateRowNum();
	}
	
	//选择不良类别
	var idIndex=null;
	function defectionCodeClick(obj){
		idIndex = obj.id;
		var url = '${sictx}/base-info/defection-code/defection-code-multi-select.htm';
			$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择不良项目"
			});
	}
	function setDefectionValue(datas){
		var idFirst = idIndex.split("_")[1];
		$("#defectionCodeNo_"+ idFirst).val(datas[0].key);
		$("#defectionCodeName_"+ idFirst).val(datas[0].value);
		$("#defectionTypeNo_"+ idFirst).val(datas[0].defectionTypeNo);
		$("#defectionTypeName_"+ idFirst).val(datas[0].defectionTypeName);
 	}
	function updateRowNum() {
		$("tr[name=siCheckItems]").each(function(index, obj) {
			$(obj).find("td[target]").html(index + 1);
		});
	}
	//删除行
	function removeRowHtml(obj) {
		var tr = $(obj).parent("td").parent("tr");
		var pre = tr.prev("tr").attr("name");
		var next = tr.next("tr").attr("name");
		if (next != undefined) {
			rowTable--;
			tr.remove();
		} else if (pre != undefined) {
			rowTable--;
			tr.remove();
		} else {
			rowTable = 1;
			alert('至少要保留一行');
		}
		updateRowNum();
	}
	function saveForm() {
		if($("#inputform").valid()){
			unRateChange();
			var stockAmount=$("#stockAmount").val();
			if(isNaN(stockAmount)){
				alert("投入数必须为数字！");
				$("#stockAmount").focus();
				return;
			}	
			var passLotAmount=$("#passLotAmount").val();
			var inspectionLotAmount=$("#inspectionLotAmount").val();
			if(passLotAmount!=""&&inspectionLotAmount!=""){
				if(isNaN(passLotAmount)){
					alert("Pass Lot数必须为数字！");
					$("#passLotAmount").focus();
					return;
				}
				if(isNaN(inspectionLotAmount)){
					alert("检验Lot数必须为数字！");
					$("#inspectionLotAmount").focus();
					return;
				}
				if((passLotAmount-0)>(inspectionLotAmount-0)){
					alert("Pass Lot数不能大于检验Lot数！");				
					$("#passLotAmount").focus();
					return;
				}
			}
			var item=getScrapItems();
			$("#zibiao").val(item);
			$("#inputform").attr("action","${actionBaseCtx}/save.htm");
			$("#inputform").submit();
			$("#message").html("正在保存，请稍候... ...");
		}else{
			var error = $("#inputform").validate().errorList[0];
			$(error.element).focus();
		}
	}
	function unAmountChange(){		
		var appearanceUnAmount=0,sizeUnAmount=0,functionUnAmount=0;
		$("tr[zbtr1=true]").each(function(index,obj){
			var defectionTypeName=$(obj).find(":input[name='defectionTypeName']").val();
			if(defectionTypeName=="外观"){
				var value=$(obj).find(":input[name='value']").val();
				appearanceUnAmount=(value-0)+(appearanceUnAmount-0);
			}
			if(defectionTypeName=="尺寸"){
				var value=$(obj).find(":input[name='value']").val();
				sizeUnAmount=(value-0)+(sizeUnAmount-0);
			}
			if(defectionTypeName=="功能"){
				var value=$(obj).find(":input[name='value']").val();
				functionUnAmount=(value-0)+(functionUnAmount-0);
			}
		});
		$("#appearanceUnAmount").val(appearanceUnAmount);		
		$("#sizeUnAmount").val(sizeUnAmount);				
		$("#functionUnAmount").val(functionUnAmount);
		unRateChange();
	}
	function unRateChange(){	
		var appearanceInspectionAmount = $("#appearanceInspectionAmount").val();
		var sizeInspectionAmount = $("#sizeInspectionAmount").val();
		var functionInspectionAmount = $("#functionInspectionAmount").val();
		var appearanceUnAmount = $("#appearanceUnAmount").val();
		var sizeUnAmount = $("#sizeUnAmount").val();
		var functionUnAmount = $("#functionUnAmount").val();
		if(isNaN(appearanceInspectionAmount)){
			alert("外观检验数必须为数字！");
			$("#appearanceInspectionAmount").val("");
			$("#appearanceInspectionAmount").focus();
			return;
		}
		if(appearanceInspectionAmount){	
			if((appearanceUnAmount-0)>(appearanceInspectionAmount-0)){
				alert("外观不良数不能大于外观检验数！");
				return;
			}
			if(appearanceUnAmount&&appearanceInspectionAmount&&(appearanceInspectionAmount-0)>0){
				var appearanceRate = (appearanceUnAmount/appearanceInspectionAmount*100).toFixed(2);
				$("#appearanceAmountRate").val(appearanceRate);
			}else{
				$("#appearanceAmountRate").val("0.0");
			}
		}
		if(isNaN(sizeInspectionAmount)){
			alert("尺寸检验数必须为数字！");
			$("#sizeInspectionAmount").val("");
			$("#sizeInspectionAmount").focus();
			return;
		}
		if(sizeInspectionAmount){
			if((sizeUnAmount-0)>(sizeInspectionAmount-0)){
				alert("尺寸不良数不能大于尺寸检验数！");
			}
			if(sizeUnAmount&&sizeInspectionAmount&&(sizeInspectionAmount-0)>0){
				var sizeRate = (sizeUnAmount/sizeInspectionAmount*100).toFixed(2);
				$("#sizeAmountRate").val(sizeRate);
			}else{
				$("#sizeAmountRate").val("0.0");
			}			
		}
		if(isNaN(functionInspectionAmount)){
			alert("功能检验数必须为数字！");
			$("#functionInspectionAmount").val("");
			$("#functionInspectionAmount").focus();
			return;
		}
		if(functionInspectionAmount){
			if((functionUnAmount-0)>(functionInspectionAmount-0)){
				alert("功能不良数不能大于功能检验数！");
				return;
			}
			if(functionUnAmount&&functionInspectionAmount&&(functionInspectionAmount-0)>0){
				var functionRate = (functionUnAmount/functionInspectionAmount*100).toFixed(2);
				$("#functionAmountRate").val(functionRate);
			}else{
				$("#functionAmountRate").val("0.0");
			}
			
		}
	}
	
	function lrrChange(){
		var passLotAmount=$("#passLotAmount").val();
		var inspectionLotAmount=$("#inspectionLotAmount").val();
		if(passLotAmount!=""&&inspectionLotAmount!=""){
			if((passLotAmount-0)>(inspectionLotAmount-0)){
				alert("Pass Lot数不能大于检验Lot数！");				
				$("#passLotAmount").focus();
				return;
			}
			var rejectLotAmount=(inspectionLotAmount-0)-(passLotAmount-0);
			$("#rejectLotAmount").val(rejectLotAmount);
			var lrrRate=rejectLotAmount*100/inspectionLotAmount;
			$("#lrrRate").val(lrrRate.toFixed(2));
		}		
	}
	function getScrapItems(){
		var infovalue="";
		$("tr[zbtr1=true]").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		var item ="["+infovalue.substring(1)+"]";
		return item;
	}
	function getTdItem(obj){
		var value="";
		$(obj).find(":input").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("name").split("_")[0]+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}

	//选择部门
	function selectDep(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择部门',
			innerWidth:'400',
			treeType:'DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj,
			showInputId:obj,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}
		});
	}
	function inspectorClick(title,showInputId,hiddenInputId,multiple,defaultTreeValue){
		var acsSystemUrl = '${ctx}';
		popZtree({
	        leaf: {
	            enable: false,
	            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
	        },
	        type: {
	            treeType: "MAN_DEPARTMENT_TREE",
	            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
	            noDeparmentUser:true,
	            onlineVisible:true
	        },
	        data: {
	            treeNodeData:"id,loginName,name",
	            chkStyle:"",
	            chkboxType:"{'Y':'ps','N':'ps'}",
	            departmentShow:""
	        },
	        view: {
	            title: title,
	            width: 400,
	            height:400,
	            url:acsSystemUrl
	        },
	        feedback:{
	            enable: true,
	            showInput:showInputId,
	            showThing:"{'name':'name'}",
	            hiddenInput:hiddenInputId,
	            hiddenThing:"{'id':'id'}",
	            append:false
	        },
	        callback: {
	            onClose:function(api){
	                if(hiddenInputId){
	                    var currentClickNodeData = api.single.getCurrentClickNodeData();
	                    var user = $.parseJSON(currentClickNodeData);
	                    $("#"+hiddenInputId).val(user.loginName);
	                }
	            }
	        }
	    });
	}
	//上传附件
	function uploadFiles(showId,hiddenId){
		$.upload({
			appendTo : '#opt-content',
			showInputId : showId,
			hiddenInputId : hiddenId
		});
	}
	//加载附件
	function parseDownloadFiles(arrs){
		for(var i=0;i<arrs.length;i++){
			var hiddenInputId = arrs[i].hiddenInputId;
			var showInputId = arrs[i].showInputId;
			var files = $("#"+hiddenInputId).val();
			if(files&&files.length>0){
		 		$.parseDownloadPath({
					showInputId : showInputId,
					hiddenInputId : hiddenInputId
				});
			}
		}
	}
	function conclusionChange(obj){
		var appearanceConclusion=$("#appearanceConclusion").val();
		var sizeConclusion=$("#sizeConclusion").val();
		var functionConclusion=$("#functionConclusion").val();
		if(appearanceConclusion=="合格"&&sizeConclusion=="合格"&&functionConclusion=="合格"){
			$("#inspectionConclusion").val("合格");
		}else{
			$("#inspectionConclusion").val("不合格");
		}
	}
	/**
	导出表单
	*	
	*/
	function exportForm(){
		var id = '${id}';
		if(!id){
			alert("请先保存!");
			return;
		}
		window.location.href = '${actionBaseCtx}/export-form.htm?id=${id}';
	}
</script>