import {getDownloadsUrl} from "./get-downloads-url";

test('return url for a single type', function() {
    const url = getDownloadsUrl(
        'foo',
        ['bar'],
        new Date('2020-04-01'),
        new Date('2020-04-04')
    );

    expect(url).toBe(
        '/api/download-counts?foo=bar&from=2020-04-01&until=2020-04-04'
    );
});

test('return url for a multiple types', function() {
    const url = getDownloadsUrl(
        'foo',
        ['bar', 'qux'],
        new Date('2020-04-01'),
        new Date('2020-04-04')
    );

    expect(url).toBe(
        '/api/download-counts?foo=bar&foo=qux&from=2020-04-01&until=2020-04-04'
    );
});
