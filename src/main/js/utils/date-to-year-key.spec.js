import {dateToYearKey} from "./date-to-year-key";

test('format date', function() {
    const date = new Date(1970, 0, 1, 0, 0, 0, 0);

    expect(dateToYearKey(date)).toBe('1970');
});
