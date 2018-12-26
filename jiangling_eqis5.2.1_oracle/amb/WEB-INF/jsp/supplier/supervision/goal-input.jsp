<%@ page contentType="text/html;charset=UTF-8" import="java.util.*"%>
<%@ include file="/common/taglibs.jsp"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<title>安必兴—企业管理效率促进专家</title>
<%@include file="/common/meta.jsp"%>
<script src="${ctx}/widgets/validation/jquery.validate.js"
	type="text/javascript"></script>
<link rel="stylesheet" type="text/css"
	href="${ctx}/widgets/validation/cmxform.css" />
<script src="${ctx}/widgets/validation/jquery.metadata.js"
	type="text/javascript"></script>
<script src="${ctx}/widgets/validation/dynamic.validate.js"
	type="text/javascript"></script>
<script type="text/javascript" src="${ctx}/js/common-layout.js"></script>
</head>

<body onclick="$('#sysTableDiv').hide(); $('#styleList').hide();">
	<script type="text/javascript">
		var secMenu="supervision";
		var thirdMenu="_goal_input";
	</script>

	<div id="header" class="ui-north">
		<%@ include file="/menus/header.jsp"%>
	</div>
	<div id="secNav">
		<%@ include file="/menus/supplier-sec-menu.jsp"%>
	</div>

	<div class="ui-layout-west">
		<%@ include file="/menus/supplier-supervision-menu.jsp"%>
	</div>

	<div class="ui-layout-center">
			<div class="opt-body" style="overflow-y:auto;">
				<div class="opt-btn">
					<button class='btn' onclick=""><span><span>保存</span></span></button>
					<button class='btn' onclick=""><span><span>返回</span></span></button>
					<button class='btn' onclick=""><span><span>导出</span></span></button>
					<button class='btn' onclick=""><span><span>打印</span></span></button>
				 </div>
				 
				<div id="opt-content" >
					<form action="" method="post" id="appraisalForm" name="appraisalForm">
						
						<table class="form-table-without-border" id="appraisal-table"	style="width:100%;">
						<caption style="height: 35px;text-align: center"><h2>供应商检查评分表</h2></caption>
								<tr>
									<td colspan="8" style="text-align: right">编号:JC-2011-10-007&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td>
								</tr>
								<tr>
									<td>供应商名称</td>
									<td colspan="7"><input /></td>
								</tr>
								<tr>
									<td>机型</td>
									<td><input /></td>
									<td>监察时间</td>
									<td><input /></td>
									<td>零件名</td>
									<td><input /></td>
									<td>零件号</td>
									<td><input /></td>
								</tr>
								<tr>
									<td>审核理由</td>
									<td colspan="7"><input  type="checkbox"/>新品开发&nbsp;&nbsp;&nbsp;&nbsp;<input  type="checkbox"/>设计变更&nbsp;&nbsp;&nbsp;&nbsp;
									<input  type="checkbox"/>过程变更&nbsp;&nbsp;&nbsp;&nbsp;<input  type="checkbox"/>例行监察&nbsp;&nbsp;&nbsp;&nbsp;</td>
								</tr>
						</table>
						<table class="form-table-border-left" id="inspection-table"	style="width:100%;">
							<thead><tr>
								<th colspan="2" width="10%">项目</th>
								<th width="5%">序号</th>
								<th width="20%">确认事项</th>
								<th width="5%">权重</th>
								<th width="5%">上次得分</th>
								<th width="20%">问题</th>
								<th width="5%">本次得分</th>
								<th width="20%">问题</th>
								<th width="10%">备注</th>
							</tr></thead>
							<tbody>
								<tr>
									<td rowspan="38">文<br />件<br />审<br />查<br />部<br />分</td>
									<td rowspan="7">产品/过程质量体系</td>
									<td>1</td>
									<td>有适用的设计开发、检验、不合格品、制造管理程序</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>2</td>
									<td>有前一次监察（或样件验收、PPAP等）的记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>3</td>
									<td>针对前一次监察（或样件验收、PPAP等）存在问题整改的结果及效果确认</td>
									<td>0.7</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>4</td>
									<td>内部发现问题的整改及效果确认</td>
									<td>0.4</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>5</td>
									<td>纠正重大不良的整改及效果确认</td>
									<td>0.7</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>6</td>
									<td>有按APQP设计符合海马动力要求的项目计划，节点评审、整改记录完整</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>7</td>
									<td>主要项目参与人有APQP、FMEA、SPC培训记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>场地平面布置图</td>
									<td>8</td>
									<td>场地平面布置图签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>过程流程图</td>
									<td>9</td>
									<td>过程流程图签字齐全，标明模具、工装卡具、检具、检测参数和频度</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="5">FMEA</td>
									<td>10</td>
									<td>FMEA文件签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>11</td>
									<td>高风险系数标准确定合理，高风险顺序数项目有纠正措施及验证记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>12</td>
									<td>制定产品/过程特殊特性表</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>13</td>
									<td>所有产品/过程特殊特性都已分析</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>14</td>
									<td>借鉴类似成熟产品的FMEA文件（如是全新开发则本条按“部分符合”打分）</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="6">生产控制计划</td>
									<td>15</td>
									<td>生产控制计划签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>16</td>
									<td>过程流程图所有工序都列入控制计划</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>17</td>
									<td>所有产品/过程特殊特性都列入控制计划</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>18</td>
									<td>高风险顺序数项目探测方法与FMEA文件一致</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>19</td>
									<td>按控制计划配备操作人员</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>20</td>
									<td>配备全尺寸检验、性能试验、分析整改人员</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="3">过程作业指导书</td>
									<td>21</td>
									<td>按控制计划配备过程作业指导书</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>22</td>
									<td>过程作业指导书签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>23</td>
									<td>操作人员完全理解过程作业指导书</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="4">初始过程能力</td>
									<td>24</td>
									<td>按控制计划使用控制图控制的项目都已进行过程能力分析</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>25</td>
									<td>初始过程能力评价报告签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>26</td>
									<td>依据的原始数据齐全</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>27</td>
									<td>量产稳定阶段，Cpk≥1.33</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="5">初始过程能力</td>
									<td>28</td>
									<td>A、AR、B类项目使用的测量系统都已进行分析</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>29</td>
									<td>测量系统分析报告签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>30</td>
									<td>依据的原始数据齐全</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>31</td>
									<td>测量系统偏倚：落在α水平=0.05区间内</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>32</td>
									<td>GRR：10个零件，3人，重复3次，GRR＜30%）；</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="4">初始过程能力</td>
									<td>33</td>
									<td>内部物流配送方案签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>34</td>
									<td>包装方案经内部评审并记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>35</td>
									<td>包装方案评审依据的原始数据齐全</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>36</td>
									<td>包装方案经海马动力确认</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="2">作业者培训</td>
									<td>37</td>
									<td>操作员、检验员等的培训计划签字齐全，全新研制产品有评审记录</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>38</td>
									<td>所有操作员有培训记录，必要时有操作证</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="21">生<br />产<br />确<br />认<br />部<br />分</td>
									<td rowspan="2">入厂检验</td>
									<td>1</td>
									<td>库内原材料、外购件标识卡清晰，并记载炉批或热处理批等批次信息</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>2</td>
									<td>有外购件入厂检验报告和供方检验报告，并与批次对应</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="8">制造管理</td>
									<td>3</td>
									<td>各工序配备作业者易得到的“工序卡”，“作业指导书”</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>4</td>
									<td>工序现场和所用的“工序卡”，“作业指导书”有“重要工序”标识</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>5</td>
									<td>规定设备点检内容，有设备点检记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>6</td>
									<td>规定夹具、模具定期点检，使用极限，有点检记录、更换记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>7</td>
									<td>规定工位器具定期点检，清洁，有点检记录、更换记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>8</td>
									<td>检具在校准周期内，有明显的“校准可用”标识</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>9</td>
									<td>有不合格品标识卡，隔离区，处理流程；有处理记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>10</td>
									<td>规定产品更换时的点检内容，有更换记录</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="6">生产过程控制</td>
									<td>11</td>
									<td>实际生产流程符合过程流程图</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>12</td>
									<td>实际内部物流符合场地平面布置图</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>13</td>
									<td>防止零件磕、碰伤的措施切实可行</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>14</td>
									<td>实际过程控制符合量产控制计划</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>15</td>
									<td>实际操作符合过程作业指导书</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>16</td>
									<td>零件抽检合格</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="2">过程能力</td>
									<td>17</td>
									<td>实际节拍符合海马要求</td>
									<td>0.4</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>18</td>
									<td>实际工序节拍平衡率）≥70%</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td rowspan="3">出厂检验</td>
									<td>19</td>
									<td>出厂检验报告由质量责任人或授权人签字</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>20</td>
									<td>按包装作业指导书包装</td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td>21</td>
									<td>每个独立包装都按海马动力采购要求粘贴标识卡，并标明提交状态</td>
									<td>0.1</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="2">其他</td>
									<td>22</td>
									<td>现场至少生产100套合格零件 </td>
									<td>0.2</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="3" style="font-weight: bold;">评价</td>
									<td style="font-weight: bold;">合计得分 </td>
									<td colspan="2">&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
									<td>&nbsp;</td>
								</tr>
								<tr>
									<td colspan="3" style="font-weight: bold;">监察小组成员</td>
									<td colspan="7">&nbsp;</td>
								</tr>
							</tbody>
						</table>
					</form>
				</div>
			</div>
		</div>

</body>
</html>