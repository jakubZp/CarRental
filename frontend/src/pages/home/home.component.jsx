import { useState } from 'react';
import CarItem from '../../components/car-item/carItem.component';

import './home.styles.css'

const Home = () => {
    const [fromDate, setFromDate] = useState(null);
    const [toDate, setToDate] = useState(null);
    const [location, setLocation] = useState('warsaw');
    const [availableCars, setAvailableCars] = useState([]);

    const fetchAvailableCars = async (from, to, page, pageSize) => {
        try {
            const url = `http://localhost:8080/api/v1/cars/${from}/${to}?page=${page}&pageSize=${pageSize}`;
            const response = await fetch(url);
            const dataJson = await response.json();
            console.log(dataJson);
            setAvailableCars(dataJson);
        }
        catch(error) {
            console.log("Error while fetching data from: " + url + "\n error: " + error);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        await fetchAvailableCars(fromDate, toDate, 0, 10);
        console.log(availableCars);
    }

    return(
        <div className="home">
            <form className='home-form' onSubmit={handleSubmit}>
                <div className="group">
                    <label htmlFor='locations-select'>Location</label>
                    <select 
                        className='location-select' 
                        name="locations" 
                        value={location}
                        onChange={(e) => setLocation(e.target.value)} 
                        required>
                            <option value="warsaw">Warsaw</option>
                    </select>
                </div>

                <div className="group">
                    <label htmlFor="from-date">From date</label>
                    <input
                        className="date-input" 
                        type="datetime-local" 
                        min={"2020-01-01"}
                        max={"2030-12-30"}
                        // step={1800}
                        onChange={(e) => setFromDate(e.target.value)}
                        required />
                </div>

                <div className="group">
                    <label htmlFor="to-date">To date</label>
                    <input
                        className="date-input"
                        type="datetime-local" 
                        min={"2020-01-01"}
                        max={"2030-12-30"}
                        // step={1800}
                        onChange={(e) => setToDate(e.target.value)}
                        required />
                </div>

                <button type="submit" className='search'>Search</button>
            </form>
            {availableCars && availableCars.map(car => (
                <CarItem car={car} key={car.id}/>
            ))}
        </div>
    )
}

export default Home;