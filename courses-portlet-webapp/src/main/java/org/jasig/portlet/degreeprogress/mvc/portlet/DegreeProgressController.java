package org.jasig.portlet.degreeprogress.mvc.portlet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.portlet.PortletPreferences;
import javax.portlet.PortletRequest;

import org.apache.commons.lang.StringUtils;
import org.jasig.portlet.courses.dao.ICoursesDao;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgramDao;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgressDao;
import org.jasig.portlet.degreeprogress.dao.WhatIfRequest;
import org.jasig.portlet.degreeprogress.model.DegreeProgramSummary;
import org.jasig.portlet.degreeprogress.model.ProgramComponent;
import org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.UncategorizedSQLException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.portlet.ModelAndView;

@Controller
@RequestMapping("VIEW")
public class DegreeProgressController {
    public static final String PREFERENCE_INFORMATION_LINK = "DegreeProgress.INFORMATION_LINK";
    public static final String PREFERENCE_INFORMATION_LINK_DEFAULT = "#;";

    private IDegreeProgressDao degreeProgressDao;

    @Autowired(required = true)
    public void setDegreeProgressDao(IDegreeProgressDao degreeProgressDao) {
        this.degreeProgressDao = degreeProgressDao;
    }
    
    private IDegreeProgramDao degreeProgramDao;
    
    @Autowired(required = true)
    public void setDegreeProgramDao(IDegreeProgramDao degreeProgramDao) {
        this.degreeProgramDao = degreeProgramDao;
    }

    private ICoursesDao coursesDao;

    @Autowired(required = true)
    public void setCoursesDao(ICoursesDao coursesDao) {
        this.coursesDao = coursesDao;
    }

    @RequestMapping
	public ModelAndView getMainView(PortletRequest request) {
        Map<String, Object> model = new HashMap<String, Object>();
        PortletPreferences preferences = request.getPreferences();
        model.put("terms", degreeProgramDao.getEntryTerms());
        model.put("infoLink",preferences.getValue(PREFERENCE_INFORMATION_LINK,PREFERENCE_INFORMATION_LINK_DEFAULT));
        return new ModelAndView("selectReport",model);
        /*
        Boolean webEnabled = degreeProgressDao.getWebEnabled(request);
        if (webEnabled == null) {
        	return new ModelAndView("error", model);
        }
        if (webEnabled) {
                return new ModelAndView("selectReport", model);
        } else {
                return new ModelAndView("selectWhatIf", model);
        }
        */
	}

    @RequestMapping(params = "action=getScenario")
    public String showDegreeProgressForm(Model model, PortletRequest request) {
        try {
            String currentTerm = coursesDao.getTermList(request).getCurrentTerm().getCode();

            if (!model.containsAttribute("whatIfForm")) {
                WhatIfRequest whatIfRequest = degreeProgressDao.createWhatIfRequest(request);
                whatIfRequest.setCampus("1");
                whatIfRequest.setCurrentTerm(currentTerm);
                model.addAttribute("whatIfForm", whatIfRequest);
            }
            model.addAttribute("campuses", degreeProgramDao.getCampuses());
            model.addAttribute("colleges", degreeProgramDao.getColleges());
            model.addAttribute("concentrations", degreeProgramDao.getConcentrations());
            model.addAttribute("degrees", degreeProgramDao.getDegrees());
            model.addAttribute("departments", degreeProgramDao.getDepartments());
            model.addAttribute("entryTerms", degreeProgramDao.getEntryTerms());
            model.addAttribute("evaluationTerms", degreeProgramDao.getEvaluationTerms());
            model.addAttribute("levels", degreeProgramDao.getLevels());
            model.addAttribute("minors", degreeProgramDao.getMinors());

            model.addAttribute("programs", degreeProgramDao.getPrograms(currentTerm));
            return "whatIf";

        } catch (Exception e) {
            System.out.println(e);
            return "error";
        }
    }

    @RequestMapping(params="action=showWhatIf")
    public ModelAndView showWhatIfProgress(PortletRequest request, @ModelAttribute("whatIfForm") WhatIfRequest whatIfRequest) {
        Map<String, Object> model = new HashMap<String, Object>();
        String err;
        try{

			DegreeProgressReport report = degreeProgressDao
					.getWhatIfReport(whatIfRequest);
			model.put("report", report);

			DegreeProgramSummary summary = new DegreeProgramSummary();

			List<ProgramComponent> programs = degreeProgramDao
					.getPrograms(whatIfRequest.getEntryTerm());
			for (ProgramComponent program : programs) {
				if (program.getKey().equals(whatIfRequest.getProgram())) {
					summary.setProgram(program.getName());
					break;
				}
			}

			List<ProgramComponent> majors = degreeProgramDao
					.getMajors(whatIfRequest.getProgram());
			for (ProgramComponent major : majors) {
				if (major.getKey().equals(whatIfRequest.getMajor())) {
					summary.getMajors().add(major);
					break;
				}
			}

			if (StringUtils.isNotBlank(whatIfRequest.getMajor2())) {
				for (ProgramComponent major : majors) {
					if (major.getKey().equals(whatIfRequest.getMajor2())) {
						summary.getMajors().add(major);
						break;
					}
				}
			}

			List<ProgramComponent> minors = degreeProgramDao.getMinors();
			if (StringUtils.isNotBlank(whatIfRequest.getMinor())) {
				for (ProgramComponent minor : minors) {
					if (minor.getKey().equals(whatIfRequest.getMinor())) {
						summary.getMinors().add(minor);
						break;
					}
				}
			}

			if (StringUtils.isNotBlank(whatIfRequest.getMinor2())) {
				for (ProgramComponent minor : minors) {
					if (minor.getKey().equals(whatIfRequest.getMinor2())) {
						summary.getMinors().add(minor);
						break;
					}
				}
			}

			List<ProgramComponent> concentrations = degreeProgramDao
					.getConcentrations();
			if (StringUtils.isNotBlank(whatIfRequest.getConcentration())) {
				for (ProgramComponent concentration : concentrations) {
					if (concentration.getKey().equals(
							whatIfRequest.getConcentration())) {
						summary.getConcentrations().add(concentration);
						break;
					}
				}
			}

			if (StringUtils.isNotBlank(whatIfRequest.getConcentration2())) {
				for (ProgramComponent concentration : concentrations) {
					if (concentration.getKey().equals(
							whatIfRequest.getConcentration2())) {
						summary.getConcentrations().add(concentration);
						break;
					}
				}
			}

			model.put("program", summary);

			return new ModelAndView("degreeProgress", model);
        
        } catch (EmptyResultDataAccessException e){
        	//No goremal record found
        	System.out.print(e);
        	
        	err = "Your Banner record is not complete at this time please try again later.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);
        	
        } catch (UncategorizedSQLException e){
        	//This is for sgastdn no record found
        	System.out.print(e);
        	
        	err = "You are not a student and cannot run this portlet.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);
        	
        } catch (Exception e){
        	System.out.print(e);
        	e.printStackTrace();
        	
        	err = "General Error\nPlease check the log.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);

        }
    }    
    
    @RequestMapping(params="action=showProgress")
	public ModelAndView showDegreeProgress(PortletRequest request) {
    	Map<String, Object> model = new HashMap<String, Object>();
    	String err;
    	try{        	
	        DegreeProgressReport report = degreeProgressDao.getProgressReport(request);
		    model.put("report", report);
		    
		    DegreeProgramSummary summary = degreeProgramDao.getProgramSummary(request);
		    model.put("program", summary);
	    
		    return new ModelAndView("degreeProgress", model);
		    
        } catch (EmptyResultDataAccessException e){
        	//No goremal record found
        	System.out.print(e);
        	
        	err = "Your Banner record is not complete at this time please try again later.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);
        	
        } catch (UncategorizedSQLException e){
        	//This is for sgastdn no record found
        	System.out.print(e);
        	
        	err = "You are not a student and cannot run this portlet.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);
        	
        } catch (Exception e){
        	System.out.print(e);
        	
        	err = "General Error\nPlease check the log.";
        	model.put("err", err);
        	
        	return new ModelAndView("error", model);

        }
	}
    
}
