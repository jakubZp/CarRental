import { useEffect, useState } from "react";
import CarItem from "../../components/car-item/carItem.component";

import "./cars.styles.css"

const Cars = () => {

    const [cars, setCars] = useState([]);

    const fetchCars = async (page, pageSize) => {
        try {
            const url = `http://localhost:8080/api/v1/cars?page=${page}&pageSize=${pageSize}`;
            const response = await fetch(url);
            const dataJson = await response.json();
            setCars(dataJson);
        }
        catch(error) {
            console.log("Error while fetching data from: " + url + "\n error: " + error);
        }
    };

    useEffect(() => {
        fetchCars(0, 10);
    }, []);

    return(
        <div>
            <div className="cars-list">
                <h2 className="header">Choose perfect car for rent</h2>
                {cars.map(car => (
                    <CarItem car={car} key={car.id}/>
                ))}
            </div>
            <div className="cars-footer">*prices for daily rental</div>
        </div>
    )
}

export default Cars;