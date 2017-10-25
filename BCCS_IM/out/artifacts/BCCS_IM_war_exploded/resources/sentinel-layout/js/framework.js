//<![CDATA[
;
(function ($) {

    var methods = {
        init: function (options) {
            return this.each(function () {
                var
                    $this = $(this),
                    data = $this.data('monthpicker'),
                    year = (options && options.year) ? options.year : (new Date()).getFullYear(),
                    settings = $.extend({
                        pattern: 'mm/yyyy',
                        selectedMonth: null,
                        selectedMonthName: '',
                        selectedYear: year,
                        startYear: year - 10,
                        finalYear: year + 10,
                        monthNames: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec'],
                        id: "monthpicker_" + (Math.random() * Math.random()).toString().replace('.', ''),
                        openOnFocus: true,
                        disabledMonths: []
                    }, options);

                settings.dateSeparator = settings.pattern.replace(/(mmm|mm|m|yyyy|yy|y)/ig, '');

                // If the plugin hasn't been initialized yet for this element
                if (!data) {

                    $(this).data('monthpicker', {
                        'target': $this,
                        'settings': settings
                    });

                    if (settings.openOnFocus === true) {
                        $this.on('focus', function () {
                            $this.monthpicker('show');
                        });
                    }

                    $this.monthpicker('parseInputValue', settings);

                    $this.monthpicker('mountWidget', settings);

                    $this.on('monthpicker-click-month', function (e, month, year) {
                        $this.monthpicker('setValue', settings);
                        $this.monthpicker('hide');
                    });

                    // hide widget when user clicks elsewhere on page
                    $this.addClass("mtz-monthpicker-widgetcontainer");
                    $(document).unbind("mousedown.mtzmonthpicker").on("mousedown.mtzmonthpicker", function (e) {
                        if (!e.target.className || e.target.className.toString().indexOf('mtz-monthpicker') < 0) {
                            $(this).monthpicker('hideAll');
                        }
                    });
                }
            });
        },

        show: function () {
            $(this).monthpicker('hideAll');
            var widget = $('#' + this.data('monthpicker').settings.id);
            widget.css("top", this.offset().top + this.outerHeight());
            if ($(window).width() > (widget.width() + this.offset().left)) {
                widget.css("left", this.offset().left);
            } else {
                widget.css("left", this.offset().left - widget.width());
            }
            widget.show();
            widget.find('select').focus();
            this.trigger('monthpicker-show');
        },

        hide: function () {
            var widget = $('#' + this.data('monthpicker').settings.id);
            if (widget.is(':visible')) {
                widget.hide();
                this.trigger('monthpicker-hide');
            }
        },

        hideAll: function () {
            $(".mtz-monthpicker-widgetcontainer").each(function () {
                if (typeof($(this).data("monthpicker")) != "undefined") {
                    $(this).monthpicker('hide');
                }
            });
        },

        setValue: function (settings) {
            var
                month = settings.selectedMonth,
                year = settings.selectedYear;

            if (settings.pattern.indexOf('mmm') >= 0) {
                month = settings.selectedMonthName;
            } else if (settings.pattern.indexOf('mm') >= 0 && settings.selectedMonth < 10) {
                month = '0' + settings.selectedMonth;
            }

            if (settings.pattern.indexOf('yyyy') < 0) {
                year = year.toString().substr(2, 2);
            }

            if (settings.pattern.indexOf('y') > settings.pattern.indexOf(settings.dateSeparator)) {
                this.val(month + settings.dateSeparator + year);
            } else {
                this.val(year + settings.dateSeparator + month);
            }

            this.change();
        },

        disableMonths: function (months) {
            var
                settings = this.data('monthpicker').settings,
                container = $('#' + settings.id);

            settings.disabledMonths = months;

            container.find('.mtz-monthpicker-month').each(function () {
                var m = parseInt($(this).data('month'));
                if ($.inArray(m, months) >= 0) {
                    $(this).addClass('ui-state-disabled');
                } else {
                    $(this).removeClass('ui-state-disabled');
                }
            });
        },

        mountWidget: function (settings) {
            var
                monthpicker = this,
                container = $('<div id="' + settings.id + '" class="ui-datepicker ui-widget ui-widget-content ui-helper-clearfix ui-corner-all" />'),
                header = $('<div class="ui-datepicker-header ui-widget-header ui-helper-clearfix ui-corner-all mtz-monthpicker" />'),
                combo = $('<select class="mtz-monthpicker mtz-monthpicker-year" />'),
                table = $('<table class="mtz-monthpicker" />'),
                tbody = $('<tbody class="mtz-monthpicker" />'),
                tr = $('<tr class="mtz-monthpicker" />'),
                td = '',
                selectedYear = settings.selectedYear,
                option = null,
                attrSelectedYear = $(this).data('selected-year'),
                attrStartYear = $(this).data('start-year'),
                attrFinalYear = $(this).data('final-year');

            if (attrSelectedYear) {
                settings.selectedYear = attrSelectedYear;
            }

            if (attrStartYear) {
                settings.startYear = attrStartYear;
            }

            if (attrFinalYear) {
                settings.finalYear = attrFinalYear;
            }

            container.css({
                position: 'absolute',
                zIndex: 999999,
                whiteSpace: 'nowrap',
                width: '250px',
                overflow: 'hidden',
                textAlign: 'center',
                display: 'none',
                top: monthpicker.offset().top + monthpicker.outerHeight(),
                left: monthpicker.offset().left
            });

            combo.on('change', function () {
                var months = $(this).parent().parent().find('td[data-month]');
                months.removeClass('ui-state-active');
                if ($(this).val() == settings.selectedYear) {
                    months.filter('td[data-month=' + settings.selectedMonth + ']').addClass('ui-state-active');
                }
                monthpicker.trigger('monthpicker-change-year', $(this).val());
            });

            // mount years combo
            for (var i = settings.startYear; i <= settings.finalYear; i++) {
                var option = $('<option class="mtz-monthpicker" />').attr('value', i).append(i);
                if (settings.selectedYear == i) {
                    option.attr('selected', 'selected');
                }
                combo.append(option);
            }
            header.append(combo).appendTo(container);

            // mount months table
            for (var i = 1; i <= 12; i++) {
                td = $('<td class="ui-state-default mtz-monthpicker mtz-monthpicker-month" style="padding:5px;cursor:default;" />').attr('data-month', i);
                if (settings.selectedMonth == i) {
                    td.addClass('ui-state-active');
                }
                td.append(settings.monthNames[i - 1]);
                tr.append(td).appendTo(tbody);
                if (i % 3 === 0) {
                    tr = $('<tr class="mtz-monthpicker" />');
                }
            }

            tbody.find('.mtz-monthpicker-month').on('click', function () {
                var m = parseInt($(this).data('month'));
                if ($.inArray(m, settings.disabledMonths) < 0) {
                    settings.selectedYear = $(this).closest('.ui-datepicker').find('.mtz-monthpicker-year').first().val();
                    settings.selectedMonth = $(this).data('month');
                    settings.selectedMonthName = $(this).text();
                    monthpicker.trigger('monthpicker-click-month', $(this).data('month'));
                    $(this).closest('table').find('.ui-state-active').removeClass('ui-state-active');
                    $(this).addClass('ui-state-active');
                }
            });

            table.append(tbody).appendTo(container);

            container.appendTo('body');
        },

        destroy: function () {
            return this.each(function () {
                $(this).removeClass('mtz-monthpicker-widgetcontainer').unbind('focus').removeData('monthpicker');
            });
        },

        getDate: function () {
            var settings = this.data('monthpicker').settings;
            if (settings.selectedMonth && settings.selectedYear) {
                return new Date(settings.selectedYear, settings.selectedMonth - 1);
            } else {
                return null;
            }
        },

        parseInputValue: function (settings) {
            if (this.val()) {
                if (settings.dateSeparator) {
                    var val = this.val().toString().split(settings.dateSeparator);
                    if (settings.pattern.indexOf('m') === 0) {
                        settings.selectedMonth = val[0];
                        settings.selectedYear = val[1];
                    } else {
                        settings.selectedMonth = val[1];
                        settings.selectedYear = val[0];
                    }
                }
            }
        }

    };

    $.fn.monthpicker = function (method) {
        if (methods[method]) {
            return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
        } else if (typeof method === 'object' || !method) {
            return methods.init.apply(this, arguments);
        } else {
            $.error('Method ' + method + ' does not exist on jQuery.mtz.monthpicker');
        }
    };

})(jQuery);


!function (a, b, c) {
    "use strict";
    var d, e, f, g, h, i, j, k, l, m, n, o, p, q, r, s, t, u, v, w, x, y, z, A, B, C, D, E, F, G, H;
    z = {
        paneClass: "nano-pane",
        sliderClass: "nano-slider",
        contentClass: "nano-content",
        iOSNativeScrolling: !1,
        preventPageScrolling: !1,
        disableResize: !1,
        alwaysVisible: !1,
        flashDelay: 1500,
        sliderMinHeight: 20,
        sliderMaxHeight: null,
        documentContext: null,
        windowContext: null
    }, u = "scrollbar", t = "scroll", l = "mousedown", m = "mouseenter", n = "mousemove", p = "mousewheel", o = "mouseup", s = "resize", h = "drag", i = "enter", w = "up", r = "panedown", f = "DOMMouseScroll", g = "down", x = "wheel", j = "keydown", k = "keyup", v = "touchmove", d = "Microsoft Internet Explorer" === b.navigator.appName && /msie 7./i.test(b.navigator.appVersion) && b.ActiveXObject, e = null, D = b.requestAnimationFrame, y = b.cancelAnimationFrame, F = c.createElement("div").style, H = function () {
        var a, b, c, d, e, f;
        for (d = ["t", "webkitT", "MozT", "msT", "OT"], a = e = 0, f = d.length; f > e; a = ++e)if (c = d[a], b = d[a] + "ransform", b in F)return d[a].substr(0, d[a].length - 1);
        return !1
    }(), G = function (a) {
        return H === !1 ? !1 : "" === H ? a : H + a.charAt(0).toUpperCase() + a.substr(1)
    }, E = G("transform"), B = E !== !1, A = function () {
        var a, b, d;
        return a = c.createElement("div"), b = a.style, b.position = "absolute", b.width = "100px", b.height = "100px", b.overflow = t, b.top = "-9999px", c.body.appendChild(a), d = a.offsetWidth - a.clientWidth, c.body.removeChild(a), d
    }, C = function () {
        var a, c, d;
        return c = b.navigator.userAgent, (a = /(?=.+Mac OS X)(?=.+Firefox)/.test(c)) ? (d = /Firefox\/\d{2}\./.exec(c), d && (d = d[0].replace(/\D+/g, "")), a && +d > 23) : !1
    }, q = function () {
        function j(d, f) {
            this.el = d, this.options = f, e || (e = A()), this.$el = a(this.el), this.doc = a(this.options.documentContext || c), this.win = a(this.options.windowContext || b), this.body = this.doc.find("body"), this.$content = this.$el.children("." + f.contentClass), this.$content.attr("tabindex", this.options.tabIndex || 0), this.content = this.$content[0], this.previousPosition = 0, this.options.iOSNativeScrolling && null != this.el.style.WebkitOverflowScrolling ? this.nativeScrolling() : this.generate(), this.createEvents(), this.addEvents(), this.reset()
        }

        return j.prototype.preventScrolling = function (a, b) {
            if (this.isActive)if (a.type === f)(b === g && a.originalEvent.detail > 0 || b === w && a.originalEvent.detail < 0) && a.preventDefault(); else if (a.type === p) {
                if (!a.originalEvent || !a.originalEvent.wheelDelta)return;
                (b === g && a.originalEvent.wheelDelta < 0 || b === w && a.originalEvent.wheelDelta > 0) && a.preventDefault()
            }
        }, j.prototype.nativeScrolling = function () {
            this.$content.css({WebkitOverflowScrolling: "touch"}), this.iOSNativeScrolling = !0, this.isActive = !0
        }, j.prototype.updateScrollValues = function () {
            var a, b;
            a = this.content, this.maxScrollTop = a.scrollHeight - a.clientHeight, this.prevScrollTop = this.contentScrollTop || 0, this.contentScrollTop = a.scrollTop, b = this.contentScrollTop > this.previousPosition ? "down" : this.contentScrollTop < this.previousPosition ? "up" : "same", this.previousPosition = this.contentScrollTop, "same" !== b && this.$el.trigger("update", {
                position: this.contentScrollTop,
                maximum: this.maxScrollTop,
                direction: b
            }), this.iOSNativeScrolling || (this.maxSliderTop = this.paneHeight - this.sliderHeight, this.sliderTop = 0 === this.maxScrollTop ? 0 : this.contentScrollTop * this.maxSliderTop / this.maxScrollTop)
        }, j.prototype.setOnScrollStyles = function () {
            var a;
            B ? (a = {}, a[E] = "translate(0, " + this.sliderTop + "px)") : a = {top: this.sliderTop}, D ? (y && this.scrollRAF && y(this.scrollRAF), this.scrollRAF = D(function (b) {
                return function () {
                    return b.scrollRAF = null, b.slider.css(a)
                }
            }(this))) : this.slider.css(a)
        }, j.prototype.createEvents = function () {
            this.events = {
                down: function (a) {
                    return function (b) {
                        return a.isBeingDragged = !0, a.offsetY = b.pageY - a.slider.offset().top, a.slider.is(b.target) || (a.offsetY = 0), a.pane.addClass("active"), a.doc.bind(n, a.events[h]).bind(o, a.events[w]), a.body.bind(m, a.events[i]), !1
                    }
                }(this), drag: function (a) {
                    return function (b) {
                        return a.sliderY = b.pageY - a.$el.offset().top - a.paneTop - (a.offsetY || .5 * a.sliderHeight), a.scroll(), a.contentScrollTop >= a.maxScrollTop && a.prevScrollTop !== a.maxScrollTop ? a.$el.trigger("scrollend") : 0 === a.contentScrollTop && 0 !== a.prevScrollTop && a.$el.trigger("scrolltop"), !1
                    }
                }(this), up: function (a) {
                    return function () {
                        return a.isBeingDragged = !1, a.pane.removeClass("active"), a.doc.unbind(n, a.events[h]).unbind(o, a.events[w]), a.body.unbind(m, a.events[i]), !1
                    }
                }(this), resize: function (a) {
                    return function () {
                        a.reset()
                    }
                }(this), panedown: function (a) {
                    return function (b) {
                        return a.sliderY = (b.offsetY || b.originalEvent.layerY) - .5 * a.sliderHeight, a.scroll(), a.events.down(b), !1
                    }
                }(this), scroll: function (a) {
                    return function (b) {
                        a.updateScrollValues(), a.isBeingDragged || (a.iOSNativeScrolling || (a.sliderY = a.sliderTop, a.setOnScrollStyles()), null != b && (a.contentScrollTop >= a.maxScrollTop ? (a.options.preventPageScrolling && a.preventScrolling(b, g), a.prevScrollTop !== a.maxScrollTop && a.$el.trigger("scrollend")) : 0 === a.contentScrollTop && (a.options.preventPageScrolling && a.preventScrolling(b, w), 0 !== a.prevScrollTop && a.$el.trigger("scrolltop"))))
                    }
                }(this), wheel: function (a) {
                    return function (b) {
                        var c;
                        if (null != b)return c = b.delta || b.wheelDelta || b.originalEvent && b.originalEvent.wheelDelta || -b.detail || b.originalEvent && -b.originalEvent.detail, c && (a.sliderY += -c / 3), a.scroll(), !1
                    }
                }(this), enter: function (a) {
                    return function (b) {
                        var c;
                        if (a.isBeingDragged)return 1 !== (b.buttons || b.which) ? (c = a.events)[w].apply(c, arguments) : void 0
                    }
                }(this)
            }
        }, j.prototype.addEvents = function () {
            var a;
            this.removeEvents(), a = this.events, this.options.disableResize || this.win.bind(s, a[s]), this.iOSNativeScrolling || (this.slider.bind(l, a[g]), this.pane.bind(l, a[r]).bind("" + p + " " + f, a[x])), this.$content.bind("" + t + " " + p + " " + f + " " + v, a[t])
        }, j.prototype.removeEvents = function () {
            var a;
            a = this.events, this.win.unbind(s, a[s]), this.iOSNativeScrolling || (this.slider.unbind(), this.pane.unbind()), this.$content.unbind("" + t + " " + p + " " + f + " " + v, a[t])
        }, j.prototype.generate = function () {
            var a, c, d, f, g, h, i;
            return f = this.options, h = f.paneClass, i = f.sliderClass, a = f.contentClass, (g = this.$el.children("." + h)).length || g.children("." + i).length || this.$el.append('<div class="' + h + '"><div class="' + i + '" /></div>'), this.pane = this.$el.children("." + h), this.slider = this.pane.find("." + i), 0 === e && C() ? (d = b.getComputedStyle(this.content, null).getPropertyValue("padding-right").replace(/[^0-9.]+/g, ""), c = {
                right: -14,
                paddingRight: +d + 14
            }) : e && (c = {right: -e}, this.$el.addClass("has-scrollbar")), null != c && this.$content.css(c), this
        }, j.prototype.restore = function () {
            this.stopped = !1, this.iOSNativeScrolling || this.pane.show(), this.addEvents()
        }, j.prototype.reset = function () {
            var a, b, c, f, g, h, i, j, k, l, m, n;
            return this.iOSNativeScrolling ? void(this.contentHeight = this.content.scrollHeight) : (this.$el.find("." + this.options.paneClass).length || this.generate().stop(), this.stopped && this.restore(), a = this.content, f = a.style, g = f.overflowY, d && this.$content.css({height: this.$content.height()}), b = a.scrollHeight + e, l = parseInt(this.$el.css("max-height"), 10), l > 0 && (this.$el.height(""), this.$el.height(a.scrollHeight > l ? l : a.scrollHeight)), i = this.pane.outerHeight(!1), k = parseInt(this.pane.css("top"), 10), h = parseInt(this.pane.css("bottom"), 10), j = i + k + h, n = Math.round(j / b * j), n < this.options.sliderMinHeight ? n = this.options.sliderMinHeight : null != this.options.sliderMaxHeight && n > this.options.sliderMaxHeight && (n = this.options.sliderMaxHeight), g === t && f.overflowX !== t && (n += e), this.maxSliderTop = j - n, this.contentHeight = b, this.paneHeight = i, this.paneOuterHeight = j, this.sliderHeight = n, this.paneTop = k, this.slider.height(n), this.events.scroll(), this.pane.show(), this.isActive = !0, a.scrollHeight === a.clientHeight || this.pane.outerHeight(!0) >= a.scrollHeight && g !== t ? (this.pane.hide(), this.isActive = !1) : this.el.clientHeight === a.scrollHeight && g === t ? this.slider.hide() : this.slider.show(), this.pane.css({
                opacity: this.options.alwaysVisible ? 1 : "",
                visibility: this.options.alwaysVisible ? "visible" : ""
            }), c = this.$content.css("position"), ("static" === c || "relative" === c) && (m = parseInt(this.$content.css("right"), 10), m && this.$content.css({
                right: "",
                marginRight: m
            })), this)
        }, j.prototype.scroll = function () {
            return this.isActive ? (this.sliderY = Math.max(0, this.sliderY), this.sliderY = Math.min(this.maxSliderTop, this.sliderY), this.$content.scrollTop(this.maxScrollTop * this.sliderY / this.maxSliderTop), this.iOSNativeScrolling || (this.updateScrollValues(), this.setOnScrollStyles()), this) : void 0
        }, j.prototype.scrollBottom = function (a) {
            return this.isActive ? (this.$content.scrollTop(this.contentHeight - this.$content.height() - a).trigger(p), this.stop().restore(), this) : void 0
        }, j.prototype.scrollTop = function (a) {
            return this.isActive ? (this.$content.scrollTop(+a).trigger(p), this.stop().restore(), this) : void 0
        }, j.prototype.scrollTo = function (a) {
            return this.isActive ? (this.scrollTop(this.$el.find(a).get(0).offsetTop), this) : void 0
        }, j.prototype.stop = function () {
            return y && this.scrollRAF && (y(this.scrollRAF), this.scrollRAF = null), this.stopped = !0, this.removeEvents(), this.iOSNativeScrolling || this.pane.hide(), this
        }, j.prototype.destroy = function () {
            return this.stopped || this.stop(), !this.iOSNativeScrolling && this.pane.length && this.pane.remove(), d && this.$content.height(""), this.$content.removeAttr("tabindex"), this.$el.hasClass("has-scrollbar") && (this.$el.removeClass("has-scrollbar"), this.$content.css({right: ""})), this
        }, j.prototype.flash = function () {
            return !this.iOSNativeScrolling && this.isActive ? (this.reset(), this.pane.addClass("flashed"), setTimeout(function (a) {
                return function () {
                    a.pane.removeClass("flashed")
                }
            }(this), this.options.flashDelay), this) : void 0
        }, j
    }(), a.fn.nanoScroller = function (b) {
        return this.each(function () {
            var c, d;
            if ((d = this.nanoscroller) || (c = a.extend({}, z, b), this.nanoscroller = d = new q(this, c)), b && "object" == typeof b) {
                if (a.extend(d.options, b), null != b.scrollBottom)return d.scrollBottom(b.scrollBottom);
                if (null != b.scrollTop)return d.scrollTop(b.scrollTop);
                if (b.scrollTo)return d.scrollTo(b.scrollTo);
                if ("bottom" === b.scroll)return d.scrollBottom(0);
                if ("top" === b.scroll)return d.scrollTop(0);
                if (b.scroll && b.scroll instanceof a)return d.scrollTo(b.scroll);
                if (b.stop)return d.stop();
                if (b.destroy)return d.destroy();
                if (b.flash)return d.flash()
            }
            return d.reset()
        })
    }, a.fn.nanoScroller.Constructor = q
}(jQuery, window, document);

//Execute Popup
function checkOpenDlg(id) {
    if (PF(id)) {
        PF(id).show();
    }
}
function checkCloseDlg(id) {
    if (PF(id)) {
        PF(id).hide();
    }
}
// validate
function checkLength(object) {
    if (object) {
        var content = object.getJQ().val();
        if (content.length > 0) {
            return true;
        }
        else {
            return false;
        }
    }
}

function excecuteValueField(object) {
    if (object) {
        if (checkLength(object)) {
            hideValueField();
        }
        else {
            showValueField();
        }
    }
}

function excecuteValueFromTo(object) {
    if (object) {
        if (checkLength(object)) {
            hideValueFromTo();
        }
        else {
            showValueFromTo();
        }
    }
}
//tab change value
function onKeyUpFrom(fromId, toId) {
    var fromNumber = document.getElementById(fromId).value;
    document.getElementById(toId).value = fromNumber;
}
//clear space 2 dau gia tri
function trimAllInputText() {
    var arrInput = document.getElementsByTagName("input");
    var arrInputarea = document.getElementsByTagName("textarea");
    try {
        for (var i = 0; i < arrInput.length; i++) {
            if (arrInput[i].getAttribute("type") == "text" || arrInput[i].getAttribute("role") == "textbox") {
                arrInput[i].value = arrInput[i].value.trim();
            }
        }
        for (var j = 0; j < arrInputarea.length; j++) {
            arrInputarea[j].value = arrInputarea[j].value.trim();
        }
    } catch (e) {
        console.log(e);
    }
}
function focusElement(id) {
    if (id != null) {
        if (PF(id) != null) {
            /* KhuongDV added 22/09/2014*/
            console.log(PF(id));
            $(PF(id).jqId).focus();
            /* End */
            PF(id).getJQ().addClass("ui-state-error ui-state-focus");
        }
    }
}
// Focus element by ID: KHUONGDV
function focusElementByRawCSSSlector(selector) {
    if (selector != null) {
        $(selector).addClass("ui-state-error ui-state-focus");
    }
}
/**
 * hàm xử lý bôi đỏ component đối tượng và label của nó
 * @author ThanhNT
 * @param id
 */
function focusElementError(className) {
    if (className != null) {
        $("." + className).addClass("ui-state-error-com");
        // $("." + className).find("input").focus();
        if ($("." + className + "_col") != null) {
            $("." + className + "_col").addClass("ui-state-error-com");
        }
    }
}

/**
 * hàm xử lý bôi đỏ component đối tượng và label của nó
 * @author ThanhNT
 * @param id
 */
function removeFocusElementError(className) {
    if (className != null) {
        $("." + className).removeClass("ui-state-error-com");
        $("." + className).removeClass("ui-state-error");
        if ($("." + className + "_col") != null) {
            $("." + className + "_col").removeClass("ui-state-error-com");
            $("." + className + "_col").removeClass("ui-state-error");
            $("." + className + "_col").find("label").removeClass("ui-state-error-com");
            $("." + className + "_col").find("label").removeClass("ui-state-error");
        }
    }
}

//Loc dau
function filterTagName(obj) {
    var str;
    if (eval(obj)) {
        str = eval(obj).value;
    }
    else
        str = obj;
    str = str.toLowerCase();
    str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
    str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
    str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
    str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
    str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
    str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
    str = str.replace(/đ/g, "d");
    str = str.replace(/!|@|%|\^|\*|\(|\)|\+|\=|\<|\>|\?|\/|,|\.|\:|\;|\'| |\"|\&|\#|\[|\]|~|$|_/g, "-");
    /* tìm và thay thế các kí tự đặc biệt trong chuỗi sang kí tự - */
    str = str.replace(/-+-/g, "-"); //thay thế 2- thành 1- 
    str = str.replace(/^\-+|\-+$/g, ""); //cắt bỏ ký tự - ở đầu và cuối chuỗi 
    eval(obj).value = str;

}
function filterAttributeName(obj) {
    var str;
    if (eval(obj)) {
        str = eval(obj).value;
    }
    else
        str = obj;
    str = str.toLowerCase();
    str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
    str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
    str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
    str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
    str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
    str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
    str = str.replace(/đ/g, "d");
    str = str.replace(/!|@|%|\^|\*|\(|\)|\+|\=|\<|\>|\?|\/|,|\.|\:|\;|\'| |\"|\&|\#|\[|\]|~|$|_/g, "");
    /* tìm và thay thế các kí tự đặc biệt trong chuỗi sang kí tự - */
    str = str.replace(/-+-/g, ""); //thay thế 2- thành 1- 
    str = str.replace(/^\-+|\-+$/g, ""); //cắt bỏ ký tự - ở đầu và cuối chuỗi 
    eval(obj).value = str;

}

function getConvertedStringFromVietnamese(str) {
    str = str.toLowerCase();
    str = str.replace(/à|á|ạ|ả|ã|â|ầ|ấ|ậ|ẩ|ẫ|ă|ằ|ắ|ặ|ẳ|ẵ/g, "a");
    str = str.replace(/À|Á|Ạ|Ả|Ã|Â|Ầ|Ấ|Ậ|Ẩ|Ẫ|Ă|Ằ|Ắ|Ặ|Ẳ|Ẵ/g, "A");
    str = str.replace(/è|é|ẹ|ẻ|ẽ|ê|ề|ế|ệ|ể|ễ/g, "e");
    str = str.replace(/È|É|Ẹ|Ẻ|Ẽ|Ê|Ề|Ế|Ệ|Ể|Ễ/g, "E");
    str = str.replace(/ì|í|ị|ỉ|ĩ/g, "i");
    str = str.replace(/Ì|Í|Ị|Ỉ|Ĩ/g, "I");
    str = str.replace(/ò|ó|ọ|ỏ|õ|ô|ồ|ố|ộ|ổ|ỗ|ơ|ờ|ớ|ợ|ở|ỡ/g, "o");
    str = str.replace(/Ò|Ó|Ọ|Ỏ|Õ|Ô|Ồ|Ố|Ộ|Ổ|Ỗ|Ơ|Ờ|Ớ|Ợ|Ở|Ỡ/g, "O");
    str = str.replace(/ù|ú|ụ|ủ|ũ|ư|ừ|ứ|ự|ử|ữ/g, "u");
    str = str.replace(/Ù|Ú|Ụ|Ủ|Ũ|Ư|Ừ|Ứ|Ự|Ử|Ữ/g, "U");
    str = str.replace(/ỳ|ý|ỵ|ỷ|ỹ/g, "y");
    str = str.replace(/Ỳ|Ý|Ỵ|Ỷ|Ỹ/g, "Y");
    str = str.replace(/đ/g, "d");
    str = str.replace(/Đ/g, "D");
    return str;

}
function focusElement(id) {
    if (id != null) {
        if (PF(id) != null) {
            /* KhuongDV added 22/09/2014*/
            console.log(PF(id));
            document.getElementById(PF(id).id).focus();
            /* End */
            PF(id).getJQ().addClass("ui-state-error ui-state-focus");
        }
    }
}

function focusByClass(className) {
    if (className != null) {
        $("." + className).focus();
    }
}

function removeFocusByClass(className) {
    if (className != null) {
        $("." + className).blur();
    }
}

function reloadWizard() {
    if (jQuery(".ui-wizard").html() != null) {
        if (jQuery(".wz-splitter").html() == null || jQuery(".wz-splitter").html() == "undefined") {
            jQuery(".ui-wizard").find("ul").addClass("ui-widget-header ui-corner-all");
            jQuery(".ui-wizard-step-title").removeClass("ui-state-default");
            jQuery(".ui-wizard-step-title").append("<div class='ui-wizard-step-title ui-state-default ui-corner-all wz-splitter pos-abs'></div>");
            var wzNavBar = jQuery(".ui-wizard-navbar");
            jQuery(".ui-wizard-step-titles").append(wzNavBar);
            jQuery(".ui-wizard-nav-next").css("margin-top", "4px");
            jQuery(".ui-wizard-nav-back").css("margin-top", "4px");
            wzNavBar.css("float", "left");
        }
    }
}


$(document).ready(function () {
    //reset search menu khi do phan giai man hinh thay doi
    if (jQuery("#buttonArea") != null) {
        if (jQuery("#buttonArea").css("display") == "none") {
            jQuery(".layout-menubarinner-box").css("padding", "10px 0px");
        }
    }
    $(document).on("keydown", ".inputLookupClass", function (event) {
        if (event.which == 120) {
            onHotkeyLookUp();
        }
    });
});

var keyBoardLookUp = '';
function setHotkeyLookUp(key, value) {
    keyBoardLookUp = key;
    if (keyBoardLookUp != null && keyBoardLookUp != '' && (value == null || value == '')) {
        onHotkeyLookUp();
    }

}
function onHotkeyLookUp() {
    if (keyBoardLookUp == null || keyBoardLookUp == '') {
        return;
    }
    var rm = 'rmF9' + keyBoardLookUp;
    window[rm]();
}

function setHotkeyLookUpOverpanel(key) {
    keyBoardLookUp = key;
    onHotkeyLookUpOverpanel();
}

function onHotkeyLookUpOverpanel() {
    if (keyBoardLookUp == null || keyBoardLookUp == '') {
        return;
    }
    var rm = 'overPanel' + keyBoardLookUp;
    window[rm]();
}

function clickLinkByClass(className) {
    if ($("." + className) != null) {
        $("." + className).click();
    }
}

var widgetTable = '';

var initWidgetTable = function (wid) {
    widgetTable = wid;
}

var down = function () {
    if (widgetTable === null || widgetTable === '') {
        return;
    }
    //If no rows selected, select first row
    if (PF(widgetTable).selection.length === 0) {
        PF(widgetTable).selectRow(0);
        return;
    }
    PF(widgetTable).selectRow(0);
    //get index of selected row, if no row is selected return 0
    var index = PF(widgetTable).originRowIndex;
    //get amount of rows in the table
    var rows = PF(widgetTable).tbody[0].childElementCount;
    //increase row index
    index++;
    //if new index equals number of rows, set index to first row
    if (index === rows) {
        index = 0;
    }
    //deselect all selected rows
    PF(widgetTable).unselectAllRows();
    //select new row
    PF(widgetTable).selectRow(index);
    //change originRowIndex value
    PF(widgetTable).originRowIndex = index;
};

var up = function () {
    if (widgetTable === null || widgetTable === '') {
        return;
    }
    var rows = PF(widgetTable).tbody[0].childElementCount;
    var index = PF(widgetTable).originRowIndex;

    if (index === 0) {
        index = rows - 1;
    } else {
        index--;
    }

    PF(widgetTable).unselectAllRows();
    PF(widgetTable).selectRow(index);
    PF(widgetTable).originRowIndex = index;
}
var enter = function () {
    if (widgetTable == null || widgetTable == '') {
        return;
    }
    var rm = widgetTable + "method";
    window[rm]();
}

//clear input, ui-error text in form
function clearAllField(formId) {
    if (formId != null && formId != "") {
        var form = document.getElementById(formId);
        if (form != null) {
            var lstInput = form.getElementsByTagName("input");
            var lstTextArea = form.getElementsByTagName("textarea");
            for (var i = 0; i < lstInput.length; i++) {
                if (!lstInput[i].className.contains("ignore-clear-data")) {
                    lstInput[i].value = "";
                }
            }
            for (var i = 0; i < lstTextArea.length; i++) {
                if (!lstTextArea[i].className.contains("ignore-clear-data")) {
                    lstTextArea[i].value = "";
                }
            }
            jQuery(".ui-state-error").each(function () {
                jQuery(this).removeClass("ui-state-error");
            });
        }
    }
}
function topPage() {
    $('html, body').animate({scrollTop: 0}, 'fast');
}
function focusElementErrorByListClass(className, listIndex) {
    if (className != '' && listIndex != '') {
        var arrIndex = listIndex.split(',');
        for (var i = 0; i < arrIndex.length; i++) {
            $("." + className + arrIndex[i]).addClass("ui-state-error");
        }
    }
}
function stopRKey(evt) {
    var evt = (evt) ? evt : ((event) ? event : null);
    var node = (evt.target) ? evt.target : ((evt.srcElement) ? evt.srcElement : null);
    if ((evt.keyCode == 13) && (node.type == "text")) {
        return false;
    }
    if ((evt.keyCode == 13) && (node.type == "checkbox")) {
        return false;
    }
    if ((evt.keyCode == 13) && (node.type == "radio")) {
        return false;
    }
}

function isValidDate(dateString) {
    // First check for the pattern
    if (!/^\d{1,2}\/\d{1,2}\/\d{4}$/.test(dateString))
        return false;

    // Parse the date parts to integers
    var parts = dateString.split("/");
    var day = parseInt(parts[0], 10);
    var month = parseInt(parts[1], 10);
    var year = parseInt(parts[2], 10);
    // Check the ranges of month and year
    if (year < 1000 || year > 9999 || month == 0 || month > 12)
        return false;
    var monthLength = [31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31];
    // Adjust for leap years
    if (year % 400 == 0 || (year % 100 != 0 && year % 4 == 0))
        monthLength[1] = 29;
    // Check the range of the day
    if (day > 0 && day <= monthLength[month - 1]) return false;
    return false;
}

function trimInput(element) {
    try {
        if (element != null) {
            console.log(element);
            element.value = element.value.trim();
        }
    } catch (e) {
        console.log("loi trim input")
    }
}


function loadInlinePlaceEdit() {
    //reload inline place edit
    jQuery(".force-inline .hasDatepicker").each(function () {
        jQuery(this).focus(function () {
            var parent = jQuery(this).parent();
            parent.find("button").css("opacity", "1");
        });
        jQuery(this).mouseover(function () {
            var parent = jQuery(this).parent();
            parent.find("button").css("opacity", "1");
            parent.mouseleave(function () {
                parent.find("button").css("opacity", "0");
            });
        });
        jQuery(this).blur(function () {
            var parent = jQuery(this).parent();
            parent.find("button").css("opacity", "0");
        });
    });
    jQuery(".force-inline .ui-spinner-input").each(function () {
        jQuery(this).focus(function () {
            var parent = jQuery(this).parent();
            parent.find("a").css("opacity", "1");
        });
        jQuery(this).mouseover(function () {
            var parent = jQuery(this).parent();
            parent.find("a").css("opacity", "1");
            parent.mouseleave(function () {
                parent.find("a").css("opacity", "0");
            });
        });
        jQuery(this).blur(function () {
            var parent = jQuery(this).parent();
            parent.find("a").css("opacity", "0");
        });
    });
}

String.prototype.capitalize = function () {
//.toLocaleLowerCase()
    return this.replace(/(?:^|\s)\S/g, function (a) {
        return a.toUpperCase();
    });

};
/**
 * Focus dung nghia~ vao selector (co cursor nhap nhay)
 * @param selector
 */
function focusElementWithCursor(selector) {
    if (selector != null) {
        $(selector).focus();
    }
}

function setFocusByClass(className) {
    $("." + className).focus();
}
function focusFirstErrorComponent() {
    $(".ui-state-error-com input:first-child").focus();
}
function adjustPositionning(sourceElementClass, dialogClass) {
    var sourceElement = $(document).find("." + sourceElementClass);
    var dialog = $(document).find("." + dialogClass);
    var x = sourceElement.offset().left;
    var xHeight = sourceElement.height();
    var y = sourceElement.offset().top;
    var width = sourceElement.width();
    var padding = 15;
    dialog.offset({top: y + xHeight + padding, left: x});
}
//thanhnt them ham rong de ko bi loi js trong tag
function emptyFunction() {

}
function emptyFunctionx() {

}
//thanhnt them ham rong de ko bi loi js
//]]

/**
 * thiendn1: sua SPACE khong an even cua SelectOneRadio
 */
PrimeFaces.widget.SelectOneRadio.prototype.bindEvents = function () {
    var a = this;
    this.outputs.filter(":not(.ui-state-disabled)").on("mouseover.selectOneRadio", function () {
        $(this).addClass("ui-state-hover")
    }).on("mouseout.selectOneRadio", function () {
        $(this).removeClass("ui-state-hover")
    }).on("click.selectOneRadio", function () {
        var c = $(this), b = c.prev().children(":radio");
        if (!c.hasClass("ui-state-active")) {
            a.unselect(a.checkedRadio);
            a.select(c);
            b.trigger("click");
            b.trigger("change")
        } else {
            b.trigger("click")
        }
    });
    this.labels.filter(":not(.ui-state-disabled)").on("click.selectOneRadio", function (d) {
        var c = $(PrimeFaces.escapeClientId($(this).attr("for"))), b = null;
        if (c.is(":input")) {
            b = c.parent().next()
        } else {
            b = c.children(".ui-radiobutton-box")
        }
        b.trigger("click.selectOneRadio");
        d.preventDefault()
    });
    this.inputs.on("focus.selectOneRadio", function () {
        var b = $(this), c = b.parent().next();
        if (b.prop("checked")) {
            c.removeClass("ui-state-active")
        }
        c.addClass("ui-state-focus")
    }).on("blur.selectOneRadio", function () {
        var b = $(this), c = b.parent().next();
        if (b.prop("checked")) {
            c.addClass("ui-state-active")
        }
        c.removeClass("ui-state-focus")
    }).on("keydown.selectOneRadio", function (h) {
        var i = $(this), f = i.parent().next(), g = a.inputs.index(i), m = a.inputs.length, l = $.ui.keyCode, j = h.which;
        switch (j) {
            case l.UP:
            case l.LEFT:
                var c = (g === 0) ? a.inputs.eq((m - 1)) : a.inputs.eq(--g), k = c.parent().next();
                i.blur();
                a.unselect(f);
                a.select(k);
                c.trigger("focus").trigger("change");
                h.preventDefault();
                break;
            case l.DOWN:
            case l.RIGHT:
                var d = (g === (m - 1)) ? a.inputs.eq(0) : a.inputs.eq(++g), b = d.parent().next();
                i.blur();
                a.unselect(f);
                a.select(b);
                d.trigger("focus").trigger("change");
                h.preventDefault();
                break;
            case l.SPACE:
                var d = a.inputs.eq(0);
                i.blur();
                if (!i.prop("checked")) {
                    a.select(f);
                    d.trigger("focus").trigger("change");
                }
                h.preventDefault();
                break;
        }
    })
}

function subString(txt) {
    if (txt != null) {
        txt = txt.trim();
        if (txt.trim().charAt(0) == '0') {
            return txt.substring(1);
        }
        return txt;
    }
}
String.prototype.capitalize = function () {
    //.toLocaleLowerCase()
    return this.replace(/(?:^|\s)\S/g, function (a) {
        return a.toUpperCase();
    });
};
function goToPosition(element_id) {
    console.log(element_id);
    var top = $("." + element_id).position().top;
    $(window).scrollTop(top);
}

PrimeFaces.widget.Growl.prototype.render = function () {
    this.jq = $('<div id="' + this.id + '_container" class="ui-growl ui-widget"></div>');
    this.jq.appendTo($(document.body));
    this.show(this.cfg.msgs);
};

PrimeFaces.widget.Growl.prototype.renderMessage = function (e) {
    var a = '<div class="ui-growl-item-container ui-state-highlight ui-corner-all ui-helper-hidden ui-shadow cbs-toolbar" aria-live="polite">';
    a += '<div class="ui-growl-item ' + e.severity +'">';
    a += '<div class="ui-growl-icon-close ui-icon ui-icon-closethick"></div>';
    a += '<span class="ui-growl-image ui-growl-image-' + e.severity + '" />';
    a += '<div class="ui-growl-message">';
    a += '<span class="ui-growl-title"></span>';
    a += "<p></p>";
    a += '</div><div style="clear: both;"></div></div></div>';
    var c = $(a), b = c.find("span.ui-growl-title"), d = b.next();
    if (this.cfg.escape) {
        b.text(e.summary);
        d.text(e.detail);
    } else {
        b.html(e.summary);
        d.html(e.detail)
    }
    this.bindEvents(c);
    var blockUI = $('<div class="ui-widget-overlay blockUI"></div>');
    blockUI.css("width",$(document).width());
    blockUI.css("height",$(document).height());
    blockUI.css("z-index",parseInt(this.jq.css("z-index"))-1);
    blockUI.appendTo($(document.body));
    c.appendTo(this.jq).fadeIn();
    var icon = c.find("div.ui-growl-icon-close");
    setTimeout('focusOut()',200);
    icon.click(function(){setTimeout('removeBlockUI()',100);});
    $(document).keyup(function(e){
        if(e.keyCode === 27){
            if(icon != null) {
                icon.click();
            }
        }
    });
    this.jq.mouseover(function(){icon.css("display","block");}).mouseout(function(){icon.css("display","block");blockUI.mouseup(function(){if(icon != null){icon.click();}});});
};
PrimeFaces.widget.Growl.prototype.removeAll = function(){
    this.jq.children("div.ui-growl-item-container").remove();
    removeBlockUI();
};
function removeBlockUI(){
    $('.blockUI').remove();
}
function focusOut(){
    $('.ui-state-focus').blur();
}
//]]>