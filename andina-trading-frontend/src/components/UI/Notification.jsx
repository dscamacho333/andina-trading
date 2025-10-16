import { useEffect } from 'react';

export const Notification = ({ message, type, onClose }) => {
  useEffect(() => {
    const timer = setTimeout(() => {
      onClose();
    }, 4000);
    return () => clearTimeout(timer);
  }, [onClose]);

  const bgColor = type === 'success' ? '#d4edda' : 
                  type === 'error' ? '#f8d7da' : 
                  '#fff3cd';
  const textColor = type === 'success' ? '#155724' : 
                   type === 'error' ? '#721c24' : 
                   '#856404';
  const borderColor = type === 'success' ? '#c3e6cb' : 
                     type === 'error' ? '#f5c6cb' : 
                     '#ffeaa7';

  return (
    <div style={{
      position: 'fixed',
      top: '20px',
      right: '20px',
      background: bgColor,
      color: textColor,
      padding: '16px 20px',
      borderRadius: '8px',
      border: `1px solid ${borderColor}`,
      boxShadow: '0 4px 12px rgba(0,0,0,0.15)',
      zIndex: 1000,
      minWidth: '300px',
      maxWidth: '400px',
      animation: 'slideInRight 0.3s ease-out'
    }}>
      <div style={{ 
        display: 'flex', 
        justifyContent: 'space-between', 
        alignItems: 'flex-start',
        gap: '12px'
      }}>
        <div style={{ flex: 1 }}>
          <strong style={{ display: 'block', marginBottom: '4px' }}>
            {type === 'success' ? '✅ Éxito' : 
             type === 'error' ? '❌ Error' : '⚠️ Información'}
          </strong>
          <span>{message}</span>
        </div>
        <button 
          onClick={onClose}
          style={{
            background: 'none',
            border: 'none',
            fontSize: '18px',
            cursor: 'pointer',
            color: textColor,
            padding: '0',
            width: '24px',
            height: '24px',
            display: 'flex',
            alignItems: 'center',
            justifyContent: 'center'
          }}
        >
          ×
        </button>
      </div>
    </div>
  );
};