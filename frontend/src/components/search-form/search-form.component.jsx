import { useContext, useState } from "react";

import { OrderContext } from "../../contexts/order.context";

import Button from "../button/button.component"

import './search-form.styles.css';

const SearchForm = ({setAvailableCars}) => {
    const {fromDate, setFromDate, toDate, setToDate, location, setLocation} = useContext(OrderContext);

    const fetchAvailableCars = async (from, to, page, pageSize) => {
        try {
            if(from < to) {
                const url = `http://localhost:8080/api/v1/cars/${from}/${to}?page=${page}&pageSize=${pageSize}`;
                const response = await fetch(url);
                if(response.ok) {
                    const dataJson = await response.json();
                    setAvailableCars(dataJson);
                }
                else {
                    console.log("fetch exception, start date greater than to date");
                    setAvailableCars(null);
                }
                // console.log('from: %s\n to: %s\n location: %s\n', fromDate, toDate, location);
            }
        }
        catch(error) {
            console.log("Error while fetching data from: " + url + "\n error: " + error);
        }
    }

    const handleSubmit = async (e) => {
        e.preventDefault();
        await fetchAvailableCars(fromDate, toDate, 0, 10);
    }

    return(
        <form className='search-form' onSubmit={handleSubmit}>
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
                        className='date-input'
                        type="datetime-local" 
                        min={"2020-01-01"}
                        max={toDate}
                        // step={1800}
                        value={fromDate}
                        onChange={(e) => {setFromDate(e.target.value)}}
                        required />
                </div>

                <div className='group'>
                    <label htmlFor="to-date">To date</label>
                    <input
                        className='date-input'
                        type="datetime-local" 
                        min={fromDate}
                        max={"2030-12-30"}
                        // step={1800}
                        value={toDate}
                        onChange={(e) => {setToDate(e.target.value)}}
                        required />
                </div>

                <Button type="submit">Search</Button>
            </form>
    )
}

export default SearchForm;