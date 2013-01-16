package org.jasig.portlet.degreeprogress.dao;

import javax.portlet.PortletRequest;
import org.jasig.portlet.degreeprogress.model.xml.DegreeProgressReport;

public interface IDegreeProgressDao {

    public DegreeProgressReport getProgressReport(PortletRequest request);

    public Boolean getWebEnabled(PortletRequest request);

    /**
     * Returns a {@link WhatIfRequest} pre-populated with things only the 
     * concrete {@link IDegreeProgressDao} should know about.
     * 
     * @param request
     * @return
     */
    public WhatIfRequest createWhatIfRequest(PortletRequest request);

    public DegreeProgressReport getWhatIfReport(WhatIfRequest whatIfRequest);

}
