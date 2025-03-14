import classes from './button.module.css';

export default function Button({
    type,
    text,
    onClick,
    disabled,
    color,
    backgroundColor,
    fontSize,
    width,
    height,
}) {
    return (
        <div className={classes.container}>
          <button
            style={{
              color,
              backgroundColor,
              fontSize,
              width,
              height,
            }}
            type={type}
            onClick={onClick}
            disabled={disabled}
          >
            {text}
          </button>
        </div>
      );
}

Button.defaultProps = {
    type: 'button',
    text: 'Submit',
    disabled: false,
    backgroundColor: 'wheat',
    color: '#864848f3',
    fontSize: '1.3rem',
    width: '12rem',
    height: '3.5rem',
  };