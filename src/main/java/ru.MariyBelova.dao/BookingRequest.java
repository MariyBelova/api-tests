package ru.MariyBelova.dao;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import lombok.Builder;
import lombok.With;

@With
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "firstname",
        "lastname",
        "totalprice",
        "depositpaid",
        "bookingdates",
        "checkin","checkout",
        "additionalneeds"
})
public  class BookingRequest {

    @JsonProperty("firstname")
    private String firstname;
    @JsonProperty("lastname")
    private String lastname;
    @JsonProperty("totalprice")
    private Integer totalprice;
    @JsonProperty("depositpaid")
    private Boolean depositpaid;
    @JsonProperty("checkin")
    private String checkin;
    @JsonProperty("checkout")
    private String checkout;
    @JsonProperty("additionalneeds")
    private String additionalneeds;

    public static class BookingRequestBuilder {
        private String firstname;
        private String lastname;
        private int totalprice;
        private boolean depositpaid;
        private String additionalneeds;
        private String checkin;
        private String checkout;


        public BookingRequestBuilder firstname(String firstname) {
            this.firstname = firstname;
            return this;
        }

        public BookingRequestBuilder lastname(String lastname) {
            this.lastname = lastname;
            return this;
        }

        public BookingRequestBuilder totalprice(int totalprice) {
            this.totalprice = totalprice;
            return this;
        }

        public BookingRequestBuilder depositpaid(boolean depositpaid) {
            this.depositpaid = depositpaid;
            return this;
        }

        public BookingRequestBuilder bookingdates(String checkin, String checkout) {
            this.checkin = checkin;
            this.checkout = checkout;
            return this;
        }

        public BookingRequestBuilder additionalneeds(String additionalneeds) {
            this.additionalneeds = additionalneeds;
            return this;
        }

        public BookingRequest build() {
            return new BookingRequest(firstname, lastname, totalprice, depositpaid, checkin, checkout, additionalneeds);
        }

    }
}
