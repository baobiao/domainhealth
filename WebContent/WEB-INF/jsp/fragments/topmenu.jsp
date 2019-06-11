				<div id="top_menu_container">					
					<div id="appname">DomainHealth</div>					
					<ul>
						<li><a href='<c:out value="${contexturl}"/>/corestats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>Core</a></li>
						<li><a href='<c:out value="${contexturl}"/>/datasourcestats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>JDBC</a></li>
						<li><a href='<c:out value="${contexturl}"/>/destinationstats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>JMS</a></li>
						<li><a href='<c:out value="${contexturl}"/>/webappstats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>Web-Apps</a></li>
						<li><a href='<c:out value="${contexturl}"/>/ejbstats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>EJBs</a></li>
						<li><a href='<c:out value="${contexturl}"/>/workmgrstats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>Work Mgrs</a></li>
						<li><a href='<c:out value="${contexturl}"/>/svrchnlstats?datetime=<c:out value="${datetime}"/>&duration=<c:out value="${duration}"/>&scope=<c:out value="${scope}"/>&direction=current'>Channels</a></li>
					</ul>
				</div> 
