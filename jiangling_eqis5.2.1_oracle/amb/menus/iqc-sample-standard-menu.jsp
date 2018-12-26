<%@ page contentType="text/html;charset=UTF-8"%>
<%@ include file="/common/taglibs.jsp"%>
<!-- 
<div style="display: block; height: 10px;"></div>
<div id="_code_letter" class="west-notree" url="${iqcctx}/sample-standard/code-letter/list.htm" onclick="changeMenu(this);"><span>样本量字码维护</span></div>
<div id="_ordinary_sample" class="west-notree" url="${iqcctx}/sample-standard/ordinary-sample/list.htm" onclick="changeMenu(this);"><span>正常检验抽样维护</span></div>
<div id="_tighten_sample" class="west-notree" url="${iqcctx}/sample-standard/tighten-sample/list.htm" onclick="changeMenu(this);"><span>加严检验抽样维护</span></div>
<div id="_relax_sample" class="west-notree" url="${iqcctx}/sample-standard/relax-sample/list.htm" onclick="changeMenu(this);"><span>放宽检验抽样维护</span></div>
<div id="_transition_sample" class="west-notree" url="${iqcctx}/sample-standard/transition-rule/input.htm" onclick="changeMenu(this);"><span>抽样方案转移规则</span></div>
 -->
<div id="accordion1" class="basic">
<security:authorize ifAnyGranted="iqc-sample-standard-code-letter-list,iqc-sample-standard-ordinary-sample-list,iqc-sample-standard-tighten-sample-list,iqc-sample-standard-relax-sample-list,iqc-sample-standard-code-letter-cl-list,iqc-sample-standard-count-sample-list,iqc-sample-standard-measure-sample-list,iqc-sample-standard-transition-rule-input">
	<h3><a href="#" id="_iqcsamplesheme">抽样方案维护</a></h3>
	<div>
		<div id="iqcsamplesheme" class="demo"></div>
</security:authorize>
		<script type="text/javascript" class="source">
		$(function () {
				$("#iqcsamplesheme").jstree({ 
					"core" : { "initially_open" : [ "root" ] },
					"html_data" : {
						"data" : "<ul><li  class='jstree-closed' id='root'>GB/T2828"+
				          "<ul>"+
				          <security:authorize ifAnyGranted="iqc-sample-standard-code-letter-list">
						  	"<li onclick='selectedNode(this)' id='2828-code-letter'><a href='${iqcctx}/sample-standard/code-letter/list.htm'>样本量字码表</a></li>"+
						  </security:authorize>
						  <security:authorize ifAnyGranted="iqc-sample-standard-ordinary-sample-list">
					      	"<li onclick='selectedNode(this)' id='2828-ordinary-sample'><a href='${iqcctx}/sample-standard/ordinary-sample/list.htm'>正常检验抽样方案</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="iqc-sample-standard-tighten-sample-list">
					      	"<li onclick='selectedNode(this)' id='2828-tighten-sample'><a href='${iqcctx}/sample-standard/tighten-sample/list.htm'>加严检验抽样方案</a></li>"+
					      </security:authorize>
					      <security:authorize ifAnyGranted="iqc-sample-standard-relax-sample-list">
					      	"<li onclick='selectedNode(this)' id='2828-relax-sample'><a href='${iqcctx}/sample-standard/relax-sample/list.htm'>放宽检验抽样方案</a></li>"+
					      </security:authorize>
					      "</ul>"+
				          "</li></ul>"+
				          "<ul><li class='jstree-closed' id='root'>MIL-STD-1916"+
				          "<ul>"+
				          <security:authorize ifAnyGranted="iqc-sample-standard-code-letter-cl-list">
				          	"<li onclick='selectedNode(this)' id='1916-code-letter'><a href='${iqcctx}/sample-standard/code-letter/cl-list.htm'>样本代字对照表</a></li>"+
				          </security:authorize>
				          <security:authorize ifAnyGranted="iqc-sample-standard-count-sample-list">
				         	 "<li onclick='selectedNode(this)' id='count-plan'><a href='${iqcctx}/sample-standard/count-sample/list.htm'>计数值抽样计划</a></li>"+
				          </security:authorize>
				          <security:authorize ifAnyGranted="iqc-sample-standard-measure-sample-list">
				          	"<li onclick='selectedNode(this)' id='measure-plan'><a href='${iqcctx}/sample-standard/measure-sample/list.htm'>计量值抽样计划</a></li>"+
				          </security:authorize>
				          "</ul>"+ 
				          "</li></ul>"+
				          "<ul><li class='jstree-closed' id='root'>零缺陷(C=0)抽样方案"+
					      "<ul>"+
						  <security:authorize ifAnyGranted="IQC_SAMPLE-STANDARD_C0-SAMPLE_LIST">
							 "<li onclick='selectedNode(this)' id='c1051'><a href='${iqcctx}/sample-standard/c1051/list.htm'>零缺陷抽样方案</a></li>"+
					      </security:authorize>
					      "</ul>"+
				          "</li></ul>"+
				          "<ul><li class='jstree-closed' id='root'>转移规则"+
				          "<ul>"+
				          <security:authorize ifAnyGranted="iqc-sample-standard-transition-rule-input">
				         	 "<li onclick='selectedNode(this)' id='transition-rule'><a href='${iqcctx}/sample-standard/transition-rule/input.htm'>转移规则维护</a></li>"+
				          </security:authorize>
				          "</ul>"+
				          "</li></ul>"
					},
					"ui" : {
						"initially_select" : [ treeMenu ]
					},
					"plugins" : [ "themes", "html_data","ui" ]
				});
				$("#accordion1").accordion({ fillSpace:	true,active:0 });
			
		});
		function selectedNode(obj){
			window.location = $(obj).children('a').attr('href');
		}
		
		</script>
	</div>

</div>
 
 
<script type="text/javascript">
$().ready(function(){
	$('#'+thirdMenu).addClass('west-notree-selected');
});
function changeMenu(obj){
	window.location = $(obj).attr('url');
}
</script>