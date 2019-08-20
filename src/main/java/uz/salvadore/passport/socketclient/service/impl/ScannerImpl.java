package uz.salvadore.passport.socketclient.service.impl;

import jssc.*;
import lombok.extern.slf4j.Slf4j;
import net.sf.scuba.smartcards.CardService;
import net.sf.scuba.smartcards.CardServiceException;
import org.jmrtd.BACKey;
import org.jmrtd.PassportService;
import org.jmrtd.lds.LDSFileUtil;
import org.jmrtd.lds.icao.DG1File;
import org.jmrtd.lds.icao.DG2File;
import org.jmrtd.lds.iso19794.FaceImageInfo;
import org.jmrtd.lds.iso19794.FaceInfo;
import org.springframework.stereotype.Service;
import uz.salvadore.passport.socketclient.exception.CustomException;
import uz.salvadore.passport.socketclient.model.MRZData;
import uz.salvadore.passport.socketclient.model.PassportData;
import uz.salvadore.passport.socketclient.model.rest.MRZRequest;
import uz.salvadore.passport.socketclient.service.Scanner;

import javax.imageio.ImageIO;
import javax.smartcardio.CardException;
import javax.smartcardio.CardTerminal;
import javax.smartcardio.TerminalFactory;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;

@Slf4j
@Service
public class ScannerImpl implements Scanner {

    private SerialPort serialPort;
    private PassportData passportData = null;

    @Override
    public PassportData scan() throws Exception {
        try {
            Arrays.stream(SerialPortList.getPortNames()).forEach(log::info);
            String comPort = Arrays.stream(SerialPortList.getPortNames()).findFirst().orElse(null);
            serialPort = new SerialPort(comPort);
            if (!serialPort.isOpened()) {
                if(serialPort.openPort()) {
                    log.info("Port opened");
                    serialPort.setParams(
                            SerialPort.BAUDRATE_9600,
                            SerialPort.DATABITS_8,
                            SerialPort.STOPBITS_1,
                            SerialPort.PARITY_NONE);
                    serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN |
                            SerialPort.FLOWCONTROL_RTSCTS_OUT);
                    byte[] inquire = { 0x49, 0, 0 };
                    if (serialPort.writeBytes(inquire)) {
                        log.info("Write success");
                        byte[] response = serialPort.readBytes(92, 2000);
                        String mrz = readMrz((char) response[0], response);
                        passportData = passportData(mrz, null);
                    }
                }
            }
        } catch (Exception ex) {
            log.error(ex.getMessage());
        } finally {
            log.info("COM port closed!");
            serialPort.closePort();
        }
        return passportData;
    }

    @Override
    public PassportData scan(MRZRequest request) throws Exception {
        return passportData(null, request);
    }

    private PassportData passportData(String mrz, MRZRequest request) {
        PassportData passportData = null;
        PassportService passportService = null;
        try {
            log.info("MRZ string is {}", mrz);
            CardService cardService = CardService.getInstance(getTerminal());
            passportService = new PassportService(
                    cardService,
                    PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                    PassportService.DEFAULT_MAX_BLOCKSIZE,
                    true,
                    false
            );
            passportService.open();
            // Нужно чтобы при вызове метода doBAC не выбрасывало Exception
            passportService.sendSelectApplet(false);
            MRZData mrzData;
            if (null != mrz) {
                mrzData = MRZData.MRZBuilder.build(mrz);
            } else {
                mrzData = MRZData.MRZBuilder.build(request);
            }

            passportService.doBAC(getBAC(mrzData));

            DG1File dg1 = getDg1(passportService);
            DG2File dg2 = getDg2(passportService);

            passportData = PassportData.PassportBuilder.build(dg1.getMRZInfo());
            passportData.setPhoto(getPhoto(dg2));

        } catch (CustomException | CardException | CardServiceException | ParseException | IOException ex) {
            log.error(ex.getMessage());
        } finally {
            if (null != passportService)
                passportService.close();
        }
        return passportData;
    }

    private byte[] getPhoto(DG2File dg2) throws IOException {
        List<FaceInfo> infos = dg2.getFaceInfos();
        List<FaceImageInfo> imageInfos = infos.get(0).getFaceImageInfos();
        FaceImageInfo faceImageInfo = imageInfos.get(0);
        InputStream inputStream = faceImageInfo.getImageInputStream();
        BufferedImage image = ImageIO.read(inputStream);

        byte[] imageInByte;

        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            ImageIO.write(image, "jpg", baos);
            baos.flush();
            imageInByte = baos.toByteArray();
        }
        return imageInByte;
    }

    private DG1File getDg1(PassportService passportService) throws CardServiceException, IOException {
        InputStream is = passportService.getInputStream(PassportService.EF_DG1);
        return  (DG1File) LDSFileUtil.getLDSFile(PassportService.EF_DG1, is);
    }

    private DG2File getDg2(PassportService passportService) throws CardServiceException, IOException {
        InputStream is = passportService.getInputStream(PassportService.EF_DG2);
        return (DG2File) LDSFileUtil.getLDSFile(PassportService.EF_DG2, is);
    }

    private CardTerminal getTerminal() throws CustomException, CardException {
        TerminalFactory factory = TerminalFactory.getDefault();
        List<CardTerminal> terminals = factory.terminals().list();

        CardTerminal terminal = terminals.get(0);

        if (null == terminal)
            throw new CustomException(-1, "Terminal not found");
        return terminal;
    }

    private BACKey getBAC(MRZData mrzData) {
        return new BACKey(
                mrzData.getPassport(),
                mrzData.getDateOfBirth(),
                mrzData.getExpiredDate()
        );
    }

    private String readMrz(char cmd, byte[] response) throws SerialPortException {
        StringBuilder builder = new StringBuilder();
        switch (cmd) {
            case 'I':
                builder.append(new String(response));
                break;
            default:
                throw new SerialPortException("COM4", "Read data", "Error when read data");
        }
        return builder.toString().replace("\r", "\r\n");
    }

}
