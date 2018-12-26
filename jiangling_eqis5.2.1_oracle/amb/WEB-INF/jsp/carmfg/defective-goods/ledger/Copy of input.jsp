<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
String userName=ContextUtils.getUserName();
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			if($("input[name='need8d']:checked").val()=="0"){
				$("#show8D").removeAttr("style");
				$("input[name='need8dNo']").addClass("{required:true,messages:{required:'必填'}}");
			} else{
				$("#show8D").css("display","none");
				$("input[name='need8dNo']").removeClass("{required:true,messages:{required:'必填'}}");
			}
			$("input[name='need8d']").bind("click",function(){
				var obj=this;
				if($(obj).val()=="0"){
					$("#show8D").removeAttr("style");
					$("input[name='need8dNo']").addClass("{required:true,messages:{required:'必填'}}");
				} else{
					$("#show8D").css("display","none");
					$("input[name='need8dNo']").val("");
					$("input[name='need8dNo']").removeClass("{required:true,messages:{required:'必填'}}");
				}
			});

			$("input[name^='inspectDate']").each(function(index,obj){
				$(obj).datepicker({changeMonth:true,changeYear:true});
			});
			$("input[name='completeDate']").datepicker({changeMonth:true,changeYear:true});
			$("input[name='reportDate']").datepicker({changeMonth:true,changeYear:true});
			
			$(":checkbox[name=defectType]").bind("click", singleCheck);
			$(":checkbox[name=qualityOpinion]").bind("click", singleCheck);
			$(":checkbox[name=concessionResult]").bind("click", singleCheck);
			$(":checkbox[name=need8d]").bind("click", singleCheck);
			$(":checkbox[name=concessionResult]").bind("click", singleCheck);
			$(":checkbox[name=isAgreeQO]").bind("click", singleCheck);
			showAttachment("reviewAttachment");
			addFormValidate('${fieldPermission}', 'defectiveGoodsForm');
			
			var fieldPermission = ${fieldPermission};
			for(var i=0;i<fieldPermission.length;i++){
				var obj=fieldPermission[i];
				if((obj.name=="inspectionDept" || obj.name=="inspector") && obj.request=="false"){
					$("input[name="+obj.name+"]").removeAttr("onclick");
				}else if(obj.name=="reviewAttachment" && obj.request=="false"){
					$("#uploadFile").removeAttr("onclick");
				}else if(obj.name=="need8d" && obj.request=="false"){
					rowUneditable("composingItemTr");
				};
			};
		});
		
		function rowUneditable(classObj){
			$("."+classObj).each(function(index, obj){
				$(obj).find("a").removeAttr("onclick");
				$(obj).find("input").attr("disabled","disabled").css("background-color","#fafafa").css("color","#303030");
	  		});
		}

	 	function submitForm(url,type){
			if($("#defectiveGoodsForm").valid()){
				getDetailItems();
				getComposingItems();
				$('#defectiveGoodsForm').attr('action',url);
				if(type=='0'){
					$(".opt-btn .btn").attr("disabled",true);
					$("#message").html("<b>数据保存中,请稍候... ...</b>");
					$('#defectiveGoodsForm').submit();
				}else{
					$(".opt-btn .btn").attr("disabled",true);
					$("#message").html("<b>数据提交中,请稍候... ...</b>");
					$('#defectiveGoodsForm').submit();
				}
			}else{
				var error = $("#defectiveGoodsForm").validate().errorList[0];
				$(error.element).focus();
			}
		}
		//获取表单的值
		function getParams(){
			var params = {};
			$(":input","form").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
					if(obj.type == 'radio'){
						if(obj.checked){
							params[obj.name] = jObj.val();
						}else if(!params[obj.name]){
							params[obj.name] = '';
						}
					}else if(obj.type == 'checkbox'){
						if(obj.checked){
							if(!params[obj.name]){
								params[obj.name] = jObj.val();
							}else{
								params[obj.name] = params[obj.name] + "," + jObj.val();
							}
						}else{
							if(!params[obj.name]){
								params[obj.name] = '';
							}
						}
					}else{
						params[obj.name] = jObj.val();
					}
				}
			});
			return params;
		}
		function addNew(){
			window.location='${mfgctx}/defective-goods/ledger/input.htm';
		}

		function selectObj(title,obj,treeType){
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:treeType,
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.id,
				showInputId:obj.id,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}

		//选择质量成本
		var composingRowId;
		function selectComposing(rowId){
			composingRowId = rowId;
			var url = '${costctx}/common/composing-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
		}
		function setFullComposingValue(datas){
			$("#name"+"-"+composingRowId).val(datas[0].name);
			$("#code"+"-"+composingRowId).val(datas[0].code);
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
	 	var isModel=0;
	 	function selectComponentCode(rowId,flag){
	 		bomRowId=rowId;
	 		isModel=flag;
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:650, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择BOM零部件"
	 		});
	 	}
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(datas){
	 		if(isModel==0){
		 		$("#productCode"+"-"+bomRowId).val(datas[0].key);
		 		$("#productName"+"-"+bomRowId).val(datas[0].name);
	 		}
	 		else if(isModel==1){
	 			$("#modelSpecification"+"-"+bomRowId).val(datas[0].model);
	 		}
	 	}
	 	
	 	//获取不良项目和 成本
		function addRowHtml(totalObj,classObj,obj){
	 		var tr = $(obj).parent("div").parent("td").parent("tr");
	 		var clonetr = tr.clone(false);
	 		tr.after(clonetr);
	 		var total = $("#"+totalObj);
			var num = total.val();
			clonetr.find(":input").each(function(index ,obj){
	 			obj=$(obj);
	 			var name=obj.attr("name").split("-")[0];
	 			obj.attr("id",name+"-"+num).val("");
	 			obj.attr("name",name+"-"+num);
	 			if(name=="inspectDate"){
	 			 	obj.removeAttr("class").datepicker({changeYear:'true',changeMonth:'true'});
	 			 	obj.addClass("{required:true,messages:{required:'必填'}}");
	 			}else if(name=="productCode"){
	 				obj.click(function(){selectComponentCode(num,0);});
	 			}else if(name=="modelSpecification"){
	 				obj.next('a').click(function(){selectComponentCode(num,1);});
	 			}else if(name=="name"){
	 				obj.click(function(){selectComposing(num);});
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
			var tr=$(obj).parent("div").parent("td").parent("tr");
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
			$(obj).find(":input").each(function(index,obj){
				iobj = $(obj);
			    value += ",\""+iobj.attr("name").split("-")[0]+"\":\""+iobj.val()+"\"";
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
		function showAttachment(name){
			var files = $("#"+name).val();
			if(files.length>0){
		 		$.parseDownloadPath({
					showInputId : 'showAttachment',
					hiddenInputId : name
				});
			}
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
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_form";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-defective-goods-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn">
				<div  id="btnDiv">
					<button class='btn' onclick="javascript:window.location='${mfgctx}/defective-goods/ledger/input.htm';" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<s:if test="launchState==0">
					<button  class='btn' type="button" onclick="submitForm('${mfgctx}/defective-goods/ledger/save.htm','0')"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					<button  class='btn' type="button" onclick="submitForm('${mfgctx}/defective-goods/ledger/submit-process.htm','1')"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
					</s:if>
					<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
				</div>
			</div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="defectiveGoodsForm">
					<div>
						<input type="hidden" name="id" id="id" value="${id}"/>
						<input type="hidden" name="taskId" id="taskId" value="${taskId }" />
					</div>
					<div style="height: 30px;text-align: center"><h2>不合格品处理单</h2></div>
					<div style="height: 25px;width:99%;text-align: right;">
						<span>No.<span style="padding: 1px 11px 1px 1px;">${formNo}</span></span><input type="hidden" name="formNo" style="padding:0 4px;" value="${formNo}"/>
					</div>

					<table class="form-table-border-left" id="rejectTable"	style="width:100%;margin: auto;">
						<tr>
							<td style="text-align:center;" colspan="6">
								<s:checkboxlist name="defectType" value="defectType" theme="simple" list="defectTypes" listKey="value" listValue="name"/>
							</td>
						</tr>
						<tr>
							<td style="width:14%;"><font color="red">*</font>送检单位</td>
							<td style="width:20%">
								<input type="text" name="inspectionDept" id='inspectionDept' value="${inspectionDept}" onclick="selectObj('选择单位',this,'DEPARTMENT_TREE')" style="width:90%;max-width:148px;" class="{required:true,messages:{required:'必填'}}"/>
							</td>
							<td colspan="2"></td>
							<td style=""><font color="red">*</font>统计归属</td>
							<td>
								<s:select list="industryTypeOptions" 
										  listKey="value" 
										  listValue="name"
										  theme="simple"
										  cssStyle="{required:true};width:90px;"
										  name="industryType"></s:select>
							</td>
						</tr>
						<tr>
							<td style="width:13%;"><font color="red">*</font>不合格来源</td>
							<td style="width:20%">
								<s:select list="unqualifiedSourceOptions" 
										  listKey="value" 
										  listValue="name"
										  theme="simple"
										  cssStyle="{required:true};width:90px;"
										  name="unqualifiedSource"></s:select>
							</td>
							<td style="width:13%;"><font color="red">*</font>检验单号</td>
							<td style="width:20%">
								<input style="width:90%;max-width:148px;" type="text" name="qualityTestingReportNo" value="${qualityTestingReportNo}" class="{required:true,messages:{required:'必填'}}"/>
							</td>
							<td style="width:13%;">缺陷分类</td>
							<td style="width:20%">
								<s:select list="defectLevelOptions" 
										  listKey="value" 
										  listValue="name"
										  theme="simple"
										  cssStyle="{required:true};width:90px;"
										  name="defectLevel"></s:select>
							</td>
						</tr>
						<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格品明细</td></tr>
						<tr>
							<td colspan="6" style="padding:0;">
								<table class="form-table-border" style="table-layout:fixed;border:0;">
									<tr>
								      	<td style="width:6%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
								      	<td style="width:5%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
								      	<!-- <td style="width:8%;border-top:0px;border-bottom:0px;">明细序号</td> -->
								      	<td style="width:20%;border-top:0px;border-bottom:0px;text-align:center;">产品代码</td>
								      	<td style="width:16%;border-top:0px;border-bottom:0px;text-align:center;">产品名称</td>
								      	<td style="width:16%;border-top:0px;border-bottom:0px;text-align:center;">产品型号</td>
								      	<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;">送检数</td>
										<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;">抽检数</td>
										<td style="width:6%;border-top:0px;border-bottom:0px;text-align:center;">不合格数</td>
								      	<td style="width:10%;border-top:0px;border-bottom:0px;border-right:0px;">送检日期</td>  
							        </tr>
							        <%int bcount=0; %>
							        <s:iterator value="detailItemList" var="detailItemList" id="detailItemList" status="st">
									    <tr class="detailItemTr">
									    	<td style="border-bottom:0px;border-left:0px;">
									    		<div style="margin:0 auto;width: 42px;">
									      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('detailItemTotal','detailItemTr',this)" title="添加不合格品"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
												<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('detailItemTotal','detailItemTr',this)" title="删除不合格品"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
												</div>
									 		</td>
									 		 <td style="border-bottom:0px;text-align:center;"><%=bcount+1%></td>
									      	<!--<td style="border-bottom:0px;text-align:center;"><input style="width:80%;" name="detailNo_<%=bcount%>" id="detailNo_<%=bcount%>" value="${detailNo}" class="{required:true,messages:{required:'必填'}}"/></td> -->
									      	<td style="border-bottom:0px;text-align:center;">
									      		<input style="width:90%;max-width:148px;" name="productCode_<%=bcount%>" id="productCode_<%=bcount%>" onclick="selectComponentCode(<%=bcount%>,0);" value="${productCode}" class="{required:true,messages:{required:'必填'}}"/>
									      		<a id="selectModel" class="small-button-bg" style="margin-bottom:-6px;margin-left:-5px;" onclick="selectComponentCode(<%=bcount%>,1);" href="javascript:void(0);" title="选择产品"><span class="ui-icon ui-icon-search" style='cursor:pointer;'></span></a>
									      	</td>
									      	<td style="border-bottom:0px;text-align:center;">
									      		<input style="width:90%;max-width:148px;"  name="productName_<%=bcount%>" id="productName_<%=bcount%>" value="${productName}" type="text"/>
									      	</td>
									      	<td style="border-bottom:0px;text-align:center;">
									      		<input style="width:80%;max-width:128px;" name="modelSpecification_<%=bcount%>" id="modelSpecification_<%=bcount%>" value="${modelSpecification}" class="{required:true,messages:{required:'必填'}}"/>
									      	</td>
									      	<td style="border-bottom:0px;text-align:center;"><input style="width:70%" name="tatolNum_<%=bcount%>" id="tatolNum_<%=bcount %>" value="${tatolNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
									      	<td style="border-bottom:0px;text-align:center;"><input style="width:70%" name="inspectNum_<%=bcount%>" id="inspectNum_<%=bcount %>" value="${inspectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
											<td style="border-bottom:0px;text-align:center;"><input style="width:70%" name="defectNum_<%=bcount%>" id="defectNum_<%=bcount %>" value="${defectNum}" class="{required:true,digits:true,messages:{required:'必填',digits:'必为整数'}}"/></td>
									      	<td style="border-bottom:0px;border-right:0px;"><input style="width:80%" readonly="readonly" name="inspectDate_<%=bcount%>" id="inspectDate_<%=bcount%>" value="<s:date name='inspectDate' format='yyyy-MM-dd'/>" class="{required:true,messages:{required:'必填'}}"/></td>
	                                	</tr>
	                                	<%bcount++; %>
							        </s:iterator>
						        </table>
						        <input type="hidden" name="detailItemTotal" id="detailItemTotal" value="<%=bcount%>"/>
						        <input type="hidden" name="detailItems" id="detailItems"/>
						    </td>
						</tr>
						<tr>
							<td colspan="6">
							<table style="width:100%;">
								<tr>
									<td style="border:0; font-weight: bold;">
										<span>不良描述:</span>
									</td>
								</tr>
								<tr>
									<td style="border:0;">
										<textarea id="unqualifiedDescription" rows="5" style="border: 1px solid #4C9DC5;" name="unqualifiedDescription">${unqualifiedDescription}</textarea>
									</td>
								</tr>
								<tr >
									<td style="border:0;">
										<div style="float:right;">
											<span>报告人：</span><input style="width:120px;" readonly="readonly" onclick="selectObj('请选择报告人', this, 'MAN_DEPARTMENT_TREE');" type="text" id="inspector" name="inspector" value="<s:if test="inspector">${inspector}</s:if><s:else><%=userName%></s:else>"></input>
											<span style="padding-left:20px;">日期：</span><input rea style="margin-right:20px;width:120px;" readonly="readonly" type="text" name="reportDate" id="reportDate" value='<s:date name='reportDate' format='yyyy-MM-dd'/>' class="{required:true,messages:{required:'必填'}}"></input>
										</div>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr>
							<td colspan="6" >
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">质量部处理建议:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<s:checkboxlist cssStyle="margin-left:8px;" name="qualityOpinion" value="qualityOpinion" theme="simple" list="qualityOpinions" listKey="value" listValue="name"/>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<div style="float:right;">
												<span style="width: 160px;padding-right:20px;display: inline-block;">处理人：${qualityDirector}</span><input type="hidden" id="qualityDirector" name="qualityDirector" value='${qualityDirector}'></input>
											</div>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">执行单位签署意见:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<input type="checkbox" name="isAgreeQO" id="isAgreeQO-1" <s:if test="isAgreeQO==0">checked="checked"</s:if> value="0" /><label for="isAgreeQO-1">同意质量部处理意见，</label>
											<span>执行单位填写完成日期</span><input readonly="readonly" id="completeDate" style="width:90px;padding-left:4px;margin-left:2px;" value="<s:date name='completeDate' format='yyyy-MM-dd'/>" name="completeDate" type="text"/>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<input type="checkbox" name="isAgreeQO" id="isAgreeQO-2" <s:if test="isAgreeQO==1">checked="checked"</s:if> value="1" /><label for="isAgreeQO-2">申请让步接收？</label>
											<span>原因描述:</span>
										</td>
									</tr>
									<tr>
										<td style="border:0;">
											<textarea rows="5" style="border: 1px solid #4C9DC5;" name="concessionReason" id="concessionReason">${concessionReason}</textarea>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
								<table style="width:100%;">
									<tr>
										<td style="border:0; font-weight: bold;">让步接收评审(风险评估)(并行评审)申请部门填写:</td>
									</tr>
									<tr>
										<td style="border:0;">
											<span>填写与会人员:</span>
											<input name="participants" id="participants" value="${participants}" style="width:60%;max-width: 363px;"/>
											<span style="color:#999;font-size:12px;">(以英文","隔开！)</span>
										</td>
									</tr>
									<tr>
										<td style="border:0;">
											<s:checkboxlist cssStyle="margin-left:8px;" name="concessionResult" value="concessionResult" theme="simple" list="agreeConcessionOptions" listKey="value" listValue="name"/><label>风险评估意见:</label>
										</td>
									</tr>
									<tr >
										<td style="border:0;">
											<textarea id="concessionOpinion" rows="5" style="border: 1px solid #4C9DC5;" name="concessionOpinion">${concessionOpinion}</textarea>
										</td>
									</tr>
									<tr>
										<td style="border:0px;">
											<span style="color:#777;font-size:13px;">(上传评审会议附件)</span>
											<button type="button" class='btn' id="uploadFile" onclick="uploadFiles('showAttachment','reviewAttachment');"><span><span><b class="btn-icons btn-icons-upload"></b>上传附件</span></span></button>
											<input type="hidden" name="hisAttachment" value='${reviewAttachment}'></input>
											<input type="hidden" name="reviewAttachment" id="reviewAttachment" value='${reviewAttachment}'></input>
											<span id="showAttachment"></span>
										</td>
									</tr>
								</table>
							</td>
						</tr>
						<tr>
							<td colspan="6">
							<table style="width:100%;">
								<tr>
									<td style="border:0; font-weight: bold;">
										<span>质量部最终处理方式(含后期整改意见):</span>
									</td>
								</tr>
								<tr>
									<td style="border:0;">
										<textarea id="finalApproach" rows="5" style="border: 1px solid #4C9DC5;" name="finalApproach">${finalApproach}</textarea>
									</td>
								</tr>
							</table>
							</td>
						</tr>
						<tr><td style="font-weight: bold;text-align:center;" colspan="6">不合格损失鉴定</td></tr>
						<tr>
							<td colspan="6" style="padding:0;">
								<table class="form-table-border" style="table-layout:fixed;border:0;">
									<thead>
										<tr>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;border-left:0px;text-align:center;">操作</td>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;text-align:center;">序号</td>
									      	<td style="width:20%;border-top:0px;border-bottom:0px;">质量成本科目</td>
									      	<td style="width:10%;border-top:0px;border-bottom:0px;">金额</td>
									      	<td style="width:20%;border-top:0px;border-bottom:0px;border-right:0px;">责任单位</td>  
								        </tr>
							        </thead>
							        <%int ccount=0; %>
							        <tbody>
							        <s:iterator value="composingItemList" var="composingItemList" id="composingItemList" status="st">
									    <tr class="composingItemTr">
									    	<td style="border-bottom:0px;border-left:0px;">
									    		<div style="margin:0 auto;width: 42px;">
									      		<a class="small-button-bg" style="float:left;" onclick="addRowHtml('composingItemTotal','composingItemTr',this)" title="添加损失鉴定"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
												<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="removeRowHtml('composingItemTotal','composingItemTr',this)" title="删除损失鉴定"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
												</div>
									 		</td>
									 		<td style="border-bottom:0px;text-align:center;"><%=ccount+1%></td>
									      	<td style="border-bottom:0px;">
									      		<input style="width:60%;" onclick="selectComposing(<%=ccount%>)" name="name_<%=ccount%>" id="name_<%=ccount%>" value="${name}"/>
									      		<input type="hidden" name="code_<%=ccount%>" id="code_<%=ccount%>" value="${code}"/>
									      	</td>
									      	<td style="border-bottom:0px;"><input style="width:50%;" name="value_<%=ccount%>" id="value_<%=ccount%>" value="${value}"/></td>
									      	<td style="border-bottom:0px;border-right:0px;"><input style="width:50%;" onclick="selectObj('选择责任部门', this, 'DEPARTMENT_TREE')" name="departmentName_<%=ccount%>" id="departmentName_<%=ccount %>" value="${departmentName}"/></td>
	                                	</tr>
	                                	<%ccount++; %>
							        </s:iterator>
							        </tbody>
						        </table>
						        <input type="hidden" name="composingItemTotal" id="composingItemTotal" value="<%=ccount%>"/>
						        <input type="hidden" name="composingItems" id="composingItems"></input>
						    </td>
						</tr>
						<tr>
							<td colspan="6" style="height: 24px;"><span style="padding:0 0 0 4px;">是否需要8D单:</span>
								<s:checkboxlist name="need8d" value="need8d" theme="simple" list="need8dOptions" listKey="value" listValue="name"/>
								<span id="show8D" style="display:none;">8D单号<input type="text" name="need8dNo" value="${need8dNo}"/></span>
							</td>
						</tr>
						<%-- <tr>
							<td colspan="6">
								<span>抄送：</span>
								<input name="carbonCopy" id="carbonCopy" value="${carbonCopy}" style="width:70%;max-width:480px;"/>
							</td>
						</tr> --%>
					</table>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>