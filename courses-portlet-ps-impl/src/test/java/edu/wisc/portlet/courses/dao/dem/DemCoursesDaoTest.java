package edu.wisc.portlet.courses.dao.dem;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.when;

import java.util.Collections;

import javax.portlet.PortletRequest;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.mock.web.portlet.MockPortletRequest;

import edu.wisc.dem.grades.FinalGradesService;
import edu.wisc.dem.grades.xom.finalgrades.res.GetCompIntfcUSRFINALGRADECIResponse;
import edu.wisc.dem.grades.xom.terms.res.GetCompIntfcUSRSTUTERMCIResponse;
import edu.wisc.portlet.courses.dao.dem.DemCoursesDao.CoursesByTermKey;

@RunWith(MockitoJUnitRunner.class)
public class DemCoursesDaoTest {
    @InjectMocks private DemCoursesDao coursesDao;
    @Mock private FinalGradesService finalGradesService;
    
    @Before
    public void setup() {
        coursesDao.setPrimaryUserAttributesPreference("studentIdAttributeName");
    }
    
    @Test
    public void testTermList() throws Exception {
        final GetCompIntfcUSRSTUTERMCIResponse result = getRawTermData();
        when(finalGradesService.getGradedTerms("AB123456")).thenReturn(result.getUSRTERMWSVWS());
        
        final MockPortletRequest request = new MockPortletRequest();
        
        request.getPreferences().setValue("studentIdAttributeName", "studentId");
        request.getPreferences().store();
        
        request.setAttribute(PortletRequest.USER_INFO, Collections.singletonMap("studentId", "AB123456"));
        
        final String key = coursesDao.getTermListKey(request);
        final TermList termList = coursesDao.getTermList(key);
        
        assertNotNull(termList);
        assertEquals(11, termList.getTerms().size());
        final Term currentTerm = termList.getCurrentTerm();
        assertNotNull(currentTerm);
        assertEquals("1124", currentTerm.getCode());
        assertEquals("2012 Sprng", currentTerm.getDisplayName());
        assertEquals(0, DateTime.parse("2012-05-20T00:00:00.000-05:00").compareTo(currentTerm.getEnd()));
        assertEquals(0, DateTime.parse("2011-12-25T00:00:00.000-06:00").compareTo(currentTerm.getStart()));
        assertEquals("UGRD", currentTerm.getTermType());
        assertNull(currentTerm.getYear());
    }
    
    @Test
    public void testCoursesByTerm() throws Exception {
        final GetCompIntfcUSRSTUTERMCIResponse terms = getRawTermData();
        when(finalGradesService.getGradedTerms("AB123456")).thenReturn(terms.getUSRTERMWSVWS());

        final GetCompIntfcUSRFINALGRADECIResponse courses = getRawCoursesData();
        when(finalGradesService.getFinalGrades("AB123456", "UGRD", null, "1124")).thenReturn(courses.getCLASSTBLSEVWS());


        final GetCompIntfcUSRFINALGRADECIResponse career = getRawCareerData();
        when(finalGradesService.getCareer("AB123456", "UGRD", null, "1124")).thenReturn(career.getSTDNTCAREERS());
        
        
        final MockPortletRequest request = new MockPortletRequest();
        
        request.getPreferences().setValue("studentIdAttributeName", "studentId");
        request.getPreferences().store();
        
        request.setAttribute(PortletRequest.USER_INFO, Collections.singletonMap("studentId", "AB123456"));
        
        final CoursesByTermKey key = coursesDao.getCoursesByTermKey(request, "1124");
        final CoursesByTerm coursesByTerm = coursesDao.getCoursesByTerm(key);
        
        assertNotNull(coursesByTerm);
        assertEquals(4, coursesByTerm.getCourses().size());
        assertEquals(new Double("15.0"), coursesByTerm.getCredits());
        assertEquals(new Double("3.945"), coursesByTerm.getGpa());
        assertEquals(0, coursesByTerm.getNewUpdateCount());
        assertEquals(new Double("96.0"), coursesByTerm.getOverallCredits());
        assertEquals(new Double("3.536"), coursesByTerm.getOverallGpa());
        assertNull(coursesByTerm.getTermCode());
        
    }

    protected GetCompIntfcUSRSTUTERMCIResponse getRawTermData() throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(GetCompIntfcUSRSTUTERMCIResponse.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final GetCompIntfcUSRSTUTERMCIResponse result = (GetCompIntfcUSRSTUTERMCIResponse)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/expectedTerms.xml"));
        return result;
    }

    protected GetCompIntfcUSRFINALGRADECIResponse getRawCoursesData() throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(GetCompIntfcUSRFINALGRADECIResponse.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final GetCompIntfcUSRFINALGRADECIResponse result = (GetCompIntfcUSRFINALGRADECIResponse)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/expectedFinalGrades.xml"));
        return result;
    }

    protected GetCompIntfcUSRFINALGRADECIResponse getRawCareerData() throws JAXBException {
        final JAXBContext context = JAXBContext.newInstance(GetCompIntfcUSRFINALGRADECIResponse.class);
        final Unmarshaller unmarshaller = context.createUnmarshaller();
        final GetCompIntfcUSRFINALGRADECIResponse result = (GetCompIntfcUSRFINALGRADECIResponse)unmarshaller.unmarshal(this.getClass().getResourceAsStream("/data/expectedCareer.xml"));
        return result;
    }
}

