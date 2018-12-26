<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
    <script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${ctx}/js/CodeCombobox.js"></script>
	<script type="text/javascript" src="${ctx}/js/CodeMultiCombobox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/timepicker/timepicker-all-1.0.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">	
		//不良代码列表
		$(function(){
			$("#customCombobox").CodeCombobox({
				value : {id:1,code:'asd',name:'add'}
			});
			$("#customCombobox1").CodeMultiCombobox({
				value : [{id:1,code:'asd',name:'add'}]
			});
		});
		function $successfunc(response){
			var result = eval("(" + response.responseText	+ ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function click(cellvalue, options, rowObject){	
			if(rowObject.id){
				return "<div style='text-align:center;'><a href='javascript:void(0);' onclick='creatInput("+rowObject.id+");'>添加不良代码</a>&nbsp;"+"|"+"&nbsp;"
		              +"<a href='javascript:void(0)' onClick='deleteSubs("+rowObject.id+")'>删除下级不良代码</a></div>";
			}else{
				return "";
			}
		}
		function creatInput(id){
			if(id){
				window.open('${mfgctx}/base-info/defection-code/list.htm?defectionTypeId='+id);
			}		
		}
		function delMyRow(rowId) {
			if(editing){
				alert("请先完成编辑！");
				return;
			}
			var ids = jQuery("#defectionTypeList").getGridParam('selarrrow');
			if(ids.length < 1){
				alert("请选中需要删除的记录！");
				return;
			}
			if(ids.length > 1){
				alert("记录可能含有不良代码，请逐条删除！");
				return;
			}
			if(confirm("确定要删除所选中的记录？")){
				var ret = $("#defectionTypeList").jqGrid('getRowData',ids);
				$.post("${mfgctx}/base-info/defection-code/search-subs.htm",{defectionTypeId : ret.id},function(result){
					if(result == "have"){
						alert("还有不良代码不能删除，请先删除其下不良代码！");
						return;
					}else{
						$.post("${mfgctx}/base-info/defection-code/defection-type-delete.htm", {
							deleteIds : ids.join(',')
						}, function(data) {
							//ids数组的长度是会自动变小的(实际是jqgrid内部的一个数组)
							while (ids.length>0) {
								jQuery("#defectionTypeList").jqGrid('delRowData', ids[0]);
							}
						});
					}
				});
			}			
		}
		function deleteSubs(rowId){
			var ret = jQuery("#defectionTypeList").jqGrid('getRowData',rowId);
			if(confirm("确定要删除"+ret.defectionTypeName+"下所有的不良代码吗？")){
				$.post("${mfgctx}/base-info/defection-code/delete-subs.htm", {defectionTypeId : ret.id}, function(){});
			};
		}
		
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_DEFECTION_TYPE_SAVE">
			  isRight =  true;
			</security:authorize>
			return isRight;
		}
		//重写(单行保存前处理行数据)
		function $processRowData(data){
			data.businessUnit = $("#businessUnit").val();
			return data;
		}
		function selectBusinessUnit(obj){
			window.location.href = encodeURI('${mfgctx}/base-info/defection-code/defection-type-list.htm?businessUnit='+ obj.value);
		}
		function $addGridOption(jqGridOption){
			jqGridOption.postData.businessUnit=$("#businessUnit").val();
		}
		//导入
		function imports(){
			var url = '${mfgctx}/base-info/defection-code/imports.htm';
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入",
				onClosed:function(){
					$("#defectionTypeList").trigger("reloadGrid");
				}
			});
		}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="defectionCode";
		var treeMenu="defection";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-base-info-menu.jsp" %>
	</div>
	
	<div class="ui-layout-center">
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				<security:authorize ifAnyGranted="MFG_DEFECTION_TYPE_SAVE">
				<button class='btn' onclick="iMatrix.addRow();" type="button"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
				</security:authorize>
				<security:authorize ifAnyGranted="MFG_DEFECTION_TYPE_DELETE">
				<button class='btn' onclick="delMyRow();" type="button"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
				</security:authorize>
				<button  class='btn' onclick="iMatrix.showSearchDIV(this);" type="button"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
				<security:authorize ifAnyGranted="MFG_DEFECTION_TYPE_EXPORT">
				<button  class='btn' onclick="iMatrix.export_Data('${mfgctx}/base-info/defection-code/exportCode.htm');" type="button"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>		
				</security:authorize>
					<!-- <button class='btn' onclick="imports();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>	 -->
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="opt-content">
					<form id="contentForm" method="post" action="" >
						<div style="margin-left:4px;line-height:30px;">
						厂区:<s:select list="businessUnits" 
										theme="simple"
										listKey="value" 
										listValue="name" 
										id="businessUnit"
										name="businessUnit"
										onchange="selectBusinessUnit(this)"
										cssStyle="width:100px"
										emptyOption="false"
										labelSeparator="">
									</s:select>
									
						</div>
						<grid:jqGrid gridId="defectionTypeList" url="${mfgctx}/base-info/defection-code/defection-type-list-datas.htm" code="MFG_DEFECTION_TYPE"></grid:jqGrid>	
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>