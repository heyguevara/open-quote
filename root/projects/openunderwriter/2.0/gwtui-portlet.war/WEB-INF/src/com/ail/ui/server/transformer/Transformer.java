package com.ail.ui.server.transformer;

public interface Transformer<I, O> {
    O apply(I input) throws Exception;
}
