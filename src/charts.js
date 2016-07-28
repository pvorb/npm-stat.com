/*!
 * (c) 2012-2016 Paul Vorbach.
 *
 * MIT License (https://vorba.ch/license/mit.html)
 */

var querystring = require('querystring');

require('./object-keys-polyfill.js');

var $nameType = $('<select id="nameType">\n'
    + '    <option value="package" selected>Package</option>\n'
    + '    <option value="author">Author</option>\n'
    + '</select>');

$nameType.change(function () {
    if ($nameType.val() == 'package') {
        $('#name').attr('name', 'package').attr('placeholder', 'package name');
    } else if ($nameType.val() == 'author') {
        $('#name').attr('name', 'author').attr('placeholder', 'author name');
    }
});

function chart(id, type, title, data, xAxisType, xAxisTitle, cats) {
    return new Highcharts.Chart({
        chart: {
            renderTo: id,
            zoomType: 'x',
            type: type
        },
        title: {
            text: title,
            style: {
                color: '#000000'
            }
        },
        subtitle: {
            text: typeof document.ontouchstart == 'undefined' ?
                'Click and drag in the plot to zoom in' :
                'Drag your finger over the plot to zoom in',
            style: {
                color: '#000000'
            }
        },
        exporting: {
            enableImages: true
        },
        credits: {
            enabled: false
        },
        xAxis: (xAxisType == 'datetime' ? {
            type: xAxisType,
            maxZoom: 14 * 24 * 60 * 60 * 1000,
            lineColor: '#000000',
            title: {
                text: xAxisTitle,
                style: {
                    color: '#000000'
                }
            }
        } : {
            type: xAxisType,
            lineColor: '#000000',
            categories: cats,
            title: {
                text: xAxisTitle,
                style: {
                    color: '#000000'
                }
            }
        }),
        yAxis: {
            min: 0,
            startOnTick: false,
            showFirstLabel: false,
            title: {
                text: 'Downloads',
                style: {
                    color: '#000000'
                }
            }
        },
        tooltip: {
            shared: true
        },
        legend: {
            enabled: false
        },
        plotOptions: {
            column: {
                borderWidth: 0,
                color: '#AA0000',
                pointPadding: 0,
                shadow: false
            },
            line: {
                color: '#AA0000',
                lineWidth: 1,
                marker: {
                    radius: 2
                }
            }
        },
        series: [{
            name: 'Downloads',
            data: data
        }]
    });
}

function totalDownloads(data) {
    var result = 0;
    for (var date in data) {
        result += data[date];
    }
    return result.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function sanitizeData(json) {
    var result = {};
    var data = json.downloads;

    var date = null;
    if (data) {
        for (var i = 0; i < data.length; i++) {
            date = data[i].day;
            result[date] = data[i].downloads;
        }
    }

    return result;
}

function getDailyData(data) {
    var result = [];

    var keys = Object.keys(data).sort();
    for (var i = 0; i < keys.length; i++) {
        result.push([new Date(keys[i]).getTime(), data[keys[i]]]);
    }

    return result;
}

function getWeekOfDate(d) {
    return Math.floor(((d.getTime() / 86400000) + 3) / 7);
}

function getWeeklyData(dailyData) {
    var result = [];

    var lastWeek = getWeekOfDate(new Date(dailyData[0][0]));
    var weekTotal = 0;
    var record, date;

    for (var i in dailyData) {
        record = dailyData[i];
        date = new Date(record[0]);
        var week = getWeekOfDate(date);
        if (lastWeek != week) {
            result.push([new Date(((lastWeek * 7) - 3) * 86400000).getTime(), weekTotal]);
            weekTotal = record[1];
            lastWeek = week;
        } else {
            weekTotal += record[1];
            if (i == dailyData.length - 1)
                result.push([new Date(((week * 7) - 3) * 86400000).getTime(), weekTotal]);
        }
    }

    return result;
}

var months = ['Dec', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
    'Oct', 'Nov', 'Dec'];

function getMonthlyData(dailyData) {
    var result = {categories: [], data: []};

    var lastMonth = new Date(dailyData[0][0]).getMonth();
    var lastYear = new Date(dailyData[0][0]).getFullYear();
    var monthTotal = 0;
    var record, date;

    for (var i = 0; i < dailyData.length; i++) {
        record = dailyData[i];
        date = new Date(record[0]);
        if (lastMonth != date.getMonth()) {
            result.categories.push(months[lastMonth + 1] + ' ' + lastYear);
            result.data.push(monthTotal);
            monthTotal = record[1];
            lastMonth = date.getMonth();
            lastYear = date.getFullYear();
        } else {
            monthTotal += record[1];
            if (i == dailyData.length - 1) {
                result.categories.push(months[date.getMonth() + 1] + ' ' + date.getFullYear());
                result.data.push(monthTotal);
            }
        }
    }

    return result;
}

function getAnnualData(dailyData) {
    var result = {categories: [], data: []};

    var lastYear = new Date(dailyData[0][0]).getFullYear();
    var yearTotal = 0;
    var record, date;

    for (var i = 0; i < dailyData.length; i++) {
        record = dailyData[i];
        date = new Date(record[0]);
        if (lastYear != date.getFullYear()) {
            result.categories.push(lastYear.toString());
            result.data.push(yearTotal);
            yearTotal = record[1];
            lastYear = date.getFullYear();
        } else {
            yearTotal += record[1];
            if (i == dailyData.length - 1) {
                result.categories.push(date.getFullYear().toString());
                result.data.push(yearTotal);
            }
        }
    }

    return result;
}

function getPackageList(json) {
    var result = [];
    var len = json.rows.length;
    for (var i = 0; i < len; i++) {
        result.push(json.rows[i].key[1]);
    }
    return result;
}

function getData(url, callback) {
    $.ajax({
        url: url,
        dataType: 'json',
        success: callback,
        error: function () {
            console.log('Could not receive download data.');
            $('#loading').html('An error occured. Please try to reload the page or contact me at '
                + '<a href="mailto:paul@vorba.ch">paul@vorba.ch</a> if that doesn\'t help.');
        }
    });
}

function padNumberZero(x) {
    return x < 10 ? '0' + x : x;
}

function dateToString(date) {
    return date.getFullYear() + '-' + padNumberZero(date.getMonth() + 1) + '-'
        + padNumberZero(date.getDate());
}

function downloadsUrl(pkg, from, to) {
    return '/downloads/range/' + dateToString(from) + ':' + dateToString(to) + '/'
        + encodeURIComponent(pkg);
}

function drawCharts(data) {
    var dailyData = getDailyData(data);
    $('#content').find('figure').css('min-width', dailyData.length * 2 + 67);
    chart('days', 'column', 'Downloads per day', dailyData, 'datetime', 'Date');
    var weeklyData = getWeeklyData(dailyData);
    chart('weeks', 'column', 'Downloads per week', weeklyData, 'datetime', 'Week');
    var monthlyData = getMonthlyData(dailyData);
    chart('months', 'column', 'Downloads per month', monthlyData.data,
        'linear', 'Month', monthlyData.categories);
    var annualData = getAnnualData(dailyData);
    chart('years', 'column', 'Downloads per year', annualData.data,
        'linear', 'Year', annualData.categories);
}

function showPackageStats(packageName, fromDate, toDate) {
    $('h2').append(' for package <em>' + packageName + '</em>');
    $nameType.val('package');
    $('#name').val(packageName);
    $('#npm-stat').after('<p id="loading"></p><p><a '
        + 'href="https://npmjs.org/package/'
        + packageName + '">View package on npm</a></p>');

    $('#loading').html('<img src="loading.gif" />');

    var url = downloadsUrl(packageName, fromDate, toDate);

    getData(url, function (json) {
        var data = sanitizeData(json);
        $('h2').after('<p>Total number of downloads between <em>'
            + dateToString(fromDate) + '</em> and <em>'
            + dateToString(toDate) + '</em>: <strong>'
            + totalDownloads(data) + '</strong></p>');

        $('#loading').remove();

        drawCharts(data);
    });
}

function showAuthorStats(authorName, fromDate, toDate) {
    $('h2').html('Downloads for author <em>' + authorName + '</em>');
    $nameType.val('author');
    $('#name').val(authorName);
    $('#npm-stat').after('<p id="loading"></p><p><a href="https://npmjs.org/~' + authorName
        + '">View author on npm</a></p>');

    $('#loading').html('<img src="loading.gif" />');

    var url = '/-/_view/browseAuthors?group_level=3&start_key=["' + authorName + '"]&end_key=["' + authorName + '",{}]';

    getData(url, function (json) {
        var pkgs = getPackageList(json);
        var len = pkgs.length;
        var todo = len;

        var all = {};
        var totals = [];
        for (var i = 0; i < len; i++) {
            (function (pkg) {
                var url = downloadsUrl(pkg, fromDate, toDate);
                getData(url, function (json) {
                    var sanitized = sanitizeData(json);

                    for (var date in sanitized) {
                        if (!all[date])
                            all[date] = 0;

                        all[date] += sanitized[date];
                    }

                    var total = totalDownloads(sanitized);
                    totals.push({name: pkg, count: total});

                    if (!--todo) {
                        $('h2').after('<p>Cumulative downloads of packages by author <em>'
                            + authorName + '</em> from <em>'
                            + dateToString(fromDate) + '</em> to <em>'
                            + dateToString(toDate) + '</em>: <strong>'
                            + totalDownloads(all) + '</strong></p>');

                        $('#loading').remove();

                        totals = totals.sort(function (a, b) {
                            return b.count.replace(/,/g, '') - a.count.replace(/,/g, '');
                        });

                        var $pkgs = $('#pkgs');
                        $pkgs.append('<h3>Packages by <em>' + authorName + '</em></h3><table><tr><td>package name</td><td>downloads</td></tr></table>');
                        for (var i = 0; i < totals.length; i++) {
                            var t = totals[i];
                            $pkgs.find('table').append('<tr><td><a href="charts.html?package='
                                + t.name + '" title="view detailed download statistics">'
                                + t.name + '</a></td><td>' + t.count + '</td></tr>');
                        }

                        drawCharts(all);
                    }
                });
            })(pkgs[i]);
        }
    });
}

function initDate(urlParams, type, baseDate) {
    var date;

    if (!urlParams[type]) {
        date = baseDate
            ? new Date(baseDate.getTime() - (1000 * 60 * 60 * 24 * 365))
            : new Date();
    } else {
        try {
            date = new Date(urlParams[type]);
        } catch (e) {
            return alert('Invalid date format in URL parameter "' + type + '"');
        }
    }

    $('input[name="' + type + '"]').val(dateToString(date));

    return date;
}

$(function () {
    $('#nameType').replaceWith($nameType);
    $('#from, #to').attr('title', 'If the date fields are omitted, you will see the data of the last 365 days.');

    var urlParams = querystring.decode(window.location.search ? window.location.search.substring(1) : '');

    var packageName = urlParams['package'];
    var authorName = urlParams['author'];

    if (!packageName && !authorName) {
        return;
    }

    var toDate = initDate(urlParams, 'to');
    var fromDate = initDate(urlParams, 'from', toDate);

    if (packageName) {
        $('title').html('npm-stat: ' + packageName);
        showPackageStats(packageName, fromDate, toDate);
    } else if (authorName) {
        $('title').html('npm-stat: ' + authorName);
        showAuthorStats(authorName, fromDate, toDate);
    }
});

window.submitForm = function submitForm() {

    var formData = {};

    var name = $('#name').val();

    if ($nameType.val() == 'package') {
        formData['package'] = name || 'clone';
    } else if ($nameType.val() == 'author') {
        formData['author'] = name || 'pvorb';
    }

    formData['from'] = $('#from').val();
    formData['to'] = $('#to').val();

    for (var key in formData) {
        if (!formData[key]) {
            delete formData[key];
        }
    }

    window.location = '/charts.html?' + querystring.encode(formData);

    return false;
};
