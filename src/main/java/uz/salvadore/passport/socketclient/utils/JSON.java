package uz.salvadore.passport.socketclient.utils;

import com.fasterxml.jackson.databind.ObjectMapper;

public class JSON {

    public String toJson() {
        String json;
        try {
            json = new ObjectMapper().writeValueAsString(this);
        } catch (Exception ex) {
            json = "";
        }
        return json;
    }

}
