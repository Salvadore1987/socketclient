package uz.salvadore.passport.socketclient.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import uz.asbt.passport.jmrtdlibrary.model.DeviceInfo;
import uz.asbt.passport.jmrtdlibrary.service.Scanner;
import uz.asbt.passport.jmrtdlibrary.service.impl.ScannerImpl;
import uz.salvadore.passport.socketclient.exception.CustomException;
import uz.salvadore.passport.socketclient.service.DeviceService;

@Slf4j
@Service
public class DeviceServiceImpl implements DeviceService {
  @Override
  public DeviceInfo getDeviceInfo(String scanPort) {
    try (Scanner scanner = new ScannerImpl(scanPort)) {
      return scanner.info();
    } catch (Exception ex) {
      throw new CustomException(101, "Can't get device info");
    }
  }
}
