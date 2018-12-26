<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
<script type="text/javascript">
	//重写保存后的方法
	function $successfunc(response){
		var result = eval("(" + response.responseText + ")");
		if(result.error){
			alert(result.message);
			return false;
		}else{
			return true;
		}
	}
	function upload(id){
		var url = '${gsmctx}/inspectionplan/upload.htm?id=' + id;
		$.colorbox({href:url,iframe:true, innerWidth:500, innerHeight:300,
			overlayClose:false,
			title:"导入",
			onClosed:function(){
				$.post("${gsmctx}/inspectionplan/get-attach-name.htm?id="+id+"",id,function(result){
					if(result.error){
						alert(result.message);
					}else{
						getAttachName(id,result);
					}
				},'json');
				
			}
		});
	}
	function getAttachName(id,result){
		var totalhtml='';
		for(var i=0;i<result.length;i++){
			var html = '';
			var obj = result[i];
			html="<a href='${gsmctx}/inspectionplan/exupload.htm?id="+obj.id+"'>"+obj.name+"</a>&nbsp;&nbsp;&nbsp;";
			html=html+"<a href='${gsmctx}/inspectionplan/uploaddelete.htm?id="+obj.id+"'>删除</a><br/>";
			totalhtml=totalhtml+html;
		}
		$("#" + id + "_attachs").html(totalhtml);
	}
	function deleteAttach(id){
		
	}
	//格式化
	function operateFormater(cellValue,options,rowObj){
		var operations='';
		if(rowObj.id){
			operations="<div style='text-align:center;'><a href='javascript:downloadFile("+rowObj.id+")'><span style='cursor:pointer;'>下载附件</span></a>"+
				       " | "+"<a href='javascript:deleteAttach("+rowObj.id+")'><span style='cursor:pointer;'>清除</span></a></div>";
		}else{
			operations="<div style='text-align:center;'><a href='javascript:upload("+rowObj.id+")'><span style='cursor:pointer;'>上传附件</span></a></div>";
		}
		return operations;
	}
	//选择受审核方（供应商）
	var dutySupplierId = null;
	function auditedSupplierClick(obj){
		dutySupplierId = obj.currentInputId;
		var url='${supplierctx}';
		$.colorbox({href:url+"/archives/select-supplier.htm",iframe:true, innerWidth:1000, innerHeight:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#" + dutySupplierId).val(obj.name);
	}
	//选择审核员
	function auditerClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择审核员',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.rowid+"_auditer",
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	} 
	//选择验证人
	function verifierClick(obj){
		var acsSystemUrl = "${ctx}";
		popTree({ title :'选择验证人',
			innerWidth:'400',
			treeType:'MAN_DEPARTMENT_TREE',
			defaultTreeValue:'id',
			leafPage:'false',
			multiple:'false',
			hiddenInputId:obj.rowid+"_verifier",
			showInputId:obj.currentInputId,
			acsSystemUrl:acsSystemUrl,
			callBack:function(){}});
	}
</script>

</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="manager";
		var thirdMenu="_improve_all";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-manager-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
		<div class="opt-body">
			<div class="opt-btn">
				<button class='btn' onclick="iMatrix.addRow();"><span><span>新建</span></span></button>
				<button class='btn' onclick="iMatrix.delRow();"><span><span>删除</span></span></button>
				<button class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span>查询</span></span></button>
				<!-- <button class='btn' onclick="imports();"><span><span>导入</span></span></button> -->
				<!-- <button class='btn' onclick="exportSuppliers();"><span><span>导出</span></span></button> -->
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<grid:jqGrid gridId="supplierImproves"
						url="${supplierctx}/manager/list-datas.htm" code="SUPPLIER_SUPPLIER_IMPROVE"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		$(document).ready(function(){
			$("#supplierImproves").jqGrid('setGroupHeaders', {
				  useColSpanStyle: true, 
				  groupHeaders:[
					{startColumnName: 'serious', numberOfColumns: 4, titleText: '不符合项描述'},
					{startColumnName: 'requireReplyTime', numberOfColumns: 3, titleText: '纠正措施'},
					{startColumnName: 'verifyTime', numberOfColumns: 3, titleText: '验证'},
					{startColumnName: 'attachment', numberOfColumns: 2, titleText: '附件'}
				  ]
			});
		});
	</script>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>