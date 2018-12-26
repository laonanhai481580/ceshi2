<%@page import="com.ambition.carmfg.entity.OrtTestItem"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@ include file="/common/meta.jsp"%>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"
	type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${ctx}/js/workflowTag.js"></script>
<!-- 表单和流程常用的方法封装 -->
<script type="text/javascript" src="${ctx}/js/workflow-form-0.9.js"></script>
<c:set var="actionBaseCtx" value="${mfgctx}/ort/ort-entrust" />
<script type="text/javascript">

	$(document).ready(function(){
		//初始化表单元素
		$initForm({
			webBaseUrl:'${ctx}',//后台执行的Action的前缀名,如:http://localhost:8080/amb/qrqc
			actionBaseUrl : '${actionBaseCtx}',//项目的前缀地址,如:http://localhost:8080/amb
			formId:'inputForm',//表单ID
			objId:'${id}',//数据库对象的ID
			taskId:'${taskId}',//任务ID
			children:{
				ortTestItems:{
					entityClass:"<%=OrtTestItem.class.getName()%>"//子表1实体全路径
				}
			},
			inputformortaskform:'inputform',//表单类型,taskform:流程办理界面,inputform:普通表单页面
			fieldPermissionStr:'${fieldPermission}'//字符串格式的字段权限
		});
		//方法多选
		var testHsfs = '${testHsf}'.split(",");
		$(":input[name=testHsf]").each(function(index,obj){
			var checked = false;
			for(var i=0;i<testHsfs.length;i++){
				if($.trim(obj.value)==$.trim(testHsfs[i])){
					checked = true;
					break;
				}
			}
			if(checked){
				$(obj).attr("checked","checked");
			}else{
				$(obj).removeAttr("checked");
			}
		});
		$.clearMessage(3000);
/* 		$("#scroll").bind("scroll",function(){
			$("#testItemsParent").find("div").scrollLeft($("#scroll").scrollLeft());
		});
		$("#testItemsParent").find("div").bind("scroll",function(){
			$("#scroll").scrollLeft($("#testItemsParent").find("div").scrollLeft());
		});
		$("#opt-content").bind("scroll",function(){
			checkScrollDiv();
		});
		contentResize();
		onLoadding();
		judgeValue(); */
		radioChange();
		$("input[name='testHsf']").attr("disabled","disabled");
	 });
	function getScrollTop(){
		 return $("#opt-content").height()+23;
	}
	function checkScrollDiv(){
		var scrollTop = getScrollTop();
		var tableTop = $("#testItemsParent").position().top + $("#testItemsParent").height()-18;
		if(tableTop<scrollTop){
			$("#scroll").hide();
		}else{
			$("#scroll").show();
		}
	}
	function initScrollDiv(){
		var width = $(".ui-layout-center").width()-30;
		var offset = $("#testItemsParent").find("div").width(width).offset();
		var contentWidth =  $("#testItemsParent").find("table").width();
		$("#scroll").width(width).css("top",getScrollTop() + "px").find("div").width(contentWidth);
	}
	function contentResize(){
		initScrollDiv();
		checkScrollDiv();
	}
	function  onLoadding(enterpriseGroup,customerNo,id){
		var params = {
				customerNo : customerNo,
				enterpriseGroup:enterpriseGroup,
				id:id
			};
			$("#scroll").hide();
			$("#testItemsParent").html("<div style='padding:4px;'><b>ORT测试项目加载中,请稍候... ...</b></div>");
			var url = "${actionBaseCtx}/test-items.htm";
			isLoadItems = true;
			$("#testItemsParent").load(url,params,function(){
				isLoadItems = false;
				var contentWidth =  $("#testItemsParent").find("table").width();
				$("#scroll").find("div").width(contentWidth);
				checkScrollDiv();  
			});
	}
	function judgeValue(){
		var  enterpriseGroup=$("#enterpriseGroup").val();
		var  customerNo=$("#customerNo").val();
		var  id=$("#id").val();
		if(enterpriseGroup!=""&&customerNo!=""){
			onLoadding(enterpriseGroup,customerNo,id);
		}
	}
	//模糊查询设置
	function searchSet(myId){
		//料号模糊查询
		$('#'+myId)
		.bind("blur",function(){
			if(!$(this).data("autocomplete").menu.element.is(":visible")){
				var hisSearch = $(this).attr("hisSearch");
				if(this.value !== hisSearch){
					$(this).attr("hisSearch",this.value); 
				}
			}
		})
		.autocomplete({
			minLength: 1,
			delay : 200,
			autoFocus: true,  
			matchSubset: true,
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
				if(ui.item.label){
					$('#'+myId).attr("hisValue",ui.item.label);
					$('#productName').val(ui.item.value);
					this.value = ui.item.label; 
				}else{
					$('#'+myId).attr("hisValue","");
					$('#productName').val("");
					this.value = ""; 
				}
				return true;
			},
			close : function(event,ui){
				var cust = $('#'+myId);
				var val=cust.val();
				if(val){//如果当前料号值非空
					var hisValue = cust.attr("hisValue");
					if(!hisValue||hisValue==null){//如果料号没有查询出值，且val不为空，则val为原始输入值，保留原始输入数据
						cust.val(val);
					}else{//如果料号查询出有值，且val非空，则val为查询出的值，保留查询出的数据
						cust.val(hisValue);
					}
				}else{ //如果当前料号值为空
					var hisValue = cust.attr("hisValue");
					if(!hisValue||hisValue==null){//如果料号没有查询出值，且val为空，则val为原始输入值就是空
						cust.val('');
					}else{//如果料号查询出有值，且val空，则val为查询出的值带出的空，保留查询出的数据
						cust.val(hisValue);
					}
				}
				cust.attr("hisValue",'').attr("hisSearch",'');
			}
		});
	}
	//弹出bom选择料号
	var materialNameId = null;
	function SelectMaterialName(obj){
			$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择品名"
			});
		}
	//选择之后的方法 data格式{key:'a',value:'a'}
	function setFullBomValue(data){
		var d = data[0];
		//产品名称
		$('#productNumber').val(d.code);
		$('#productName').val(d.name);
	}

	function deleteForm(objId){
		if(!confirm("确定要删除吗?")){
			return;
		}
	 	$.showMessage("正在删除数据,请稍候... ...",'custom');
		$.post("${actionBaseCtx}/delete-form.htm",{id:objId},function(str){
			$.showMessage(str);
		});
		window.location="${actionBaseCtx}/input.htm"
	}
	//选择人员
	function checkManClick(title,inputId,hiddenInputId,multiple,defaultTreeValue,callback){
		callback = callback||function(){};
		var acsSystemUrl = "${ctx}";
		popTree({ title :title,
			innerWidth:'400',
			treeType:"MAN_DEPARTMENT_TREE",
			defaultTreeValue:defaultTreeValue?defaultTreeValue:"id",
			leafPage:'false',
			multiple:multiple,
			hiddenInputId:hiddenInputId,
			showInputId:inputId,
			acsSystemUrl:acsSystemUrl,
			callBack:callback
		});
}
	function saveFormTemp(){
		var item=getScrapItems();
		$("#zibiao").val(item);
			saveForm();
	}
	function submitFormTemp(){
		var item=getScrapItems();
		$("#zibiao").val(item);
		submitForm();
	}
	function getScrapItems(){
		var infovalue="";
		$("tr[zbtr=true]").each(function(index,obj){
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
	
	function radioChange(){
		var val=$("input[name='testProject']:checked").val();
		if(!val){
			$("#testOrt").val("");
			$("#testOrt").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOther").attr("disabled","disabled");
			$("#testHsf").val("");
			$("input[name='testHsf']").attr("disabled","disabled");		
		}
		if(val=="HSF"){
			$("#testOrt").val("");
			$("#testOrt").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOther").attr("disabled","disabled");
			$("input[name='testHsf']").removeAttr("disabled");
		}
		if(val=="ORT"){
			$("input[name='testHsf']").each(function(index,obj){
				iobj = $(obj);
				iobj.attr("checked",false);
			});
			$("input[name='testHsf']").attr("disabled","disabled");
			$("#testOther").attr("disabled","disabled");
			$("#testOther").val("");
			$("#testOrt").removeAttr("disabled");
		}
		if(val=="其他"){
			$("input[name='testHsf']").each(function(index,obj){
				iobj = $(obj);
				iobj.attr("checked",false);
			});
			$("input[name='testHsf']").attr("disabled","disabled");
			$("#testOrt").attr("disabled","disabled");
			$("#testOrt").val("");
			$("#testOther").removeAttr("disabled");
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
	//选择不良类别
	var idIndex=null;
	function inspectionItemClick(obj){
		idIndex = obj.id;
		var customerNo=$("#customerNo").val();
		var url = '${mfgctx}/ort/ort-base/inspection-item-select.htm?customerNo='+customerNo;
			$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择测试项目"
			});
	}
	function setItemValue(datas){
		var idFirst = idIndex.split("_")[0];
		$("#"+idFirst+"_testItem").val(datas[0].testItem);
		$("#"+idFirst+"_testCondition").val(datas[0].testCondition);
		$("#"+idFirst+"_judgeStandard").val(datas[0].judgeStandard);
		$("#"+idFirst+"_value").val(datas[0].value);
 	}
	//选择不良类别
	function customerClick(obj){
		var url = '${mfgctx}/ort/ort-base/customer-select.htm';
			$.colorbox({href:url,iframe:true, 
			innerWidth:$(window).width()<900?$(window).width()-50:900, 
			innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
				overlayClose:false,
				title:"选择客户"
			});
	}
	function setCustomerValue(datas){
		$("#customerNo").val(datas[0].key);
		$("#customer").val(datas[0].value);
 	}
	//物料清单弹出框选择
	function checkBomClick(){
		$.colorbox({href:"${mfgctx}/common/product-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:900,
			height:$(window).height()<400?$(window).height()-100:600,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	function setProductValue(datas){
		$("#productNo").val(datas[0].materielCode);
 	}
	</script>
</head>

<body onload="getContentHeight();">
	<script type="text/javascript">
		var secMenu="ort_list";
		var thirdMenu="_ORT_TEST_INPUT";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ort-menu.jsp" %>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<s:if test="taskId>0">
						<wf:workflowButtonGroup taskId="${taskId}"></wf:workflowButtonGroup>
					</s:if>
					<s:else>
						<security:authorize ifAnyGranted="MFG_ORT_TEST_SAVE">
						  <button class='btn' type="button" onclick="saveForm();"><span><span><b class="btn-icons btn-icons-save"></b><s:text name='保存'/></span></span></button> 
							 <%-- <button class='btn' type="button" onclick="saveFormTemp();">
								<span><span><b class="btn-icons btn-icons-save"></b>
									<s:text name='暂存' /></span></span>
							</button>  --%>
							<button class='btn' type="button" onclick="submitFormTemp();">
								<span><span><b class="btn-icons btn-icons-ok"></b>
									<s:text name='提交' /></span></span>
							</button>
						</security:authorize>
					</s:else>
					<s:if test="taskId>0">
						<button class="btn" onclick="viewFormInfo()">
							<span><span><b class="btn-icons btn-icons-info"></b>
								<s:text name='详情' /></span></span>
						</button>
					</s:if>
					<s:if test="task.active==0&&returnableTaskMaps.size>0">
						<button class='btn' type="button" id="_task_button"
							onclick="showIdentifiersDiv();">
							<span><span><b class="btn-icons btn-icons-unbo"></b>驳回到</span></span>
						</button>
					</s:if>
					<c:if test="${id>0}">
						<security:authorize ifAnyGranted="MFG_ORT_TEST_EXPORT_FORM">
							<button class='btn' id="print" type="button"
								onclick="exportForm();">
								<span><span><b class="btn-icons btn-icons-print"></b>导出</span></span>
							</button>
						</security:authorize>
					</c:if>
				</div>
				<div>
					<iframe id="iframe" style="display:none;"></iframe>
				</div>
				<div id="opt-content" class="form-bg">
					<div style="color: red;" id="message">
						<s:actionmessage theme="mytheme" />
					</div>
					<div>
						<form id="defaultForm1" name="defaultForm1" action="">
							<input type="hidden" name="id" id="id" value="${id}" />
							<input name="taskId" id="taskId" value="${taskId}" type="hidden" />
							<input id="selecttacheFlag" type="hidden" value="true" />
						</form>
					</div>
					<div>
						<s:form id="inputForm" name="inputForm" method="post" action="">
							<jsp:include page="input-form.jsp" />
							<c:if test="taskId>0">
								<%@ include file="process-form.jsp"%>
							</c:if>
						</s:form>
					</div>
				</div>
			</aa:zone>
			<div id="flag" style="display: none;"
				onmouseover='show_moveiIdentifiersDiv();'
				onmouseout='hideIdentifiersDiv();'>
				<ul style="width: 240px;">
					<s:iterator var="returnableTaskMap" value="returnableTaskMaps">
						<li
							onclick="backToTask('${taskId}','${returnableTaskMap.taskName}','${returnableTaskMap.loginName}');"
							style="cursor: pointer; width: 232px; overflow: hidden;"
							title="驳回到 ${returnableTaskMap.taskName}(${returnableTaskMap.userName})">
							${returnableTaskMap.taskName}(${returnableTaskMap.userName})</li>
					</s:iterator>
				</ul>
			</div>
		</div>
	</div>
</body>
</html>