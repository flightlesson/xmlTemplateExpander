package com.strongblackcoffee.xsltemplateexpander;

/**
 *
 */
class TemplateExpanderException extends Exception {
    
    public TemplateExpanderException(String s, Exception x) {
        super(s + x.toString());
    }
    
    public TemplateExpanderException(String s) {
        super(s);
    }
}
