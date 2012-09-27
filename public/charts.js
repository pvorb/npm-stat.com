/*!
 * (c) 2012 Paul Vorbach.
 *
 * MIT License (http://vorb.de/license/mit.html)
 */

(function () {

  function getURLParam(name) {
    var match = new RegExp(name + '=' + '(.+?)(&|$)')
        .exec(location.search);
    if (match === null)
      return null;

    return match[1];
  }

  $(document).ready(function () {
    var pkg = getURLParam('package');

    if (pkg === null)
      pkg = 'npm';

    $('h2').html('Download statistics for <input type="search" '
      + 'name="package" size="18" value="'+pkg
      + '"> <input type="submit" value="Show charts">');
    $('h2').after('<p><a href="https://npmjs.org/package/'+pkg
      + '">View package on npm</a></p>');

    url = 'http://isaacs.iriscouch.com/downloads/_design/app/_view/pkg?'
      + 'group_level=3&start_key=["'+pkg+'"]&end_key=["'+pkg+'",{}]';

    $.ajax({
      url: url,
      dataType: 'jsonp',
      success: function (json) {
        var dailyData = getDailyData(json);
        chart('days', 'Downloads per day', dailyData, 'datetime');
        var weeklyData = getWeeklyData(dailyData);
        chart('weeks', 'Downloads per week', weeklyData, 'datetime');
        var monthlyData = getMonthlyData(dailyData);
        chart('months', 'Downloads per month', monthlyData.data, 'linear',
          monthlyData.categories);
      },
      error: function () {
        alert('Could not receive statistical data.');
      }
    });
  });

  function chart(id, title, data, xAxisType, cats) {
    return new Highcharts.Chart({
      chart: {
        renderTo: id,
        zoomType: 'x',
        type: 'column'
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
          text: 'Date',
          style: {
            color: '#000000'
          }
        }
      } : {
        type: xAxisType,
        lineColor: '#000000',
        categories: cats,
        title: {
          text: 'Month',
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
          color: '#AA0000'
        }
      },
      series: [{
        name: 'Downloads',
        data: data
      }]
    });
  }

  var oneDay = 24 * 60 * 60 * 1000;

  function getDailyData(json) {
    var result = [];
    var data = json.rows;

    var date = new Date(data[0].key[1]);

    for (var i in data) {
      date = new Date(data[i].key[1]);
      result.push([ date.getTime(), data[i].value ]);
    }

    return result;
  }

  function getWeekOfDate(d) {
    var year = new Date(d.getFullYear(), 0, 1);
    return Math.ceil((((d - year) / 86400000) + year.getDay() + 1) / 7);
  }

  var threeDays = 3 * oneDay;

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
    var monthTotal = 0;
    var record, date;

    for (var i in dailyData) {
      record = dailyData[i];
      date = new Date(record[0]);
      if (lastMonth != date.getMonth()) {
        result.categories.push(months[date.getMonth()] + ' ' + date.getFullYear());
        result.data.push(monthTotal);
        monthTotal = record[1];
        lastMonth = date.getMonth();
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

}).call();
