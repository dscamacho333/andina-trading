// src/components/FormFields/CheckboxInput.jsx
import React from 'react';
import { Form } from 'react-bootstrap';

const CheckboxInput = ({ label, name, checked, onChange }) => {
  return (
    <Form.Group className="mb-3">
      <Form.Check
        type="checkbox"
        label={label}
        name={name}
        checked={checked}
        onChange={onChange}
      />
    </Form.Group>
  );
};

export default CheckboxInput;