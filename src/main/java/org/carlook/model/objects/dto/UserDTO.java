package org.carlook.model.objects.dto;

import com.vaadin.ui.Notification;
import org.carlook.model.dao.RoleDAO;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDTO extends AbstractDTO implements Serializable {
    private int id;
    private String anrede;
    private String vorname;
    private String name;
    private String email;
    private String password;
    private List<RoleDTO> rolle = null;

    public UserDTO() {}
    public UserDTO(UserDTO userDTO) {
        this.id = userDTO.id;
        this.anrede = userDTO.anrede;
        this.vorname = userDTO.vorname;
        this.name = userDTO.name;
        this.email = userDTO.email;
        this.password = userDTO.password;
        this.rolle = userDTO.rolle;
    }

    public String getAnrede() {
        return anrede;
    }

    public void setAnrede(String anrede) {
        this.anrede = anrede;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getId() { return id; }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String id) {
        this.email = id;
    }
    //Überprüft ob einem User bereits eine Rolle zugeteilt wurde
    public boolean hasRole(String role){
        if (this.rolle == null) {
            getRoles();
        }
        for(RoleDTO r : rolle) {
            if (r.getBezeichnung().equals(role)) return true;
        }
        return false;

    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void getRoles() {
        try {
            this.rolle = RoleDAO.getInstance().getRolesForUser(this);
        } catch (SQLException e) {
            Notification.show("Es ist ein SQL-Fehler aufgetreten. Bitte informieren Sie einen Administrator!", Notification.Type.ERROR_MESSAGE);
        }

    }

    public void setRole(String roles) {
        RoleDTO roleDTO = new RoleDTO();
        roleDTO.setBezeichnung(roles);
        List<RoleDTO> roleDTOList = new ArrayList<>();
        roleDTOList.add(roleDTO);
        this.rolle = roleDTOList;
    }
    public String getRole(){
        return rolle.get(0).getBezeichnung();
    }

}
