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

package org.jasig.portlet.courses.dao.xml;

import java.io.IOException;
import java.io.Serializable;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SealedObject;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

import org.apache.commons.lang.RandomStringUtils;

/**
 * Holds a username and password in a secure manner. The intent is not to protect from in memory attacks as calling {@link #getPassword()}
 * returns the plain-text password. The intent is to prevent accidental exposure via serialization, toString or other similar approach.
 * <br/>
 * Decryption will also only work within the same JVM as the object was created as the password used is random and scoped to the life of this class.
 * 
 * @author Eric Dalquist
 */
public final class SecureRequestCredentials implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private static final String PASSWORD_ALGORITHM = "PBEWithMD5AndDES";
    private static final int ITERATIONS = 20;
    private static final SecretKey SECRET_KEY;
    
    static {
        final SecretKeyFactory secretKeyFactory;
        try {
            secretKeyFactory = SecretKeyFactory.getInstance(PASSWORD_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to create SecretKeyFactory for algorithm '" + PASSWORD_ALGORITHM + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }

        //Generate a random 256 character password
        final char[] keyPassPhrase = RandomStringUtils.randomAscii(256).toCharArray();
        
        final PBEKeySpec pbeKeySpec = new PBEKeySpec(keyPassPhrase);
        try {
            SECRET_KEY = secretKeyFactory.generateSecret(pbeKeySpec);
        }
        catch (InvalidKeySpecException e) {
            throw new Error("Failed to create SecretKey for PBEKeySpec '" + pbeKeySpec + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
    }
    
    private final String username;
    private final SealedObject sealedPassword;
    
    /**
     * @param username Username to store
     * @param password Password to store, the array will be filled with empty strings before this constructor returns
     */
    public SecureRequestCredentials(String username, char[] password) {
        try {
            final Cipher cipher = getCipher(username, Cipher.ENCRYPT_MODE);
            
            final SealedObject sp;
            try {
                sp = new SealedObject(password, cipher);
            }
            catch (IllegalBlockSizeException e) {
                throw new Error("Failed to create SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
            }
            catch (IOException e) {
                throw new Error("Failed to create SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
            }
            
            this.username = username;
            this.sealedPassword = sp;
        }
        finally {
            //Nuke the password array in memory as soon as we're done with it
            Arrays.fill(password, ' ');
        }
    }
    
    /**
     * @return The username
     */
    public String getUsername() {
        return username;
    }
    
    /**
     * @return The password, will fail if attempting this on an object deserialized from another server.
     */
    public char[] getPassword() {
        final Cipher cipher = getCipher(username, Cipher.DECRYPT_MODE);
        
        try {
            return (char[])this.sealedPassword.getObject(cipher);
        }
        catch (IllegalBlockSizeException e) {
            throw new Error("Failed to decrypt SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        catch (BadPaddingException e) {
            throw new Error("Failed to decrypt SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        catch (IOException e) {
            throw new Error("Failed to decrypt SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        catch (ClassNotFoundException e) {
            throw new Error("Failed to decrypt SealedObject. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
    }

    private static Cipher getCipher(String username, int opmode) {
        final Cipher cipher;
        try {
            cipher = Cipher.getInstance(PASSWORD_ALGORITHM);
        }
        catch (NoSuchAlgorithmException e) {
            throw new Error("Failed to create Cipher for algorithm '" + PASSWORD_ALGORITHM + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        catch (NoSuchPaddingException e) {
            throw new Error("Failed to create Cipher for algorithm '" + PASSWORD_ALGORITHM + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        
        final byte[] salt = getSalt(username);
        final PBEParameterSpec pbeParamSpec = new PBEParameterSpec(salt, ITERATIONS);
        try {
            cipher.init(opmode, SECRET_KEY, pbeParamSpec);
        }
        catch (InvalidKeyException e) {
            throw new Error("Failed to init Cipher for SecretKey '" + SECRET_KEY + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        catch (InvalidAlgorithmParameterException e) {
            throw new Error("Failed to init Cipher for PBEParameterSpec '" + pbeParamSpec + "'. " + SecureRequestCredentials.class.getSimpleName() + " will not work", e);
        }
        
        return cipher;
    }

    /**
     * Uses the first 8 bytes of the username as a salt
     */
    private static byte[] getSalt(String username) {
        final byte[] usernameBytes = username.getBytes();
        if (usernameBytes.length == 8) {
            return usernameBytes;
        }
        
        final byte[] salt = new byte[8];
        System.arraycopy(usernameBytes, 0, salt, 0, Math.min(usernameBytes.length, salt.length));
        return salt;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Arrays.hashCode(getPassword());
        result = prime * result + ((username == null) ? 0 : username.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        SecureRequestCredentials other = (SecureRequestCredentials) obj;
        if (!Arrays.equals(getPassword(), other.getPassword()))
            return false;
        if (username == null) {
            if (other.username != null)
                return false;
        }
        else if (!username.equals(other.username))
            return false;
        return true;
    }

    @Override
    public String toString() {
        return "SecureRequestCredentials [username=" + username + "]";
    }
}