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
package com.ail.openquote.ui;

import static com.ail.openquote.ui.messages.I18N.i18n;
import static com.ail.openquote.ui.util.Functions.hideNull;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;

import com.ail.core.Type;
import com.ail.insurance.policy.AssessmentLine;
import com.ail.insurance.policy.AssessmentNote;
import com.ail.insurance.policy.AssessmentSheet;
import com.ail.insurance.policy.CalculationLine;
import com.ail.insurance.policy.FixedSum;
import com.ail.insurance.policy.Marker;
import com.ail.insurance.policy.RateBehaviour;
import com.ail.insurance.policy.Section;
import com.ail.insurance.policy.SumBehaviour;
import com.ail.openquote.Quotation;

/**
 * <p>Page element to display the contents of a quotation's assessment sheet(s) in a tabular format.</p>
 * <p><img src="doc-files/AssessmentSheetDetails.png"/></p>
 * <p>The rendered details (as shown above) include all of the assessment sheet line types: notes, calculations, 
 * markers (referrals, declines etc). All the assessment sheets defined in the quotation are rendered, starting
 * with the sheet held at the quote level, then for each of the quote's sections. The table also includes 
 * summary information: quote number, total premium, status, etc.</p>
 * <p>In the above example the policy has just one section (MotorPlusSection) which has no associated lines. All
 * section's assessment sheets are included even those that are empty.</p>
 */
public class AssessmentSheetDetails extends PageElement {
	private static final long serialVersionUID = -4810599045554021748L;
    private static String cellStyle="class='portlet-font' style='border: 1px solid gray;'";
    
	public AssessmentSheetDetails() {
		super();
	}

    @Override
    public Type renderResponse(RenderRequest request, RenderResponse response, Type model) throws IllegalStateException, IOException {
        PrintWriter w=response.getWriter();
        
        render(w, model);
        
        return model;
    }

    /**
     * Render the assessment sheet as an HTML table into a specified PrintWriter. AssessmentSheetDetail is a {@link PageElement}
     * so it can be used within a {@link PageFlow}, though this isn't typical. It is used in broker notification emails and
     * from the product development sandpit portlet {@link com.ail.openquote.portlet.SandpitPortlet}.
     * @param w Render HTML is written here.
     * @param model Quote to render the assessment sheet for
     */
    public void render(PrintWriter w, Type model) {
        Quotation quote=(Quotation)model;

        w.printf("<table width='100%%'>");
        w.printf(  "<tr width='100%%'>");
        w.printf(    "<td>");
        w.printf(      "<table width='100%%' class='portlet-section-header'");
        w.printf(        "<tr width='100%%'>");
        w.printf(          "<td>"+i18n("i18n_assessment_sheet__details_title")+"</td>", quote.getQuotationNumber());
        w.printf(          "<td align='right'>");
        w.printf(            "<table>");
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_product_title")+"</td><td class='portlet-font'>%s</td></tr>", quote.getProductName());
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_status_title")+"</td><td class='portlet-font'>%s</td></tr>", quote.getStatusAsString());
        w.printf(              "<tr><td class='portlet-font'>"+i18n("i18n_assessment_sheet_details_total_premium_title")+"</td><td class='portlet-font'>%s</td></tr>", totalPremium(quote));
        w.printf(            "</table>");
        w.printf(          "</td>");
        w.printf(        "</tr>");
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf(  "<tr>");
        w.printf(    "<td height='10'></td>");
        w.printf(  "</tr>");
        w.printf(  "<tr>");
        w.printf(    "<td colspan='2'>");
        w.printf(      "<table width='100%%' style='border-collapse: collapse;'>");

        renderAssessmentSheet(w, i18n("i18n_assessment_sheet_details_policy_title"), quote.getAssessmentSheet());

        w.printf(  "<tr>");
        w.printf(    "<td height='10'></td>");
        w.printf(  "</tr>");

        for(Section s: quote.getSection()) {
            renderAssessmentSheet(w, i18n("i18n_assessment_sheet_details_section_title")+" "+s.getSectionTypeId(), s.getAssessmentSheet());
        }
        
        w.printf(      "</table>");
        w.printf(    "</td>");
        w.printf(  "</tr>");
        w.printf("</table>");
    }

    private void renderAssessmentSheet(PrintWriter w, String title, AssessmentSheet sheet) {
        w.printf("<tr>");
        w.printf(  "<td colspan='6' style='border: 1px solid gray;' class='portlet-section-selected'><b>"+i18n("i18n_assessment_sheet_section_title")+"</b></td>", title);
        w.printf("</tr>");

        renderNotesLines(w, sheet);

        renderCalculationLines(w, sheet);

        renderMarkerLines(w, sheet);
    }

    private void renderNotesLines(PrintWriter w, AssessmentSheet sheet) {
        w.printf("<tr>");
        w.printf(  "<td colspan='6' style='border: 1px solid gray;' colspan='6' height='5'></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_notes_title")+"</b></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
        w.printf("</tr>");

        ArrayList<AssessmentNote> sortedLines=new ArrayList<AssessmentNote>(sheet.getLinesOfType(AssessmentNote.class).values());
        Collections.sort(sortedLines);

        if (sortedLines.size()!=0) {
            for(AssessmentNote note: sortedLines) {
                w.printf("</tr>");
                w.printf(  "<td "+cellStyle+">%s</td>", note.getId());
                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", note.getReason());
                w.printf(  "<td "+cellStyle+">%s</td>", note.getOrigin());
                w.printf(  "<td "+cellStyle+">&nbsp</td>");
                w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
                w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
                w.printf("</tr>");
            }
        }
        else {
            w.printf("</tr>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">&nbsp</td>");
            w.printf(  "<td class='portlet-font' align='right' style='border: 1px solid gray;'>&nbsp;</td>");
            w.printf(  "<td class='portlet-font' align='right' style='border: 1px solid gray;'>&nbsp;</td>");
            w.printf("</tr>");
        }
    }

    private void renderCalculationLines(PrintWriter w, AssessmentSheet sheet) {
        w.printf("<tr>");
        w.printf(  "<td colspan='6' style='border: 1px solid gray;' height='5'></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_calculations_title")+"</b></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_type_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader' align='center'>"+i18n("i18n_assessment_sheet_rate_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader' align='center'>"+i18n("i18n_assessment_sheet_amount_title")+"</td>");
        w.printf("</tr>");
 
        ArrayList<CalculationLine> sortedLines=new ArrayList<CalculationLine>(sheet.getLinesOfType(CalculationLine.class).values());

        Collections.sort(sortedLines, new Comparator<AssessmentLine>() {
            public int compare(AssessmentLine a1, AssessmentLine a2) {
                if (!a1.getOrigin().equals(a2.getOrigin())) {
                    if ("AssessRisk".equals(a1.getOrigin())) {
                        return -1;
                    }
                    if ("CalculatePremium".equals(a2.getOrigin())) {
                        return -1;
                    }
                }
                return a1.getCalculatedOrder()-a2.getCalculatedOrder();
            }
        });
        
        if (sortedLines.size()!=0) {
            for(CalculationLine line: sortedLines) {
                w.printf("<tr>");
                w.printf(  "<td class='%s' style='border: 1px solid gray;'>%s</td>", line.getId().matches("[0-9A-F]{8}") ? "portlet-font-dim" : "portlet-font", line.getId());
                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", line.getReason());
                w.printf(  "<td "+cellStyle+">%s</td>", line.getOrigin());
                w.printf(  "<td "+cellStyle+">%s</td>", calculationLineType(line));
                w.printf(  "<td "+cellStyle+" align='right'>%s</td>", calculationLineRate(line));
                w.printf(  "<td "+cellStyle+" align='right'>%s</td>", line.getAmountAsString());
                w.printf("</tr>");
            }
        }
        else {
            w.printf("<tr>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+" align='right'>-</td>");
            w.printf(  "<td "+cellStyle+" align='right'>-</td>");
            w.printf("</tr>");
        }
    }

    private void renderMarkerLines(PrintWriter w, AssessmentSheet sheet) {
        w.printf("<tr>");
        w.printf(  "<td colspan='6' style='border: 1px solid gray;' height='5'></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' colspan='6' class='portlet-section-header'><b>"+i18n("i18n_assessment_sheet_markers_title")+"</b></td>");
        w.printf("</tr>");
        w.printf("<tr>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_id_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' width='40%%' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_description_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_source_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>"+i18n("i18n_assessment_sheet_type_title")+"</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
        w.printf(  "<td style='border: 1px solid gray;' class='portlet-section-subheader'>&nbsp;</td>");
        w.printf("</tr>");
        
        ArrayList<Marker> sortedLines=new ArrayList<Marker>(sheet.getLinesOfType(Marker.class).values());
        Collections.sort(sortedLines);

        if (sortedLines.size()!=0) {
            for(Marker marker: sortedLines) {
                w.printf("</tr>");
                w.printf(  "<td "+cellStyle+">%s</td>", marker.getId());
                w.printf(  "<td width='40%%' "+cellStyle+">%s</td>", marker.getReason());
                w.printf(  "<td "+cellStyle+">%s</td>", marker.getOrigin());
                w.printf(  "<td "+cellStyle+">%s</td>", marker.getType());
                w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
                w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
                w.printf("</tr>");                
            }
        }
        else {
            w.printf("</tr>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td width='40%%' "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+">-</td>");
            w.printf(  "<td "+cellStyle+" align='right'>&nbsp;</td>");
            w.printf(  "<td "+cellStyle+" align='right'>&nbsp</td>");
            w.printf("</tr>");
        }
    }

    private String totalPremium(Quotation quote) {
        try {
            return quote.getTotalPremium().toString();
        }
        catch(Throwable e) {
            return "-";
        }
    }

    private String calculationLineRate(CalculationLine line) {
        try {
            return ((RateBehaviour)line).getRate().getRate();
        }
        catch(ClassCastException e) {
            return "&nbsp;";
        }
    }
    
    private String calculationLineType(CalculationLine line) {
        if (line instanceof RateBehaviour) {
            RateBehaviour r=(RateBehaviour)line;
            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
                                   "<tr><td class='portlet-font' rowspan='2'>%s</td><td align='right' class='portlet-section-footer'>%s</td></tr>"+
                                   "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
                                 "</table>", 
                                 r.getType().getLongName(), 
                                 r.getDependsOn(), 
                                 r.getContributesTo());
        }
        else if (line instanceof FixedSum) {
            FixedSum f=(FixedSum)line;
            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
                                   "<tr><td class='portlet-font' rowspan='2'>"+i18n("i18n_assessment_sheet_fixedsum_title")+"</td><td align='right' class='portlet-section-footer'>&nbsp;</td></tr>"+
                                   "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
                                 "</table>", 
                                 hideNull(f.getContributesTo()));
        }
        else if (line instanceof SumBehaviour) {
            SumBehaviour s=(SumBehaviour)line;
            return String.format("<table width='100%%' style='border-collapse: collapse;'>"+
                                    "<tr><td class='portlet-font' rowspan='2'>%s</td><td align='right' class='portlet-section-footer'>&nbsp;</td></tr>"+
                                    "<tr><td align='right' class='portlet-section-footer'>%s</td></tr>"+
                                  "</table>", 
                                  s.getType().getLongName(), 
                                  s.getContributesTo());
        }
        else {
            return "";
        }
    }
}
