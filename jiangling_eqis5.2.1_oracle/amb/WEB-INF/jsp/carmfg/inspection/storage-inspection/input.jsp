<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ include file="/common/taglibs.jsp"%>
<%
List<Option> mfg_inspection_result = ApiFactory.getSettingService().getOptionsByGroupCode("mfg_inspection_result");
ActionContext.getContext().put("mfg_inspection_result", mfg_inspection_result);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
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
		$('#inspectionDate').datepicker({changeYear:'true',changeMonth:'true'});
		$("#stockAmount").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				loadCheckItems();
			}
		});
		$("#inspectionForm").validate({});
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
		$("#checkBomCode")
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisSearch = $(this).attr("hisSearch");
				if(this.value != hisSearch){
					$(this).attr("hisSearch",this.value);
					loadCheckItems();
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200,
			source: function( request, response ) {
				response([{label:'数据加载中...',value:''}]);
				$.post("${mfgctx}/common/search-product-boms.htm",{searchParams:'{"code":"'+request.term+'"}',label:'code'},function(result){
					response(result);
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.value){
					var his = $(":input[name=checkBomCode]").attr("hisValue");
					$(":input[name=checkBomName]").val(ui.item.name)
									  .closest("td").find("span").html(ui.item.name);
					$(":input[name=checkBomCode]").val(ui.item.code).attr("hisValue",ui.item.code);
					$(":input[name=checkBomModel]").val(ui.item.model)
						.closest("td").find("span").html(ui.item.model);
					if(ui.item.code != his){
						loadCheckItems();
					}
				}else{
					$(":input[name=checkBomName]").val("")
									  .closest("td").find("span").html(ui.item.name);
					$(":input[name=checkBomCode]").val("").attr("hisValue","");
					$(":input[name=checkBomModel]").val("")
						.closest("td").find("span").html("");
				}
				return true;
			},
			close : function(event,ui){
				var val = $(":input[name=checkBomCode]").val();
				if(val){
					var hisValue = $(":input[name=checkBomCode]").attr("hisValue");
					if(val != hisValue){
						$(":input[name=checkBomCode]").val(hisValue);
					}
				}else{
					$(":input[name=checkBomCode]").val("").attr("hisValue","").attr("hisSearch",'');
					$(":input[name=checkBomName]").val("")
									  .closest("td").find("span").html(ui.item.name);
					$(":input[name=checkBomModel]").val("")
						.closest("td").find("span").html("");
				}
			}
		});
	});
	function submitForm(url){
		if($("#inspectionForm").valid()){
			var unqualifiedAmount=$("#unqualifiedAmount").val();
			var amount=$(".amount");
			var total=0;
			for(var i=0;i<amount.length;i++){
				var number=amount[i].value;
				total= parseInt(total)+ parseInt(number);
			}
			if(total<unqualifiedAmount){
				alert("不良项目数量不能小于不良数!");
				return ;
			}
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}

	function selectInspectionPoint(obj){
		window.location.href = encodeURI('${mfgctx}/inspection/storage-inspection/input.htm?inspectionPoint=' + obj.value);
		return;
		/**var inspectionObj = inspectionPointMapping[obj.value];
		if(inspectionObj){
			var hisWorkProcedure = $("#workProcedure").val();
			$(":input[name=section]").val(inspectionObj.section);
			$(":input[name=section]").closest("td").html(inspectionObj.section);
			$(":input[name=productionLine]").val(inspectionObj.productionLine);
			$(":input[name=productionLine]").closest("td").html(inspectionObj.productionLine);
			$(":input[name=workGroupType]").val(inspectionObj.workGroupType);
			$(":input[name=workGroupType]").closest("td").html(inspectionObj.workGroupType);
			$(":input[name=businessUnitName]").val(inspectionObj.businessUnitName);
			$(":input[name=businessUnitCode]").val(inspectionObj.businessUnitCode);
			$(":input[name=businessUnitName]").closest("td").html(inspectionObj.businessUnitName);
			$(":input[name=workProcedure]").val(inspectionObj.workProcedure);
			$(":input[name=workProcedure]").closest("td").html(inspectionObj.workProcedure);
			if(hisWorkProcedure != inspectionObj.workProcedure){
				loadCheckItems();
			}
		}*/
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
		$(":input[name=checkBomCode]").val(datas[0].code);
		$(":input[name=checkBomName]").val(datas[0].name);
		$(":input[name=checkBomName]").closest("td").find("span").html(datas[0].name);
		$(":input[name=checkBomModel]").val(datas[0].model);
		$(":input[name=checkBomModel]").closest("td").find("span").html(datas[0].model);
		if(datas[0].code != his){
			loadCheckItems();
		}
 	}
	
	function customerClick(){
		$.colorbox({href:"${marketctx}/customer/select-customer.htm",iframe:true,
			width:$(window).width()<1000?$(window).width()-100:1000,
			height:$(window).height()<600?$(window).height()-100:600,
			overlayClose:false,
			title:"选择客户"
		});
	}
	
	function setCustmerValue(objs){
		var obj = objs[0];
		$("#customerCode").val(obj.code);
		$("#customerName").val(obj.name);
		$("#customerName").next().html(obj.name);
	}
	
	function callback(){
		showMsg();
	}
	
	//选择检验人员
 	function selectObj(title,hiddenInputId,showInputId){
		var acsSystemUrl = "${ctx}";
		popTree({ title : title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId :hiddenInputId,
			showInputId : showInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	
	function loadCheckItems(){
		var inspectionDate = $("#inspectionDate").val();
		var workProcedure = $("#workProcedure").val();
		var checkBomCode = $("#checkBomCode").val();
		var stockAmount = $("#stockAmount").val();
		if(!checkBomCode||!stockAmount){
			
		}else{
			var params = {
				workProcedure : workProcedure,
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				inspectionDate : inspectionDate,
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
			var url = "${mfgctx}/inspection/storage-inspection/check-items.htm";
			$("#checkItemsParent").find("div").load(url,params,function(){
				$("#inspectionForm").validate({});
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
		if(tdLen==30){
			alert("最多能添加30项！");
		}else{
			var html = "<input color=\"black\" style=\"width:33px;float:left;margin-left:2px;\" title=\""+checkItemName+"样品"+(tdLen+1)+"\" name=\"result"+(tdLen+1)+"\"  class=\"{number:true,min:0}\"></input>";
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
		$(".checkItemsClass").each(function(index,obj){
			var len = $(obj).find("input").length;
			if(len>maxLen){
				maxLen = len;
			}
		});
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
			var re = parentTr.find(":input[fieldName=aqlRe]").val();
			if(isNaN(re)){
				parentTr.find(":input[fieldName=conclusionSpan]").html("OK");
				parentTr.find(":input[fieldName=conclusion]").val("OK");
			}else{
				if(parseInt(obj.value)<parseInt(re)){
					parentTr.find(":input[fieldName=conclusion] ~ span").html("OK");
					parentTr.find(":input[fieldName=conclusion]").val("OK");
				}else{
					parentTr.find(":input[fieldName=conclusion] ~ span").html("NG");
					parentTr.find(":input[fieldName=conclusion]").val("NG");
				}
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
		
		var $qualifiedAmount = $(obj).parent().find(":input[fieldName=qualifiedAmount]");
		$qualifiedAmount.val(qualifiedAmount);
		$qualifiedAmount.parent().find("span").html(qualifiedAmount);
		
		if($unqualifiedAmount.length>0){
			unqualifiedAmountChange($unqualifiedAmount[0]);
		}
	}
	//上传附件
	function uploadFiles(){
		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
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
	
	//选择不良代码
 	var selectInputObj = '';
 	function selectDefectionCode(obj){
 		selectInputObj = obj;
 		var url = '${mfgctx}/common/code-bom-multi-select.htm';
 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
 			overlayClose:false,
 			title:"选择不良代码"
 		});
 	}
	var temp=[];
	var tempi=0;
 	//选择之后的方法 data格式{key:'a',value:'a'}
 	function setDefectionValue(tempdatas){
 		//不良项目表头
 		var hisDatas = {};
 		var datas = [];
 		$("#badItem").find(":input[name]").each(function(index,obj){
 			if(obj.name.indexOf("_amount")==-1&&obj.name.indexOf("badItems")>-1){
 				var code = obj.name.split(".")[1],name = $(obj).val(),number = $(":input[name="+obj.name+"_amount]").val();
 				code = code.substr(1);
 				datas.push({key:code,value:name,number:number});
 				hisDatas[code] = name;
 			}
 		});
 		for(var i=0;i<tempdatas.length;i++){
 			var data = tempdatas[i];
 			if(!hisDatas[data.key]){
 				datas.push(data);
 			}
 		}
 		var tableHtml="<table class='form-table-border' align='center' style='border:0px;table-layout:fixed;'>";
 		var firstTr = "<tr><td style='text-align:center;border-left:0px;'>不良名称</td>",secondTr = "<tr><td style='text-align:center;border-left:0px;border-bottom:0px;'>不良数量</td>";
 		for(var i=0;i<datas.length;i++){
 			var data = datas[i];
 			firstTr += "<td style='text-align:center;cursor:pointer;' title='双击移除不良项' header='"+data.key+"' code='"+data.key+"'>" + data.value + "<input type='hidden' value='"+data.value+"' name='badItems.a"+data.key+"'/></td>";
 			secondTr += "<td style='text-align:center;border-bottom:0px;' code='"+data.key+"'><input type='text' style='width:50px;' class='amount' name='badItems.a"+data.key+"_amount' value='"+(data.number==undefined?"":data.number)+"'/></td>";
 		}
 		firstTr += "</tr>";
 		secondTr += "</tr>";
 		$("#badItem").html(tableHtml + firstTr + secondTr + "</table>");
 		$("#badItem").find("td[header]").mouseover(function(){
 			$(this).css("background","#ffff99");
 		}).mouseout(function(){
 			$(this).css("background","");
 		}).dblclick(function(){
 			var code = $(this).attr("header");
 			$("#badItem td[code="+code+"]").remove();
 		});
 		return;
 		for(var i=0;i<tempDatas.length;i++){
 			var data=tempDatas[i];
 			if(i==0){
 				str=str+"<table class='form-table-border' align='center' style='border:0px;table-layout:fixed;'><tr><td style='text-align:center;'>不良名称</td><td style='text-align:center;'>"+data.value+"<input type='hidden' value="+data.value+" name='badItems.unqualifiedItem"+data.id+"'></input></td>";	
 			}else if(i==tempDatas.length-1){
 				str=str+"<td style='text-align:center;'>"+data.value+"<input type='hidden' value="+data.value+" name='badItems.unqualifiedItem"+data.id+"'></input></td></tr>";	
 			}else{
 				str=str+"<td style='text-align:center;'>"+data.value+"<input type='hidden' value="+data.value+" name='badItems.unqualifiedItem"+data.id+"'></input></td>";	
 			}
 		}
 		for(var i=0;i<tempDatas.length;i++){
 			var data=tempDatas[i];
 			if(i==0){
 				str=str+"<tr><td style='text-align:center;'>不良数量</td><td style='text-align:center;'><input class='amount' type='text' style='width:50px;' name='badItems.unqualifiedItem"+data.id+"_amount'></input></td>";	
 			}else if(i==tempDatas.length-1){
 				str=str+"<td style='text-align:center;'><input type='text' style='width:50px;' class='amount' name='badItems.unqualifiedItem"+data.id+"_amount'></td></tr></table>";	
 			}else{
 				str=str+"<td style='text-align:center;'><input type='text' style='width:50px;' class='amount' name='badItems.unqualifiedItem"+data.id+"_amount'></td>";	
 			}
 		}
 		$("#badItem").html(str);
 	}
 	function selectMethod(obj){
 		$("input[name=checkItemStrs]").val(getCheckItemStrs_1());
 		var paras = $("input[name=checkItemStrs]").val();
 		var workProcedure = $("#workProcedure").val() + "检验数据";
 		var num = $("#stockAmount").val();
 		var code = $("#checkBomCode").val();
 		var method = obj.value;
 		if(method == "off"){
 			if(num==""||code==""){
 				alert("产品或送检数量为空，不能进行离线采集！");	
 				$("#stockAmount").focus();
 				return;
 			}
 	 		if(paras == "[]"||paras==""){
 	 			alert("请先设置检验项目！");
 	 			return;
 	 		}
 			$("#td1").css("display","block");
 			$("#td2").css("display","block");
 			$("#mya").css("display","block");
 			mya.innerHTML = "<span style='text-decoration:underline'><a href='javascript:generalModel();' title='"+workProcedure+".xls'>"+workProcedure+".xls&nbsp;</a></span>";
 			$("#uploadBtn").css("display","block");
 		}else{
 			$("#td1").css("display","none");
 			$("#td2").css("display","none");
 			$("#mya").css("display","none");
 			$("#uploadBtn").css("display","none");
 		}
 	}
 	function getCheckItemStrs_1(){
		var checkItemStrs = "";
		$("#checkItemsParent").find("table>tbody>tr").each(function(index,obj){
			if(checkItemStrs){
				checkItemStrs += ",";
			}
			var str = '';
			$(obj).find(":input").each(function(index,obj){
				if(obj.name){
					if(str){
						str += ","; 
					}
					str += "\"" + obj.name + "\":\"" + $(obj).val() + "\"";
					//$(obj).attr("name","");
				}
			});
			checkItemStrs += "{" + str + "}";
		});
		return "[" + checkItemStrs + "]";
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
			if(inspectionConclusion=='合格' || inspectionConclusion=='OK'){
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
	<jsp:include page="../first-inspection/audit-method.jsp" />
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
		var secMenu='data_acquisition';
		var thirdMenu="storageInspectionRecord";
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
	<%
		ActionContext.getContext().put("REPORT_STATE_DEFAULT",MfgCheckInspectionReport.STATE_DEFAULT);
		ActionContext.getContext().put("REPORT_STATE_RECHECK",MfgCheckInspectionReport.STATE_RECHECK);
		ActionContext.getContext().put("REPORT_STATE_AUDIT",MfgCheckInspectionReport.STATE_AUDIT);
		ActionContext.getContext().put("REPORT_STATE_COMPLETE",MfgCheckInspectionReport.STATE_COMPLETE);
	%>
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn">
						<table style="width:100%;" cellpadding="0" cellspacing="0">
							<tr>
								<td style="padding-left:4px;">
									<security:authorize ifAnyGranted="MFG_INSPECTION_STORAGE-INSPECTION_FORM_INPUT">
									<button class='btn' onclick="javascript:window.location='${mfgctx}/inspection/storage-inspection/input.htm?inspectionPoint=${inspectionPoint}';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
									</security:authorize>
									<c:if test="${reportState==REPORT_STATE_DEFAULT||reportState==REPORT_STATE_RECHECK}">
									<security:authorize ifAnyGranted="MFG_INSPECTION_STORAGE-INSPECTION_FORM_SAVE">
									<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/storage-inspection/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
									<button class='btn' type="button" onclick="submitForm('${mfgctx}/inspection/storage-inspection/save.htm?isSubmit=true');"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
									</security:authorize>
									</c:if>
									<security:authorize ifAnyGranted="MFG_INSPECTION_AUDIT">
									<c:if test="${reportState==REPORT_STATE_AUDIT}">
									<button class='btn' type="button" onclick="auditPqc({id:'${id}',reportState:'${reportState}'},'storage');"><span><span><b class="btn-icons btn-icons-audit"></b>审核</span></span></button>			
									</c:if>
									<c:if test="${reportState==REPORT_STATE_DEFAULT||reportState==REPORT_STATE_COMPLETE}">
									<button class='btn' type="button" onclick="reCheck('${id}','storage');"><span><span><b class="btn-icons btn-icons-cancel"></b>重新检验</span></span></button>
									</c:if>
									</security:authorize>
									<c:if test="id>0&&reportState==REPORT_STATE_AUDIT&&(defectiveGoodsProcessingFormNo==null||defectiveGoodsProcessingFormNo=='')">
									<button class="btn" onclick="beginDefectiveGoodsProcessForm()"><span><span><b class="btn-icons btn-icons-alarm"></b>不合格品处理</span></span></button>
									</c:if>
									<c:if test="defectiveGoodsProcessingFormNo!=null&&defectiveGoodsProcessingFormNo!='')">
									<button class="btn" onclick="showDefectiveGoodsProcessForm('${defectiveGoodsProcessingFormNo}')"><span><span><b class="btn-icons btn-icons-info"></b>查看不合格品处理单</span></span></button>
									</c:if>
									<button class='btn' type="button" onclick="prn1_preview();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>	
									<security:authorize ifAnyGranted="MFG_INSPECTION_STORAGE-INSPECTION_FORM_EXPORT">
									<button class='btn' onclick="SaveAsFile();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
									</security:authorize>
									<button class='btn' type="button" onclick="javascript:window.history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
									<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>
								</td>
								<td align="right">
									采集点：<s:select list="inspectionPointList" 
											  theme="simple"
											  onchange="selectInspectionPoint(this)"
											  listKey="inspectionPointName" 
											  listValue="inspectionPointName" 
											  id="inspectionPointSelect"
											  name="inspectionPoint"
											  value="inspectionPoint"
											  labelSeparator=""
											  emptyOption="false"></s:select>
								</td>
							</tr>
						</table>
					</div>
					<div id="opt-content" style="text-align: center;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
					</div>
					<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
						<input type="hidden" name="inspectionPoint" value="${inspectionPoint}"/>
						<input type="hidden" name="id" value="${id}"></input>
						<input type="hidden" name="params.savetype" value="input"></input>
						<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
						<caption style="height: 25px"><h2>成品检验报告</h2></caption>
						<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}</caption>
						<tr>
							<td style="width:140px;">事业部</td>
							<td style="">
								<input name="businessUnitCode" value="${businessUnitCode}" type="hidden"></input>
								<input name="businessUnitName" value="${businessUnitName}" type="hidden"></input>
								<span>${businessUnitName}</span>
							</td>
							<td style="width:140px;">工段</td>
							<td style="">
								<input name="section" value="${section}" type="hidden"></input>
								<span>${section}</span>
							</td>
							<td style="width:140px;">生产线</td>
							<td style="">
								<input name="productionLine"  value="${productionLine}" type="hidden"></input>
								<span>${productionLine}</span>
							</td>
						</tr>
						<tr>
							<td>班别</td>
							<td>
								<input name="workGroupType" value="${workGroupType}" type="hidden"></input>
								<span>${workGroupType}</span>
							</td>
							<td>工序</td>
							<td>
								<input name="workProcedure" id="workProcedure" value="${workProcedure}" type="hidden"></input>
								<span>${workProcedure}</span>
							</td>
							<td><span style="color:red">*</span>产品编码</td>
							<td>
								<input style="float:left;" name="checkBomCode" id="checkBomCode" value="${checkBomCode}" hisValue="${checkBomCode}" hisSearch="${checkBomCode}" class="{required:true,messages:{required:'产品型号不能为空'}}"></input>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="checkBomClick(this)" href="javascript:void(0);" title="选择产品型号"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
							</td>
						</tr>
						<tr>
							<td>产品型号</td>
							<td>
								<input name="checkBomModel"  value="${checkBomModel}" type="hidden"></input>
								<span>${checkBomModel}</span>
							</td>
							<td>产品名称</td>
							<td>
								<input name="checkBomName"  value="${checkBomName}" type="hidden"></input>
								<span>${checkBomName}</span>
							</td>
							<td><span style="color:red;width:10%">*</span>出货数量</td>
							<td>
								<input id="stockAmount" name="stockAmount" value="${stockAmount}"  class="{required:true,digits:true,messages:{required:'出货数量不能为空',digits:'必须是整数!'}}"></input>
							</td>
						</tr>
						<tr>
							<td>交货日期</td>
							<td>
								<input id="inspectionDate" name="inspectionDate" value="<s:date name="inspectionDate" format="yyyy-MM-dd" />"></input>
							</td>
							<td>客户编码</td>
							<td>
								<input style="float:left;" readonly="readonly" id="customerCode" name="customerCode" value="${customerCode}"></input>
								<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="customerClick(this)" href="javascript:void(0);" title="选择客户"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
							</td>
							<td>客户名称</td>
							<td>
								<input id="customerName" name="customerName" value="${customerName}" type="hidden"></input>
								<span>${customerName}</span>
							</td>
						</tr>
						<tr>
							 <td>消防编号</td>
							<td>
								<input name="fireControlNo" value="${fireControlNo}"></input>
							</td>
							<td>是否3C产品</td>
							<td>
								<input name="is3C" value="${is3C}" type="hidden"></input>
								<span>${is3C}</span>
							</td>
							<td>是否标准件</td>
							<td>
								<input name="isStandard" value="${isStandard}" type="hidden"></input>
								<span>${isStandard}</span>
							</td>
						</tr>
						<tr>
							<td>是否关键件</td>
							<td>
								<input name="iskeyComponent" value="${iskeyComponent}" type="hidden"></input>
								<span>${iskeyComponent}</span>
							</td>
							<td>是否量产</td>
							<td>
								<input type="radio" name="isBatchProduct" value="是" <s:if test="%{isBatchProduct==\"是\"}">checked="checked"</s:if>/>是
								<input type="radio" name="isBatchProduct" value="否" <s:if test="%{isBatchProduct==\"否\"}">checked="checked"</s:if>/>否
							</td>
							<td>检验标准版本</td>
							<td>
								<input name="standardVersion" value="${standardVersion}" type="hidden"></input>
								<span>${standardVersion}</span>
							</td>
						</tr>
						<tr>
							<td>检验标准附件</td>
							<td colspan="5" id="showIndicatorAttachmentFiles"></td>
						</tr>
						<!-- <tr>
							<td>
								<input type="radio" name="acquisitionMethod" id="acquisitionMethod" value="on" checked="checked"  onclick="selectMethod(this)">在线采集</input>
								<input type="radio" name="acquisitionMethod" id="acquisitionMethod" value="off" onclick="selectMethod(this)">离线采集</input>
							</td>
							<td>
								<div id="td1" style="display: none;">Excel模板（下载模板填写数据后上传）</div>
							</td>
							<td colspan="2">
								<div id="mya" style="display: none;"></div>
							</td>
							<td>
								<input type="hidden" name="hisinspectionDatas" value='${inspectionDatas}'></input>
								<input type="hidden" name="inspectionDatas" id="inspectionDatas" value='${inspectionDatas}'></input>
								<button  class='btn' type="button" onclick="uploadInspectionDatas();" id="uploadBtn" style="display: none;"><span><span><b class="btn-icons btn-icons-upload"></b>上传离线数据</span></span></button>
							</td>
							<td id="showInspectionDatas"></td>
						</tr> -->
						<tr>
							<td colspan="6" style="padding:0px;" id="checkItemsParent">
								<div style="overflow-x:auto;overflow-y:hidden;">
									<%@ include file="check-items.jsp"%>
								</div>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="border-bottom:0px;">
								<button  class='btn' type="button" onclick="selectDefectionCode();"><span><span><b class="btn-icons btn-icons-search"></b>选择不良项目</span></span></button>
							</td>
						</tr>
						<tr>
							<td colspan="6" style="padding:0px;border-top:0px;">
								<div id="badItem">
								${badItemStrs}
								</div>
							</td>
						</tr>
						<tr>
							<td style="border-top:0px;">检验数</td>
							<td colspan="5" style="border-top:0px;">
								<span>${inspectionAmount}</span>
								<input type="hidden" name="inspectionAmount" id="inspectionAmount"   value="${inspectionAmount}" ></input>
							</td>
						</tr>
						<tr>
							<td>合格数</td>
							<td>
								<span>${qualifiedAmount}</span>
								<input type="hidden" name="qualifiedAmount" id="qualifiedAmount"  value="${qualifiedAmount}" onkeyup="caclute();"></input>
							</td>
							<td>不良数</td>
							<td>
								<span>${unqualifiedAmount}</span>
								<input type="hidden" name="unqualifiedAmount" id="unqualifiedAmount" value="${unqualifiedAmount}"></input>
							</td>
							<td>合格率</td>
							<td>
								<span>${qualifiedRate}</span>
								<input type="hidden" name="qualifiedRate" id="qualifiedRate" value="${qualifiedRate}"></input>
							</td>
						</tr>
						<tr>
							
							<td>检验判定</td>
							<td style="border-top:0px;">
								<span>${inspectionConclusion}</span>
								<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"></input>
							</td>
							
							<td>
								<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
								<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
								<button  class='btn' type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
							</td>
							<td colspan="5" id="showAttachmentFiles">
							</td>
						</tr>
						<tr>
							<!--<td>
								结果
							</td>
							<td>
								<input type="radio" name="processingResult" value="合格" <s:if test="%{processingResult==\"合格\"}">checked="checked"</s:if>/>合格
								<input type="radio" name="processingResult" value="不合格" <s:if test="%{processingResult==\"不合格\"}">checked="checked"</s:if>/>不合格
							</td>  -->
							<td>再次检验确认</td>
							<td>
								<s:select list="mfg_inspection_result"
								  value="sureOKAgain"
								  theme="simple"
								  listKey="name" 
								  listValue="name" 
								  name="sureOKAgain"
								  labelSeparator=""
								  emptyOption="true">
								</s:select>
							</td>
							
							<td>
								审核员
							</td>
							<td>
								<input name="auditMan" id="auditMan" value="${auditMan}"  readonly="readonly" onclick="selectObj('选择审核人员','auditMan','auditMan')"></input>
							</td>
							<td style="border-left:0px;"><span style="color:red">*</span>检验员</td>
							<td >
								<input name="inspector" id="inspector" value="${inspector}"  readonly="readonly" onclick="selectObj('选择检验人员','inspector','inspector')" class="{required:true,messages:{required:'检验员不能为空'}}"></input>
							</td>
						</tr>
					</table>
					</form>
				</div>
			</div>
	</div>
</body>
</html>