<%@page import="org.jboss.portal.server.ServerURL"%>
<%@page import="org.jboss.portal.server.AbstractServerURL"%>
<script src="<%=request.getContextPath()%>/js/modal.js" type="text/javascript"></script>
<div id="login-modal" style="display:none">
     <div id="login-modal-msg" style="display:none;width:257px;height:157px">
        <% String loginHeight = "100%"; %>
      <iframe src="<%=request.getAttribute("org.jboss.portal.PORTAL_CONTEXT_PATH")%>/auth/?loginheight=0" frameborder="0" width="257" height="157" scrolling="no" marginheight="0" marginwidth="0" name="login-content" class="login-content" id="loginIframe"></iframe>
     </div>
</div>