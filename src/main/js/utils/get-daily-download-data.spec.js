import {getDailyDownloadData} from "./get-daily-download-data";
import {getDateRange} from "./get-date-range";

import downloadData from './__mocks__/package-data';

test('combine daily data', function() {
    const dateRange = getDateRange(
        new Date('2020-01-01'),
        new Date('2020-01-01')
    );
    const data = getDailyDownloadData(downloadData, dateRange);

    expect(data).toEqual({
        'package-name': [
            [
                1577826000000,
                1
            ]
        ]
    });
});
