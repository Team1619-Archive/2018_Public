const path = require('path')

module.exports = {
    context: path.resolve('./src'),
    entry: {
        app: '.'
    },
    output: {
        path: path.resolve('./build'),
        filename: '[name].js'
    },
    module: {
        rules: [
            {
                test: /\.tsx?$/,
                loader: 'ts-loader'
            }
        ]
    },
    resolve: {
        extensions: ['.js', '.ts', '.tsx'],
        modules: [
            path.resolve('/src'),
            'node_modules'
        ]
    },
    target: 'web',
    devtool: 'eval'
}