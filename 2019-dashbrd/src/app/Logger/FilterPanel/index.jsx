import React from 'react'
import { Table, Tag, Drawer } from 'antd'

import './component.less'

class FilterPanel extends React.Component {
    columns = [
        {
            title: '',
            dataindex: 'color',
            width: 10,
            render: object =>
                (
                    <span><div onClick={this._toggleFilter} className={`color-identifier ${object.active ? 'active' : 'inactive'}`} style={{ 'backgroundColor': object.color }}></div></span>
                ),
            // onCell: record => console.log(record)
        }, {
            title: 'Name',
            dataIndex: 'name',
            key: 'name'
        }, {
            title: 'Tags',
            dataIndex: 'tags',
            key: 'tags',
            render: tags => (<span>{tags.map(tag => <Tag color='blue' key={tag}>{tag}</Tag>)}</span>)
        }
    ]

    rowSelection = {
        onChange: (selectedRowKeys, selectedRows) => {
            console.log(`selectedRowKeys: ${selectedRowKeys}`, 'selectedRows: ', selectedRows);
        },
        onSelect: (record, selected, selectedRows) => {
            console.log(record, selected, selectedRows);
        },
        onSelectAll: (selected, selectedRows, changeRows) => {
            console.log(selected, selectedRows, changeRows);
        },
    };

    constructor(props) {
        super(props)

        this.state = {
            visible: false,
            filters: [{ color: 'red', name: 'randomfilter', tags: ['yo', 'trace', 'untidy', 'active'], active: true }]
        }
    }

    render() {
        return (
            <div>
                <Table dataSource={this.state.filters} rowKey={(row) => row.name} columns={this.columns} title={() => 'Filters'} rowSelection={this.rowSelection} pagination={false}></Table>
                <Drawer className='filter-add-panel' closable destroyOnClose title='Add Filter' visible={this.state.visible} width={256} placement='right' onClose={this._onClose}></Drawer>
            </div>
        )
    }

    _toggleFilter = (event) => {
        this.setState({})
    }

    _onClose = () => {
        this.setState({ visible: false })
    }
}

export default FilterPanel
