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
package com.ail.core.document.model;

import static com.ail.core.document.model.Placement.FOOTER;

public class FooterData extends BlockData {
    private String leftLogo=null;
    private String rightLogo=null;

    public FooterData() {
        setPlacement(FOOTER);
    }

    @Override
    public void render(RenderContext context) {
        context.getOutput().printf("<footerData%s%s%s%s applicability=\"%s\">", 
                                                    idAsAttribute(), titleAsAttribute(), styleClassAsAttribute(), orderAsAttribute(),
                                                    getApplicabilityAsString());
        if (leftLogo!=null) {
            context.getOutput().printf("<leftLogo>%s</leftLogo>", getLeftLogo());
        }
        
        if (rightLogo!=null) {
            context.getOutput().printf("<rightLogo>%s</rightLogo>", getRightLogo());
        }
        
        for(ItemData it: getItem()) {
            it.render(context);
        }

        context.getOutput().println("</footerData>");
    }

    /**
     * @return the leftLogo
     */
    public String getLeftLogo() {
        return leftLogo;
    }

    /**
     * @param leftLogo the leftLogo to set
     */
    public void setLeftLogo(String leftLogo) {
        this.leftLogo = leftLogo;
    }

    /**
     * @return the rightLogo
     */
    public String getRightLogo() {
        return rightLogo;
    }

    /**
     * @param rightLogo the rightLogo to set
     */
    public void setRightLogo(String rightLogo) {
        this.rightLogo = rightLogo;
    }
}
