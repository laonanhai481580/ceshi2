<%@page import="com.ambition.gsm.entity.GsmInspectionRecord"%>
<%@page import="com.ambition.gsm.entity.InspectionRecordAttach"%>
<%@page import="com.opensymphony.xwork2.util.ValueStack"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
	<title><s:text name='gsm.title-3'/></title>
	<%@include file="/common/meta.jsp" %>
	<script type="text/javascript">
	function contentResize(){
		$("#opt-content").height($(".ui-layout-center").height()-55);
	}
	$(document).ready(function(){
		contentResize();
	});
	function updateInput(){
		var id=$("#id").val();
		if($.isFunction(window.parent.updateInput)){
			window.parent.updateInput(id);
		}
	}
	</script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();" >
	<input type="hidden" id="id" value="${id}"/>
	<script type="text/javascript">
		var secMenu="inspectionPlanReport";
		var thirdMenu="myInspectionPlanInput";
	</script>
	<div class="ui-layout-center" style="overflow-y:auto;">
		<div class="opt-body" >
			<div class="opt-btn">
				<button class='btn' onclick="window.parent.$.colorbox.close();"><span><span><b class="btn-icons btn-icons-cancel"></b><s:text name='取消'/></span></span></button>
				<button class='btn' onclick="updateInput();"><span><span><b class="btn-icons btn-icons-edit"></b><s:text name='编辑'/></span></span></button>
			</div>
			<div style="display: none;" id="message"><font class=onSuccess><nobr><s:text name='删除成功'/></nobr></font></div>
			<div id="opt-content" style="text-align: center;">
				<form action="" method="post" id="measurementForm" name="measurementForm">
					<input type="hidden" name="params.savetype" value="input"/>
					<table class="form-table-border-left" style="width:100%;margin: auto;">
						<caption style="height: 50px"><h2><s:text name='检定记录'/></h2></caption>
						<tr>
							<td >公司主体</td>
							<td>${equipment.companyMain}</td>
							<td >制程区段</td>
							<td>${equipment.processSection}</td>
							<td >使用部门</td>
							<td>${equipment.devName}</td>
						</tr>
						<tr>
							<td >工序</td>
							<td>${equipment.workProducre}</td>
							<td >安装地点</td>
							<td>${equipment.address}</td>
							<td >责任人</td>
							<td>${equipment.dutyMan}</td>
						</tr>
						<tr>
							<td >抄送</td>
							<td>${equipment.copyMan}</td>
							<td >固定资产编号</td>
							<td>${equipment.fixedAssets}</td>
							<td >管理编号</td>
							<td>${equipment.managerAssets}</td>
						</tr>
						
						<tr>
							<td >设备名称</td>
							<td>${equipment.equipmentName}</td>
							<td >规格型号</td>
							<td>${equipment.equipmentModel}</td>
							<td >测量范围</td>
							<td>${equipment.measuringRange}</td>	
						</tr>				
						<tr>
							<td >精度/分度</td>
							<td>${equipment.accuracy}</td>
							<td >制造商</td>
							<td>${equipment.manufacturer}</td>
							<td >机身号</td>
							<td>${equipment.factoryNumber}</td>	
						</tr>
						<tr>
							<td >校准方式</td>
							<td>${equipment.checkMethod}</td>
							<td >分级</td>
							<td>${equipment.equipmentLevel}</td>
							<td >频率（月）</td>
							<td>${equipment.frequency}</td>	
						</tr>
						<tr>
							<td colspan="6">
								<table class="form-table-border-left" id="list4"	style="width:100%">
									<tr>
										<td style="width:10%"><s:text name='使用人'/></td>
										<td style="width:15%"><s:text name='计划日期'/></td>
										<td style="width:15%"><s:text name='实际日期'/></td>
										<td style="width:10%"><s:text name='校验状态'/></td>
										<td style="width:10%"><s:text name='结果'/></td>
										<td style="width:10%"><s:text name='鉴定员'/></td>
										<td style="width:15%"><s:text name='附件'/></td>
										<td style="width:15%"><s:text name='是否为计划检定'/></td>
									</tr>
									<s:iterator value="inspectionRecords" id="inspectionRecord" var="inspectionRecord">
										<%
											ValueStack valueStack = (ValueStack)request.getAttribute("struts.valueStack");
											GsmInspectionRecord inspectionRecord = (GsmInspectionRecord)valueStack.findValue("inspectionRecord");
										%>
										<%if(inspectionRecord.getIsPlan()) {%>
										<tr>
											<input type="hidden" style="width:100%" name="params.id" value=<%=inspectionRecord.getId() %> ></input>
											<td style="width:10%">${inspectionRecord.user}</td>
											<td style="width:15%"><input style="border:0px;background: #ECF7FB;width:100%;" name="params.planDate" readonly="readonly" class="line" value=${inspectionRecord.planDate}></input></td>
											<td style="width:15%"><input style="border:0px;background: #ECF7FB;width:100%;"  id="datepicker4" name="params.actualDate"  class="line" value=${inspectionRecord.actualDate} ></input></td>
											<td style="width:10%">
											${inspectionRecord.checkState}
											</td>
											<td style="width:10%">${inspectionRecord.resultState}</td>
											<td style="width:10%">${inspectionRecord.surveyor}</td>
											<td style="width:15%">
												<div id="<%=inspectionRecord.getId() %>_attachs">
												<%
												List<InspectionRecordAttach> attachlist=inspectionRecord.getInspectionRecordAttachs();
												for(int i=0;i<attachlist.size();i++){
													InspectionRecordAttach inspectionRecordAttach=(InspectionRecordAttach)attachlist.get(i);
													out.write("<a href='");
												%>
													${gsmctx}
													<%
													out.write("/inspectionplan/exupload.htm?id="+inspectionRecordAttach.getId()+"'>"+inspectionRecordAttach.getAttachname()+"</a>&nbsp;&nbsp;&nbsp;");
													%>
												<%} %>
												</div>
											</td>
											<td style="width:10%"><%=inspectionRecord.getIsPlan()==true?"是":"否" %></td>
										</tr>
										<%}else{ %>
										<tr>
											<input type="hidden" style="width:100%" name="params.simpleid" value=<%=inspectionRecord.getId() %> ></input>
											<td style="width:10%">${inspectionRecord.user}</td>
											<td style="width:15%"></td>
											<td style="width:15%"><input style="border:0px;background: #ECF7FB;width:100%;"  id="datepicker4" name="params.simpleactualDate"  class="line" value=${inspectionRecord.actualDate} ></input></td>
											<td style="width:10%">
											${inspectionRecord.checkState}
											</td>
											<td style="width:10%">${inspectionRecord.resultState}</td>
											<td style="width:10%">${inspectionRecord.surveyor}</td>
											<td style="width:15%">
												<div id="<%=inspectionRecord.getId() %>_attachs">
												<%
												List<InspectionRecordAttach> attachlist=inspectionRecord.getInspectionRecordAttachs();
												for(int i=0;i<attachlist.size();i++){
													InspectionRecordAttach inspectionRecordAttach=(InspectionRecordAttach)attachlist.get(i);
													out.write("<a href='");
												%>
													${gsmctx}
													<%
													out.write("/inspectionplan/exupload.htm?id="+inspectionRecordAttach.getId()+"'>"+inspectionRecordAttach.getAttachname()+"</a>&nbsp;&nbsp;&nbsp;");
													%>
												<%}%>
												</div>
											</td>
											<td style="width:10%"><%=inspectionRecord.getIsPlan()==true?"是":"否" %></td>
										</tr>
										<%}%>
									</s:iterator>
								</table>
							</td>
						</tr>
					</table>
				</form>
			</div>
		</div>	
	</div>
</body>
</html>