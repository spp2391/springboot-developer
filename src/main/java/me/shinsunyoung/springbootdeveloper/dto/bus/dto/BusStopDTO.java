package me.shinsunyoung.springbootdeveloper.dto.bus.dto;

import lombok.Data;

@Data
public class BusStopDTO {
    private String bstopid;    // 정류소 ID
    private String bstopnm;    // 정류소명
    private String arsno;      // 정류소 번호
    private String gpsx;       // 경도
    private String gpsy;       // 위도
    private String stoptype;   // 정류소 유형
}
