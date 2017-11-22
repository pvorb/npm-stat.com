var webpack = require('webpack');
var path = require('path');

module.exports = {
    entry: {
        charts: path.resolve(__dirname, 'src/main/js/charts.js')
    },
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/'),
        filename: "[name].js"
    },
    plugins: [
        // ignore all locales coming with moment.js
        new webpack.ContextReplacementPlugin(/moment[\\\/]locale$/, /^$/)
    ],
    devtool: 'source-map',
    devServer: {
        contentBase: path.resolve(__dirname, 'src/main/resources/static/'),
        proxy: {
            '/api/**': {
                target: 'http://localhost:8080/',
                secure: false,
                changeOrigin: true
            }
        },
        port: 8081
    }
};
