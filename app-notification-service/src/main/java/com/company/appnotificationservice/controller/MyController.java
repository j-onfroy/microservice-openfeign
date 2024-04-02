package com.company.appnotificationservice.controller;

import com.company.appnotificationservice.aop.CheckUser;
import com.company.appnotificationservice.aop.CheckUserExecutor;
import com.company.appnotificationservice.dto.UserDto;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notification")
public class MyController {
    @CheckUser
    @GetMapping("/get-secure-data")
    public String yopiqYul() {
        UserDto currentUser = (UserDto) CheckUserExecutor.getCurrentRequest().getAttribute("currentUser");

        return "MAxfiy ma'lumot tanish odam: " + currentUser.getId();
    }

    @CheckUser(permissions = {"ADD_CATEGORY", "EDIT_CATEGORY"})
    @GetMapping("/get-secure-data-permission")
    public String yopiqYulPermission() {
        UserDto currentUser = (UserDto) CheckUserExecutor.getCurrentRequest().getAttribute("currentUser");

        return "MAxfiy ma'lumot huquqi bor: " + currentUser.getUsername();
    }

    public static void main(String[] args) {
        String s = new MyController().yopiqYul();
    }

}
