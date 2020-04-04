import $ from 'jquery';
import moment from "moment";

import {dateToDayKey} from "./date-to-day-key";

export function getDailyDownloadData(downloadData, dateRange) {

    var dailyData = {};

    $.each(downloadData, function(packageName, data) {
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
