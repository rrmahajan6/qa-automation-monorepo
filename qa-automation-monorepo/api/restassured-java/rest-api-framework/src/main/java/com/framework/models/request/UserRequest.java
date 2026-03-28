package com.framework.models.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Request payload DTO for creating / updating a User.
 * Jackson: nulls excluded from serialisation to keep payloads clean.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserRequest {

    private String name;
    private String username;
    private String email;
    private String phone;
    private String website;
    private Address address;
    private Company company;

    public static UserRequestBuilder builder() {
        return new UserRequestBuilder();
    }

    public static class UserRequestBuilder {
        private final UserRequest instance = new UserRequest();

        public UserRequestBuilder name(String name) {
            instance.name = name;
            return this;
        }

        public UserRequestBuilder username(String username) {
            instance.username = username;
            return this;
        }

        public UserRequestBuilder email(String email) {
            instance.email = email;
            return this;
        }

        public UserRequestBuilder phone(String phone) {
            instance.phone = phone;
            return this;
        }

        public UserRequestBuilder website(String website) {
            instance.website = website;
            return this;
        }

        public UserRequestBuilder address(Address address) {
            instance.address = address;
            return this;
        }

        public UserRequestBuilder company(Company company) {
            instance.company = company;
            return this;
        }

        public UserRequest build() {
            return instance;
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Address {
        private String street;
        private String suite;
        private String city;
        private String zipcode;
        private Geo geo;

        public static AddressBuilder builder() {
            return new AddressBuilder();
        }

        public static class AddressBuilder {
            private final Address instance = new Address();

            public AddressBuilder street(String street) {
                instance.street = street;
                return this;
            }

            public AddressBuilder suite(String suite) {
                instance.suite = suite;
                return this;
            }

            public AddressBuilder city(String city) {
                instance.city = city;
                return this;
            }

            public AddressBuilder zipcode(String zipcode) {
                instance.zipcode = zipcode;
                return this;
            }

            public AddressBuilder geo(Geo geo) {
                instance.geo = geo;
                return this;
            }

            public Address build() {
                return instance;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Geo {
        private String lat;
        private String lng;

        public static GeoBuilder builder() {
            return new GeoBuilder();
        }

        public static class GeoBuilder {
            private final Geo instance = new Geo();

            public GeoBuilder lat(String lat) {
                instance.lat = lat;
                return this;
            }

            public GeoBuilder lng(String lng) {
                instance.lng = lng;
                return this;
            }

            public Geo build() {
                return instance;
            }
        }
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Company {
        private String name;
        private String catchPhrase;
        private String bs;

        public static CompanyBuilder builder() {
            return new CompanyBuilder();
        }

        public static class CompanyBuilder {
            private final Company instance = new Company();

            public CompanyBuilder name(String name) {
                instance.name = name;
                return this;
            }

            public CompanyBuilder catchPhrase(String catchPhrase) {
                instance.catchPhrase = catchPhrase;
                return this;
            }

            public CompanyBuilder bs(String bs) {
                instance.bs = bs;
                return this;
            }

            public Company build() {
                return instance;
            }
        }
    }
}
