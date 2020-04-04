import $ from 'jquery';
import {dateToDayKey} from "./date-to-day-key";

export function getDataGroupedPerPeriod(downloadData, dateRange, dateToPeriod, nthVisibleAxisLabel) {

    var groupedData = {};

    var xAxisLabels = null;
    var coveredPeriods = {};

    $.each(downloadData, function(packageName, data) {
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
