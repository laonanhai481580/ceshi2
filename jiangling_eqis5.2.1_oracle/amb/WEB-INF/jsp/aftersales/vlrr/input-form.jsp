<%@page import="com.opensymphony.xwork2.ActionContext"%>
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
		<caption style="font-size: 24px;">VLRR数据录入表</caption>
		<input type="hidden" name= "itemData" id="itemData" value=""/>
	</table>
	<table style="width:100%;margin: auto;" class="form-table-border-left" id="default-table" partData="true">
		<tr>
			<td style="width:10%">厂区</td>
			<td>
				<s:select list="businessUnits" 
						  listKey="value" 
						  listValue="name"
						  theme="simple"
						  cssStyle="{required:true}"
						  name="businessUnitName"></s:select>
			</td>
			<td style="width:10%">日期<span style="color:red">*</span></td>
			<td style="width:25%">
				<input name="vlrrDate" isDate=true id="vlrrDate" value="<s:date name="vlrrDate" format="yyyy-MM-dd" />" class="{required:true,messages:{required:'必填'}}" ></input>
			</td>
			<td style="width:10%">客户名称</td>
			<td>
				<%-- <input id="customerName" name="customerName" value="${customerName}" /> --%>
				<s:select list="customers" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customerName"
					name="customerName"
					onchange="customerNameChange(this);"
					emptyOption="true">
				</s:select>
			</td>
			<td style="width:10%">客户场地</td>
			<td>
				<%-- <input id="customerFactory" name="customerFactory" value="${customerFactory}" /> --%>
				<s:select list="customerFactorys" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="customerFactory"
					name="customerFactory"
					emptyOption="true">
				</s:select>
			</td>
		</tr>
		<tr>
			<td style="width:10%">欧菲机型</td>		
			<td>
				<input id="ofilmModel" name="ofilmModel" value="${ofilmModel}" onclick="modelClick(this);"/>
			</td>
			<td style="width:10%">客户机型</td>		
			<td>
				<input id="customerModel" name="customerModel" value="${customerModel}" onclick="modelClick(this);"/>
			</td>
			<td style="width:10%">产品结构</td>
			<td>
				<%-- <input id="productStructure" name="productStructure" value="${productStructure}" /> --%>
				<s:select list="productStructures" 
					theme="simple"
					listKey="value" 
					listValue="name" 
					id="productStructure"
					name="productStructure"
					emptyOption="true">
				</s:select>
			</td>
			<td style="width:10%">生产日期</td>		
			<td>
				<input name="produceDate" isDate=true id="produceDate" value="<s:date name="produceDate" format="yyyy-MM-dd" />"  ></input>
			</td>			
		</tr>	
	</table>
	<div style="overflow-x:auto;overflow-y:hidden;">
	<table style="width:10%;margin: auto;float: left;" class="form-table-border-left" id="default-table" style="border:0px;">
		<tr>
			<td colspan="2" style="text-align: center;">不良项目</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">投入数</td>		
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良数</td>	
		</tr>
		<tr>
			<td colspan="2" style="text-align: center;">不良率</td>		
		</tr>
		<% 	
		List<Map<String,String>> defectionList=(List<Map<String,String>>)ActionContext.getContext().get("defectionList");		
	    for (Map<String,String> map:defectionList) {
		   int size=map.size();
		%>		
<%-- 	<div class="badItem" style="width: 10%;mar">
		<ul>
			<li style="background-color:#00ffe5;"><%=map.get("typeName")%></li>
			 <% for (String itemCode:map.keySet()) {
				 if(!itemCode.equals("typeName")){
				 
			%>	
			<li>
				<div><%=map.get(itemCode)%></div>
				<div><input type="text" style="width: 55%;float: left;" vlrr=true name="<%=itemCode%>_itemName" id="<%=itemCode%>_itemName" class="text-inp" value="" onkeyup="mustNum(this);"><span id="<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></div>				
			</li>
			<% 					
				 }}
			%>	
		</ul>
	</div>	 --%>
		<tr>
			<td rowspan="<%=size%>"><%=map.get("typeName")%></td>		
		</tr>
		 <% for (String itemCode:map.keySet()) {			
			 if(!itemCode.equals("typeName")){				 
		%>
		<tr>
			<td style="width: 100px;"><input  value="<%=map.get(itemCode)%>" style="width: 90%;border:none;background: none;" readonly="readonly" title="<%=map.get(itemCode)%>"/></td>
		</tr>
		<% 					
			}			
		}
		%>				
	<% 					
		 }
	    int flag=1;
	%>	
	</table>
	<input type="hidden"  id="fir" name="fir" value="<%=flag+1%>"/>
	<input type="hidden" id="flagIds" name="flagIds" value="a_<%=flag%>"/>
	<table style="width:8%;margin: auto;float: left;" class="form-table-border-left" id="default-table" name="datasTable" itemData="true">
		<tr>
			<td colspan="2" style="text-align: center;">
				<a  class="small-button-bg" style="float:left;" name="add" onclick="addRowHtml(this)" title="添加"><span class="ui-icon ui-icon-plus" style='cursor:pointer;'></span></a>
				<a  class="small-button-bg" style="margin-left:2px;float:left;" name="remove" onclick="removeRowHtml(this)" title="删除"><span class="ui-icon ui-icon-minus" style='cursor:pointer;'></span></a>
			</td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="inputCount" name="a<%=flag%>_inputCount" id="a<%=flag%>_inputCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_inputCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>		
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="unqualifiedCount" name="a<%=flag%>_unqualifiedCount" id="a<%=flag%>_unqualifiedCount"  value="" onkeyup="mustNum(this);caculateBadRate(this);" >
			<span id="a<%=flag%>_unqualifiedCount_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>
		</tr>
		<tr>
			<td style="width: 80px;"><input type="text" fieldName="unqualifiedRate" name="a<%=flag%>_unqualifiedRate" id="a<%=flag%>_unqualifiedRate"  value="" readonly="readonly"></td>	
		</tr>
		<% 	
	    for (Map<String,String> map:defectionList) {
	    	for (String itemCode:map.keySet()) {			
				 if(!itemCode.equals("typeName")){
		%>	
		<tr>
			<td style="width: 80px;"><input type="text"  fieldName="<%=itemCode%>" name="a<%=flag%>_<%=itemCode%>" id="a<%=flag%>_<%=itemCode%>"  value="" isItem="true" onkeyup="mustNum(this);caculateBadCount(this);">
			<span id="a<%=flag%>_<%=itemCode%>_span" style="color:red;padding-top:0.2rem;text-align: center;"></span></td>	
		</tr>
		<% 					
				}			
			}
	    }
		%>	
	</table>
	</div>