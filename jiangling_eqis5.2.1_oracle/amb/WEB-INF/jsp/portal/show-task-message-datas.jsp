<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<link rel="stylesheet" href="${hostResources}/css/defaultCss" type="text/css">
<div class="contentleadTable" id="portal_task_message_div">
	<table class="leadTable">
		<thead>
			<tr>
				<th >任务名称</th>
<!-- 				<th >发布人</th> -->
				<th >创建时间</th>
			</tr>
		</thead>
		<tbody id="portal_task_message"> 
			<s:iterator value="taskDatas" var="item">
				<tr>
					<td>
						<a target='_blank' href="#" onclick="popWindow(this,'${hostImatrix}/task/task/task!input.htm?id=${item.id}', 'task');" >
							${item.title}
						</a>
					</td>
<!-- 					<td> -->
<%-- 						${item.publisher} --%>
<!-- 					</td> -->
					<td style="width:25%">
						${item.createdTime}
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>