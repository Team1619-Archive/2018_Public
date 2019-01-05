import React from 'react'

import './component.less'

class ConnectionStatus extends React.Component {
    render() {
        const indicatorColor = this.props.isConnected ? 'connected' : 'disconnected'

        return (
            <div className='connection-status'>
                <h4>Connection status<div className={`indicator ${indicatorColor}`}/></h4>
            </div>
        )
    }
}

export default ConnectionStatus
