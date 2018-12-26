<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	Integer selLevel = (Integer)request.getAttribute("selLevel");
	if(selLevel==null){
		selLevel = 0;
	}
%>
<script type="text/javascript">
	function selectSupplier(){
		var url='${supplierctx}/archives/select-supplier.htm';
		$.colorbox({href:url,iframe:true, innerWidth:1000, innerHeight:600,
			overlayClose:false,
			title:"选择供应商"
		});
	}
	function setSupplierValue(objs){
		var obj = objs[0];
		$("#supplierName").val(obj.name);
		changeLocation({supplierId:obj.id});
	}
	$(document).ready(function(){
		var active = 0;
		var selLevel = "<%=selLevel%>";
		if(selLevel!='null'){
			active = parseInt(selLevel);
		}
		$( "#accordion1" ).accordion({
			animated:false,
			collapsible:false,
			event:'click',
			fillSpace:true,
			active:active
		});
		if(active==0){
			createInputForm();
		}
	});
	function createInputForm(){
		 var materialType = "";
		 var evaluateMonth = "";
		 var modelId = "";
		 if(document.getElementsByName("evaluate.materialType").length!=0){
			 materialType = document.getElementsByName("evaluate.materialType")[0].selectedOptions[0].innerHTML;
		 }
		 if(document.getElementsByName("evaluate.evaluateMonth").length!=0){
			 evaluateMonth = document.getElementsByName("evaluate.evaluateMonth")[0].selectedOptions[0].value;
		 }
		 if(document.getElementsByName("modelId").length!=0){
			 modelId = document.getElementsByName("modelId")[0].selectedOptions[0].value;
		 }
		<%if(selLevel==0){%>
			var selectId = '${estimateModelId}';
			var error = '${error}';
			var children = ${estimateModelMaps};
			var tempChildren = new Array();
			//数据权限
			for (var i = 0; i < children.length; i++) {
				//alert(children[i].data);
				//alert(children[i].attr.customType);
				if("estimateModel" == children[i].attr.customType){
					<security:authorize ifAnyGranted="supplier_evaluate_add">
						tempChildren.push(children[i]);
					</security:authorize>
				}
				/* if("all" == children[i].attr.customType){
					<security:authorize ifAnyGranted="supplier_evaluate_all">
						tempChildren.push(children[i]);
					</security:authorize>
				} */
				if("ledger" == children[i].attr.customType){
					<security:authorize ifAnyGranted="supplier_evaluate_ledger">
						tempChildren.push(children[i]);
					</security:authorize>
				}
				if("evaluate-total-table" == children[i].attr.customType){
					<security:authorize ifAnyGranted="supplier_evaluate_total-table">
						tempChildren.push(children[i]);
					</security:authorize>
				};
			}
			$("#left_tree_div").jstree({ 
				"json_data" : {
					"data" : tempChildren,
				},
				"ui" : {
					"initially_select" : [selectId],
				},
				"plugins" : [ "themes", "json_data", "ui" ],
			}).bind("select_node.jstree",function(e,data){
				var id = data.rslt.obj.attr("id");
				if(id == selectId){
					return;
				}
				var isLeaf = data.rslt.obj.attr("isLeaf");
				if(isLeaf == 'true'){
					var customType = data.rslt.obj.attr("customType");
					if(customType == 'estimateModel'){
						<security:authorize ifAnyGranted="supplier_evaluate_add">
						$(document).mask();
						window.location = '${supplierctx}/evaluate/quarter/add.htm?supplierId=${supplierId}&estimateModelId=' + data.rslt.obj.attr("id") + "&evaluateYear=${evaluateYear}&materialType="+materialType+"&evaluateMonth="+evaluateMonth+"&modelId="+modelId;
						</security:authorize>
					}else if(customType == 'all'){
						<security:authorize ifAnyGranted="supplier_evaluate_all">
						$(document).mask();
						window.location = '${supplierctx}/evaluate/quarter/all.htm?supplierId=${supplierId}&evaluateYear=${evaluateYear}';
						</security:authorize>
					}else if(customType == 'ledger'){
						<security:authorize ifAnyGranted="supplier_evaluate_ledger">
						$(document).mask();
						window.location = '${supplierctx}/evaluate/quarter/ledger.htm?supplierId=${supplierId}&evaluateYear=${evaluateYear}';
						</security:authorize>
					}else if(customType == 'point-rank'){
						<security:authorize ifAnyGranted="supplier_evaluate_point-rank">
						$(document).mask();
						window.location = '${supplierctx}/evaluate/point-rank/performanceEvaluate-list.htm?supplierId=${supplierId}&evaluateYear=${evaluateYear}';
						</security:authorize>
					}else if(customType == 'evaluate-total-table'){
						<security:authorize ifAnyGranted="supplier_evaluate_total-table">
						$(document).mask();
						window.location = '${supplierctx}/evaluate/quarter/evaluate-total-table.htm?supplierId=${supplierId}&evaluateYear=${evaluateYear}';
						</security:authorize>
					};
				};
			});
		<%}%>
	}
</script>
<div id="accordion1">
	<security:authorize ifAnyGranted="supplier_evaluate_add,supplier_evaluate_total-table,supplier_evaluate_all,supplier_evaluate_point-rank,supplier_evaluate_ledger">
		<h3><a id="_bom" onclick="javascript:window.location='<grid:authorize code="supplier_evaluate_add,supplier_evaluate_total-table,supplier_evaluate_all,supplier_evaluate_point-rank,supplier_evaluate_ledger" systemCode="supplier"></grid:authorize>';">评价录入</a></h3>
		<div style="padding:0px;">
			<div class="opt-body" id="selectsupplier" style="height:0px;">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="supplier_evaluat_quarter_left_select_supplier">
						<script>
						   $("#selectsupplier").attr("style","height:30px;");
						</script>
						<table cellpadding="0" cellspacing="0" style="width:100%;height:30px;">
							<tr>
								<td valign="middle">
								<input type="text" name="supplierName" id="supplierName" value="${supplierName}"
									readonly="readonly" style="width: 140px;" />
								</td>
								<td>
									<a class="small-button-bg" style="margin-left:2px;float:left;" onclick="selectSupplier();"><span class="ui-icon ui-icon-search" style='cursor:pointer;' title="选择供应商"></span></a>
								</td>
							</tr>
						</table>
					</security:authorize>
				</div>
			</div>
			<div id="left_tree_div" style="padding:4px;"></div>
		</div>
	</security:authorize>
	<security:authorize ifAnyGranted="supplier_evaluate_total-view,supplier_evaluate_manager_warning-sign,supplier_evaluate_manager_estimate-stat-distribution,supplier_evaluate_manager_qcds,supplier_evaluate_point-rank">
		<h3><a id="_list" onclick="javascript:window.location='<grid:authorize code="supplier_evaluate_total-view,supplier_evaluate_manager_warning-sign,supplier_evaluate_manager_estimate-stat-distribution,supplier_evaluate_manager_qcds,supplier_evaluate_point-rank" systemCode="supplier"></grid:authorize>';">评价汇总</a></h3>
		<div style="padding:0px;">
			<!-- 供应商红黄牌 -->
			<security:authorize ifAnyGranted="supplier_evaluate_manager_warning-sign">
				<div id="_warning_sign" class="west-notree" onclick="javascript:window.location='${supplierctx }/manager/warning-sign.htm';"><span>供应商红黄牌</span></div>
			</security:authorize>
			<security:authorize ifAnyGranted="supplier_evaluate_total-view">
				<div id="total_view_list" class="west-notree" onclick="javascript:window.location='${supplierctx}/evaluate/quarter/list.htm';"><span>供应商评价汇总表</span></div>
			</security:authorize>
			<!-- 供应商评级分布 -->
			<security:authorize ifAnyGranted="supplier_evaluate_manager_estimate-stat-distribution">
				<div id="mySupplierEstimateStatDistribution" class="west-notree" onclick="javascript:window.location='${supplierctx }/manager/degree/supplier-estimate-stat-distribution.htm';"><span>供应商评级分布</span></div>
			</security:authorize>
			<!-- 供应商QCDS评价总表 -->
			<security:authorize ifAnyGranted="supplier_evaluate_manager_qcds">
				<div id="mySupplierQcdsEvaluate" class="west-notree" onclick="javascript:window.location='${supplierctx }/manager/supplier-qcds.htm';"><span>供应商QCDS评价总表</span></div>
			</security:authorize>
<%-- 			<security:authorize ifAnyGranted="supplier_evaluate_point-rank"> --%>
<%-- 				<div id="performanceEvaluate_list" class="west-notree" onclick="javascript:window.location='${supplierctx}/evaluate/point-rank/performanceEvaluate-list.htm';"><span>供应商评价排名</span></div> --%>
<%-- 			</security:authorize> --%>
		</div>
	</security:authorize>
</div>
<script type="text/javascript">
	$(document).ready(function(){
		$('#'+thirdMenu).addClass('west-notree-selected');
	});
</script>
