<%@page import="com.ambition.supplier.utils.DateUtil"%>
<%@page import="com.norteksoft.product.api.ApiFactory"%>
<%@page import="com.norteksoft.product.api.entity.Department"%>
<%@page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript"
	src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
<script type="text/javascript">
	 //点击指定的编号进入编辑
	function click(cellvalue, options, rowObject){
		if(cellvalue){
			return "<a href='${mfgctx}/data-acquisition/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
		}else{
			return '';
		}		
	}
	function $oneditfunc(rowId){
		var readOnlys = ['productName','productModel'];
		for(var i=0;i<readOnlys.length;i++){
			$("#" + rowId + "_" + readOnlys[i]).attr("readOnly","readOnly")
				.css({
					'background-color':'rgb(251, 236, 136)',
					'border':'0px'
				});
		}
		var val1 = $("#" +rowId + "_reportNo").val();
		val1= val1.replace(/<[^<>]*>/g,'').replace("&nbsp;","");
		$("#" +rowId + "_reportNo").val(val1);
		var repairDate = $("#" +rowId + "_repairDate").val();
		if(!repairDate){
			$("#" +rowId + "_repairDate").val('<%=DateUtil.formateDateStr(new Date())%>');
		}
       	$('#'+rowId+'_workHoursCost','#dynamicRepairRecordReport').change(function(){
			caclute(rowId);
         });
       	$('#'+rowId+'_productCode','#dynamicRepairRecordReport').bind('click',function(){
		 	selectModel(rowId);
		});
        $('#'+rowId+'_badPhenomenon','#dynamicRepairRecordReport').bind('click',function(){
			 selectBadPhenomenon(rowId);
		});
        $('#'+rowId+'_replaceMaterial','#dynamicRepairRecordReport').bind('click',function(){
		 	selectReplaceMaterial(rowId);
		});
		$("tr[id="+rowId+"] :input").bind("keydown",function(e){
			if(e.keyCode == 13){
				caclute(rowId);
			}
		});
    }
	
	//选择产品型号
	var editRowId = null;
	var isFlag=null;
	function selectModel(rowId){
		editRowId=rowId;
		isFlag = "product";
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择产品型号"
 		});
 	}
	function setFullBomValue(datas){
     	if("product"==isFlag){
	 		$("#"+editRowId+"_productModel").val(datas[0].model);
	 		$("#"+editRowId+"_productCode").val(datas[0].code);
	 		$("#"+editRowId+"_productName").val(datas[0].name);
		}
		if("material"==isFlag){
			$("#"+editRowId+"_replaceMaterial").val(datas[0].name);
		}
	}
	//选择不良现象
	function selectBadPhenomenon(rowId){
		editRowId=rowId;
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/defection-code-bom.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择不良现象"
 		});
 	}
	
	function  setDefectionValue(datas){
		$("#"+editRowId+"_badPhenomenon").val(datas[0].value);
	}
	//选择物料
	function selectReplaceMaterial(rowId){
		editRowId=rowId;
		isFlag = "material";
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料"
 		});
 	}
	
	//选择部门
	function selectObj(title,obj,treeType,treeValue,hiddenInputId){
		var acsSystemUrl = "${ctx}";
		popTree({ title :title,
			innerWidth:'400',
			treeType:treeType,
			defaultTreeValue:treeValue?treeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:hiddenInputId?hiddenInputId:obj.id,
			showInputId:obj.id,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
	
	function  setDefectionValue(datas){
		$("#"+editRowId+"_badPhenomenon").val(datas[0].value);
	}
	
	//系统计算维修总计
	function caclute(rowId){
        var materialCost = $('#'+rowId+'_materialCost','#dynamicRepairRecordReport').val();
        var a = parseFloat(materialCost);
        var productCost = $('#'+rowId+'_productCost','#dynamicRepairRecordReport').val();
	    var b = parseFloat(productCost);
	    var workHoursCost = $('#'+rowId+'_workHoursCost','#dynamicRepairRecordReport').val();
	    var c = parseFloat(workHoursCost);
		var	totalValue=a+b+c;
        jQuery('#'+rowId+'_repairTotalCost','#dynamicRepairRecordReport').val(totalValue);
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="MFG_REPAIR-RECORD_LIST_SAVE">
		  isRight =  true;
		</security:authorize>
		return isRight;
	}
</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu = "data_acquisition";
		var thirdMenu = "repair_record_report_list";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>

	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div id="opt-content">
					<p style="font-size: 40px;font-weight: bold;text-align: center;margin: 200px;">请到基础设置的采集点配置中添加用户权限</p>
				</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>