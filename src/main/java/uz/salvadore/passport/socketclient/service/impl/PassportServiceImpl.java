package uz.salvadore.passport.socketclient.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.asbt.passport.jmrtdlibrary.model.rest.MRZRequest;
import uz.asbt.passport.jmrtdlibrary.service.Scanner;
import uz.asbt.passport.jmrtdlibrary.service.impl.ScannerImpl;
import uz.salvadore.passport.socketclient.exception.CustomException;
import uz.salvadore.passport.socketclient.model.Passport;
import uz.salvadore.passport.socketclient.service.PassportService;

@Slf4j
@Service
public class PassportServiceImpl implements PassportService {
  @Override
  public Passport scanPassport(String scanPort) {
    try (Scanner scanner = new ScannerImpl(scanPort)) {
      return Passport.Builder.build(scanner.scan());
    } catch (Exception ex) {
      throw new CustomException(100, "Can't read passport data");
    }
  }

  @Override
  public Passport scanPassport(String scanPort, String terminalName, MRZRequest mrz) {
    try (Scanner scanner = new ScannerImpl(scanPort)) {
      return Passport.Builder.build(scanner.scan(mrz, terminalName));
    } catch (Exception ex) {
      throw new CustomException(100, "Can't read passport data");
    }
  }
}
