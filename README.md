# Socket Client - Passport Reader Web Service

A Spring Boot web service for reading and processing passport data using JMRTD (Java Machine Readable Travel Documents) library. This application provides REST API endpoints for scanning passports through connected devices.

## Features

- **Passport Scanning**: Read passport data from connected devices via serial ports
- **Device Management**: Get information about connected passport reading devices
- **REST API**: RESTful endpoints for passport data retrieval
- **Swagger Documentation**: Interactive API documentation
- **Data Serialization**: Custom JSON serializers for date fields
- **Exception Handling**: Global exception handling with custom error responses

## Technology Stack

- **Java 8**
- **Spring Boot 2.1.4**
- **Maven** - Build tool
- **JMRTD Library** - Machine Readable Travel Documents processing
- **Swagger 2** - API documentation
- **Lombok** - Code generation
- **Jackson** - JSON processing
- **BouncyCastle** - Cryptographic operations

## Dependencies

Key dependencies include:
- `jmrtd` (0.7.12) - Core JMRTD functionality
- `jmrtd-library` (1.0.0) - Custom JMRTD library extension ([GitHub](https://github.com/Salvadore1987/jmrtdlibrary))
- `scuba-smartcards` & `scuba-sc-j2se` (0.0.18) - Smart card operations
- `jssc` (2.8.0) - Serial port communication
- `bcprov-jdk16` (1.46) - Cryptographic provider
- `jai-imageio-jpeg2000` (1.3.0) - Image processing

## API Endpoints

### Passport Operations
- `GET /api/v1/passport/info/elyctis/{scanPort}` - Scan passport using Elyctis device
- `POST /api/v1/passport/info/{scanPort}/{terminalName}` - Scan passport with MRZ data
- `GET /api/v1/passport/device/{scanPort}` - Get device information

### Response Format
All endpoints return responses in the following format:
```json
{
  "code": 200,
  "message": "OK",
  "response": {
    // Passport or device data
  }
}
```

## Configuration

### Application Properties
- **Server Port**: 8088
- **Base Path**: `/api/v1`
- **Swagger**: Enabled with custom configuration

### Swagger Configuration
Access the API documentation at: `http://localhost:8088/swagger-ui.html`

## Project Structure

```
src/main/java/uz/salvadore/passport/socketclient/
├── Application.java                    # Main Spring Boot application
├── config/                            # Configuration classes
│   ├── SwaggerConfig.java
│   └── SwaggerConfigProperties.java
├── controller/                        # REST controllers
│   └── PassportController.java
├── exception/                         # Custom exceptions
│   └── CustomException.java
├── handler/                          # Exception handlers
│   └── GlobalExceptionHandler.java
├── model/                            # Data models
│   └── Passport.java
├── serializer/                       # JSON serializers
│   ├── JsonDateDeserializer.java
│   └── JsonDateSerializer.java
├── service/                          # Business logic
│   ├── DeviceService.java
│   ├── PassportService.java
│   └── impl/
│       ├── DeviceServiceImpl.java
│       └── PassportServiceImpl.java
└── valueobject/                      # Constants and value objects
    └── Constant.java
```

## Building and Running

### Prerequisites
- Java 8 or higher
- Maven 3.6+

### Build
```bash
mvn clean compile
```

### Package
```bash
mvn clean package
```

### Run
```bash
java -jar target/socket-client.jar
```

Or using Maven:
```bash
mvn spring-boot:run
```

## Usage

1. **Start the application**
   ```bash
   java -jar target/socket-client.jar
   ```

2. **Access Swagger UI**
   Navigate to `http://localhost:8088/swagger-ui.html`

3. **Scan a passport**
   ```bash
   curl -X GET "http://localhost:8088/api/v1/passport/info/elyctis/COM3"
   ```

4. **Get device information**
   ```bash
   curl -X GET "http://localhost:8088/api/v1/passport/device/COM3"
   ```

## Data Model

The `Passport` model includes:
- Document information (code, issuing state, document number)
- Personal data (surname, name, nationality, gender, dates)
- MRZ lines (Machine Readable Zone)
- Photo data (byte array)

## Error Handling

The application includes:
- Global exception handler for consistent error responses
- Custom exceptions for business logic errors
- Proper HTTP status codes and error messages

## Version

Current version: **1.0.2**

## License

This project uses various open-source libraries. Please check individual dependency licenses for compliance requirements.