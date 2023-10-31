import { createContext, useState } from "react";

const UserContext = createContext({
    setUser: () => {},
    currentUser: {}
});
export default UserContext;

export const UserProvider = ({children}) => {
    const [currentUser, setUser] = useState(null);

    return (
        <UserContext.Provider value={{currentUser, setUser}}>
            {children}
        </UserContext.Provider>
    )
};