import moment from "moment";

export function dateToYearKey(date) {
    return moment(date).format('YYYY');
}
