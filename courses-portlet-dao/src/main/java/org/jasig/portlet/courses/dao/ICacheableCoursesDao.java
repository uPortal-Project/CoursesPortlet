package org.jasig.portlet.courses.dao;

import java.io.Serializable;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;

/**
 * @author Eric Dalquist
 * @version $Revision$
 * @param <TLK> Type of cache key for the term list
 * @param <CK> Type of cache key for the courses by term list
 */
public interface ICacheableCoursesDao<TLK extends Serializable, CK extends Serializable> {

    /**
     * A cache key for the term list that would be retrieved by this request
     */
    public TLK getTermListKey(PortletRequest request);

    /**
     * A cache key for the courses that would be retrieved by this request
     */
    public CK getCoursesByTermKey(PortletRequest request, String termCode, TermList termList);

    /**
     * Get a term list for the current user
     */
    public TermList getTermList(TLK key);

    /**
     * Get courses for a term for the current user
     */
    public CoursesByTerm getCoursesByTerm(CK key);

}
