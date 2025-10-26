// src/components/FormFields/SelectInput.jsx
import React from 'react';
import { Form } from 'react-bootstrap';

const SelectInput = ({ label, name, value, onChange, options = [], required = false }) => {
  return (
    <Form.Group className="mb-3">
      <Form.Label>{label}</Form.Label>
      <Form.Select name={name} value={value} onChange={onChange} required={required}>
        <option value="">Seleccione una opción</option>
        {options.map((opt, idx) => (
          <option key={idx} value={opt}>
            {opt}
          </option>
        ))}
      </Form.Select>
    </Form.Group>
  );
};

export default SelectInput;
