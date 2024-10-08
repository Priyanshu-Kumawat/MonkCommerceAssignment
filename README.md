# MonkCommerceAssignment

A Spring Boot application for managing and applying various types of coupons in an e-commerce platform. This project supports cart-wise, product-wise, and BxGy (Buy X, Get Y) coupon types. It is designed with extensibility in mind and includes robust error handling and unit tests to ensure functionality.

## CouponsSystem API

Developed a Coupons Management API with the following features:
- **Support for multiple coupon types**: Cart-wise, Product-wise, and BxGy.
- **CRUD operations**: Create, Retrieve, Update, and Delete coupons.
- **Coupon application**: Apply coupons to carts and calculate discounts.
- **Error handling**: Comprehensive error management for different scenarios.
- **Unit tests**: Ensuring reliability and correctness of functionality.

The project is backed by an in-memory database and detailed in the comprehensive README available in the public Git repository. Future features may include coupon expiration dates.

## API Endpoints

### 1. Create a New Coupon (Cart-wise)

- **Endpoint**: `POST /api/coupons`
- **Description**: Creates a new coupon.
- **Request Body**:
    ```json
    {
      "type": "cart-wise",
      "details": {
        "threshold": 100,
        "discount": 10
      }
    }
    ```
- **Response**:
  - **Success (201)**: The created coupon.
  - **Error (500)**: Internal server error.

### 2. Retrieve All Coupons

- **Endpoint**: `GET /api/coupons`
- **Description**: Retrieves a list of all coupons.
- **Response**:
  - **Success (200)**: List of coupons.
  - **Error (500)**: Internal server error.

### 3. Retrieve a Specific Coupon by Its ID

- **Endpoint**: `GET /api/coupons/{id}`
- **Description**: Retrieves a coupon by its ID.
- **Response**:
  - **Success (200)**: The requested coupon.
  - **Error (404)**: Coupon not found.

### 4. Update a Specific Coupon by Its ID

- **Endpoint**: `PUT /api/coupons/{id}`
- **Description**: Updates a coupon by its ID.
- **Request Body**:
    ```json
    {
      "type": "product-wise",
      "details": {
        "product_id": 1,
        "discount": 20
      }
    }
    ```
- **Response**:
  - **Success (200)**: The updated coupon.
  - **Error (404)**: Coupon not found.
  - **Error (500)**: Internal server error.

### 5. Delete a Specific Coupon by Its ID

- **Endpoint**: `DELETE /api/coupons/{id}`
- **Description**: Deletes a coupon by its ID.
- **Response**:
  - **Success (204)**: No content.
  - **Error (404)**: Coupon not found.
  - **Error (500)**: Internal server error.

### 6. Fetch All Applicable Coupons for a Given Cart

- **Endpoint**: `POST /api/coupons/applicable-coupons`
- **Description**: Retrieves all applicable coupons for a given cart and calculates the total discount.
- **Request Body**:
    ```json
    {
      "items": [
        {"product_id": 1, "quantity": 6, "price": 50},
        {"product_id": 2, "quantity": 3, "price": 30},
        {"product_id": 3, "quantity": 2, "price": 25}
      ]
    }
    ```
- **Response**:
  - **Success (200)**: List of applicable coupons with discounts.
  - **Error (500)**: Internal server error.

### 7. Apply a Specific Coupon to the Cart

- **Endpoint**: `POST /api/coupons/apply-coupon/{id}`
- **Description**: Applies a coupon to the cart and returns the updated cart with discounted prices.
- **Request Body**:
    ```json
    {
      "items": [
        {"product_id": 1, "quantity": 6, "price": 50},
        {"product_id": 2, "quantity": 3, "price": 30},
        {"product_id": 3, "quantity": 2, "price": 25}
      ]
    }
    ```
- **Response**:
  - **Success (200)**: Updated cart with discounted prices.
  - **Error (404)**: Coupon not found.
  - **Error (500)**: Internal server error.

## Implemented Cases

- **Cart-wise Coupons**:
  - **Discount**: 10% off on carts over Rs. 100
  - **Condition**: Cart total must be greater than Rs. 100.
  - **Discount**: 10% off the entire cart total.

- **Product-wise Coupons**:
  - **Discount**: 20% off on Product A
  - **Condition**: Product A must be in the cart.
  - **Discount**: 20% off the price of Product A.

- **BxGy Coupons**:
  - **Discount**: Buy 2 products from [X, Y, Z] and get 1 product from [A, B, C] free
  - **Condition**: Buy at least 2 products from the "buy" array to get 1 product from the "get" array free.
  - **Repetition Limit**: Coupon can be applied multiple times based on the repetition limit.

## Unimplemented Cases

- **Expiration Dates for Coupons**:
  - **Reason**: Not implemented due to time constraints.

- **Additional Coupon Types**:
  - **Reason**: New coupon types (e.g., seasonal discounts) are not implemented but can be added in the future.

## Limitations

- **No Expiration Dates**:
  - Coupons do not have expiration dates. Adding this feature would require additional fields and logic.

- **Basic Error Handling**:
  - Basic error handling is in place, but more detailed error responses and validations could be implemented.

- **Coupon Type Extension**:
  - Currently, the system supports only cart-wise, product-wise, and BxGy coupons. Future extensions may require changes to the existing structure.

## Future Extensibility

- **New Coupon Types**:
  - The current system is designed to be easily extensible. New coupon types can be added by introducing new classes and updating the service layer.

- **Expiration Dates**:
  - Adding expiration dates to coupons will require modifications to the coupon schema and additional logic in the service layer.

- **Advanced Discount Logic**:
  - More complex discount logic and rules can be implemented as needed, including tiered discounts and combination offers.

**NOTE**: A Postman Collection will also be added in the `CouponSystem` folder named as `PostmanCollection`.
