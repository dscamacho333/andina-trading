export const Button = ({ 
  children, 
  variant = 'primary', 
  disabled = false, 
  onClick, 
  style = {},
  type = 'button',
  ...props 
}) => {
  const baseStyle = {
    padding: '12px 24px',
    borderRadius: '6px',
    cursor: disabled ? 'not-allowed' : 'pointer',
    border: 'none',
    fontWeight: '500',
    fontSize: '14px',
    transition: 'all 0.2s ease',
    boxShadow: '0 2px 4px rgba(0,0,0,0.1)',
    opacity: disabled ? 0.6 : 1,
    ...style
  };

  const variants = {
    primary: { background: '#007bff', color: 'white' },
    secondary: { background: '#28a745', color: 'white' },
    warning: { background: '#ffc107', color: '#212529' },
    danger: { background: '#dc3545', color: 'white' },
    info: { background: '#17a2b8', color: 'white' },
    cancel: { background: '#6c757d', color: 'white' },
  };

  const buttonStyle = {
    ...baseStyle,
    ...variants[variant],
  };

  return (
    <button
      type={type}
      style={buttonStyle}
      onClick={onClick}
      disabled={disabled}
      {...props}
    >
      {children}
    </button>
  );
};