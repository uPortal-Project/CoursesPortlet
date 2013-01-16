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
