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

import javax.portlet.PortletRequest;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.io.Serializable;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

/**
 *
 * @author edomazlicky
 */
public class PortletRequestCacheKeyGeneratorTest {

    @Mock PortletRequest request;

    public PortletRequestCacheKeyGeneratorTest() {
    }

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        when(request.getRemoteUser()).thenReturn("student");
    }

      
    /**
     * Test of generateKey method, of class PortletRequestCacheKeyGenerator.
     */
    @Test
    public void testGenerateKey_ObjectArr() {
        System.out.println("generateKey");
        Object[] os = {request};
        PortletRequestCacheKeyGenerator instance = new PortletRequestCacheKeyGenerator();
        Serializable expResult = "student";
        Serializable result = instance.generateKey(os);
        assertEquals(expResult, result);        
    }

}