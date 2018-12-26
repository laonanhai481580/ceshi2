<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<div class="contentleadTable" id="portal_supplier_message_div">
	<table class="leadTable">
		<thead>
			<tr>
				<th >检验报告编号</th>
				<th >物料编号</th>
				<th >物料名称</th>
				<th >检验项目</th>
				<th >Lot.No</th>
				<th >来料数</th>
				<th >供应商</th>
				<th >来料日期</th>
				<th >不良率</th>
			</tr>
		</thead>
		<tbody id="portal_supplier_message"> 
			<s:iterator value="datas" var="item">
				<tr>
					<td style="background-color:#FF4500;">
						<a target='_blank' href='${hostApp}/iqc/inspection-report/input.htm?_from=portal&&id=${item.id}'>
							${item.inspectionNo}
						</a>
					</td>
					<td>
						${item.checkBomCode}
					</td>
					<td style="width:25%">
						${item.checkBomName}
					</td>
					<td style="width:25%">
						${item.checkItemName}
					</td>
					<td style="width:25%">
						${item.lotNo}
					</td>
					<td style="width:25%">
						${item.stockAmount}
					</td>
					<td style="width:25%">
						${item.supplierName}
					</td>
					<td style="width:25%">
						${item.enterDate}
					</td>
					<td style="width:25%">
						${item.checkItemRate}
					</td>
				</tr>
			</s:iterator>
		</tbody>
	</table>
</div>