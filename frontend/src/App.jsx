import { Routes, Route } from 'react-router-dom'
import Home  from './pages/home/home.component'
import Navigation from './pages/navigation/navigation.component'
import Authentication from './pages/authentication/authentication.component'
import Cars from './pages/cars/cars.component'
import Unauthorized from './pages/unauthorized/unauthorized.component'
import Rentals from './pages/rentals/rentals.component'

import AuthHandler from './components/auth-handler/auth-handler.component'

function App() {

  return (
    <Routes>
        <Route path='/' element={<Navigation/>}>
            <Route index element={<Home/>}/>
            <Route path='auth' element={<Authentication/>}/>
            <Route path='cars' element={<Cars/>}/>
            <Route element={<AuthHandler allowedRoles={['EMPLOYEE', 'ADMIN']}/>}>
                <Route path='rentals' element={<Rentals/>}/>
            </Route>
            <Route path='unauthorized' element={<Unauthorized/>}/>
        </Route>
    </Routes>
  )
}

export default App