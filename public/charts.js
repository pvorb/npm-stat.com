/**!
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

    var days, weeks, months;
    var pkg = getURLParam('package');

    if (pkg === null)
      pkg = 'npm';

    $('h2').html('Download statistics for <input type="search" '
      + 'name="package" size="18" value="'+pkg
      + '"> <input type="submit" value="Show charts">');

    url = 'http://isaacs.iriscouch.com/downloads/_design/app/_view/pkg?'
      + 'group_level=3&start_key=["'+pkg+'"]&end_key=["'+pkg+'",{}]';

    $.ajax({
      url: url,
      dataType: 'jsonp',
      success: function (json) {
        var dailyData = getDailyData(json);
        chart('days', 'Downloads per day', dailyData);
        var weeklyData = getWeeklyData(dailyData);
        chart('weeks', 'Downloads per week', weeklyData);
        var monthlyData = getMonthlyData(dailyData);
        chart('months', 'Downloads per month', monthlyData);
      },
      error: function () {
        alert('Could not receive statistical data.');
      }
    });
  });

  function chart(id, title, data) {
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
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 60 * 60 * 1000,
        lineColor: '#000000',
        title: {
          text: 'Date',
          style: {
            color: '#000000'
          }
        }
      },
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
      } else weekTotal += record[1];
    }

    return result;
  }

  function getMonthlyData(dailyData) {
    var result = [];

    var lastMonth = new Date(dailyData[0][0]).getMonth();
    var monthTotal = 0;
    var record, date;

    for (var i in dailyData) {
      record = dailyData[i];
      date = new Date(record[0]);
      if (lastMonth != date.getMonth()) {
        result.push({ x: new Date(date.getFullYear(), date.getMonth(), 1).getTime(),
            y: monthTotal });
        monthTotal = record[1];
        lastMonth = date.getMonth();
      } else monthTotal += record[1];
    }

    return result;
  }

}).call();
