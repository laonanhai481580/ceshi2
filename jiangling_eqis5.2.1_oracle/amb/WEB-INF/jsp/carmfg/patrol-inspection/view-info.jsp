<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script language="javascript" src="${ctx}/widgets/lodop/LodopFuncs.js"></script>
	<script language="javascript" src="${ctx}/widgets/jqzoom_ev-2.3/js/jquery.jqzoom-core.js"></script>
	<link   type="text/css" rel="stylesheet" href="${ctx}/widgets/jqzoom_ev-2.3/css/jquery.jqzoom.css"/>
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
		//只能修改状态为巡检中的报告
		$("#opt-content :input").attr("disabled","disabled");
		$("#opt-content").find("button").remove();
		$("#opt-content .small-button-bg[onclick]").remove();
		caculateTotalAmount();
		contentResize();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
	});
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
	//绑定图片放大镜事件
	function bindImgZoom(){
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
				zoomWidth: imgWidth*1.5,   
	        	zoomHeight: imgHeight*1.5, 
	            xOffset: 10, 
	            yOffset: 0,
	            position: "right" 
			});
		});
		if(imgFileId){
			$("#img").attr("src",$.getDownloadPath(imgFileId));
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
	//补全空表格
	function checkEmpty(){
		var pWidth = $("#checkItemsParent").width(),itemWidth = $("#checkItemsParent").find("table").width();
		if(itemWidth<pWidth){
			var width = pWidth - itemWidth;
			var total = width/140;
			if(width%140>10){
				total++;
			}
			$("#checkItemsParent").find("tr").each(function(index,obj){
				for(var i=0;i<total;i++){
					$(obj).append("<td style='padding:0px;width:140px;text-align:center;border-top:0px;'>&nbsp;</td>");						
				}
			});
		}
 	}
	</script>
</head>
<body>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn" id="btnDiv">
					<button class='btn' type="button" onclick="prn1_preview();"><span><span><b class="btn-icons btn-icons-print"></b>打印</span></span></button>
					<button class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-close"></b>关闭</span></span></button>		
				</div>
				<div id="opt-content" style="text-align:center;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:400;">
						<div style="">&nbsp;</div>
					</div>
					<div class="container"><div class="content">&nbsp;</div>
					<s><i></i></s></div>
					<%
						request.setAttribute("isView",true);
					%>
					<jsp:include page="form.jsp"/>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>