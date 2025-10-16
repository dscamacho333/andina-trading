export const Table = ({ children, style = {} }) => {
  return (
    <table style={{ 
      borderCollapse: 'collapse',
      width: '100%',
      background: 'white',
      ...style 
    }}>
      {children}
    </table>
  );
};

export const Th = ({ children, style = {} }) => {
  return (
    <th style={{
      textAlign: 'left',
      padding: '16px 12px',
      borderBottom: '2px solid #dee2e6',
      background: '#f8f9fa',
      fontWeight: '600',
      fontSize: '14px',
      color: '#495057',
      ...style
    }}>
      {children}
    </th>
  );
};

export const Td = ({ children, style = {}, ...props }) => {
  return (
    <td {...props} style={{
      padding: '16px 12px',
      borderBottom: '1px solid #dee2e6',
      fontSize: '14px',
      color: '#495057',
      ...style
    }}>
      {children}
    </td>
  );
};