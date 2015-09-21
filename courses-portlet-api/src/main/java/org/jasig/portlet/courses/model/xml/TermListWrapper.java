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
package org.jasig.portlet.courses.model.xml;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * Adds base functionality to the {@link TermSummary} object
 * 
 * @author Eric Dalquist
 */
public abstract class TermListWrapper {

    public Term getTerm(String termCode) {

        for (Term term : getTerms()) {
            if (termCode.equals(term.getCode())) {
                return term;
            }
        }

        return null;
    }

    public Term getCurrentTerm() {
        Term rslt = null;  // default, but not helpful
        int bestDist = Integer.MAX_VALUE;
        final int currentYear = new GregorianCalendar().get(Calendar.YEAR);

        List<Term> terms = getTerms();
        for (Term term : terms) {
            if (term.getCurrent()) {
                return term;
            }

            //If terms have years set determine a fall-back term based on the current year and term years
            final BigInteger termYear = term.getYear();
            if (termYear != null) {
                if (rslt == null) {
                    rslt = term;
                    bestDist = Math.abs(termYear.intValue() - currentYear);
                }
                else {
                    final int dist = Math.abs(termYear.intValue() - currentYear);
                    if (dist < bestDist) {
                        rslt = term;
                    }
                }
            }
        }

        if (rslt == null && !terms.isEmpty()) {
            /*
             * We have terms -- so we're obligated to choose one -- but we have
             * no reason to prefer one over the others;  just choose the first.
             */
            rslt = terms.get(0);
        }

        return rslt;
    }

    public abstract List<Term> getTerms();

}
