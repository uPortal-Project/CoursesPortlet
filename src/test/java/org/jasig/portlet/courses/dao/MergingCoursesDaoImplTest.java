package org.jasig.portlet.courses.dao;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.wrapper.CourseSummaryWrapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "/testContext.xml")
public class MergingCoursesDaoImplTest {
    
    @Autowired
    MergingCoursesDaoImpl dao;

    @Mock PortletRequest request;
    
    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        Map<String, String> userInfo = new HashMap<String,String>();
        userInfo.put("user.login.id", "student");
        userInfo.put("password", "student");
        when(request.getAttribute(PortletRequest.USER_INFO)).thenReturn(userInfo);
        when(request.getRemoteUser()).thenReturn("student");

    }

    @Test
    public void test() {
        CourseSummaryWrapper summary = dao.getSummary(request);
        assertEquals(6, summary.getCourses().size());        
    }

}
