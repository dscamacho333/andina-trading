// src/components/FormFields/TextInput.jsx
import React from 'react';
import { Form } from 'react-bootstrap';

const TextInput = ({ label, name, value, onChange, type = "text", required = false }) => {
  return (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Control
        type={type}
        name={name}
        value={value}
        onChange={onChange}
        required={required}
      />
    </Form.Group>
  );
};

export default TextInput;
