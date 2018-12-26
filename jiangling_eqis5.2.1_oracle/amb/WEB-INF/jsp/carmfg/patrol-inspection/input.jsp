<%@page import="com.ambition.carmfg.checkinspection.web.MfgPatrolInspectionReportAction"%>
<%@page import="com.norteksoft.product.util.PropUtils"%>
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
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<script language="javascript" src="${ctx}/widgets/jqzoom_ev-2.3/js/cloud-zoom.1.0.2.js"></script>
	<link  type="text/css" rel="stylesheet" href="${ctx}/widgets/jqzoom_ev-2.3/css/cloud-zoom.css"/>
	<object id="LODOP_OB" classid="clsid:2105C259-1E0C-4534-8141-A753534CB4CA" width=0 height=0> 
		<embed id="LODOP_EM" type="application/x-print-lodop" width=0 height=0 pluginspage="${ctx}/widgets/lodop/install_lodop.exe"></embed>
	</object>
	<style>
		div.container{
		position:absolute;
		top:30px;
		left:40px;
		visibility:hidden;
		font-size:13px;
		display:block;
		width:200px;
		background-color:yellow;
		*border:1px solid #666;
		}
		s{
		position:absolute;
		top:-20px;
		*top:-22px;
		left:20px;
		display:block;
		height:0;
		width:0;
		font-size: 0;
		line-height: 0;
		border-color:transparent transparent #666 transparent;
		border-style:dashed dashed solid dashed;
		border-width:10px;
		}
		i{position:absolute;
		top:-9px;
		*top:-9px;
		left:-10px;
		display:block;
		height:0;
		width:0;
		font-size: 0;
		line-height: 0;
		border-color:transparent transparent #fff transparent;
		border-style:dashed dashed solid dashed;
		border-width:10px;
		}
		.content{
		border:1px solid #666;
		-moz-border-radius:3px;
		-webkit-border-radius:3px;
		position:absolute;
		background-color:#fff;
		padding:6px;
		padding-top:10px;
		width:100%;
		text-align:left;
		/*height:100%;*/
		*top:-2px;
		*border-top:1px solid #666;
		*border-top:1px solid #666;
		*border-left:none;
		*border-right:none;
		/**height:102px;*/
		box-shadow: 3px 3px 4px #999;
		-moz-box-shadow: 3px 3px 4px #999;
		-webkit-box-shadow: 3px 3px 4px #999;
		/* For IE 5.5 - 7 */
		filter: progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#999999');
		/* For IE 8 */
		-ms-filter: "progid:DXImageTransform.Microsoft.Shadow(Strength=4, Direction=135, Color='#999999')";
		}
	</style>
	<script type="text/javascript">
	 var LODOP; //声明打印的全局变量 
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#checkItemsParent").position().top + $("#checkItemsParent").height()-30;
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
			$("div.container").hide();
		});
		$("body").click(function(){
			$("div.container").hide();
		});
		//绑定图片事件
		//bindImgZoom();
		//只能修改状态为巡检中的报告
		$("#message").find("span").next().remove();
		var patrolState = '${patrolState}';
		if("巡检中" != patrolState){
			$("#opt-content :input").attr("disabled","disabled");
			$("#opt-content").find("button").remove();
			$("#opt-content .small-button-bg").remove();
			return;
		}
		$('#inspectionDate').datepicker({changeYear:'true',changeMonth:'true'});
		$('#sendDate').datepicker({changeYear:'true',changeMonth:'true'});
		/**$("#stockAmount").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				loadCheckItems();
			}
		});*/
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
						//loadCheckItems();
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
			$("input[name=checkItemStrs]").val(getCheckItemStrs());
			$("input[name=patrolItemStrs]").val(getPatrolItemStrs());
			
			$("#checkBomName").attr("disabled","");
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
		}else{
			var error = $("#inspectionForm").validate().errorList[0];
			$(error.element).focus();
		}
	}
	
	//获取检验项目
	function getCheckItemStrs(){
		var checkItemStrs = "";
		$("#checkItemsParent").find("table>tbody>tr").each(function(index,obj){
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
			if(str){
				if(checkItemStrs){
					checkItemStrs += ",";
				}
				checkItemStrs += "{" + str + "}";
			}
		});
		return "[" + checkItemStrs + "]";
	}
	//获取巡检记录
	function getPatrolItemStrs(){
		var patrolItemStrs = "";
		var patrolMap = {},conclusionMap={},patrolItemMap = {},spcSampleIdMap={},remarkMap={};
		$(":input[patrolSpcSampleIds]").each(function(index,obj){
			spcSampleIdMap[$(obj).attr("patrolSpcSampleIds")] = obj.value;
		});
		$(":input[patrolItemId]").each(function(index,obj){
			patrolItemMap[$(obj).attr("patrolItemId")] = obj.value;
		});
		$(":input[patrolRemark]").each(function(index,obj){
			var val = $(obj).val();
			if(val){
				val = val.replace(/\n/g,'');
			}
			remarkMap[$(obj).attr("patrolRemark")] = val;
		});
		$(":input[patrolName]").each(function(index,obj){
			var patrolName = $(obj).attr("patrolName");
			if(!conclusionMap[patrolName]){
				conclusionMap[patrolName] = "OK";
			}
			if(obj.value){
				if(!patrolMap[patrolName]){
					patrolMap[patrolName] = [];
				}
				patrolMap[patrolName].push(obj.value);
				if(obj.type == 'text'){
					if($(obj).attr("color")=='red'){
						conclusionMap[patrolName] = "NG";
					}
				}else{
					if(obj.value=='NG'){
						conclusionMap[patrolName] = "NG";
					}
				}
			}
		});
		for(var pro in patrolMap){
			if(patrolItemStrs){
				patrolItemStrs += ",";
			}
			var conclusion = conclusionMap[pro];
			var patrolSpcSampleIds = spcSampleIdMap[pro];
			var patrolItemId = patrolItemMap[pro];
			var strs = pro.split("_");
			var remark = remarkMap[strs[1]];
			patrolItemStrs += "{\"checkItemName\":\"" + strs[0] + "\",\"inspectionDate\":\"" 
				+ strs[1] + "\",\"conclusion\":\"" 
				+ (conclusion?conclusion:"OK") + "\",\"result\":\"" + patrolMap[pro].join(",")
				+ "\",\"id\":\"" + (patrolItemId?patrolItemId:"")
				+ "\",\"remark\":\"" + (remark?remark:"")
				+ "\",\"spcSampleIds\":\"" + (patrolSpcSampleIds?patrolSpcSampleIds:"") + "\"}";
		}
		return "[" + patrolItemStrs + "]";
	}
	
	function checkBomClick(obj){
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setFullBomValue(datas){
		var his = $("#checkBomCode").val();
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
		if(!checkBomCode||!workProcedure){
			$("#checkItemsParent div").empty();
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
			var url = "${mfgctx}/patrol-inspection/check-items.htm";
			$("#checkItemsParent").find("div").load(url,params,function(){
				//$("#inspectionForm").validate({});
				bindCustomEvent();
				$("input[patrolConclusion]").each(function(index,obj){
					caculateUqualifiedAmount($(obj).attr("patrolConclusion"));
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
		//计量的值不符合范围时的事件
		$(":input[patrolName]").bind("change",resultChange);
		//绑定删除的事件
		var saveMode = '<%=MfgPatrolInspectionReportAction.getCurrentSaveMode()%>';
		if(saveMode == '<%=MfgCheckInspectionReport.SAVE_MODE_HISTORY%>'){
			$("td .checkItemsHeader").each(function(index,obj){
				if(index>0){
					bindRemovePatrolEvent(obj);
				}
			});		
		}
	}
	function bindImgZoom1(){
		var pHeight = $("#img").closest("td").height()-4;
		var pWidth = $("#img").closest("td").width()-20;
		var imgFileId = $("#imgFileId").val();
		$("#img").closest("a").css({
			'z-index':1000,
			top:'2px',
			left:'2px',
			position:'absolute'
		});
		$("#img").css({
			width:pWidth - 15
		}).bind("load",function(){
			$(this).show();
			var imgWidth = $(this).width();
			var imgHeight = $(this).height();
			var rate = imgWidth/imgHeight;
			
			//如果图片高度大于td的高度,则缩放
			if(imgHeight>pHeight){
				imgHeight = pHeight;
				$("#img").height(pHeight);
				imgWidth = pHeight*rate;
				$("#img").width(imgWidth);
			}
			//设置定位
			$("#img").closest("a").css({
				left:((pWidth-imgWidth)/2+2)+"px",
				top:((pHeight-imgHeight)/2+2)+"px"
			});
			//添加放大镜
			$("#img").closest("a").attr("href",$(this).attr("src"));
			
			$("#img").closest("a").jqzoom({
				zoomWidth: imgWidth*2.5,   
	        	zoomHeight: imgHeight*2.5, 
	            xOffset: 10, 
	            yOffset: 0,
	            position: "right" 
			});
		});
		if(imgFileId){
			$("#img").attr("src",$.getDownloadPath(imgFileId));
		}
	}
	function bindRemovePatrolEvent(obj){
		$(obj).mouseover(function(){
			$(this).css({background:'yellow',cursor:'pointer'})
			.attr("title","双击移除本次巡检记录!");
		}).mouseout(function(){
			$(this).css({background:''});
		}).dblclick(movePatrolRec);
	}
	function resultChange(){
		var type = this.type;
		//如果是输入文本框
		if(type == 'text'){
			if(!isNaN(this.value)){
				var parentTr = $(this).closest("tr");
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
				}
			}
		}
		caculateUqualifiedAmount($(this).attr("patrolTime"));
	}
	//计算总的检验数量
	function caculateTotalAmount(){
		var qualifiedAmount = 0,unqualifiedAmount = 0;
		$(":input[patrolConclusion]").each(function(index,obj){
			if("合格"==obj.value){
				qualifiedAmount++;
			}else{
				unqualifiedAmount++;
			}
		});
		//巡检次数
		var patrolTimes = qualifiedAmount + unqualifiedAmount;
		$("#patrolTimes").val(patrolTimes);
		$("#patrolTimes").parent().find("span").html(patrolTimes);
		//不合格次数
		$("#patrolUnqualifiedTimes").val(unqualifiedAmount);
		$("#patrolUnqualifiedTimes").parent().find("span").html(unqualifiedAmount);
		//合格次数
		$("#patrolQualifiedTimes").val(qualifiedAmount);
		$("#patrolQualifiedTimes").parent().find("span").html(qualifiedAmount);
	}
	//判断是否合格
	function caculateUqualifiedAmount(patrolTime){
		var count = 0;
		$(":input[patrolTime="+patrolTime+"]").each(function(index,obj){
			if(obj.type == 'text'){
				if($(obj).attr("color") == 'red'){
					count++;
					return false;
				}
			}else{
				if(this.value == 'NG'){
					count++;
					return false;
				}
			}
		});
		if(count>0){
			$(":input[patrolConclusion="+patrolTime+"]").val("不合格");
			$("span[patrolConclusionSpan="+patrolTime+"]").html("不合格");
		}else{
			$(":input[patrolConclusion="+patrolTime+"]").val("合格");
			$("span[patrolConclusionSpan="+patrolTime+"]").html("合格");
		}
		caculateTotalAmount();
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
	
	function printAndPreview() {	
		window.open("${mfgctx}/patrol-inspection/print.htm?id=${id}","巡检报告打印");	
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
			hiddenInputId : 'inspectionDatas'
		});
 	}
 	//添加巡检记录
 	function addPatrolRec(btn){
 		//表头
 		var $td = $(".checkItemsHeader").last();
 		var cloneObj = $td.clone(false);
 		//下一次的时间间隔
 		var interval = <%=PropUtils.getProp("mfg.patrol.interval")%>;
 		var lastDateTimeStr = cloneObj.find("input").val();
 		var strs = lastDateTimeStr.split(" ")[1].split(":");
 		var yearAndMonthStrs = lastDateTimeStr.split(" ")[0].split("-");
 		var lastDate = new Date();
 		lastDate.setFullYear(parseInt(yearAndMonthStrs[0]));
 		lastDate.setHours(parseInt(yearAndMonthStrs[1])-1);
 		lastDate.setDate(parseInt(yearAndMonthStrs[2]));
 		lastDate.setHours(parseInt(strs[0]));
 		lastDate.setMinutes(parseInt(strs[1]));
 		var milliseconds = interval*60*60*1000;
 		var date = new Date(lastDate.getTime() + milliseconds);
 		var mon = date.getMonth()+1,d=date.getDate(),h=date.getHours(),m=date.getMinutes();
 		var patrolTime = date.getFullYear() + "-" + (mon<10?"0"+mon:mon) + "-" + (d<10?"0"+d:d) + " " + (h<10?"0"+h:h) + ":" + (m<10?"0"+m:m);
 		cloneObj.find("input").val(patrolTime);
 		//绑定日期选择
 		var $dateTimeObj = cloneObj.find(".hasDatepicker").removeAttr("id")
 		.removeClass("hasDatepicker valid");
 		bindDateTimeChange($dateTimeObj);
 		//绑定时间到span
 		cloneObj.find("span").html(patrolTime);
 		var next = $td.next();
 		if(next.length>0){
 			next.replaceWith(cloneObj);
 		}else{
 			$td.after(cloneObj);
 		}
 		bindRemovePatrolEvent(cloneObj);
 		
 		//内容
 		$(".patrolItemTr").each(function(index,obj){
 			var $lastTd = $(obj).find("td.checkItemsClass").last();
 			var cloneTd = $lastTd.clone(true);
 			var checkItemName = $lastTd.find(":input[patrolName]").attr("patrolName").split("_")[0];
 			cloneTd.find(":input").attr("patrolName",checkItemName + "_" + patrolTime)
 			.attr("patrolTime",patrolTime)
 			.val("");
 			cloneTd.find("input").attr("color","black");
 			var next = $lastTd.next();
 			if(next.length>0){
 				next.replaceWith(cloneTd);
 			}else{
 				$lastTd.after(cloneTd);
 			}
 		});
 		//检验结果
 		$lastTd = $(".checkItemConclusion").last();
 		cloneObj = $lastTd.clone(true);
 		cloneObj.find(":input").attr("patrolConclusion",patrolTime).val("合格");
 		cloneObj.find("span").attr("patrolConclusionSpan",patrolTime).html("合格");
 		var next = $lastTd.next();
 		if(next.length>0){
			next.replaceWith(cloneObj);
		}else{
			$lastTd.after(cloneObj);
		}
		//备注
 		$lastTd = $(".patrolItemRemark").last();
 		cloneObj = $lastTd.clone(true);
 		cloneObj.find(":input").attr("patrolRemark",patrolTime).val("");
 		var next = $lastTd.next();
 		if(next.length>0){
			next.replaceWith(cloneObj);
		}else{
			$lastTd.after(cloneObj);
		}
 		//计算总的巡检批数
 		caculateTotalAmount();
 		//重新定位滚动条
 		initScrollDiv();
 	}
 	
 	function bindDateTimeChange($obj){
 		$obj.datetimepicker({
			changeYear:'true',
			changeMonth:'true',
			onClose:timeChange
		});
 	}
 	function timeChange(dateText,obj){
		var val = dateText;
		var $obj = $("#" + obj.id);
		if(val){
			//检查是否有其他相同的日期和时间
			var isExist = false;
			$(":input[hisVal]").each(function(){
				if(obj.id != this.id && this.value == val){
					isExist = true;
					return false;
				}
			});
			var $dateTimeObj = $obj.closest("td").find(":input.patrolDateTime");
			var hisVal = $dateTimeObj.val();
			if(isExist){
				alert("已经存在相同的巡检日期!");
				$obj.val(hisVal);
			}else{
				//内容
		 		$(".patrolItemTr").each(function(index,obj){
		 			var checkItemName = $(obj).find(":input[name=checkItemName]").val();
		 			$(obj).find(":input[patrolName="+checkItemName + "_" + hisVal+"]").attr("patrolName",checkItemName + "_" + val)
		 			.attr("patrolTime",val);
		 		});
		 		//检验结果
		 		$(".checkItemConclusion").find(":input[patrolConclusion="+hisVal+"]")
		 		.attr("patrolConclusion",val)
		 		.next().attr("patrolConclusionSpan",val);
		 		$dateTimeObj.val(val);
		 		$obj.attr("hisVal",val);
		 		//备注
		 		$(".patrolItemRemarkTr").find(":input[patrolItemRemark="+hisVal+"]")
		 		.attr("patrolItemRemark",val);
			}
		}
	}
 	function movePatrolRec(event){
 		$t = $(event.target);
 		if($t.hasClass("hasDatepicker")){
 			return;
 		}
 		if(confirm("确定要移除本次巡检记录吗?")){
 			var patrolTime = $(this).find(":input.patrolDateTime").val();
 			//移除头部
 			$(this).remove();
 			//移除内容
 			$(":input[patrolTime=" + patrolTime + "]").closest("td").remove();
 			//移除结果
 			$("input[patrolConclusion="+patrolTime+"]").closest("td").remove();
 			//移除备注
 			$(":input[patrolRemark="+patrolTime+"]").closest("td").remove();
 			//计算总的巡检批数
 			caculateTotalAmount();
 			//检查空格
 			checkEmpty();
 		}
 	}
 	//重置巡检状态
 	function rePatrol(){
 		if(confirm("确定要重置巡检状态吗?")){
 			var url = '${mfgctx}/patrol-inspection/re-patrol.htm';
 			$("#message").html("正在重置巡检状态,请稍候... ...");
 			$.post(url,{deleteIds:'${id}'},function(result){
 				$("#message").html("");
 				if(result.error){
 					alert(result.message);
 				}else{
 					window.location.href = '${mfgctx}/patrol-inspection/input.htm?id=${id}&workshop=${workshop}&inspectionPointId=${inspectionPointId}';
 				}
 			},"json");
 		}
 	}
 	function checkEmpty(){
 		//补全表格
		var pWidth = $("#checkItemsParent").width(),itemWidth = $("#checkItemsParent").find("table").width();
		if(itemWidth<pWidth){
			var width = pWidth - itemWidth;
			var total = width/200;
			if(width%200>10){
				total++;
			}
			$("#checkItemsParent").find("tr").each(function(index,obj){
				for(var i=0;i<total;i++){
					$(obj).append("<td style='padding:0px;width:140px;text-align:center;border-top:0px;'>&nbsp;</td>");						
				}
			});
		}
		//重新定位滚动条
 		initScrollDiv();
 	}
 	//切换表单录入方式 
 	function changeSaveMode(saveMode){
 		$("#saveModeForm").find(":input[name=saveMode]").val(saveMode);
 		$("#saveModeForm").submit(); 	
 	}
 	function selectInspectionPoint(obj){
		window.location.href = encodeURI('${mfgctx}/patrol-inspection/input.htm?inspectionPoint=' + obj.value);
	}
 	function submitProcessCard(url){
 		var processCard=$("#processCard").val();
 		if(processCard=""){
 			return;
 		}
 		window.location.href=url+"?processCard="+processCard;
 	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu='data_acquisition';
		var thirdMenu="patrolTemporary";
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
				<div class="opt-btn">
					<security:authorize ifAnyGranted="mfg-inspection-patrol-inspection-save">
					<s:if test='patrolState.equals("巡检中")'>
					<button class='btn' type="button" onclick="submitForm('${mfgctx}/patrol-inspection/save.htm');"><span><span><b class="btn-icons btn-icons-save"></b>暂存</span></span></button>
					</s:if>
					<s:if test='patrolState.equals("巡检中")'>
					<button class='btn' type="button" onclick="submitForm('${mfgctx}/patrol-inspection/save.htm?isSubmit=true');"><span><span><b class="btn-icons btn-icons-submit"></b>提交</span></span></button>
					</s:if>
					</security:authorize>
					<security:authorize ifAnyGranted="mfg-patrol-inspection-re-patrol">
					<s:if test='!patrolState.equals("巡检中")'>
					<button class='btn' type="button" onclick="rePatrol();"><span><span><b class="btn-icons btn-icons-edit"></b>重置状态</span></span></button>
					</s:if>
					</security:authorize>
					<button class='btn' onclick="javascript:window.location='${mfgctx}/patrol-inspection/input.htm?inspectionPoint=${inspectionPoint}';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<s:if test="id>0">
					<button class='btn' type="button" onclick="printAndPreview();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>
					</s:if>	
					<!-- <button class='btn' onclick="SaveAsFile();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button> -->
					<span style="margin-left:6px;line-height:30px;color:red;" id="message">
						<s:actionmessage theme="mytheme"/>
						<s:if test='!patrolState.equals("巡检中")'>
						巡检报告已完工,不能修改!
						</s:if>
					</span>
					<div style="float:right;">
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
					<s:if test='patrolState.equals("巡检中")'>
						<%
							String saveMode = MfgPatrolInspectionReportAction.getCurrentSaveMode();
							if(MfgCheckInspectionReport.SAVE_MODE_HISTORY.equals(saveMode)){
						%>
						<button class='btn' type="button" onclick="javascript:changeSaveMode('<%=MfgCheckInspectionReport.SAVE_MODE_PATROL%>');" style="margin-left:4px;"><span><span><b class="btn-icons btn-icons-change"></b>切换到巡检模式</span></span></button>
						<button class='btn' type="button" onclick="javascript:addPatrolRec();" style=""><span><span><b class="btn-icons btn-icons-add"></b>添加巡检记录</span></span></button>
						<%}else{ %>
						<button class='btn' type="button" onclick="javascript:changeSaveMode('<%=MfgCheckInspectionReport.SAVE_MODE_HISTORY%>');" style=""><span><span><b class="btn-icons btn-icons-date"></b>切换到历史记录模式</span></span></button>
						<%} %>
					</s:if>
					</div>
				</div>
				<div id="opt-content" style="text-align: center;">
				<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
					<div style="">&nbsp;</div>
				</div>
				<div class="container"><div class="content">&nbsp;</div>
				<s><i></i></s></div>
				<jsp:include page="form.jsp"/>
				<form action="${mfgctx}/patrol-inspection/input.htm?inspectionPoint=${inspectionPoint}" method="post" id="saveModeForm" name="saveModeForm" enctype="multipart/form-data">
					<input type="hidden" name="id" value="${id}"></input>
					<input type="hidden" name="saveMode"></input>
				</form>
			</div>
		</div>
	</div>
</body>
</html>