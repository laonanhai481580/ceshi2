<%@page import="com.ambition.spc.entity.FeatureLayer"%>
<%@ taglib uri="/struts-tags" prefix="s"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.ArrayList"%>
<%@ page language="java"  pageEncoding="UTF-8"%>
<%
	List<FeatureLayer> layerItems = (List<FeatureLayer>)request.getAttribute("layerItems");
	if(layerItems == null){layerItems = new ArrayList<FeatureLayer>();}
	List<Map<Object,List<Object>>> layerLists = (List<Map<Object,List<Object>>>)request.getAttribute("layerLists");
	if(layerLists == null){layerLists = new ArrayList<Map<Object,List<Object>>>();}
%>
<table class="form-table-border-left" style="border:0px;table-layout:fixed;">
	<thead>
		<tr style="height:20px;background:#99CCFF;font-weight:bold;">
			<td style="width:20px;text-align:center;">NO</td>
			<td style="width:80px;text-align:center;">层别</td>
			<td style="width:150px;text-align:center;">取值</td>
			<td style="width:1px;text-align:center;display: none;">简码</td>
			<td style="width:1px;text-align:center;display: none;">取值方式</td>
		</tr>
	</thead>
	<tbody>
		<%
			int i=1,flag = 0;
			boolean isLast = false;
			for(Map<Object,List<Object>> map : layerLists){
				flag++;
				if(flag == layerLists.size()){
					isLast = true;
				}
				FeatureLayer layer = new FeatureLayer();
				List<Object> list = new ArrayList<Object>();
				Iterator iterator = map.keySet().iterator();//获得一个键的Set的集合,set是无顺序的.不可以重复,
				while (iterator.hasNext()) {
					layer = (FeatureLayer)iterator.next();
					list = (List<Object>)map.get(layer);
				}
		%>
				<tr>
					<td style="text-align: center;width:20px;"><%=i++ %></td>
					<td style="text-align: center;">
						<input type="hidden" name="tagName" value="<%=layer.getDetailName()==null?"":layer.getDetailName()%>"/>
						<%=layer.getDetailName()==null?"":layer.getDetailName().replaceAll("\n","")%>
					</td>
					<td style="text-align:center;">
		<%
			if("2".equals(layer.getSampleMethod())){
		%>
						<select id="tagValue" name="tagValue" style="width:140px;">
							<option value=""></option>
							<%
								for(Object obj:list){
									Map map1 = (Map)obj;
							%>
							<option value="<%=map1.get("value")%>"><%=map1.get("value")%></option>
							<%}%>
						</select>
		<%}else{ %>
						<input id="tagValue" name="tagValue" style="width:140px;"/>
		<%} %>
					</td>
					<td style="text-align: center;display: none;">
						<input type="hidden" name="tagCode" value="<%=layer.getDetailCode()==null?"":layer.getDetailCode()%>"/>
						<%=layer.getDetailCode()==null?"":layer.getDetailCode() %>
					</td>
					<td style="text-align: center;display: none;">
						<input type="hidden" name="method" value="<%=layer.getSampleMethod()==null?"":layer.getSampleMethod()%>"/>
						<%=layer.getSampleMethod()==null?"":layer.getSampleMethod() %>
					</td>
				</tr>
		<%} %>
	</tbody>
</table>