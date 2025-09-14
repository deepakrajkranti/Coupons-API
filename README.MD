# Monk Commerce - Coupons API

[![Java](https://img.shields.io/badge/Java-17+-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)
[![Build Status](https://img.shields.io/badge/Build-Passing-brightgreen.svg)]()

A robust and scalable **Coupon Management System** for e-commerce platforms built with **Spring Boot 3**, featuring multiple coupon types, strategic design patterns, and comprehensive REST APIs.

## 🚀 Features

### ✅ Implemented Coupon Types

| Coupon Type | Description | Example |
|-------------|-------------|---------|
| **Cart-wise** | Discount applies when total cart value exceeds threshold | 10% off if cart total > ₹100 |
| **Product-wise** | Discount applies on specific products | 20% off on Product ID: 1 |
| **BxGy (Buy X Get Y)** | Buy specified products, get others free | Buy 2 Product A → Get 1 Product B free |

### 🏗️ Architecture & Design

- **Strategy Pattern** implementation for different coupon calculation strategies
- **Spring Data JPA** for robust data persistence
- **RESTful API** design following best practices
- **Swagger/OpenAPI** documentation for easy testing
- **H2 In-Memory Database** for development and testing
- **Lombok** for reduced boilerplate code

## 📋 Table of Contents

- [Quick Start](#quick-start)
- [API Documentation](#api-documentation)
- [Code Structure](#code-structure)
- [Database Schema](#database-schema)
- [Testing](#testing)
- [Postman Collection](#postman-collection)
- [Limitations & Future Enhancements](#limitations--future-enhancements)

## 🏃‍♂️ Quick Start

### Prerequisites

- **Java 17+**
- **Maven 3.6+**
- **Git**

### Installation & Setup

```bash
# Clone the repository
git clone https://github.com/your-username/monk-commerce-coupons-api.git
cd monk-commerce-coupons-api

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run
```

### Access Points

| Service | URL | Credentials |
|---------|-----|-------------|
| **API Server** | http://localhost:8080 | - |
| **Swagger UI** | http://localhost:8080/swagger-ui.html | - |
| **H2 Console** | http://localhost:8080/h2-console | JDBC: `jdbc:h2:mem:testdb`<br>User: `sa`<br>Password: *(empty)* |

## 📚 API Documentation

### Base URL
```
http://localhost:8080/api/coupons
```

### 1. Create Coupon
```http
POST /api/coupons
Content-Type: application/json
```

#### Cart-wise Coupon
```json
{
  "type": "CART_WISE",
  "detailsJson": "{ \"threshold\": 100, \"discount\": 10 }",
  "repetitionLimit": 1,
  "expiryDate": "2025-12-31"
}
```

#### Product-wise Coupon
```json
{
  "type": "PRODUCT_WISE",
  "detailsJson": "{ \"product_id\": 1, \"discount\": 20 }",
  "repetitionLimit": 1,
  "expiryDate": "2025-12-31"
}
```

#### BxGy Coupon
```json
{
  "type": "BXGY",
  "detailsJson": "{ \"buy_products\": [{ \"product_id\": 1, \"quantity\": 2 }], \"get_products\": [{ \"product_id\": 3, \"quantity\": 1 }], \"repetition_limit\": 2 }",
  "repetitionLimit": 2,
  "expiryDate": "2025-12-31"
}
```

### 2. Retrieve Coupons

#### Get All Coupons
```http
GET /api/coupons
```

#### Get Coupon by ID
```http
GET /api/coupons/{id}
```

### 3. Update Coupon
```http
PUT /api/coupons/{id}
Content-Type: application/json
```
*Note: Use same request body format as Create Coupon*

### 4. Delete Coupon
```http
DELETE /api/coupons/{id}
```

### 5. Calculate Discount
```http
POST /api/coupons/calculate/{id}
Content-Type: application/json
```

**Request Body:**
```json
{
  "items": [
    { "productId": 1, "quantity": 2, "price": 60 },
    { "productId": 2, "quantity": 1, "price": 50 }
  ]
}
```

**Response:**
```json
{
  "coupon_id": 1,
  "discount": 12.0
}
```

### 6. Get Applicable Coupons
```http
POST /api/coupons/applicable-coupons
Content-Type: application/json
```

**Request Body:**
```json
{
  "items": [
    { "productId": 1, "quantity": 6, "price": 50 },
    { "productId": 2, "quantity": 3, "price": 30 },
    { "productId": 3, "quantity": 2, "price": 25 }
  ]
}
```

**Response:**
```json
{
  "applicable_coupons": [
    {
      "coupon_id": 1,
      "type": "CART_WISE",
      "discount": 40.0
    },
    {
      "coupon_id": 2,
      "type": "BXGY",
      "discount": 50.0
    }
  ]
}
```

## 🏗️ Code Structure

```
src/main/java/com/monkcommerce/coupons/
├── config/
│   └── SwaggerConfig.java          # API documentation configuration
├── controller/
│   └── CouponController.java       # REST API endpoints
├── dto/
│   ├── CartItemDto.java           # Cart item data transfer object
│   ├── CartDto.java               # Cart data transfer object
│   └── CouponDto.java             # Coupon data transfer object
├── entity/
│   └── Coupon.java                # JPA entity for coupon
├── repository/
│   └── CouponRepository.java      # Data access layer
├── service/
│   ├── CouponService.java         # Business logic layer
│   └── strategy/
│       ├── CouponStrategy.java    # Strategy interface
│       ├── CartWiseStrategy.java  # Cart-wise calculation
│       ├── ProductWiseStrategy.java # Product-wise calculation
│       └── BxGyStrategy.java      # BxGy calculation
└── CouponsApiApplication.java     # Main application class
```

### Key Components

#### Strategy Pattern Implementation
```java
public interface CouponStrategy {
    double calculateDiscount(Coupon coupon, CartDto cart);
    boolean isApplicable(Coupon coupon, CartDto cart);
}
```

#### Service Layer Architecture
- **CouponService**: Main business logic coordinator
- **Strategy Classes**: Individual coupon calculation logic
- **Repository Layer**: Data persistence abstraction

## 🗄️ Database Schema

### Coupon Entity
```sql
CREATE TABLE coupon (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    type VARCHAR(255) NOT NULL,
    details_json TEXT NOT NULL,
    repetition_limit INTEGER,
    expiry_date DATE,
    created_date TIMESTAMP,
    updated_date TIMESTAMP
);
```

### Supported Coupon Types
- `CART_WISE`
- `PRODUCT_WISE` 
- `BXGY`

## 🧪 Testing

### Manual Testing with Swagger
1. Start the application: `mvn spring-boot:run`
2. Navigate to: http://localhost:8080/swagger-ui.html
3. Test each endpoint directly in the browser

### Sample Test Data
```json
// Cart for testing
{
  "items": [
    { "productId": 1, "quantity": 3, "price": 100 },
    { "productId": 2, "quantity": 2, "price": 50 },
    { "productId": 3, "quantity": 1, "price": 75 }
  ]
}
```

## 📫 Postman Collection

Import the provided Postman collection to test all endpoints:

```json
{
  "info": {
    "name": "Monk Commerce - Coupons API",
    "_postman_id": "c5c47c42-9a3c-4f61-9d2e-b1a7f12a0abc",
    "description": "Complete API testing collection for Coupons Management System"
  }
}
```

### Quick Import Steps:
1. Open Postman
2. Click **Import** → **Raw Text**
3. Paste the collection JSON (provided in repository)
4. Click **Import**
5. Set base URL variable: `http://localhost:8080`

## ⚠️ Limitations & Future Enhancements

### Current Limitations
- ❌ **Single Coupon Application**: Only one coupon per transaction
- ❌ **No Coupon Stacking**: Multiple coupons cannot be combined
- ❌ **Category-based Coupons**: Not implemented
- ❌ **User-specific Coupons**: No user-level restrictions
- ❌ **Concurrency Control**: Limited multi-user support
- ❌ **Auto-add Free Products**: BxGy doesn't auto-add missing items

### 🚀 Planned Enhancements
- ✅ **Multi-coupon Support**: Stack compatible coupons
- ✅ **Category-based Discounts**: Discount by product categories
- ✅ **User-level Tracking**: One-time use and user-specific coupons
- ✅ **Advanced BxGy**: Auto-add free products to cart
- ✅ **Expiry Enforcement**: Strict date validation
- ✅ **Performance Optimization**: Caching and batch processing
- ✅ **Admin Dashboard**: Web UI for coupon management

## 🔧 Configuration

### Application Properties
```properties
# Server Configuration
server.port=8080

# Database Configuration
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA Configuration
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.show-sql=true

# H2 Console
spring.h2.console.enabled=true

# Swagger Configuration
springdoc.swagger-ui.path=/swagger-ui.html
```

## 🤝 Contributing

1. **Fork** the repository
2. Create your **feature branch**: `git checkout -b feature/amazing-feature`
3. **Commit** your changes: `git commit -m 'Add amazing feature'`
4. **Push** to the branch: `git push origin feature/amazing-feature`
5. Open a **Pull Request**

## 📄 License

This project is licensed under the **MIT License** - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Author

**Monk Commerce Development Team**
- Email: dev@monkcommerce.com
- GitHub: [@monkcommerce](https://github.com/monkcommerce)

## 🙏 Acknowledgments

- Spring Boot team for the excellent framework
- H2 Database for seamless development experience
- Swagger team for API documentation tools
- Open source community for continuous inspiration

---

**⭐ If you find this project helpful, please give it a star!**

For more information, visit our [Documentation](https://docs.monkcommerce.com) or [API Reference](https://api.monkcommerce.com/docs).
