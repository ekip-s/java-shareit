package ru.practicum.shareit.booking.model;

import org.junit.jupiter.api.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

class RequestParametersTest {

    @Test
    void modelTest() {
        RequestParameters requestParameters = RequestParameters.ALL;
        assertThat(requestParameters, equalTo(RequestParameters.ALL));
    }
}