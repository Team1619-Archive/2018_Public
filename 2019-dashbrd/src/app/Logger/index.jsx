import React from 'react'
import { Col, Row } from 'antd'
import Directory from './Directory'
import FilterPanel from './FilterPanel'

class Logger extends React.Component {

    render() {
        return (
            <Row>
                <Col span={6}><Directory path='assets/logs'/></Col>
                <Col span={12} offset={6}><FilterPanel /></Col>
            </Row>
        )
    }

}

export default Logger
