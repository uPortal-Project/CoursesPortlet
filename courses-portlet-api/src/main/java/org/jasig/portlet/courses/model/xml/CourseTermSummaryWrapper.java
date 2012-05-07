package org.jasig.portlet.courses.model.xml;

import java.util.List;

public abstract class CourseTermSummaryWrapper {
    public CourseSummary getCourseSummary(String termCode) {
        for (final CourseSummary courseSummary : this.getCourseSummaries()) {
            if (termCode.equals(courseSummary.getTermCode())) {
                return courseSummary;
            }
        }
        
        return null;
    }
    
    public abstract List<CourseSummary> getCourseSummaries();
}
