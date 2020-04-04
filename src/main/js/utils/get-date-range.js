import moment from "moment";

/**
 * @param {Date} startDate
 * @param {Date} stopDate
 * @returns {Date[]}
 */
export function getDateRange(startDate, stopDate) {
    var dateArray = [];
    var current = moment(startDate).startOf('day');
    var stop = moment(stopDate).startOf('day');
    while (current.isSameOrBefore(stop)) {
        dateArray.push(current.toDate());
        current = current.add(1, 'days');
    }
    return dateArray;
}
