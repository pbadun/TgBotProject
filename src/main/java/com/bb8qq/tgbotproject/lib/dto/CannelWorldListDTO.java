package com.bb8qq.tgbotproject.lib.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class CannelWorldListDTO {

    private List<WorldDTO> worldDTOS;

    private String channelName;

    public CannelWorldListDTO() {
        this.worldDTOS = new ArrayList<>();
    }
}
