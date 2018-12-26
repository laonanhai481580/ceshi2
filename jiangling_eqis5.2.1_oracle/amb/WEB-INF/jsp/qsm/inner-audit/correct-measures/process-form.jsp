<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page
	import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="opt-content" style="overflow-y: auto; padding: 0px">
	<input type="hidden" name="operateName" id="operateName" value=""></input>
	<input type="hidden" name="paramsStr" id="paramsStr"></input> <input
		type="hidden" name="taskTransact" id="taskTransact" />
	<s:if test="task.active!=2">
		<table class="form-table-border-left" id="opinion-table"
			style="width: 100%; margin: auto;">
			<tr height=28>
				<td style="background: #99CCFF; font-weight: bold; font-size: 14px;">
					我的意见</td>
			</tr>
			<tr height=28>
				<td><textarea rows="6" style="width: 99.6%;" name="opinion"></textarea>
				</td>
			</tr>
		</table>
	</s:if>
	<%
		List<Opinion> opinionParameters = (List<Opinion>)request.getAttribute("opinionParameters");
		if(opinionParameters==null){
			opinionParameters = new ArrayList<Opinion>();
		}
	%>
	<table class="form-table-border-left" id="history-table"
		style="width: 100%; margin: auto;">
		<tr height=28>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; width: 50px; text-align: center">
				序号</td>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; width: 140px; text-align: center">
				时间</td>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; width: 100px; text-align: center">
				办理人</td>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; width: 90px; text-align: center">
				任务</td>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; text-align: center">
				意见</td>
			<td
				style="background: #99CCFF; font-weight: bold; font-size: 14px; width: 100px; text-align: center">
				结果</td>
		</tr>
		<%
			int index = 1;
			for(Opinion param : opinionParameters){
		%>
		<tr height=24>
			<td style="text-align: center"><%=index++ %></td>
			<td><%=DateUtil.formateTimeStr(param.getCreatedTime()) %></td>
			<td><%=param.getTransactorName() %></td>
			<td><%=param.getTaskName() %></td>
			<td><%=param.getOpinion()==null?"":param.getOpinion() %></td>
			<td><%=param.getCustomField()==null?"":param.getCustomField() %>
			</td>
		</tr>
		<%} %>
	</table>
</div>
