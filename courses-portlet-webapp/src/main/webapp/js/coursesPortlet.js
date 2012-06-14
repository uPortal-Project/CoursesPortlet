//Create the coursesPortlet variable if it does not already exist
var coursesPortlet = coursesPortlet || {};

/*
 * Switch jQuery to extreme noConflict mode, keeping a reference to it in the coursesPortlet
 * variable if one doesn't already exist
 */
if (!coursesPortlet.jQuery) {
    coursesPortlet.jQuery = jQuery.noConflict(true);
}
else {
    jQuery.noConflict(true);
}

(function($, fluid, coursesPortlet) {
    "use strict";
    
    if (coursesPortlet.commonInit) {
        return;
    }
    
    /*
     * Adds a handler for updating the grades content when the term changes
     */
    coursesPortlet.updateGradesTermHandler = function(options) {
        var defaults = {
            termSelector: undefined,
            coursesContentSelector: undefined,
            coursesDataSelector: '#grades-course-list',
            footerContentSelector: undefined,
            footerDataSelector: '#grades-footer',
            loadingSelector: undefined,
            errorSelector: undefined,
            errorMessageSelector: '.error_message',
            errorMessage: '',
            dataUrl: undefined
        };
        
        //Overlay the provided options on top of the defaults
        var opts = $.extend(true, {}, defaults, options);
        
        $.log("Initializing update term handler using URL: " + JSON.stringify(opts));
        
        var termSelector = $(opts.termSelector);
        termSelector.change(function() {
            var newTerm = $(opts.termSelector + " option:selected").val();
            $.log(opts.termSelector + ": Term selector changed to: " + newTerm);
            
            //Show spinner
            $(opts.loadingSelector).show();
            
            //Request the course data using the selected term
            $.ajax({
                type: 'POST',
                url: opts.dataUrl,
                data: { termCode : newTerm },
                traditional: true,
                success: function(data) {
                    $.log(opts.coursesContentSelector + ": Updating courses view using data: " + data);

                    //Convert the html string into a DOM
                    var dataDom = $(data);
                    
                    var courseData = $(opts.coursesDataSelector, dataDom);
                    var footerData = $(opts.footerDataSelector, dataDom);
                    
                    //Have to re-show the content in case the last update resulted in an error
                    $(opts.coursesContentSelector).show();
                    $(opts.footerContentSelector).show();
                    $(opts.errorSelector).hide();
                    
                    $(opts.coursesContentSelector).html(courseData);
                    $(opts.footerContentSelector).html(footerData);
                    
                    //hide spinner
                    $(opts.loadingSelector).hide();
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    $.log(opts.coursesContentSelector + ": Failed to load data for term " + newTerm + " status=" + textStatus + " error=" + errorThrown);
                    
                    //Hide the content
                    $(opts.coursesContentSelector).hide();
                    $(opts.footerContentSelector).hide();

                    //Show and update the error message
                    $(opts.errorSelector).show();
                    $(opts.errorSelector + ' ' + opts.errorMessageSelector).text(opts.errorMessage)
                    
                    //hide spinner
                    $(opts.loadingSelector).hide();
                },
                dataType: "html"
            });
        });
    };

    coursesPortlet.commonInit = true;
})(coursesPortlet.jQuery, coursesPortlet.fluid, coursesPortlet);