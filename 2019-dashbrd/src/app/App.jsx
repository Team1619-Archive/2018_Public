import { observer } from 'mobx-react'
import React from 'react'
import { Tabs, Layout, Switch } from 'antd'

import appState from 'services/appState'

import Dashboard from './Dashboard'
import Viewer from './Viewer'
import Config from './Config'
import Logger from './Logger'
import ConnectionStatus from '../components/ConnectionStatus';

@observer
class App extends React.Component {
	render() {
		return (
			<section id='app'>
				<Layout>
					<Layout.Content>
						<Tabs activeKey={`${appState.$page}`} onChange={this._onTabChange}>
							<Tabs.TabPane key='1' tab='Dashboard'>
								<Dashboard />
							</Tabs.TabPane>
							<Tabs.TabPane key='2' tab='Viewer'>
								<Viewer />
							</Tabs.TabPane>
							<Tabs.TabPane key='3' tab='Config'>
								<Config />
							</Tabs.TabPane>
							<Tabs.TabPane key='4' tab='Logger'>
								<Logger />
							</Tabs.TabPane>
						</Tabs>
					</Layout.Content>
					<Layout.Footer>
						<ConnectionStatus isConnected={appState.$loaded}/>
						<Switch onChange={this._onSwitchChange} />
					</Layout.Footer>
				</Layout>
			</section>
		)
	}

	_onTabChange = (key) => {
		appState.setPage(parseInt(key))
	}

	_onSwitchChange = () => {
		appState.toggleRobotSocket
	}
}

export default App
