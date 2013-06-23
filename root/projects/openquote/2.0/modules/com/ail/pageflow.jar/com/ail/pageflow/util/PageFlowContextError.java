package com.ail.pageflow.util;

import com.ail.core.BaseError;
import com.ail.core.BaseException;

/**
 * This error is thrown in response to the PageFlowContext encountering a configuration issue 
 * from which it cannot recover. The situation is such that even redirecting to the error 
 * page is not possible.
 */
public class PageFlowContextError extends BaseError {

    public PageFlowContextError(String description, Throwable target) {
        super(description, target);
    }

    public PageFlowContextError(String description) {
        super(description);
    }

    public PageFlowContextError(BaseException e) {
        super(e);
    }

}
