/* Copyright Applied Industrial Logic Limited 2003. All rights Reserved */
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

package com.ail.core.document;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import com.ail.annotation.ServiceImplementation;
import com.ail.core.PostconditionException;
import com.ail.core.PreconditionException;
import com.ail.core.Service;
import com.ail.core.Type;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfStamper;
        

/**
 * This class provides an implementation of the render document service which renders to PDF
 * using iText.<p/>
 */
@ServiceImplementation
public class RenderItextPdfDocumentService extends Service<RenderDocumentService.RenderDocumentArgument> {

    @Override
    public void invoke() throws PreconditionException, PostconditionException, RenderException {
        if (args.getSourceDataArg()==null) {
            throw new PreconditionException("args.getSourceDataArg()==null");
        }
        
        if (args.getTemplateUrlArg()==null || args.getTemplateUrlArg().length()==0) {
            throw new PreconditionException("args.getTemplateUrlArg()==null || args.getTemplateUrlArg().length()==0");
        }
        
        // The resulting PDF will end up in this array
        ByteArrayOutputStream output=new ByteArrayOutputStream();
        PdfStamper stamper = null;
        PdfReader pdfTemplate = null;
        Type data = null;
        
        try {
            data=(Type)core.fromXML(args.getSourceDataArg().getType(), args.getSourceDataArg());
        } catch (Exception e) {
            throw new PreconditionException("Failed to translate source data into an object.", e);
        }
        
        try {
            pdfTemplate = new PdfReader(new URL(args.getTemplateUrlArg()));
            stamper = new PdfStamper(pdfTemplate, output);
    
            for(String fieldName: stamper.getAcroFields().getFields().keySet()) {
                try {
                    String value=data.xpathGet(fieldName).toString();
                    stamper.getAcroFields().setField(fieldName, value);
                } catch (Exception e) {
                    throw new PreconditionException("xpath expression: '"+fieldName+"' could not be evaulated: "+e.toString());
                }
            }
    
            stamper.setFormFlattening(true);
            stamper.close();
            pdfTemplate.close();
        }
        catch (DocumentException e) {
            throw new RenderException("PDF generation failed: " + e);
        } catch (MalformedURLException e) {
            throw new RenderException("Failed to read template: '"+args.getTranslationUrlArg()+"' "+e);
        } catch (IOException e) {
            throw new RenderException("IO Exception during document render: "+e);
        }
        finally {
            if (stamper!=null) {
                try {
                    stamper.close();
                } catch (Exception e) {
                    // Ignored
                }
            }
        }
        
        args.setRenderedDocumentRet(output.toByteArray());
        
        if (args.getRenderedDocumentRet()==null || args.getRenderedDocumentRet().length==0) {
            throw new PostconditionException("args.getRenderedDocumentRet()==null || args.getRenderedDocumentRet().length==0");
        }
    }
}

