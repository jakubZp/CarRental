import "./carItem.styles.css"
import { useContext } from "react";
import { useNavigate } from "react-router-dom";

import UserContext from "../../contexts/user.context";
import { OrderContext } from "../../contexts/order.context";
import Button from "../button/button.component";

const CarItem = ({car = {}}) => {
    const {brand, model, productionYear, actualDailyPrice} = car;
    const {currentUser} = useContext(UserContext);
    const {setCar} = useContext(OrderContext);
    const navigate = useNavigate();

    const handleClick = () => {
        if(currentUser === null) {
            navigate("/auth");
        }
        setCar(car);
        navigate("/order");
    }

    return(
        <div className="car-item">
            <span className="info">{brand} {model} {productionYear}</span>
            <div>
                <span className="price">{actualDailyPrice} PLN</span>
                <Button onClick={handleClick}>Choose</Button>
            </div>
        </div>
    )
}

export default CarItem;