/*
 * Copyright 2012-2018 the original author or authors.
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
import querystring from 'querystring';
import escapeHtml from "./escape-html";
import moment from 'moment';
import Promise from 'pinkie-promise';

import './object-keys-polyfill';
import {getDateRange} from "./utils/get-date-range";
import {formatNumber} from "./utils/format-number";
import {dateToDayKey} from "./utils/date-to-day-key";
import {dateToWeekKey} from "./utils/date-to-week-key";
import {dateToMonthKey} from "./utils/date-to-month-key";
import {dateToYearKey} from "./utils/date-to-year-key";
import {getDataGroupedPerPeriod} from "./utils/get-data-grouped-per-period";
import {calculateTotalDownloads} from "./utils/calculate-total-downloads";
import {getDailyDownloadData} from "./utils/get-daily-download-data";
import {getDownloadsUrl} from "./utils/get-downloads-url";
import {sumUpDownloadCounts} from "./utils/sum-up-download-counts";

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

function requestData(url) {
    return $.ajax({
        url: url,
        dataType: 'json'
    });
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

function getDownloadData(type, packageNames, fromDate, untilDate) {
    return new Promise(function (accept, reject) {
        try {
            var downloadsUrl = getDownloadsUrl(type, packageNames, fromDate, untilDate);
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
        msg = 'Does that package exist? Case matters. <i>' + escapeHtml(error.responseJSON && error.responseJSON.message || '') + '</i>';
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

    getDownloadData('package', packageNames, fromDate, untilDate).then(function (downloadData) {
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

    getDownloadData('author', [authorName], fromDate, untilDate).then(function (sanitizedData) {
        $('#loading').remove();
        showTotalDownloads(sanitizedData, fromDate, untilDate, true);
        var summedUpDownloadCounts = sumUpDownloadCounts(sanitizedData);
        drawCharts(summedUpDownloadCounts, fromDate, untilDate);
    }, showBadLoad);
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
