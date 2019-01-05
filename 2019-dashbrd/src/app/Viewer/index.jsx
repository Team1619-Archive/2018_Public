import React from 'react'

import Inputs from './Inputs'
import Outputs from './Outputs'
import EditInputModal from './EditInputModal'

import './component.less'

const Viewer = () => (
	<section className='viewer'>
		<Inputs />
		<Outputs />
		<EditInputModal />
	</section>
)

export default Viewer
