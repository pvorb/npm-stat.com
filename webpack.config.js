var webpack = require('webpack');

module.exports = {
    entry: {
      charts: "./src/charts.js"
    },
    output: {
        path: __dirname + '/public',
        filename: "[name].js"
    },
    plugins: [
        // ignore all locales coming with moment.js
        new webpack.ContextReplacementPlugin(/moment[\\\/]locale$/, /^$/)
    ],
    devtool: 'source-map',
    devServer: {
        contentBase: 'public/',
        proxy: {
            '/-/_view/**': {
                target: 'https://npm-stat.com/',
                secure: false,
                changeOrigin: true
            },
            '/downloads/**': {
                target: 'https://npm-stat.com/',
                secure: false,
                changeOrigin: true
            }
        }
    }
};
