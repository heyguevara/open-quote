<%@ page
      import="java.util.MissingResourceException" %>

<basic:forEachActionInWindow family="mode">
   <%
      if(Boolean.TRUE == enabled)
      {
         try
         {
            String path = MODES.getString(name);
   %>
   <a href="<%= url %>"><img src="<%= contextPath + path %>" height="20" border="0"alt="<%= name %>"/></a>
   <%
         }
         catch(MissingResourceException e)
         {
            e.printStackTrace();
         }
      }
   %>
</basic:forEachActionInWindow>
<basic:forEachActionInWindow family="windowstate">
   <%
      if(Boolean.TRUE == enabled)
      {
         try
         {
            String path = WINDOW_STATES.getString(name);
   %>
   <a href="<%= url %>"><img src="<%= contextPath + path %>" height="20" border="0" alt="<%= name %>"/></a>
   <%
         }
         catch(MissingResourceException e)
         {
            e.printStackTrace();
         }
      }
   %>
</basic:forEachActionInWindow>

