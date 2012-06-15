package org.jasig.portlet.courses.model.xml;

import java.util.List;

public abstract class TermsAndCoursesWrapper {
    public CoursesByTerm getCoursesByTerm(String termCode) {
        for (final CoursesByTerm courseSummary : this.getCoursesByTerms()) {
            if (termCode.equals(courseSummary.getTermCode())) {
                return courseSummary;
            }
        }
        
        return null;
    }
    
    public abstract List<CoursesByTerm> getCoursesByTerms();
}
