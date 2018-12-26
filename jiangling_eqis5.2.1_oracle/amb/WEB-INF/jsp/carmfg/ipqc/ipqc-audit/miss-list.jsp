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
	<script type="text/javascript" src="${resourcesCtx}/js/staff-tree.js"></script>
	<script type="text/javascript">
		<% String loginName= ContextUtils.getUserName();
			request.setAttribute("loginName", loginName);
		
		%>
		function showPicture(value,options,obj){
			var strs = "";
			strs = "<div style='width:100%;' title='上传附件' ><a class=\"small-button-bg\" onclick=\"attachmentFilesClick('"+obj.id+"');\" href=\"#\"><input type='hidden' id='"+obj.id+"_hiddenFiles' value='"+value+"'/><span id='"+obj.id+"_uploadBtn' class='ui-icon ui-icon-image uploadBtn' style='cursor:pointer;display:none;'></span></a><span style='text-align:left;' id='"+obj.id+"_showFiles'>"+$.getDownloadHtml(value)+"</span><div>";
			return strs;
		}
		var params = {};
		function attachmentFilesClick(rowId){
			//上传附件 
			$.upload({   
				showInputId : rowId + "_showFiles",
				hiddenInputId : rowId + "_hiddenFiles",
				title:"上传附件",
				callback:function(files){
					params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
				}
			}); 
		}
		function addNew(){
			iMatrix.addRow();
			$("#undefined_uploadBtn").show();
			var loginName=$("#loginName").val();
			$("#0_auditMan").val(loginName);
		}
		
		function $oneditfunc(rowId){
			selRowId = rowId;
			params.attachmentFiles = $("#" + rowId + "_hiddenFiles").val();
			params.hisAttachmentFiles = params.attachmentFiles;
			$(".uploadBtn").hide();
			$("#undefined_uploadBtn").show();
			$("#" + rowId + "_uploadBtn").show();
			var loginName=$("#loginName").val();
			$("#0_auditMan").val(loginName);
			$("#" + rowId + "_factory").change(function(){
				factoryChange(rowId);
			}); 
		}
		function $successfunc(response){
			var result = eval("(" + response.responseText + ")");
			if(result.error){
				alert(result.message);
				return false;
			}else{
				return true;
			}
		}
		function $afterrestorefunc(rowId){
			$("#" + rowId + "_uploadBtn").hide();
		}
		function $beforeEditRow(rowId,iRow,iCol,e){
			var isRight = false;
			<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_SAVE">
				isRight =  true;
			</security:authorize>
			return isRight;
		}			
		/*---------------------------------------------------------
		函数名称:showIdentifiersDiv
		参          数:
		功          能:标识为（下拉选）
		------------------------------------------------------------*/
		function showIdentifiersDiv(){
			if($("#flag").css("display")=='none'){
				removeSearchBox();
				$("#flag").show();
				var position = $("#_task_button").position();
				$("#flag").css("left",position.left+15);
				$("#flag").css("top",position.top+28);
			}else{
				$("#flag").hide();
			}
		}
		var identifiersDiv;
		function hideIdentifiersDiv(){
			identifiersDiv = setTimeout('$("#flag").hide()',300);
		}
		
		function show_moveiIdentifiersDiv(){
			clearTimeout(identifiersDiv);
		}
		//保存邮件设置时获取的参数
		function getIndicators(params){			
			params.ids = $("#list").jqGrid("getGridParam","selarrrow").join(",");
		}	
		function improve(){
			var rowIds = $("#list").jqGrid("getGridParam","selarrrow");
			var auditId=rowIds[0];
			if(rowIds.length==0){
			alert("请选择需要改善的数据!");
			return false;
			}
			if(rowIds.length>1){
			alert("只能选择一条数据发起改善!");
			return false;
			}
			window.location="${mfgctx}/ipqc/ipqc-improve/input.htm?auditId="+auditId;
		}
	
		//选择提醒人员
		function addPerson(){			
			var rowIds = $("#list").jqGrid("getGridParam","selarrrow");
			if(rowIds.length==0){
				alert("请选择需要邮件通知的数据!");
				return false;
			}						
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
					submitForm();
				}
			});			
		}
		
		function submitForm(){
			var url = "${mfgctx}/ipqc/ipqc-audit/mail-settings.htm?";
			if($("#contentForm").valid()){
				var params = {};				
				var rows1 = $("#personId").val().split(",");	
				var rows2 = $("#personName").val().split(",");
				var paras = new Array();
				if(rows1.length<1){
					alert("提醒人员不能为空！");
					return;
				}
				for(var i=0;i<rows1.length;i++){
				    var row1 = rows1[i];
				    var row2 = rows2[i];
				    paras.push('{"userId":"'+row1+'","userName":"'+row2+'"}');
				}
				params.personStrs = "[" + paras.toString() + "]";				
				
				
				getIndicators(params);
				$(".opt-btn .btn").attr("disabled","disabled");
				$("#message").html("正在保存配置,请稍候... ...");
				$.post(url,params,function(result){
					$(".opt-btn .btn").attr("disabled","");
					$("#message").html(result.message);
					if(result.error){
						alert(result.message);
					}else{
						$.colorbox.close();
					}
				},'json');
			}
		}
		function downloadTemplate(){
			window.location = '${mfgctx}/ipqc/ipqc-audit/download-ipqc-audit.htm';
		}		
		//导入台账数据
		function importDatas(){
			var url = encodeURI('${mfgctx}/ipqc/ipqc-audit/import.htm');
			$.colorbox({href:url,iframe:true, innerWidth:350, innerHeight:200,
				overlayClose:false,
				title:"导入台账数据",
				onClosed:function(){
					$("#list").trigger("reloadGrid");
				}
			});
		}	
 		function exportExcell(){			
			var url='${mfgctx}/ipqc/ipqc-audit/export-miss.htm';
			iMatrix.export_Data(url);
		} 	
 		function factoryChange(rowid){
 			selRowId=rowid;	
 			var factory=$("#"+selRowId+"_factory").val();
 			var url = "${mfgctx}/base-info/factory-procedure/procedure-select.htm?factory="+factory;
 			$.post(encodeURI(url),{},function(result){
 	 			if(result.error){
 	 				alert(result.message);
 	 			}else{
 					var procedures = result.procedures;
 					var procedureArr = procedures.split(",");
 					var procedure = document.getElementById(selRowId+"_station");
 					procedure.options.length=0;
 					var opp1 = new Option("","");
 					procedure.add(opp1);
 	 				for(var i=0;i<procedureArr.length;i++){
 	 					var opp = new Option(procedureArr[i],procedureArr[i]);
 	 					procedure.add(opp);
 	 				}
 	 			}
 	 		},'json');
 		}			
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<script type="text/javascript">
		var secMenu="ipqc_list";
		var thirdMenu="_IPQC_AUDIT_MISS";
	</script>
	
	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp" %>
	</div>
	
	<div id="secNav">
		<%@ include file="/menus/manufacture-sec-menu.jsp" %>
	</div>
	
	<div class="ui-layout-west">
		<%@ include file="/menus/manufacture-ipqc-menu.jsp" %>
	</div>
	<div class="ui-layout-center">
		<div class="opt-body">
			<form id="defaultForm" name="defaultForm" method="post" action=""></form>
			<aa:zone name="main">
				<div class="opt-btn">
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_SAVE">
						<button class="btn" onclick="addNew();"><span><span><b class="btn-icons btn-icons-add"></b>新建</span></span></button>
					</security:authorize>
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_DELETE">
						<button class="btn" onclick="iMatrix.delRow();"><span><span><b class="btn-icons btn-icons-delete"></b>删除</span></span></button>
					</security:authorize>
					<button id="searchBtn" class='btn' onclick="iMatrix.showSearchDIV(this);"><span><span><b class="btn-icons btn-icons-search"></b>查询</span></span></button>	
					<security:authorize ifAnyGranted="MFG_IPQC_AUDIT_EXPORT">
						<button class="btn" onclick="exportExcell();"><span><span><b class="btn-icons btn-icons-export"></b>导出</span></span></button>
					</security:authorize>												
					<input type="hidden" name="loginName" id="loginName" value="<%=loginName%>"/>
					<span style="color:red;font-size:18px;" >* <span style="font-family:verdana;color:red;font-size:10px;">双击可编辑,Enter(回车)可保存.</span></span>
				</div>
				<div id="message"><s:actionmessage theme="mytheme" /></div>	
				<script type="text/javascript">setTimeout("$('#message').hide('show');",3000);</script>
				<div id="opt-content" >
					<form id="contentForm" name="contentForm" method="post"  action="">
						<grid:jqGrid gridId="list" url="${mfgctx}/ipqc/ipqc-audit/miss-list-datas.htm" submitForm="defaultForm" code="MFG_IPQC_AUDIT_MISS" ></grid:jqGrid>
					</form>
					<table style="width:100%;margin-top:8px;" cellpadding="0" cellspacing="0" id="delayRemindTable">
						<tr>
							<td >
								<input id="personId" name="personId" type="hidden"/>
								<input id="personName" name="personName" type="hidden"/>
							</td>
						</tr>
					</table>
				</div>
			</aa:zone>
		</div>
	</div>
	
</body>
</html>