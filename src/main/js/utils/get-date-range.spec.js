import {getDateRange} from "./get-date-range";

test('returns `Date` array', function() {
    const start = new Date(1970, 0, 1, 0, 0, 0, 0);
    const end = new Date(1970, 0, 3, 0, 0, 0, 0);
    const range = getDateRange(start, end);

    expect(range).toHaveLength(3);
    expect(range[0]).toEqual(start);
    expect(range[2]).toEqual(end);
});
