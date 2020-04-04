import {dateToDayKey} from "./date-to-day-key";

test('format date', function() {
    const date = new Date(1970, 0, 1, 0, 0, 0, 0);

    expect(dateToDayKey(date)).toBe('1970-01-01');
});
