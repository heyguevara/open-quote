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
package com.ail.openquote.portlet;

import static javax.faces.event.PhaseId.RENDER_RESPONSE;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;

public class CatalogPhaseListener implements PhaseListener {
    private static final long serialVersionUID = 1L;

    public void afterPhase(PhaseEvent event) {
        // do nothing
    }

    public void beforePhase(PhaseEvent event) {
        FacesContext ctx=FacesContext.getCurrentInstance();
        CatalogBean catalog=(CatalogBean)ctx.getApplication().getVariableResolver().resolveVariable(ctx, "catalog");
        catalog.refresh();
    }

    public PhaseId getPhaseId() {
        return RENDER_RESPONSE;
    }
}
