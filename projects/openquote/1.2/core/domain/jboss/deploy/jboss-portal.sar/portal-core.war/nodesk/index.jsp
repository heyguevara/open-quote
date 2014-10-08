<%!

   /** Left region index. */
   private static final int LEFT = 0;

   /** Center region index. */
   private static final int CENTER = 1;

   /** Right region index. */
   private static final int RIGHT = 2;

   private static ResourceBundle MODES;
   private static ResourceBundle WINDOW_STATES;

   static
   {
      MODES = ResourceBundle.getBundle("nodesk.Modes");
      WINDOW_STATES = ResourceBundle.getBundle("nodesk.WindowStates");
   }

%>

<%@ taglib uri="/WEB-INF/theme-basic-lib.tld" prefix="basic" %>
<%@ page
    import="java.util.Iterator,
            java.io.Writer,
            java.io.IOException,
            java.io.StringWriter,
            java.util.Map,
            java.util.HashMap,
            org.jboss.portal.theme.LayoutConstants,
            org.jboss.portal.theme.page.PageResult,
            java.util.ResourceBundle"%>
<%@ page import="org.jboss.portal.server.PortalConstants"%>
<%
   String contextPath = request.getContextPath();
   int nbCol = 3;
   boolean leftIsEmpty = false;
   boolean centerIsEmpty = false;
   boolean rightIsEmpty = false;

   String left = "left";
   String center = "center";
   String right = "right";

   PageResult pageResult = (PageResult)request.getAttribute(LayoutConstants.ATTR_PAGE);
//   String layoutState = (String)request.getAttribute(LayoutConstants.PARAM_LAYOUT_STATE);
   if ("maximized".equals(pageResult.getLayoutState()))
   {
      left = "";
      center = "left,center,right";
      right = "";
      leftIsEmpty = true;
      centerIsEmpty = false;
      rightIsEmpty = true;
   }
   else
   {
      %>
      <%
   }

%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<title><%= PortalConstants.VERSION %></title>
<meta http-equiv="Content-Type" content="<%= response.getContentType() %>" />
<script language="JavaScript" type="text/javascript">
<!--
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  if (init==true) with (navigator) {if ((appName=="Netscape")&&(parseInt(appVersion)==4)) {
    document.MM_pgW=innerWidth; document.MM_pgH=innerHeight; onresize=MM_reloadPage; }}
  else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) location.reload();
}
MM_reloadPage(true);
//-->
</script>
<link href="<%= contextPath %>/nodesk/css/portal_style.css" rel="stylesheet" type="text/css" />
<!--<link href="/portal-forums/subSilver/styles.css" rel="stylesheet" type="text/css" />-->
<link rel="shortcut icon" href="<%= contextPath %>/images/favicon.ico" />
</head>
<body>
<table width="789" height="100%" border="0" align="center" cellpadding="0" cellspacing="0" class="portal-table">
 <!--DWLayoutTable-->
 <tr>
  <td height="111" colspan="<%= nbCol %>" align="center" valign="top"><img src="<%= contextPath %>/nodesk/images/portal_utils/portal_head.jpg" width="743" height="109"></td>
 </tr>
 <tr>
  <%
    if (!leftIsEmpty) {
      int index = LEFT;
    %>
  <td width="221" height="568" valign="top">
    <table border="0" align="right" cellpadding="0" cellspacing="0" background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_background.gif">

    <basic:forEachWindowInRegion region="<%= left %>">
   <tr>
    <td>
      <table id="left" border="0" cellpadding="0" cellspacing="0">
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_01.gif" width="15" height="5" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_02.gif" width="100%" height="5" alt=""></td>
       <td rowspan="2"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_03.gif" width="14" height="21" alt=""></td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_04.gif" width="15" height="16" alt=""></td>
       <td width="15"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_05.gif" width="15" height="16" alt=""></td>
       <!--td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_06.gif" width="1" height="16" alt=""></td-->
       <td nowrap height="16" valign="middle" background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_07.gif" class="portal-left-title-white"><%= title %></td>
       <td width="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_08.gif" width="4" height="16" alt=""></td>
       <td background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_07.gif" align="right" width="100%">
          <%@ include file="decoration.jsp" %>
       </td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_12.gif" width="15" height="7" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_13.gif" width="100%" height="7" alt=""></td>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_14.gif" width="14" height="7" alt=""></td>
      </tr>
      <tr>
       <td background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_15.gif"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_15.gif" width="15" height="100%" alt=""></td>
       <td colspan="4" bgcolor="#FFFFFF"><%= content %></td>
       <td width="14" background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_17.gif"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_17.gif" width="14" height="100%" alt=""></td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_18.gif" width="15" height="15" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_19.gif" width="100%" height="15" alt=""></td>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_20.gif" width="14" height="15" alt=""></td>
      </tr>
      <tr>
       <td colspan="6"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_21.gif" width="191" height="1" alt=""></td>
      </tr>
      <tr>
       <td colspan="6"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/spacer.gif" height="1" alt=""></td>
      </tr>
     </table>
     </td>
     </tr>
    </basic:forEachWindowInRegion>
  </table>
  </td>
    <%
  }
    %>

    <%
    if (!centerIsEmpty) {
      int index = CENTER;
    %>
  <td width="100%" valign="top">
    <table id="main" width="100%" height="100%" border="0" cellpadding="0" cellspacing="0">
    <basic:forEachWindowInRegion region="<%= center %>">
      <tr>
       <td height="16" width="24" colspan="2" rowspan="2"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_01.gif" width="24" height="16" alt=""></td>
       <td height="3" colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_02.gif" width="100%" height="3" alt=""></td>
       <td height="3"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="3" alt=""></td>
      </tr>
      <tr>
       <td height="18" rowspan="2" width="100%" valign="middle" background="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_03.gif" class="portal-left-title-black"><%= title %></td>
       <td height="18" rowspan="2" background="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_03.gif"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_04.gif" width="9" height="18" alt=""></td>



       <td height="18" rowspan="2" background="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_03.gif" align="right">
          <%@ include file="decoration.jsp" %>
       </td>
       <td height="18" rowspan="2"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_08.gif" width="17" height="18" alt=""></td>
       <td height="13"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="13" alt=""></td>
      </tr>
      <tr>
       <td height="11" width="14" rowspan="2"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_09.gif" width="14" height="11" alt=""></td>
       <td height="5" width="10"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_10.gif" width="10" height="5" alt=""></td>
       <td height="5"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="5" alt=""></td>
      </tr>
      <tr>
       <td height="6" colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_11.gif" width="100%" height="6" alt=""></td>
       <td height="6"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_12.gif" width="17" height="6" alt=""></td>
       <td height="6"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="6" alt=""></td>
      </tr>
      <tr>
       <td background="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_13.gif">&nbsp;</td>
       <td colspan="4" valign="top" bgcolor="#FFFFFF"><%
            %>
            <%= content %>
        </td>
       <td background="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_15.gif">&nbsp;</td>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="328" alt=""></td>
      </tr>
      <tr>
       <td height="16"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_16.gif" width="14" height="16" alt=""></td>
       <td height="16" colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_18.gif" width="100%" height="16" alt=""></td>
       <td height="16"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/portlet_middle_top_19.gif" width="17" height="16" alt=""></td>
       <td height="16"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="1" height="16" alt=""></td>
      </tr>
      <tr>
       <td colspan="7"><img src="<%= contextPath %>/nodesk/images/portlet_middle_top/spacer.gif" width="100%" height="1" alt=""></td>
      </tr>
    </basic:forEachWindowInRegion>
    </table>
  </td>
    <%
  }
    %>

    <%
    if (!rightIsEmpty) {
      int index = RIGHT;
    %>
  <td valign="top">
      <table width="191" border="0" align="right" cellpadding="0" cellspacing="0" background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_background.gif">
      <basic:forEachWindowInRegion region="<%= right %>">
   <tr>
    <td>
      <table id="right" border="0" cellpadding="0" cellspacing="0">
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_01.gif" width="15" height="5" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_02.gif" width="100%" height="5" alt=""></td>
       <td rowspan="2"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_03.gif" width="15" height="21" alt=""></td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_04.gif" width="15" height="16" alt=""></td>
       <td width="15"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_05.gif" width="15" height="16" alt=""></td>
       <!--td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_06.gif" width="1" height="16" alt=""></td-->
       <td nowrap valign="middle" background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_07.gif" class="portal-left-title-white"><%= title %></td>
       <td width="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_08.gif" width="4" height="16" alt=""></td>
       <td background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_07.gif" align="right" width="100%">
          <%@ include file="decoration.jsp" %>
       </td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_12.gif" width="15" height="7" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_13.gif" width="100%" height="7" alt=""></td>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_14.gif" width="14" height="7" alt=""></td>
      </tr>
      <tr>
       <td background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_15.gif"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_15.gif" width="15" height="100%" alt=""></td>
       <td colspan="4" bgcolor="#FFFFFF"><%= content %></td>
       <td background="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_17.gif"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_17.gif" width="14" height="100%" alt=""></td>
      </tr>
      <tr>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_18.gif" width="15" height="15" alt=""></td>
       <td colspan="4"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_19.gif" width="100%" height="15" alt=""></td>
       <td><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_20.gif" width="14" height="15" alt=""></td>
      </tr>
      <tr>
       <td colspan="6"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/portlet_left_top_21.gif" width="191" height="1" alt=""></td>
      </tr>
      <tr>
       <td colspan="6"><img src="<%= contextPath %>/nodesk/images/portlet_left_top/spacer.gif" height="1" alt=""></td>
      </tr>
     </table>
     </td>
     </tr>
     </basic:forEachWindowInRegion>
</table>

  </td>
 </tr>
 <% } %>

 <tr>
  <td class="portal-copyright" height="10" colspan="<%= nbCol %>" align="center" valign="top">Powered by <a class="portal-copyright" href="http://www.jboss.com/products/jbossportal">JBoss Portal</a><br>
  Theme by <a class="portal-copyright" href="http://www.nodesk.org">Nodesk</a>
  </td>
 </tr>
</table>
</body>
</html>
