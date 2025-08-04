package me.shinsunyoung.springbootdeveloper.dto;

import lombok.Data;

@Data
public class MedicalInstitutionDTO {
    private String instit_nm;
    private String instit_kind;
    private String medical_instit_kind;
    private String zip_code;
    private String street_nm_addr;
    private String tel;
    private String organ_loc;
    private String monday;
    private String tuesday;
    private String wednesday;
    private String thursday;
    private String friday;
    private String saturday;
    private String sunday;
    private String holiday;
    private String sunday_oper_week;
    private String exam_part;
    private String regist_dt;
    private String update_dt;
    private String lng;
    private String lat;
}
