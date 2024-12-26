
# Daraja Integration Project

This project demonstrates the integration of the M-Pesa Daraja API for seamless C2B (Customer to Business) and B2C (Business to Customer) transactions. It is built using Spring Boot, OkHttp, and adheres to best practices for secure and efficient API interaction. More implementaion still in construction.

---

## Features

- **Register URL:** Automates the process of registering validation and confirmation URLs for M-Pesa transactions.
- **C2B Integration:** Allows customers to make payments to a business via M-Pesa.
- **Validation and Confirmation Logic:** Implements endpoint handling for transaction validation and confirmation callbacks.
- **Access Token Management:** Securely fetches and manages access tokens from M-Pesa's API.
- **Custom DTOs and Configuration:** Utilizes DTOs and `application.yml` for structured data handling and configuration management.

---

## Tech Stack

- **Backend:** Spring Boot
- **HTTP Client:** OkHttp
- **JSON Processing:** Jackson
- **API Documentation:** Swagger (optional)
- **Logging:** SLF4J with Logback

---

## Prerequisites

- Java 17+
- Maven
- An M-Pesa Developer Account
- Postman (optional, for testing)
- Access to Daraja Sandbox Environment

---

## Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone https://github.com/your-username/daraja-integration.git
   cd daraja-integration
   ```

2. **Configure `application.yml`**
   Update the following details in the `src/main/resources/application.yml` file:
   ```yaml
   mpesa:
     base-url: https://sandbox.safaricom.co.ke
     consumer-key: YOUR_CONSUMER_KEY
     consumer-secret: YOUR_CONSUMER_SECRET
     short-code: YOUR_SHORT_CODE
     confirmation-url: https://yourdomain.com/confirmation
     validation-url: https://yourdomain.com/validation
     url-registration-endpoint: /mpesa/c2b/v1/registerurl
   ```

3. **Build and Run**
   Build and start the application:
   ```bash
   mvn clean install
   mvn spring-boot:run
   ```

4. **Test the Endpoints**
   Use Postman or a similar tool to test:
   - **Register URL:** `POST /api/daraja/register-url`
   - **Validation Endpoint:** `POST /api/daraja/validation`
   - **Confirmation Endpoint:** `POST /api/daraja/confirmation`

---

## Endpoints

### **Register URL**
- **Method:** `POST`
- **URL:** `/api/daraja/register-url`
- **Description:** Registers confirmation and validation URLs with the M-Pesa API.

### **Validation Endpoint**
- **Method:** `POST`
- **URL:** `/api/daraja/validation`
- **Description:** Handles validation of incoming transactions.

### **Confirmation Endpoint**
- **Method:** `POST`
- **URL:** `/api/daraja/confirmation`
- **Description:** Confirms successful transactions.

---

## Project Structure

```
src/
├── main/
│   ├── java/com/ian/daraja/
│   │   ├── controllers/       # API Controllers
│   │   ├── services/          # Business Logic Services
│   │   ├── dtos/              # Data Transfer Objects
│   │   ├── configs/           # Configuration Classes
│   │   ├── utils/             # Utility Classes
│   │   └── exceptions/        # Custom Exception Handling
│   ├── resources/
│       ├── application.yml    # Application Configuration
```

---

## Contributions

Contributions are welcome! Feel free to open issues or submit pull requests.

---

## License

This project is licensed under the [MIT License](LICENSE).

---
