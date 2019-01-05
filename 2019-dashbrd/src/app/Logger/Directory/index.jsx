import React from 'react'
import { Tree } from 'antd'
import * as fs from 'fs'

class Directory extends React.Component {
    constructor(props) {
        super(props)

        this.state = {
            treeData: []
        }
    }
    
    componentDidMount() {
        if (!(window.File && window.FileReader && window.FileList && window.Blob)) {
            alert('The file APIs are not fully supported in this browser')
        }
    }

    render() {
        return (
            <div>
                {this.state.treeData.length ?
                    <Tree.DirectoryTree onSelect={this._onSelect} autoExpandParent>{this.state.treeData.map(data => <Tree.TreeNode title={data.title} key={data.title} isLeaf='true' />)}</Tree.DirectoryTree> :
                    'loading files'}
            </div>
        )
    }

    _onSelect = (keys, e) => {
        if (e.node.isLeaf) {
            console.log("yu pass")
        }
    }
}

export default Directory
