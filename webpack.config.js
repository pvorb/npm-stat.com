module.exports = {
    entry: {
      charts: "./src/charts.js"
    },
    output: {
        path: __dirname + '/public',
        filename: "[name].js"
    },
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
