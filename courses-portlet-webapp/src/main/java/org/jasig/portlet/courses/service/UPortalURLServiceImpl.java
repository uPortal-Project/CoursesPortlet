package org.jasig.portlet.courses.service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.portlet.PortletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;

public class UPortalURLServiceImpl implements IURLService {
    
    protected final Log log = LogFactory.getLog(getClass());
    
    private String portalContext = "/uPortal";
    
    /* (non-Javadoc)
     * @see org.jasig.portlet.courses.service.IURLService#getLocationUrl(org.jasig.portlet.courses.model.xml.Location, javax.portlet.PortletRequest)
     */
    @Override
    public String getLocationUrl(Location location, PortletRequest request) {
        try {
            final String encodedLocation = URLEncoder.encode(location.getIdentifier(), "UTF-8");
            return portalContext.concat("/s/location?id=").concat(encodedLocation);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to encode location id " + location.getIdentifier());
            return null;
        }
    }

    /* (non-Javadoc)
     * @see org.jasig.portlet.courses.service.IURLService#getInstructorUrl(org.jasig.portlet.courses.model.xml.Instructor, javax.portlet.PortletRequest)
     */
    @Override
    public String getInstructorUrl(Instructor instructor, PortletRequest request) {
        try {
            final String encodedUsername = URLEncoder.encode(instructor.getIdentifier(), "UTF-8");
            return portalContext.concat("/s/person?action=findByUsername&id=").concat(encodedUsername);
        } catch (UnsupportedEncodingException e) {
            log.error("Unable to encode location id " + instructor.getIdentifier());
            return null;
        }
    }

}
