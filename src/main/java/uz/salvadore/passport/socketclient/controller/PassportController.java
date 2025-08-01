package uz.salvadore.passport.socketclient.controller;

import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import uz.asbt.passport.jmrtdlibrary.model.DeviceInfo;
import uz.asbt.passport.jmrtdlibrary.model.rest.MRZRequest;
import uz.asbt.passport.jmrtdlibrary.model.rest.Response;
import uz.salvadore.passport.socketclient.model.Passport;
import uz.salvadore.passport.socketclient.service.DeviceService;
import uz.salvadore.passport.socketclient.service.PassportService;

import static uz.salvadore.passport.socketclient.valueobject.Constant.OK_MESSAGE;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/passport/")
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class PassportController {

  PassportService passportService;
  DeviceService deviceService;

  @GetMapping(value = "info/elyctis/{scanPort}")
  public ResponseEntity<Response<Passport>> info(@PathVariable String scanPort) {
    return ResponseEntity.ok(Response.<Passport>builder()
      .code(HttpStatus.OK.value())
      .message(OK_MESSAGE)
      .response(passportService.scanPassport(scanPort))
      .build());
  }

  @GetMapping(value = "device/{scanPort}")
  public ResponseEntity<Response<DeviceInfo>> deviceInfo(@PathVariable String scanPort) {
    return ResponseEntity.ok(Response.<DeviceInfo>builder()
      .code(HttpStatus.OK.value())
      .message(OK_MESSAGE)
      .response(deviceService.getDeviceInfo(scanPort))
      .build());
  }

  @PostMapping(value = "info/{scanPort}/{terminalName}")
  public ResponseEntity<Response<Passport>> info(@RequestBody MRZRequest request,
                                                 @PathVariable String scanPort,
                                                 @PathVariable String terminalName) {
    return ResponseEntity.ok(Response.<Passport>builder()
        .code(HttpStatus.OK.value())
        .message(OK_MESSAGE)
        .response(passportService.scanPassport(scanPort, terminalName, request))
      .build());
  }

}
