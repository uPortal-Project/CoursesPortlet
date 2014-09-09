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
package org.jasig.portlet.courses.util;

import java.math.BigInteger;
import java.util.Comparator;

import org.jasig.portlet.courses.model.xml.Term;
import org.joda.time.DateTime;

public class TermComparator implements Comparator<Term> {
    public static final TermComparator INSTANCE = new TermComparator();
    

    @Override
    public int compare(Term o1, Term o2) {
        if (o1 == o2) {
            return 0;
        }
        if (o1 == null) {
            return -1;
        }
        if (o2 == null) {
            return 1;
        }
        
        //Try comparing by start DateTime first
        final DateTime s1 = o1.getStart();
        final DateTime s2 = o2.getStart();
        if (s1 != null && s2 != null) {
            return s1.compareTo(s2);
        }
        
        //Then try comparing by year
        final BigInteger y1 = o1.getYear();
        final BigInteger y2 = o2.getYear();
        if (y1 != null && y2 != null) {
            return y1.compareTo(y2);
        }
        
        //Fall back on code comparison
        if(o1.getCode() == o2.getCode()) return 0;
        if(null == o1.getCode()) return -1;
        if(null == o2.getCode()) return +1;
        return o1.getCode().compareTo(o2.getCode());
    }
}
