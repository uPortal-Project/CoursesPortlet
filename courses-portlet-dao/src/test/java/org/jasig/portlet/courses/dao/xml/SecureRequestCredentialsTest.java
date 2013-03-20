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
