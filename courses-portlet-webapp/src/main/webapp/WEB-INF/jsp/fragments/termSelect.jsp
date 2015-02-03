<%--

    Licensed to Apereo under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Apereo licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License.  You may obtain a
    copy of the License at the following location:

      http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on an
    "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied.  See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
    <span class="term-header">Choose term:</span>
    <select id="${n}_termPicker" name="termCode">
      <c:forEach var="term" items="${termList.terms}">
        <c:set var="selected" value="" />
        <c:if test="${term.code == selectedTerm.code}">
          <c:set var="selected" value="selected=\"selected\"" />
        </c:if>
        <option value="${term.code}" ${selected}>${term.displayName}</option>
      </c:forEach>
    </select>
