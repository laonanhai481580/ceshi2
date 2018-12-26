<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<style type="text/css">
		.ui-jqgrid .ui-jqgrid-htable th div {
		    height:auto;
			overflow:hidden;
			padding-right:2px;
			padding-top:2px;
			position:relative;
			vertical-align:text-top;
			white-space:normal !important;
		}
		.ui-autocomplete {
			max-width: 180px;
			max-height: 200px;
		    overflow-y: auto;
		    overflow-x: hidden;
		}
	</style>
	<script type="text/javascript">
		//重写(给单元格绑定事件)
		var myId = null;
		function $oneditfunc(rowid){
			myId = rowid;
			if(rowid == 0){
				var rowData = null;
				var prevId = $("#" + rowid).prev().attr("id");
				if(prevId){
					rowData = $("#dynamicInspection").jqGrid("getRowData",prevId);
				}
				if(!rowData){
					rowData = {};
				}
				var date = new Date();
				var year = date.getFullYear();
				var month = date.getMonth()+1;
				var day = date.getDate();
				var dateStr = year + "-" + (month>9?month:"0"+month) + "-" + (day>9?day:"0"+day);
				rowData['maintenanceDate'] = dateStr;
				var fields = ['lossType','level','maintenancePerson','maintenanceDate'];
				for(var i=0;i<fields.length;i++){
					var val = rowData[fields[i]];
					if(val){
						$("#" + rowid + "_" + fields[i]).val(val);
					}
				}
			}
			//更改回车事件为下一单元格
			enterKeyToNext("dynamicInspection",myId,function(){
// 				caclute(rowid);
			});			
			//模糊查询设置
 			searchSet(myId);
			
			var isChange = false;
			$("#" + rowid + " :input[name]").each(function(index,obj){
				if(obj.name.indexOf("_")>0){
					if(obj.name.indexOf("_")>0){
						if($.browser.msie){
							$(obj)[0].onpropertychange=function(event){
								if(!isChange){
									isChange = true;
// 									caclute(rowid);	
									setTimeout(function(){isChange=false;},500);							
								}
							};
						}else{
							$(obj).bind("change",function(){
// 								caclute(rowid);
							});
						}
					}
				}
			});
		}
		
		var isEnter = false;
		//模糊查询设置
		function searchSet(myId){
			//料号模糊查询
			$('#'+myId+'_code' ,'#dynamicInspection')
			.bind("blur",function(){
				if(!$(this).data("autocomplete").menu.element.is(":visible")){
					var hisSearch = $(this).attr("hisSearch");
					if(this.value !== hisSearch){
						$(this).attr("hisSearch",this.value); 
					}
				}
			})
			.autocomplete({
				minLength: 4,
				delay: 100,
				autoFocus: true, 
				source: function( request, response ) {
					var value = request.term;
					if(value.indexOf("\.")==1){
						if(value.length>=11){
							response([{label:'数据加载中...',value:''}]);
							$.post("${mfgctx}/common/search-product-boms.htm",{searchParams:'{"code":"'+request.term+'"}',label:'code'},function(result){
								response(result);
							},'json');
						}
					}else{
						if(value.length>=4){
							response([{label:'数据加载中...',value:''}]);
							$.post("${mfgctx}/common/search-product-boms.htm",{searchParams:'{"code":"'+request.term+'"}',label:'code'},function(result){
								response(result);
							},'json');
						}
					}
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					if(ui.item.label){
						$('#'+myId+'_code','#dynamicInspection').attr("hisValue",ui.item.label);
						$('#'+myId+'_name','#dynamicInspection').val(ui.item.value);
						this.value = ui.item.label; 
					}else{
						$('#'+myId+'_code','#dynamicInspection').attr("hisValue","");
						$('#'+myId+'_name','#dynamicInspection').val("");
						this.value = ""; 
					}
					return true;
				},
				close : function(event,ui){
					var cust = $('#'+myId+'_code','#dynamicInspection');
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
					$('#'+myId+'_code','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
					$('#'+myId+'_name','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
				}
			});
			
			$('#'+myId+'_code' ,'#dynamicInspection').bind("blur",function(){
				var bomCodeVal=$(this).val();
				//当当前值不符合模糊查询所要求的条件时
				if((bomCodeVal.indexOf("\.")==1&&bomCodeVal.length<12)||(bomCodeVal.indexOf("\.")!=1&&bomCodeVal.length<4)){
					isEnter = false;
				}
			}).bind("keydown",function(e){
				var bomCodeVal=$(this).val();
				if((bomCodeVal.indexOf("\.")==1&&bomCodeVal.length<12)||(bomCodeVal.indexOf("\.")!=1&&bomCodeVal.length<4)){
					if(e.keyCode == 13){
						isEnter = true;
					}else{
						isEnter = false;
					}
				}
			});
			
			//客户模糊查询
			$('#'+myId+'_customerCode','#dynamicInspection').bind("blur",function(){
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
				source: function( request, response ) {
					response([{label:'数据加载中...',value:''}]);
					$.post("${marketctx}/customer/select-customer-ByKey.htm",{searchParams:'{"name":"'+request.term+'"}'},function(result){
						response(result);
					},'json');
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					if(ui.item.value){
						$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue",ui.item.value);
						$('#'+myId+'_customerCode','#dynamicInspection').val(ui.item.label);
						this.value = ui.item.value;
					}else{
						$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue","");
						$('#'+myId+'_customerCode','#dynamicInspection').val("");
						this.value = ""; 
					}
					return true;
				},
				close : function(event,ui){
					var cust = $('#'+myId+'_customerName','#dynamicInspection');
					var val = cust.val();
					if(val){//如果当前值非空
						var hisValue = cust.attr("hisValue");
						if(!hisValue||hisValue==null){//如果没有查询出值，且val不为空，则val为原始输入值，保留原始输入数据
							cust.val(val);
						}else{//如果查询出有值，且val非空，则val为查询出的值，保留查询出的数据
							cust.val(hisValue);
						}
					}else{//如果当前值为空
						var hisValue = cust.attr("hisValue");
						if(!hisValue||hisValue==null){//如果没有查询出值，且val为空，则val为原始输入值就是空
							cust.val('');
						}else{//如果查询出有值，且val空，则val为查询出的值带出的空，保留查询出的数据
							cust.val(hisValue);
						}
					}
					cust.attr("hisValue",'').attr("hisSearch",'');
					$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
					$('#'+myId+'_customerCode','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
				}
			});
			//客户模糊查询
			$('#'+myId+'_customerName','#dynamicInspection').bind("blur",function(){
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
				source: function( request, response ) {
					response([{label:'数据加载中...',value:''}]);
					$.post("${marketctx}/customer/select-customer-ByKey.htm",{searchParams:'{"name":"'+request.term+'"}'},function(result){
						response(result);
					},'json');
				},
				focus: function() {
					return false;
				},
				select: function( event, ui ) {
					if(ui.item.value){
// 						alert(ui.item.value);
						$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue",ui.item.label);
						$('#'+myId+'_customerCode','#dynamicInspection').val(ui.item.value);
						this.value = ui.item.value;
					}else{
						$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue","");
						$('#'+myId+'_customerCode','#dynamicInspection').val("");
						this.value = ""; 
					}
					return true;
				},
				close : function(event,ui){
					var cust = $('#'+myId+'_customerName','#dynamicInspection');
					var val = cust.val();
					if(val){//如果当前值非空
						var hisValue = cust.attr("hisValue");
						if(!hisValue||hisValue==null){//如果没有查询出值，且val不为空，则val为原始输入值，保留原始输入数据
							cust.val(val);
						}else{//如果查询出有值，且val非空，则val为查询出的值，保留查询出的数据
							cust.val(hisValue);
						}
					}else{//如果当前值为空
						var hisValue = cust.attr("hisValue");
						if(!hisValue||hisValue==null){//如果没有查询出值，且val为空，则val为原始输入值就是空
							cust.val('');
						}else{//如果查询出有值，且val空，则val为查询出的值带出的空，保留查询出的数据
							cust.val(hisValue);
						}
					}
					cust.attr("hisValue",'').attr("hisSearch",'');
					$('#'+myId+'_customerName','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
					$('#'+myId+'_customerCode','#dynamicInspection').attr("hisValue",'').attr("hisSearch",'');
				}
			});
			
		}
		
		
		
	
		var timeout = null;
		function showMessage(message,showTimes){
			clearTimeout(timeout);
			$("#message").html(message);
			timeout = setTimeout(function(){
				$("#message").html("");
			},showTimes?showTimes:5000);
		}
		
		//重写(单行保存前处理行数据)
		function $processRowData(data){
// 			data.inspectionPoint = $("#inspectionPoint").val();
			return data;
		}
		
		function $addGridOption(jqGridOption){
// 			jqGridOption.postData.inspectionPoint=$("#inspectionPoint").val();
		}
		
		function $successfunc(response){
			var jsonData = eval("(" + response.responseText+ ")");
			if(jsonData.error){
				alert(jsonData.message);
			}else{
				return true;
			}
		}
		
		//==================行保存完毕后的动作 ：复制到下一行start=================
		function afterSaveRow(rowId, data) {
			//必须加括号才能转换为对象
			var jsonData = eval("(" + data.responseText + ")");
			if (rowId == 0) {//新纪录删除了再增加
				editableGrid.jqGrid('delRowData', rowId);
				editableGrid.jqGrid('addRowData', jsonData.id, jsonData, "last");
			} else {//更新已有记录
				editableGrid.jqGrid('setRowData', jsonData.id, jsonData);
			}
			editNextRow(jsonData.id,jsonData);
		}
		
		/**
		 * 编辑下一行
		 * @param rowId
		 * @return
		 */
		 function editNextRow(rowId,jsonData) {
			var ids = editableGrid.jqGrid("getDataIDs");
			var index = editableGrid.jqGrid("getInd", rowId);
			index++;
			if (index > ids.length) {//当前编辑行是最后一行
				editableGrid.resetSelection();
				iMatrix.addRow(true);
				$('#'+0+'_lossType' ,'#dynamicInspection').val(jsonData.lossType);//类别
				$('#'+0+'_level' ,'#dynamicInspection').val(jsonData.level);//档次
				$('#'+0+'_createdTime' ,'#dynamicInspection').val(jsonData.createdTime);//维护日期
				$('#'+0+'_creator' ,'#dynamicInspection').val(jsonData.creator);//维护人
			} else {
				editableGrid.resetSelection();
				editRow(ids[index - 1]);
				editableGrid.setSelection(ids[index - 1],true);
			}
		}
		
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${costctx}/innerproductloss/import-form.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#dynamicInspection").trigger("reloadGrid");
				}
			});
		}
		//导出
	 	function createReport(){
			$("#iframe").attr("src","${costctx}/innerproductloss/template-export.htm");
			}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="composing_detail";
		var thirdMenu="partsloss";
		var treeMenu = '_inner-productLoss-list';
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-composing-detail-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="cost-innerproductloss-save">
					<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					<button class='btn' onclick="customSave('dynamicInspection');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
					</security:authorize>
<!-- 					<button class='btn' onclick="customSave('dynamicInspection');" type="button"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button> -->
					<security:authorize ifAnyGranted="cost-innerproductloss-delete">
					<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="cost-innerproductloss-import">
					<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
					</security:authorize>
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="cost-innerproductloss-export">
					<button class='btn' onclick="iMatrix.export_Data('${costctx}/innerproductloss/export.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="cost-innerproductloss-export-template">
					<button class='btn' onclick="createReport();" type="button"><span><span><b class="btn-icons btn-icons-export"></b>模板导出</span></span></button>
					</security:authorize>
					<span style="color:red;margin-left:4px;" id="message"></span>
				</div>
				<div><iframe id="iframe" style="display:none;"></iframe></div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicInspection" url="${costctx}/innerproductloss/list-datas.htm" code="COST_INNER_PRODUCT_LOSS" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>