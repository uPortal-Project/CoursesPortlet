package org.jasig.portlet.courses.dao;

import java.util.List;

import javax.annotation.Resource;
import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;

public class MergingCoursesDaoImpl implements ICoursesDao {
    
    private List<ICoursesDao> courseDaos;
    
    @Resource(name="courseDaos")
    public void setCourseDaos(List<ICoursesDao> courseDaos) {
        this.courseDaos = courseDaos;
    }

    @Override
    public CourseSummaryWrapper getSummary(PortletRequest request) {
        CourseSummaryWrapper wrapper = new CourseSummaryWrapper();
        
        for (ICoursesDao dao : courseDaos) {
            try {
                wrapper.getCourses().addAll(dao.getSummary(request).getCourses());
            } catch (Exception e) {
            }
        }

        return wrapper;
    }

}
