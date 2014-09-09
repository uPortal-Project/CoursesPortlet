/**
 * Licensed to Jasig under one or more contributor license
 * agreements. See the NOTICE file distributed with this work
 * for additional information regarding copyright ownership.
 * Jasig licenses this file to you under the Apache License,
 * Version 2.0 (the "License"); you may not use this file
 * except in compliance with the License. You may obtain a
 * copy of the License at:
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on
 * an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied. See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.jasig.portlet.degreeprogress.dao;

import java.util.List;
import javax.portlet.PortletRequest;
import org.jasig.portlet.degreeprogress.model.DegreeProgramSummary;
import org.jasig.portlet.degreeprogress.model.ProgramComponent;

public interface IDegreeProgramDao {
    
    public DegreeProgramSummary getProgramSummary(PortletRequest request);

    public List<ProgramComponent> getEntryTerms();

    public List<ProgramComponent> getPrograms(String term);

    public List<ProgramComponent> getLevels();

    public List<ProgramComponent> getDegrees();

    public List<ProgramComponent> getColleges();

    public List<ProgramComponent> getCampuses();

    public List<ProgramComponent> getMajors(String program);

    public List<ProgramComponent> getMinors();

    public List<ProgramComponent> getDepartments();

    public List<ProgramComponent> getConcentrations();

    public List<ProgramComponent> getEvaluationTerms();

    public ProgramInformation getInformationForProgram(String term, String program);

}
