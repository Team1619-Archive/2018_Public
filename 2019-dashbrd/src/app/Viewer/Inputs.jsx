import { observer } from 'mobx-react'
import React from 'react'
import { Table } from 'antd'

import appState from 'services/appState'

const COLUMNS = [{
	dataIndex: 'name',
	title: 'Name'
}, {
	dataIndex: 'type',
	title: 'Type'
}, {
	dataIndex: 'value',
	title: 'Value',
	render: (value, input) => {
		switch (input.type) {
			case 'boolean':
				return input.value ? 'true' : 'false'
			case 'numeric':
				return value.toString()
			case 'vector':
				return `(${value.map(component => component.toString()).join(', ')})`
		}
	}
}]

@observer
class Inputs extends React.Component {

	render() {
		return (
			<div>
				<h2>Inputs</h2>
				<Table dataSource={appState.$inputs}
					columns={COLUMNS}
					rowKey={input => `${input.name}-${input.type}`}
					pagination={false}
					scroll={{ y: true }}
					onRow={this._onRow} />
			</div>
		)
	}

	_onRow = (input) => {
		return { onClick: this._onRowClick.bind(null, input) }
	}

	_onRowClick = (input) => {
		appState.onEditInput(input)
	}

}

export default Inputs
