package uz.salvadore.passport.socketclient.controller;

import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.asbt.passport.jmrtdlibrary.exception.JMRTDException;
import uz.asbt.passport.jmrtdlibrary.model.DeviceInfo;
import uz.asbt.passport.jmrtdlibrary.model.PassportData;
import uz.asbt.passport.jmrtdlibrary.model.rest.MRZRequest;
import uz.asbt.passport.jmrtdlibrary.model.rest.Response;
import uz.asbt.passport.jmrtdlibrary.service.Scanner;
import uz.asbt.passport.jmrtdlibrary.service.impl.ScannerImpl;
import uz.salvadore.passport.socketclient.model.Passport;

@Slf4j
@RestController
@RequestMapping("/passport/")
public class PassportController {

    @RequestMapping(value = "info/elyctis", method = RequestMethod.GET)
    public ResponseEntity<Response<Passport>> info() {
        Response<Passport> response = new Response<>();
        try {
            Passport passport;
            try (Scanner scanner = new ScannerImpl()) {
                passport = Passport.Builder.build(scanner.scan(null));
            }
            if (null == passport)
                throw new Exception("Can't read passport data");
            response.setCode(HttpStatus.OK.value());
            response.setMessage("OK");
            response.setResponse(passport);
        } catch (SerialPortException ex) {
            log.error(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Can't opened or close COM port");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "device", method = RequestMethod.GET)
    public ResponseEntity<Response<DeviceInfo>> deviceInfo() {
        Response<DeviceInfo> response = new Response<>();
        try {
            DeviceInfo deviceInfo;
            try (Scanner scanner = new ScannerImpl()){
                deviceInfo = scanner.info();
            }
            response.setCode(0);
            response.setMessage("OK");
            response.setResponse(deviceInfo);
        } catch (JMRTDException ex) {
            log.error(ex.getMessage());
            response.setCode(ex.getCode());
            response.setMessage(ex.getMessage());
        } catch (Exception ex) {
            log.error(ex.getMessage());
            response.setCode(-1);
            response.setMessage(ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

    @RequestMapping(value = "info", method = RequestMethod.POST)
    public ResponseEntity<Response<Passport>> info(@RequestBody MRZRequest request) {
        Response<Passport> response = new Response<>();
        try {
            Passport passport;
            try (Scanner scanner = new ScannerImpl()) {
                passport = Passport.Builder.build(scanner.scan(request, null));
            }
            if (null == passport)
                throw new Exception("Can't read passport data");
            response.setCode(HttpStatus.OK.value());
            response.setMessage("OK");
            response.setResponse(passport);
        } catch (SerialPortException ex) {
            log.error(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage("Can't opened or close COM port");
        } catch (Exception ex) {
            log.error(ex.getMessage());
            response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
            response.setMessage(ex.getMessage());
        }
        return ResponseEntity.ok(response);
    }

}
