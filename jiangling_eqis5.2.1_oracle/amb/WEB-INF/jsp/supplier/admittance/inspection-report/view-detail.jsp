<%@page import="net.sf.json.JSONObject"%>
<%@page import="net.sf.json.JSONArray"%>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.text.DecimalFormat"%>
<%@page import="com.ambition.supplier.entity.InspectionGradeType"%>
<%@page import="com.ambition.util.common.DateUtil"%>
<%@page import="com.norteksoft.product.api.entity.Opinion"%>
<%@page import="com.norteksoft.task.base.enumeration.TaskProcessingResult"%>
<%@page import="com.ambition.supplier.entity.InspectionReport"%>
<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<%
	InspectionGradeType inspectionGradeType = (InspectionGradeType)request.getAttribute("inspectionGradeType");
	if(inspectionGradeType == null){
		inspectionGradeType = new InspectionGradeType();
	}
	List<InspectionGradeType> childGradeTypes = inspectionGradeType.getChildren();
	if(childGradeTypes==null){
		childGradeTypes = new ArrayList<InspectionGradeType>();
	}
%>
<div class="opt-content" id="opt-content" style="overflow-y:auto;">
	<table class="form-table-border-left" style="width:100%;border:0px;">
		<caption style="height: 35px;text-align: center"><h2>潜在供应商考察报告 -- <%=inspectionGradeType.getName() %></h2></caption>
		<caption style="margin-bottom:4px;"><div style="float:right;padding-right:8px;padding-bottom:4px;">编号:${inspectionReport.code}</div></caption>
		<tr>
			<td style="width:90px;">供应商<font color="red">*</font></td>
			<td>
				<input id="supplierName" value="${inspectionReport.supplier.name}" style="width:70%;" readonly="readonly"/>
				<input type="hidden" name="supplierId" id="supplierId" value="${inspectionReport.supplier.id}"></input>
			</td>
			<td style="width:90px;">生产的产品</td>
			<td colspan="3">
				<input name="matingProducts" value="${inspectionReport.matingProducts}" style="width:90%;"  readonly="readonly"/>
			</td>
		</tr>
		<tr>
			<td style="width:90px;">稽核日期<font color="red">*</font></td>
			<td>
				<input name="inspectionDate" id="inspectionDate"  readonly="readonly" value="${inspectionReport.inspectionDateStr}" style="width:120px;"/>
			</td>
			<td style="width:90px;">稽核人员<font color="red">*</font></td>
			<td>
				<input id="inspectionManLoginName" value="${inspectionReport.inspectionManLoginName}" type="hidden"/>
				<input id="inspectionMan" readonly="readonly" value="${inspectionReport.inspectionMan}" style="width:70%;"/>
			</td>
			<td style="width:90px;">版本<font color="red">*</font></td>
			<td>
				<input name="reportVersion" value="${inspectionReport.reportVersion}" readonly="readonly" style="width:90px;"/>
			</td>
		</tr>
		<tr>
			<td style="width:90px;">评审人员</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getReviewer() %>" style="width:70%;"/>
			</td>
			<td style="width:90px;">标准分</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getTotalFee() %>" style="width:90px;"/>
			</td>
			<td style="width:90px;">实际分</td>
			<td>
				<input readonly="readonly" value="<%=inspectionGradeType.getRealFee() %>" style="width:90px;"/>
			</td>
		</tr>
	</table>
	<form>
		<table class="form-table-border-left" style="width:100%;margin-top:4px;margin-bottom:4px;">
			<tr height=29>
				<td style="width:30px;background:#FFCC99;font-weight:bold;text-align:center;">
					序号
				</td>
				<td style="width:300px;background:#FFCC99;font-weight:bold;">
					项目
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					标准分数
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					实际分数
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					实际%
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					总计%
				</td>
				<td style="width:60px;background:#FFCC99;font-weight:bold;">
					目标%
				</td>
			</tr>
			<%
			JSONArray category = new JSONArray(),totalData = new JSONArray(),realData = new JSONArray(),targetData = new JSONArray();
			DecimalFormat df = new DecimalFormat("0.0%");
			int index=1;
			for(InspectionGradeType childGridType : childGradeTypes){
				JSONObject label = new JSONObject();
				label.put("label",childGridType.getName());
				category.add(label);
			%>
			<tr height=29>
				<td style="text-align:center;">
					<%=index++ %>
				</td>
				<td>
					<%=childGridType.getName() %>
				</td>
				<td>
					<%=childGridType.getTotalFee() %>
				</td>
				<td>
					<%=childGridType.getRealFee() %>
				</td>
				<td>
					<%
						JSONObject real = new JSONObject();
						real.put("value", childGridType.getRealFee()/childGridType.getTotalFee()*100.0);
						realData.add(real);
					%>
					<%=df.format(childGridType.getRealFee()/childGridType.getTotalFee())%>
				</td>
				<td>
					<%
						JSONObject total = new JSONObject();
						total.put("value",100);
						totalData.add(total);
					%>
					100%
				</td>
				<td>
					<%
						JSONObject target = new JSONObject();
						target.put("value",70);
						targetData.add(target);
					%>
					70%
				</td>
			</tr>
			<%} %>
			<tr height=29>
				<td style="background:#CCFFCC;font-weight:bold;">
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					综合评分
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					<%=inspectionGradeType.getTotalFee() %>
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					<%=inspectionGradeType.getRealFee() %>
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					<%=df.format(inspectionGradeType.getRealFee()/inspectionGradeType.getTotalFee())%>
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					100%
				</td>
				<td style="background:#CCFFCC;font-weight:bold;">
					70%
				</td>
			</tr>
		</table>
		<div id="chart-<%=inspectionGradeType.getId()%>" style="margin-top:4px;">
		</div>
	</form>
</div>
<script type="text/javascript">
<!--
	var chart<%=inspectionGradeType.getId()%> = new FusionCharts({
		"swfUrl":"<%=request.getContextPath()%>/widgets/fusionCharts/Radar.swf",
		"width" : 600, 
		"height" : 600,
		"renderAt" : "chart-<%=inspectionGradeType.getId()%>"
// 		"chart": {        "numberprefix": "$",        "caption": "Sales Per Employee for year 1996",        "startingangle": "125",        "pieslicedepth": "30",        "formatnumberscale": "0",        "animation": "1",        "palette": "2"    },    "data": [        {            "label": "Leverling",            "value": "100524",            "issliced": "1"        },        {            "label": "Fuller",            "value": "87790",            "issliced": "1"        },        {            "label": "Davolio",            "value": "81898",            "issliced": "0"        },        {            "label": "Peacock",            "value": "76438",            "issliced": "0"        },        {            "label": "King",            "value": "57430",            "issliced": "0"        },        {            "label": "Callahan",            "value": "55091",            "issliced": "0"        },        {            "label": "Dodsworth",            "value": "43962",            "issliced": "0"        },        {            "label": "Suyama",            "value": "22474",            "issliced": "0"        },        {            "label": "Buchanan",            "value": "21637",            "issliced": "0"        }    ],    "styles": {        "definition": [            {                "color": "666666",                "size": "15",                "type": "font",                "name": "CaptionFont"            },            {                "bold": "0",                "type": "font",                "name": "SubCaptionFont"            }        ],        "application": [            {                "styles": "CaptionFont",                "toobject": "caption"            },            {                "styles": "SubCaptionFont",                "toobject": "SubCaption"            }        ]    }
	}).render();  
	chart<%=inspectionGradeType.getId()%>.setJSONData({
		"chart": {
			numberendfix : '%',
			numVDivLines : 10,
			"caption": "<%=inspectionGradeType.getName()%>",
// 			"legendposition": "RIGHT",
			"numdivlines": "2",        
			"anchoralpha": "100",        
			"plotborderthickness": "2",        
			"plotfillalpha": "5",        
			"radarfillcolor": "FFFFFF",        
			"bgcolor": "FFFFFF"
		},
		"categories": [{
			"category": <%=category%>
		}],
		"dataset": [{
			"color": "#4A7EBB",
			"anchorradius": "2",
			"anchorsides": "6",
			"seriesname": "实际%",
			"data": <%=realData%>
		},{
			"color": "#9BBB59",
			"anchorradius": "2",
			"anchorsides": "6",
			"seriesname": "目标%",
			"data": <%=targetData%>
		}]
	});
//-->
</script>
				