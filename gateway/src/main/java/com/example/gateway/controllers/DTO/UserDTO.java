package com.example.gateway.controllers.DTO;


import java.util.Set;


public record UserDTO (   String username,
                          String email,
                          String firstName,
                          String lastName,
                          String password,
                          Set<String> roles) {


}
