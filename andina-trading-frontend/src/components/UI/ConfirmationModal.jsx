export const ConfirmationModal = ({ isOpen, title, message, onConfirm, onCancel }) => {
  if (!isOpen) return null;

  return (
    <div style={{
      position: 'fixed',
      top: 0,
      left: 0,
      right: 0,
      bottom: 0,
      background: 'rgba(0,0,0,0.5)',
      display: 'flex',
      alignItems: 'center',
      justifyContent: 'center',
      zIndex: 2000
    }}>
      <div style={{
        background: 'white',
        padding: '32px',
        borderRadius: '12px',
        boxShadow: '0 8px 32px rgba(0,0,0,0.2)',
        maxWidth: '400px',
        width: '90%'
      }}>
        <h3 style={{ margin: '0 0 16px 0', color: '#2c3e50' }}>{title}</h3>
        <p style={{ margin: '0 0 24px 0', color: '#6c757d', lineHeight: '1.5' }}>{message}</p>
        <div style={{ display: 'flex', gap: '12px', justifyContent: 'flex-end' }}>
          <button
            onClick={onCancel}
            style={{
              padding: '10px 20px',
              border: '1px solid #6c757d',
              background: 'white',
              color: '#6c757d',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            Cancelar
          </button>
          <button
            onClick={onConfirm}
            style={{
              padding: '10px 20px',
              border: 'none',
              background: '#dc3545',
              color: 'white',
              borderRadius: '6px',
              cursor: 'pointer',
              fontWeight: '500'
            }}
          >
            Eliminar
          </button>
        </div>
      </div>
    </div>
  );
};