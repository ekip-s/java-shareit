package ru.practicum.shareit.booking.service;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.model.RequestParameters;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Transactional
@SpringBootTest(
        properties = "db.name=test",
        webEnvironment = SpringBootTest.WebEnvironment.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
class EnumConverterTest {

    @Autowired
    private EnumConverter enumConverter;

    @Test
    void convert() {
        RequestParameters requestParameters = enumConverter.convert("CURRENT");
        assertThat(requestParameters, equalTo(RequestParameters.CURRENT));

        RequestParameters requestParameters2 = enumConverter.convert("");
        assertThat(requestParameters2, equalTo(RequestParameters.ALL));

        Throwable thrown = assertThrows(IllegalArgumentException.class, () -> {
            enumConverter.convert("TTYRW");
        });
        assertThat(thrown.getMessage(),
                equalTo("No enum constant ru.practicum.shareit.booking.model.RequestParameters.TTYRW"));
    }
}