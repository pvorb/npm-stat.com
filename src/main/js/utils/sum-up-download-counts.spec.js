import {sumUpDownloadCounts} from "./sum-up-download-counts";

const authorData = {
    'package-1': {
        "2020-01-01": 1
    },
    'package-2': {
        "2020-01-01": 2
    },
    'package-3': {
        "2020-01-01": 3
    }
};

test('return sum', function() {
    expect(sumUpDownloadCounts(authorData)).toEqual({
        total: {
            "2020-01-01": 6
        }
    });
});
