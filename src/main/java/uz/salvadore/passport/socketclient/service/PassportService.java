package uz.salvadore.passport.socketclient.service;

import uz.asbt.passport.jmrtdlibrary.model.rest.MRZRequest;
import uz.salvadore.passport.socketclient.model.Passport;

public interface PassportService {
  Passport scanPassport(String scanPort);
  Passport scanPassport(String scanPort, String terminalName, MRZRequest mrz);
}
