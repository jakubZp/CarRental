import { useContext, useState, useEffect } from "react";
import UserContext from "../../contexts/user.context";

import "./sign-in-form.styles.css";
import { useLocation } from "react-router-dom";

const SignIn = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const {setUser} = useContext(UserContext);
    const [error, setError] = useState(false);
    const location = useLocation();
    const from = location.state?.from?.pathname || "/";

    // useEffect(() => {
    //     const storedUser = sessionStorage.getItem("user");
    //     console.log(storedUser);
    //     if(storedUser) {
    //         const user = JSON.parse(storedUser);
    //         console.log(user);
    //         setUser(user);
    //     }
    // }, [])

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/login`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    email: email,
                    password: password
                }),
            })

            if(response.ok) {
                const data = await response.json();
                const roles = data?.role;
                const token = data?.token;
                // sessionStorage.setItem(
                //     "user",
                //     JSON.stringify({ roles, token })
                // );
                setUser({roles, token});
            }
            else if(response.status === 403) {
                setError(true);
            }
            else {
                setError(true);
                alert("Cannot log in.")
            }
            
        } catch (error){
            if(!error?.response) {
                console.log("no server response");
            } else {
                console.log("login failed");
            }
    }

    setEmail('');
    setPassword('');
    };

    return(
    <div className="login-form-container">
        <h1>Sign in</h1>
        <form onSubmit={handleSubmit}>
            <div className="form-group">
                <label htmlFor="email">Email</label>
                    <input
                        type="text"
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="form-input"
                        required
                    />
            </div>
            <div className="form-group">
                <label htmlFor="password">Password</label>
                <input
                    type="password"
                    id="password"
                    value={password}
                    onChange={(e) => setPassword(e.target.value)}
                    className="form-input"
                    required
                />
                {error && <span className="incorrect-password">Incorrect password</span>}
            </div>
        <button type="submit" className="btn btn-login">Sign in</button>
        </form>
        <div className="buttons-container">
            <button className="btn btn-forgot-password">Forgot password?</button>
            <button className="btn btn-sign-up-redirect">Don't have account? Sign up</button>
        </div>
    </div>
    );
};

export default SignIn;