import $ from 'jquery';

export function calculateTotalDownloads(downloadsPerDay) {
    var totalDownloads = 0;
    $.each(downloadsPerDay, function(date, downloadsOfDay) {
        totalDownloads += downloadsOfDay;
    });
    return totalDownloads;
}
