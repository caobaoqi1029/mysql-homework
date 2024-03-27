const {defineConfig} = require('@vue/cli-service')
module.exports = defineConfig({
    devServer: {
        host: 'localhost',
        port: '8090',
        open: true
    },
    transpileDependencies: true,
    publicPath: process.env.NODE_ENV === 'production' ? '' : '',
    outputDir: 'dist',
    assetsDir: 'static',
})
