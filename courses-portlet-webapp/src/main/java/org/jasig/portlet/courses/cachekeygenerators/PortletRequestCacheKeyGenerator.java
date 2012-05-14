/*
 *  Copyright 2012 edomazlicky.
 * 
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 * 
 *       http://www.apache.org/licenses/LICENSE-2.0
 * 
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *  under the License.
 */

package org.jasig.portlet.courses.cachekeygenerators;

import com.googlecode.ehcache.annotations.key.CacheKeyGenerator;
import java.io.Serializable;
import javax.portlet.PortletRequest;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * PortletRequestCacheKeyGenerator generates a cache key based on a Portlet
 * Request using the username as the key
 *
 * @author Eric Domazlicky, edomazlicky@tacomacc.edu
 * @version $Revision$
 */
public class PortletRequestCacheKeyGenerator implements CacheKeyGenerator<Serializable> {

    @Override
    public Serializable generateKey(MethodInvocation methodInvocation) {
        if(methodInvocation.getArguments().length>0)
            return this.generateKey(methodInvocation.getArguments()[0]);
        else            
            return "GUEST";
        
    }

    @Override
    public Serializable generateKey(Object... os) {
       if(os.length==1)
       {
           PortletRequest portletRequest = (PortletRequest)os[0];
           if(portletRequest.getRemoteUser()!=null)               
               return portletRequest.getRemoteUser();           
           else
               return "GUEST";           
       }
       else 
           return "GUEST";
    }



}
