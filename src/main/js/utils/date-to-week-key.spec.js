import {dateToWeekKey} from "./date-to-week-key";

test('format date', function() {
    const date = new Date(1970, 0, 1, 0, 0, 0, 0);

    expect(dateToWeekKey(date)).toBe('1970-W01');
});
