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
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
		$.parseDownloadPath({
			showInputId : 'showInspectionDatas',
			hiddenInputId : 'inspectionDatas'
		});
		$("#opt-content").width($(window).width()-20);
		$("#scroll").bind("scroll",function(){
			$("#checkItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#checkItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#checkItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
		$(":input").attr("disabled","disabled");
		$("a.small-button-bg").remove();
		$("#opt-content button").remove();
		//caculateTotalAmount();
		contentResize();
		setTimeout(function(){
			$("#message").html("");
		},3000);
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
	//添加各种事件
	function bindCustomEvent(){
		//不合格数
		$("#checkItemsParent").find(":input[fieldName=unqualifiedAmount]").bind("change",function(){
			unqualifiedAmountChange(this);
		});
		//合格数
		$("#checkItemsParent").find(":input[fieldName=qualifiedAmount]").bind("change",function(){
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
	
 	//上传检验数据
 	function uploadInspectionDatas(){
 		$.upload({
			appendTo : '#opt-content',
			showInputId : 'showInspectionDatas',
			hiddenInputId : 'inspectionDatas',
		});
 	}
 	//发起不合格品处理流程
	function beginDefectiveGoodsProcessForm(){
		window.location.href = '${mfgctx}/defective-goods/ledger/input.htm?qualityTestingReportNo=${inspectionNo}&sourceType=pqc';
	}
	</script>
</head>

<body>
	<div class="ui-layout-center">
			<div class="opt-body">
					<div id="opt-content" style="text-align: center;overflow:auto;">
					<div id="scroll" style="position:absolute;top:0px;left:15px;overflow-y:hidden;overflow-x:auto;height:35px;line-height:35px;display:block;z-index:2;">
						<div style="">&nbsp;</div>
					</div>
					<form action="" method="post" id="inspectionForm" name="inspectionForm" enctype="multipart/form-data">
						<table class="form-table-border-left" style="width:100%;border:0px;">
						<caption style="height: 25px"><h2>检验报告</h2></caption>
						<caption style="text-align:right;padding-bottom:4px;">编号:${inspectionNo}</caption>
						<tr>
							<td style="width:15%;">事业部</td>
							<td style="width:18%;">
								<input name="businessUnitCode" value="${businessUnitCode}" type="hidden"></input>
								<input name="businessUnitName" value="${businessUnitName}" type="hidden"></input>
								<span>${businessUnitName}</span>
							</td>
							<td style="width:15%;">工段</td>
							<td style="width:18%;">
								<input name="section" value="${section}" type="hidden"></input>
								<span>${section}</span>
							</td>
							<td style="width:15%;">生产线</td>
							<td style="width:18%;">
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
							<td colspan="3">
								<input type="radio" name="isBatchProduct" value="是" <s:if test="%{isBatchProduct==\"是\"}">checked="checked"</s:if>/>是
								<input type="radio" name="isBatchProduct" value="否" <s:if test="%{isBatchProduct==\"否\"}">checked="checked"</s:if>/>否
							</td>
						</tr>
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
							<td>
								<input type="hidden" name="hisAttachmentFiles" value='${attachmentFiles}'></input>
								<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${attachmentFiles}'></input>
								<button  class='btn' type="button" onclick="uploadFiles();"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
							</td>
							<td colspan="5" id="showAttachmentFiles">
							</td>
						</tr>
						<tr>
							<td style="border-left:0px;border-top:0px;">检验判定</td>
							<td style="border-top:0px;">
								<span><s:if test="inspectionConclusion=='OK'">合格</s:if><s:if test="inspectionConclusion=='NG'">不合格</s:if></span>
								<input name="inspectionConclusion" id="inspectionConclusion" value="${inspectionConclusion}" type="hidden"></input>
							</td>
							<td>
								审核员
							</td>
							<td >
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