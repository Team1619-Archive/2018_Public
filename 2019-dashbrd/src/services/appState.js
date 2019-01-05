import { observable, computed, action } from 'mobx'

import socket from './socket'

const Page = {
	DASHBOARD: 1,
	VIEWER: 2,
	CONFIG: 3,
	LOGGER: 4
}

class AppState {
	@observable $page = Page.DASHBOARD
	// @observable $isRobotSocket = true
	@observable.ref $inputs = []
	@observable.ref $motors = []
	@observable $isEditingInput = false
	@observable.ref $editInput = null

	@computed get $loaded() {
		return socket.$isConnected
	}

	constructor() {
		socket.connect()

		socket.subscribe(this._onBroadcast)
	}

	@action
	setPage(page) {
		this.$page = page
	}

	@action
	toggleRobotSocket() {
		// this.$isRobotSocket = !$isRobotSocket
		socket.connect()
	}

	@action
	onEditInput(input) {
		this.$isEditingInput = true
		this.$editInput = input
	}

	@action
	onEditInputCancel() {
		this.$isEditingInput = false
	}

	@action
	editBooleanInput(value) {
		socket.send({
			type: 'boolean',
			name: this.$editInput.name,
			value
		})

		this.$isEditingInput = false
	}

	@action
	_onBroadcast = (data) => {
		this.$inputs = data.inputs
		this.$motors = data.motors
	}

}

const appState = new AppState()

export { Page, appState as default }
