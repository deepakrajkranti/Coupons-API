Monk Commerce - Coupons API

This project implements a Coupon Management System for an e-commerce platform using Spring Boot 3, Spring Data JPA, Lombok, and Strategy Pattern.

It exposes REST APIs for creating, retrieving, updating, deleting, and applying coupons.
Swagger UI is enabled for easy testing.

 **Features Implemented**

Cart-wise Coupons

Discount applies when total cart value exceeds a threshold.

Example: 10% off if cart total > 100.

Product-wise Coupons

Discount applies on specific product(s).

Example: 20% off on product with ID = 1.

BxGy (Buy X Get Y) Coupons

Buy specified products (with quantity), get other products free.

Example: Buy 2 of product 1 → Get 1 of product 3 free (max 2 times).

❌ Unimplemented Cases

Multiple coupons stacking together.

Category-based coupons (e.g., 15% off all Electronics).

User-specific or one-time-use coupons.

Auto-adding free products in BxGy if not in the cart.

⚠️ Limitations

Coupon rules stored as JSON string (detailsJson).

Only one coupon is applied per calculation request.

Expiry date stored but not enforced strictly.

No concurrency/user-level redemption tracking.

📌 Assumptions

Cart items provided in request always valid.

Prices are tax-inclusive and final.

Coupons can be created/updated/deleted only via API (no admin UI).

repetitionLimit applies globally for each coupon.

🔗 API Endpoints

Base URL: http://localhost:8080/api/coupons

1. Create Coupon

POST /api/coupons

Request Body (Cart-wise):

`
{
"type": "CART_WISE",
"detailsJson": "{ \"threshold\": 100, \"discount\": 10 }",
"repetitionLimit": 1,
"expiryDate": "2025-12-31"
}
`

Request Body (Product-wise):
`
{
"type": "PRODUCT_WISE",
"detailsJson": "{ \"product_id\": 1, \"discount\": 20 }",
"repetitionLimit": 1,
"expiryDate": "2025-12-31"
}`


Request Body (BxGy):

`{
"type": "BXGY",
"detailsJson": "{ \"buy_products\": [{ \"product_id\": 1, \"quantity\": 2 }], \"get_products\": [{ \"product_id\": 3, \"quantity\": 1 }], \"repetition_limit\": 2 }",
"repetitionLimit": 2,
"expiryDate": "2025-12-31"
}
`
2. Get All Coupons

GET /api/coupons

3. Get Coupon by ID

GET /api/coupons/{id}

4. Update Coupon

PUT /api/coupons/{id}

(Same request body format as POST /api/coupons)

5. Delete Coupon

DELETE /api/coupons/{id}

6. Calculate Discount for a Coupon

POST /api/coupons/calculate/{id}

Request:
`
{
"items": [
{ "productId": 1, "quantity": 2, "price": 60 },
{ "productId": 2, "quantity": 1, "price": 50 }
]
}`


Response:

{
"coupon_id": 1,
"discount": 12.0
}

7. Get Applicable Coupons for Cart

POST /api/coupons/applicable-coupons

Request:

{
"items": [
{ "productId": 1, "quantity": 6, "price": 50 },
{ "productId": 2, "quantity": 3, "price": 30 },
{ "productId": 3, "quantity": 2, "price": 25 }
]
}


Response:

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

📖 Swagger UI

Once the app is running, open:
👉 http://localhost:8080/swagger-ui.html

From here, you can test all endpoints directly in your browser.

▶️ How to Run
mvn clean install
mvn spring-boot:run


Swagger: http://localhost:8080/swagger-ui.html

H2 Console: http://localhost:8080/h2-console

JDBC URL: jdbc:h2:mem:testdb

User: sa

Password: (empty)