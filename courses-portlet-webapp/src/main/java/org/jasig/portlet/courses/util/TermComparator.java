package org.jasig.portlet.courses.util;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Comparator;

import org.jasig.portlet.courses.model.xml.Term;

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
        
        //Try comparing by start cal first
        final Calendar s1 = o1.getStart();
        final Calendar s2 = o2.getStart();
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
        return o1.getCode().compareTo(o2.getCode());
    }
}
