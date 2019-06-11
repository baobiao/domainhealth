<%@ page import="domainhealth.core.jmx.*"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.HEAP_USED_CURRENT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.OPEN_SOCKETS%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>																		
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.THROUGHPUT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.COMPLETED_REQUEST_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.THREAD_POOL_QUEUE_LENGTH%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.PENDING_USER_REQUEST_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.EXECUTE_THREAD_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.EXECUTE_THREAD_IDLE_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.STANDBY_THREAD_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.HOGGING_THREAD_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.MIN_THREADS_CONSTRAINT_COMPLETED%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.MIN_THREADS_CONSTRAINT_PENDING%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>												
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTION_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTIONS_ACTIVE_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTION_COMMITTED_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTION_ROLLEDBACK_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						
	<tr>
		<td class="datarow" align="left" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTION_HEURISTICS_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
		<td class="datarow" align="right" width="50%"><img src='<c:out value="${contexturl}"/>/charts/<c:out value="${resourcetype}"/>/<%=WebLogicMBeanPropConstants.TRANSACTION_ABANDONED_TOTAL_COUNT%>.png?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>'/></td>
	</tr>						