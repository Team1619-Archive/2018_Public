import { observer } from 'mobx-react'
import React from 'react'
import { Modal, Form, Switch } from 'antd'

import appState from 'services/appState'

@observer
class EditInputModal extends React.Component {

	render() {
		let title
		let content
		let handler
		if (appState.$editInput === null) {
			title = 'Loading'
			content = null
		}
		else {
			title = appState.$editInput.name

			switch (appState.$editInput.type) {
				case 'boolean':
					handler = this._onBooleanSubmit

					const booleanInput = this.props.form.getFieldDecorator('booleanInput')

					content = (
						<Form onSubmit={this._onBooleanSubmit}>
							<Form.Item label='Value'>
								{booleanInput(
									<Switch />
								)}
							</Form.Item>
						</Form>
					)
					break
				case 'numeric':
					content = <p>Numeric</p>
					break
				case 'vector':
					content = <p>Vector</p>
					break
			}
		}

		return (
			<Modal visible={appState.$isEditingInput}
				title={title}
				onOk={this._onSubmit.bind(null, handler)}
				onCancel={this._onCancel}>{content}</Modal>
		)
	}

	_onSubmit = (handler) => {
		this.props.form.validateFields((error) => {
			if (!error) {
				handler()
			}
		})
	}

	_onBooleanSubmit = (event) => {
		if (event) event.preventDefault()

		const value = this.props.form.getFieldValue('booleanInput')
		appState.editBooleanInput(value)
	}

	_onNumericSubmit = (event) => {
		if (event) event.preventDefault()
	}

	_onVectorSubmit = (event) => {
		if (event) event.preventDefault()
	}

	_onCancel = () => {
		appState.onEditInputCancel()
	}

}

export default Form.create()(EditInputModal)
