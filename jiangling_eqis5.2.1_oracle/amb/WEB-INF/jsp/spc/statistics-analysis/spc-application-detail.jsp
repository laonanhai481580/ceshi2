<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title>安必兴—企业管理效率促进专家</title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
		var params = ${params};
		var postData = {};
		$(document).ready(function(){
			for(var pro in params){
				postData['params.' + pro] = params[pro];
			}
		});
		//设置默认 参数
		function $addGridOption(jqGridOption){
			jqGridOption.postData = postData;
		}
		//选择异常通知人员
		function selectPerson(){
			/**if(!_ambWorkflowFormObj.webBaseUrl){
			alert("workflow-ofrm.js _selectUser方法提示:初始化选择用户时项目地址未指定!");
			return;
			}*/
			var acsSystemUrl = '${ctx}';
			popZtree({
		        leaf: {
		            enable: false,
		            multiLeafJson:"[{'name':'用户树','type':'MAN_DEPARTMENT_TREE','hiddenValue':'{\"id\":\"id\",\"loginName\":\"loginName\"}','showValue':'{\"name\":\"name\"}'}]"
		        },
		        type: {
		            treeType: "MAN_DEPARTMENT_TREE",
		            showContent:"[{'id':'id','loginName':'loginName','name':'name'}]",
		            noDeparmentUser:true,
		            onlineVisible:false
		        },
		        data: {
		            treeNodeData:"id,loginName,name",
		            chkStyle:"checkbox",
		            chkboxType:"{'Y':'ps','N':'ps'}",
		            departmentShow:""
		        },
		        view: {
		            title: title,
		            width: 400,
		            height:400,
		            url:acsSystemUrl
		        },
		        feedback:{
		            enable: true,
		            showInput:'',
		            showThing:"{'name':'name'}",
		            hiddenInput:'',
		            hiddenThing:"{'id':'id'}",
		            append:false
		        },
		        callback: {
		            onClose:function(api){
		            	$("#personId").val(ztree.getIds());
		            	$("#personName").val(ztree.getNames());
		            	sendPerson(ztree.getIds());
		            }
		        }
		    });
		}
		function sendPerson(ids){
			var url = "${spcctx}/statistics-analysis/send-email.htm?userIds="+ids;
			$.post(url,params,function(result){
				if(result.error){
					alert(result.message);
				}else{
					$("#message").html("邮件发送成功！");
				}
			},'json');
		}
	</script>
</head>
<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<div class="ui-layout-center">
		
		<div class="opt-body">
			<aa:zone name="main">
				<div class="opt-btn">
				  <button  class='btn' onclick="selectPerson();" type="button"><span><span><b class="btn-icons btn-icons-audit"></b>邮件</span></span></button>
 				</div>
				<div id="opt-content">
				<table style="width:99.7%;">
					<tr>
						<td>
							<grid:jqGrid gridId="list" url="${spcctx}/statistics-analysis/spc-application-detail-datas.htm" code="SPC_QUALITY_FEATURE_DETAIL" pageName="page"></grid:jqGrid>
						</td>
					</tr>
				</table>
			</div>
			</aa:zone>
		</div>
	</div>
</body>
</html>