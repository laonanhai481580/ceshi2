<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
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
		//重写编辑后的方法
		function $oneditfunc(rowId){
			$("input","#"+rowId).each(function(index,obj){
				if(obj.name.indexOf("composingItems.name")>-1){
					if(obj.name.indexOf("_") == -1){
						$(obj).attr("readonly","readonly").click(function(){
							selectComposing(this);
						});
					}else if(obj.name.indexOf("_departmentName")>-1){
						$(obj).attr("readonly","readonly").click(function(){
							selectWorkGroup("选择部门","DEPARTMENT_TREE");
							selectObj = obj;
						});
					}
				}else if(obj.name.indexOf("detailItems.")>-1){
					if(obj.name.indexOf("_") == -1){
						$(obj).attr("readonly","readonly").click(function(){
							selectDefectionCode(this);
						});
					}else{
						$(obj).bind("keyup",function(){
							caculateBadNumbers(obj.id.split("_")[0]);
						}).bind("change",function(){
							caculateBadNumbers(obj.id.split("_")[0]);
						});
					}
				}
			});
			/* $("#" + rowId + "_partCode").click(function(){
				selectComponentCode(this);
			});
			$("#" + rowId + "_partName").click(function(){
				selectComponentCode(this);
			}); */
		}
		var modelSpecificationId = null;
		var myId = null;
		function modelSpecificationClick(obj){
			modelSpecificationId = obj.currentInputId;
			myId = modelSpecificationId.replace("_modelSpecification","");
			$.colorbox({href:"${mfgctx}/common/product-bom-model-select.htm",
				iframe:true, 
				innerWidth:750, 
				innerHeight:500,	
				overlayClose:false,	
				title:"选择产品型号"
			});
		}
		
		function nameFactory(cellvalue, options, rowObject){
			if(cellvalue == 'FQC'){
				return '制程';
			}else if (cellvalue == 'IQC'){
				return '进货检验';
			}
		}

		//设置产品型号的值
		function setBomModelValue(data){
			$("#" + modelSpecificationId).val(data[0].value);
			//自动生成产品类型和系列
			setModelAndSeries(data[0].value);
		}
		//根据产品型号设置产品类型和产品系列
		function setModelAndSeries(value){
			$.post('${mfgctx}/inspection/daliy-report/model-series.htm',{model:value},function(data){
				if(data.error){
					alert(data.message);
				}else{
					$("#"+myId+"_productModel").val(data.type);
// 					jQuery('#'+myId+'_productSeries','#defectiveGoods').val(data.series);
				}
			},'json');
		}
		function caculateBadNumbers(rowId){
			var val = 0;
			$("input","#"+rowId).each(function(index,obj){
				if(obj.name.indexOf("detailItems.")>-1&&obj.name.indexOf("_") > -1){
					if(!isNaN(obj.value)&&!isNaN(parseInt(obj.value))){
						val += parseInt(obj.value);
					}
				}
			});
			$("#" + rowId + "_unqualifiedAmount").val(val);
		}
		function selectWorkGroup(title,treeType){
			var acsSystemUrl = "${ctx}";
			popTree({ title :title,
				innerWidth:'400',
				treeType:treeType,
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:'workGroupId',
				showInputId:'workGroupId',
				acsSystemUrl:acsSystemUrl,
				callBack:function(){
					selectObj.value = document.getElementById("workGroupId").value;
				}});
		}
		//选择质量成本
		var selComposingObj = '';
		function selectComposing(obj){
			selComposingObj = obj;
			var url = '${costctx}/common/composing-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
		}
		
		function setFullComposingValue(datas){
	 		var obj = datas[0];
	 		$(selComposingObj).val(obj.name).focus();
		}
		//选择不良代码
	 	function selectDefectionCode(obj){
	 		selComposingObj = obj;
	 		var url = '${mfgctx}/common/defection-code-bom.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择不良代码"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setDefectionValue(data){
	 		$(selComposingObj).val(data[0].value).focus();
	 	}
	 	
	 	//选择BOM零部件
	 	var partId = '';
	 	function partCodeClick(obj){
	 		partId = obj.currentInputId;
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择BOM零部件"
	 		});
	 	}
	 	function partNameClick(obj){
	 		partId = obj.currentInputId;
	 		var url = '${mfgctx}/common/product-bom-select.htm';
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"选择BOM零部件"
	 		});
	 	}
		
	 	//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setBomValue(datas){
	 		var rowId = partId.split("_")[0];
	 		$("#"+rowId+"_partCode").val(datas[0].key);
	 		$("#"+rowId+"_partName").val(datas[0].value);
	 	}
		function showInfo(id){
			var url = '${mfgctx}/defective-goods/ledger/view-info.htm?id=' + id;
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:$(window).width()<900?$(window).width()-50:900, 
				innerHeight:$(window).height()<680?$(window).height()-50:$(window).height(),
	 			overlayClose:false,
	 			title:"查看不合格处理单详情"
	 		});
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="defective-goods-ledger-save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		function createReport(){
			window.location='${mfgctx}/defective-goods/ledger/input.htm';
		}
		function inputLinkClick(cellvalue, options, rowObject){
			return "<a href='${mfgctx}/defective-goods/ledger/input.htm?id="+rowObject.id+"'>"+cellvalue+"</a>";
		}
		function formateAttachmentFiles(value,o,obj){
			var btn = "<a style='float:left;display:none;' class=\"small-button-bg upload\" onclick=\"beginUpload("+obj.id+");\" href=\"#\" title='上传附件'><span class='ui-icon ui-icon-image' style='cursor:pointer;'></span></a>";
			return "<div>" + btn +"<span id='"+obj.id+"_showAttachmentFiles'>" + $.getDownloadHtml(value) + "</span><input id='"+obj.id+"_hiddenAttachmentFiles' type='hidden' value='"+(value?value:'')+"'></input></div>";
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="regectManager";
		var thirdMenu="_defective_goods_input";
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
		<div class="opt-body">
			<div class="opt-btn">
			<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_LIST_INPUT">
			<button class='btn' onclick="createReport();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_LIST_DELETE">
			<button class='btn' onclick="iMatrix.delRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
			</security:authorize>
			<button class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
			<security:authorize ifAnyGranted="MFG_DEFECTIVE-GOODS_LIST_EXPORT">
			<button class='btn' onclick="iMatrix.export_Data('${mfgctx}/defective-goods/ledger/exports.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>	
			</security:authorize>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr>删除成功</nobr></font></div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<input type="hidden" id="workGroupId"></input>
					<grid:jqGrid gridId="defectiveGoods" url="${mfgctx}/defective-goods/ledger/list-datas.htm" code="MFG_DEFECTIVE_GOODS" dynamicColumn="${dynamicColumn}"></grid:jqGrid>
				</form>
			</div>
		</div>
	</div>
</body>
<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
</html>