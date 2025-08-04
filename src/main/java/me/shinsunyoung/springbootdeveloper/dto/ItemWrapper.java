package me.shinsunyoung.springbootdeveloper.dto;
import lombok.Data;
import java.util.List;

@Data
public class ItemWrapper {
    private List<MedicalInstitutionDTO> item;
}
