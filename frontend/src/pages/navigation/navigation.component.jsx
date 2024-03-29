import { Fragment } from 'react'
import './navigation.styles.css'

import { Link, Outlet } from 'react-router-dom'
import { useContext } from 'react'
import UserContext from '../../contexts/user.context'

import SignOutButton from '../../components/sign-out-button/sign-out-button.component'

const Navigation = () => {
    const {currentUser} = useContext(UserContext);

    return(
        <Fragment>
            <nav className="navbar">
                <div className='navbar-up'>
                    {/* <Link to={'/'} className='logo'>
                        <li>carrental</li>
                    </Link> */}
                    <Link to={'/'}>
                        <li>Home</li>
                    </Link>
                    <Link to={'cars'}>
                        <li>Cars</li>
                    </Link>
                    {/* <li>Reviews</li> */}
                    <Link to={'about-us'}>
                        <li>About us</li>
                    </Link>
                    {/* <li>Contact</li> */}

                    {currentUser && (<li>Profile</li>)}

                    {currentUser ? (
                        <Link>
                            <SignOutButton/>
                        </Link>) 
                        : (
                        <Link to={'/auth'}>
                            <li>Sign in</li>
                        </Link>
                    )}
                    
                </div>

                {['EMPLOYEE', 'ADMIN'].includes(currentUser?.roles) &&
                <div className='navbar-down'>
                    <li>Customers</li>
                    <Link to={'/rentals'}>
                        <li>Rentals</li>
                    </Link>
                    <li>Reports</li>
                </div>}

            </nav>
            <Outlet/>
        </Fragment>
    )
}

export default Navigation