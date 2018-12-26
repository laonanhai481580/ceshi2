<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	
	$(document).ready(function(){
	$("#detail_table").jqGrid({
		url : '${mfgctx}/data-acquisition/list-reason-measure.htm?id='+${defectiveItem.id},
		datatype : 'json',
		rowNum : 15,
		rownumbers : true,
		multiselect : false,
		colNames : [ '', '原因编码', '原因描述', '措施编码','措施描述' ],
		colModel : [ {
			name : 'id',
			index : 'id',
			width : 1,
			hidden : true,
			align : 'center'
		}, {
			name : 'reasonNo',
			index : 'reasonNo',
			width : 65,
			align : 'center',
			editable : false
		}, {
			name : 'reasonName',
			index : 'reasonName',
			width : 80,
			align : 'center',
			editable : false
		}, {
			name : 'measureNo',
			index : 'measureNo',
			width : 65,
			align : 'center',
			editable : false
		},{
			name : 'measureName',
			index : 'measureName',
			width : 80,
			align : 'center',
			editable : false
		} ],
		multiselect : true,
		gridComplete : function() {

		},
	});
	});
	
	//获取表单的值
	function getParams(){
		var params = {};
		$("#detail input[type=text]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#detail textarea").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		$("#detail input[type=hidden]").each(function(index,obj){
			var jObj = $(obj);
			if(obj.name&&jObj.val()){
				params[obj.name] = jObj.val();
			}
		});
		return params;
	}
		function saveReason(){
			var params=getParams();
			$.post('${mfgctx}/data-acquisition/save-reason.htm',params,function(){
				window.parent.$.colorbox.close();
			});
		}
		function saveReasonAndMeasure(){
			var params=getParams();
			$.post('${mfgctx}/data-acquisition/save-reason-measure.htm',params,function(){
				$("#detail_table").trigger("reloadGrid");
				$("#reasonNo").val('');
				$("#reasonName").val('');
				$("#measureNo").val('');
				$("#measureName").val('');
			});
		}
		function delReasonAndMeasure(){
			var ids = jQuery("#detail_table").getGridParam('selarrrow');
			 $.post('${mfgctx}/data-acquisition/delete-reason-measure.htm?ids='+ids,ids,function(){
				$("#detail_table").trigger("reloadGrid");
			}); 
		}
	
		function RAndMClick(obj){
			var url = '${mfgctx}';
			$.colorbox({href:url+"/data-acquisition/reason-measure-lib/reason-measure-select.htm",iframe:true, innerWidth:700, innerHeight:400,
	 			overlayClose:false,
	 			title:"原因措施库!"
	 		});
	 	}
		function setRAndMValue(datas){
			$("#reasonNo").val(datas[0].reasonNo);
			$("#reasonName").val(datas[0].reasonName);
			$("#measureNo").val(datas[0].measureNo);
			$("#measureName").val(datas[0].measureName);
			
	 	}
		
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="opt-body">
	<div id="opt-content">
	<div id="detail">
	<input type="hidden" name="id" value="${defectiveItem.id}" />
		<table>	
		<tr>
		<td  colspan="4">不良信息</td>
		</tr>
		<tr>
		<td>方位:</td>
		<td><input value="${defectiveItem.directionCodeNo }" disabled="disabled"/></td>
		<td><input value="${defectiveItem.directionCodeName }" disabled="disabled"/></td>
		</tr>
		<tr>
		<td>部位:</td>
		<td><input value="${defectiveItem.positionCodeNo }" disabled="disabled"></input></td>
		<td><input value="${defectiveItem.positionCodeName}" disabled="disabled"></input></td>
		</tr>
		<tr>
		<td>不良:</td>
		<td><input value="${defectiveItem.code }" disabled="disabled"></input></td>
		<td><input value="${defectiveItem.name }" disabled="disabled"></input></td>
		</tr>
		<tr>
		<td colspan="4">原因/措施</td>
		</tr>
		<tr>
		<td>原因:</td>
		<td><input type="text" name="reasonNo" id="reasonNo"/></td>
		<td><input type="text" name="reasonName" id="reasonName"/></td>
		<td><button  class='btn' onclick="RAndMClick();"><span><span>选择</span></span></button></td>
		</tr>
		<tr>
		<td>措施:</td>
		<td><input type="text" name="measureNo" id="measureNo"/></td>
		<td><input type="text" name="measureName" id="measureName"/></td>
		<td><button  class='btn' onclick="RAndMClick();"><span><span>选择</span></span></button></td>
		</tr>
		<tr >
		<td colspan="4">详细说明:</td>
		</tr>
		<tr>
		<td colspan="4"><textarea rows="5" style="width:100%" name="detailDescription" id="detailDescription">${defectiveItem.detailDescription}</textarea></td>
		</tr>
		<tr>
		</tr>
		<tr>
		<td colspan="5">
	<table id="detail_table"></table>
		</td>
		</tr>
		</table>
		<table>
		<tr align="center">
		<td style="width:25%"></td>
		<td>&nbsp;<button  class='btn' onclick="delReasonAndMeasure();"><span><span>删除</span></span></button></td>
		<td>&nbsp;<button  class='btn' onclick="saveReasonAndMeasure();"><span><span>新增</span></span></button></td>
		<td>&nbsp;<button  class='btn' onclick="saveReason();"><span><span>保存</span></span></button></td>
		<td>&nbsp;<button  class='btn' onclick="window.parent.$.colorbox.close();"><span><span>退出</span></span></button></td>
		</tr></table>
	</div>
	</div>
	</div>
</body>
</html>