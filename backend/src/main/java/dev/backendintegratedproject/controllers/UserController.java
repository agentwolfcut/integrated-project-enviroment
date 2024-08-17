package dev.backendintegratedproject.controllers;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v2/users")
@CrossOrigin(origins = {"http://localhost:5173",
                        "http://ip23ft.sit.kmutt.ac.th",
                        "http://intproj23.sit.kmutt.ac.th"})
public class UserController {

}
