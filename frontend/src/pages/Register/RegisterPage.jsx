import React, { useState } from 'react';
import { Form, Button } from 'react-bootstrap';
import { useNavigate } from 'react-router-dom';
import { TextInput, SelectInput, CheckboxInput, DateInput } from '../../components/FormFields';
import { createAccount } from '../../services/accountService';
import { useNotification } from '../../context/NotificationContext';
import styles from './RegisterPage.module.css';

const RegisterPage = () => {
  const [acceptTerms, setAcceptTerms] = useState(false);
  const navigate = useNavigate();
  const { notify } = useNotification();

  const [formData, setFormData] = useState({
    contact: {
      email_address: '',
      phone_number: '',
      street_address: ['', ''],
      city: ''
    },
    identity: {
      given_name: '',
      family_name: '',
      date_of_birth: '',
      tax_id: '',
      tax_id_type: '',
    },
    disclosures: {
      is_control_person: false,
      is_affiliated_exchange_or_finra: false,
      is_politically_exposed: false,
      immediate_family_exposed: false
    },
    agreements: [],
    password: ''
  });

  const handleChange = (e) => {
    const { name, type, value, checked } = e.target;
    const keys = name.split('.');
    setFormData((prevData) => {
      const newData = { ...prevData };
      let current = newData;
      for (let i = 0; i < keys.length - 1; i++) {
        current = current[keys[i]] = current[keys[i]] || {};
      }
      current[keys[keys.length - 1]] = type === 'checkbox' ? checked : value;
      return newData;
    });
  };

  const handleAcceptTermsChange = (e) => {
    const checked = e.target.checked;
    setAcceptTerms(checked);
    setFormData((prevData) => ({
      ...prevData,
      agreements: checked
        ? [{
          agreement: 'customer_agreement',
          signed_at: new Date().toISOString(),
          ip_address: '127.0.0.1'
        }]
        : []
    }));
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      await createAccount(formData);
      notify('Cuenta creada exitosamente', 'success');
      navigate('/');
    } catch (error) {
      notify(' Error al crear la cuenta: ' + error.message, 'error');
    }
  };

  return (
    <div className={styles.registerContainer}>
      <h2 className={styles.title}>Registro de Usuario</h2>
      <Form onSubmit={handleSubmit} className={styles.form}>

        <h5>Datos de contacto</h5>
        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <TextInput label="Email" name="contact.email_address" value={formData.contact.email_address} onChange={handleChange} required />
          </div>
          <div className={styles.formGroup}>
            <TextInput label="Contraseña" name="password" value={formData.password} onChange={handleChange} type="password" required />
          </div>
        </div>

        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <TextInput label="Teléfono" name="contact.phone_number" value={formData.contact.phone_number} onChange={handleChange} required />
          </div>
          <div className={styles.formGroup}>
            <TextInput label="Ciudad" name="contact.city" value={formData.contact.city} onChange={handleChange} required />
          </div>
        </div>
        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <TextInput label="Dirección" name="contact.street_address.0" value={formData.contact.street_address[0]} onChange={handleChange} required />
          </div>
          <div className={styles.formGroup}>
            <TextInput label="Complemento" name="contact.street_address.1" value={formData.contact.street_address[1]} onChange={handleChange} />
          </div>
        </div>
        <hr />

        <h5>Datos de identidad</h5>
        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <TextInput label="Nombre" name="identity.given_name" value={formData.identity.given_name} onChange={handleChange} required />
          </div>
          <div className={styles.formGroup}>
            <TextInput label="Apellido" name="identity.family_name" value={formData.identity.family_name} onChange={handleChange} required />
          </div>
        </div>

        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <DateInput label="Fecha de nacimiento" name="identity.date_of_birth" value={formData.identity.date_of_birth} onChange={handleChange} required max={'2006-12-31'} />
          </div>
          <div className={styles.formGroup}>
            <TextInput label="Tax ID" name="identity.tax_id" value={formData.identity.tax_id} onChange={handleChange} required />
          </div>
        </div>

        <SelectInput label="Tipo de Tax ID" name="identity.tax_id_type" value={formData.identity.tax_id_type} onChange={handleChange} required options={["USA_SSN", "USA_ITIN"]} />

        <hr />

        <h5>Declaraciones</h5>
        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <CheckboxInput label="¿Es persona controladora?" name="disclosures.is_control_person" checked={formData.disclosures.is_control_person} onChange={handleChange} />
          </div>
          <div className={styles.formGroup}>
            <CheckboxInput label="¿Afiliado a bolsa o FINRA?" name="disclosures.is_affiliated_exchange_or_finra" checked={formData.disclosures.is_affiliated_exchange_or_finra} onChange={handleChange} />
          </div>
        </div>
        <div className={styles.formRow}>
          <div className={styles.formGroup}>
            <CheckboxInput label="¿Persona políticamente expuesta?" name="disclosures.is_politically_exposed" checked={formData.disclosures.is_politically_exposed} onChange={handleChange} />
          </div>
          <div className={styles.formGroup}>
            <CheckboxInput label="¿Familiar expuesto políticamente?" name="disclosures.immediate_family_exposed" checked={formData.disclosures.immediate_family_exposed} onChange={handleChange} />
          </div>
        </div>

        <hr />

        <CheckboxInput label="Acepto los términos y condiciones" name="accept_terms" checked={acceptTerms} onChange={handleAcceptTermsChange} />

        <div>
          <Button type="submit" disabled={!acceptTerms}>Crear cuenta</Button>
          <p className={styles.terms}>
          ¿Ya tienes cuenta de Forest Trade? 
          <a href="/login"><br/> Ingresa aquí</a>
        </p>
        </div>
      </Form>
    </div>
  );
};

export default RegisterPage;
