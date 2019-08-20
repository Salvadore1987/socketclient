package uz.salvadore.passport.socketclient.model;

import lombok.Data;
import uz.salvadore.passport.socketclient.model.rest.MRZRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Data
public class MRZData {

    private String passport;
    private Date dateOfBirth;
    private Date expiredDate;

    private MRZData() {}

    private MRZData(String passport, Date dateOfBirth, Date expiredDate) {
        this.passport = passport;
        this.dateOfBirth = dateOfBirth;
        this.expiredDate = expiredDate;
    }

    public static class MRZBuilder {
        public static MRZData build(String mrz) throws ParseException{
            MRZData info = new MRZData();
            DateFormat df = new SimpleDateFormat("yyMMdd");
            String[] mrzArr = mrz.split("\n");
            info.setPassport(mrzArr[1].substring(0, 9));
            String birthDate = mrzArr[1].substring(13, 19);
            info.setDateOfBirth(df.parse(birthDate));
            String expiry = mrzArr[1].substring(21, 27);
            info.setExpiredDate(df.parse(expiry));
            return info;
        }

        public static MRZData build(MRZRequest request) throws ParseException {
            MRZData info = new MRZData();
            DateFormat df2 = new SimpleDateFormat("yyMMdd");
            String[] birthDateArr = request.getDateOfBirth().split("\\.");
            String birthDate = birthDateArr[2].substring(2, 4).concat(birthDateArr[1]).concat(birthDateArr[0]);
            String[] expiryDateArr = request.getDateOfExpiry().split("\\.");
            String expiryDate = expiryDateArr[2].substring(2, 4).concat(expiryDateArr[1]).concat(expiryDateArr[0]);
            info.setPassport(request.getPassportNumber());
            info.setDateOfBirth(df2.parse(birthDate));
            info.setExpiredDate(df2.parse(expiryDate));
            return info;
        }
    }

}
