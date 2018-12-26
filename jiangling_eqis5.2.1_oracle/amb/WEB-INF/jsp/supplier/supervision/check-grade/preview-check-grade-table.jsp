<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<table class="form-table-without-border" id="appraisal-table"	style="width:100%;">
<caption style="height: 27px;text-align: center"><h2>供应商检查评分表</h2></caption>
		<tr height=20>
			<td colspan="8" style="text-align: right">编号:JC-2011-10-007&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
		<tr height=29>
			<td style="width:70px;">供应商名称</td>
			<td colspan="7">
				<input style="width:270px;border:0px;border-bottom:1px solid;"></input>
			</td>
		</tr>
		<tr height=29>
			<td>机型</td>
			<td colspan="7">
				<input style="width:90px;border:0px;border-bottom:1px solid;"></input>
				&nbsp;&nbsp;监察时间&nbsp;&nbsp;
				<input style="width:90px;border:0px;border-bottom:1px solid;"></input>
				零件名&nbsp;&nbsp;
				<input style="width:90px;border:0px;border-bottom:1px solid;"></input>
				零件号&nbsp;&nbsp;
				<input style="width:90px;border:0px;border-bottom:1px solid;"></input>
			</td>
		</tr>
		<tr height=29>
			<td>审核理由</td>
			<td colspan="7">
				<s:iterator value="auditReasons" id="option">
					<input  type="checkbox"/>${option.name}&nbsp;&nbsp;
				</s:iterator>
		</tr>
</table>
<table class="form-table-border-left" id="inspection-table"	style="width:100%;margin-top:4px;">
	<thead>
		<tr style="background:#99CCFF;">
			<th colspan="2" width="10%" style="text-align:center">项目</th>
			<th width="5%">序号</th>
			<th width="20%">确认事项</th>
			<th width="5%">权重</th>
			<th width="5%">上次得分</th>
			<th width="20%">问题</th>
			<th width="5%">本次得分</th>
			<th width="20%">问题</th>
			<th width="10%">备注</th>
		</tr>
	</thead>
	<tbody>
		${checkGradeTableStr}
	</tbody>
</table>