<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object> 
	<script type="text/javascript">
	var LODOP; //声明打印的全局变量 
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
	var topMenu ='';
	$(document).ready(function(){
		$("#scroll").bind("scroll",function(){
			$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#checkItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
		$(":input[filedName=manufactureTime]").datepicker({changeMonth:true,changeYear:true});
		$('#inspectionDate').datetimepicker({changeYear:'true',changeMonth:'true'});
		$("#stockAmount").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				loadCheckItems();
			}
		});
		autoSave();
		initForm();
// 		$("#inspectionForm").validate({});
		bindCustomEvent();
		caculateTotalAmount();
		contentResize();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		$.parseDownloadPath({
			showInputId : 'showInspectionDatas',
			hiddenInputId : 'inspectionDatas'
		});
		var attachments=[];
		$("td[name=attachmentSon").each(function(index,obj){
			attachments.push({showInputId:'a'+(index+1)+'_showAttachment',hiddenInputId:'a'+(index+1)+'_attachmentFiles'});//子表上传附件
		});
		attachments.push({showInputId:'iqcInspectionDatas',hiddenInputId:'fileAll'});
		parseDownloadFiles(attachments);
		if("${reportState}" != "<%=MfgCheckInspectionReport.STATE_DEFAULT%>"
			&&"${reportState}" != "<%=MfgCheckInspectionReport.STATE_RECHECK%>"){
			$("button").attr("disabled","disabled");
			$(":input").attr("disabled","disabled");
			$("a").removeAttr("onClick");
			$("#message").html("<font color=red>检验状态为【${reportState}】,不能修改!</font>");
			$(".opt-btn").find("button").attr("disabled","");
			return;
		}
		setTimeout(function(){
			$("#message").html("");
		},3000);
		//自动填写,物料编码
		$("#machineNo11")
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisSearch = $(this).attr("hisSearch");
				if(this.value != hisSearch){
					$(this).attr("hisSearch",this.value);
// 					loadCheckItems();
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200,
			source: function( request, response ) {
				response([{label:'数据加载中...',value:''}]);
				$.post("${mfgctx}/common/search-machine-no.htm",{searchParams:'{"machineNo":"'+request.term+'"}',label:'machineNo'},function(result){
					response(result);
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.value){
					var his = $(":input[name=machineNo]").attr("hisValue");
					$(":input[name=machineNo]").val(ui.item.code);
					$(":input[name=machineNo]").val(ui.item.code).attr("hisValue",ui.item.code);
					$(":input[name=machineName]").val(ui.item.value);
					$(":input[name=machineName]").val(ui.item.value).attr("hisValue",ui.item.value);
				}
				return true;
			},
			close : function(event,ui){
				var val = $(":input[name=machineNo]").val();
				if(val){
					var hisValue = $(":input[name=machineNo]").attr("hisValue");
					if(val != hisValue){
						$(":input[name=machineNo]").val(hisValue);
					}
				}/* else{
					$(":input[name=machineNo]").val("").attr("hisValue","").attr("hisSearch",'');
					$(":input[name=machineName]").val("");
				} */
			}
		});
	});

	function autoSave(){
		setTimeout(function(){
			/* submitForm('${mfgctx}/inspection/made-inspection/save.htm',false); */
			alert("为防止数据丢失，请先暂存表单！");
			autoSave();
		},1000*60*15);
	}
	function initForm(){
		$("input[name^='inspectDate']").each(function(index,obj){
			$(obj).datepicker({changeMonth:true,changeYear:true});
		});
		addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
		var attachmentMap = {
			reviewAttachment : true,
			inspectorAttachment :true,
			qualityEngineerAttachment:true,
		};
		var fieldPermission = ${fieldPermission};
		var editDetailItemReadonly =false;
		if(fieldPermission.length==1&&fieldPermission[0].controlType=='allReadolny'){
			$("a").removeAttr("onclick");
			$("button[uploadBtn=true]").hide();
			$(":input[name]").attr("disabled","disabled");
		}
		if("${reportState}" == "<%=MfgCheckInspectionReport.STATE_DEFAULT%>"||"${reportState}" == "<%=MfgCheckInspectionReport.STATE_RECHECK%>"){
			$("#processingResult").attr("disabled","disabled");
			$("#auditText").attr("disabled","disabled");
		}
		for(var i=0;i<fieldPermission.length;i++){
			var obj=fieldPermission[i];
			if(obj.readonly=='true'){
				var $obj = $(":input[name="+obj.name+"]");
				if($obj.attr("type") != 'hidden'){
					$obj.attr("disabled","disabled");
				}else{
					if(attachmentMap[obj.name]){
						$obj.closest("td").find("button[uploadBtn=true]").hide();
					}
				}
				if(obj.name=="supplierMessageStr"){
					$(".mfgSupplierMessagesTr").find(":input").attr("disabled","disabled");
					$(".mfgSupplierMessagesTr").find("a").removeAttr("onclick");
				}else if(obj.name=="checkedItemStr"){
					$("#checkItemsParent").find(":input").attr("disabled","disabled");
					$("#checkItemsParent").find("a").removeAttr("onclick");
				}
			}
		};
	}
	function choiceWaitCheckItem(){
		var stockAmountobj = $("#stockAmount").val();
		var inspectionDate = $("#inspectionDate").val();
		var workProcedure = $("#workProcedure").val().replace("+","%2B");
		var businessUnitCode=$(":input[name=businessUnitName]").val();
		var checkBomCode = $("#machineNo").val();
		var inspectionPointType=$("#inspectionPointType").val();
		var url="id=${id}&businessUnitCode="+businessUnitCode+"&checkBomCode="+checkBomCode+"&inspectionDate="+inspectionDate+"&stockAmount="+stockAmountobj+"&workProcedure="+workProcedure+"&inspectionPointType=" +inspectionPointType;
		if(checkBomCode==''||workProcedure==''||stockAmountobj==''){
			return;
		}
		$.colorbox({href:"${mfgctx}/inspection/made-inspection/wait-checked-items.htm?"+url,
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择检验项目"
 		});
	}
	function getTempItem(){
		var itemName="";
		//根据是否有检验员，来判定该项目是否有提交过
		$(":input[fieldName=inspector]").each(function(index,obj){
			var objval=$(obj).val();
			if(objval==""){
				$(obj).prev().val("未领取");
				$("td[tdShow=false]").hide();
			}else{
				itemName+=$(obj).attr("inspector")+",";
			}
		});
		return itemName;
	}
	function completeItem(datas,parentColspan){
		$("td[tdShow=false]").hide();
		var index=0;
		$("td[tdShow=false]").hide();
		var index=0;
		for(var pro in datas){
			index++;
			$("td[name="+datas[pro]+"]").html($("td[name][tdShow=true]").length+index);
			$("td[itemName="+datas[pro]+"]").show();
			$(":input[itemstatus="+datas[pro]+"]").val("已领取");
			$(":input[canSee="+datas[pro]+"]").attr("class",'{required:true,number:true}');
		}
	}
	function submitForm(url,b){
		if(b){
			if($("#inspectionForm").valid()){
//	 			var unqualifiedAmount=$("#unqualifiedAmount").val();
//	 			var amount=$(".amount");
//	 			var total=0;
//	 			for(var i=0;i<amount.length;i++){
//	 				var number=amount[i].value;
//	 				total= parseInt(total)+ parseInt(number);
//	 			}
//	 			if(total<unqualifiedAmount){
//	 				alert("不良项目数量不能小于不良数!");
//	 				return ;
//	 			}
				getMfgSupplierMessages();
				$('#inspectionForm').attr('action',url);
				$("#message").html("<b>数据保存中,请稍候... ...</b>");
				$(".opt-btn .btn").attr("disabled",true);
				$('#inspectionForm').submit();
			}else{
				var error = $("#inspectionForm").validate().errorList[0];
				$(error.element).focus();
			}
		}else{
			getMfgSupplierMessages();
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}
		
	}

	function selectInspectionPoint(obj){
		window.location.href = encodeURI('${mfgctx}/inspection/first-inspection/input.htm?inspectionPoint=' + obj.value);
		return;
	}
	function checkBomClick(obj){
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function setFullBomValue(datas){
		var his = $(":input[name=checkBomCode]").val();
		$(":input[name=checkBomCode]").val(datas[0].materielCode);
		$(":input[name=checkBomName]").val(datas[0].materielName);
		$(":input[name=checkBomName]").closest("td").find("span").html(datas[0].materielName);
		$(":input[name=checkBomModel]").val(datas[0].model);
		$(":input[name=checkBomModel]").closest("td").find("span").html(datas[0].model);
		if(datas[0].code != his){
			loadCheckItems();
		}
 	}
	
	function callback(){
		showMsg();
	}
	
	 //选择检验人员
 	function selectObj(title,hiddenInputId,showInputId,treeValue){
 		var data = {
 				treeNodeData: "loginName,name",
 				chkboxType:"{'Y' : 'ps', 'N' : 'ps' }",
 				departmentShow:''
 			};
 		var zTreeSetting={
 				leaf: {
 					enable: false,
 					multiLeafJson:  "[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\",\"code\":\"code\",\"cardNumber\":\"cardNumber\"}','showValue':'{\"name\":\"name\"}'}]"
 				},
 				type: {
 					treeType: "MAN_DEPARTMENT_TREE",
 					showContent:"[{'id':'id','code':'code','name':'name'}]",
 					noDeparmentUser:false,
 					onlineVisible:true
 				},
 				data: data,
 				view: {
 					title: "用户树",
 					width: 300,
 					height:400,
 					url:webRoot
 				},
 				feedback:{
 					enable: true,
 			                showInput:"showInput", 
 			                showThing:"{'name':'name'}",
 			                hiddenInput:"hiddenInput",
 			                hiddenThing:"{'loginName':'loginName'}",
 			                append:false
 				},
 				callback: {
 					onClose:function(api){
 							$("#"+hiddenInputId).val(ztree.getLoginName());
 							$("#"+showInputId).val(ztree.getName());							
 					}
 				}	
			};
		    popZtree(zTreeSetting);
	}
 	function loadPlantParameters(){
 		var workProcedure = $("#workProcedure").val();
 		var machineNo=$("#machineNo").val();
 		if(!machineNo||!workProcedure){
 			
 		}else{
 			var params = {
 					machineNo : machineNo,
 					workProcedure : workProcedure,
 					planFlagIds : $(":input[name=planFlagIds]").val()
 				};
 			var url = "${mfgctx}/inspection/made-inspection/plant-parameter-items.htm";
			$("#plantParameterItems").find("div").load(url,params,function(){
// 				$("#inspectionForm").validate({});
				$(".plantItemsClass input").bind("change",resultPlantChange);
				var contentWidth =  $("#plantParameterItems").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();
			});
 		}
 	}
 	
 	function resultPlantChange(){
 		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find(":input[fieldName=parameterMax]").val(),minlimit = parentTr.find(":input[fieldName=parameterMin]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!this.value){
					$(this).css("color","black").attr("color","black");
				}else{
					var val = parseFloat(this.value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(val>=parseFloat(minlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else{//有上限和下限
						if(val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}
				}
				var count = 0;
				$(this).parent().children().each(function(index,obj){
					if($(obj).attr("color")=='red'){
						count++;
					}
				});
				if(count>0){
					$(this).parent().next().find("input").val("NG");
					$(this).parent().next().find("span").attr("style","color:red").html("不合格");
				}else{
					$(this).parent().next().find("input").val("OK");
					$(this).parent().next().find("span").html("合格");
				}
			}
		}
 	}
 	
	function loadCheckItems(){
		var inspectionDate = $("#inspectionDate").val();
		var workProcedure = $("#workProcedure").val();
		var checkBomCode = $("#machineNo").val();
		var stockAmount = $("#stockAmount").val();
		var inspectionAmount=$("#inspectionAmount").val();
		var inspectionPointType=$("#inspectionPointType").val();
		loadPlantParameters();
	if(!checkBomCode||!stockAmount||!inspectionDate||!workProcedure){
			
		}else{
			var params = {
				workProcedure : workProcedure,
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				inspectionDate : inspectionDate,
				inspectionPointType:inspectionPointType,
				flagIds : $(":input[name=flagIds]").val()
			};
			$("#checkItemsParent").find(":input[fieldName=countType]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=checkItemName]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=results]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[results=true]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
				params[obj.name] = $(obj).val();
			});
			$("#scroll").hide();
			$("#checkItemsParent").find("div").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${mfgctx}/inspection/made-inspection/check-items.htm";
			$("#checkItemsParent").find("div").load(url,params,function(){
// 				$("#inspectionForm").validate({});
				bindCustomEvent();
				//更新不合格数和合格数
				$(".checkItemsClass").each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
				var contentWidth =  $("#checkItemsParent").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();
			});
		}
	}
	function addResultInput(obj,checkItemName){
		var tdLen = $(obj).parent().find("input").length;
		if(tdLen==80){
			alert("最多能添加80项！");
		}else{
			var html="";
			if(tdLen%10==0){
				html+="<br/>";
			}
			html+= "<input color=\"black\" style=\"width:33px;float:left;margin-left:2px;\" title=\""+checkItemName+"样品"+(tdLen+1)+"\" name=\"result"+(tdLen+1)+"\"  class=\"{number:true,min:0}\"></input>";
			$(obj).before(html);
			//更新顶部的宽度
			updateCheckItemsHeaderWidth();
			$(obj).parent().find("input").last().bind("change",resultChange);
		}
	}
	function removeResultInput(obj){
		$(obj).parent().find("input:last").remove();
		//更新顶部的宽度
		updateCheckItemsHeaderWidth();
		//初始化滚动条
		initScrollDiv();
	}
	//更新顶部的宽度
	function updateCheckItemsHeaderWidth(){
		var maxLen = 10;
// 		$(".checkItemsClass").each(function(index,obj){
// 			var len = $(obj).find("input").length;
// 			if(len>maxLen){
// 				maxLen = len;
// 			}
// 		});
		$("#checkItemsHeader").width(maxLen*38+70);
		//初始化滚动条
		initScrollDiv();
	}
	//添加各种事件
	function bindCustomEvent(){
		//不合格数
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").bind("change",function(){
			if(!isNaN(this.value)){
				var inspectionAmount = $(this).closest("tr").find(":input[fieldName=inspectionAmount]").val();
				if(!isNaN(inspectionAmount)){
					inspectionAmount = parseFloat(inspectionAmount);
					var amount = inspectionAmount - parseFloat(this.value);
					$(this).closest("tr").find(":input[fieldName=qualifiedAmount]").val(amount<0?0:amount);
					var rate=amount*100/inspectionAmount;
					$(this).closest("tr").find(":input[fieldName=qualifiedRate]").val(rate.toFixed(2));
				}
			}
			unqualifiedAmountChange(this);
		});
		//合格数
		$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").bind("change",function(){
			if(!isNaN(this.value)){
				var inspectionAmount = $(this).closest("tr").find(":input[fieldName=inspectionAmount]").val();
				if(!isNaN(inspectionAmount)){
					inspectionAmount = parseFloat(inspectionAmount);
					var amount = inspectionAmount - parseFloat(this.value);
					$(this).closest("tr").find(":input[fieldName=unqualifiedAmount]").val(amount<0?0:amount);
					var field = $(this).closest("tr").find(":input[fieldName=unqualifiedAmount]");
					if(field.length>0){
						unqualifiedAmountChange(field[0]);
					}
				}
			}
			/**if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				caculateTotalAmount();
			}*/
		});
		//计量的值不符合范围时的事件
		$(".checkItemsClass input").bind("change",resultChange);
	}
	function unqualifiedAmountChange(obj){
		if(!isNaN(obj.value)&&obj.value.indexOf(".")==-1){
			var parentTr = $(obj).parent().parent();
			if(parseInt(obj.value)==0){
				parentTr.find(":input[fieldName=conclusion] ~ span").html("OK");
				parentTr.find(":input[fieldName=conclusion]").val("OK");
			}else{
				parentTr.find(":input[fieldName=conclusion] ~ span").html("NG");
				parentTr.find(":input[fieldName=conclusion]").val("NG");
			}
			caculateTotalAmount();
		}
	}
	function resultChange(){
		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find(":input[fieldName=maxlimit]").val(),minlimit = parentTr.find(":input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!this.value){
					$(this).css("color","black").attr("color","black");
				}else{
					var val = parseFloat(this.value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(val>=parseFloat(minlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}else{//有上限和下限
						if(val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(this).css("color","black").attr("color","black");
						}else{
							$(this).css("color","red").attr("color","red");
						}
					}
				}
				$(this).parent().each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
			}
		}
	}
	function resultChangeSelf(obj,value){
		if(!isNaN(value)){
			var parentTr = $(obj).parent().parent();
			var maxlimit = parentTr.find("input[fieldName=maxlimit]").val(),minlimit = parentTr.find("input[fieldName=minlimit]").val();
			if(maxlimit&&!isNaN(maxlimit)||minlimit&&!isNaN(minlimit)){
				if(!value){
					$(obj).css("color","black").attr("color","black");
				}else{
					//判断是按照大于还是大于等于的方式来进行判断,onlyGreater=true表示仅按照>或小于的方式来判断,默认按照>=和<=的方式来进行
					var specifications = $(obj).closest("tr").find(":input[fieldName=specifications]").val()+"";
					var onlyGreater = false;
					if(specifications.indexOf(">")>-1||specifications.indexOf("<")>-1){
						onlyGreater = true;
					}
					var val = parseFloat(value);
					if(!maxlimit||isNaN(maxlimit)){//只有下限
						if(onlyGreater&&val>parseFloat(minlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val>=parseFloat(minlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}else if(!minlimit||isNaN(minlimit)){//只有上限
						if(onlyGreater&&val<parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val<=parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}else{//有上限和下限
						if(onlyGreater&&val>parseFloat(minlimit)&&val<parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else if(!onlyGreater&&val>=parseFloat(minlimit)&&val<=parseFloat(maxlimit)){
							$(obj).css("color","black").attr("color","black");
						}else{
							$(obj).css("color","red").attr("color","red");
						}
					}
				}
				$(obj).parent().each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
			}
		}
	}
	//计算总的检验数量
	function caculateTotalAmount(){
		if($("#checkItemsParent").find(":input[fieldName=conclusion]").length==0){
			var qualifiedRate = $("#qualifiedRate").val();
			try {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
			} catch (e) {
				$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
			}
			return;
		}
		//总的判定
		var conclusion = 'OK';
		$("#checkItemsParent").find(":input[fieldName=conclusion]").each(function(index,obj){
			if(obj.value=='NG'){
				conclusion = 'NG';
				return false;
			}
		});
		$("input[name=inspectionConclusion]").val(conclusion);
		$("input[name=inspectionConclusion]").parent().find("span").html(conclusion=='OK'?"合格":"<b>不合格</b>");
		//检验数量
		var inspectionAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=inspectionAmount]").each(function(index,obj){
			if(obj.value){
				if(parseInt(obj.value)>inspectionAmount){
					inspectionAmount = parseInt(obj.value);
				}
			}
		});
		$("#inspectionAmount").val(inspectionAmount);
		$("#inspectionAmount").parent().find("span").html(inspectionAmount);
		//不合格数量 
		var unqualifiedAmount = 0;
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").each(function(index,obj){
			if(obj.value&&!isNaN(obj.value)){
				unqualifiedAmount += parseInt(obj.value);
			}
		});
		if(unqualifiedAmount>inspectionAmount){
			unqualifiedAmount = inspectionAmount;
		}
		$("#unqualifiedAmount").val(unqualifiedAmount);
		$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
		//合格数量 
		var qualifiedAmount = inspectionAmount - unqualifiedAmount;
		if(qualifiedAmount<0){
			qualifiedAmount = 0;
		}
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);

		var qualifiedRate = 1;
		if(inspectionAmount>0){
			qualifiedRate = qualifiedAmount/(inspectionAmount*1.0);
		}
		$("#qualifiedRate").val(qualifiedRate);
		try {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0).toFixed(1) + "%");
		} catch (e) {
			$("#qualifiedRate").parent().find("span").html((qualifiedRate*100.0) + "%");
		}
	}
	//计算不合格数量和合格数 
	function caculateUqualifiedAmount(obj){
		var count = 0;
		$(obj).find("input").each(function(index,obj){
			if($(obj).attr("color")=='red'){
				count++;
			}
		});
		var $unqualifiedAmount = $(obj).parent().find(":input[fieldName=unqualifiedAmount]");
		$unqualifiedAmount.val(count);
		$unqualifiedAmount.closest("td").find("span").html(count);
		
		var qualifiedAmount = 0;
		var inspectionAmount = $(obj).parent().find(":input[fieldName=inspectionAmount]").val();
		if(inspectionAmount&&!isNaN(inspectionAmount)){
			qualifiedAmount = parseInt(inspectionAmount)-count;
			if(qualifiedAmount<1){
				qualifiedAmount=0;
			}
		}
		if(count==0||inspectionAmount==0||inspectionAmount==''){
			var $qualifiedRate = $(obj).parent().find(":input[fieldName=qualifiedRate]");
			$qualifiedRate.val(100);
		}else{
			var $qualifiedRate = $(obj).parent().find(":input[fieldName=qualifiedRate]");
			var rate = qualifiedAmount*100/inspectionAmount;
			$qualifiedRate.val(rate.toFixed(2));
		}
		var $qualifiedAmount = $(obj).parent().find(":input[fieldName=qualifiedAmount]");
		$qualifiedAmount.val(qualifiedAmount);
		$qualifiedAmount.parent().find("span").html(qualifiedAmount);
		
		if($unqualifiedAmount.length>0){
			unqualifiedAmountChange($unqualifiedAmount[0]);
		}
	}
	//上传附件
/* 	function uploadFiles(){
		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
	} */
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
		 		/* $("#" + hiddenInputId + "_uploadBtn").hide(); */
			}
		}
	}
	function submitImprove(id){
 		var url='${improvectx}/correction-precaution/called-input.htm?iqcInspectionReportId='+id;
 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
 			overlayClose:false,
 			title:"改进页面",
 			onClosed:function(){
 			}
 		});
 	}
	function prn1_preview() {	
		CreateOneFormPage();	
		
		LODOP.PREVIEW();	
	}
	function CreateOneFormPage(){
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml = strBodyStyle+"<body>"+document.getElementById("inspectionForm").innerHTML+"</body>";

		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_HTM(0,0,"100%","100%",strFormHtml);
	}
	function SaveAsFile(){
		var id = '${id}';
		if(!id){
			alert("请先保存!");
			return;
		}
		window.location.href = '${mfgctx}/inspection/export/export-report.htm?id=${id}';
		return;
		LODOP=getLodop(document.getElementById('LODOP_OB'),document.getElementById('LODOP_EM'));  
		LODOP.PRINT_INIT("进货检验表单打印");
		var strBodyStyle="<style>table { border: 1 solid #000000;border-collapse:collapse }</style>";
		var strFormHtml=strBodyStyle+"<body>"+document.getElementById("inspectionForm").innerHTML+"</body>";

		LODOP.SET_PRINT_STYLE("FontSize",12);
		LODOP.SET_PRINT_STYLE("Bold",1);
		LODOP.ADD_PRINT_TABLE(0,0,"100%","100%",strFormHtml);

		LODOP.SET_SAVE_MODE("QUICK_SAVE",true);//快速生成（无表格样式,数据量较大时或许用到）
		LODOP.SAVE_TO_FILE("进货检验报告.xlsx");
	}
	
 	function generalModel(){
 		var fileName = $("#workProcedure").val() + "检验数据";
 		var url = "${mfgctx}/check-inspection/general-model.htm?filename="+fileName;
 		$('#inspectionForm').attr('action',url);
		$('#inspectionForm').submit();
 	}
 	//上传检验数据
 	function uploadInspectionDatas(){
 		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showInspectionDatas',
			hiddenInputId : 'inspectionDatas',
		});
 	}
 	//发起不合格品处理流程
	function beginDefectiveGoodsProcessForm(id){
		if(id){
			window.location.href = '${mfgctx}/defective-goods/ledger/input.htm?sourceType=pqc&sourceId='+id;
		}else{
			var inspectionConclusion=$("#inspectionConclusion").val();
			if(inspectionConclusion=='OK'){
				alert("检验判定为合格时，不能触发不合格处理流程");
				return ;
			}
			window.location.href = '${mfgctx}/defective-goods/ledger/input.htm?qualityTestingReportNo=${inspectionNo}&sourceType=pqc';
		}
	}
	function showDefectiveGoodsProcessForm(formNo){
		$.colorbox({href:'${mfgctx}/defective-goods/ledger/view-info.htm?formNo='+formNo,iframe:true,
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
			overlayClose:false,
			title:"不合格品处理单详情"
		});
	}
	<jsp:include page="audit-method.jsp" />
	//获取不良项目和 成本
	function addRowHtml(obj,objId){
 		var tr = $(obj).closest("tr");
 		var clonetr = tr.clone(false);
 		tr.after(clonetr);
 		var total = $("#"+objId);
		var num = total.val();
		clonetr.find(":input").each(function(index ,obj){
 			obj=$(obj);
 			var name=obj.attr("name").split("-")[0];
 			obj.attr("id",name+"_"+num).val("");
 			obj.attr("name",name+"_"+num);
 			if($(obj).attr("filedName")=="manufactureTime"){
 				obj.removeClass("hasDatepicker").datepicker({changeYear:'true',changeMonth:'true'});
 			}
 		});
 	}
 	function removeRowHtml(classObj,obj,objId){
 		var total = $("#"+objId);
		var tr=$(obj).closest("tr");
		var pre=tr.prev("tr").attr("class");
		var next=tr.next("tr").attr("class");
		if(next==classObj){
		 	tr.remove();
		 	total.val(parseInt(total.val())-1);
		}else if(pre==classObj){
			tr.remove();
			total.val(parseInt(total.val())-1);
		}else{
			alert('至少要保留一行');
			total.val(1);
		}
 	}
 	function getMfgSupplierMessages(){
		var infovalue="";
		$(".mfgSupplierMessagesTr").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		$(":input[name=mfgSupplierMessagesStr]").val("["+infovalue.substring(1)+"]");
		infovalue="";
		$(".manufactureMessagesTr").each(function(index,obj){
			infovalue += getTdItem(obj);
		});
		$(":input[name=manufactureMessageStr]").val("["+infovalue.substring(1)+"]");
	}
	function getTdItem(obj){
		var value="";
		$(obj).find(":input[name]").each(function(index,obj){
			iobj = $(obj);
		    value += ",\""+iobj.attr("filedName")+"\":\""+iobj.val()+"\"";
		});
		return ",{"+value.substring(1)+"}";
   	}
	function submitProcessCard(url){
		var processCard=$("#processCard").val();
		var workProcedure=$("#workProcedure").val();
		if(processCard==""){
			alert("请先填写流程卡号");
			return;
		}
		$.post(url,{processCard:processCard,workProcedure:workProcedure},function(result){
			var resultObj=eval("("+result+")");
			if(resultObj.message!=""){
				window.location.href="${mfgctx}/inspection/made-inspection/input.htm?id="+resultObj.message +"&processCard=" +processCard +"&workProcedure="+workProcedure;
			}else{
				alert("未找到相关的集成数据，请联系管理员或再次确认流程卡号输入是否有误!!");
			}
		});
	}
	//导出
	function exportForm(){
		var id = '${id}';
		var current = 0;
		var dd = setInterval(function(){
			current++;
			var str = '';
			for(var i=0;i<(current%3);i++){
				str += "...";
			}
		}, 500);
		$("#iframe").bind("readystatechange",function(){
			clearInterval(dd);
			$("#message").html("");
			$("#iframe").unbind("readystatechange");
			printCertification();
		}).attr("src","${mfgctx}/inspection/made-inspection/download-report.htm?id="+id);
	}
	function setAuditMan(obj){
		var loginName = obj.value;
		var selectIndex = obj.selectedIndex;
		var name = obj.options[selectIndex].innerHTML;
		$("#choiceAuditLoinMan").val(loginName);
		$("#choiceAuditMan").val(name);
	}
	
	function modelClick(){
		var customerName=$("#customerName").val();
		if(!customerName){
			alert("请先选择客户！");
			return;
		}
			var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
			$.colorbox({href:encodeURI(url),iframe:true, 
				innerWidth:700, 
				innerHeight:500,
				overlayClose:false,
				title:"选择机型"
			});
		}
	function setProblemValue(datas){
		$("#customerModel").val(datas[0].value);
		$("#ofilmModel").val(datas[0].key);
	}	
	function customerChange(obj){
		var customerName=$("#customerCode").attr("")
		
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='made_inspection';
		var thirdMenu="_made_inspection_input";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-inprocess-inspection-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn" style="padding:0px;height:30px;">
						<table style="width:100%;" cellpadding="0" cellspacing="0">
							<tr>
								<td style="padding-left:4px;">
									<security:authorize ifAnyGranted="inspection-made-inspection-input">
									<button class='btn' onclick="window.location='${mfgctx}/inspection/made-inspection/input.htm'" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
									</security:authorize>
									<c:if test="${taskId==null}">
										<security:authorize ifAnyGranted="mfg-inspection-made-inspection-save,mfg-inspection-made-inspection-submit-process">
										<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/made-inspection/save.htm',false);"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
 										<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/made-inspection/submit-process.htm',true);"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
										<button class='btn' type="button" onclick="loadCheckItems();"><span><span><b class="btn-icons btn-icons-search"></b>同步检验项目</span></span></button>
										</security:authorize>
									</c:if>
									<c:if test="id>0&&reportState==REPORT_STATE_AUDIT&&(defectiveGoodsProcessingFormNo==null||defectiveGoodsProcessingFormNo=='')">	
									<button class="btn" onclick="beginDefectiveGoodsProcessForm()"><span><span><b class="btn-icons btn-icons-alarm"></b>不合格品处理</span></span></button>
									</c:if>
									<c:if test="defectiveGoodsProcessingFormNo!=null&&defectiveGoodsProcessingFormNo!='')">
									<button class="btn" onclick="showDefectiveGoodsProcessForm('${defectiveGoodsProcessingFormNo}')"><span><span><b class="btn-icons btn-icons-info"></b>查看不合格品处理单</span></span></button>
									</c:if>
<%-- 									<button class='btn' type="button" onclick="prn1_preview();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>--%>	
									<security:authorize ifAnyGranted="mfg-inspection-made-inspection-export-report">
									  <button class='btn' onclick="exportForm();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
									</security:authorize> 
									<button class='btn' type="button" onclick="javascript:window.history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
									<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
								</td>
							</tr>
						</table>
					</div>
					<div><iframe id="iframe" style="display:none;"></iframe></div>
					<div id="opt-content" style="text-align: center;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
					</div>
					<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
						<input type="hidden" name="inspectionPoint" value="${inspectionPoint}"/>
						<input type="hidden" name="id" value="${id}"></input>
						<input type="hidden" name="checkItemStrs" value="${checkItemStrs}"/>
						<input type="hidden" name="params.savetype" value="input"></input>
						<input type="hidden" name="acquisitionMethod" value="on"></input>
						<input type="hidden" name="mfgSupplierMessagesStr"/>
						<input type="hidden" name="manufactureMessageStr"/>
						<jsp:include page="input-form.jsp" />
					</form>
				</div>
			</div>
	</div>
</body>
</html>