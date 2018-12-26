<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.opensymphony.xwork2.ActionContext"%>
<%@page import="com.ambition.cost.entity.CostRecord"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript" src="${resourcesCtx}/widgets/validation/validate-all-1.0.js"></script>
    <script type="text/javascript" src="${resourcesCtx}/widgets/validation/dynamic.validate.js"></script>
	<script type="text/javascript">
	<%
		String costType=request.getParameter("costType");
		ActionContext.getContext().put("costType",costType);
	%>
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		var myId = null,params = {};
		function $oneditfunc(rowid){
			myId = rowid;
			params={};
			//左右键
			enterKeyToNext("list",rowid,function(){
			});
			if(rowid==0){
				$('#'+rowid+'_occurringMonthStr','#list').val("<%=DateUtil.formateDateStr(new Date(),"yyyy-MM")%>");
			}
			$('#'+rowid+'_occurringMonthStr','#list')
				.datepicker({changeYear:'true',changeMonth:'true',dateFormat:'yy-mm'})
				.blur();
			$('#'+rowid+'_name','#list').attr("readonly","readonly")
			.click(selectComposing);
			$("#" + rowid + "_levelTwoName").change(function(){
				levelTwoNameChange(rowid);
			});
		}
		function $processRowData(data){
			for(var pro in params){
				data[pro] = params[pro];
			}
			var costType=$("#costType").val();
			data['costType']=costType;
			return data;
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="cost_collection_save">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		//选择质量成本
		function selectComposing(){
			var url = '${costctx}/common/composing-select.htm';
	 		$.colorbox({href:url,iframe:true, innerWidth:800, innerHeight:400,
	 			overlayClose:false,
	 			title:"选择质量成本"
	 		});
		}
		function setFullComposingValue(datas){
			$('#'+myId+'_name','#list').val(datas[0].name)
			.closest("td").prev().html(datas[0].code);
			params['code'] = datas[0].code;
			params['levelTwoCode'] = datas[0].levelTwoCode;
			params['levelTwoName'] = datas[0].levelTwoName;
		}
		function onbindClick(){
			var sbtitle1=document.getElementById("search_box");
			if(sbtitle1.style.display=="block"){
				$("#condition_0").datepicker({changeYear:'true',changeMonth:'true',dateFormat:'yy-mm'})
				.blur()
			};
		}
		function exportDatas(){
			var costType=$("#costType").val();
			var listCode=$("#listCode").val();
			var listName=$("#listName").val();
			iMatrix.export_Data('${costctx}/collection/export.htm?costType='+costType+"&listName="+listName+"&listCode="+listCode);
		}
		var selRowId = null;
		function levelTwoNameChange(rowid){
			selRowId=rowid;	
			var levelTwo=$("#"+selRowId+"_levelTwoName").val();
			var url = "${costctx}/common/select-by-level-two.htm?levelTwo="+levelTwo;
			$.post(encodeURI(url),{},function(result){
	 			if(result.error){
	 				alert(result.message);
	 			}else{
 					var leavlThrees = result.leavlThrees;
 					var leavlThreeArr = leavlThrees.split(",");
					var leavlThree = document.getElementById(selRowId+"_levelThreeName");
					leavlThree.options.length=0;
					var opp1 = new Option("","");
					leavlThree.add(opp1);
	 				for(var i=0;i<leavlThreeArr.length;i++){
	 					var opp = new Option(leavlThreeArr[i],leavlThreeArr[i]);
	 					leavlThree.add(opp);
	 				}
	 			}
	 		},'json');
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="collection";
		var thirdMenu="_quality_failure";
	</script>
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	 <div id="secNav">
		<%@ include file="/menus/cost-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/cost-collection-menu.jsp" %>
	</div> 
	
	<div class="ui-layout-center">
		<div class="opt-body">
		<aa:zone name="main">
			<div class="opt-btn">
				<table cellpadding="0" cellspacing="0" width="100%">
					<tr>
						<td valign="middle" style="padding: 0px;width:50%; margin: 0px;" id="btnTd">
							<security:authorize ifAnyGranted="cost_collection_save">
							<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
							</security:authorize>
							<security:authorize ifAnyGranted="cost_collection_delete">
							<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
							</security:authorize>
							<button class="btn" onclick="iMatrix.showSearchDIV(this);onbindClick();"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
							<security:authorize ifAnyGranted="cost_collection_export">
							<button class="btn" onclick="exportDatas();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
							</security:authorize>
							<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
						</td>
					</tr>
				</table>
			</div>
			<div id="opt-content">
				<form id="contentForm" name="contentForm" method="post" action="">
					<input type="hidden" name="listCode" id="listCode" value="COST_QUALITY_FAILURE"></input>
					<input type="hidden" name="listName" id="listName" value="品质故障数据"></input>
					<input type="hidden" name="costType" id="costType" value="${costType}"></input>
					<grid:jqGrid gridId="list" url="${costctx}/collection/quality-failure-list-datas.htm?costType=${costType}" code="COST_QUALITY_FAILURE"></grid:jqGrid>
				</form>
			</div>
		</aa:zone>
		</div>
	</div>
</body>
</html>