package org.jasig.portlet.courses.mvc;

import javax.portlet.RenderRequest;
import javax.portlet.RenderResponse;
import javax.portlet.WindowState;

import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.portlet.handler.HandlerInterceptorAdapter;

public class MyCoursesMinimizedStateHandlerInterceptor extends HandlerInterceptorAdapter {

    @Autowired(required = true)
    private ICoursesDao coursesDao;
    
    @Override
    public boolean preHandleRender(RenderRequest request, RenderResponse response, Object handler) throws Exception {
        if (WindowState.MINIMIZED.equals(request.getWindowState())) {
            
            final TermList termList = coursesDao.getTermList(request);
            final Term currentTerm = termList.getCurrentTerm();
            final String termCode = currentTerm.getCode();
            final CoursesByTerm coursesByTerm = coursesDao.getCoursesByTerm(request, termCode);
            final int newUpdateCount = coursesByTerm.getNewUpdateCount();
            response.setProperty("newItemCount", String.valueOf(newUpdateCount));
            
            return false;
        }
        
        return true;
    }
}
