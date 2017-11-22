module.exports = function escapeHtml(html) {
    if (!html) {
        return null;
    }

    return html.replace(/&/g, '&amp;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#39;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;');
};
