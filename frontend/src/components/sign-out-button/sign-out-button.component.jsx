import { useContext } from "react"
import { useNavigate, useLocation } from "react-router-dom";

import UserContext from "../../contexts/user.context"

const SignOutButton = () => {
    const {currentUser, setUser} = useContext(UserContext);
    const navigate = useNavigate();
    const location = useLocation();

    const handleSignOut = async () => {
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/logout`, {
                method: "POST",
                headers: {
                    "Content-Type": "application/json",
                    Authorization: `Bearer ${currentUser.token}`
                    }
                });
            setUser(null);
            navigate("/auth", {state: {from: location}, replace: true});
            } catch(error) {
                console.log(error);
            }
    } 

    return(
        <li onClick={handleSignOut}>Sign Out</li>
    )
}

export default SignOutButton;