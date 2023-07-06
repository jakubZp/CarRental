import { Routes, Route } from 'react-router-dom'
import Home  from './pages/home/home.component'
import Navigation from './pages/navigation/navigation.component'
import Authentication from './pages/authentication/authentication.component'
import Cars from './pages/cars/cars.component'

function App() {

  return (
    <Routes>
      <Route path='/' element={<Navigation/>}>
        <Route index element={<Home/>}/>
        <Route path='auth' element={<Authentication/>}/>
        <Route path='cars' element={<Cars/>}/>
      </Route>
    </Routes>
  )
}

export default App