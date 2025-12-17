package net.foodeals.user.application.dtos.responses;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TimelineEventDto {
    private String status;
    private String timestamp;
    private String description;
}