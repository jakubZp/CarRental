import { useContext, useState } from 'react';

import { OrderContext } from '../../contexts/order.context';
import SearchForm from '../../components/search-form/search-form.component';

import './order.styles.css';

const Order = () => {
    const {car, location, fromDate, toDate} = useContext(OrderContext);

    const calculateTotalPrice = () => {
        const days = Math.ceil(Math.abs(new Date(fromDate) - new Date(toDate)) / (1000 * 60 * 60 * 24));
        return days * car.actualDailyPrice;
    }

    const formattedLocation = location.charAt(0).toUpperCase() + location.slice(1);;
    const formattedFromDate = new Date(fromDate).toLocaleString();
    const formattedToDate = new Date(toDate).toLocaleString();

    const totalPrice = calculateTotalPrice();

    return(
        <div className='order'>
            <h2 className='overview'> Overview </h2>
            <div>
                <span className='attribute'> Chosen car: </span>
                <span>{car.brand} {car.model} {car.productionYear}</span>
            </div>
            <div>
                <span className='attribute'> Daily price: </span> 
                <span>{car.actualDailyPrice} PLN</span>
            </div>
            <div>
                <span className='attribute'> Location: </span>  
                <span>{formattedLocation}</span>
            </div>
            <div>
                <span className='attribute'> Period:     </span>  
                <span>{formattedFromDate} - {formattedToDate}</span>
            </div>
                {/* <SearchForm setAvailableCars={setAvailableCars}/> */}
            <div>
                <span className='attribute'>Total price: </span>
                <span> {totalPrice} PLN</span>
            </div>
            <div>
                <span className='attribute'>Payment method </span>
                <div>
                    <span>credit card</span>
                </div>
            </div>
        </div>
    )
}

export default Order;