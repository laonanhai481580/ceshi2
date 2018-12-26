<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
	function contentResize(){
		$("#checkItemsParent").find("div").width($(".ui-layout-center").width()-30);
		$("#opt-content").height($(".ui-layout-center").height()-55);
	}
	var topMenu ='';
	$(document).ready(function(){
		$('#enterDate').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd'});});
		$('#inspectionDate').click(function(){WdatePicker({dateFmt:'yyyy-MM-dd HH:mm'});});
		$("#stockAmount").bind("change",function(){
			if(!isNaN(this.value)&&this.value.indexOf(".")==-1){
				loadCheckItems();
			}
		});
		bindCustomEvent();
		caculateTotalAmount();
		contentResize();
		showMsg();
		$.parseDownloadPath({
			showInputId : 'showAttachmentFiles',
			hiddenInputId : 'attachmentFiles'
		});
	});
	function submitForm(url){
		if($("#inspectionForm").valid()){
			$("input[name=checkItemStrs]").val(getCheckItemStrs());
			$('#inspectionForm').attr('action',url);
			$("#message").html("<b>数据保存中,请稍候... ...</b>");
			$(".opt-btn .btn").attr("disabled",true);
			$('#inspectionForm').submit();
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
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",iframe:true, innerWidth:700, innerHeight:400,
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
		$.colorbox({href:"${supplierctx}/archives/select-supplier.htm",iframe:true, innerWidth:1000, innerHeight:600,
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
		var supplierId = $("#supplierId").val();
		var checkBomCode = $("#checkBomCode").val();
		var stockAmount = $("#stockAmount").val();
		if(!supplierId||!checkBomCode||!stockAmount){
// 			$("#checkItemsParent").find("div").html("&nbsp;");
		}else{
			var params = {
				supplierId : supplierId,
				checkBomCode : checkBomCode,
				stockAmount : stockAmount,
				inspectionDate : inspectionDate,
				checkItemStrs : getCheckItemStrs()
			};
			$("#checkItemsParent").find("div").html("<div style='padding:4px;'><b>检验项目加载中,请稍候... ...</b></div>");
			var url = "${iqcctx}/inspection-report/check-items.htm";
			$("#checkItemsParent").find("div").load(url,params,function(){
				$("#inspectionForm").validate({});
				bindCustomEvent();
				//更新不合格数和合格数
				$(".checkItemsClass").each(function(index,obj){
					caculateUqualifiedAmount(obj);
				});
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
			if(obj.value){
				if(parseInt(obj.value)>unqualifiedAmount){
					unqualifiedAmount = parseInt(obj.value);
				}
			}
		});
		$("#unqualifiedAmount").val(unqualifiedAmount);
		$("#unqualifiedAmount").parent().find("span").html(unqualifiedAmount);
		//合格数量 
		var qualifiedAmount = 0;
		$("#checkItemsParent").find("input[name=qualifiedAmount]").each(function(index,obj){
			if(obj.value){
				if(parseInt(obj.value)>qualifiedAmount){
					qualifiedAmount = parseInt(obj.value);
				}
			}
		});
		$("#qualifiedAmount").val(qualifiedAmount);
		$("#qualifiedAmount").parent().find("span").html(qualifiedAmount);
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
		var secMenu="inspectionReport";
		var thirdMenu="myInspectionReportInput";
	</script>
	
	<div class="ui-layout-center">
			<div class="opt-body">
					<div class="opt-btn">
					<button  class='btn' type="button" onclick="javascript:window.parent.$.colorbox.close();" style="float:left;margin-left:6px;"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
						<%
							String myid = request.getParameter("myId");
							if(StringUtils.isNotEmpty(myid)){
						%>
						<button  class='btn' type="button" style="float:left;margin-left:6px;" onclick="javascript:window.parent.callList(<%=myid%>);"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
						<%} %>
					</div>
					<s:if test="error">
					<div id="opt-content">
						<h2>对不起，没有符合条件的单据或该单据已删除！</h2>
					</div>
					</s:if><s:else>
					<div id="opt-content" style="text-align: center;">
					<form action="" method="post" id="inspectionForm" name="inspectionForm">
						<table class="form-table-border-left" style="width:100%;margin: auto;">
							<caption style="height: 25px"><h2>进货检验报告</h2></caption>
							<caption style="text-align:right;padding-bottom:4px;">编号:${incomingInspectionActionsReport.inspectionNo}</caption>
							<tr>
								<td style="width:10%;">供应商</td>
								<td style="width:23%;">
								${incomingInspectionActionsReport.supplierName}
								</td >
								<td  style="width:10%;">到货日期</td>
								<td  style="width:23%;">
								${incomingInspectionActionsReport.enterDateStr}
								</td>
								<td style="width:10%;">检验日期</td>
								<td style="width:24%;">
								<s:date name="incomingInspectionActionsReport.inspectionDate" format="yyyy-MM-dd HH:mm"/>
								</td>
							</tr>
							<tr>
								<td>物料编码</td>
								<td>
								${incomingInspectionActionsReport.checkBomCode}
								</td>
								<td>物料名称</td>
								<td>${incomingInspectionActionsReport.checkBomName}</td>
								<td>批次号</td>
								<td>${incomingInspectionActionsReport.batchNo}</td>
							</tr>
							<tr>
								<td>来料数量</td>
								<td colspan="3">
								${incomingInspectionActionsReport.stockAmount}
								</td>
								<td>检验状态</td>
								<td>
									<s:if test="#incomingInspectionActionsReport.sampleSchemeType=='加严'}">
										<font color=red><b>${incomingInspectionActionsReport.sampleSchemeType}</b></font>
									</s:if>
									<s:else>
										<b>${incomingInspectionActionsReport.sampleSchemeType}</b>
									</s:else>
								</td>
							</tr>
							<tr>
								<td colspan="6" style="padding:0px;" id="checkItemsParent">
									<div style="width:1130px;height:100%;overflow:auto;">
										<%@ include file="check-items.jsp"%>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="4">
								</td>
								<td style="border-left:0px;">检验判定</td>
								<td>
									<span>${incomingInspectionActionsReport.inspectionConclusion}</span>
								</td>
							</tr>
							<tr>
								<td>检验数</td>
								<td>
									<span>${incomingInspectionActionsReport.inspectionAmount}</span>
								</td>
								<td>合格数</td>
								<td>
									<span>${incomingInspectionActionsReport.qualifiedAmount}</span>
								</td>
								<td>不良数</td>
								<td>
									<span>${incomingInspectionActionsReport.unqualifiedAmount}</span>
								</td>
							</tr>
							<tr>
								<td>环保是否检验</td>
								<td>
									<input type="radio" name="isCheckEnvironment" value="是" <s:if test="%{#incomingInspectionActionsReport.isCheckEnvironment==\"是\"}">checked="checked"</s:if>/>是
									<input type="radio" name="isCheckEnvironment" value="否" <s:if test="%{#incomingInspectionActionsReport.isCheckEnvironment==\"否\"}">checked="checked"</s:if>/>否
								</td>
								<td >此批是否新料</td>
								<td>
									<input type="radio" name="isNewBom" value="是" <s:if test="%{#incomingInspectionActionsReport.isNewBom==\"是\"}">checked="checked"</s:if>/>是
									<input type="radio" name="isNewBom" value="否" <s:if test="%{#incomingInspectionActionsReport.isNewBom==\"否\"}">checked="checked"</s:if>/>否
								</td>
								<td >环保检验是否OK</td>
								<td>
									<input type="radio" name="isPassEnvironment" value="是" <s:if test="%{#incomingInspectionActionsReport.isNewBom==\"是\"}">checked="checked"</s:if>/>是
									<input type="radio" name="isPassEnvironment" value="否" <s:if test="%{#incomingInspectionActionsReport.isNewBom==\"否\"}">checked="checked"</s:if>/>否
								</td>
							</tr>
							<tr>
								<td>
									<input type="hidden" name="hisAttachmentFiles" value='${incomingInspectionActionsReport.attachmentFiles}'></input>
									<input type="hidden" name="attachmentFiles" id="attachmentFiles" value='${incomingInspectionActionsReport.attachmentFiles}'></input>
									<span><span>附件</span></span>
								</td>
								<td colspan="5" id="showAttachmentFiles">
								</td>
							</tr>
							<tr>
								<td>结果</td>
								<td>
									<input type="radio" name="processingResult" value="允收" <s:if test="%{#incomingInspectionActionsReport.processingResult==\"允收\"}">checked="checked"</s:if>/>允收
									<input type="radio" name="processingResult" value="判退" <s:if test="%{#incomingInspectionActionsReport.processingResult==\"判退\"}">checked="checked"</s:if>/>判退
									<input type="radio" name="processingResult" value="特裁" <s:if test="%{#incomingInspectionActionsReport.processingResult==\"特裁\"}">checked="checked"</s:if>/>特裁
								</td>
								<td>审核员</td>
								<td>
									${incomingInspectionActionsReport.auditMan}
								</td>
								<td style="border-left:0px;">检验员</td>
								<td>
									${incomingInspectionActionsReport.inspector}
								</td>
							</tr>
						</table>
					</form>
				</div>
				</s:else>
			</div>
	</div>
	
</body>
</html>