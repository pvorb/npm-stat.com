import moment from "moment";

export function dateToDayKey(date) {
    return moment(date).format('YYYY-MM-DD');
}
