package uz.salvadore.passport.socketclient.service;

import uz.asbt.passport.jmrtdlibrary.model.DeviceInfo;

public interface DeviceService {
  DeviceInfo getDeviceInfo(String scanPort);
}
