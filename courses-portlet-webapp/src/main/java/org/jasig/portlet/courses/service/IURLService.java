package org.jasig.portlet.courses.service;

import javax.portlet.PortletRequest;

import org.jasig.portlet.courses.model.xml.Instructor;
import org.jasig.portlet.courses.model.xml.Location;

public interface IURLService {

    public String getLocationUrl(Location location, PortletRequest request);

    public String getInstructorUrl(Instructor instructor, PortletRequest request);

}