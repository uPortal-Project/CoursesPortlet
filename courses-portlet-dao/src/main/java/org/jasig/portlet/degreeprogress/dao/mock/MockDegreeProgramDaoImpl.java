package org.jasig.portlet.degreeprogress.dao.mock;

import java.util.List;
import javax.portlet.PortletRequest;
import org.jasig.portlet.degreeprogress.model.DegreeProgramSummary;
import org.jasig.portlet.degreeprogress.model.ProgramComponent;
import org.jasig.portlet.degreeprogress.dao.IDegreeProgramDao;
import org.jasig.portlet.degreeprogress.dao.ProgramInformation;
import org.springframework.stereotype.Service;

public class MockDegreeProgramDaoImpl implements IDegreeProgramDao {

    public DegreeProgramSummary getProgramSummary(PortletRequest request) {
        
        DegreeProgramSummary summary = new DegreeProgramSummary();
        summary.setProgram("Bachelor of Science");
        summary.addMajor(new ProgramComponent("CSE", "Computer Science and Engineering"));
        summary.addMinor(new ProgramComponent("ECON", "Economics"));
        summary.addConcentration(new ProgramComponent("SD", "Software Development"));
        
        return summary;
    }

    @Override
    public List<ProgramComponent> getEntryTerms() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getPrograms(String term) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getLevels() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getDegrees() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getColleges() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getCampuses() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getMajors(String program) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getMinors() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getDepartments() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getConcentrations() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public List<ProgramComponent> getEvaluationTerms() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public ProgramInformation getInformationForProgram(String term,
            String program) {
        // TODO Auto-generated method stub
        return null;
    }

}
