<%@ page import="java.util.Iterator,
                 java.util.MissingResourceException"%>
<table border="0" cellpadding="0" cellspacing="0">
<tr>
<basic:forEachActionInWindow family="mode">
   <% if (Boolean.TRUE == enabled)
   {
      try
      {
         String path = MODES.getString(name + "." + index);
         %>
         <td><a href="<%= url %>"><img src="<%= contextPath + path %>" border="0"/></a></td>
         <%
      }
      catch (MissingResourceException e)
      {
         e.printStackTrace();
      }
   }
   %>
</basic:forEachActionInWindow>
<basic:forEachActionInWindow family="windowstate">
   <% if (Boolean.TRUE == enabled)
   {
      try
      {
         String path = WINDOW_STATES.getString(name + "." + index);
         %>
         <td><a href="<%= url %>"><img src="<%= contextPath + path %>" border="0"/></a></td>
         <%
      }
      catch (MissingResourceException e)
      {
         e.printStackTrace();
      }
   }
   %>
</basic:forEachActionInWindow>
<%--
   <basic:forEachActionInWindow family="move">
   <% if (Boolean.TRUE == enabled)
      {
      %>
      <td><a href="<%= url %>"><%= name %></a></td>
      <%
      }
   %>
   </basic:forEachActionInWindow>
--%>
</tr>
</table>
