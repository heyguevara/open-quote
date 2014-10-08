<%@ page import="org.jboss.portal.server.PortalConstants" %>
<%@ taglib uri="/WEB-INF/theme/portal-layout.tld" prefix="p" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
   <title>OpenQuote @major.version@.@minor.version@@increment.version@</title>
   <meta http-equiv="Content-Type" content="text/html;"/>
   <!-- to correct the unsightly Flash of Unstyled Content. -->
   <script type="text/javascript"></script>
   <!-- inject the theme, default to the Renaissance theme if nothing is selected for the portal or the page -->
   <p:theme themeName="renaissance"/>
   <!-- insert header content that was possibly set by portlets on the page -->
   <p:headerContent/>
</head>

<body id="body">
<p:region regionName='AJAXScripts' regionID='AJAXScripts'/>
<%@include file="/layouts/common/modal.jsp"%>
<div id="portal-container">
   <div id="sizer">
      <div id="expander">
         <div id="logoName"></div>
         <table border="0" cellpadding="0" cellspacing="0" id="header-container">
            <tr>
               <td align="center" valign="top" id="header">
									&nbsp;
               </td>
            </tr>
         </table>
         <div id="content-container">
            <table width="100%">
              <tr>
                <td valign="top">
                  <!-- insert the content of the 'center' region of the page, and assign the css selector id 'regionB' -->
                  <p:region regionName='center' regionID='center'/>
                </td>
              </tr>
            </table>
            <hr class="cleaner"/>
         </div>
      </div>
   </div>
</div>

<!-- TODO: Fix the auto jump in this tag -->
<div id="footer-container" class="portal-copyright">Powered by
<a class="portal-copyright" href="http://openquote.opensourceinsurance.org">OpenQuote Community @major.version@.@minor.version@@increment.version@ build @build.number@</a><br/>
</div>

<p:region regionName='AJAXFooter' regionID='AJAXFooter'/>

<script type="text/javascript">
var gaJsHost = (("https:" == document.location.protocol) ? "https://ssl." : "http://www.");
document.write(unescape("%3Cscript src='" + gaJsHost + "google-analytics.com/ga.js' type='text/javascript'%3E%3C/script%3E"));
</script>
<script type="text/javascript">
var pageTracker = _gat._getTracker("UA-1690094-6");
pageTracker._initData();
pageTracker._trackPageview();
</script>

</body>
</html>
