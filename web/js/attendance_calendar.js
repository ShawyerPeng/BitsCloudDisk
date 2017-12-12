/**
 * 创建 AttendanceCalendar 对象后，调用 init 方法生成日历
 *
 * @param _obj 日历的 div 元素
 *
 * @param _fn 考勤数据的获取方法，返回的数据必须为数组，具体属性绑定请见 {@link bindAttendance}
 *
 * 显示日期的 label 的 id
 *
 * model 示例
 * var model = {
 * dValue: 'datetime',   // 数组中日期属性
 * status: 'status',     // 考勤状态属性
 * absense:0,            // 缺勤状态值
 * normal:1              // 正常出勤状态值
 * };
 */
function AttendanceCalendar(_obj, _fn, _lable) {
    var $elem;                          // 日历父 div
    var $content;                       // 日历内容 div
    /**
     * // 获取数据方式
     *
     * 0, 函数获取方式
     * 1. 直接传入数组
     * @type {number}
     */
    var getDataType = 0;
    var getDataFn;                      // 获取数据函数
    var $left, $right;                  // 左右翻动 div
    var $actualDate;                    // 实际日期
    var $selectedSpan;                  // 选中 span
    var myDate;                         // 当前日期
    var date;                           // 当前日 (1-31)
    var day;                            // 当前星期 X(0-6,0 代表星期天)
    var firstWeekDay;                   //1 号星期 X
    var mds;                            // 当月天数
    var index = 0;                      // 索引 (若是需要对日历的属性进行赋值，该索引可直接作为数组索引)
    var hasMoveButton = true;          // 是否开启日历上下月翻动按钮
    var refreshBody = false;           // 是否刷新日历主体
    var target_lable = _lable;          // 当前日期需要绑定的位置
    var currentYM = null;               // 当前年月
    var showCurrentDateFlag = true;     // 是否显示当前日期
    var self = this;                    // 当前日历对象
    var clickFn;                        // 点击日期时调用的外部函数
    var _model = {};                    // 配置显示的数据模型，自定义样式状态值
    /**
     * map 形式的数组
     * 时间可接收时间戳
     * @type {object}
     */
    var map = {};
    /**
     * 传入的数组类型
     * 0. 数组长度刚好与当月天数完全吻合
     * 1. 数组长度与当月天数不吻合
     * @type {number}
     */
    var arryType = 0;
    var attendances = [];               // 绑定的数组
    /**
     * 设置 arryType
     * @param _value
     */
    this.setArryType = function (_value) {
        arryType = _value;
    };
    /**
     * 是否显示左右翻动按钮
     * @param _value
     */
    this.setHasMoveButton = function (_value) {
        hasMoveButton = _value;
    };
    /**
     * 设置数据获取方式
     * @param _value
     */
    this.setGetDataType = function (_value) {
        getDataType = _value;
    }
    /**
     * 设置数组
     * @param _value
     */
    this.setAttendance = function (_value) {
        attendances = _value;
        if (getDataType == 1) {
            initCalendar();
        }
    }

    /**
     * 设置点击日期时调用的外部函数
     * @param _value
     */
    this.setClickFn = function (_value) {
        clickFn = _value;
    }
    /**
     * 获取当前选中日期
     * @returns {*}
     */
    this.getCurrentDate = function () {
        return myDate;
    }
    /**
     * 初始化
     * 调用此方法开始生成日历
     */
    this.init = function () {
        $elem = document.getElementById(_obj);
        if (!$elem) {
            alert(" 需要绑定的考勤日历 div 不存在 ");
            return;
        }
        getDataFn = _fn;
        initCalendar();
    };

    /**
     * 下一月
     */
    this.nextMonth = function () {
        nextMonth();
    }

    /**
     * 上一月
     */
    this.upMonth = function () {
        upMonth();
    }

    this.setModel = function (model) {
        _model = model;
    }

    /**
     * 创建日历
     */
    function initCalendar() {
        index = 0;              // 索引
        initArray();
        initDate();
        $elem.innerHTML = null;
        // addLeftBtn();
        initCalendarBody();
        // addRightBtn();
    };

    /**
     * 初始化绑定数据
     * 此获取的数组长度必须与当月的天数一致
     */
    function initArray() {
        if (getDataType == 0) {
            if (getDataFn && typeof getDataFn == "function") {
                attendances = getDataFn(getYearAndMonth(myDate));
                if (attendances && attendances.length) {
                    arrayToMap();
                }
                else {
                    console.log(" 日历暂时没有绑定数据!");
                }
            }
        }
        if (getDataType == 1) {
            arrayToMap();
        }
    }

    /**
     * 数组填充 map
     */
    function arrayToMap() {
        map = {};   // 清空 map
        var length = attendances.length;
        if (length == mds) {
            arryType = 0;
        } else {
            arryType = 1;
        }
        for (var i = 0; i < length; i++) {
            if (!_model || !_model.dValue) {
                console.log(" 请先配置 model!");
            }
            var key = attendances[i][_model.dValue];
            if (typeof key == "object") {
                map[key.getDate()] = attendances[i];
            }
            if (typeof key == "number") {
                var temp_date = new Date();
                temp_date.setTime(key);
                map[temp_date.getDate()] = attendances[i];
            }
        }
    }

    /**
     * 获取随即数
     * @param Min
     * @param Max
     * @returns {*}
     * @constructor
     */
    function GetRandomNum(Min, Max) {
        var Range = Max - Min;
        var Rand = Math.random();
        return (Min + Math.round(Rand * Range));
    }

    /**
     * ym 年月
     * 初始化参数
     */
    function initDate() {
        setDate();
        date = myDate.getDate();
        day = myDate.getDay();
        firstWeekDay = getfirstWeekDay();
        mds = DayNumOfMonth(myDate.getYear(), myDate.getMonth() + 1);
        currentYM = myDate.getFullYear() + "-" + formatMonth(myDate.getMonth());
    }

    /**
     * 比较传入日期与当前日期年月是否一样，一样则不刷新当前日历，否则刷新
     * @param _newDate
     */
    function setDate(ym) {
        if (ym) {
            var _newDate = convertDateFromString(ym);
            var newYm = getYearAndMonth(_newDate);
            var currentYm = getYearAndMonth(myDate);
            if (newYm == currentYm) {
                refreshBody = false;
            }
            else {
                myDate = _newDate;
                refreshBody = true;
            }
        }
        else {
            if (!myDate) {
                myDate = new Date();
                $actualDate = myDate;
            }
        }
    }

    /**
     * 月份补 0
     * @param target
     * @returns {*}
     */
    function formatMonth(target) {
        if (target < 9) {
            target = "0" + (target + 1);
        }
        else {
            target = target + 1;
        }
        return target;
    }

    /**
     * 日补 0
     */
    function formatDate(target) {
        if (typeof target != "number") {
            target = parseInt(target);
        }
        if (target < 10) {
            target = "0" + (target);
        }
        return target;
    }

    /**
     * 获取星期
     */
    function formatDay(_day) {
        if (typeof _day != "number") {
            _day = parseInt(_day);
        }
        switch (_day) {
            case 0:
                _day = " 星期日 ";
                break;
            case 1:
                _day = " 星期一 ";
                break;
            case 2:
                _day = " 星期二 ";
                break;
            case 3:
                _day = " 星期三 ";
                break;
            case 4:
                _day = " 星期四 ";
                break;
            case 5:
                _day = " 星期五 ";
                break;
            case 6:
                _day = " 星期六 ";
                break;
        }
        return _day;
    }

    /**
     * 转换显示的完整日期
     *
     * 兼容 ios 显示
     * @param target
     * @returns {string}
     */
    function getCompleteDate(target) {
        if ($selectedSpan) {
            var year = currentYM.substr(0, 4);
            var month = currentYM.substr(5, 2);
            var _day = formatDate(target.getDate());
            var _weekday = formatDay(target.getDay());
            return year + "-" + month + "-" + _day + " " + _weekday;
        }
        else {
            if (!target) {
                target = new Date();
            }
            var month = formatMonth(target.getMonth());
            var _date = formatDate(target.getDate());
            var _day = formatDay(target.getDay());
            return target.getFullYear() + "-" + month + "-" + _date + " " + _day;
        }

    }

    /**
     * 根据日期获取年月
     * @param target
     * @returns {string}
     */
    function getYearAndMonth(target) {
        if (!target) {
            target = new Date();
        }
        var month = target.getMonth();
        if (month < 9) {
            month = "0" + (month + 1);
        }
        else {
            month = month + 1;
        }
        return target.getFullYear() + "-" + month;
    }

    /**
     * 初始化日历主体
     */
    function initCalendarBody() {
        $content = document.createElement("div");
        addClass($content, "div_calendar_body");
        $elem.appendChild($content);
        addCalendarHead();

        // 日期计算 bug，例：2017-01
        var num = 0;
        do {
            var length = 0;
            if (num == 0) {
                // 除星期天外，星期数 (1~6) 和第一行显示的日期数 (7~2)，相加的结果都为 8
                length = firstWeekDay == 0 ? 1 : 8 - firstWeekDay;
                num = length;
                createLine(length - 1, 1);
            }
            else {
                var length = mds - num > 6 ? 7 : mds - num;
                num = num + length;
                createLine(length - 1);
            }
        }
        while (num < mds)
        showCurrentDate();
    }

    /**
     * 计算 1 号星期 X
     * @returns {number|*}
     */
    function getfirstWeekDay() {
        result = day - (date % 7 - 1);
        return result < 0 ? 7 + result : result;
    }

    /**
     * 根据年月查询当月天数
     * @param Year
     * @param Month
     * @returns {number}
     * @constructor
     */
    function DayNumOfMonth(Year, Month) {
        Month--;
        var d = new Date(Year, Month, 1);
        d.setDate(d.getDate() + 32 - d.getDate());
        return (32 - d.getDate());
    }

    /**
     * 日历头部
     * @param calendar_div
     */
    function addCalendarHead() {
        var newDiv = document.createElement("div");
        var calendarHeads = [" 一 ", " 二 ", " 三 ", " 四 ", " 五 ", " 六 ", " 日 "];
        for (var i = 0; i < calendarHeads.length; i++) {
            var newspan = document.createElement("span");
            var span_text = document.createTextNode(calendarHeads[i]);
            newspan.appendChild(span_text);
            newDiv.appendChild(newspan);
        }
        $content.appendChild(newDiv);
    }

    /**
     * 创建一行数据，并绑定数据
     * @param num 当前行的 span 数量
     * type:1 为首行，2 为末行
     */
    function createLine(num, type) {
        var newDiv = document.createElement("div");
        for (var i = 0; i <= num; i++, index++) {
            var span = document.createElement("span");
            var weekDay = 0;
            if (type == 1) {
                // 设置星期数
                var weekDay = firstWeekDay + i;
            }
            else {
                weekDay = i + 1;
            }
            if (weekDay > 6) {
                weekDay -= 7;
            }
            span.setAttribute("weekday", weekDay);
            var span_text = document.createTextNode(index + 1);
            span.setAttribute("dayOfMonth", index + 1);
            setTodayClass(span);
            setOnClick(span);
            bindType(span);
            span.appendChild(span_text);
            newDiv.appendChild(span);
        }
        if (type == 1) {
            addClass(newDiv, "first_div")
        }
        else {
            addClass(newDiv, "last_div")
        }
        $content.appendChild(newDiv);
    }

    /**
     * 设置今日元素样式和默认选中
     */
    function setTodayClass(span) {
        var actual_ym = getYearAndMonth(new Date());
        var actual_date = $actualDate.getDate();
        var ym = getYearAndMonth(myDate);
        if (actual_ym == ym) {
            if (index == actual_date - 1) {
                addClass(span, "today");
            }
        }
        if (index == myDate.getDate() - 1) {
            addClass(span, "select");
        }

    }

    /**
     * 设置单击事件
     */
    function setOnClick(span) {
        span.onclick = function () {
            var spans = document.getElementsByClassName("select");
            if (spans && spans.length > 0) {
                spans[0].className = spans[0].className.replace("select", "").trim();
            }
            $selectedSpan = this;
            addClass(this, "select");
            var _date = formatDate(parseInt(this.innerText));
            myDate = convertDateFromString(currentYM + "-" + _date);
            showCurrentDate();
            if (typeof clickFn == "function") clickFn(myDate);
        }
    }

    /**
     * 设置绑定方式
     */
    function bindType(span) {
        if (arryType == 0) {
            if (attendances && attendances.length > 0) {
                bindAttendance(span, attendances[index]);
            }
        }
        else {
            if (map && map[index + 1]) {
                bindAttendance(span, map[index + 1]);
            }
        }
    }

    /**
     * 绑定渲染
     * @param span 日元素
     * @param attendance 日考勤对象
     */
    function bindAttendance(span, attendance) {
        if (attendance) {
            span.setAttribute("id", attendance[_model.date]);
            switch (attendance[_model.status]) {
                case _model.absense:
                    addClass(span, "absense");
                    break;
                case _model.normal:
                    addClass(span, "normal");
                    break;
            }
        }
    }

    /**
     * 显示当前年月
     */
    function showCurrentDate() {
        if (!showCurrentDateFlag) return;
        if (target_lable) {
            document.getElementById(target_lable).innerText = getCompleteDate(myDate);
        }
        else {
            if (document.getElementById("currentDate_text")) {
                var text = document.getElementById("currentDate_text")
                text.innerHTML = getCompleteDate(myDate);
            }
            else {
                var currentDiv = document.createElement("div");
                addClass(currentDiv, "div_currentDate");
                span = document.createElement("span");
                span.setAttribute("id", "currentDate_label");
                var em_title = document.createElement("em");
                var em_text = document.createTextNode(" 当前年月:");
                em_title.appendChild(em_text);
                var em_date = document.createElement("em");
                var currentdate_text = getCompleteDate(myDate);
                var em_date_text = document.createTextNode(currentdate_text);
                em_date.setAttribute("id", "currentDate_text");
                em_date.appendChild(em_date_text);
                span.appendChild(em_title);
                span.appendChild(em_date);
                currentDiv.appendChild(span);
                $content.appendChild(currentDiv);
            }
        }
    }


    /**
     * string 转 date
     * @param dateString
     * @returns {Date}
     */
    function convertDateFromString(dateString) {
        if (dateString) {
            // 在 ios 环境下 new Date() 参数只接收 yyyy/MM/dd，而在 android 或者 web 端没有严格要求
            while (dateString.indexOf("-") > -1) {
                dateString = dateString.replace(/-/, "/");
            }
            var date = new Date(dateString);
            return date;
        }
    }

    /**
     * 添加左边翻动按钮，向前一个月
     */
    function addLeftBtn() {
        if (!hasMoveButton) return;
        $left = document.createElement("div");
        var span = document.createElement("span");
        var span_text = document.createTextNode("<");
        addClass(span, "changeDate");
        span.appendChild(span_text);
        span.setAttribute("title", " 上个月 ");
        span.onclick = function () {
            upMonth();
        }
        $left.appendChild(span);
        $elem.appendChild($left);

    }

    /**
     * 添加右边翻动按钮，向后一个月
     */
    function addRightBtn() {
        if (!hasMoveButton) return;
        $right = document.createElement("div");
        var span = document.createElement("span");
        var span_text = document.createTextNode(">");
        addClass(span, "changeDate");
        span.setAttribute("title", " 下个月 ");
        span.onclick = function () {
            nextMonth();
        }
        span.appendChild(span_text);
        $right.appendChild(span);
        $elem.appendChild($right);
    }

    /**
     * 下个月
     */
    function nextMonth() {
        myDate.addMonths(1);
        // 直接传入数组的方式，由设置数组时初始化
        if (getDataType != 1) {
            initCalendar();
        }
    }

    /**
     * 上个月
     */
    function upMonth() {
        myDate.addMonths(-1);
        // 直接传入数组的方式，由设置数组时初始化
        if (getDataType != 1) {
            initCalendar();
        }
    }

    /**
     * 给指定的 element 追加样式
     * @param element
     * @param value
     */
    function addClass(element, value) {
        if (!element.className) {
            element.className = value;
        }
        else {
            var newClass = element.className;
            newClass += " ";
            newClass += value;
            element.className = newClass;
        }
    }
}


/**** 此为测试 js 部分 测试日历 start  *****/
var ac = new AttendanceCalendar("calendar_div", null, "current_date_label");
ac.init();
ac.setGetDataType(1);
var model = {
    dValue: 'datetime',
    status: 'status',
    absense: 0,
    normal: 1
};
ac.setModel(model);
ac.setAttendance(getData2());

function upMonth() {
    ac.upMonth();
    ac.setAttendance(getData2());
}

function nextMonth() {
    ac.nextMonth();
    ac.setAttendance(getData2());
}

/**
 * @param ym ni 月
 * @returns {Array}
 */
function getData(ym) {
    var attendances = [];
    for (var i = 0; i < 30; i++) {
        var num = GetRandomNum(0, 2);
        attendances.push({day: i, status: num});
    }
    return attendances;
}

function getData2() {
    var attendances = [];
    var today = new Date();
    attendances.push({datetime: today.clone().addDays(1).valueOf(), status: GetRandomNum(0, 2)});
    attendances.push({datetime: today.clone().addDays(2).valueOf(), status: GetRandomNum(0, 2)});
    attendances.push({datetime: today.clone().addDays(-5).valueOf(), status: GetRandomNum(0, 2)});
    attendances.push({datetime: today.clone().addDays(-7).valueOf(), status: GetRandomNum(0, 2)});
    attendances.push({datetime: today.clone().addDays(-11).valueOf(), status: GetRandomNum(0, 2)});
    attendances.push({datetime: today.clone().addDays(-12).valueOf(), status: GetRandomNum(0, 2)});
    return attendances;
}

function GetRandomNum(Min, Max) {
    var Range = Max - Min;
    var Rand = Math.random();
    return (Min + Math.round(Rand * Range));
}

ac.setClickFn(clickFn);
function clickFn(clickDate) {
    alert(clickDate);
}
/**** 此为测试 js 部分 测试日历  *****/
// console.log(DayNumOfMonth(2016, 2));