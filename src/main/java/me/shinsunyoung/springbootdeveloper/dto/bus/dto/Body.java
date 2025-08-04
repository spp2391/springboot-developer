package me.shinsunyoung.springbootdeveloper.dto.bus.dto;

import lombok.Data;

@Data
public class Body {
    private ItemWrapper items;
    private int numOfRows;
    private int pageNo;
    private int totalCount;
}
