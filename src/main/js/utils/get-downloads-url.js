import $ from 'jquery';
import {dateToDayKey} from "./date-to-day-key";

export function getDownloadsUrl(type, names, fromDate, untilDate) {

    var queryString = '';
    $.each(names, function(i, packageName) {
        queryString += type + '=' + encodeURIComponent(packageName) + '&';
    });

    return '/api/download-counts?' + queryString
        + 'from=' + dateToDayKey(fromDate)
        + '&until=' + dateToDayKey(untilDate);
}
