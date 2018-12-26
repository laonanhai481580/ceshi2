<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<style>
<!--
	#searchUl{
		margin:0px;
		padding:0px;
	}
	#searchUl li{
		float:left;
		width:260px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#searchUl li select{
		width:178px;
	}
	.input{
		width:170px;
	}
	.label{
		float:left;
		width:80px;
		text-align:right;
		padding-right:2px;
	}
	#groupUl{
		margin:0px;
		padding:0px;
	}
	#groupUl li{
		float:left;
		width:95px;
		height:24px;
		line-height:24px;
		list-style:none;
	}
	#groupUl li.last{
		padding:0px;
		width:280px;
		margin-bottom:2px;
		text-align:right;
	}
-->
</style>
<script type="text/javascript">
///////////////页面使用方法////////////////////////////////////
//	//选择供应商
// 	function selectSupplier(){
// 		var url='${supplierctx}/archives/select-supplier.htm';
// 		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
// 			overlayClose:false,
// 			title:"选择供应商"
// 		});
// 	}
	
//  //选择之后的方法 data格式{key:'a',value:'a'}
// 	function setSupplierValue(data){
// 		alert("select is key:" + data.key + ",value:" + data.value);
// 	}
///////////////页面使用方法结束////////////////////////////////////
		isUsingComonLayout=false;
		var multiselect = ${multiselect};
		var params = '';
		$(document).ready(function(){
			params = getParams();
			createGrid();
			contentResize();
		});
		function contentResize(){
			var height = $(window).height() - 115;
			var display = $("#searchDiv").css("display");
			if(display=='block'){
				height -= $("#searchDiv").height();
				$(".search").html("隐藏查询");
			}else{
				height += 10;
				$(".search").html("显示查询");
			}
			$("#gridList").jqGrid("setGridHeight",height);
		}
		
		function toggleSearchBtn(){
			var display = $("#searchDiv").css("display");
			if(display=='block'){
				$("#searchDiv").css("display","none");
			}else{
				$("#searchDiv").css("display","block");
			}
			contentResize();
		}
		//创建表格
		function createGrid(){
			var colModel = [
				{name:'id',index:'id',hidden:true},
				{label:'供应商编号 ',name:'code',index:'code',width:100,hidden:false},
				{label:'供应商名称',name:'name',index:'name',width:200,hidden:false},
				{label:'供应的产品',name:'supplyProductStr',index:'supplyProductStr', width:180,hidden:false},
				{label:'地区',name:'region',index:'region',width:120,hidden:false},
				{label:'企业性质',name:'enterpriseProperty',index:'enterpriseProperty', width:90,hidden:false},
				{label:'联系人',name:'linkMan',index:'linkMan', width:80,hidden:false},
				{label:'联系电话',name:'linkPhone',index:'linkPhone', width:110,hidden:false},
				{label:'重要度',name:'importance',index:'importance', width:120,hidden:false},
				{label:'开发状态',name:'state',index:'state'},
				{label:'使用状态',name:'useState',index:'useState', width:80,hidden:false},
				{name:'address',index:'address',hidden:true},
				{name:'enterpriseProperty',index:'enterpriseProperty',hidden:true},
				{name:'linkPhone',index:'linkPhone',hidden:true},
				{name:'headcount',index:'headcount',hidden:true},
				{name:'fax',index:'fax',hidden:true},
				{name:'technologyCount',index:'technologyCount',hidden:true},
				{name:'homepage',index:'homepage',hidden:true},
				{name:'managerCount',index:'managerCount',hidden:true},
				{name:'email',index:'email',hidden:true},
				{name:'builtUpArea',index:'builtUpArea',hidden:true},
				{name:'createDate',index:'createDate',hidden:true},
				{name:'floorArea',index:'floorArea',hidden:true},
				{name:'productArea',index:'productArea',hidden:true},
				{name:'equityStake',index:'equityStake',hidden:true},
				{name:'enterpriseDescription',index:'enterpriseDescription',hidden:true},
				{name:'fixedAssets',index:'fixedAssets',hidden:true},
				{name:'otherCertification',index:'otherCertification',hidden:true},
				{name:'certificationOf3c',index:'certificationOf3c',hidden:true},
				{name:'certificationOfTS16949',index:'certificationOfTS16949',hidden:true},
				{name:'equipmentDescription',index:'equipmentDescription',hidden:true},
				{name:'buildAbility',index:'buildAbility',hidden:true},
				{name:'gaizhiDate',index:'gaizhiDate',hidden:true},
				{name:'registrationAsset',index:'registrationAsset',hidden:true},
				{name:'tradeLocation',index:'tradeLocation',hidden:true},
				{name:'researchAbility',index:'researchAbility',hidden:true},
				{name:'qualityAssurance',index:'qualityAssurance',hidden:true}
				];
			$("#gridList").jqGrid({
				rownumbers:true,
				gridComplete:function(){},
				loadComplete:function(){},
				ondblClickRow:function(rowId,iRow,iCol,e){
					var objs = [];
					var data = $("#gridList").jqGrid('getRowData',rowId);
					if(data){
						objs.push(data);
					}
					if(objs.length>0){
						window.parent.setSupplierValue(objs);
						window.parent.$.colorbox.close();
					}else{
						alert("选择的值不存在!");
					};
				},
				postData : {state:$("#state").val(),},
				rowNum:15,
				datatype: "json",
				height : 120,
				url:'${supplierctx}/archives/select-supplier-datas.htm',
				prmNames:{
					rows:'page.pageSize',
					page:'page.pageNo',
					sort:'page.orderBy',
					order:'page.order',
				},
			   	colModel : colModel,
			   	pager:'#pager',
			});
		}
		//获取表单的值
		function getParams(){			
			var params = {};
			$("#searchDiv input[type=text]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name && jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#searchDiv input[type=hidden]").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name && jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			$("#searchDiv select").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name && jObj.val()){
					params[obj.name] = jObj.val();
				}
			});
			return params;
		}
		//查询
		function search(){
			var params = getParams();
			
			$('table.ui-jqgrid-btable').each(function(index,obj){
				obj.p.postData = params;
				$(obj).trigger("reloadGrid");
			});
		}
		//确定
		function realSelect(){
			var ids = $("#gridList").jqGrid("getGridParam","selarrrow");
			if(ids.length == 0){
				alert("请选择供应商!");
				return;
			}
			if($.isFunction(window.parent.setSupplierValue)){
				var objs = [];
				for(var i=0;i<ids.length;i++){
					var data = $("#gridList").jqGrid('getRowData',ids[i]);
					if(data){
						objs.push(data);
					}
				}
				if(objs.length>0){
					window.parent.setSupplierValue(objs);
					window.parent.$.colorbox.close();
				}else{
					alert("选择的值不存在!");
				}
			}else{
				alert("页面还没有 setSupplierValue 方法!");
			}
		}
		//取消
		function cancel(){
			window.parent.$.colorbox.close();
		}
		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
<input type="hidden" id="state" name="state" value="${state }"/>
	<div class="opt-body">
		<div class="opt-btn" id="btnDiv">
			<button class='btn' onclick="realSelect();">
				<span><span><b class="btn-icons btn-icons-ok"></b>确定</span></span>
			</button>
			<button class='btn' onclick="cancel();">
				<span><span><b class="btn-icons btn-icons-cancel"></b>取消</span></span>
			</button>
			<button class='btn' onclick="toggleSearchBtn();">
				<span><span><b class="btn-icons btn-icons-search"></b>显示查询</span></span>
			</button>
		</div>
		<div style="padding-left:10px;padding-right:10px;padding-top:2px;display:none;margin:0px;" id="searchDiv">
			<form onsubmit="return false;">
			
			<table class="form-table-outside-border" style="width:100%;display:block;margin:0px auto;" id="searchTable">
				<tr>
					<td style="padding-left:6px;padding-bottom:4px;">
					<input type="hidden" id="params.state" name="params.state" value="${state }"/>
						<ul id="searchUl">
					 		<li>
					 			<span class="label">供应商编号</span>
					 			<input class="input" type="text"  name="params.code"/>
					 		</li>
					 		<li>
					 			<span class="label">供应商名称</span>
					 			<input class="input" type="text"  name="params.name"/>
					 		</li>
					 		<li>
					 			<span class="label">地区</span>
				 				<s:select list="regions" class="input"
									  listKey="value" 
									  listValue="value"
						              labelSeparator=""
						  			  emptyOption="true" 
									  theme="simple"
									  name="params.region">
								</s:select>
					 		</li>
					 		<li>
					 			<span class="label">企业性质</span>
					 			<s:select list="properties" class="input"
									  listKey="value" 
									  listValue="value"
						              labelSeparator=""
						  			  emptyOption="true" 
									  theme="simple"
									  name="params.enterpriseProperty">
								</s:select>
					 		</li>
					 		<li>
					 			<span class="label">重要度</span>
					 			<s:select list="importances" class="input"
									  listKey="value" 
									  listValue="value"
						              labelSeparator=""
						  			  emptyOption="true" 
									  theme="simple"
									  name="params.importance">
								</s:select>
					 		</li>
					 		<!-- 
					 		<li>
					 			<span class="label">开发状态</span>
					 			<select name="params.state" class="input">
					 				<option value=""></option>
					 				<option value="合格">合格</option>
					 				<option value="潜在">潜在</option>
					 				<option value="准供应商">准供应商</option>
					 				<option value="已淘汰">已淘汰</option>
					 			</select>
					 		</li>
					 		 -->
					 	</ul>
					 </td>
				</tr>
				<tr>
					<td style="text-align:right;">
						<button class="btn" onclick="toggleSearchBtn();search();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
						<button class="btn" type="reset"><span><span><b class="btn-icons btn-icons-redo"></b>重置</span></span></button>
					</td>
				</tr>
			</table>
			</form>
		</div>
		<div id="opt-content" style="padding-top:6px;margin:0px;">
			<div id="pager"></div>
			<table id="gridList"></table>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>