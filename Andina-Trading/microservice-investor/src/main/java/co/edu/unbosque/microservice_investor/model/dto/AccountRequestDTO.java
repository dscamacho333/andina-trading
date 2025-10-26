package co.edu.unbosque.microservice_investor.model.dto;


import java.util.List;

public class AccountRequestDTO {

    private Contact contact;
    private Identity identity;
    private Disclosures disclosures;
    private List<Agreement> agreements;
    private String password;

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public Identity getIdentity() {
        return identity;
    }

    public void setIdentity(Identity identity) {
        this.identity = identity;
    }

    public Disclosures getDisclosures() {
        return disclosures;
    }

    public void setDisclosures(Disclosures disclosures) {
        this.disclosures = disclosures;
    }

    public List<Agreement> getAgreements() {
        return agreements;
    }

    public void setAgreements(List<Agreement> agreements) {
        this.agreements = agreements;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public static class Contact {
        private String email_address;
        private String phone_number;
        private List<String> street_address;
        private String city;

        public String getEmail_address() {
            return email_address;
        }

        public void setEmail_address(String email_address) {
            this.email_address = email_address;
        }

        public String getPhone_number() {
            return phone_number;
        }

        public void setPhone_number(String phone_number) {
            this.phone_number = phone_number;
        }

        public List<String> getStreet_address() {
            return street_address;
        }

        public void setStreet_address(List<String> street_address) {
            this.street_address = street_address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }


    public static class Identity {
        private String given_name;
        private String family_name;
        private String date_of_birth; // ISO-8601 format: "yyyy-MM-dd"
        private String tax_id;
        private String tax_id_type;
        private String country_of_tax_residence;

        public String getGiven_name() {
            return given_name;
        }

        public void setGiven_name(String given_name) {
            this.given_name = given_name;
        }

        public String getFamily_name() {
            return family_name;
        }

        public void setFamily_name(String family_name) {
            this.family_name = family_name;
        }

        public String getDate_of_birth() {
            return date_of_birth;
        }

        public void setDate_of_birth(String date_of_birth) {
            this.date_of_birth = date_of_birth;
        }

        public String getTax_id() {
            return tax_id;
        }

        public void setTax_id(String tax_id) {
            this.tax_id = tax_id;
        }

        public String getTax_id_type() {
            return tax_id_type;
        }

        public void setTax_id_type(String tax_id_type) {
            this.tax_id_type = tax_id_type;
        }

        public String getCountry_of_tax_residence() {
            return country_of_tax_residence;
        }

        public void setCountry_of_tax_residence(String country_of_tax_residence) {
            this.country_of_tax_residence = country_of_tax_residence;
        }
    }

    public static class Disclosures {
        private boolean is_control_person;
        private boolean is_affiliated_exchange_or_finra;
        private boolean is_politically_exposed;
        private boolean immediate_family_exposed;

        public boolean isIs_control_person() {
            return is_control_person;
        }

        public void setIs_control_person(boolean is_control_person) {
            this.is_control_person = is_control_person;
        }

        public boolean isIs_affiliated_exchange_or_finra() {
            return is_affiliated_exchange_or_finra;
        }

        public void setIs_affiliated_exchange_or_finra(boolean is_affiliated_exchange_or_finra) {
            this.is_affiliated_exchange_or_finra = is_affiliated_exchange_or_finra;
        }

        public boolean isIs_politically_exposed() {
            return is_politically_exposed;
        }

        public void setIs_politically_exposed(boolean is_politically_exposed) {
            this.is_politically_exposed = is_politically_exposed;
        }

        public boolean isImmediate_family_exposed() {
            return immediate_family_exposed;
        }

        public void setImmediate_family_exposed(boolean immediate_family_exposed) {
            this.immediate_family_exposed = immediate_family_exposed;
        }
    }

    public static class Agreement {
        private String agreement; // e.g., "customer_agreement"
        private String signed_at; // ISO-8601 format
        private String ip_address;

        public String getAgreement() {
            return agreement;
        }

        public void setAgreement(String agreement) {
            this.agreement = agreement;
        }

        public String getSigned_at() {
            return signed_at;
        }

        public void setSigned_at(String signed_at) {
            this.signed_at = signed_at;
        }

        public String getIp_address() {
            return ip_address;
        }

        public void setIp_address(String ip_address) {
            this.ip_address = ip_address;
        }
    }
}