package ru.practicum.shareit.booking.controller;

import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import ru.practicum.shareit.booking.model.Booking;
import ru.practicum.shareit.booking.model.BookingDto;
import ru.practicum.shareit.booking.model.BookingStatus;
import ru.practicum.shareit.booking.service.BookingServiceJPA;
import ru.practicum.shareit.item.model.Item;
import ru.practicum.shareit.user.model.User;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@WebMvcTest(controllers = BookingController.class)
class BookingControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper mapper;
    @MockBean
    private BookingServiceJPA bookingService;
    private BookingDto bookingDto;
    private Booking booking;
    private Booking booking2;
    private LocalDateTime start;
    private LocalDateTime end;
    private List<Booking> bookings;
    private final String sharerUserId = "X-Sharer-User-Id";
    private final String url = "/bookings";
    private final String name = "name";
    private final String name2 = "name2";
    private final String description = "description";
    private final String description2 = "description2";
    private final String waiting = "WAITING";
    private final String approved = "APPROVED";

    @BeforeEach
    void createTest() {
        mapper.registerModule(new JavaTimeModule());

        bookingDto = new BookingDto();
        start = LocalDateTime. now().plusHours(2);
        end = LocalDateTime. now().plusHours(8);
        bookingDto = new BookingDto(0, start, end,1);
        booking = new Booking(1, start, end, new Item(name, description),
                new User(1L), BookingStatus.WAITING);
        booking2 = new Booking(2, start, end, new Item(name2, description2),
                new User(1L), BookingStatus.APPROVED);
        bookings = new ArrayList();
        bookings.add(booking);
        bookings.add(booking2);
    }

    @Test
    void addNewBookingTest() throws Exception {
        when(bookingService.addBooking(any(), any()))
                .thenReturn(booking);

        mockMvc.perform(post(url)
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(bookingDto))
                .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.item.name", equalTo(name)));
    }

    @Test
    void setStatusTest() throws Exception {
        bookingDto.setId(1);
        booking.setStatus(BookingStatus.APPROVED);
        when(bookingService.setStatus(anyLong(), anyLong(), anyBoolean()))
                .thenReturn(booking);

        mockMvc.perform(patch(url + "/1?approved=true")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(bookingDto))
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status", equalTo(approved)));
    }

    @Test
    void getBookingByIdTest() throws Exception {
        when(bookingService.getById(anyLong(), anyLong()))
                .thenReturn(booking);

        mockMvc.perform(get(url + "/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id", equalTo(1)))
                .andExpect(jsonPath("$.status", equalTo(waiting)))
                .andExpect(jsonPath("$.item.name", equalTo(name)))
                .andExpect(jsonPath("$.item.description", equalTo(description)));
    }

    @Test
    void getBookingsListTest() throws Exception {
        when(bookingService.getBookings(anyLong(), any()))
                .thenReturn(bookings);

        mockMvc.perform(get(url)
                .header(sharerUserId, 1)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(waiting, approved)))
                .andExpect(jsonPath("$[*].item.name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].item.description", containsInAnyOrder(description,
                        description2)));
    }

    @Test
    void getBookingsListPageTest() throws Exception {
        when(bookingService.getBookings(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get(url + "?from=0&size=10")
                .header(sharerUserId, 1)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(waiting, approved)))
                .andExpect(jsonPath("$[*].item.name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].item.description", containsInAnyOrder(description2,
                        description)));
    }

    @Test
    void getBookingsOwnerListTest() throws Exception  {
        when(bookingService.getBookingsOwner(anyLong(), any()))
                .thenReturn(bookings);

        mockMvc.perform(get(url + "/owner")
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(waiting, approved)))
                .andExpect(jsonPath("$[*].item.name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].item.description", containsInAnyOrder(description2,
                        description)));
    }

    @Test
    void getBookingsOwnerListPageTest() throws Exception  {
        when(bookingService.getBookingsOwner(anyLong(), any(), anyInt(), anyInt()))
                .thenReturn(bookings);

        mockMvc.perform(get(url + "/owner?from=0&size=10")
                        .header(sharerUserId, 1)
                )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].id", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[*].status", containsInAnyOrder(waiting, approved)))
                .andExpect(jsonPath("$[*].item.name", containsInAnyOrder(name, name2)))
                .andExpect(jsonPath("$[*].item.description", containsInAnyOrder(description2,
                        description)));
    }
}