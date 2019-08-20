package uz.salvadore.passport.socketclient.controller;

import jssc.SerialPortException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uz.salvadore.passport.socketclient.model.PassportData;
import uz.salvadore.passport.socketclient.model.rest.MRZRequest;
import uz.salvadore.passport.socketclient.model.rest.Response;
import uz.salvadore.passport.socketclient.service.Scanner;

@Slf4j
@RestController
@RequestMapping("/passport/")
public class PassportController {

    @Autowired
    private Scanner scanner;

    @RequestMapping(value = "info/", method = RequestMethod.GET)
    public ResponseEntity<Response<PassportData>> info() {
        Response<PassportData> response = new Response<>();
        try {
            PassportData passportData = scanner.scan();
            if (null == passportData)
                throw new Exception("Can't read passport data");
            response.setCode(HttpStatus.OK.value());
            response.setMessage("OK");
            response.setResponse(passportData);
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

    @RequestMapping(value = "info", method = RequestMethod.POST)
    public ResponseEntity<Response<PassportData>> info(@RequestBody MRZRequest request) {
        Response<PassportData> response = new Response<>();
        try {
            PassportData passportData = scanner.scan(request);
            if (null == passportData)
                throw new Exception("Can't read passport data");
            response.setCode(HttpStatus.OK.value());
            response.setMessage("OK");
            response.setResponse(passportData);
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
