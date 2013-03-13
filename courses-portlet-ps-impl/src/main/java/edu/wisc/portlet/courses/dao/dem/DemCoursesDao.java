package edu.wisc.portlet.courses.dao.dem;

import static edu.wisc.dem.grades.PeopleSoftWsUtils.getValue;

import java.io.Serializable;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.dao.ICacheableCoursesDao;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.model.xml.personal.Course;
import org.jasig.portlet.courses.model.xml.personal.CoursesByTerm;
import org.jasig.portlet.courses.util.TermComparator;
import org.joda.time.DateMidnight;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;

import edu.wisc.dem.grades.FinalGradesService;
import edu.wisc.dem.grades.xom.finalgrades.res.CLASSTBLSEVWComplexTypeShape;
import edu.wisc.dem.grades.xom.finalgrades.res.STDNTCAREERComplexTypeShape;
import edu.wisc.dem.grades.xom.finalgrades.res.STDNTCARTERM2ComplexTypeShape;
import edu.wisc.dem.grades.xom.terms.res.USRTERMWSVWComplexTypeShape;
import edu.wisc.portlet.courses.dao.dem.DemCoursesDao.CoursesByTermKey;

public class DemCoursesDao implements ICacheableCoursesDao<String, CoursesByTermKey> {
    protected final Log logger = LogFactory.getLog(getClass());
    
    private FinalGradesService finalGradesService;
    private String primaryUserAttributesPreference;

    @Required
    public void setPrimaryUserAttributesPreference(String pviUserAttributesPreference) {
        this.primaryUserAttributesPreference = pviUserAttributesPreference;
    }

    @Autowired
    public void setFinalGradesService(FinalGradesService finalGradesService) {
        this.finalGradesService = finalGradesService;
    }
    
    @Override
    public String getTermListKey(PortletRequest request) {
        return this.getPrimaryUserAttribute(request);
    }

    @Override
    public TermList getTermList(String emplid) {
        final List<USRTERMWSVWComplexTypeShape> gradedTerms = this.finalGradesService.getGradedTerms(emplid);
        
        final TermList termList = new TermList();

        boolean hasCurrent = false;
        
        final List<Term> terms = termList.getTerms();
        for (final USRTERMWSVWComplexTypeShape usrtermwsvwComplexTypeShape : gradedTerms) {
            final Term term = new Term();
            
            term.setCode((String)getValue(usrtermwsvwComplexTypeShape.getSTRM()));
            term.setCurrent("Y".equals(getValue(usrtermwsvwComplexTypeShape.getCURRENTTERM())));
            term.setDisplayName((String)getValue(usrtermwsvwComplexTypeShape.getTERMDESCR()));
            term.setTermType((String)getValue(usrtermwsvwComplexTypeShape.getACADCAREER()));
            term.setRegistered("Y".equals(getValue(usrtermwsvwComplexTypeShape.getREGISTERED())));
            
            final DateMidnight start = getValue(usrtermwsvwComplexTypeShape.getTERMBEGINDT());
            if (start != null) {
                term.setStart(start.toDateTime());
            }
            
            final DateMidnight end = getValue(usrtermwsvwComplexTypeShape.getTERMENDDT());
            if (end != null) {
                term.setEnd(end.toDateTime());
            }
            
            //track if we have found a current term
            hasCurrent = hasCurrent || term.isCurrent();
            
            terms.add(term);
        }

        if (!terms.isEmpty()) {
            //Sort terms by start
            Collections.sort(terms, TermComparator.INSTANCE);
            
            //No current-term set, use the most recent term
            if (!hasCurrent) {
                terms.get(terms.size() - 1).setCurrent(true);
            }
        }
        
        return termList;
    }

    
    
    @Override
    public CoursesByTermKey getCoursesByTermKey(PortletRequest request, String termCode) {
        final String emplid = this.getPrimaryUserAttribute(request);

        final String termListKey = this.getTermListKey(request);
        final TermList termList = this.getTermList(termListKey);
        final Term term = termList.getTerm(termCode);
        final String academicCareer;
        if (term == null) {
            academicCareer = null;
        }
        else {
            academicCareer = term.getTermType();
        }
        
        return new CoursesByTermKey(termCode, emplid, academicCareer);
    }


    @Override
    public CoursesByTerm getCoursesByTerm(CoursesByTermKey key) {
        final List<CLASSTBLSEVWComplexTypeShape> finalGrades = this.finalGradesService.getFinalGrades(key.getEmplid(), key.getAcademicCareer(), null, key.getTermCode());
        
        final CoursesByTerm coursesByTerm = new CoursesByTerm();
        
        final List<Course> courses = coursesByTerm.getCourses();
        for (final CLASSTBLSEVWComplexTypeShape classtblsevwComplexTypeShape : finalGrades) {
            final Course course = new Course();

            course.setCode(classtblsevwComplexTypeShape.getCATALOGNBR().getValue());
            course.setCredits(classtblsevwComplexTypeShape.getUNTTAKEN().getValue().doubleValue());
            course.setGrade((String)getValue(classtblsevwComplexTypeShape.getCRSEGRADEOFF()));
            course.setSubject((String)getValue(classtblsevwComplexTypeShape.getSUBJDESCR()));
            course.setTitle((String)getValue(classtblsevwComplexTypeShape.getCLASSDESCR()));
            
            courses.add(course);
        }
        final List<STDNTCAREERComplexTypeShape> careers = this.finalGradesService.getCareer(key.getEmplid(), key.getAcademicCareer(), null, key.getTermCode());
        for (final STDNTCAREERComplexTypeShape stdntcareerComplexTypeShape : careers) {
        	List<STDNTCARTERM2ComplexTypeShape> stdntcarterm2ComplexTypeShape = stdntcareerComplexTypeShape.getSTDNTCARTERM2S();
        	for (final STDNTCARTERM2ComplexTypeShape car_Term2  : stdntcarterm2ComplexTypeShape) {
                coursesByTerm.setGpa(car_Term2.getCURGPA().getValue().doubleValue());
                coursesByTerm.setCredits(car_Term2.getUNTPASSDPRGRSS().getValue().doubleValue());
                coursesByTerm.setOverallGpa(car_Term2.getCUMGPA().getValue().doubleValue());
                coursesByTerm.setOverallCredits(car_Term2.getTOTCUMULATIVE().getValue().doubleValue());
        	}
        }
        
        //TODO sort courses
        return coursesByTerm;
    }


    /**
     * Get the user's primary attribute.
     *
     * @param primaryUserAttributesPreference The portlet preference that contains a list of user attributes to inspect in order. The first attribute with a value is returned.
     * @return The primary attribute, will never return null or empty string
     */
    @SuppressWarnings("unchecked")
    protected final String getPrimaryUserAttribute(PortletRequest request) {
        final PortletPreferences preferences = request.getPreferences();
        final Map<String, String> userAttributes = (Map<String, String>)request.getAttribute(PortletRequest.USER_INFO);
        
        final String[] attributeNames = preferences.getValues(primaryUserAttributesPreference, new String[0]);
        for (final String attributeName : attributeNames) {
            final String id = userAttributes.get(attributeName);
            if (StringUtils.isNotEmpty(id)) {
                if (logger.isDebugEnabled()) {
                    logger.debug("Found id " + id + " for under attribute " + attributeName);
                }
                
                return id;
            }
        }
        
        throw new RuntimeException("No primary attribute found in '" + primaryUserAttributesPreference + "' values " + Arrays.toString(attributeNames));
    }
    
    public static final class CoursesByTermKey implements Serializable {
        private static final long serialVersionUID = 1L;
        
        private final String termCode;
        private final String emplid;
        private final String academicCareer;
        private final int hashCode;
        
        private CoursesByTermKey(String termCode, String emplid, String academicCareer) {
            this.termCode = termCode;
            this.emplid = emplid;
            this.academicCareer = academicCareer;
            this.hashCode = internalHashCode();
        }

        public String getTermCode() {
            return termCode;
        }

        public String getEmplid() {
            return emplid;
        }

        public String getAcademicCareer() {
            return academicCareer;
        }

        @Override
        public int hashCode() {
            return hashCode;
        }

        private int internalHashCode() {
            final int prime = 31;
            int result = 1;
            result = prime * result + ((academicCareer == null) ? 0 : academicCareer.hashCode());
            result = prime * result + ((emplid == null) ? 0 : emplid.hashCode());
            result = prime * result + ((termCode == null) ? 0 : termCode.hashCode());
            return result;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj)
                return true;
            if (obj == null)
                return false;
            if (getClass() != obj.getClass())
                return false;
            CoursesByTermKey other = (CoursesByTermKey) obj;
            if (academicCareer == null) {
                if (other.academicCareer != null)
                    return false;
            }
            else if (!academicCareer.equals(other.academicCareer))
                return false;
            if (emplid == null) {
                if (other.emplid != null)
                    return false;
            }
            else if (!emplid.equals(other.emplid))
                return false;
            if (termCode == null) {
                if (other.termCode != null)
                    return false;
            }
            else if (!termCode.equals(other.termCode))
                return false;
            return true;
        }

        @Override
        public String toString() {
            return "CoursesByTermKey [termCode=" + termCode + ", emplid=" + emplid + ", academicCareer="
                    + academicCareer + "]";
        }
    }
}
