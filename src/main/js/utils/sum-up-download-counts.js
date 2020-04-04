import $ from 'jquery';

export function sumUpDownloadCounts(downloadData) {
    var summedUpDownloads = {};
    $.each(downloadData, function(packageName, packageDownloads) {
        $.each(packageDownloads, function(date, packageDownloadsForDate) {
            if (typeof summedUpDownloads[date] !== 'undefined') {
                summedUpDownloads[date] = summedUpDownloads[date] + packageDownloadsForDate;
            } else {
                summedUpDownloads[date] = packageDownloadsForDate;
            }
        })
    });

    return {total: summedUpDownloads};
}
