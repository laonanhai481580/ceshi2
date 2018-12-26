<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript">
	function $successfunc(response){
		if(response.responseText=="false"){
			alert("检验数不等于合格数加不良数！");
			return false;
		}else if(response.responseText=="false1"){
			alert("检验数必须小于来料数！");
			return false;
		}else if(response.responseText=="false2"){
			alert("不良数不等于不良细项中的不良数总和！");
			return false;
		}else if(response.responseText=="false3"){
			alert("不合格品处理单号已经存在！");
			return false;
		}else{
			return true;
			}
			
		}
	var materialNameId = null;
	function materialCodeClick(obj){
		materialNameId = obj.currentInputId;
		$.colorbox({href:"${mfgctx}/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function materialNameClick(obj){
		materialNameId = obj.currentInputId;
		var url = '${mfgctx}';
		$.colorbox({href:url+"/common/product-bom-select.htm",
			iframe:true, 
			width:$(window).width()<700?$(window).width()-100:700, 
			innerHeight:$(window).height()<400?$(window).height()-100:400,
 			overlayClose:false,
 			title:"选择物料BOM"
 		});
 	}
	function setBomValue(datas){
		var id=materialNameId.split("_");
		$("#" + materialNameId).val(datas[0].value);
		$("#" + id[0]+"_materialCode").val(datas[0].key);
		$("#" + id[0]+"_materialName").val(datas[0].value);
 	}
	
	var dutySupplierId = null;
	function dutySupplierClick(obj){
		dutySupplierId = obj.currentInputId;
		var url='${supplierctx}'
		//var url='http://localhost:8082/supplier/evaluate/supplier-select.htm';
		$.colorbox({href:url+"/archives/select-supplier.htm",iframe:true, innerWidth:1000, innerHeight:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#" + dutySupplierId).val(obj.name);
	}
	//超链接显示表单
	function descMessage(cellValue,options,rowObj){
		if(rowObj.status==0){
			return "<a   href='${iqcctx}/defective-goods/input.htm?id=" + rowObj.id + "'>"+cellValue+"</a>";
		}else{
			return "<a   href='${iqcctx}/defective-goods/input.htm?id=" + rowObj.id + "'><font color='red'>"+cellValue+"</font></a>";
		}
		
    }
	
	//导入
	function imports(){
		var url = '${iqcctx}/defective-goods/imports.htm';
		$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
			overlayClose:false,
			title:"导入",
			onClosed:function(){
			}
		});
	}
	//重写(给单元格绑定事件)
	function $oneditfunc(rowid){
		var val = $("#" +rowid + "_unqualityNo").val();
		val = val.replace(/<[^<>]*>/g,'');
		if(val=='undefined'){
			val="";
		}
		$("#" +rowid + "_unqualityNo").val(val);
		jQuery('#'+rowid+'_inspectionAmount','#dynamicDefectiveGood').change(function(){
			caclute(rowid);
		});
		jQuery('#'+rowid+'_unqualifiedAmount','#dynamicDefectiveGood').change(function(){
			caclute(rowid);
		});
		enterKeyToNext("dynamicDefectiveGood",rowId,function(){
		},true);
	}
	//系统计算合格率和不良值
	function caclute(rowid){
		var inspectionAmount = jQuery('#'+rowid+'_inspectionAmount','#dynamicDefectiveGood').val();
		var unqualifiedAmount = jQuery('#'+rowid+'_unqualifiedAmount','#dynamicDefectiveGood').val();
		var qualifiedAmount = inspectionAmount-unqualifiedAmount;
		jQuery('#'+rowid+'_qualifiedAmount','#dynamicDefectiveGood').val(qualifiedAmount);
	}
	function click(cellvalue, options, rowObject){	
		return "<a href='${iqcctx}/defective-goods/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
	}
	function showInfo(id){
		window.location="${iqcctx}/defective-goods/input.htm?id="+id;
	}
	function $beforeEditRow(rowId,iRow,iCol,e){
		var isRight = false;
		<security:authorize ifAnyGranted="defective-save">
			isRight =  true;
		</security:authorize>
		return isRight;
	}
	function toImprove(){
		//获取行号
		var id1 = jQuery("#dynamicDefectiveGood").jqGrid('getGridParam','selarrrow');
		var status1=jQuery("#dynamicDefectiveGood").getCell(id1,"status");
		var id=jQuery("#dynamicDefectiveGood").getCell(id1,"id");
		if(id1.length>=2){
			$("#message").html("只能选一条");
			setTimeout(function() {
				$("#message").html('');
			}, 1000);
			return;
		}
		if(id1.length==0){
			$("#message").html("选择一条要整改的数据");
			setTimeout(function() {
				$("#message").html('');
			}, 1000);
			return;
		}
		if(status1==1){
			$("#message").html("已经提交过了");
			setTimeout(function() {
				$("#message").html('');
			}, 1000);
			return;
		}
		//window.location.href="${iqcctx}/defective-goods/input.htm?id="+ id ;
	    $("#message").html("正在提交，请稍候... ...");
		 var url = "${iqcctx}/defective-goods/submit-process.htm?id="+ id ;
	     $(jQuery("#dynamicDefectiveGood").setCell(id1,"status","1"));
	     var status = jQuery("#dynamicDefectiveGood").getCell(id1,"status");
	     
		$.post(url,{"status":status},function(result){
			//$("#btnDiv").find("button.btn").attr("disabled",false);
			$("#message").html("");
			if(result.error&&result.error!=""){
				alert(result.message);
			}else{
				$("#id").val(result.id);
				$("#message").html(result.message);
				showMsg();
			}
			setTimeout(function() {
				$("#message").html('');
			}, 1000);
			$("#dynamicDefectiveGood").trigger("reloadGrid");
		},"json"); 
		//$("#dynamicDefectiveGood").trigger("reloadGrid");
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="defectiveReport";
		var thirdMenu="myDefectiveMonitor";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/iqc-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/iqc-defective-report-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
					<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<span style="color:red;" id="message"></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" name="contentForm" method="post" action="">
						<grid:jqGrid gridId="dynamicDefectiveGood" url="${iqcctx}/defective-goods/list-monitor-datas.htm" code="IQC_DGPR_MONITOR" pageName="page"></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
</html>