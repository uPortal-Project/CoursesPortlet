package org.jasig.portlet.degreeprogress.mvc.servlet;

import java.util.HashMap;
import java.util.Map;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgramDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/program-info")
public class MajorsController {

    private IDegreeProgramDao degreeProgramDao;
    
    @Autowired(required = true)
    public void setDegreeProgramDao(IDegreeProgramDao degreeProgramDao) {
        this.degreeProgramDao = degreeProgramDao;
    }

    @RequestMapping(method = RequestMethod.GET)
    public ModelAndView getMajors(String term, String program) {
        Map<String,Object> model = new HashMap<String,Object>();
        model.put("majors", degreeProgramDao.getMajors(program));
        model.put("programInfo", degreeProgramDao.getInformationForProgram(term, program));
        return new ModelAndView("jsonView", model);
    }
    
}
