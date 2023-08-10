import { useState, useEffect } from 'react';
import CarItem from '../../components/car-item/carItem.component';
import SearchForm from '../../components/search-form/search-form.component';

import './home.styles.css'

const Home = () => {
    // const [fromDate, setFromDate] = useState(null);
    // const [toDate, setToDate] = useState(null);
    // const [location, setLocation] = useState('warsaw');
    const [availableCars, setAvailableCars] = useState([]);

    return(
        <div className="home">
            <SearchForm setAvailableCars={setAvailableCars} />
            {availableCars && availableCars.map(car => (
                <CarItem car={car} key={car.id}/>
            ))}
        </div>
    )
}

export default Home;