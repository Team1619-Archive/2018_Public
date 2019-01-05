import React from 'react'
import { observer } from 'mobx-react';

import appState from 'services/appState'
import Loading from 'components/Loading'

@observer
class Config extends React.Component {

	render() {
		if (!appState.$loaded) {
			return <Loading />
		}

		return <h1>Config</h1>
	}

}

export default Config
