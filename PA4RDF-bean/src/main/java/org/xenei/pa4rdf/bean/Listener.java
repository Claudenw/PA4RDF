package org.xenei.pa4rdf.bean;

/**
 * An interface that receives SubjectInfo recrods when they are parsed.
 *
 */
public interface Listener {
    /**
     * Called when a class is parsed.
     * 
     * @param info
     *            the SubjectInfo for the parsed class.
     */
    void onParseClass(SubjectInfo info);
}