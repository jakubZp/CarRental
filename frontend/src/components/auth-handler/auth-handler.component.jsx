import { useContext } from "react"
import UserContext from "../../contexts/user.context"
import { useLocation, Navigate, Outlet } from "react-router-dom";

const AuthHandler = ({allowedRoles}) => {
    const {currentUser} = useContext(UserContext);
    const location = useLocation();

    return(
        // user?.roles?.find(role => allowedRoles?.includes(role))
        allowedRoles?.includes(currentUser?.roles)
        ? <Outlet/> 
        : currentUser 
        ? <Navigate to="/unauthorized" state={{from: location}} replace />
        : <Navigate to="/auth" state={{from: location}} replace />
    );
}

export default AuthHandler;