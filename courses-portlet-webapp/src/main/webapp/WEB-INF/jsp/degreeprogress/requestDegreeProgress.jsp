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