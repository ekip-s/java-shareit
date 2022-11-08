package ru.practicum.shareit.booking.service;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.model.RequestParameters;

@Component
public class EnumConverter implements Converter<String, RequestParameters> {

    @Override
    public RequestParameters convert(String value) {
        if (value.isBlank()) {
            value = "ALL";
        }
        return RequestParameters.valueOf(value);
    }
}

