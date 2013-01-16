<jsp:directive.include file="/WEB-INF/jsp/include.jsp"/>
<portlet:renderURL var="reportUrl"/>
<c:set var="n"><portlet:namespace/></c:set>
<script type="text/javascript" src="<rs:resourceURL value="/rs/jquery/1.4.2/jquery-1.4.2.min.js"/>"></script>

<link rel="stylesheet" type="text/css" href="<c:url value="/css/degree-progress.css"/>"></link>
<style type="text/css">
.degree-progress table select { width: 20em; }
</style>

<div id="${n}" class="degree-progress">

    <h2 class="title" role="heading">Create What-If Analysis</h2>

    <p class="note">
        This tool may be used to determine the impact of changing your majors, minors, and/or concentration.
    </p>
    
    <form:form action="${ reportUrl }" method="post" commandName="whatIfForm">
        <input type="hidden" name="action" value="showWhatIf"/>
        <form:hidden path="currentTerm"/>

        <table>
            <tr>
                <td>Entry Term:</td>
                <td>
                    <form:select path="entryTerm" cssClass="entryTerm-list">
                        <form:options items="${entryTerms}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Evaluation Term:</td>
                <td>
                    <form:select path="evaluationTerm">
                        <form:options items="${evaluationTerms}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Program:</td>
                <td>
                    <form:select path="program" cssClass="program-list">
                        <form:options items="${programs}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Level:</td>
                <td>
                    <div class="level">
                        <form:hidden path="level"/>
                        <span></span>
                    </div>
                </td>
            </tr>
        
            <tr>
                <td>Degree:</td>
                <td>
                    <div class="degree">
                        <form:hidden path="degree"/>
                        <span></span>
                    </div>
                </td>
            </tr>
        
            <tr>
                <td>College:</td>
                <td>
                    <div class="college">
                        <form:hidden path="college"/>
                        <span></span>
                    </div>
                </td>
            </tr>
        
            <tr>
                <td>Campus:</td>
                <td>
                    <div class="campus">
                        <form:select path="campus" cssStyle="display:none">
                            <form:options items="${campuses}" itemValue="key" itemLabel="name" />
                        </form:select>
                        <span></span>
                    </div>
                </td>
            </tr>
        
            <tr>
                <td>Major:</td>
                <td>
                    <form:select path="major" cssClass="major-list" disabled="true">
                        <form:options items="${majors}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Concentration:</td>
                <td>
                    <form:select path="concentration" cssClass="concentration-list">
                        <form:option value="" label=""/>
                        <form:options items="${concentrations}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>

            <tr>
                <td>Concentration 2:</td>
                <td>
                    <form:select path="concentration2" cssClass="concentration2-list">
                        <form:option value="" label=""/>
                        <form:options items="${concentrations}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Minor:</td>
                <td>
                    <form:select path="minor" cssClass="minor-list">
                        <form:option value="" label=""/>
                        <form:options items="${minors}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
            <tr>
                <td>Minor 2:</td>
                <td>
                    <form:select path="minor2" cssClass="minor2-list">
                        <form:option value="" label=""/>
                        <form:options items="${minors}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>

            <tr>
                <td>Major 2:</td>
                <td>
                    <form:select path="major2" cssClass="major2-list" disabled="true">
                        <form:options items="${majors}" itemValue="key" itemLabel="name" />
                    </form:select>
                </td>
            </tr>
        
        </table>    
    
        <input type="submit" value="Create Report"/>
    </form:form>

</div>

<script type="text/javascript">
var ${n} = ${n} || {};
${n}.jQuery = jQuery.noConflict(true);
${n}.jQuery(function(){
    var $ = ${n}.jQuery;
    
    var setPrepoluatedField = function (cssClass, item) {
        $(cssClass + " span").text(item.name);
        $(cssClass + " input").val(item.key);
    };
    
    var updateInfo = function (updateMajors) {
        $.get(
            "<c:url value="/ajax/program-info"/>", 
            { program: $(".program-list").val(), term: $(".entryTerm-list").val() },
            function (data) {
                if (updateMajors) {
                    $(".major-list").html("").removeAttr("disabled");
                    $(data.majors).each(function (idx, major) {
                        $(".major-list").append($(document.createElement("option")).val(major.key).text(major.name));
                    });
                    $(".major2-list").html("").removeAttr("disabled");
                    $(".major2-list").append($(document.createElement("option")).val(""));
                    $(data.majors).each(function (idx, major) {
                        $(".major2-list").append($(document.createElement("option")).val(major.key).text(major.name));
                    });
                }
                
                setPrepoluatedField(".college", data.programInfo.college);
                setPrepoluatedField(".degree", data.programInfo.degree);
                setPrepoluatedField(".level", data.programInfo.level);

                if (data.programInfo.campus.key) {
                    $(".campus span").text(data.programInfo.campus.name).show();
                    $(".campus select").val(data.programInfo.campus.key).hide();
                } else {
                    $(".campus select").show();
                    $(".campus span").hide();
                }
                
            },
            "json"
        );
    };
    
    $(".program-list").change(function(){ updateInfo(true); });
    $(".entryTerm-list").change(function(){ updateInfo(false); });
    
    $(document).ready(function () {
        updateInfo(true);
    });

});
</script>