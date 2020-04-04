import moment from "moment";

export function dateToMonthKey(date) {
    return moment(date).format('MMM YYYY');
}
