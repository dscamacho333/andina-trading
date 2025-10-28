import React from 'react';
import { Form } from 'react-bootstrap';

const DateInput = ({ label, name, value, onChange, required, max }) => {
  return (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Control
        type="date"
        name={name}
        value={value}
        onChange={onChange}
        required={required}
        max={max}
      />
    </Form.Group>
  );
};

export default DateInput;
