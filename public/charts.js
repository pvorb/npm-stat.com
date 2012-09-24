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

    $('h2').html('Download statistics for package <input type="text" '
      + 'name="package" value="'+pkg+'"> <input type="submit" value="Show charts">');

    $.getJSON('/dev/npm-stat/'+pkg+'.json', function success(json) {
      var dailyData = getDailyData(json);
      chart('days', 'Downloads per day', dailyData);
      var weeklyData = getWeeklyData(dailyData);
      chart('weeks', 'Downloads per week', weeklyData);
      var monthlyData = getMonthlyData(dailyData);
      chart('months', 'Downloads per month', monthlyData);
    }).error(function () {
      alert('Could not receive statistical data.');
    });
  });

  function chart(id, title, data) {
    return new Highcharts.Chart({
      chart: {
        renderTo: id,
        zoomType: 'x',
      },
      title: {
        text: title
      },
      subtitle: {
        text: typeof document.ontouchstart == 'undefined' ?
          'Click and drag in the plot to zoom in' :
          'Drag your finger over the plot to zoom in'
      },
      xAxis: {
        type: 'datetime',
        maxZoom: 14 * 24 * 60 * 60 * 1000,
        title: {
          text: 'Date'
        }
      },
      yAxis: {
        title: {
          text: 'Downloads'
        },
        min: .5,
        startOnTick: false,
        showFirstLabel: false
      },
      tooltip: {
        shared: true
      },
      legend: {
        enabled: false
      },
      plotOptions: {
        area: {
          fillColor: {
            linearGradient: { x1: 0, y1: 0, x2: 0, y2: 1},
            stops: [
              [0, '#800'],
              [1, 'rgba(255, 0, 0, 0)']
            ]
          },
          lineWidth: 1,
          marker: {
            enabled: false,
            states: {
              hover: {
                enabled: true,
                radius: 5
              }
            }
          },
          shadow: false,
          states: {
            hover: {
              lineWidth: 1
            }
          }
        }
      },
      series: [{
        type: 'area',
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
        result.push([ date.getTime() - threeDays, weekTotal ]);
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
        result.push({ x: new Date(date.getFullYear(), date.getMonth(), 15).getTime(),
            y: monthTotal });
        monthTotal = record[1];
        lastMonth = date.getMonth();
      } else monthTotal += record[1];
    }

    return result;
  }

}).call();
