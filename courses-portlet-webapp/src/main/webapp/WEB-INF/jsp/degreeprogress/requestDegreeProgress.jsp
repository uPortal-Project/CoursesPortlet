<%--

    Licensed to Jasig under one or more contributor license
    agreements. See the NOTICE file distributed with this work
    for additional information regarding copyright ownership.
    Jasig licenses this file to you under the Apache License,
    Version 2.0 (the "License"); you may not use this file
    except in compliance with the License. You may obtain a
    copy of the License at:

    http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing,
    software distributed under the License is distributed on
    an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
    KIND, either express or implied. See the License for the
    specific language governing permissions and limitations
    under the License.

--%>
<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>

<form:form action="submitUrl" modelAttribute="degreeForm" method="post">

    <p>
        Students can run a degree audit for their current degree to view which 
        courses have been taken and which courses still need to be required
        for their current program.  Students can also choose to look at 
        what their degree progress would be for a different program to see what
        the impact would be of changing majors, minors, and/or concentration.
    </p>

    <table>
        <tr>
            <td><form:label path="major">Major:</form:label></td>
            <td><form:input path="major"/></td>
        </tr>
        <tr>
            <td><form:label path="concentration">Concentration:</form:label></td>
            <td><form:input path="concentration"/></td>
        </tr>
        <tr>
            <td><form:label path="minor">Minor:</form:label></td>
            <td><form:input path="minor"/></td>
        </tr>
    </table>

    <div class="buttons">
        <input type="submit" value="Run Audit"/>
    </div>
    
</form:form>