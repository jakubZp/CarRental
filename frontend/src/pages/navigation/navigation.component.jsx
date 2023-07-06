import { Fragment } from 'react'
import './navigation.styles.css'

import { Link, Outlet } from 'react-router-dom'

const Navigation = () => {
    return(
        <Fragment>
            <nav className="navbar">
                <Link to={'/'}>
                    <li>Home</li>
                </Link>
                <Link to={'cars'}>
                    <li>Cars</li>
                </Link>
                <li>Reviews</li>
                <li>About us</li>
                <li>Contact</li>
                <Link to={'/auth'}>
                    <li>Sign in</li>
                </Link>
            </nav>
            <Outlet/>
        </Fragment>
    )
}

export default Navigation