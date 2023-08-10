import { createContext, useState } from "react";

export const OrderContext = createContext({
    location: {},
    setLocation: () => {},
    fromDate: '',
    setFromDate: () => {},
    toDate: '',
    setToDate: () => {},
    car: {},
    setCar: () => {}
});

export const OrderProvider = ({children}) => {
    const [location, setLocation] = useState('warsaw');
    const [fromDate, setFromDate] = useState('');
    const [toDate, setToDate] = useState('');
    const [car, setCar] = useState({});

    const value = {location, setLocation, fromDate, setFromDate, toDate, setToDate, car, setCar};

    return(
        <OrderContext.Provider value={value}>
            {children}
        </OrderContext.Provider>
    )
}