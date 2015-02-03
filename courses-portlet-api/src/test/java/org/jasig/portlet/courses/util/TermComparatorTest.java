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
package org.jasig.portlet.courses.util;

import static org.junit.Assert.*;

import org.jasig.portlet.courses.model.xml.Term;
import org.joda.time.DateTime;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class TermComparatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testCompare() {
		Term term1 = null;
		Term term2 = null;
		Term term3 = new Term();
		Term term4 = new Term();
		term3.setCode("1142");
		term4.setCode("1142");
		DateTime start = new DateTime();
		term3.setStart(start);
		term4.setStart(start);
		//null test
		assertEquals("They are not equal",0,new TermComparator().compare(term1,term2));
		//null vs not null
		assertEquals("They are not equal",-1,new TermComparator().compare(term1,term3));
		//not null vs null
		assertEquals("They are not equal",+1,new TermComparator().compare(term3,term1));
		//same
		assertEquals("They are not equal",0,new TermComparator().compare(term3,term4));
		term4.setStart(start.plusMinutes(5));
		//different
		assertEquals("They are not equal",-1,new TermComparator().compare(term3,term4));
		//different
		assertEquals("They are not equal",+1,new TermComparator().compare(term4,term3));
		term1 = new Term();
		term2 = new Term();
		assertEquals("They are not equal",0,new TermComparator().compare(term1,term2));
		term1.setCode("1142");
		assertEquals("They are not equal",+1,new TermComparator().compare(term1,term2));
		term1.setCode(null);
		term2.setCode("1142");
		assertEquals("They are not equal",-1,new TermComparator().compare(term1,term2));
	}

}
