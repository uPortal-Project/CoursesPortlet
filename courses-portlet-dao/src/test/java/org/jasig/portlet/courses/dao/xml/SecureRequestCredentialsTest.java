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
package org.jasig.portlet.courses.dao.xml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import org.junit.Test;

public class SecureRequestCredentialsTest {
    @Test
    public void testEncDec() {
        final SecureRequestCredentials secureRequestCredentials = new SecureRequestCredentials("juser", "foobar".toCharArray());
        assertNotNull(secureRequestCredentials);
        
        final char[] password = secureRequestCredentials.getPassword();
        assertNotNull(password);
        
        assertEquals("foobar", new String(password));
        
        //Different object same user/pass
        final SecureRequestCredentials secureRequestCredentialsSame = new SecureRequestCredentials("juser", "foobar".toCharArray());
        
        assertEquals(secureRequestCredentials, secureRequestCredentialsSame);
        assertEquals(secureRequestCredentials.hashCode(), secureRequestCredentialsSame.hashCode());
        
        
        //Different object same user diff pass
        final SecureRequestCredentials secureRequestCredentialsDiffPass = new SecureRequestCredentials("juser", "barfoo".toCharArray());
        
        assertNotEquals(secureRequestCredentials, secureRequestCredentialsDiffPass);
        assertNotEquals(secureRequestCredentials.hashCode(), secureRequestCredentialsDiffPass.hashCode());
        
        
        //Different object diff user same pass
        final SecureRequestCredentials secureRequestCredentialsDiffUser = new SecureRequestCredentials("jsmithdoe", "foobar".toCharArray());
        
        assertNotEquals(secureRequestCredentials, secureRequestCredentialsDiffUser);
        assertNotEquals(secureRequestCredentials.hashCode(), secureRequestCredentialsDiffUser.hashCode());
    }
    
    @Test
    public void testEncDecWithSerialize() throws Exception {
        final SecureRequestCredentials secureRequestCredentials = new SecureRequestCredentials("juser", "foobar".toCharArray());
        assertNotNull(secureRequestCredentials);
        
        final ByteArrayOutputStream baos = new ByteArrayOutputStream();
        final ObjectOutputStream oos = new ObjectOutputStream(baos);
        oos.writeObject(secureRequestCredentials);
        
        final byte[] data = baos.toByteArray();
        final ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(data));
        final SecureRequestCredentials secureRequestCredentialsCopy = (SecureRequestCredentials)ois.readObject();
        
        final char[] password = secureRequestCredentialsCopy.getPassword();
        assertNotNull(password);
        
        assertEquals("foobar", new String(password));
        
        assertEquals(secureRequestCredentials, secureRequestCredentialsCopy);
        assertEquals(secureRequestCredentials.hashCode(), secureRequestCredentialsCopy.hashCode());
    }
}
