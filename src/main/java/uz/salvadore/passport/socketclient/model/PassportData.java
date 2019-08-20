package uz.salvadore.passport.socketclient.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.Data;
import org.jmrtd.lds.icao.MRZInfo;
import uz.salvadore.passport.socketclient.serializer.JsonDateSerializer;
import uz.salvadore.passport.socketclient.utils.JSON;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class PassportData extends JSON {

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
    private String optionalData1; /* NOTE: holds personal number for some issuing states (e.g. NL), but is used to hold (part of) document number for others. */
    private String optionalData2;
    private byte[] photo;

    private PassportData() {
    }

    public static class PassportBuilder {
        public static PassportData build(MRZInfo mrzInfo) throws ParseException {
            PassportData data = new PassportData();
            DateFormat df = new SimpleDateFormat("yyMMdd");
            data.setDocumentCode(mrzInfo.getDocumentCode());
            data.setIssuingState(mrzInfo.getIssuingState());
            data.setSurname(mrzInfo.getPrimaryIdentifier());
            data.setName(mrzInfo.getSecondaryIdentifier().replace("<", ""));
            data.setNationality(mrzInfo.getNationality());
            data.setDocumentNumber(mrzInfo.getDocumentNumber());
            data.setDateOfBirth(df.parse(mrzInfo.getDateOfBirth()));
            data.setGender(mrzInfo.getGender().name());
            data.setDateOfExpiry(df.parse(mrzInfo.getDateOfExpiry()));
            data.setOptionalData1(mrzInfo.getOptionalData1());
            data.setOptionalData2(mrzInfo.getOptionalData2());
            return data;
        }
    }

}
