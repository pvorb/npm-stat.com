import {calculateTotalDownloads} from "./calculate-total-downloads";

import downloadsData from './__mocks__/package-data';

test('calculates total', function() {
    expect(calculateTotalDownloads(downloadsData['package-name'])).toBe(1759);
});
