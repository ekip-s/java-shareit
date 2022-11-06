package ru.practicum.shareit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import ru.practicum.shareit.user.model.User;
import ru.practicum.shareit.user.service.UserServiceJPA;

@SpringBootApplication
public class ShareItApp {

	static UserServiceJPA userServiceJPA;
    @Autowired
	public ShareItApp(UserServiceJPA userServiceJPA) {
		this.userServiceJPA = userServiceJPA;
	}


	public static void main(String[] args) {
		SpringApplication.run(ShareItApp.class, args);
	}

}
