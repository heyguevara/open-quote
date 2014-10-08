<table width="100%" cellpadding="0" cellspacing="0" border="0">
   <tr>
      <td width="8" height="33"><img
            src="<%= contextPath %>/phalanx/images/portlet_window_top_left.gif"></td>
      <td height="33" background="<%= contextPath %>/phalanx/images/portlet_header_bg.gif">
         <table cellpadding="0" cellspacing="0" border="0" width="100%">
            <tr>
               <td align="left">
                  <font face="Verdana, Arial, Helvetica, sans-serif" style="font-size:14px">
                     <b><%= title %></b></font>
               </td>
               <td align="right">
                  <table cellpadding="0" cellspacing="0" border="0">
                     <tr>
                        <td align="right" height="20" background="<%= contextPath %>/phalanx/images/modes_bg.gif">
                           <img src="<%= contextPath %>/phalanx/images/modes_left.gif">
                           <%@ include file="decoration.jsp" %>
                           <img src="<%= contextPath %>/phalanx/images/modes_right.gif">
                        </td>
                     </tr>
                  </table>
               </td>
            </tr>
         </table>
      </td>
      <td height="33" width="12"><img
            src="<%= contextPath %>/phalanx/images/portlet_window_top_right.gif" height="33"
            width="12"></td>
   </tr>
<%
   // hack for minimized windows
   if (content != null)
   {
%>
   <tr>
      <td colspan="3">
         <table width="100%" cellpadding="0" cellspacing="0" border="0">
            <tr>
               <td bgcolor="#535353"><img src="<%= contextPath %>/phalanx/images/spacer.gif">
               </td>
               <td width="100%" bgcolor="#FFFFFF" style="padding:5px;" align="left"><div
                     class="portlet-font"><%= content %></div></td>
               <td background="<%= contextPath %>/phalanx/images/portlet_window_border_right.gif">
                  <img src="<%= contextPath %>/phalanx/images/spacer.gif" width="4">
               </td>
            </tr>
         </table>
      </td>
   </tr>
<% }  %>
   <tr>
      <td><img src="<%= contextPath %>/phalanx/images/portlet_window_btm_left.gif"></td>
      <td width="100%"
          background="<%= contextPath %>/phalanx/images/portlet_window_btm_center.gif"></td>
      <td><img src="<%= contextPath %>/phalanx/images/portlet_window_btm_right.gif"></td>
   </tr>

</table>
<br/>