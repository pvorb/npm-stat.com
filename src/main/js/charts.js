/*
 * Copyright 2012-2017 the original author or authors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
var querystring = require('querystring');
var escapeHtml = require('./escape-html.js');
var moment = require('moment');
var Promise = require('pinkie-promise');

require('./object-keys-polyfill.js');

var $nameType = $('<select id="nameType">\n'
    + '    <option value="package" selected>Package</option>\n'
    + '    <option value="author">Author</option>\n'
    + '</select>');

$nameType.change(function () {
    if ($nameType.val() === 'package') {
        $('#name').attr('name', 'package').attr('placeholder', 'package name(s) (comma separated)');
    } else if ($nameType.val() === 'author') {
        $('#name').attr('name', 'author').attr('placeholder', 'author name');
    }
});

function getDateRange(startDate, stopDate) {
    var dateArray = [];
    var current = moment(startDate).startOf('day');
    var stop = moment(stopDate).startOf('day');
    while (current.isSameOrBefore(stop)) {
        dateArray.push(current.toDate());
        current = current.add(1, 'days');
    }
    return dateArray;
}

function showChart(id, title, data, xAxisType, xAxisTitle, cats) {

    var series = $.map(data, function (values, packageName) {
        return {
            name: packageName,
            data: values,
            type: 'spline'
        };
    });

    new Highcharts.Chart({
        chart: {
            renderTo: id,
            zoomType: 'x'
        },
        colors: ['#CC333F', '#00A0B0', '#73AC42', '#EB6841', '#542437'],
        title: {
            text: title,
            style: {
                color: '#000000'
            }
        },
        subtitle: {
            text: typeof document.ontouchstart === 'undefined' ?
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
        xAxis: (xAxisType === 'datetime' ? {
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
            labels: {
                rotation: -20
            },
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
            enabled: series.length > 1
        },
        plotOptions: {
            spline: {
                marker: {
                    radius: 1.5
                },
                lineWidth: 1,
                states: {
                    hover: {
                        lineWidth: 1
                    }
                },
                threshold: null
            }
        },
        series: series
    });
}

function calculateTotalDownloads(downloadsPerDay) {
    var totalDownloads = 0;
    $.each(downloadsPerDay, function (date, downloadsOfDay) {
        totalDownloads += downloadsOfDay;
    });
    return totalDownloads;
}

function formatNumber(number) {
    return number.toString()
        .replace(/\B(?=(\d{3})+(?!\d))/g, ',');
}

function getDailyDownloadData(downloadData, dateRange) {

    var dailyData = {};

    $.each(downloadData, function (packageName, data) {
        var values = [];
        for (var i = 0; i < dateRange.length; i++) {
            var key = dateToDayKey(dateRange[i]);
            var dateAsMidnight = moment(dateRange[i]).startOf('day').valueOf();
            values.push([dateAsMidnight, data[key] || 0]);
        }
        dailyData[packageName] = values;
    });

    return dailyData;
}

function dateToDayKey(date) {
    return moment(date).format('YYYY-MM-DD');
}

function dateToWeekKey(date) {
    return moment(date).format('GGGG-[W]WW');
}

function dateToMonthKey(date) {
    return moment(date).format('MMM YYYY');
}

function dateToYearKey(date) {
    return moment(date).format('YYYY');
}

function getDataGroupedPerPeriod(downloadData, dateRange, dateToPeriod, nthVisibleAxisLabel) {

    var groupedData = {};

    var xAxisLabels = null;
    var coveredPeriods = {};

    $.each(downloadData, function (packageName, data) {
        var values = [];

        var periodSum = 0;
        var lastPeriod = null;

        for (var i = 0; i < dateRange.length; i++) {

            var key = dateToDayKey(dateRange[i]);
            var currentPeriod = dateToPeriod(dateRange[i]);

            if (coveredPeriods !== null) {
                coveredPeriods[currentPeriod] = true;
            }

            if (currentPeriod !== lastPeriod && lastPeriod !== null) {
                values.push({name: lastPeriod, y: periodSum});
                periodSum = 0;
            }

            periodSum += data[key] || 0;

            lastPeriod = currentPeriod;

            if (i === dateRange.length - 1) {
                // push data for last (incomplete) period
                values.push({name: currentPeriod, y: periodSum});
            }
        }

        if (coveredPeriods !== null) {
            xAxisLabels = Object.keys(coveredPeriods);
        }

        coveredPeriods = null;

        groupedData[packageName] = values;
    });

    for (var i = 0; i < xAxisLabels.length; i++) {
        if (i % nthVisibleAxisLabel !== 0) {
            xAxisLabels[i] = ' ';
        }
    }

    return {
        data: groupedData,
        xAxisLabels: xAxisLabels
    };
}

function requestData(url) {
    return $.ajax({
        url: url,
        dataType: 'json'
    });
}

function getDownloadsUrl(packageNames, fromDate, untilDate) {

    console.dir(packageNames);

    var packageNamesQueryString = '';
    $.each(packageNames, function (i, packageName) {
        packageNamesQueryString += 'package=' + encodeURIComponent(packageName) + '&';
    });

    return '/api/download-counts?' + packageNamesQueryString
        + 'from=' + dateToDayKey(fromDate)
        + '&until=' + dateToDayKey(untilDate);
}

function sumUpDownloadCounts(downloadData) {
    var summedUpDownloads = {};
    $.each(downloadData, function (packageName, packageDownloads) {
        $.each(packageDownloads, function (date, packageDownloadsForDate) {
            if (typeof summedUpDownloads[date] !== 'undefined') {
                summedUpDownloads[date] = summedUpDownloads[date] + packageDownloadsForDate;
            } else {
                summedUpDownloads[date] = packageDownloadsForDate;
            }
        })
    });

    return {total: summedUpDownloads};
}

function drawCharts(downloadData, fromDate, untilDate) {

    var dateRange = getDateRange(fromDate, untilDate);

    var dailyDownloadData = getDailyDownloadData(downloadData, dateRange);
    showChart('days', 'Downloads per day', dailyDownloadData, 'datetime', 'Date');

    var weeklyDownloadData = getDataGroupedPerPeriod(downloadData, dateRange, dateToWeekKey, 4);
    showChart('weeks', 'Downloads per week', weeklyDownloadData.data, 'categories', 'Week',
        weeklyDownloadData.xAxisLabels);

    var monthlyDownloadData = getDataGroupedPerPeriod(downloadData, dateRange, dateToMonthKey, 1);
    showChart('months', 'Downloads per month', monthlyDownloadData.data, 'categories', 'Month',
        monthlyDownloadData.xAxisLabels);

    var annualDownloadData = getDataGroupedPerPeriod(downloadData, dateRange, dateToYearKey, 1);
    showChart('years', 'Downloads per year', annualDownloadData.data, 'categories', 'Year',
        annualDownloadData.xAxisLabels);

}

function showTotalDownloads(sanitizedData, fromDate, untilDate, showSum) {

    var sum = 0;
    var $table = $('<table class="alternating"><tr><td>package</td><td>downloads</td></tr></table>');

    var totals = $.map(sanitizedData, function (downloads, packageName) {
        var totalDownloads = calculateTotalDownloads(downloads);

        if (showSum) {
            sum += totalDownloads;
        }

        return {packageName: packageName, downloads: totalDownloads};
    });

    var sortedTotals = totals.sort(function (a, b) {
        return b.downloads - a.downloads;
    });

    $.each(sortedTotals, function (index, total) {

        var packageHtml;

        if (showSum) {
            packageHtml = '<a href="charts.html?package=' + total.packageName
                + '&from=' + dateToDayKey(fromDate) + '&to=' + dateToDayKey(untilDate) + '">'
                + total.packageName + '</a>';
        } else {
            packageHtml = total.packageName;
        }

        $table.append('<tr><td>' + packageHtml + '</td><td>' + formatNumber(total.downloads) + '</td></tr>');
    });

    if (showSum) {
        $table.append('<tr><td><strong>Σ</strong></td><td><strong>' + formatNumber(sum) + '</strong></td></tr>');
    }

    $('#pkgs')
        .after($table)
        .after('<p>Total number of downloads between <em>'
            + dateToDayKey(fromDate) + '</em> and <em>'
            + dateToDayKey(untilDate) + '</em>:');
}

function getDownloadData(packageNames, fromDate, untilDate) {
    return new Promise(function (accept, reject) {
        try {
            var downloadsUrl = getDownloadsUrl(packageNames, fromDate, untilDate);
            var downloadData = requestData(downloadsUrl);
            return accept(downloadData);
        } catch (error) {
            return reject(error);
        }
    });
}

function showBadLoad(error) {
    var msg = '';

    if (error && error.status === 404) {
        msg = 'Does that package exist? Case matters. <i>' + escapeHtml(error.responseJSON && error.responseJSON.error || '') + '</i>';
    } else {
        msg = escapeHtml(error.status + ' ' + error.statusText + ' ' + error.responseText);
    }

    $('#loading').replaceWith('<div style="padding: 1.2em; background: #900; color: white; text-shadow: 1px 1px 2px rgba(0,0,0, 0.2);"><strong>Could not fetch data.</strong> ' + msg + '</div>');
}

function showPackageStats(packageNames, fromDate, untilDate) {

    var joinedPackageNames = packageNames.join(', ');

    $('h2').append(' for package'
        + (packageNames.length > 1 ? 's' : '')
        + ' <em>' + joinedPackageNames + '</em>');
    $nameType.val('package');
    $('#name')
        .attr('name', 'package')
        .val(joinedPackageNames);

    if (packageNames.length > 5) {
        window.alert('You cannot compare more than 5 packages at once.');
        return;
    }

    var $npmStat = $('#npm-stat');

    $npmStat.after('<p id="loading"><img src="loading.gif" /></p>');

    if (packageNames.length === 1) {
        $npmStat.after('<p><a href="https://www.npmjs.com/package/' + packageNames + '">View package on npm</a></p>');
    }

    getDownloadData(packageNames, fromDate, untilDate).then(function (downloadData) {
        $('#loading').remove();
        showTotalDownloads(downloadData, fromDate, untilDate, false);
        drawCharts(downloadData, fromDate, untilDate);
    }, showBadLoad);
}

function showAuthorStats(authorName, fromDate, untilDate) {
    $('h2').html('Downloads for author <em>' + authorName + '</em>');
    $nameType.val('author');
    $('#name')
        .attr('name', 'author')
        .val(authorName);
    $('#npm-stat').after('<p id="loading"></p><p><a href="https://npmjs.org/~'
        + authorName + '">View author on npm</a></p>');

    $('#loading').html('<img src="loading.gif" />');

    getPackagesForAuthor(authorName).then(function (packageNames) {

        getDownloadData(packageNames, fromDate, untilDate).then(function (sanitizedData) {
            $('#loading').remove();
            showTotalDownloads(sanitizedData, fromDate, untilDate, true);
            var summedUpDownloadCounts = sumUpDownloadCounts(sanitizedData);
            drawCharts(summedUpDownloadCounts, fromDate, untilDate);
        }, showBadLoad);

    }, showBadLoad);
}

function getPackagesForAuthor(authorName) {
    var url = '/api/author-packages?author=' + authorName;
    return requestData(url)
}

function initDate(urlParams, type, baseDate) {
    var date;

    if (urlParams[type]) {
        date = moment(urlParams[type]).startOf('day');
    } else if (baseDate) {
        date = moment(baseDate).startOf('day').subtract(1, 'year');
    } else {
        date = moment().startOf('day').subtract(1, 'day');
    }
    if (!date.isValid()) {
        alert('Invalid date format in URL parameter "' + type + '"');
        return;
    }
    date = date.toDate();
    $('input[name="' + type + '"]').val(dateToDayKey(date));

    return date;
}

$(function () {
    $('#nameType').replaceWith($nameType);
    $('#from, #to').attr('title', 'If the date fields are omitted, you will see the data of the past year.');

    var urlParams = querystring.decode(window.location.search ? window.location.search.substring(1) : '');

    var packageNames = urlParams['package'];
    var authorName = escapeHtml(urlParams['author']);

    if (!packageNames && !authorName) {
        return;
    }

    var untilDate = initDate(urlParams, 'to');
    var fromDate = initDate(urlParams, 'from', untilDate);

    if (packageNames) {
        if (!(packageNames instanceof Array)) {
            packageNames = [packageNames];
        }

        packageNames = $.map(packageNames, function (packageName) {
            return escapeHtml(packageName.trim());
        });

        $('title').html('npm-stat: ' + packageNames.join(', '));
        showPackageStats(packageNames, fromDate, untilDate);
    } else if (authorName) {
        $('title').html('npm-stat: ' + authorName);
        showAuthorStats(authorName, fromDate, untilDate);
    }
});

window.submitForm = function submitForm() {

    var formData = {};

    if ($nameType.val() === 'package') {
        var packageNames = $('input[name=package]').val().split(',');

        if (packageNames.length >= 1 && packageNames[0].trim() !== '') {
            formData['package'] = $.map(packageNames, function (packageName) {
                return packageName.trim();
            });
        } else {
            formData['package'] = ['clone'];
        }
    } else if ($nameType.val() === 'author') {
        var authorName = $('input[name=author]').val().trim();
        formData['author'] = authorName || 'pvorb';
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
