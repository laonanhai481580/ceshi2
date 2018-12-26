<%@page import="sun.java2d.pipe.SpanShapeRenderer.Simple"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%@ page import="com.norteksoft.product.util.ContextUtils"%>
<%@ page import="java.text.SimpleDateFormat"%>
<%
	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	Calendar calendar = Calendar.getInstance();
	String dateStr = sdf.format(calendar.getTime());
	String user = ContextUtils.getUserName();
%> 
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<caption style="font-size: 28px;padding-top: 10px;padding-bottom: 10px;">FAR解析跟踪录入表</caption>
	<tr>
		<th colspan="4" style="text-align:right;padding-bottom:4px;">
			编号:${formNo}
		</th>
	</tr>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table">
		<tr>
			<td style="width:160px;text-align:center;">发生日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="happenDate" name="happenDate" value="<s:date name='happenDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">确认日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="confirmDate" name="confirmDate" value="<s:date name='confirmDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">客户端工序</td>
			<td style="width:200px;">
				<s:select list="workingProcedures"
					listKey="value" 
					listValue="value" 
					name="workingProcedure" 
					id="workingProcedure" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">客户名称</td>
			<td style="width:200px;">
				<input  name="customerName" id="customerName" value="${customerName}"></input>
			</td>
			<td style="width:160px;text-align:center;">欧菲机型</td>
			<td style="width:200px;">
				<input  name="ofilmModel" id="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);"></input>
			</td>
			<td style="width:160px;text-align:center;">客户机型</td>
			<td style="width:200px;">
				<input  name="customerModel" id="customerModel" value="${customerModel}" onclick="modelClick(this);"></input>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">CS负责人</td>
			<td style="width:200px;">
				<input type="text" id="csMan" isTemp="true" isUser="true" hiddenInputId="csManLogin" style="float: left;"  name="csMan" value="${csMan}" />
				<input type="hidden" id="csManLogin" name="csManLogin" value="${csManLogin}" />
			</td>
			<td style="width:160px;text-align:center;">生产事业群</td>
			<td style="width:200px;">
				<%-- <input  name="productionEnterpriseGroup" id="productionEnterpriseGroup" value="${productionEnterpriseGroup}"></input> --%>
				<s:select list="productionEnterpriseGroups"
					listKey="value" 
					listValue="name" 
					name="productionEnterpriseGroup" 
					id="productionEnterpriseGroup" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">事业部</td>
			<td style="width:200px;">
				<s:select list="businessUnits"
					listKey="value" 
					listValue="name" 
					name="businessUnitCode" 
					id="businessUnitCode" 
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">不良现象&数量</td>
			<td colspan="5" style="padding: 0px; border-bottom: 0px;">
			<table class="form-table-border-left" style="border: 0px; width: 100%;">
				<thead>
					<tr>
						<td style="width: 5%; text-align: center; border-top: 0px; border-left: 0px;">操作</td>
						<td width="30%" style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">不良项目</td>
						<td width="30%" style="border-top: 0px; border-bottom: 0px; border-right: 0px;text-align: center;">数量</td>
					</tr>
				</thead>
				<tbody>
					<s:iterator value="_farAnalysisItems" id="item" var="item">
						<tr class="farAnalysisItems">
							<td style="text-align:center;">
								<a class="small-button-bg" addBtn="true"  onclick="addRowHtml(this)" href="#" title="添加">
								<span class="ui-icon ui-icon-plus" style='cursor: pointer;'></span></a> 
								<a class="small-button-bg" delBtn="true"  onclick="removeRowHtml(this)" href="#" title="删除">
								<span class="ui-icon ui-icon-minus" style='cursor: pointer;'></span></a>
							</td>
							<td style="text-align: center;"><input type="text" name="defectionItem" value="${defectionItem}" /></td>
							<td style="text-align: center;"><input type="text" name="defectionItemValue" value="${defectionItemValue}" class="{digits:true,messages:{digits:'必须是整数!'}}" /></td>
						</tr>
					</s:iterator>
				</tbody>
			</table>
		</td>
	</tr>
		<tr>
			<td style="width:160px;text-align:center;">快递公司</td>
			<td style="width:200px;">
				<input  name="courierCompany" id="courierCompany" value="${courierCompany}"></input>
			</td>
			<td style="width:160px;text-align:center;">快递单号</td>
			<td style="width:200px;">
				<input  name="courierNumber" id="courierNumber" value="${courierNumber}"></input>
			</td>
			<td style="width:160px;text-align:center;">寄件日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="sendDate" name="sendDate" value="<s:date name='sendDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">接受者</td>
			<td style="width:200px;">
				<input type="text" id="receiver" isTemp="true" isUser="true" hiddenInputId="receiverLogin" style="float: left;"  name="receiver" value="${receiver}" />
				<input type="hidden" id="receiverLogin" name="receiverLogin" value="${receiverLogin}" />
			</td>
			<td style="width:160px;text-align:center;">接收日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="receiptDate" name="receiptDate" value="<s:date name='receiptDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">样品转交接收人</td>
			<td style="width:200px;">
				<input type="text" id="transferMan" isTemp="true" isUser="true" hiddenInputId="transferManLogin" style="float: left;"  name="transferMan" value="${transferMan}" />
				<input type="hidden" id="transferManLogin" name="transferManLogin" value="${transferManLogin}" />
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">转交日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="transferDate" name="transferDate" value="<s:date name='transferDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>
			<td style="width:160px;text-align:center;"></td>
			<td style="width:200px;">
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6">Identify Root Cause of the Problem（识别问题根因）</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">方法</td>
			<td style="width:200px;">
				<s:checkboxlist
					theme="simple" list="methods" listKey="value" listValue="name"
					name="method" value="method">
				</s:checkboxlist>
			</td>
			<td style="width:160px;text-align:center;">原因分类</td>
			<td style="width:200px;">
				<s:select list="reasons"
					listKey="value" 
					listValue="name" 
					name="reason" 
					id="reason" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">责任部门</td>
			<td style="width:200px;">
				<s:select list="departments"
					listKey="value" 
					listValue="name" 
					name="department" 
					id="department" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remark1" name="remark1"  >${remark1}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment1" name="attachment1" value="${attachment1}"/>
			</td>
		</tr>
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >Formulate Corrective Actions（制定纠正措施）</td>
		</tr>			
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remark2" name="remark2"  >${remark2}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="5">
				<input type="hidden"  isFile="true" id="attachment2" name="attachment2" value="${attachment2}"/>
			</td>
		</tr>	
		<tr style="background-color: CornflowerBlue;color: white;font-weight: bold;">
			<td style="width:200px;text-align: center;font-size: 18px;" colspan="6" >回传客户接受状况</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">回复日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="replyDate" name="replyDate" value="<s:date name='replyDate' format="yyyy-MM-dd"/>" />
			</td>
			<td style="width:160px;text-align:center;">关闭状态</td>
			<td style="width:200px;">
				<s:select list="closeStates"
					listKey="value" 
					listValue="name" 
					name="closeState" 
					id="closeState" 
					cssStyle="width:140px;"
					onchange=""
					theme="simple">
				</s:select>
			</td>
			<td style="width:160px;text-align:center;">关闭确认人</td>
			<td style="width:200px;">
				<input type="text" id="closeMan" isTemp="true" isUser="true" hiddenInputId="closeManLogin" style="float: left;"  name="closeMan" value="${closeMan}" />
				<input type="hidden" id="closeManLogin" name="closeManLogin" value="${closeManLogin}" />
			</td>
		</tr>	
		<tr>
			<td style="width:200px;" colspan="6">
				<textarea rows="5"   id="remark3" name="remark3"  >${remark3}</textarea>
			</td>
		</tr>
		<tr>
			<td style="width:160px;text-align:center;">附件上传</td>
			<td style="width:200px;" colspan="3">
				<input type="hidden"  isFile="true" id="attachment3" name="attachment3" value="${attachment3}"/>
			</td>
			<td style="width:160px;text-align:center;">回复日期</td>
			<td style="width:200px;">
				<input  type="text" isDate="true" id="closeDate" name="closeDate" value="<s:date name='closeDate' format="yyyy-MM-dd"/>" />
			</td>
		</tr>
	</table>