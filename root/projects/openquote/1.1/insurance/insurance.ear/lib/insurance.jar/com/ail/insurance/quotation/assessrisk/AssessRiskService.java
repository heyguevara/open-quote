/* Copyright Applied Industrial Logic Limited 2002. All rights Reserved */
/*
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later 
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or 
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License for
 * more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51 
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */

package com.ail.insurance.quotation.assessrisk;

import com.ail.core.BaseException;
import com.ail.core.Core;
import com.ail.core.CoreProxy;
import com.ail.core.Functions;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Version;
import com.ail.core.VersionEffectiveDate;
import com.ail.core.command.CommandArg;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.Policy;
import com.ail.insurance.policy.PolicyStatus;
import com.ail.insurance.policy.Section;

/**
 * @version $Revision: 1.7 $
 * @state $State: Exp $
 * @date $Date: 2007/06/10 11:05:59 $
 * @source $Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessRiskService.java,v $
 * @stereotype service
 */
public class AssessRiskService extends Service {
    private static final long serialVersionUID = 7260448770297048139L;
    private AssessRiskArg args = null;
    private Core core = null;

    /** Default constructor */
    public AssessRiskService() {
        core = new Core(this);
    }

    /**
     * Getter to fetch the entry point's code. This method is demanded by the EntryPoint class.
     * @return This entry point's instance of Core.
     */
    public Core getCore() {
        return core;
    }

    /**
     * Fetch the version of this entry point.
     * @return A version object describing the version of this entry point.
     */
    public Version getVersion() {
        com.ail.core.Version v = (com.ail.core.Version) core.newType("Version");
        v.setCopyright("Copyright Applied Industrial Logic Limited 2002. All rights reserved.");
        v.setDate("$Date: 2007/06/10 11:05:59 $");
        v.setSource("$Source: /home/bob/CVSRepository/projects/insurance/insurance.ear/insurance.jar/com/ail/insurance/quotation/assessrisk/AssessRiskService.java,v $");
        v.setState("$State: Exp $");
        v.setVersion("$Revision: 1.7 $");
        return v;
    }

    /**
     * Return the date for which rules etc should be used.
     * todo This should come from someplace in the policy.
     * @return Date to define rules to be used etc.
     */
    public VersionEffectiveDate getVersionEffectiveDate() {
        return core.getVersionEffectiveDate();
    }

    /**
     * Return the product type id of the policy we're assessing the risk for as the
     * configuration namespace. The has the effect of selecting the product's configuration.
     * @return product type id
     */
    public String getConfigurationNamespace() {
        return core.getConfigurationNamespace();
    }
    
    /**
     * Setter used to the set the entry points arguments.
     * @param args for invoke
     */
    public void setArgs(CommandArg args) {
        this.args = (AssessRiskArg)args;
    }

    /**
     * Getter returning the arguments used by this entry point.
     * @return An instance of $Name:  $Args.
     */
    public CommandArg getArgs() {
        return args;
    }

    /**
     * Assess risks logic is pretty simple, all of the actual risk assessment logic is
     * handled by business rules. The steps are as follows:
     * <ol>
     * <li>Check the precondition:<li>
     *    <ol>
     *    <li>We must have been given a policy (policy!=null)</li>
     *    <li>The policy must have a status of Application (policy.status=Application)</li>
     *    <li>The policy must have a productTypeId (policy.productTypeId!=null && policy.productTypeId.length>0)</li>
     *    </ol>
     *  <li>Loop through each of the sections in the policy, and:</li>
     *   <ol>
     *   <li>Check that the section has a SectionTypeId, throw a PreconditionException if it doesn't.</li>
     *   <li>Load the service called &lt;ProductTypeID&gt;/&lt;SectionTypeID&gt;</li>
     *   <li>Create a AssessmentSheet object.</li>
     *   <li>Pass the policy, section, and assessmentSheet into the rule.</li>
     *   <li>Invoke the rule.</li>
     *   <li>Pull the assessmentSheet out of the rule and add it to the section.</li>
     *   </ol>
     *  <li>Load the service called &lt;ProductTypeID&gt;</li>
     *  <li>Create a AssessmentSheet object.</li>
     *  <li>Pass the policy, and assessmentSheet into the rule.</li>
     *  <li>Invoke the rule.</li>
     *  <li>Pull the assessmentSheet out of the rule and add it to the policy.</li>
     * </ol>
     */
    public void invoke() throws BaseException {
        Policy policy=args.getPolicyArgRet();
        AssessmentSheet as;

        // Check that we have been given a policy
        if (policy==null) {
            throw new PreconditionException("policy==null");
        }

        // Check that the policy has a productTypeID - we'll need it to build rule names.
        if (policy.getProductTypeId()==null || policy.getProductTypeId().length()==0) {
            throw new PreconditionException("policy.productTypeId==null || policy.productTypeId==\"\"");
        }

        // Make sure the policy has a status of Application
        if (policy.getStatus()==null || !policy.getStatus().equals(PolicyStatus.APPLICATION)) {
            throw new PreconditionException("policy.status==null || policy.status!=Application");
        }

        String namespace=Functions.productNameToConfigurationNamespace(args.getPolicyArgRet().getProductTypeId());
        core=new CoreProxy(namespace, args.getCallersCore()).getCore();

        // Loop through each section
        for(Section section: policy.getSection()) {

            // Make sure the section has a SectionType - we'll need it to build rule names
            if (section.getSectionTypeId()==null || section.getSectionTypeId().length()==0) {
                throw new PreconditionException("policy.section[id="+section.getId()+"].sectionTypeId==null || policy.section[id="+section.getId()+"].sectionTypeId==\"\"");
            }

            // Get the assessment sheet.
            if (section.getAssessmentSheet()==null) {
                // The section doesn't have one yet, so create it.
                as=new AssessmentSheet();
            }
            else {
                // The section has one, so us it - after clearing out the old entries.
                as=section.getAssessmentSheet();
                as.removeLinesByOrigin("AssessRisk");
            }

            // Make up the rule name <ProductType>.<SectionType>
            String ruleName="AssessSectionRisk/"+section.getSectionTypeId();

            // Load the rule, and populate it with arguments
            AssessSectionRiskCommand rule=(AssessSectionRiskCommand)core.newCommand(ruleName);
            rule.setCoreArg(core);
            rule.setPolicyArg(policy);
            rule.setSectionArg(section);
            rule.setAssessmentSheetArgRet(as);

            // Lock the sheet to us, run the rules, and unlock the sheet.
            rule.getAssessmentSheetArgRet().setLockingActor("AssessRisk");
            rule.invoke();
            rule.getAssessmentSheetArgRet().clearLockingActor();

            // Pull the AssessmentSheet out or rules and add it to the section.
            section.setAssessmentSheet(rule.getAssessmentSheetArgRet());
        }

        // Get the AssessmentSheet for the policy
        if (policy.getAssessmentSheet()==null) {
            // The policy doesn't have one yet, so create it.
            as=new AssessmentSheet();
        }
        else {
            // The policy has one already, so clear out our old enties and use it.
            as=policy.getAssessmentSheet();
            as.removeLinesByOrigin("AssessRisk");
        }

        // Load the rule (name=<ProductType>), and populate it with args
        AssessPolicyRiskCommand rule=(AssessPolicyRiskCommand)core.newCommand("AssessPolicyRisk");
        rule.setCoreArg(core);
        rule.setPolicyArg(policy);
        rule.setAssessmentSheetArgRet(as);

        // invoke the rule
        rule.getAssessmentSheetArgRet().setLockingActor("AssessRisk");
        rule.invoke();
        rule.getAssessmentSheetArgRet().clearLockingActor();

        // Pull the premium calcuation table out of the results and add it to the policy
        policy.setAssessmentSheet(rule.getAssessmentSheetArgRet());
    }
}


