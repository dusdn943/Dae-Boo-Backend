package com.example.teamproject.user.dto;

import com.example.teamproject.user.entity.User;
import com.example.teamproject.user.entity.UserInterest;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserInfoResponse {

    private String nickname;
    private int age;
    private String gender;
    private String mobile;
    private String email;
    private UserInterest interest1;
    private UserInterest interest2;

    public static UserInfoResponse from(User user) {
        return new UserInfoResponse(
                user.getNickname(),
                Integer.parseInt(user.getAge()),
                user.getGender(),
                user.getMobile(),
                user.getEmail(),
                user.getInterest1(),
                user.getInterest2()
        );
    }
}
