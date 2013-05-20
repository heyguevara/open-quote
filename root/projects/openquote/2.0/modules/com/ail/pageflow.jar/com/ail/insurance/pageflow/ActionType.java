package com.ail.insurance.pageflow;

import com.ail.core.Functions;
import com.ail.core.TypeEnum;

public enum ActionType implements TypeEnum {
    ON_APPLY_REQUEST_VALUES("onApplyRequestValues"),
    ON_PROCESS_ACTIONS("onProcessActions"),
    ON_PROCESS_VALIDATIONS("onProcessValidations"),
    ON_RENDER_RESPONSE("onRenderResponse"),
    ON_PAGEFLOW_ENTRY("onPageFlowEntry"),
    ON_PAGE_ENTRY("onPageEntry"),
    ON_PAGE_EXIT("onPageExit"),
    ON_PAGE_FORWARD("onPageForward"),
    ON_PAGE_BACK("onPageBack"),
    ON_ERROR("onError");

    private final String longName;
    
    ActionType() {
        this.longName=name();
    }
    
    ActionType(String longName) {
        this.longName=longName;
    }
    
    public String valuesAsCsv() {
        return Functions.arrayAsCsv(values());
    }

    public String longName() {
        return longName;
    }
    
    /**
     * This method is similar to the valueOf() method offered by Java's Enum type, but
     * in this case it will match either the Enum's name or the longName.
     * @param name The name to lookup
     * @return The matching Enum, or IllegalArgumentException if there isn't a match.
     */
    public static ActionType forName(String name) {
        return (ActionType)Functions.enumForName(name, values());
    }
    
    public String getName() {
        return name();
    }
    
    public String getLongName() {
        return longName;
    }

    public int getOrdinal() {
        return ordinal();
    }

    public String toString() {
        return longName;
    }
}
