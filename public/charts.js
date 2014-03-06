(function e(t,n,r){function s(o,u){if(!n[o]){if(!t[o]){var a=typeof require=="function"&&require;if(!u&&a)return a(o,!0);if(i)return i(o,!0);throw new Error("Cannot find module '"+o+"'")}var f=n[o]={exports:{}};t[o][0].call(f.exports,function(e){var n=t[o][1][e];return s(n?n:e)},f,f.exports,e,t,n,r)}return n[o].exports}var i=typeof require=="function"&&require;for(var o=0;o<r.length;o++)s(r[o]);return s})({1:[function(require,module,exports){
/*!
 * (c) 2012-2014 Paul Vorbach.
 *
 * MIT License (http://vorba.ch/license/mit.html)
 */

// Object.keys polyfill
// from https://developer.mozilla.org/en-US/docs/Web/JavaScript/Reference/Global_Objects/Object/keys
if (!Object.keys) {
  Object.keys = (function () {
    'use strict';
    var hasOwnProperty = Object.prototype.hasOwnProperty,
        hasDontEnumBug = !({toString: null}).propertyIsEnumerable('toString'),
        dontEnums = [
          'toString',
          'toLocaleString',
          'valueOf',
          'hasOwnProperty',
          'isPrototypeOf',
          'propertyIsEnumerable',
          'constructor'
        ],
        dontEnumsLength = dontEnums.length;

    return function (obj) {
      if (typeof obj !== 'object' && (typeof obj !== 'function' || obj === null)) {
        throw new TypeError('Object.keys called on non-object');
      }

      var result = [], prop, i;

      for (prop in obj) {
        if (hasOwnProperty.call(obj, prop)) {
          result.push(prop);
        }
      }

      if (hasDontEnumBug) {
        for (i = 0; i < dontEnumsLength; i++) {
          if (hasOwnProperty.call(obj, dontEnums[i])) {
            result.push(dontEnums[i]);
          }
        }
      }
      return result;
    };
  }());
}

function getURLParam(name) {
  var match = new RegExp(name + '=' + '(.*?)(&|$)')
      .exec(location.search);
  if (match === null)
    return null;

  return match[1];
}

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

var oneDay = 24 * 60 * 60 * 1000;

function totalDownloads(data) {
  var result = 0;
  for (var date in data) {
    result += data[date];
  }
  return result;
}

function sanitizeData(json) {
  var result = {};
  var data = json.downloads;

  var date = null;
  for (var i = 0; i < data.length; i++) {
    date = data[i].day;
    result[date] = data[i].downloads;
  }

  return result;
}

function getDailyData(data) {
  var result = [];

  var keys = Object.keys(data).sort();
  for (var i = 0; i < keys.length; i++) {
    result.push([ new Date(keys[i]).getTime(), data[keys[i]] ]);
  }

  return result;
}

function getWeekOfDate(d) {
  var year = new Date(d.getFullYear(), 0, 1);
  return Math.ceil((((d - year) / 86400000) + year.getDay() + 1) / 7);
}

function getWeeklyData(dailyData) {
  var result = [];

  var lastWeek = getWeekOfDate(new Date(dailyData[0][0]));
  var weekTotal = 0;
  var record, date;

  for (var i in dailyData) {
    record = dailyData[i];
    date = new Date(record[0]);
    if (lastWeek != getWeekOfDate(date)) {
      result.push([ date.getTime(), weekTotal ]);
      weekTotal = record[1];
      lastWeek = getWeekOfDate(date);
    } else {
      weekTotal += record[1];
      if (i == dailyData.length - 1)
        result.push([ date.getTime(), weekTotal]);
    }
  }

  return result;
}

var months = [ 'Dec', 'Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep',
    'Oct', 'Nov', 'Dec' ];

function getMonthlyData(dailyData) {
  var result = { categories: [], data: [] };

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
  var result = { categories: [], data: [] };

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
      console.log('Could not receive statistical data.');
      $('#loading').html('An error occured. Please try to reload the page or '
        + 'contact me at <a href="mailto:paul@vorba.ch">paul@vorba.ch</a> if '
        + 'that doesn\'t help.');
    }
  });
}

function numPad(x) {
  return x < 10 ? '0'+x : x;
}

function dateToString(date) {
  return date.getFullYear() + '-' + numPad(date.getMonth() + 1) + '-'
    + numPad(date.getDay());
}

function dateToHumanString(date) {
  return date.toDateString().substring(4);
}

function downloadsURL(pkg, from, to) {
  return '/downloads/range/' + dateToString(from) + ':' + dateToString(to) + '/'
    + pkg;
}

function drawCharts(data) {
  var dailyData = getDailyData(data);
  $('#content figure').css('min-width', dailyData.length * 2 + 67);
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

function showPackageStats(pkg, from, to) {
  $('h2').append(' for package "' + pkg + '"');
  $('#npm-stat input[type=search]').attr('value', pkg);
  $('#npm-stat-author').after('<p id="loading"></p><p><a '
    + 'href="https://npmjs.org/package/'
    + pkg + '">View package on npm</a></p>');

  $('#loading').html('<img src="loading.gif" />');

  var url = downloadsURL(pkg, from, to);

  getData(url, function (json) {
    var data = sanitizeData(json);
    $('h2').after('<p>Total number of downloads between '
      + dateToHumanString(from) + ' and '
      + dateToHumanString(to) + ': '
      + totalDownloads(data) + '</p>');

    $('#loading').remove();

    drawCharts(data);
  });
}

function showAuthorStats(author, from, to) {
  $('h2').html('Downloads for author "' + author + '"');
  $('#npm-stat-author input[type=search]').attr('value', author);
  $('#npm-stat-author').after('<p id="loading"></p><p><a '
    + 'href="https://npmjs.org/~'
    + author + '">View author on npm</a></p>');

  $('#loading').html('<img src="loading.gif" />');

  var url = '/-/_view/browseAuthors?'
    +'group_level=3&start_key=["'+author+'"]&end_key=["'+author+'",{}]';

  getData(url, function (json) {
    var pkgs = getPackageList(json);
    var len = pkgs.length;
    var todo = len;

    var all = {};
    var totals = [];
    for (var i = 0; i < len; i++) {(function (pkg) {
      var url = downloadsURL(pkg, from, to);
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
          $('h2').after('<p>All downloads of packages by author '
            + author + ' between '
            + dateToHumanString(from) + ' and '
            + dateToHumanString(to) + ': '
            + totalDownloads(data) + '</p>');

          $('#loading').remove();

          totals = totals.sort(function(a,b) {
            return b.count - a.count;
          });

          $('#pkgs').append('<h3>Packages by '+author+'</h3><ul></ul>');
          for (var i = 0; i < totals.length; i++) {
            var t = totals[i];
            $('#pkgs ul').append('<li><a href="charts.html?package='
              + t.name + '" title="view detailed download statistics">'
              + t.name + '</a>, total downloads: '+t.count+'</li>');
          }

          drawCharts(all);
        }
      });
      })(pkgs[i]);
    }
  });
}

$(function() {
  var from, to;
  var period = getURLParam('period');
  if (period === null || period === '') {
    to = new Date();
    from = new Date(to.getTime() - (1000*60*60*24*365*2));
  } else {
    var range = period.split(':');
    from = new Date(range[0]);
    to = new Date(range[1]);
  }

  var pkg;

  var author = getURLParam('author');
  if (author === null) {
    pkg = getURLParam('package');

    if (pkg === null || pkg === '')
      pkg = 'clone';

    $('title').html('npm-stat: ' + pkg);
    showPackageStats(pkg, from, to);
  } else {
    if (author === '')
      author = 'pvorb';

    $('title').html('npm-stat: ' + author);
    showAuthorStats(author);
  }
});

},{}]},{},[1])