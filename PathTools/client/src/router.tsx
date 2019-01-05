import * as React from 'react'
import { BrowserRouter, Route, Switch } from 'react-router-dom'

import PathExplorer from './PathExplorer'
import Path from './Path'
import NotFound from './NotFound'

const router: JSX.Element = (
    <BrowserRouter>
        <div>
            <Switch>
                <Route exact path='/' component={PathExplorer} />
                <Route path='/path/:id' component={Path} />
                <Route component={NotFound} />
            </Switch>
        </div>
    </BrowserRouter>
)

export default router
