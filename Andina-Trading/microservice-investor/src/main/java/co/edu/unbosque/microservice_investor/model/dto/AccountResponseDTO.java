package co.edu.unbosque.microservice_investor.model.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponseDTO {

    private String id;

    @JsonProperty("account_number")
    private String accountNumber;

    private String status;

    @JsonProperty("crypto_status")
    private String cryptoStatus;

    private String currency;

    @JsonProperty("last_equity")
    private String lastEquity;

    @JsonProperty("created_at")
    private String createdAt;

    private Contact contact;

    private Identity identity;

    private Disclosures disclosures;

    private List<Agreement> agreements;

    @JsonProperty("account_type")
    private String accountType;

    @JsonProperty("trading_type")
    private String tradingType;

    @JsonProperty("trading_configurations")
    private Object tradingConfigurations;

    @JsonProperty("enabled_assets")
    private List<String> enabledAssets;

    public AccountResponseDTO() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCryptoStatus() {
        return cryptoStatus;
    }

    public void setCryptoStatus(String cryptoStatus) {
        this.cryptoStatus = cryptoStatus;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getLastEquity() {
        return lastEquity;
    }

    public void setLastEquity(String lastEquity) {
        this.lastEquity = lastEquity;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

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

    public String getAccountType() {
        return accountType;
    }

    public void setAccountType(String accountType) {
        this.accountType = accountType;
    }

    public String getTradingType() {
        return tradingType;
    }

    public void setTradingType(String tradingType) {
        this.tradingType = tradingType;
    }

    public Object getTradingConfigurations() {
        return tradingConfigurations;
    }

    public void setTradingConfigurations(Object tradingConfigurations) {
        this.tradingConfigurations = tradingConfigurations;
    }

    public List<String> getEnabledAssets() {
        return enabledAssets;
    }

    public void setEnabledAssets(List<String> enabledAssets) {
        this.enabledAssets = enabledAssets;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Contact {
        @JsonProperty("email_address")
        private String emailAddress;

        @JsonProperty("phone_number")
        private String phoneNumber;

        @JsonProperty("street_address")
        private List<String> streetAddress;

        @JsonProperty("local_street_address")
        private String localStreetAddress;

        private String city;

        private String country;

        public Contact() {
        }

        public String getEmailAddress() {
            return emailAddress;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public List<String> getStreetAddress() {
            return streetAddress;
        }

        public String getLocalStreetAddress() {
            return localStreetAddress;
        }

        public String getCity() {
            return city;
        }

        public String getCountry() {
            return country;
        }
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class Identity {
        @JsonProperty("given_name")
        private String givenName;

        @JsonProperty("family_name")
        private String familyName;

        @JsonProperty("date_of_birth")
        private String dateOfBirth;

        @JsonProperty("party_type")
        private String partyType;

        @JsonProperty("tax_id_type")
        private String taxIdType;

        @JsonProperty("country_of_tax_residence")
        private String countryOfTaxResidence;

        @JsonProperty("funding_source")
        private Object fundingSource;

        public Identity() {
        }

        public String getGivenName() {
            return givenName;
        }

        public void setGivenName(String givenName) {
            this.givenName = givenName;
        }

        public String getFamilyName() {
            return familyName;
        }

        public void setFamilyName(String familyName) {
            this.familyName = familyName;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getPartyType() {
            return partyType;
        }

        public void setPartyType(String partyType) {
            this.partyType = partyType;
        }

        public String getTaxIdType() {
            return taxIdType;
        }

        public void setTaxIdType(String taxIdType) {
            this.taxIdType = taxIdType;
        }

        public String getCountryOfTaxResidence() {
            return countryOfTaxResidence;
        }

        public void setCountryOfTaxResidence(String countryOfTaxResidence) {
            this.countryOfTaxResidence = countryOfTaxResidence;
        }

        public Object getFundingSource() {
            return fundingSource;
        }

        public void setFundingSource(Object fundingSource) {
            this.fundingSource = fundingSource;
        }
    }

    public static class Disclosures {
        @JsonProperty("is_control_person")
        private boolean isControlPerson;

        @JsonProperty("is_affiliated_exchange_or_finra")
        private boolean isAffiliatedExchangeOrFinra;

        @JsonProperty("is_affiliated_exchange_or_iiroc")
        private Boolean isAffiliatedExchangeOrIiroc;

        @JsonProperty("is_politically_exposed")
        private boolean isPoliticallyExposed;

        @JsonProperty("immediate_family_exposed")
        private boolean immediateFamilyExposed;

        @JsonProperty("is_discretionary")
        private boolean isDiscretionary;

        public Disclosures() {
        }

        public boolean isControlPerson() {
            return isControlPerson;
        }

        public void setControlPerson(boolean controlPerson) {
            isControlPerson = controlPerson;
        }

        public boolean isAffiliatedExchangeOrFinra() {
            return isAffiliatedExchangeOrFinra;
        }

        public void setAffiliatedExchangeOrFinra(boolean affiliatedExchangeOrFinra) {
            isAffiliatedExchangeOrFinra = affiliatedExchangeOrFinra;
        }

        public Boolean getAffiliatedExchangeOrIiroc() {
            return isAffiliatedExchangeOrIiroc;
        }

        public void setAffiliatedExchangeOrIiroc(Boolean affiliatedExchangeOrIiroc) {
            isAffiliatedExchangeOrIiroc = affiliatedExchangeOrIiroc;
        }

        public boolean isPoliticallyExposed() {
            return isPoliticallyExposed;
        }

        public void setPoliticallyExposed(boolean politicallyExposed) {
            isPoliticallyExposed = politicallyExposed;
        }

        public boolean isImmediateFamilyExposed() {
            return immediateFamilyExposed;
        }

        public void setImmediateFamilyExposed(boolean immediateFamilyExposed) {
            this.immediateFamilyExposed = immediateFamilyExposed;
        }

        public boolean isDiscretionary() {
            return isDiscretionary;
        }

        public void setDiscretionary(boolean discretionary) {
            isDiscretionary = discretionary;
        }
    }

    public static class Agreement {
        private String agreement;

        @JsonProperty("signed_at")
        private String signedAt;

        @JsonProperty("ip_address")
        private String ipAddress;

        private String revision;

        public Agreement() {
        }

        public String getAgreement() {
            return agreement;
        }

        public void setAgreement(String agreement) {
            this.agreement = agreement;
        }

        public String getSignedAt() {
            return signedAt;
        }

        public void setSignedAt(String signedAt) {
            this.signedAt = signedAt;
        }

        public String getIpAddress() {
            return ipAddress;
        }

        public void setIpAddress(String ipAddress) {
            this.ipAddress = ipAddress;
        }

        public String getRevision() {
            return revision;
        }

        public void setRevision(String revision) {
            this.revision = revision;
        }
    }

}