(function($) {
    var TimeTable = function(options, container) {

        var defaultOptions = {
            orientation: "landscape",
            dayView: false,
            dayViewThreshold: 300,
            minHourSize: 50,
            minDaySize: 50,
            dayTitleSize: 75,
            dayTitleCalcFontSize: true,
            hourTitleSize: 75,
            startHour: 9,
            endHour: 17,
            nowLine: true,
            amPm: false,
            startDay: 1,
            endDay: 5,
            activities: [],
            NoContentMsg: "No Activities to Display",
            loadingMsg: "Loading...",
            loadErrorMsg: "An error occured loading the Timetable",
            hoursOptions: {
                events: {}
            },
            daysOptions: {
                events: {}
            },
            ActivityOptions: {
                mouseoverDelay: 500,
                mouseoverMinHeight: 0,
                mouseoverMinWidth: 0,
                mouseoverAnimation: true,
                mouseoverEasing: "easeOutElastic",
                mouseoverSpeed: "normal",
                zindex: 1000,
                events: {}
            }
        };

        container = $(container);
        var OptionsDependant = function(container) {
            this.container = container;
        };

        var settings = $.extend(true, defaultOptions, options);

        OptionsDependant.prototype = {
            orientation: function() {

                if (this.isDayView()) {
                    return 'portrait';
                } else {
                    return settings.orientation;
                }

            },
            isMobile: function() {
                if (settings['isMobile'] != null && settings['isMobile'] != undefined) {
                    if ($.isFunction(settings.isMobile)) {
                        return settings.isMobile();
                    } else {
                        return settings.isMobile;
                    }
                } else {
                    return /Android|webOS|iPhone|iPod|BlackBerry/i.test(navigator.userAgent);
                }
            },
            isDayView: function() {
                if (settings.dayView || this.container.width() < settings.dayViewThreshold || (this.isMobile())) {
                    return true;
                } else {
                    return false;
                }
            }
        };



        function createTimeTable() {
            return $.extend(
                    true,
                    new OptionsDependant(container),
                    {
                        hoursContainer: new HoursContainer(),
                        daysContainer: new DaysContainer(),
                        messageOverlay: new MessageOverlay(),
                        nowLine: new NowLine(),
                        name: "timetable",
                        _create: function() {
                            var cssObj = {
                                "position": "relative"
                            };
                            this.container
                                    .addClass("tt-container")
                                    .css(cssObj)
                                    .attr("role", "grid");
                            if (this.isMobile()) {
                                this.container.addClass("tt-mobile");
                            }
                        },
                        _init: function() {
                            this.daysContainer.init();
                            this.hoursContainer.init();
                            this.messageOverlay.init();
                            this.nowLine.init();
                            this.resize();
                        },
                        option: function(key, value) {

                            if ($.isPlainObject(key)) {
                                settings = $.extend(true, settings, key);
                                $.each(key, $.proxy(function(key, val) {
                                    var evnt = {
                                        type: key + "Changed",
                                        newValue: val
                                    }
                                    this.container.trigger(evnt);
                                }, this));

                            } else if (key && typeof value === "undefined") {
                                return settings[ key ];
                            } else {
                                settings[ key ] = value;
                                var evnt = {
                                    type: key + "Changed",
                                    newValue: value
                                }
                                this.container.trigger(evnt);
                            }

                            this.container.trigger("tt.update");
                            return this;
                        },
                        render: function(activityList) {

                            if (typeof activityList !== 'undefined') {
                                settings.activities = activityList;
                            }
                            this.daysContainer.renderActivities();


                        },
                        refresh: function() {
                            this.activityContainer.render();
                        },
                        resize: function() {

                            this.container.trigger("tt.update");

                        }
                    }
            );
        }
        ;


        var HoursContainer = function() {
            var HC = new OptionsDependant(container);

            var posRef = {
                landscape: {
                    size: "width",
                    nonsize: "height",
                    position: "left",
                    nonposition: "top"
                },
                portrait: {
                    size: "height",
                    nonsize: "width",
                    position: "top",
                    nonposition: "left"
                }
            };

            $.extend(true, HC, {
                hour: 0,
                init: function() {
                    this.hours = this._generateHours();
                    this.render();
                },
                _generateHours: function() {
                    var aHours = new Array(),
                        hour,
                        hourTitle;

                    if( settings.amPm ){
                        for (hour = 0; hour < 24; hour++) {
                            hourTitle = $("<div/>", {"class": "tt-hourTitle"}).text(
                                (hour === 0 ? "12 " :
                                (hour > 12 ? (hour - 12)+" " : hour+" ")) +
                                (hour > 11 ? "PM" : "AM")
                            );
                            aHours.push($("<div/>", {"class": "tt-hour", style: "width: 100%;", "aria-hidden": "true"})
                              .append(hourTitle).on(this.events));
                        }
                    } else {
                        for (hour = 0; hour < 24; hour++) {
                            hourTitle = $("<div/>", {"class": "tt-hourTitle"}).text(
                                (hour < 10) ? "0" + hour + "00" : hour + "00"
                            );
                            aHours.push($("<div/>", {"class": "tt-hour", style: "width: 100%;", "aria-hidden": "true"})
                              .append(hourTitle).on(this.events));
                        }
                    }
                    return aHours;
                },
                render: function() {
                    this.container.append(this.hours.slice(settings.startHour, settings.endHour + 1));
                    this.hour = settings.endHour - settings.startHour + 1;
                    this.resize();
                },
                resize: function() {
                    settings.hourSize = (parseInt(this.container[posRef[this.orientation()].size]()) - settings.dayTitleSize) / (this.hour);
                    settings.hourNumber = this.hour;

                    if (settings['minHourSize'] != null && settings.hourSize < settings.minHourSize) {
                        settings.hourSize = settings.minHourSize;

                        var cssObj = {};
                        cssObj[posRef[this.orientation()].size] = (settings.hourSize * this.hour) + settings.dayTitleSize;
                        this.container.css(cssObj);

                    }
                    var size = settings.hourSize;
                    var offset = settings.dayTitleSize;
                    var that = this;
                    $("div.tt-hour", this.container).each(function(index, el) {
                        var cssObj = {
                            position: "absolute",
                            height: "100%"
                        };
                        cssObj[posRef[that.orientation()].size] = size;
                        cssObj[posRef[that.orientation()].nonsize] = "100%";
                        cssObj[posRef[that.orientation()].position] = offset + (size * index);
                        cssObj[posRef[that.orientation()].nonposition] = 0;
                        $(this).css(cssObj);
                    });
                }

            }, settings.hoursOptions);

            return HC;
        };


        var DaysContainer = function() {
            var DC = new OptionsDependant(container);

            var posRef = {
                portrait: {
                    size: "width",
                    nonsize: "height",
                    position: "left",
                    nonposition: "top"
                },
                landscape: {
                    size: "height",
                    nonsize: "width",
                    position: "top",
                    nonposition: "left"
                }
            };
            $.extend(true, DC, {
                dayNames: {
                    en: ["Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"],
                    fr: ["Dim", "Lun", "Mar", "Mer", "Jeu", "Ven", "Sam"],
                    de: ["Son", "Mon", "Die", "Mit", "Don", "Fr", "Sam"],
                    es: ["Dom", "Lun", "Mar", "Mie", "Jue", "Vie", "Sab"],
                    it: ["Dom", "Lun", "Mar", "Mer", "Gio", "Ven", "Sab"]
                },
                lang: "en",
                day: 0,
                init: function() {
                    this.days = this._generateDays();
                    var now = new Date();
                    var today = now.getDay();
                    if (today > settings.endDay || today < settings.startDay) {
                        this.viewDay = settings.startDay;
                    } else {
                        this.viewDay = today;
                    }

                    this.render();
                },
                touch: {},
                move: 0,
                nextDay: $('<div class="tt-mobNxtDay"></div>').bind("click touchstart", function(event) {
                    DC.viewDay = Math.min(Math.max(DC.viewDay + 1, settings.startDay), settings.endDay);
                    DC.render();
                    event.preventDefault();
                    return false;
                }),
                previousDay: $('<div class="tt-mobPrevDay"></div>').bind("click touchstart", function(event) {
                    DC.viewDay = Math.min(Math.max(DC.viewDay - 1, settings.startDay), settings.endDay)
                    DC.render();
                    event.preventDefault();
                    return false;
                }),
                _generateDayHeaders: function() {
                    var days = [];
                    $.each(this.dayNames[this.lang], function(index, day) {
                        var $dayNode = $("<div/>", {"class": "tt-dayTitle", "role": "rowheader"}).text(day);
                        days.push($dayNode);
                    });
                    return days;
                },
                _generateDays: function() {
                    var eventHandlers = this.events,
                        days = [],
                        today = (new Date()).getDay(),
                        dayHeaders = this._generateDayHeaders();
                    $.each(dayHeaders, function(index, $day) {
                        days.push(
                            $("<div/>", {
                                "class": "tt-day" + (today === index ? " tt-today" : ""),
                                "role": "row"
                            })
                            .on(eventHandlers)
                            .append($day)
                        );
                    });

                    return days;
                },
                render: function() {
                    if (this.isDayView()) {
                        $('.tt-day', this.container).detach();
                        this.container.prepend(this.days[this.viewDay].hide().fadeIn("fast"));
                        this.container.prepend(this.nextDay);
                        this.container.prepend(this.previousDay);
                        this.day = 1;
                    } else {
                        this.nextDay.detach();
                        this.previousDay.detach();
                        this.container.append(this.days.slice(settings.startDay, settings.endDay + 1));
                        this.day = settings.endDay - settings.startDay + 1;
                    }
                    this.resize();
                },
                daysActivities: new Array(),
                defaultAjax: {
                    beforeSend: $.proxy(function() {
                        this.container.trigger("tt-activitiesLoading");
                    }, DC),
                    success: $.proxy(function() {
                        this.container.trigger("tt-activitiesLoaded");
                    }, DC)
                },
                ajaxOn: null,
                renderActivities: function() {
                    var toabort = false;
                    if (this.ajaxOn !== null) {
                        toabort = this.ajaxOn;
                    }
                    $('.tt-activity', this.container).fadeOut().remove();

                    $.each(this.daysActivities, function(index, activity) {
                        activity.remove();
                        delete activity;
                    });


                    if ($.isFunction(settings.activities)) {
                        this.__populateActivities(settings.activities());
                    } else if ($.isPlainObject(settings.activities)) {
                        var ajaxSettings = $.extend(settings.activities, this.defaultAjax);
                        this.ajaxOn = $.ajax(ajaxSettings);
                    } else if ($.isArray(settings.activities)) {
                        this.__populateActivities(settings.activities);
                    } else {
                        this.ajaxOn = $.ajax(settings.activities, this.defaultAjax);
                    }

                    if (toabort)
                        toabort.abort();
                    if (this.ajaxOn !== null) {
                        this.ajaxOn.done(function(data) {
                            container.trigger("tt-renderStart");
                        }).done($.proxy(this.__populateActivities, this)).fail(function() {
                            container.trigger("tt-activityLoadFailed");
                        });

                    }


                },
                __populateActivities: function(activities) {
                    if (activities.length > 0) {
                        $.each(activities, $.proxy(function(index, activityData) {

                            var activity = new Activity(this.days[activityData.scheduledDay], activityData);

                            this.daysActivities.push(activity);
                        }, this));
                        container.trigger("tt-activitiesRendered");
                    } else {
                        container.trigger("tt-noActivities");
                    }
                },
                addActivity: function(activity) {
                    var day = this.days[activity.dow];
                    activity.container = day;
                    activity._attach();
                },
                resize: function() {
                    settings.daySize = (parseInt(this.container[posRef[this.orientation()].size]()) - settings.hourTitleSize) / this.day;

                    if (settings['minDaySize'] != null && settings.daySize < settings.minDaySize) {
                        settings.daySize = settings.minDaySize;

                        var cssObj = {};
                        cssObj[posRef[this.orientation()].size] = (settings.daySize * this.day) + settings.hourTitleSize;
                        this.container.css(cssObj);

                    }

                    var size = settings.daySize;
                    var offset = settings.hourTitleSize;

                    settings.dayNumber = this.day;

                    var that = this;
                    $("div.tt-day", this.container).each(function(index, el) {
                        var cssObj = {
                            position: "absolute"
                        };
                        cssObj[posRef[that.orientation()].size] = size;
                        cssObj[posRef[that.orientation()].nonsize] = "100%";
                        cssObj[posRef[that.orientation()].position] = offset + (size * index);
                        cssObj[posRef[that.orientation()].nonposition] = 0;
                        $(this).css(cssObj);
                    });

                    var titlePos = {
                        position: "absolute",
                        "text-align": "center"
                    };

                    if( settings.dayTitleCalcFontSize ) {
                        titlePos[posRef[that.orientation()].size] = "100%";
                        titlePos[posRef[that.orientation()].nonsize] = settings.dayTitleSize;
                        var hidden = $("<span/>", {style: "visibility:hidden;width:auto;height:auto;font-size:5px;"});
                        hidden.text(this.dayNames[this.lang][0]);
                        this.container.append(hidden);
                        var hiddenSize = {
                            "width": hidden.width(),
                            "height": hidden.height()
                        };
                        while (hiddenSize[posRef[that.orientation()].size] < size / 2 && hiddenSize[posRef[that.orientation()].nonsize] < settings.dayTitleSize/ 2) {
                            var fs = parseInt(hidden.css("font-size"), 10);
                            hidden.css({
                                "font-size": (fs + 1) + "px"
                            });
                            hiddenSize.width = hidden.width();
                            hiddenSize.height = hidden.height();
                        }
                        titlePos["font-size"] = hidden.css("font-size");

                        hidden.remove();

                        $('div.tt-dayTitle', this.container).each(function(index, el) {
                            $(this).css(titlePos);
                            $(this).css({
                                "line-height": settings.dayTitleSize + "px"
                            });
                        });
                    }

                }
            }, settings.daysOptions);

            container.on("tt-activitiesChanged", $.proxy(DC.renderActivities, DC));

            return DC;
        };


        var NowLine = function() {
            var NL = new OptionsDependant(container);

            var posRef = {
                landscape: {
                    size: "width",
                    nonsize: "height",
                    position: "left",
                    nonposition: "top"
                },
                portrait: {
                    size: "height",
                    nonsize: "width",
                    position: "top",
                    nonposition: "left"
                }
            };

            $.extend(true, NL, {
                now: new Date(),
                init: function() {
                    var that = this;
                    if( settings.nowLine && this.isDayView() ) {
                        this.container.on("tt.dayChange", function(){
                            if( that.container.find('.tt-today')[0] ) {
                                that.render();
                            }
                        });
                    }
                },
                render: function() {
                    var hourIndex = this.now.getHours() - settings.startHour,
                        hourOffset = (settings.hourSize / 60) * this.now.getMinutes();

                    if( this.element ) {
                        this.element.remove();
                    }
                    if( this.now.getHours() >= settings.startHour &&
                        this.now.getHours() <= settings.endHour )
                    {
                        cssObj = {
                            "position": "absolute",
                            "z-index": settings.ActivityOptions.zindex+1
                        };
                        cssObj[posRef[this.orientation()].size] = "2px";
                        cssObj[posRef[this.orientation()].nonsize] = "100%";
                        cssObj[posRef[this.orientation()].position] = (hourIndex * settings.hourSize) + hourOffset + settings.dayTitleSize;

                        this.element = $("<div/>", { "class": "tt-nowLine", "aria-hidden": "true" }).css(cssObj);
                        this.container.find('.tt-today').append(this.element);
                    }
                }
            });

            return NL;
        }

        var Activity = function(activityContainer, data) {
            var A = new OptionsDependant(activityContainer);

            var posRef = {
                landscape: {
                    hour: "left",
                    day: "top",
                    size: "width",
                    nonsize: "height"
                },
                portrait: {
                    hour: "top",
                    day: "left",
                    size: "height",
                    nonsize: "width"
                }
            };

            $.extend(true, A, {
                color: "#C0A3D1",
                title: "Event Title",
                startTime: "00:00",
                scheduledDay: 0,
                duration: 60,
                activityMargin: 0,
                activityObj: $("<div/>", {
                    "class": "tt-activity",
                    style: "position: relative; display: none; overflow: hidden; z-index: " + settings.ActivityOptions.zindex,
                    "role": "gridcell",
                    "tabindex": 0
                }),
                _init: function() {

                    var start = this.startTime.split(":");

                    this.dow = parseInt(this.scheduledDay);
                    this.hour = parseInt(start[0], 10);
                    this.minute = parseInt(start[1], 10);

                    var changed = false;
                    if (this.hour < settings.startHour) {
                        settings.startHour = this.hour;
                        changed = true;
                    }
                    if (this.hour + (this.duration / 60) > settings.endHour) {
                        settings.endHour = Math.ceil(this.hour + (this.duration / 60));
                        changed = true;
                    }
                    if (this.dow < settings.startDay) {
                        settings.startDay = this.dow;
                        changed = true;
                    }
                    if (this.dow > settings.endDay) {
                        settings.endDay = this.dow;
                        changed = true;
                    }
                    this._attach();


                    if (changed === true) {
                        container.trigger("tt.update");
                    } else {
                        this.resize();
                    }

                    this._setColour();
                    this.container.on("activityAdded", $.proxy(this._onActivityAdded, this));

                    this.activityObj.data("activity", this);

                },
                overlaps: new Array(),
                addOverlap: function(overlap) {
                    this.overlaps.push(overlap);
                },
                _onActivityAdded: function(event) {
                    if (this.overlapWith(event.activity)) {
                        this.negotiatePosition(event.activity);
                        this.addOverlap(event.activity);
                        event.activity.addOverlap(this);
                    }
//                    this.container.trigger("tt.event.update");
                },
                overlapWith: function(activity) {
                    var thisTime = (this.hour * 60) + this.minute;
                    var thatTime = (activity.hour * 60) + activity.minute;
                    if (
                            (thisTime >= thatTime && thisTime < thatTime + activity.duration) ||
                            (thatTime >= thisTime && thatTime < thisTime + this.duration)
                            )
                        return true;
                },
                negotiatePosition: function(activity) {
                    if (activity.sizeFactor < this.sizeFactor) {
                        activity.sizeFactor++;
                    } else if (activity.sizeFactor > this.sizeFactor) {
                        this.sizeFactor++;
                    } else {
                        activity.sizeFactor++;
                        this.sizeFactor++;
                    }

                    var positions = new Array();
                    positions.push(this.position);
                    $.each(activity.overlaps, function(index, overlap) {
                        positions.push(overlap.position);
                    });
                    while ($.inArray(activity.position, positions) != - 1)
                        activity.position++;


                },
                position: 0,
                sizeFactor: 1,
                content: function() {
                    return this.title + "<br />" + this.hour + ":" + this.minute + " - " + this.duration + " Minutes";
                },
                _setColour: function() {
                    var baseColour = this.activityObj.css("background-color");
                    if (this.color)
                        baseColour = this.color;
                    if (this.colour)
                        baseColour = this.colour;


                    if ($.isFunction($.Color)) {

                        var startColour = $.Color(baseColour);
                        var lightness = startColour.lightness();
                        var change = lightness / 4;
                        var endColour = startColour.lightness("-=" + change);

                        var css = {
                            background: 'linear-gradient(127deg, ' + startColour.toHexString() + ' 25%, ' + endColour.toHexString() + ' 100%)'
                        }

                        this.activityObj.css(css);

                    }
                    this.activityObj.css({"background-color": baseColour});
                },
                _attach: function() {
                    var content = "";
                    if ($.isFunction(this.content)) {
                        content = $.proxy(this.content, this);
                        this.activityObj.html(content());
                    } else
                        this.activityObj.html(content);
                    this.container.trigger({
                        type: "activityAdded",
                        activity: this
                    });
                    this.activityObj.fadeIn("slow");
                    this.resize();
                },
                remove: function() {
                    this.activityObj.fadeOut("slow").remove();
                    this.container.off("activityAdded", this._onActivityAdded);
                },
                __activityCSSObj: {
                    position: "absolute"
                },
                render: function() {
                    this.container.append(this.activityObj);
                    this.activityObj.css(this.__activityCSSObj);
                },
                resize: function() {
                    var dayIndex = this.dow - settings.startDay;
                    var hourIndex = this.hour - settings.startHour;
                    var hourOffset = (settings.hourSize / 60) * this.minute;

                    var expandto = {
                        position: 1000000,
                        sizeFactor: 0
                    };
                    $.each(this.overlaps, $.proxy(function(index, activity) {
                        if (activity.position > this.position && activity.position <= expandto.position && activity.sizeFactor >= expandto.sizeFactor) {
                            expandto = activity;
                        }
                    }, this));

                    var activityWidth = (settings.daySize) / (this.sizeFactor);

                    this.__activityCSSObj[posRef[this.orientation()].hour] = (hourIndex * settings.hourSize) + hourOffset + settings.dayTitleSize;
                    this.__activityCSSObj[posRef[this.orientation()].day] = (activityWidth * this.position) + this.activityMargin;
                    this.__activityCSSObj[posRef[this.orientation()].size] = this.duration * (settings.hourSize / 60);
                    if (expandto.sizeFactor > 0 && (activityWidth * this.position + activityWidth) != ((settings.daySize / expandto.sizeFactor) * expandto.position)) {
                        activityWidth += ((settings.daySize / expandto.sizeFactor) * expandto.position) - (activityWidth * this.position + activityWidth);
                        this.__activityCSSObj[posRef[this.orientation()].nonsize] = activityWidth - this.activityMargin - 1;
                    } else {
                        this.__activityCSSObj[posRef[this.orientation()].nonsize] = activityWidth - (this.activityMargin * 2) - 1;
                    }

                },
                events: {
                    "focus mouseenter": function() {
                        var content = (this.activityObj) ? this.activityObj : this;
                        if (content.expanded != true) {
                            content.expanded = true;
                            var position = $(content).position();
                            content.oldH = $(content).height();
                            content.oldW = $(content).width();
                            content.oldT = position.top;
                            content.oldL = position.left;
                            this.to = setTimeout(function() {
                                var css = {
                                    "z-index": settings.ActivityOptions.zindex+2
                                };
                                $(content).css({
                                    "z-index": settings.ActivityOptions.zindex+2,
                                    "box-shadow": "0 6px 10px rgba(0,0,0,0.75)"
                                });
                                var width = Math.max(content.scrollWidth, settings.ActivityOptions.mouseoverMinWidth) + 5;
                                var height = Math.max(content.scrollHeight, settings.ActivityOptions.mouseoverMinHeight);
                                var diffH = height - content.oldH;
                                var diffW = width - content.oldW;
                                if (diffH > 0) {
                                    css['top'] = "-=" + diffH / 2;
                                    css['height'] = "+=" + diffH;
                                }
                                if (diffW > 0) {
                                    css['left'] = "-=" + diffW / 2;
                                    css['width'] = "+=" + diffW;
                                }

                                if( settings.ActivityOptions.mouseoverAnimation ){
                                    $(content).animate(css, settings.ActivityOptions.mouseoverSpeed, settings.ActivityOptions.mouseoverEasing);
                                } else {
                                    $(content).css(css);
                                }
                                $(content).addClass('tt-activity-expanded');

                            }, settings.ActivityOptions.mouseoverDelay);
                        }
                    },
                    "blur mouseleave": function() {
                        clearTimeout(this.to);
                        if (this.expanded == true) {
                            this.expanded = false;
                            var css = {
                                width: this.oldW,
                                height: this.oldH,
                                top: this.oldT,
                                left: this.oldL,
                                "z-index": settings.ActivityOptions.zindex
                            };

                            $(this).css({
                                "box-shadow": "none"
                            });

                            if( settings.ActivityOptions.mouseoverAnimation ){
                                $(this).animate(css, settings.ActivityOptions.mouseoverSpeed, settings.ActivityOptions.mouseoverEasing);
                            } else {
                                $(this).css(css);
                            }
                            $(this).removeClass('tt-activity-expanded');

                        }
                    }
                }


            }, settings.ActivityOptions, data);



            container.on("tt.event.update", $.proxy(function() {
                this.resize();
            }, A));

            container.on("tt.container.updated", function(event) {
                A.resize();
                A.render();
                event.stopPropagation();
            });

            container.on("tt-activitiesRendered", function(event) {
                A.resize();
                A.render();
                event.stopPropagation();
            });

            A._init();

            $(A.activityObj).on(A.events);

            return A;
        };

        var MessageOverlay = function() {
            var MO = new OptionsDependant(container);

            $.extend(true, MO, {
                overlay: $("<div/>", {"class": "tt-overlay", style: "display: none;"}).append($("<div/>", {"class": "tt-message"}).html("No Message")),
                init: function() {
                    this.container.append(this.overlay);

                    $(this.overlay).css({
                        "position": "relative",
                        "z-index": settings.ActivityOptions.zindex + 10
                    });

                    $(".tt-message", this.overlay).css({
                        position: "absolute",
                        opacity: 1

                    });
                },
                show: function(message) {
                    $('.tt-message', this.overlay).html(message);
                    this.overlay.fadeIn();
                    this.resize();
                },
                hide: function() {
                    this.overlay.fadeOut();
                },
                resize: function() {
                    var width = this.container.width();
                    var height = this.container.height();
                    var top = 0;
                    var left = 0;

                    this.overlay.css({
                        width: width,
                        height: height,
                        top: top,
                        left: left
                    });

                    var overlayMsg = $(".tt-message", this.overlay);
                    overlayMsg.css({
                        top: (this.container.height() - overlayMsg.height()) / 2,
                        left: (this.container.width() - overlayMsg.width()) / 2

                    });
                }
            });



            container.on("tt-noActivities", function() {
                MO.show($.isFunction(settings.NoContentMsg) ? settings.NoContentMsg() : settings.NoContentMsg);
            });
            container.on("tt.container.updated", function(event) {
                MO.resize();
            });
            container.on("tt-activitiesRendered", function(event) {
                MO.hide();
            });
            container.on("tt-activitiesLoading", function(event) {
                MO.show($.isFunction(settings.loadingMsg) ? settings.loadingMsg() : settings.loadingMsg);
            });
            container.on("tt-renderStart", function(event) {
                MO.show("Rendering Timetable");
            });
            container.on("tt-activityLoadFailed", function(event) {
                MO.show($.isFunction(settings.loadErrorMsg) ? settings.loadErrorMsg() : settings.loadErrorMsg);
            });

            return MO;
        }





        var tt = createTimeTable();


        // attach event Handlers
        $(window).on("resize", $.proxy(function() {
            this.container.trigger("tt.update");
        }, tt));
        container.on("tt.changed", $.proxy(function(event) {
            this.container.trigger("tt.update");
        }, tt));
        container.on("tt.update", $.proxy(function(event) {
            this.daysContainer.render();
            this.hoursContainer.render();
            if( settings.nowLine ) {
              this.nowLine.render();
            }
            this.container.trigger("tt.container.updated");
            event.stopPropagation();
        }, tt));
        container.on("tt-activitiesChanged", $.proxy(function(evnt) {
            this.render();
        }, tt));

        tt._init();
        tt._create();
        tt.render();
        return tt;
    };

    $.widget.bridge("timetable", TimeTable);

})(jQuery);

