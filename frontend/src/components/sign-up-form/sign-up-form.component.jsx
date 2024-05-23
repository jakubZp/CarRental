import { useContext, useState } from "react";

import "./sign-up-form.styles.css";
import { useNavigate } from "react-router-dom";
import UserContext from "../../contexts/user.context";

const SignUpForm = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [phoneNumber, setPhoneNumber] = useState('');
    const [address, setAddress] = useState('');
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState("");
    const [confirmPassword, setConfirmPassword] = useState("");

    const [error, setError] = useState(false);
    const {setUser} = useContext(UserContext)
    const navigate = useNavigate();

    const handleSubmit = async (e) => {
        e.preventDefault();
        try {
            const response = await fetch(`http://localhost:8080/api/v1/auth/register`, {
                method: "POST",
                headers: {"Content-Type": "application/json"},
                body: JSON.stringify({
                    firstName: firstName,
                    lastName: lastName,
                    phoneNumber: phoneNumber,
                    address: address,
                    email: email,
                    password: password,
                    role: "CUSTOMER" //TODO
                }),
            })

            if(response.ok) {
                const data = await response.json();
                const roles = data?.role;
                const token = data?.token;
                setUser({roles, token});
                navigate('/cars');
            }

        } catch(error) {

        }
    }

    const validatePassword = (e) => {
        e.preventDefault();
        if(confirmPassword === password) {
            setError(false);
        }
        else {
            setError(true);
        }
        console.log(error);
    }

    return (
        <div className="register-form-container">
            <h1>Sign up</h1>
            <form onSubmit={handleSubmit}>
                <div className="form-group">
                    <label htmlFor="firstName">First name</label>
                    <input 
                        type="text" 
                        id="firstName"
                        value={firstName}
                        onChange={(e) => setFirstName(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="lastName">Last name</label>
                    <input 
                        type="text" 
                        id="lastName"
                        value={lastName}
                        onChange={(e) => setLastName(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="email">Email</label>
                    <input 
                        type="email" 
                        id="email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="passsword">Password</label>
                    <input 
                        type="password" 
                        id="password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="confirmPasssword">Confirm password</label>
                    <input 
                        type="password" 
                        id="confirmPassword"
                        value={confirmPassword}
                        onChange={(e) => setConfirmPassword(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                {error && <span>Passwords are not the same</span>}
                <div className="form-group">
                    <label htmlFor="phoneNumber">Phone number</label>
                    <input 
                        type="text" 
                        id="phoneNumber"
                        value={phoneNumber}
                        onChange={(e) => setPhoneNumber(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>
                <div className="form-group">
                    <label htmlFor="address">Address</label>
                    <input 
                        type="text" 
                        id="address"
                        value={address}
                        onChange={(e) => setAddress(e.target.value)}
                        className="form-input"
                        required
                    />
                </div>

                <button type="submit" className="btn btn-register">Sign up</button>

            </form>
        </div>
    )
}

export default SignUpForm;