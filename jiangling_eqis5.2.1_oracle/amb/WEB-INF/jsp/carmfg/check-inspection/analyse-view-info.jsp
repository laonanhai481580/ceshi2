<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
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
		$('#inspectionDate').datetimepicker({changeYear:'true',changeMonth:'true'});
		$("#stockAmount").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				loadCheckItems();
			}
		});
		$("#inspectionForm").validate({
// 			showErrors : function(errorMap,errorList){
// 				$("#inspectionForm .customError").remove();
// 				for(var i=0;i<errorList.length;i++){
// 					var error = errorList[i];
// 					var offset = $(error.element).position();
// 					$("<div class='customError'>"+error.message+"</div>")
// 					.css("position","absolute")
// 					.css("color","red")
// 					.css("clear","both")
// 					.css("z-index",100)
// 					.css("left",offset.left+"px")
// 					.css("top",(offset.top+$(error.element).height()+2) + "px")
// 					.width($(error.element).width())
// 					.insertAfter($("#opt-content"));
// 				}
// 				this.defaultShowErrors();
// 			}
		});
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
					$("#checkBomName").val(ui.item.value)
									  .attr("hisValue",ui.item.value);
					this.value = ui.item.label;
					$("#checkBomCode").attr("hisValue",this.value);
				}else{
					$("#checkBomName").val("")
					  .attr("hisValue","");
					this.value = "";
					$("#checkBomCode").attr("hisValue","");
					this.value = "";
				}
				return true;
			},
			close : function(event,ui){
				var val = $("#checkBomCode").val();
				if(val){
					var hisValue = $("#checkBomCode").attr("hisValue");
					if(val != hisValue){
						$("#checkBomCode").val(hisValue);
					}
				}else{
					$("#checkBomCode").val('');
					$("#checkBomName").val('').attr("hisValue",'').attr("hisSearch",'');
				}
			}
		});
		//自动填写,物料名称
		$("#checkBomName")
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
				$.post("${mfgctx}/common/search-product-boms.htm",{searchParams:'{"name":"'+request.term+'"}',label:'name'},function(result){
					response(result);
				},'json');
			},
			focus: function() {
				return false;
			},
			select: function( event, ui ) {
				if(ui.item.value){
					$("#checkBomCode").val(ui.item.value)
									  .attr("hisValue",ui.item.value);
					this.value = ui.item.label;
					$("#checkBomName").attr("hisValue",this.value);
				}else{
					$("#checkBomCode").val("")
					  .attr("hisValue","");
					this.value = "";
					$("#checkBomName").attr("hisValue","");
					this.value = "";
				}
				return false;
			},
			close : function(event,ui){
				var val = $("#checkBomName").val();
				if(val){
					var hisValue = $("#checkBomName").attr("hisValue");
					if(val != hisValue){
						$("#checkBomName").val(hisValue);
					}
				}else{
					$("#checkBomName").val('');
					$("#checkBomCode").val('').attr("hisValue",'').attr("hisSearch",'');
				}
			}
		});
	});
	function submitForm(url){
// 		var inspectionAmount=$("#inspectionAmount").val();
// 		var unqualifiedAmount=$("#unqualifiedAmount").val();
// 		var qualifiedAmount=$("#qualifiedAmount").val();
// 		if(parseInt(inspectionAmount)!=parseInt(unqualifiedAmount)+parseInt(qualifiedAmount)){
// 			alert("检验数不等于合格数加不良数");
// 			return false;
// 		}
		if($("#inspectionForm").valid()){
			$("input[name=checkItemStrs]").val(getCheckItemStrs());
			
			var unqualifiedAmount=$("#unqualifiedAmount").val();
			var amount=$(".amount");
			var total=0;
			for(var i=0;i<amount.length;i++){
				var number=amount[i].value;
				total= parseInt(total)+ parseInt(number);
			}
			if(unqualifiedAmount!=total){
				alert("不良数量不相等");
				return ;
			}
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
// 			$("#message").html("<font color=red>" + error.message + "</font>");
// 			alert(error.message);
			$(error.element).focus();
// 			setTimeout(function(){
// 				$("#message").html("");
// 			}, 3000);
		}
	}

	function getCheckItemStrs(){
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
					$(obj).attr("name","");
				}
			});
			checkItemStrs += "{" + str + "}";
		});
		return "[" + checkItemStrs + "]";
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
	function setBomValue(datas){
		var his = $("#checkBomCode").val();
		$("#checkBomCode").val(datas[0].key);
		$("#checkBomName").val(datas[0].value);
		if(datas[0].key != his){
			loadCheckItems();
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
		var his = $("#supplierId").val();
		$("#supplierName").val(obj.name);
		$("#supplierId").val(obj.id);
		if(obj.id != his){
			loadCheckItems();
		}
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
// 			$("#checkItemsParent").find("div").html("&nbsp;");
		}else{
			var params = {
				workProcedure : workProcedure,
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				inspectionDate : inspectionDate,
				checkItemStrs : getCheckItemStrs()
			};
			$("#scroll").hide();
			$("#checkItemsParent").find("div").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${mfgctx}/check-inspection/check-items.htm";
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
		$("#checkItemsParent").find("input[name=unqualifiedAmount]").bind("change",function(){
			unqualifiedAmountChange(this);
		});
		//合格数
		$("#checkItemsParent").find("input[name=qualifiedAmount]").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				caculateTotalAmount();
			}
		});
		//计量的值不符合范围时的事件
		$(".checkItemsClass input").bind("change",resultChange);
	}
	function unqualifiedAmountChange(obj){
		if(!isNaN(obj.value)&&obj.value.indexOf(".")==-1){
			var parentTr = $(obj).parent().parent();
			var re = parentTr.find("input[name=aqlRe]").val();
			if(isNaN(re)){
				parentTr.find("input[name=conclusionSpan]").html("OK");
				parentTr.find("input[name=conclusion]").val("OK");
			}else{
				if(parseInt(obj.value)<parseInt(re)){
					parentTr.find("input[name=conclusion] ~ span").html("OK");
					parentTr.find("input[name=conclusion]").val("OK");
				}else{
					parentTr.find("input[name=conclusion] ~ span").html("NG");
					parentTr.find("input[name=conclusion]").val("NG");
				}
			}
			caculateTotalAmount();
		}
	}
	function resultChange(){
		if(!isNaN(this.value)){
			var parentTr = $(this).parent().parent();
			var maxlimit = parentTr.find("input[name=maxlimit]").val(),minlimit = parentTr.find("input[name=minlimit]").val();
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
		if($("#checkItemsParent").find("input[name=conclusion]").length==0){
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
		$("#checkItemsParent").find("input[name=conclusion]").each(function(index,obj){
			if(obj.value=='NG'){
				conclusion = 'NG';
				return false;
			}
		});
		$("input[name=inspectionConclusion]").val(conclusion);
		$("input[name=inspectionConclusion]").parent().find("span").html(conclusion);
		//检验数量
		var inspectionAmount = 0;
		$("#checkItemsParent").find("input[name=inspectionAmount]").each(function(index,obj){
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
		$("#checkItemsParent").find("input[name=unqualifiedAmount]").each(function(index,obj){
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
		var $unqualifiedAmount = $(obj).parent().find("input[name=unqualifiedAmount]");
		$unqualifiedAmount.val(count);
		$unqualifiedAmount.parent().find("span").html(count);
		
		var qualifiedAmount = 0;
		var inspectionAmount = $(obj).parent().find("input[name=inspectionAmount]").val();
		if(inspectionAmount&&!isNaN(inspectionAmount)){
			qualifiedAmount = parseInt(inspectionAmount)-count;
			if(qualifiedAmount<1){
				qualifiedAmount=0;
			}
		}
		
		var $qualifiedAmount = $(obj).parent().find("input[name=qualifiedAmount]");
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
 		var datas = new Array(); 
 		for(var i=0;i<tempdatas.length;i++){ 
 			datas.push(tempdatas[i]); 
 		 } 
 		if(tempi!=0){
 		for(var i=0;i<datas.length;i++){
 			var data=datas[i];
 			var isExit=false;
 			for(var j=0;j<tempDatas.length;j++){
 				var tempData=tempDatas[j];
 				if(data.id==tempData.id){
 					isExit=true;
 					break;
 				}
 			}
 			if(!isExit){
				tempDatas.push(data);
			}
 		}
 		}else{
 			tempDatas=datas;
 		}
 		tempi++;
 		//不良项目表头
 		var str="";
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
 		$("#badItem").html("");
 		$("#badItem").html(str);
 	}
 	function selectMethod(obj){
 		$("input[name=checkItemStrs]").val(getCheckItemStrs_1());
 		var paras = $("input[name=checkItemStrs]").val();
 		var workProcedure = $("#workProcedure").val();
 		workProcedure = workProcedure + "检验数据";
 		var num = $("#stockAmount").val();
 		var code = $("#checkBomCode").val();
 		var method = obj.value;
 		if(method == "off"){
 			if(num==""||code==""){
 				alert("产品或送检数量为空，不能进行离线采集！");	
 				$("#stockAmount").focus();
 				return;
 			}
 	 		if(paras == "[]"){
 	 			alert("请先设置检验项目！");
 	 			return;
 	 		}
 			$("#td1").css("display","block");
 			$("#td2").css("display","block");
 			$("#mya").css("display","block");
 			mya.innerHTML = "<span style='text-decoration:underline'><a href='${mfgctx}/check-inspection/general-model.htm?filename="+workProcedure+"&paras="+paras+"' title='"+workProcedure+".xls'>"+workProcedure+".xls&nbsp;</a></span>";
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
 	//上传检验数据
 	function uploadInspectionDatas(){
 		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showInspectionDatas',
			hiddenInputId : 'inspectionDatas',
		});
 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>

	<script type="text/javascript">
		var secMenu='${workshop}';
		var thirdMenu="check_${inspectionPointId}";
		var accordionMenu="input";
	</script>
	
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn">
						<span style="margin-left:6px;line-height:30px;" id="message"><s:actionmessage theme="mytheme" cssStyle="color:red;" /></span>				
					</div>
					<div id="opt-content" style="text-align: center;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
					</div>
					<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
						<input type="hidden" name="checkItemStrs" value=""></input>
						<input type="hidden" name="id" value="${id}"></input>
						<input type="hidden" name="params.savetype" value="input"></input>
						
						<table class="form-table-border-left" style="width:100%;margin: auto;border:0px;">
							<caption style="height: 25px"><h2>检验报告</h2></caption>
							<caption style="text-align:right;padding-bottom:4px;">编号:${incomingInspectionActionsReport.inspectionNo}</caption>
							<tr>
								<td style="width:10%">车间</td>
								<td style="width:25%">
									${incomingInspectionActionsReport.workshop}
								</td>
								<td style="width:10%">生产线</td>
								<td style="width:25%">
									${incomingInspectionActionsReport.productionLine}
								</td>
								<td style="width:10%">工序</td>
								<td style="width:20%">
									${incomingInspectionActionsReport.workProcedure}
								</td>
							</tr>
							<tr>
								<td>订单号</td>
								<td>
									${incomingInspectionActionsReport.orderNo}
								</td>
								<td>批次号</td>
								<td>${incomingInspectionActionsReport.batchNo}</td>
								<td>产品编号</td>
								<td>${incomingInspectionActionsReport.productNo}</td>
							</tr>
							<tr>
								<td><font color="red">*</font>产品类型</td>
								<td>
									${incomingInspectionActionsReport.productModel}
								</td>
								<td><span style="color:red">*</span>产品型号</td>
								<td>
								${incomingInspectionActionsReport.modelSpecification}
								</td>
								<td><span style="color:red">*</span>零部件代号</td>
								<td>
									${incomingInspectionActionsReport.checkBomCode}
								</td>
							</tr>
							<tr>
								<td><span style="color:red">*</span>零部件名称</td>
								<td>
									${incomingInspectionActionsReport.checkBomName}
								</td>
								<td><span style="color:red">*</span>检验日期</td>
								<td>
									${incomingInspectionActionsReport.inspectionDate}
								</td>
								<td><span style="color:red">*</span>送检数量</td>
								<td>
									${incomingInspectionActionsReport.stockAmount}
								</td>
							</tr>
							<tr>
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
							</tr>
							<tr>
								<td colspan="6" style="padding:0px;" id="checkItemsParent">
									<div style="overflow-x:auto;overflow-y:hidden;padding-bottom:18px;">
										<%@ include file="check-items.jsp"%>
									</div>
								</td>
							</tr>
							<tr>
							</tr>
							<tr >
								<td colspan="6" style="padding:0px;">
									<div id="badItem">
									${badItemStrs}
									</div>
								</td>
							</tr>
							<tr>
								<td style="border-top:0px;">检验数</td>
								<td colspan="3" style="border-top:0px;">
									<span>${incomingInspectionActionsReport.inspectionAmount}</span>
								</td>
								<td style="border-left:0px;border-top:0px;">检验判定</td>
								<td style="border-top:0px;">
									<span>${incomingInspectionActionsReport.inspectionConclusion}</span>
								</td>
							</tr>
							<tr>
								<td>合格数</td>
								<td>
									<span>${incomingInspectionActionsReport.qualifiedAmount}</span>
								</td>
								<td>不良数</td>
								<td>
									<span>${incomingInspectionActionsReport.unqualifiedAmount}</span>
								</td>
								<td>合格率</td>
								<td>
									<span>${incomingInspectionActionsReport.qualifiedRate}</span>
								</td>
							</tr>
							<tr>
								<td>
									<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
									<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
									<button  class='btn' type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
								</td>
								<td colspan="5" id="showAttachmentFiles">
								</td>
							</tr>
							<tr>
								<td>
									结果
								</td>
								<td>
								${incomingInspectionActionsReport.processingResult }
								</td>
								<td><span style="color:red">*</span>
									审核员
								</td>
								<td >
									${incomingInspectionActionsReport.auditMan}
								</td>
								<td style="border-left:0px;"><span style="color:red">*</span>检验员</td>
								<td >
									${incomingInspectionActionsReport.inspector}
								</td>
							</tr>
						</table>
					</form>
				</div>
			</div>
	</div>
</body>
</html>