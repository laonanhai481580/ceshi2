<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript">
		var topMenu = '',editing=false,isUsingComonLayout=false;
		function isSetFormat(cellvalue,options,rowObject){
			if(rowObject.isLeaf == true){
				var checked = cellvalue=='yes'?"checked":"";
				var disabled = rowObject.canUse == 'yes'?"":"disabled=true";
				return '<div style="text-align:center;"><input type="checkbox" ' + checked + ' ' + disabled + ' onclick="isEstimateChange(this,'+rowObject.id+')"></input></div>';
			}else{
				return '';
			}
		}
		function isEstimateChange(obj,id){
			var $t = $("#itemIndicatorList")[0];
			if($t.p.savedRow.length>0){
				$($t).jqGrid("restoreCell",$t.p.iRow,$t.p.iCol);
			}
			var rowData = $("#itemIndicatorList").jqGrid("getRowData",id);
			if(rowData){
				var val = obj.checked?"yes":"no";
				var params = {};
				for(var pro in rowData){
					params[pro] = rowData[pro];
				}
				params.indicatorId = '${indicatorId}';
				params.isSet = val;
				$.post("${sictx}/base-info/indicator/save-item.htm",params,function(result){
					if(result.error){
						alert(result.message);
						obj.checked = !obj.checked;
					}else{
						$("#itemIndicatorList").jqGrid('setRowData',id,{isSet:val,isLeaf:true,canUse:'yes',id : id});
					}
				},'json');
			}
		}
		var levelObj = ${levelObject};
		function inspectionLevelFormatter(value){
			if(!value||!levelObj[value]){
				return '';
			}else{
				return levelObj[value];
			}
		}
		var postData = {
				indicatorId:'${indicatorId}',
				materialCode:'<%=request.getParameter("materialCode")%>',
				workingProcedure:'<%=request.getParameter("workingProcedure")%>'
		};
		$(document).ready(function(){
			jQuery("#itemIndicatorList").jqGrid({
				url:'${sictx}/base-info/indicator/edit-indicator-datas.htm',
// 				rownumbers:true,
				postData : postData,
				treedatatype: "json",
				height : $(document).height()-75,
				mtype: "POST",
			   	colModel:[
			   		{name:'id',index:'id', width:1,hidden:true,key:true},
			   		{name:'isLeaf',index:'isLeaf', width:1,hidden:true},
			   		{name:'itemIndicatorId',index:'itemIndicatorId', width:1,hidden:true},
			   		{label:'操作',name:'',index:'',width:80,editable:false,formatter:function(val,o,obj){
			   			if(obj.itemIndicatorId){
			   				return '<button class="btn" type="button" onclick="deleteItemIndicator('+obj.itemIndicatorId+');" style="margin-left:6px;" title="删除【'+obj.name+'】"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button> ';
			   			}else{
			   				return "";
			   			}
			   		}},
			   		{label:'检验项目',name:'name',index:'name', width:160,editable:false},
			   		{label:'首检检验数量',name:'params.inAmountFir',index:'inAmountFir', width:120,editable:true,editrules:{min:0,number:true}},
			   		{label:'IPQC检验数量',name:'params.inAmountPatrol',index:'inAmountPatrol', width:120,editable:true,editrules:{min:0,number:true}},
			   		{label:'末检检验数量',name:'params.inAmountEnd',index:'inAmountEnd', width:120,editable:true,editrules:{min:0,number:true}},
			   		{label:'统计类型',name:'params.countType',index:'countType', width:80,editable:true,edittype:'select',editoptions:{value:{'计数':'计数','计量':'计量'},style:'width:100%;'}},
			   		{label:'检验方法',name:'params.method',index:'method', width:120,editable:true},
			   		{label:'单位',name:'params.unit',index:'unit', width:80,editable:true},
			   		{label:'规格',name:'params.specifications',index:'specifications', width:80,editable:true},
			   		{label:'上限',name:'params.levela',index:'levela', width:80,editable:true,editrules:{min:0,number:true}},
			   		{label:'下限',name:'params.levelb',index:'levelb', width:80,editable:true,editrules:{min:0,number:true}},
			   		{label:'质量参数',name:'params.massParameter',index:'massParameter', width:80,editable:true},
			   		{label:'规格',name:'params.specifications',index:'specifications', width:80,editable:true,edittype:'select',editoptions:{value:{${isJnUnitOjbect}},style:'width:100%;'}},
			   		{label:"质量参数",name:'params.featureId',index:'featureId', width:100,editable:false,hidden:true,},
			   		{label:"质量特性",name:'params.featureId',index:'featureId', width:120,editable:false,edittype:'select',editoptions:{value:{${featureOptions}},style:'width:100%;'},formatter:featureFormatter,},
			   		{label:"同步到SPC",name:'',align:'center',index:'',width:130,editable:false,formatter:function(val,o,obj){
			   			if(obj.itemIndicatorId){
			   				return "<button class=\"btn\" type=\"button\" onclick=\"synchroSpc('"+obj.itemIndicatorId+"','"+obj.id+"');\" style=\"margin-left:6px;\" title=\"同步到spc【'"+obj.name+"'】\"><span><span><b class=\"btn-icons btn-icons-delete\"></b>同步到SPC</span></span></button> ";
			   			}else{
			   				return "";
			   			}   
			   			}
			   		},
			   		{label:'是否IPQC测试项',name:'params.isJnUnit',index:'isJnUnit', width:80,editable:true,edittype:'select',editoptions:{value:{${isJnUnitOjbect}},style:'width:100%;'}},
			   		{label:'是否集成设备',name:'params.isInEquipment',index:'isInEquipment', width:80,editable:true,edittype:'select',editoptions:{value:{${isJnUnitOjbect}},style:'width:100%;'}},
			   		{label:'备注',name:'params.remark',index:'remark', width:180,editable:true,edittype:"textarea", editoptions:{rows:"5",style:"width:100%;"}}
			   		
			   	],
				treeGridModel: 'adjacency',
			    treeGrid: true,
			    viewrecords: true,
				ExpandColumn : 'name',
// 				ExpandColClick : true,
// 				shrinkToFit : true,
				gridComplete : function(){
				},
				loadComplete : function(){
					if(scroolTop != null){
						$($("#itemIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop(scroolTop);
						scroolTop = null;
					}
				},
				cellEdit: true, 
				cellsubmit : 'remote',
				cellurl : "${sictx}/base-info/indicator/save-item.htm?indicatorId=${indicatorId}",
				afterSubmitCell : function(serverresponse, rowid, cellname, value, iRow, iCol){
					var result = eval("(" + serverresponse.responseText
							+ ")");
					if(result.error){
						return [false,result.message];
					}else{
						return [true,""];
					}
				},
				beforeEditCell : function(){
					return true;
				}
			});
		});
		/**
		* 刷新检验项目
		*/
		var scroolTop = null;
		function refreshItemIndicator(){
			var data = $("#itemIndicatorList")[0].p.data;
			var expandIds = [];
			for(var i=0;i<data.length;i++){
				var d = data[i];
				if(d.expanded){
					expandIds.push(d.id);
				}
			}
			var postData = {
				indicatorId:'${indicatorId}',
				materialCode:'<%=request.getParameter("materialCode")%>',
				workingProcedure:'<%=request.getParameter("workingProcedure")%>',
				nodeid:'',
				expandIds : expandIds.join(",")
			};
			scroolTop = $($("#itemIndicatorList").closest(".ui-jqgrid-bdiv")[0]).scrollTop();
			$("#itemIndicatorList").clearGridData();
			$("#itemIndicatorList").jqGrid("setGridParam",{postData:postData}).trigger("reloadGrid");
			//重置展开的节点为空
			$("#itemIndicatorList").jqGrid("setGridParam",{postData:{expandIds:''}});
		}
		//质量参数格式化
		var featureObj = ${featureObject};
		function featureIdFormatter(value){
			if(!value||!featureObj[value]){
				return '';
			}else{
				return featureObj[value];
			}
		}
		//格式化选择质量特性
		function featureFormatter(cellvalue, options, rowdata){
			if(rowdata.itemIndicatorId){
				var div = "<div style=\"width:75%;height:100%;float:left;\" title='"+featureIdFormatter(cellvalue)+"' ondblclick=\"selectFeature('"+rowdata.id+"');\">"+featureIdFormatter(cellvalue)+"</div>";
				div += "<a class=\"small-button-bg\" style=\"margin-left:2px;float:right;\" onclick=\"selectFeature('"+rowdata.id+"');\" href='javascript:void(0);' title='选择质量特性'><span class='ui-icon ui-icon-search' style='cursor:pointer;'></span></a><a class=\"small-button-bg\" style=\"margin-left:2px;float:right\" onclick=\"clearValue('"+rowdata.id+"','"+cellvalue+"')\" href=\"javascript:void(0);\" title=\"清空\"> <span class=\"ui-icon ui-icon-trash\" style='cursor:pointer;'></span></a>";
			   	return div;
			}else{
				return "";
			}
			
		}
		
		//选择质量特性
		var myRowId;
		function selectFeature(rowId){
			myRowId = rowId;
			$.colorbox({href:"${spcctx}/common/feature-bom-select.htm",
				/* href:"${spcctx}/common/feature-bom-multi-select.htm", */
				iframe:true, 
				width:$(window).width()<700?$(window).width()-100:900,
				height:$(window).height()<400?$(window).height()-100:600,
				overlayClose:false,
				title:"选择质量特性"
			});
		}
		//设置值
		function setFeatureValue(datas){
			var featureId = datas[0].id;
			var featureName = datas[0].value;
			var params = {featureId:featureId,featureName:featureName};
// 			params.featureId = featureId;
// 			params.featureName = featureName;
			$("#message").html("正在更新 请稍后 ... ...");
			$.post("${sictx}/base-info/indicator/save-item.htm?indicatorId=${indicatorId}&id="+myRowId,params,function(result){
				$("#message").html(result.message);
				if(result.error){
					//alert(result.message);
				}else{
					refreshItemIndicator();
				}
				setTimeout(function(){
					$("#message").html("");
				},3000);
			},'json');
		}
		function clearValue(rowId,value){
			var params = {featureId:'',featureName:''};
			myRowId = rowId;
			if(!value||!featureObj[value]){
				alert("未设置质量特性");
				return ;
			}else{
				var featureId = featureObj[value];
				$("#message").html("正在更新 请稍后 ... ...");
				$.post("${sictx}/base-info/indicator/save-item.htm?indicatorId=${indicatorId}&id="+myRowId,params,function(result){
					$("#message").html(result.message);
					if(result.error){
						//alert(result.message);
					}else{
						refreshItemIndicator();
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		//同步SPC
		function synchroSpc(id,rowId){
			var rowData = $("#itemIndicatorList").getRowData(rowId);  
			if(rowData['params.featureId'].indexOf("></div>")>0){
				alert("质量参数不能为空，请配置质量参数!");
				return;
			}
			var url = "${sictx}/base-info/indicator/synchro-spc-form.htm?indicatorId="+id;
			$.colorbox({href:url,iframe:true,
				innerWidth:$(window).width()<1366?$(window).width()-124:$(window).width()-366, 
				innerHeight:$(window).height()<768?$(window).height()-68:$(window).height()-68,
				overlayClose:false,
				title:"选择需要同步的时间段",
				onClosed:function(){
					$("#evaluatingIndicatorList").trigger("reloadGrid");
				}
			});
		}
		function deleteItemIndicator(id){
			if(confirm("确定要删除吗?")){
				$("button").attr("disabled","disabled");
				$("#opt-content").attr("disabled","disabled");
				$("#message").html("正在删除项目,请稍候... ...");
				$.post("${sictx}/base-info/indicator/delete-item.htm",{indicatorId:id},function(result){
					$("button").attr("disabled","");
					$("#opt-content").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						refreshItemIndicator();
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		function addInspectingItems(){
			var url='${sictx}/base-info/indicator/inspecting-item-multi-select.htm?indicatorId=${indicatorId}';
			$.colorbox({href:url,
				iframe:true,
				width:850,
				height:$(window).height()-100,
				overlayClose:false,
				onClosed:function(){
// 					refreshItemIndicator();
				},
				title:"添加检验项目"
			});
		}
		
		function setInspectingItemValue(datas){
			var ids = [];
			for(var i=0;i<datas.length;i++){
				ids.push(datas[i].id);
			}
			if(ids.length>0){
				$("button").attr("disabled","disabled");
				$("#opt-content").attr("disabled","disabled");
				$("#message").html("正在添加项目,请稍候... ...");
				$.post("${sictx}/base-info/indicator/add-item.htm",{indicatorId:${indicatorId},deleteIds:ids.join(",")},function(result){
					$("button").attr("disabled","");
					$("#opt-content").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						refreshItemIndicator();
					}
					setTimeout(function(){
						$("#message").html("");
					},3000);
				},'json');
			}
		}
		
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<div class="opt-body" style="height:30px;">
		<div class="opt-btn" style="height:30px;line-height:30px;">
		<button class='btn' onclick="addInspectingItems();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>添加检验项目</span></span></button>
		<button class='btn' type="button" onclick="window.parent.$.colorbox.close();;"><span><span ><b class="btn-icons btn-icons-ok"></b>确定</span></span></button>	
			<span style="color:red;" id="message">
			</span>
		</div>
	</div>
	<div id="opt-content" style="padding-left:6px;">
		<form id="contentForm" name="contentForm" method="post" action="">
			<table id="itemIndicatorList"></table>
		</form>
	</div>
</body>
</html>