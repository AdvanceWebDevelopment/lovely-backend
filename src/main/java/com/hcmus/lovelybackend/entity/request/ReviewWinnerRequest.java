package com.hcmus.lovelybackend.entity.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewWinnerRequest {
    @NotNull(message = "Comment is mandatory.")
    private String comment;

    @NotNull(message = "Like is mandatory.")
    private Boolean like;
}
