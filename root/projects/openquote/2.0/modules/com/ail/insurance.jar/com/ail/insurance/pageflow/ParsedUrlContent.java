/* Copyright Applied Industrial Logic Limited 2006. All rights Reserved */
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
package com.ail.insurance.pageflow;

import static com.ail.core.Functions.configurationNamespaceToProductName;
import static com.ail.core.Functions.loadUrlContentAsString;
import static com.ail.core.Functions.productNameToConfigurationNamespace;

import java.io.IOException;
import java.net.URL;
import java.util.Collection;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.CoreProxy;
import com.ail.core.Type;
import com.ail.insurance.pageflow.render.RenderService.RenderCommand;
import com.ail.insurance.policy.Policy;

/**
 * A PageElement which contains content read from an arbitrary URL. The content is parsed
 * for embedded JXPath expressions and the expressions evaluated against the {@com.ail.openquote.Policy Policy} 
 * and the results of the evaluation inserted into the content.</br><br/>
 * For example, if the content returned included:<br/><br/>
 * &nbsp;&nbsp;"The proposer's name is: &lt;b&gt;${proposer/forname}&lt;/b&gt;"<br/><br/>
 * Then the rendered output might read: <br/><br/>
 * &nbsp;&nbsp;"The proposer's name is: <b>Jimbo</b>".
 * <p>Note: This element takes no special steps to ensure that the content read is suitable for use within
 * the PageFlow or in a portal context.</p>
 */
public class ParsedUrlContent extends PageElement {
    private static final long serialVersionUID = -4810599045554021748L;

    /** URL to read content from. */
    private String url;

    /**
     * Default constructor
     */
    public ParsedUrlContent() {
        super();
	}

    /**
     * URL to read raw content from.
     * @return URL to read from 
     */
    public String getUrl() {
        return url;
    }

    /**
     * @see #getUrl()
     * @param url URL to read content from.
     */
    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Type applyRequestValues(ActionRequest request, ActionResponse response, Type model) {
    	return model;
    }

	@Override
    public boolean processValidations(ActionRequest request, ActionResponse response, Type model) {
        return false;
    }

	@Override
	public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        String productName=null;
        boolean success=false;
        String content=null;
        
    	if (conditionIsMet(model)) {
        	Policy quote=(com.ail.insurance.policy.Policy)model;
    
            CoreProxy cp=new CoreProxy(productNameToConfigurationNamespace(quote.getProductTypeId()));
            Collection<String> namespaces=cp.getConfigurationNamespaceParent();
            
            for(String namespace: namespaces) {
                try {
                    if (namespace.endsWith("Registry")) {
                        productName=configurationNamespaceToProductName(namespace);
                        content=expand(loadUrlContentAsString(new URL(expandRelativeUrlToProductUrl(getUrl(), request, productName))), quote, quote);
                        success=true;
                        break;
                    }
                }
                catch(IOException e) {
                    // next! This just indicates that we didn't find it in this namespace
                }
            }
            
            if (!success) {
                content="ERROR: Content not found for:"+getUrl();
            }
            
            RenderCommand command = buildRenderCommand("ParsedUrlContent", request, response, model);
            command.setContentArg(content);
            model = invokeRenderCommand(command);
    	}

    	return model;
	}
}
