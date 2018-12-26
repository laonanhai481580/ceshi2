<%@page import="com.norteksoft.product.util.PropUtils"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>添加监控灯</title>
	<%@include file="/common/meta.jsp" %>
<%-- 	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script> --%>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	
	<script type="text/javascript">
		isUsingComonLayout=false;
		$(document).ready(function(){
			$("#pointEditDate").datepicker({changeMonth:true,changeYear:true});
		});
		//获取表单的值
		function getParams(){
			var params = {};
			$("#detail :input").each(function(index,obj){
				var jObj = $(obj);
				if(obj.name){
				 	if(obj.type == 'checkbox'){
						if(obj.checked){
							if(!params[obj.name]){
								params[obj.name] = jObj.val();
							}else{
								params[obj.name] = params[obj.name] + "," + jObj.val();
							}
						}
					}else{
						params[obj.name] = jObj.val();
					}
			 	}
			});
			return params;
		}
		function saveLight(){
			var params=getParams();
			var ids = jQuery("#qualityFeatureList").getGridParam('records');
			if(ids>1){
				var isCpk=$('input:checkbox[name="params\\.isCpk"]:checked').val();
				if(isCpk=='Y'){
					alert("显示CPK时只能选择一个质量参数!");
					return ;
				}
			}
			 $.post('${spcctx}/base-info/monitor-point/save.htm',params,function(result){
				if(result.errr){
					alert(result.message);
				}else{
					if($.isFunction(window.parent.afterSaveLight)){
						window.parent.afterSaveLight(result);
					}
					window.parent.$.colorbox.close();
				}
			},'json'); 
		}
		//选择人员
		function selectPerson(obj){
			var acsSystemUrl = "${ctx}";
			popTree({ title :'选择人员',
				innerWidth:'400',
				treeType:'MAN_DEPARTMENT_TREE',
				defaultTreeValue:'id',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:obj.id,
				showInputId:obj.id,
				acsSystemUrl:acsSystemUrl,
				callBack:function(){}});
		}
		
		function selectQualityFeature(){
			$.colorbox({href:'${spcctx}/common/feature-bom-multi-select.htm',iframe:true, innerWidth:800, innerHeight:500,overlayClose:false,title:"选择质量特性"});
		}
		
		function setFeatureValue(data){
			 var ids="";
			 for(var i=0;i<data.length;i++){
				 var id=data[i].id;
				 if(ids){
				 ids=ids+','+id;
				 }else{
					 ids=ids+id;
				 }
			 }
			 $("#qualityFeatureIds").val(ids);
		 	//$.post("${spcctx}/base-info/monitor-point/save-qualityfeature.htm",{monitPointId : monitPointId,ids:ids},function(data){
		 		//var monitPointId = $("#monitPointId").val();
	 		$("#qualityFeatureList").setGridParam({postData:{"qualityFeatureIds":ids,type:"add"}});
	 		$("#qualityFeatureList").trigger("reloadGrid");	
		 	//},'json');
		}

		function delRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#inspectionReportList").getGridParam('selarrrow');
			alert(ids);
			if(ids.length<1){
				alert("请选中需要删除的记录！");
				return;
			}
			$.post("${ctx}/inspection-report/delete.htm", {
				deleteIds : ids.join(',')
			}, function(data) {
				//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
				while (ids.length>0) {
					jQuery("#inspectionReportList").jqGrid('delRowData', ids[0]);
				}
			});
		}
		
		function delQualityFeature(){
			var ids = jQuery("#qualityFeatureList").getGridParam('selarrrow');
			if(ids.length<1){
				alert("请选中需要删除的记录！");
				return;
			}
			$.post("${spcctx}/base-info/monitor-point/delete-monit-quality-feature.htm", {
				deleteIds : ids.join(',')
			}, function(data) {
				while (ids.length>0) {
					jQuery("#qualityFeatureList").jqGrid('delRowData', ids[0]);
				}
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
		<div class="opt-btn" style="line-height:28px;">
		<security:authorize ifAnyGranted="spc_base-info_monitor-point_save">
			<button class='btn' onclick="saveLight()"><span><span><b class="btn-icons btn-icons-save"></b>保存</span></span></button>
			</security:authorize>
			<security:authorize ifAnyGranted="spc_common_feature-bom-multi-select">
			<button class='btn' onclick="selectQualityFeature();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>选择质量参数</span></span></button>
			</security:authorize>
			<!-- <button class='btn' onclick="delQualityFeature();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除质量参数</span></span></button> -->
			<button class='btn' onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b>关闭</span></span></button>
			<div style="display: none;" id="message"><s:actionmessage theme="mytheme" /></div>
		</div>
	<div id="detail">
		<input type="hidden" name="id" value="${monitPoint.id}" id="monitPointId"/>
		<input type="hidden" name="type" value="${type}" id="type"/>
		<input type="hidden" name="monitorProgramId" value="${monitorProgramId}" />
		<input type="hidden"  name="qualityFeatureIds" id="qualityFeatureIds" />
		<input type="hidden" name="imageWidth" value="${imageWidth}" />
		<input type="hidden" name="imageHeight" value="${imageHeight}" />
		<input type="hidden" name="myLeft" value="${myLeft}" />
		<input type="hidden" name="myTop" value="${myTop}" />
		<table cellpadding="0" cellspacing="0"  align="center" class="form-table-border-left" style="margin-top: 10px;width: 95%;">
			<tr>
				<td style="width:90px"><span style="color:red;">*</span>监控点编号 :</td>
				<td><input id="pointCode" name="pointCode" value="${monitPoint.pointCode }" class="{required:true}"/></td>
				<td style="width:90px"><span style="color:red;">*</span>监控点名称:</td>
				<td><input id="pointName" name="pointName" value="${monitPoint.pointName }" class="{required:true}"/> </td>
			</tr>
			<tr>
				<td style="width:90px"><span style="color:red;">*</span>编&nbsp;辑&nbsp;人:</td>
				<td><input id='pointEditer' name='pointEditer' value='${monitPoint.pointEditer }' onclick="selectPerson(this)" readonly="readonly" class="{required:true}"/></td>
				<td style="width:90px"><span style="color:red;">*</span>编辑日期:</td>
				<td><input id='pointEditDate' name='pointEditDate' value='<s:date name="pointEditDate" format="yyyy-MM-dd" />' readonly="readonly" class="line"/></td>
			</tr>
			<tr>
			<td style="width:90px"></span>是否显示CPK:</td>
				<td colspan="3"><input id='params.isCpk' name='params.isCpk'  type="checkbox" class="{required:true}"  value="Y" <s:if test="%{#monitPoint.isCpk==\"Y\"}"> checked="checked" </s:if>/></td>
			</tr>
			<tr>
				<td style="width:90px"></span>备注:</td>
				<td colspan="3"><textarea id='remark' name='remark' rows="10" style="width:450px;" >${monitPoint.remark }</textarea></td>
			</tr>
		</table>
		<table id="qualityFeatureList" style="width: 80%;"></table>
		<div id="pager"></div> 
		<script type="text/javascript">
		$(document).ready(function(){
			var monitPointId = $("#monitPointId").val();
			var type = $("#type").val();
			jQuery("#qualityFeatureList").jqGrid({
				url:'${spcctx}/base-info/monitor-point/list-monit-qualityfeature-datas.htm',
				postData:{monitPointId:monitPointId,type:type},
				pager:"#pager",
				rownumbers:true,
				height:200,
				colNames:['参数名称','参数编码','数据类型','数据单位'],
				colModel:[{name:'name', index:'name',width:100},
				          {name:'code', index:'code',width:100},
				          {name:'paramType', index:'paramType',width:100},
				          {name:'unit', index:'unit',width:100}]
			});
       	});
		</script>
		</div>
	</div>
</body>
</html>