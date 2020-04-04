import {getDataGroupedPerPeriod} from "./get-data-grouped-per-period";
import {dateToWeekKey} from "./date-to-week-key";

import downloadData from './__mocks__/package-data';
import {getDateRange} from "./get-date-range";

test('group data by week', function() {
    const dateRange = getDateRange(
        new Date('2020-01-01'),
        new Date('2020-04-03')
    );
    const data = getDataGroupedPerPeriod(
        downloadData,
        dateRange,
        dateToWeekKey,
        4
    );

    expect(data).toEqual({
        data: {
            'package-name': [
                {
                    name: "2020-W01",
                    y: 20
                },
                {
                    name: "2020-W02",
                    y: 108
                },
                {
                    "name": "2020-W03",
                    "y": 112,
                },
                {
                    "name": "2020-W04",
                    "y": 129
                },
                {
                    "name": "2020-W05",
                    "y": 92
                },
                {
                    "name": "2020-W06",
                    "y": 104
                },
                {
                    "name": "2020-W07",
                    "y": 142
                },
                {
                    "name": "2020-W08",
                    "y": 96
                },
                {
                    "name": "2020-W09",
                    "y": 162
                },

                {
                    "name": "2020-W10",
                    "y": 113,
                },
                {
                    "name": "2020-W11",
                    "y": 146,
                },
                {
                    "name": "2020-W12",
                    "y": 223
                },
                {
                    "name": "2020-W13",
                    "y": 158
                },
                {
                    "name": "2020-W14",
                    "y": 154
                }
            ]
        },
        xAxisLabels: [
            "2020-W01",
            " ",
            " ",
            " ",
            "2020-W05",
            " ",
            " ",
            " ",
            "2020-W09",
            " ",
            " ",
            " ",
            "2020-W13",
            " "
        ]
    });
});
