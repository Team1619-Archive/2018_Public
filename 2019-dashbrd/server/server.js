const path = require('path')
const express = require('express')
const serveStatic = require('serve-static')

console.log(`Using ${process.env.NODE_ENV === 'production' ? 'production' : 'development'} build`)

const BUILD_PATH = process.env.NODE_ENV === 'production' ? path.resolve(__dirname, '../prod') : path.resolve(__dirname, '../build')

const app = express()

app.use(serveStatic(BUILD_PATH))

app.get('/*', (_, response) => {
	response.sendFile(path.join(BUILD_PATH, 'index.html'))
})

app.listen(5804, '0.0.0.0', () => {
	console.log('Server listening at http://0.0.0.0:5804')
})
