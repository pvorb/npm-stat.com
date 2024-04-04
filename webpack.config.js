const path = require('path');

module.exports = {
    mode: 'development',
    entry: {
        charts: path.resolve(__dirname, 'src/main/js/charts.js')
    },
    output: {
        path: path.resolve(__dirname, 'src/main/resources/static/'),
        filename: "[name].js"
    },
    devtool: 'source-map',
    devServer: {
        static: {
            directory: path.resolve(__dirname, 'src/main/resources/static/')
        },
        port: 8081
    },
    resolve: {
        fallback: {
          "querystring": false
        }
      }      
};
