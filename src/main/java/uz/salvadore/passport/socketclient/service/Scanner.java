package uz.salvadore.passport.socketclient.service;

import uz.salvadore.passport.socketclient.model.PassportData;
import uz.salvadore.passport.socketclient.model.rest.MRZRequest;

public interface Scanner {
    PassportData scan() throws Exception;
    PassportData scan(MRZRequest request) throws Exception;
}
