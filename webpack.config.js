var webpack = require('webpack');
var HTMLWebpackPlugin = require('html-webpack-plugin');
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
        new webpack.ContextReplacementPlugin(/moment[\\\/]locale$/, /^$/),
        new HTMLWebpackPlugin({
            template: 'src/main/html/index.ejs',
            filename: 'index.html'
        }),
        new HTMLWebpackPlugin({
            template: 'src/main/html/charts.ejs',
            filename: 'charts.html'
        })
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
