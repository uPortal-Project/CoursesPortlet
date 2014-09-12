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
package org.jasig.portlet.courses.handler;

import org.jasig.portlet.courses.dao.ICoursesSectionDao;
import org.jasig.portlet.courses.model.xml.Term;
import org.jasig.portlet.courses.model.xml.TermList;
import org.jasig.portlet.courses.mvc.portlet.UWCoursesPortletController;
import org.jasig.portlet.utils.mvc.IViewSelector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.StringUtils;
import org.springframework.web.portlet.ModelAndView;
import org.springframework.web.portlet.handler.SimpleMappingExceptionResolver;

import javax.portlet.PortletRequest;
import javax.portlet.PortletSession;

/**
 * Check if the request that failed is from a mobile device and uses a mobile
 * view if one exists.
 * <p/>
 *
 * applicationContext.xml Example:
 * <pre>
 *   <bean id="defaultPortletExceptionHandlerTemplate" class="org.jasig.portlet.courses.handler.MobileAwareExceptionHandler">
 *     <constructor-arg value="/WEB-INF/jsp/"/>
 *     <constructor-arg value=".jsp"/>
 *     <constructor-arg value="-jQM"/>
 *
 *     <property name="exceptionMappings">
 *       <props>...
 * </pre>
 *
 * @author Colin Kennedy (cpkennedy2@wisc.edu)
 */
public class MobileAwareExceptionHandler extends SimpleMappingExceptionResolver implements ResourceLoaderAware {
  protected final Logger logger = LoggerFactory.getLogger(getClass());

  private IViewSelector viewSelector;
  private String prefix;
  private String suffix;
  private String mobileSuffix;
  private ResourceLoader resourceLoader;

  @Autowired
  public void setViewSelector(IViewSelector viewSelector) {
    this.viewSelector = viewSelector;
  }

  MobileAwareExceptionHandler(String prefix, String suffix, String mobileSuffix) {
    this.prefix = prefix;
    this.suffix = suffix;
    this.mobileSuffix = mobileSuffix;

  }

    private ICoursesSectionDao coursesSectionDao;

    @Autowired
    @Qualifier("coursesSectionDao")
    public void setCoursesSectionDao(ICoursesSectionDao coursesSectionDao) {
        this.coursesSectionDao = coursesSectionDao;
    }

  @Override
  protected ModelAndView getModelAndView(String view,
                                         Exception ex,
                                         PortletRequest request)
  {
    boolean isMobile = this.viewSelector.isMobile(request);
    Resource newView = null;
    if( isMobile ){
      try {
        String mobileViewPath = this.prefix +
                                view + this.mobileSuffix +
                                this.suffix;
        logger.debug( "Looking for mobile friendly Exception view: " + mobileViewPath );
        newView = resourceLoader.getResource( mobileViewPath );
      } catch (Exception e) {
        // No view, eat the exception
        logger.debug("No mobile view for: " + view, e);
      }
    }

    ModelAndView  mav;
    if( newView != null && newView.exists() ){
      mav = super.getModelAndView(view + this.mobileSuffix, ex, request);
    } else {
      mav = super.getModelAndView(view, ex, request);
    }

    mav.getModelMap().put("isMobile", isMobile);
    if (logger.isDebugEnabled())
        logger.debug("Accessing getSelectedTerm from Request/Session");
    Term selectedTerm=getSelectedTerm(request);
    mav.getModelMap().put("selectedTerm", selectedTerm);
    return mav;
  }

  public void setResourceLoader(ResourceLoader resourceLoader) {
    this.resourceLoader = resourceLoader;
  }

  private Term getSelectedTerm(PortletRequest request)
  {

      String requestTermCode=request.getParameter("termCode");
      PortletSession portletSession=request.getPortletSession(true);
      Term selectedTerm=new Term();
      if ((portletSession!=null) && (!StringUtils.isEmpty(portletSession.getAttribute(UWCoursesPortletController.TERMCODE))))
      {

         String sessionTermCode=(String)portletSession.getAttribute(UWCoursesPortletController.TERMCODE);
         if (logger.isDebugEnabled())
            logger.debug("Term Code found in PortletSession......"+sessionTermCode);
         selectedTerm.setCode(sessionTermCode);
      }
      else if (!StringUtils.isEmpty(requestTermCode))
      {
          if (logger.isDebugEnabled())
             logger.debug("Term Code found in Request......"+requestTermCode);
          selectedTerm.setCode(requestTermCode);
          portletSession.setAttribute(UWCoursesPortletController.TERMCODE, requestTermCode);
      }
      else
      {
          try
          {
            TermList termList = coursesSectionDao.getTermList(request);
            selectedTerm = termList.getCurrentTerm();
            portletSession.setAttribute(UWCoursesPortletController.TERMCODE, selectedTerm.getCode());
            if (logger.isDebugEnabled())
               logger.debug("Term Code found in ICoursesSectionDao....."+selectedTerm.getCode());
          }
          catch (Exception ex)
          {
            logger.error("Error Occurred while accessing TermCode from ICoursesSectionDao",ex);
          }
      }
      return selectedTerm;
  }
}
