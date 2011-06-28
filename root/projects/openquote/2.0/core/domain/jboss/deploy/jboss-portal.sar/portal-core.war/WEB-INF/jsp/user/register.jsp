<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>
<portlet:defineObjects/>

<p><span class="requiredfield">*</span>&nbsp;${n:i18n("REQUIRED")}</p>
<hr/>
<table width="100%" cellpadding="0" cellspacing="10" border="0" class="pn-normal">
   <form name="<portlet:namespace/>register"
         action="<portlet:actionURL><portlet:param name="op" value="userRegister"/></portlet:actionURL>" method="post">
      <tr>
         <td colspan="3" nowrap><h4>${n:i18n("REGISTER_BASICINFO")}</h4></td>
      </tr>
      <tr>

         <td class="portlet-form-label" nowrap>${n:i18n("USERNAME")}<span class="requiredfield">*</span></td>
         <td colspan="2"><input type="text" name="uname" value="${param["USERNAME"]}"
                                size="30" maxlength="25">
            <n:error key="uname_error"/>
         </td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("REGISTER_REALEMAIL")}<span class="requiredfield">*</span>
         </td>
         <td><input type="text" name="realemail" value="user@portal.com" size="30" maxlength="60">
            <n:error key="realemail_error"/>
         </td>

         <td><input type="checkbox" name="viewrealemail" value="true" checked="checked" class="portlet-form-label"/>Allow
            other users to view my real e-mail address
         </td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("REGISTER_FAKEEMAIL")}</td>
         <td colspan="2"><input type="text" name="fakeemail" value="" size="30" maxlength="60"></td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("PASSWORD")}<span class="requiredfield">*</span></td>

         <td colspan="2"><input type="password" name="pass1" size="30" maxlength="20">
            <n:error key="pass1_error"/>
         </td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("REGISTER_PASSWORDAGAIN")}<span
            class="requiredfield">*</span></td>
         <td colspan="2"><input type="password" name="pass2" size="30" maxlength="20">
            <n:error key="pass2_error"/>
         </td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("REGISTER_QUESTION")}</td>

         <td class="pn-normal" colspan="2"><input type="text" name="question" value="" size="30" maxlength="100">
         </td>
      </tr>
      <tr>
         <td class="portlet-form-label" nowrap>${n:i18n("REGISTER_ANSWER")}</td>
         <td class="pn-normal" colspan="2"><input type="text" name="answer" value="" size="30" maxlength="100">
         </td>
      </tr>
      <tr>
         <td colspan="3" class="bottombuttonbar">
            <input name="Save" class="portlet-form-button" id="register" type="submit"
                   value="${n:i18n("REGISTER_NEWUSER")}"/>
            &nbsp;
            <input name="Cancel" class="portlet-form-button" id="Cancel" type="submit"
                   value="${n:i18n("Cancel")}"/>
         </td>
      </tr>
   </form>
</table>