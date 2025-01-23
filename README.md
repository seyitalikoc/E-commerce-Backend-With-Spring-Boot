# E-commerce-Backend
My first Spring Boot E-Commerce Backend Application

## Table of Contents
- [About](#about)
- [Features](#features)
- [Getting Started](#getting-started)
- [Installation](#installation)
- [Usage](#usage)
- [Endpoints](#endpoints)
- [Technologies Used](#technologies-used)

## About

This API is a simple e-commerce service that allows users to manage their shopping carts, view product details, and place orders.

## Features

- User authentication
- Product management (CRUD operations)
- Cart management (add, remove items)

## Getting Started

Instructions for setting up your project locally.

### Prerequisites

- Java 17 or higher
- Maven
- MySQL

## Installation

Clone the repository:

```bash
git clone https://github.com/seyitalikoc/E-commerce-Backend-With-Spring-Boot.git
```

Navigate into the project folder:

```bash
cd your-project
```

Install dependencies (for Maven):
```bash
mvn install
```

## Configuration

- Set up your application.properties
- Configure the database connection:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/yourdb
spring.datasource.username=root
spring.datasource.password=yourpassword
```

## Usage
To run the API locally:
```bash
mvn spring-boot:run
```

Once the application is running, the API will be available at:
```arduino
http://localhost:8080
```

## Endpoints
### Auth Controller Endpoints
### 1. Login
- URL: /rest/api/auth/login
- Method: POST
  
#### Body Parameters

- email: (required) user email (String)
- password: (required) user password (String)
  
#### Example Request
```bash
POST   http://localhost:8080/rest/api/auth/login
```
```json
{
       "email":"example@example.com",
       "password":"examplePassword"
}
```
#### Success Response 
```json
   {
    "result": true,
    "errorMessage": null,
    "data": {
          "firstName": "Seyit Ali",
          "lastName": "Koç",
          "email": "seyit@seyitkoc.com",
          "cart": {
            "productList": []
          },
          "token": "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJzZXlpdEBzZXlpdGtvYy5jb20iLCJpYXQiOjE3Mzc0NTk1MzIsImV4cCI6MTczNzQ2MDEzMn0.sgqZ3ljRcM3J7tJ2hw7JjsDC6G6xyjhBXRR8kuozrVQ"
      }
    }
```
   
#### Error Response
```json
   {
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/auth/login",
            "createTime": "2025-01-21T11:38:05.017+00:00",
            "message": "Yanlış email veya şifre. : "
        }
    },
    "data": null
    }
```

### Cart Controller Endpoints
### 1. Add Product to Cart
- URL: /rest/api/cart/addToCart
- Method: PUT
  
#### Query Parameters
- itemId : (required) product id that want to add cart (Long)
- userEmail : (required) user email (String)
  
#### Example Request
```bash
PUT   http://localhost:8080/rest/api/cart/addToCart?itemId=1&userEmail=seyit@seyitkoc.com
```
#### Success Response
```json
{
      "result": true,
      "errorMessage": null,
      "data": {
          "productList": [
              {
                  "id": 1,
                  "productName": "Product 1",
                  "description": "Description for Product 1",
                  "price": 19.99,
                  "subCategory": {
                      "id": 0,
                      "categoryName": "bilgisayar_tablet"
                  }
              }
          ]
       }
}
```
   
#### Error Response
```json
{
      "result": false,
      "errorMessage": {
          "status": 400,
          "exception": {
            "hostName": "seyit",
            "path": "/rest/api/cart/addToCart",
            "createTime": "2025-01-21T11:48:38.815+00:00",
            "message": "Kayıt bulunamadı. : Product already in cart"
          }
      },
      "data": null
}
```

### 2. Remove Product from Cart
- URL: /rest/api/cart/removeFromCart
- Method: PUT
  
#### Query Parameters
- itemId : (required) product id that want to add cart (Long)
- userEmail : (required) user email (String)
  
#### Example Request
```bash
PUT   http://localhost:8080/rest/api/cart/addToCart?itemId=1&userEmail=seyit@seyitkoc.com
```
#### Success Response
```json
{
      "result": true,
      "errorMessage": null,
      "data": {
          "productList": []
      }
}
```
   
#### Error Response
```json
   {
      "result": false,
      "errorMessage": {
          "status": 400,
          "exception": {
            "hostName": "seyit",
            "path": "/rest/api/cart/removeFromCart",
            "createTime": "2025-01-21T11:52:19.484+00:00",
            "message": "Kayıt bulunamadı. : Product not in cart"
          }
      },
      "data": null
    }
```

### Category Controller Endpoints
### 1. Get SubCategory List From MainCategory Name
- URL: /rest/api/category/getSubCategories
- Method: GET
  
#### Query Parameters
- category : (required) MainCategory Name (String)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/category/getSubCategories?category=elektronik
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": [
        {
            "id": 0,
            "categoryName": "bilgisayar_tablet",
            "mainCategory": {
                "categoryName": "elektronik"
            }
        },
        {
            "id": 0,
            "categoryName": "yazıcılar_projeksiyon",
            "mainCategory": {
                "categoryName": "elektronik"
            }
        },
        {
            "id": 0,
            "categoryName": "telefon_tekefon-aksesuarlari",
            "mainCategory": {
                "categoryName": "elektronik"
            }
        }
    ]
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/category/getSubCategories",
            "createTime": "2025-01-21T12:00:48.571+00:00",
            "message": "Kayıt bulunamadı. : No main category found with name: market"
        }
    },
    "data": null
}
```

### 2. Get SubCategory From SubCategory Name
- URL: /rest/api/category/get
- Method: GET
  
#### Query Parameters
- category : (required) SubCategory Name (String)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/category/get?category=elektronik
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "id": 0,
        "categoryName": "bilgisayar_tablet",
        "mainCategory": {
            "categoryName": "elektronik"
        }
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/category/get",
            "createTime": "2025-01-21T12:02:35.200+00:00",
            "message": "Kayıt bulunamadı. : No sub category found with name: bilgisayar_laptop"
        }
    },
    "data": null
}
```

### Product Controller Endpoints
### 1. Get Product List From MainCategory or SubCategory Name
- URL: /rest/api/product/getList
- Method: GET
  
#### Query Parameters
- mainCat : MainCategory Name (String)
- subCat : SubCategory Name (String)
- page : Page number (Integer)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/product/getList
```
```bash
GET   http://localhost:8080/rest/api/product/getList?mainCat=elektronik
```
```bash
GET   http://localhost:8080/rest/api/product/getList?subCat=bilgisayar_tablet
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "content": [
            {
                "id": 1,
                "productName": "Product 1",
                "description": "Description for Product 1",
                "price": 19.99,
                "subCategory": {
                    "id": 0,
                    "categoryName": "bilgisayar_tablet"
                }
            },
            {
                "id": 2,
                "productName": "Product 2",
                "description": "Description for Product 2",
                "price": 29.99,
                "subCategory": {
                    "id": 0,
                    "categoryName": "bilgisayar_tablet"
                }
            },
            ...
        }
      ]
    "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": false,
        "totalPages": 6,
        "totalElements": 54,
        "size": 10,
        "number": 0,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "first": true,
        "numberOfElements": 10,
        "empty": false
  }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/getList",
            "createTime": "2025-01-21T12:07:17.326+00:00",
            "message": "Kayıt bulunamadı. : bilgisayar_telefon"
        }
    },
    "data": null
}
```

### 2. Search Keyword in Product Table
- URL: /rest/api/product/search
- Method: GET
  
#### Query Parameters
- q : Searching keyword (String)
- page : Page number (Integer)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/product/search?q=product 12
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "content": [
            {
                "id": 12,
                "productName": "Product 12",
                "description": "Description for Product 12",
                "price": 129.99,
                "subCategory": {
                    "id": 0,
                    "categoryName": "ayakkabi"
                }
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": {
                "empty": true,
                "sorted": false,
                "unsorted": true
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 1,
        "size": 10,
        "number": 0,
        "sort": {
            "empty": true,
            "sorted": false,
            "unsorted": true
        },
        "first": true,
        "numberOfElements": 1,
        "empty": false
    }
}
```
   
#### Error Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": []
}
```

### 3. Filter Products By Price, MainCategory or SubCategory and Sort
- URL: /rest/api/product/filter
- Method: GET
  
#### Query Parameters
- mainCat : MainCategory Name (String)
- subCat : SubCategory Name (String)
- price_min : Min Price (Double)
- price_max : Max Price (Double)
- sort : (required) Sorting type (String)
- page : Page number (Integer)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/product/filter?mainCat=elektronik&price_min=10&price_max=30&sort=price_desc
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "content": [
            {
                "id": 2,
                "productName": "Product 2",
                "description": "Description for Product 2",
                "price": 29.99,
                "subCategory": {
                    "id": 0,
                    "categoryName": "bilgisayar_tablet"
                }
            },
            {
                "id": 1,
                "productName": "Product 1",
                "description": "Description for Product 1",
                "price": 19.99,
                "subCategory": {
                    "id": 0,
                    "categoryName": "bilgisayar_tablet"
                }
            }
        ],
        "pageable": {
            "pageNumber": 0,
            "pageSize": 10,
            "sort": {
                "empty": false,
                "sorted": true,
                "unsorted": false
            },
            "offset": 0,
            "paged": true,
            "unpaged": false
        },
        "last": true,
        "totalPages": 1,
        "totalElements": 2,
        "size": 10,
        "number": 0,
        "sort": {
            "empty": false,
            "sorted": true,
            "unsorted": false
        },
        "first": true,
        "numberOfElements": 2,
        "empty": false
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/filter",
            "createTime": "2025-01-21T12:15:45.478+00:00",
            "message": "Kayıt bulunamadı. : bilgisayar"
        }
    },
    "data": null
}
```

### 4. Get Product By Name or Id
- URL: /rest/api/product/get
- Method: GET
  
#### Query Parameters
- id : Product Id (Long)
- name : Product Name (String)
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/product/get?id=1
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "id": 1,
        "productName": "Product 1",
        "description": "Description for Product 1",
        "price": 19.99,
        "subCategory": {
            "id": 0,
            "categoryName": "bilgisayar_tablet"
        }
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/get",
            "createTime": "2025-01-21T12:17:52.700+00:00",
            "message": "Kayıt bulunamadı. : 0"
        }
    },
    "data": null
}
```

### 5. Save Product
- URL: /rest/api/product/save
- Method: POST
  
#### Body Parameters
- productName : Product Name (String)
- description : Product Description (String)
- price : Product Price (BigDecimal)
- subCategory : SubCategory Id (Long)
  
#### Example Request
```bash
POST   http://localhost:8080/rest/api/product/save
```
```json
{
    "productName":"example name",
    "description":"description description",
    "price": 199.99,
    "subCategory":1
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "id": 55,
        "productName": "example name",
        "description": "description description",
        "price": 199.99,
        "subCategory": {
            "id": 0,
            "categoryName": "bilgisayar_tablet"
        }
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/save",
            "createTime": "2025-01-21T12:22:05.866+00:00",
            "message": "Kayıt bulunamadı. : SubCategory with ID: 100"
        }
    },
    "data": null
}
```

### 6. Update Product
- URL: /rest/api/product/update
- Method: PUT

#### Query Parameters
- id : Product Id (Long)
  
#### Body Parameters
- productName : Product Name (String)
- description : Product Description (String)
- price : Product Price (BigDecimal)
- subCategory : SubCategory Id (Long)
  
#### Example Request
```bash
PUT   http://localhost:8080/rest/api/product/update?id=55
```
```json
{
    "productName":"example name2",
    "description":"description description2",
    "price": 199.99,
    "subCategory":2
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "id": 55,
        "productName": "example name2",
        "description": "description description2",
        "price": 199.99,
        "subCategory": {
            "id": 0,
            "categoryName": "yazıcılar_projeksiyon"
        }
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/update",
            "createTime": "2025-01-21T12:24:44.055+00:00",
            "message": "Kayıt bulunamadı. : 56"
        }
    },
    "data": null
}
```

### 6. Delete Product
- URL: /rest/api/product/save
- Method: DELETE

#### Query Parameters
- id : Product Id (Long)
  
#### Example Request
```bash
DELETE   http://localhost:8080/rest/api/product/delete?id=55
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": " product was deleted : 55"
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/product/delete",
            "createTime": "2025-01-21T12:26:00.940+00:00",
            "message": "Kayıt bulunamadı. : 55"
        }
    },
    "data": null
}
```

### User Controller Endpoints
### 1. Get User By Id or Email
- URL: /rest/api/user/
- Method: GET
  
#### Query Parameters
- id : User Id (String)
- email : User Email (String)

#### Header Parameters
- Authorization : JWt token to access endpoints.
  
#### Example Request
```bash
GET   http://localhost:8080/rest/api/user/?id=1000
```
```header
{
  "Authorization" : "example_token"
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "firstName": "Seyit Ali",
        "lastName": "Koç",
        "email": "seyit@seyitkoc.com",
        "cart": {
            "productList": []
        },
        "token": null
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/user",
            "createTime": "2025-01-21T12:32:01.094+00:00",
            "message": "Kayıt bulunamadı. : 1000"
        }
    },
    "data": null
}
```

### 2. Get User By Id or Email
- URL: /rest/api/user/save
- Method: POST

#### Body Parameters
- firstName : (required) User first name
- lastName : (required) User last name
- email : (required) User email
- password : (required) User password
  
#### Example Request
```bash
POST   http://localhost:8080/rest/api/user/save
```
```body
{
  "firstName": "example",
  "lastName": " lastname",
  "email": "example@seyitkoc.com",
  "password": "12345678"
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "firstName": "example",
        "lastName": " lastname",
        "email": "example@seyitkoc.com",
        "cart": {
            "productList": []
        },
        "token": null
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/user/save",
            "createTime": "2025-01-21T12:39:04.732+00:00",
            "message": "Genel bir hata oluştu. : Email exist."
        }
    },
    "data": null
}
```

### 3. Update User
- URL: /rest/api/user/update
- Method: PUT

#### Query Parameters
- email : User Email (String)

#### Header Parameters
- Authorization : JWt token to access endpoints.

#### Body Parameters
- firstName : (required) User first name
- lastName : (required) User last name
- email : (required) User email
- password : (required) User password
  
#### Example Request
```bash
PUT   http://localhost:8080/rest/api/user/update?email=example@seyitkoc.com
```
```header
{
  "Authorization" : "example_token"
}
```
```body
{
  "firstName": "example2",
  "lastName": " lastname2",
  "email": "example@seyitkoc.com",
  "password": "12345678"
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "firstName": "example2",
        "lastName": " lastname2",
        "email": "example@seyitkoc.com",
        "cart": {
            "productList": []
        },
        "token": null
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/user/update",
            "createTime": "2025-01-21T12:43:30.622+00:00",
            "message": "Kayıt bulunamadı. : example2@seyitkoc.com"
        }
    },
    "data": null
}
```

### 4. Update User Password
- URL: /rest/api/user/passwordUpdate
- Method: PUT

#### Query Parameters
- email : User Email (String)

#### Header Parameters
- Authorization : JWt token to access endpoints.

#### Body Parameters
- oldPassword : (required) User old password
- newPassword : (required) User new password
  
#### Example Request
```bash
PUT   http://localhost:8080/rest/api/user/update?email=example@seyitkoc.com
```
```header
{
  "Authorization" : "example_token"
}
```
```body
{
  "oldPassword": "12345678",
  "newPassword": "123456789"
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": {
        "firstName": "example2",
        "lastName": " lastname2",
        "email": "example@seyitkoc.com",
        "cart": {
            "productList": []
        },
        "token": null
    }
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/user/passwordUpdate",
            "createTime": "2025-01-21T12:50:34.867+00:00",
            "message": "Genel bir hata oluştu. : Old password is incorrect."
        }
    },
    "data": null
}
```

### 5. Delete User By Id
- URL: /rest/api/user/delete
- Method: DELETE

#### Query Parameters
- id : User Id (Long)

#### Header Parameters
- Authorization : JWt token to access endpoints.
 
#### Example Request
```bash
DELETE   http://localhost:8080/rest/api/user/delete?id=12
```
```header
{
  "Authorization" : "example_token"
}
```
#### Success Response
```json
{
    "result": true,
    "errorMessage": null,
    "data": "User deleted: 2"
}
```
   
#### Error Response
```json
{
    "result": false,
    "errorMessage": {
        "status": 400,
        "exception": {
            "hostName": "seyit",
            "path": "/rest/api/user/delete",
            "createTime": "2025-01-21T13:04:40.291+00:00",
            "message": "Kayıt bulunamadı. : 2"
        }
    },
    "data": null
}
```


## Technologies Used
List of technologies and frameworks used in the project:
- Spring Boot 3.4.1
- Maven
- Spring Data JPA
- MySQL
- Hibernate
- Lombok
- Spring Security
- JWT
