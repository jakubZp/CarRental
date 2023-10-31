import './button.styles.css'

const Button = ({ children, ...otherProps }) => {
    return(
        <button {...otherProps} className="default">
            {children}
        </button>
    );
};

export default Button;