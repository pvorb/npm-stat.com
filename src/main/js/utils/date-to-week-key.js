import moment from "moment";

export function dateToWeekKey(date) {
    return moment(date).format('GGGG-[W]WW');
}
