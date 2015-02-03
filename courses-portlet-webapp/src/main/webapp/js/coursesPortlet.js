/*
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

(function($, fluid, coursesPortlet, up$, moment) {
    "use strict";
    
    if (coursesPortlet.commonInit) {
        return;
    }
    
    /*
     * Adds a handler for updating the grades content when the term changes
     */
    coursesPortlet.updateGradesTermHandler = function(options) {
        var defaults = {
            termEl: undefined,
            gradesSelector: undefined,
            loadingEl: undefined,
            errorEl: undefined,
            errorMessageSelector: '.error_message',
            errorMessage: '',
            dataUrl: undefined
        };
        
        //Overlay the provided options on top of the defaults
        var opts = $.extend(true, {}, defaults, options);
        
        $.log("Initializing update term handler using URL: " + opts.dataUrl);
        
        opts.termEl.change(function() {
            var newTerm = opts.termEl.val();
            $.log("Term selector changed to: " + newTerm);

            //Show spinner
            opts.loadingEl.show();
            
            //Request the course data using the selected term
            $.ajax({
                type: 'POST',
                url: opts.dataUrl,
                data: { termCode : newTerm },
                traditional: true,
                success: function(data) {
                    $.log("Updating courses view using data: " + data);

                    // Update using the uPortal jQuery so we can initiate mobile enhancement
                    var $grades = up$(opts.gradesSelector);
                    var $gradesParent = $grades.parent();
                    $grades.remove();
                    $gradesParent.append(data).trigger("create");

                    //Have to re-show the content in case the last update resulted in an error
                    opts.errorEl.hide();

                    //hide spinner
                    opts.loadingEl.hide();
                },
                error: function(jqXHR, textStatus, errorThrown) {
                    $.log("Failed to load data for term " + newTerm + " status=" + textStatus + " error=" + errorThrown);
                    
                    //Hide the content
                    $(tops.gradesSelector).hide();

                    //Show and update the error message
                    opts.errorEl.show();
                    opts.errorEl.find(opts.errorMessageSelector).text(opts.errorMessage);
                    
                    //hide spinner
                    opts.loadingEl.hide();
                },
                dataType: "html"
            });
        });
    };

    coursesPortlet.createScheduleGrid = (function(){
      var colors = [
        '#ff7373', // reddish
        '#ffb265', // orangish
        '#ffff65', // yellowish
        '#57d957', // greenish
        '#8181ff', // bluish
        '#c070ff', // violetish
        '#efefef'  // boring default grey
      ];
      var dateFormat = "MMM DD YYYY";

      var defaultOptions = {
        orientation: 'portrait',
        startDay: 0,
        endDay: 6,
        nowLine: true,
        amPm: true,
        dayTitleSize: 40,
        dayTitleCalcFontSize: false,
        dayViewThreshold: 1,
        hourTitleSize: 72,
        ActivityOptions: {
          mouseoverMinWidth: 150,
          mouseoverMinHeight: 115,
          mouseoverAnimation: false,
          colour: '#efefef',
          content: function() {
              this.setColorForSession();

              return '<div role="link"><div class="tt-activityTitle">' +
                     this.courseDescr + " " + this.courseCode + '</div>' +
                     "<div>" + this.meetingDescr + "</div>" +
                     "<div>" + this.room + "&nbsp;" + this.locationDisplayName + "</div>" +
                     "<div>" + this.sessionStartEndDate + "</div>" +
                     '<div>' + this.timeRangeDisplay + '</div></div>';
          },
          mouseoverEasing: "linear"
        }
      };

      var defaultMobileOptions = {
        isMobile: false,
        dayView: false,
        dayViewChangeEvent: "swipe"
      };

      return function($grid, activities, mobile, standalone, tt_options) {
        var options = {},
            colorLookup = {},
            dateList = [],
            seenSessions = {};

        var setColorForSession = function(){
          this.colour = colorLookup[this.sessionCode];
        };

        $.each(activities, function(i, activity){
          var startDate = activity.sessionStartEndDate.substring(0,7) +
                          activity.sessionStartEndDate.substring(
                            activity.sessionStartEndDate.length-4
                          );

          if( !seenSessions[activity.sessionCode] ){
            dateList.push({
              session: activity.sessionCode,
              date: moment(startDate,dateFormat).unix()
            });
            seenSessions[activity.sessionCode] = true;
          }
          activity.setColorForSession = setColorForSession;
        });

        // In the case of a single session, default to grey
        if(dateList.length === 1) {
          colorLookup[dateList[0].session] = colors[colors.length-1];
        } else {
          dateList.sort(function(a,b){
            return a.date - b.date;
          });
          $.each(dateList, function(i, s){
            colorLookup[s.session] = colors[i > colors.length-1 ?
              colors.length-1 : i];
          });
        }

        $.extend(true, options, defaultOptions);
        if( mobile ) { $.extend(true, options, defaultMobileOptions); }

        if( typeof tt_options !== 'undefined' ) {
          $.extend(true, options, tt_options);
        }

        options.activities = activities;

        var timetable = $grid.timetable(options).data('timetable');

        if( mobile && timetable.isDayView() ) {
          $grid.on('click', '.tt-dayTitle', function(e) {
            timetable.daysContainer.__dayChange(e);
            e.stopPropagation();
            return false;
          });
        }

        if( ! standalone ) {
          // Event handler for activity links... to link to detail views
          $grid.parent().on('click', '.tt-activity', function(e) {
            window.location = $(this).data('activity').url;
          });
        }

        return timetable;
      };
    })();

    coursesPortlet.commonInit = true;
})(coursesPortlet.jQuery, coursesPortlet.fluid, coursesPortlet, up.jQuery, coursesPortlet.moment);