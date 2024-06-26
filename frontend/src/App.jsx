import { Routes, Route } from 'react-router-dom'
import Home  from './pages/home/home.component'
import Navigation from './pages/navigation/navigation.component'
import Cars from './pages/cars/cars.component'
import AboutUs from './pages/about-us/about-us.component'
import Authentication from './pages/authentication/authentication.component'
import Unauthorized from './pages/unauthorized/unauthorized.component'
import Rentals from './pages/rentals/rentals.component'
import Order from './pages/order/order.component'

import AuthHandler from './components/auth-handler/auth-handler.component'
import Register from './pages/register/register.component'

function App() {

  return (
    <Routes>
        <Route path='/' element={<Navigation/>}>
            <Route index element={<Home/>}/>
            <Route path='cars' element={<Cars/>}/>
            <Route path='about-us' element={<AboutUs/>}/>
            <Route path='auth' element={<Authentication/>}/>
            <Route path='register' element={<Register/>}/>
            <Route element={<AuthHandler allowedRoles={['EMPLOYEE', 'ADMIN']}/>}>
                <Route path='rentals' element={<Rentals/>}/>
            </Route>
            <Route element={<AuthHandler allowedRoles={['CUSTOMER', 'EMPLOYEE', 'ADMIN']}/>}>
                <Route path='order' element={<Order/>}/>
            </Route>
            <Route path='unauthorized' element={<Unauthorized/>}/>
        </Route>
    </Routes>
  )
}

export default App