import { configure, } from 'mobx'
import { render } from 'react-dom'

import router from './router'

configure({ enforceActions: true })

render(
    router,
    document.getElementById('root')
)

const socket: WebSocket = new WebSocket('ws://localhost:7777')

socket.addEventListener('open', () => {
    socket.send('{ "command": "IMPORT_PATH", "path": "SamplePath.json" }')
})
