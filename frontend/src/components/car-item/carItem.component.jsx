import "./carItem.styles.css"

const CarItem = ({car}) => {
    const {brand, model, productionYear, actualDailyPrice} = car;
    return(
        <div className="item-container">
            <span className="info">{brand} {model} {productionYear}  </span>
            <span className="price">{actualDailyPrice} PLN</span>
        </div>
    )
}

export default CarItem;