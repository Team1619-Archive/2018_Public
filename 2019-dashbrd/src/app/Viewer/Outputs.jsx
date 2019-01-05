import { observer } from 'mobx-react'
import React from 'react'
import { Table } from 'antd'

import appState from 'services/appState'

const COLUMNS = [{
	dataIndex: 'name',
	title: 'Name'
}, {
	dataIndex: 'outputType',
	title: 'Output type'
}, {
	dataIndex: 'output',
	title: 'Output'
}]

@observer
class Outputs extends React.Component {

	render() {
		return (
			<div>
				<h2>Motors</h2>
				<Table dataSource={appState.$motors}
					columns={COLUMNS}
					rowKey={motor => `${motor.name}`}
					pagination={false}
					scroll={{ y: true }} />
			</div>
		)
	}

}

export default Outputs
