import React from 'react'
import ReactDOM from 'react-dom/client'
import App from './App.jsx'

import {BrowserRouter} from 'react-router-dom'
import { UserProvider } from './contexts/user.context.jsx'
import { OrderProvider } from './contexts/order.context.jsx'

import './index.css'

ReactDOM.createRoot(document.getElementById('root')).render(
    <React.StrictMode>
        <BrowserRouter>
            <UserProvider>
                <OrderProvider>
                    <App />
                </OrderProvider>
            </UserProvider>
        </BrowserRouter>
    </React.StrictMode>,
)
