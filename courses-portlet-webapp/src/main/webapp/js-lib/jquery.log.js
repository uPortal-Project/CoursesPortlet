/**
* jQuery Log
* Fast & safe logging in Firebug console
*
* @param mixed - as many parameters as needed
* @return void
*
* @url http://plugins.jquery.com/project/jQueryLog
* @author Amal Samally [amal.samally(at)gmail.com]
* @version 1.0
* @example:
* $.log(someObj, someVar);
* $.log("%s is %d years old.", "Bob", 42);
* $('div.someClass').log().hide();
*/
(function ($) {
    $.extend({
        /* boolean status whether debugging is enabled */
        _debug$: null,

        /* method for getting and setting debug status */
        debug: function (onoff) {
            var old_value = ($._debug$ == true ? true : false);
            $._debug$ = (onoff ? true : false);
            return old_value;
        },

        /* method for logging an object or message */
        log: function (message) {
            if ($._debug$ != true || window.console == undefined || window.console.log == undefined) {
                return;
            }
            
            //If the message is a function evaluate it for logging
            if ($.isFunction(message)) {
                message = message();
            }

            if ($.isArray(message)) {
                if (console.log.apply != undefined) {
                    console.log.apply(window.console, message);
                }
                else {
                    var fullMsg = "";
                    $.each(message, function(idx, msg) {
                        fullMsg = fullMsg + msg + ", ";
                    });
                    console.log(fullMsg);
                }
            }
            else {
                console.log(message);
            }
        }
    });
})(jQuery);