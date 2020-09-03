package org.carlook.model.objects.dto;

public class VertrieblerDTO extends UserDTO {
   private String stadt;


    public VertrieblerDTO(UserDTO userDTO) {
        super(userDTO);
    }

    public String getStadt() {
        return stadt;
    }
    public void setStadt(String id) {
        this.stadt = id;
    }


}
