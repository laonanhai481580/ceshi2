<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="contentleadTable" id="portal_supplier_message_div">
	<table class="leadTable">
		<thead>
			<tr>
				<th >检验报告编号</th>
				<th >工序</th>
				<th >机种</th>
				<th >检验项目</th>
				<th >不良率</th>
			</tr>
		</thead>
		<tbody id="portal_supplier_message"> 
			<s:iterator value="datas" var="item">
				<tr>
					<td style="background-color:#FF4500;">
						<a target='_blank' href='${hostApp}/carmfg/inspection/made-inspection/input.htm?_from=portal&&id=${item.id}'>
							${item.inspectionNo}
						</a>
					</td>
					<td >
						${item.workProcedure}
					</td>
					<td >
						${item.machineNo}
					</td>
					<td >
						${item.checkItemName}
					</td>
					<td >
						${item.checkItemRate}
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>