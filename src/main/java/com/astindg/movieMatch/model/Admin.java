package com.astindg.movieMatch.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@EqualsAndHashCode
public class Admin {
    private String username;
    private String email;
    private String password;
    private User user;

}
