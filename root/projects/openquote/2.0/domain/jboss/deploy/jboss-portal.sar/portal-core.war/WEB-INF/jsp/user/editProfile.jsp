<%@ page language="java" extends="org.jboss.portal.core.servlet.jsp.PortalJsp" %>
<%@ taglib uri="/WEB-INF/portal-lib.tld" prefix="n" %>
<%@ taglib uri="http://java.sun.com/portlet" prefix="portlet" %>
<%@ page isELIgnored="false" %>

<p><span class="requiredfield">*</span>&nbsp;${n:i18n("REQUIRED")}</p>

<hr/>

<table cellpadding="0" cellspacing="8" border="0" class="pn-normal">
<form name="<portlet:namespace/>save"
      action="<portlet:actionURL><portlet:param name="op" value="storeProfile"/></portlet:actionURL>" method="post">
<input type="hidden" name="userid" value="${n:out("userid")}"/>
<tr>
   <td colspan="3" nowrap><h4>${n:i18n("REGISTER_BASICINFO")}</h4></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_GIVENNAME")}</td>
   <td><input type="text" name="givenname" value="${n:out("GIVENNAME")}" size="30" maxlength="60"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_FAMILYNAME")}</td>
   <td><input type="text" name="familyname" value="${n:out("FAMILYNAME")}" size="30" maxlength="60"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_REALEMAIL")}<span class="requiredfield">*</span></td>
   <td><input type="text" name="realemail" value="${n:out("REALEMAIL")}" size="30" maxlength="60">
      <n:error key="realemail_error"/>
   </td>
   <td></td>
</tr>
<tr>
   <td>&nbsp;</td>
   <td><span class="portlet-form-label">${n:i18n("REGISTER_EMAILNOTPUBLIC")}</span></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_FAKEEMAIL")}</td>
   <td><input type="text" name="fakeemail" value="${n:out("FAKEEMAIL")}" size="30" maxlength="60">
      <n:error key="fakeemail_error"/>
   </td>
</tr>
<tr>
   <td>&nbsp;</td>
   <td><span class="portlet-form-label">${n:i18n("REGISTER_EMAILPUBLIC")}</span></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_VIEWREALEMAIL")}</td>
   <td><input type="checkbox" name="viewrealemail" value="true" ${n:out("VIEWREALEMAIL")}/></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_HOMEPAGE")}</td>
   <td><input type="text" name="homepage" value="${n:out("HOMEPAGE")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_LANGUAGE")}</td>
   <td>
      <select name="locale" class="pn-normal">
         <n:iterate ctx="locale">
            <option value="${n:out("locale.id")}" ${n:out("locale.selected")}>${n:out("locale.name")}</option>
         </n:iterate>
      </select>
   </td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_TIMEZONEOFFSET")}</td>
   <td>
      <select name="timezoneoffset" class="pn-normal">
         <n:iterate ctx="timezone">
            <option value="${n:out("timezone.id")}" ${n:out("timezone.selected")}>${n:out("timezone.name")}</option>
         </n:iterate>
      </select>
   </td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_THEME")}</td>
   <td>
      <select name="theme" class="pn-normal">
         <option value="" selected>${n:i18n("REGISTER_DEFAULT_THEME")}</option>
         <n:iterate ctx="theme">
            <option value="${n:out("theme.id")}" ${n:out("theme.selected")}>${n:out("theme.id")}</option>
         </n:iterate>
      </select>
   </td>
</tr>
<%--tr>
   <td>${n:i18n("REGISTER_YOURAVATAR")}</td>
   <td>
      <select name="avatar" class="pn-normal">${param["AVATARS"]}</select>
      &nbsp;&nbsp;<img src="modules/user/images/avatars/{AVATAR}" name="avatar" width="32" height="32" alt="" align="top">
   </td>
</tr>
<tr>
   <td>${n:i18n("REGISTER_MESSAGEORDER")}</td>
   <td class="pn-normal">
      <div><input type="radio" name="sort_order_desc" value="{ORDER_VALUE_1}" checked="checked"/>{ORDER_LABEL_1}</div>
      <div><input type="radio" name="sort_order_desc" value="{ORDER_VALUE_2}"/>{ORDER_LABEL_2}</div>
   </td>
</tr--%>
<tr>
   <td colspan="3" nowrap><h4>${n:i18n("REGISTER_IM")}</h4></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_ICQ")}</td>
   <td><input type="text" name="icq" value="${n:out("ICQ")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_AIM")}</td>
   <td><input type="text" name="aim" value="${n:out("AIM")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_YIM")}</td>
   <td><input type="text" name="yim" value="${n:out("YIM")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_MSNM")}</td>
   <td><input type="text" name="msnm" value="${n:out("MSNM")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_SKYPE")}</td>
   <td><input type="text" name="skype" value="${n:out("SKYPE")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_XMMP")}</td>
   <td><input type="text" name="xmmp" value="${n:out("XMMP")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td colspan="3" nowrap><h4>${n:i18n("REGISTER_ADDITIONAL")}</h4></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_LOCATION")}</td>
   <td><input type="text" name="location" value="${n:out("LOCATION")}" size="30" maxlength="100"></td>
</tr>
<!--tr>
            <td>${n:i18n("REGISTER_LANGUAGE")}</td>
            <td>
               <select name="language" class="pn-normal">${param["LANGUAGE"]}</select>
            </td>
         </tr-->
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_OCCUPATION")}</td>
   <td><input type="text" name="occupation" value="${n:out("OCCUPATION")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_INTERESTS")}</td>
   <td><input type="text" name="interests" value="${n:out("INTERESTS")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_SIGNATURE")}</td>
   <td><textarea wrap="virtual" cols="50" rows="5" name="signature" class="pn-normal">${n:out("SIGNATURE")}</textarea>
   </td>
</tr>
<tr>
   <td>&nbsp;</td>
   <td class="portlet-form-label">${n:i18n("255MAX")}</td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_EXTRAINFO")}</td>
   <td><textarea wrap="virtual" cols="50" rows="5" name="extra" class="pn-normal">${n:out("EXTRA")}</textarea></td>
</tr>
<tr>
   <td>&nbsp;</td>
   <td class="portlet-form-label">${n:i18n("REGISTER_CANKNOWABOUT")}</td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("PASSWORD")}</td>
   <td><input type="password" name="pass1" size="10" maxlength="20">&nbsp;
      <n:error key="pass1_error"/>
      &nbsp;<input type="password" name="pass2" size="10" maxlength="20">&nbsp;<span
      class="portlet-form-label">${n:i18n("REGISTER_PASSWORDAGAIN")}</span>
      <n:error key="pass2_error"/>
   </td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_QUESTION")}</td>
   <td class="pn-normal"><input type="text" name="question" value="${n:out("QUESTION")}" size="30" maxlength="100">
   </td>
</tr>
<tr>
   <td class="portlet-form-label">${n:i18n("REGISTER_ANSWER")}</td>
   <td class="pn-normal"><input type="text" name="answer" value="${n:out("ANSWER")}" size="30" maxlength="100"></td>
</tr>
<tr>
   <%--<td colspan="3" class="bottombuttonbar"><input class="portlet-form-button" type="submit"
                                                  value="${n:i18n("REGISTER_SAVECHANGES")}">
      <span class="portlet-font"><a
         href="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showMenu"/></portlet:renderURL>">${n:i18n("Cancel")}</a></span>
   </td>--%>
</tr>
</form>
<tr>
   <td colspan="3">
      <input name="Save" class="portlet-form-button" id="Assign" type="submit"
             onclick="document.forms['<portlet:namespace/>save'].submit();"
             value="${n:i18n("REGISTER_SAVECHANGES")}"/>
      &nbsp;
      <input name="Cancel" class="portlet-form-button" id="Cancel" type="submit"
             onclick="document.forms['<portlet:namespace/>cancel'].submit();"
             value="${n:i18n("Cancel")}"/>
</tr>
<form
   name="<portlet:namespace/>cancel"
   action="<portlet:renderURL windowState="normal"><portlet:param name="op" value="showMenu"/></portlet:renderURL>"
   method="post"/>
</table>