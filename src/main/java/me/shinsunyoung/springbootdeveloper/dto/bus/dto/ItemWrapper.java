package me.shinsunyoung.springbootdeveloper.dto.bus.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;
@Data
public class ItemWrapper {

    @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
    private List<BusStopDTO> item = new ArrayList<>();
}