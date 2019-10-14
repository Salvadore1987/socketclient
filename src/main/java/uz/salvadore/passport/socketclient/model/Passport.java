package uz.salvadore.passport.socketclient.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import uz.asbt.passport.jmrtdlibrary.model.PassportData;
import uz.salvadore.passport.socketclient.serializer.JsonDateSerializer;

import java.util.Date;

@Data
public class Passport {
    private String documentCode;
    private String issuingState;
    private String surname;
    private String name;
    private String nationality;
    private String documentNumber;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date dateOfBirth;
    private String gender;
    @JsonSerialize(using = JsonDateSerializer.class)
    private Date dateOfExpiry;
    private String optionalData1;
    private String optionalData2;
    private String mrzLine1;
    private String mrzLine2;
    private String mrzLine3;
    private byte[] photo;

    private Passport() {
    }

    public static class Builder {
        public static Passport build(PassportData data) {
            Passport passport = new Passport();
            passport.setDocumentCode(data.getDocumentCode());
            passport.setIssuingState(data.getIssuingState());
            passport.setSurname(data.getSurname());
            passport.setName(data.getName());
            passport.setNationality(data.getNationality());
            passport.setDocumentNumber(data.getDocumentNumber());
            passport.setDateOfBirth(data.getDateOfBirth());
            passport.setGender(data.getGender());
            passport.setDateOfExpiry(data.getDateOfExpiry());
            passport.setOptionalData1(data.getOptionalData1());
            passport.setOptionalData2(data.getOptionalData2());
            passport.setMrzLine1(data.getMrzLine1());
            passport.setMrzLine2(data.getMrzLine2());
            passport.setMrzLine3(data.getMrzLine3());
            passport.setPhoto(data.getPhoto());
            return passport;
        }
    }


}
