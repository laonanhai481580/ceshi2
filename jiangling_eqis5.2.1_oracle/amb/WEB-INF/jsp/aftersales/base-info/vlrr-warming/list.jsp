<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script src="${resourcesCtx}/widgets/validation/validate-all-1.0.js" type="text/javascript"></script>
	<script src="${resourcesCtx}/widgets/validation/dynamic.validate.js" type="text/javascript"></script>
	<script type="text/javascript" src="${resourcesCtx}/widgets/colorbox/jquery.colorbox.js"></script>
	<script type="text/javascript" src="${resourcesCtx}/js/search.js"></script>
	<script type="text/javascript">
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="AFS_VLRR_WARMING_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}	
		
		//选择提醒人员
		var selRowId = null;
		function warmingManClick(obj){	
			selRowId=obj.rowid;
			var acsSystemUrl = "${ctx}";
			popTree({ title :'选择提醒人员',
				innerWidth:'400',
				treeType:'MAN_DEPARTMENT_TREE',
				defaultTreeValue:'loginName',
				leafPage:'false',
				multiple:'false',
				hiddenInputId:'personId',
				showInputId:'personName',
				acsSystemUrl:acsSystemUrl,
				callBack:function(){
					setWarmingMan();
				}
			});			
		}
		
		function setWarmingMan(){
			var warmingMan=$("#personName").val();
			$("#"+selRowId+"_warmingMan").val(warmingMan);
			var warmingManLogin=$("#personId").val();
			$("#"+selRowId+"_warmingManLogin").val(warmingManLogin);
		}
		
		function customerModelClick(obj){
			selRowId=obj.rowid;	
			modelClick();
		}
		function ofilmModelClick(obj){
			selRowId=obj.rowid;	
			modelClick();
		}
	 	function modelClick(){
			var customerName=$("#"+selRowId+"_customerName").val();//customerName
	 		var url = '${aftersalesctx}/base-info/customer/model-select.htm?customerName='+customerName;
	 		$.colorbox({href:url,iframe:true, 
	 			innerWidth:700, 
				innerHeight:500,
	 			overlayClose:false,
	 			title:"选择机型"
	 		});
	 	}
		//选择之后的方法 data格式{key:'a',value:'a'}
	 	function setProblemValue(datas){
	 		$("#"+selRowId+"_customerModel").val(datas[0].value);
	 		$("#"+selRowId+"_ofilmModel").val(datas[0].key);//model
	 	}
	 	
		//导入台账数据
		function importDatas(){
	 		var businessUnit=$("#businessUnit").val();
			var url = encodeURI('${aftersalesctx}/base-info/vlrr-warming/import.htm?businessUnit='+businessUnit);
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#inspectionList").trigger("reloadGrid");
				}
			});
		}
		function downloadTemplate(){
			window.location = '${aftersalesctx}/base-info/vlrr-warming/download-template.htm';
		}
	 	
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="baseInfo";
		var thirdMenu="vlrr_warming";
		var treeMenu = "_vlrr_warming";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/aftersales-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/aftersales-baseinfo-third-menu.jsp"%>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="AFS_VLRR_WARMING_SAVE">
						<button class="btn" onclick="iMatrix.addRow();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="AFS_VLRR_WARMING_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>
					<security:authorize ifAnyGranted="AFS_VLRR_WARMING_EXPORT">
						<button class="btn" onclick="iMatrix.export_Data('${aftersalesctx}/base-info/vlrr-warming/export.htm');"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>
			<security:authorize ifAnyGranted="AFS_VLRR_WARMING_IMPORT">
				<button class='btn' onclick="importDatas();" type="button"><span><span><b class="btn-icons btn-icons-import"></b>导入</span></span></button>
				<button class="btn" onclick="downloadTemplate();"><span><span><b class="btn-icons btn-icons-download"></b>下载导入模板</span></span></button>
			</security:authorize>	
				<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<input type="hidden" name="personName"  id="personName" value=""/>
				<input type="hidden" name="personId"  id="personId" value=""/>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${aftersalesctx}/base-info/vlrr-warming/list-datas.htm" submitForm="defaultForm" code="AFS_VLRR_WARMING" ></grid:jqGrid>
					</form>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>