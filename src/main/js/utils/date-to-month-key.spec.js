import {dateToMonthKey} from "./date-to-month-key";

test('format date', function() {
    const date = new Date(1970, 0, 1, 0, 0, 0, 0);

    expect(dateToMonthKey(date)).toBe('Jan 1970');
});
