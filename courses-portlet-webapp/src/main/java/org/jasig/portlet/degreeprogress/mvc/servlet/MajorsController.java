/**
 * Licensed to Apereo under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Apereo licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License.  You may obtain a
 * copy of the License at the following location:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
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
