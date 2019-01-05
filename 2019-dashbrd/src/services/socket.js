import { action, observable } from 'mobx'

import logger from './logger'
import config from 'config'
import appState from './appState'

class Socket {
	_socket
	_listeners = new Set()

	@observable $isConnected = false

	connect() {
		logger.info('Socket connecting...')

		// if (appState.$isRobotSocket) {
			this._socket = new WebSocket(config.robotServerUrl)
		// } else {
		// 	this._socket = new WebSocket(config.simServerUrl)
		// }

		this._socket.addEventListener('open', this._onConnect)
		this._socket.addEventListener('close', this._onDisconnect)
		this._socket.addEventListener('message', this._onMessage)
	}

	send(data) {
		this._socket.send(JSON.stringify(data))
	}

	subscribe(listener) {
		this._listeners.add(listener)
	}

	unsubscribe(listener) {
		this._listeners.remove(listener)
	}

	@action
	_onConnect = () => {
		logger.info('Socket connected')

		this.$isConnected = true
	}

	_onDisconnect = () => {
		logger.info('Socket disconnected')

		this.$isConnected = false
	}

	_onMessage = (event) => {
		try {
			const message = JSON.parse(event.data)

			switch (message.type) {
				case 'broadcast':
					this._listeners.forEach(listener => listener(message))
					break
				case 'response':
					break
			}
		} catch (ex) {
			logger.error(ex)
		}
	}
}

export default new Socket()