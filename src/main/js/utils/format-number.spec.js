import {formatNumber} from "./format-number";

test('format number', function() {
    expect(formatNumber(1)).toBe('1');
    expect(formatNumber(10)).toBe('10');
    expect(formatNumber(100)).toBe('100');
    expect(formatNumber(1000)).toBe('1,000');
    expect(formatNumber(10000)).toBe('10,000');
    expect(formatNumber(100000)).toBe('100,000');
    expect(formatNumber(1000000)).toBe('1,000,000');
});
