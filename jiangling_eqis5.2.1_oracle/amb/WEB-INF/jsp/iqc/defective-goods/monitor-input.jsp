<%@ page contentType="text/html;charset=UTF-8" import="java.util.*,java.text.*"%>
<%@ page import="com.ambition.util.common.DateUtil" %>
<%@ include file="/common/taglibs.jsp"%>
<% 
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
	Date date  = new Date();
	String dateStr = sdf.format(date).substring(0,10);
%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${ctx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		$(document).ready(function(){
			addFormValidate('${fieldPermission}', 'DefectiveGoodsForm');
			var status = $("#status").val();
			var fieldPermission = ${fieldPermission};
			for(var i=0;i<fieldPermission.length;i++){
				var obj=fieldPermission[i];
				if(obj.readonly=='true'){
					$("#"+obj.name).attr("disabled","disabled");
					$("#"+obj.name+"a").removeAttr("onclick");
					if(status!=0){
						$(".btn","#DefectiveGoodsForm").each(function(index,obj){
							$(obj).removeAttr("onclick");
						});
					}
					if(obj.name=="qualityState"){
						$(".aa","#DefectiveGoodsForm").parent().attr("style","display:none");
					}
					if(obj.name=="verifyState"){
						$(".aaa","#DefectiveGoodsForm").parent().attr("style","display:none");
					}
				}
				if(obj.controlType=="allReadolny"){
					$("input","#DefectiveGoodsForm").each(function(index,obj){
						$(obj).attr("disabled","disabled");
					});
					$("textarea","#DefectiveGoodsForm").each(function(index,obj){
						$(obj).attr("disabled","disabled");
					});
					$(".small-button-bg","#DefectiveGoodsForm").each(function(index,obj){
						$(obj).removeAttr("onclick");
					});
					$("button","#DefectiveGoodsForm").each(function(index,obj){
						$(obj).removeAttr("onclick");
					});
				}
			}
			setTimeout(function(){
				$("#message").html("");
			},3000);
			
			$("input[name='disposeMethod']").each(function(index,obj){
				if($("#"+obj.id).next().is("label")){
					$("#"+obj.id).next().after("<br>");
				}else{
					$("#"+obj.id).next().next().after("<br>");
				}
			});
		});
		
	    function chooseOne(cb) {   
	        //先取得Div元素   
	        var obj = document.getElementById("cbTd");   
	        ///判斷obj中的子元素i是否為cb，若否則表示未被點選   
	        for (var i=0; i<obj.children.length; i++){   
	            if (obj.children[i]!=cb){
	            	obj.children[i].checked = false;
	            }else{
		            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
		            obj.children[i].checked = cb.checked;  
	            } 
	        }   
	    }
	    
	    function chooseOne1(cb) {   
	        //先取得Div元素   
	        var obj = document.getElementById("cbTd1");   
	        ///判斷obj中的子元素i是否為cb，若否則表示未被點選   
	        for (var i=0; i<obj.children.length; i++){   
	            if (obj.children[i]!=cb){
	            	obj.children[i].checked = false;
	            }else{
		            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
		            obj.children[i].checked = cb.checked;  
	            } 
	        }   
	    }
	    
	    function chooseOne2(cb) {   
	        //先取得Div元素   
	        var obj = document.getElementById("cbTd2");   
	        ///判斷obj中的子元素i是否為cb，若否則表示未被點選   
	        for (var i=0; i<obj.children.length; i++){   
	            if (obj.children[i]!=cb){
	            	obj.children[i].checked = false;
	            }else{
		            //若是 但原先未被勾選 則變成勾選；反之 則變為未勾選   
		            obj.children[i].checked = cb.checked;  
	            } 
	        }   
	    }
	    
		$(function(){
			//添加验证
			$("#inspectionDate").val('<%=dateStr %>').datepicker();
			$("#verifyDate").datepicker({changeMonth:'true',changeYear:'true'});
			$("#directorOpininDate").datepicker({changeMonth:'true',changeYear:'true'});
			$("#discoverDate").datepicker({changeMonth:'true',changeYear:'true'});
			$("#qualitySignDate").datepicker({changeMonth:'true',changeYear:'true'});
			$("input","#DefectiveGoodsForm").each(function(index,obj){
				if(obj.name&&obj.name.indexOf("params.")>-1){
					if(obj.name.indexOf("_") == -1){
						$(obj).attr("readonly","readonly").click(function(){
							selectDefectionCode(this);
						});
					}else{
						$(obj).bind("keyup",function(){
							caculateBadNumbers();
						}).bind("change",function(){
							caculateBadNumbers();
						});
					}
				}
			});
			$("#partCode").click(function(){selectComponent()});
			$("#partName").click(function(){selectComponent()});
			$("#dutySupplier").click(function(){selectDutySupplier()});
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles',
				hiddenInputId : 'attachmentFiles'
			});
			$.parseDownloadPath({
				showInputId : 'showAttachmentFiles1',
				hiddenInputId : 'attachmentFiles1'
			});
		});
		
		//上传附件
		function uploadFiles(showId,hiddenId){
			$.upload({
				appendTo : '#opt-content',
				showInputId : showId,
				hiddenInputId : hiddenId
			});
		}
		
		function caculateBadNumbers(rowId){
			var val = 0;
			$("input","#DefectiveGoodsForm").each(function(index,obj){
				if(obj.name&&obj.name.indexOf("params.")>-1&&obj.name.indexOf("_") > -1){
					if(!isNaN(obj.value)&&!isNaN(parseInt(obj.value))){
						val += parseInt(obj.value);
					}
				}
			});
			$("#" + "unqualifiedAmount").val(val);
		}
		
	 	function submitForm(url,type){
	 		var status = $("#status").val();
			if($('#DefectiveGoodsForm').valid()){
				if(url&&status==1){
					$("#message").html("已经提交过了");
					setTimeout(function() {
						$("#message").html('');
					}, 1000);
					return;
				}
				var params = getParams();
				$("#btnDiv").find("button.btn").attr("disabled",true);
				if(type=='0'){
					$("#message").html("正在保存,请稍候... ...");
				}else{
					$("#message").html("正在提交,请稍候... ...");
				}
				$.post(url,params,function(result){
					$("#btnDiv").find("button.btn").attr("disabled",false);
					if(result.error&&result.error!=""){
						alert(result.message);
					}else{
						$("#message").html(result.message);
						document.getElementById("btnsub").remove();
					}
					setTimeout(function() {
						$("#message").html('');
					}, 1000);
				},"json");
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
			window.location='${iqcctx}/defective-goods/input.htm';
		}
		
		function selectObj(title,id,treeType){
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:treeType,
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:id,
				showInputId:id,
				acsSystemUrl:acsSystemUrl,
				callBack : function() {}
			});
		}
		
		function selectPerson1(obj) {
			var acsSystemUrl = "${ctx}";
			popTree({
				title : '选择人员',
				innerWidth : '400',
				treeType : 'MAN_DEPARTMENT_TREE',
				defaultTreeValue : 'id',
				leafPage : 'false',
				multiple : 'false',
				hiddenInputId : obj.id,
				showInputId : obj.id,
				acsSystemUrl : acsSystemUrl,
				callBack : function() {
					copyPersonLoginName();
				}
			});
		}
		
		function copyPersonLoginName() {
			$('#analyseLoginName').attr("value", jstree.getLoginName());
			$("#technologyDeptPrincipal").attr("value",jstree.getName());
			
		}
		
		function selectPerson2(obj) {
			var acsSystemUrl = "${ctx}";
			popTree({
				title : '选择人员',
				innerWidth : '400',
				treeType : 'MAN_DEPARTMENT_TREE',
				defaultTreeValue : 'id',
				leafPage : 'false',
				multiple : 'true',
				hiddenInputId : obj.id,
				showInputId : obj.id,
				acsSystemUrl : acsSystemUrl,
				callBack : function() {
					copyPersonLoginName2();
				}
			});
		}
		function copyPersonLoginName2() {
			$('#reviewMenLoginNames').attr("value", jstree.getLoginNames());
		}
		
		//选择质量成本
		var selComposingItemId = '';
		function selectComposing(composingItemId){
			selComposingItemId = composingItemId;
			var url = '${costctx}/common/composing-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
		}
		
		//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setFullComposingValue(datas){
	 		var obj = datas[0];
	 		var index = selComposingItemId.split("_")[1];
	 		var val = "<td style=\"text-align:center\">";
	 		val += "<a href=\"#\" onclick=\"javascript:deleteComposingItem('"+selComposingItemId+"')\" style=\"font-weight:bold;\" title='清除质量成本'>清除</a>";
	 		val += "&nbsp;<a href=\"#\" onclick=\"javascript:selectComposing('"+selComposingItemId+"')\" style=\"font-weight:bold;\" title='选择质量成本'>选择</a>"
	 		val += "</td>";
	 		val += "<td style=\"text-align:center\"><input type='hidden' name='composingItems.name"+index+"' value='"+obj.name+"'></input>"+obj.name+"</td>";
	 		val += "<td style=\"text-align:center\"><input type='text' name='composingItems.name"+index+"_departmentName' id='composingItemsname"+index+"_departmentName' style='width:120px;'class='{required:true}' onclick='selectObj(\"选择部门\",\"composingItemsname"+index+"_departmentName\",\"DEPARTMENT_TREE\")'></input></td>";
	 		val += "<td style=\"text-align:center;\"><input type='text' id='composingItemsName"+index+"' name='composingItems.name"+index+"_amount' class='{min:0,number:true,required:true}' style='width:80px;'></input></td>";
	 		val += "<td>"+obj.remark+"<input type='hidden' name='composingItems.name"+index+"_remark' value='"+obj.remark+"'></input></td>";
	 		$("#" + selComposingItemId).html(val);
	 		$("#composingItemsName" + index).bind("keyup",function(){
	 			caculateAmount();
	 		}).bind("change",function(){
	 			caculateAmount();
	 		});
	 		caculateAmount();
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
	 	
	 	//清除费用
	 	function deleteComposingItem(composingItemId){
	 		var val = "<td style=\"text-align:center\">";
	 		val += "<a href=\"#\" onclick=\"javascript:selectComposing('"+composingItemId+"')\" style=\"font-weight:bold;\" title='选择质量成本'>选择</a>"
	 		val += "</td>";
	 		val += "<td></td>";
	 		val += "<td></td>";
	 		val += "<td></td>";
	 		$("#" + composingItemId).html(val);
	 		caculateAmount();
	 	}
	 	
	 	//选择不良代码
	 	var selectInputObj = '';
	 	function selectDefectionCode(obj){
	 		selectInputObj = obj;
	 		var url = '${mfgctx}/common/defection-code-bom.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择不良代码"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setDefectionValue(data){
	 		$(selectInputObj).val(data[0].value).focus();
	 	}
	 	
	 	//选择物料
	 	function selectComponent(){
	 		var supplierCode=$("#dutySupplierCode").val(); 
			if(supplierCode==''){
				alert('请选择供应商');
				
			}
	 		var url = '${carmfgctx}/common/product-bom-select.htm?supplierCode='+supplierCode;
	 		$.colorbox({href:url,iframe:true, innerWidth:700, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择物料"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setFullBomValue(datas){
			setBomValue(datas[0].code, datas[0].name, datas[0].model);
	 	}
	 	
		function setBomValue(code,name,model){
			var materialName=$("#materialName");
			var materialCode=$("#materialCode");
			materialName.val(name);
			materialCode.val(code);
		}
	 	
	 	function supplierClick(){
			$.colorbox({
				href:"${supplierctx}/archives/select-supplier.htm?state=有效",
				iframe:true,
				width:$(window).width()<1000?$(window).width()-100:1000,
				height:$(window).height()<600?$(window).height()-100:600,
				overlayClose:false,
				title:"选择供应商"
			});
		}
		
		function setSupplierValue(objs){
			var obj = objs[0];
			var his = $("#dutySupplierCode").val();
			$("#dutySupplier").val(obj.name);
			$("#dutySupplierCode").val(obj.code);
			if(obj.code != his){
				setBomValue("","","");
				//loadCheckItems();
			}
			//getSupplierProduct();
		} 
	 	
		function setSupplierProduct(result){
			var checkBomCode = $("#checkBomCode").val();
			if(result.length<=0){
				alert("对不起，该供方没有准入物料！");
				return false;
			}else{
				var isSelected = false;
				for(var i=0;i<result.length;i++){
					if(checkBomCode!=""&&checkBomCode!=null&&checkBomCode==result[i].code){
						$("#selectBomCode").append("<option value="+result[i].code+" selected='selected'>"+result[i].code+"</option>");
						isSelected = true;
					}else{
						$("#selectBomCode").append("<option value="+result[i].code+">"+result[i].code+"</option>");
					}
				}
				if(!isSelected){
					$("#checkBomName").val(result[0].name);
					$("#checkBomCode").val(result[0].code);
					$("#checkBomMaterialType").val(result[0].materialType);
				}
			}
		}
		
	 	//发起改进
	 	function importImprove(){
	 		var id = '${id}';
	 		if(confirm("确定要发起改进吗？")){
				$.post("${iqcctx}/defective-goods/import-improve.htm?id="+id, {}, function(result) {});
			}
	 	}
	 	
	 	function endInstance(id){
	 		var id = '${id}';
	 		if(id==""){
	 			alert("流程未提交");
	 			return;
	 		}	
	 		if(confirm("确定要结束流程吗？")){
				$.post("${iqcctx}/defective-goods/end-instance.htm?id="+id, {}, function(result) {
					alert("流程结束");
				});
			}
	 	}
	 	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
<!-- 	<script type="text/javascript"> -->
<!-- 		var secMenu="defectiveReport"; -->
<!-- 		var thirdMenu="myDefectiveInput"; -->
<!-- 	</script> -->
	
<!-- 	<div id="header" class="ui-north"> -->
<%-- 		<%@ include file="/menus/header.jsp" %> --%>
<!-- 	</div> -->
<!-- 	<div id="secNav"> -->
<%-- 		<%@ include file="/menus/iqc-sec-menu.jsp" %> --%>
<!-- 	</div> -->
	
<!-- 	<div class="ui-layout-west"> -->
<%-- 		<%@ include file="/menus/iqc-defective-report-menu.jsp" %> --%>
<!-- 	</div> -->
	
	<div class="ui-layout-center">
		<div class="opt-body" style="overflow-y:auto;">
			<div class="opt-btn">
				<div id="btnDiv">
					<div style="display:inline" id="btn">
				   	<security:authorize ifAnyGranted="iqc_defective_goods_input_save">
						<button class='btn' type="button" onclick="submitForm('${iqcctx}/defective-goods/save.htm','0');"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
				   	</security:authorize> 
				   	<security:authorize ifAnyGranted="iqc_defective_goods_input_submit">	  	
					  	<s:if test="status==0">
					     	<button class='btn' id="btnsub" type="button" onclick="submitForm('${iqcctx}/defective-goods/submit-process.htm','1');"><span><span><b class="btn-icons btn-icons-ok"></b>提交</span></span></button>
					  	</s:if>
				  	</security:authorize>	
						<button class='btn' type="button" onclick="history.back();"><span><span><b class="btn-icons btn-icons-undo"></b>返回</span></span></button>
						<span style="color:red;" id="message"><s:actionmessage theme="mytheme" /></span>
					</div>
				</div>
			</div>
			<div id="opt-content" style="text-align: center;padding-bottom: 4px;">
				<form action="" method="post" id="DefectiveGoodsForm" name="DefectiveGoodsForm">
					<jsp:include page="form.jsp" />
				</form>
			</div>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
</html>