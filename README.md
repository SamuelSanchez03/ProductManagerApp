# Android Product Manager App

This document explains the current structure of the **Product Manager App** Android project, focusing on the backend integration with the FastAPI service and the app's internal architecture.  

---

## Table of Contents
- [Android Product Manager App](#android-product-manager-app)
  - [Table of Contents](#table-of-contents)
  - [Project Purpose](#project-purpose)
  - [Backend Repository](#backend-repository)
    - [Local Development Setup](#local-development-setup)
  - [Architecture Overview](#architecture-overview)
  - [Package Structure](#package-structure)
  - [Class Responsibilities](#class-responsibilities)
    - [**1. data/model/**](#1-datamodel)
    - [**2. data/network/**](#2-datanetwork)
    - [**3. data/repository/**](#3-datarepository)
    - [**4. ui/main/**](#4-uimain)
  - [API Integration](#api-integration)
  - [Data Flow](#data-flow)
  - [How to Use the Existing Components](#how-to-use-the-existing-components)
  - [What Remains to Be Implemented (Frontend)](#what-remains-to-be-implemented-frontend)
  - [Additional Notes](#additional-notes)

---

## Project Purpose

This Android project connects to a **FastAPI + MySQL REST API**.  
Its purpose is to manage products in an inventory: create, list, and delete products.  
The current implementation provides the **data layer (repository, API calls, ViewModel)**, while the **frontend (UI and interaction)** is still pending.

---

## Backend Repository

This Android project connects to an external REST API built with **FastAPI** and **MySQL**, which is located in a separate repository: [ProductManagerAPI (GitHub)](https://github.com/SamuelSanchez03/ProductManagerAPI)

### Local Development Setup

The Android app communicates with the backend through: 
`http://10.0.2.2:8000/`

(`10.0.2.2` allows the Android emulator to access the local machine running the FastAPI server.)

Make sure to:
1. Clone and set up the backend repository (`ProductManagerAPI`).
2. Run the **Docker Compose** stack to start both the API and MySQL containers.
3. Verify that `http://localhost:8000/docs` works before testing the Android app.

---
## Architecture Overview

The app follows the **MVVM (Model–View–ViewModel)** pattern, providing separation between UI, logic, and data management.

```
UI (Activity / Fragment)
        ↓
   ViewModel
        ↓
   Repository
        ↓
   Retrofit API (FastAPI backend)
```

This structure ensures:
- A clean separation of concerns.
- Easier maintenance and testing.
- Automatic updates via LiveData observers.

---

## Package Structure

```
com.up.productmanager/
├── data/
│   ├── model/
│   │   ├── Product.kt
│   │   └── ProductCreate.kt
│   ├── network/
│   │   ├── ProductApi.kt
│   │   └── RetrofitClient.kt
│   └── repository/
│       └── ProductRepository.kt
└── ui/
    └── main/
        ├── ProductViewModel.kt
        └── MainActivity.kt
```

---

## Class Responsibilities

### **1. data/model/**

- **Product.kt**  
  Represents a product as returned by the backend API.  
  Contains:
  ```kotlin
  val id: Int
  val nombre: String
  val precio: Double
  val cantidad_disponible: Int
  ```

- **ProductCreate.kt**  
  Represents the request body for creating a product (no ID).  
  ```kotlin
  val nombre: String
  val precio: Double
  val cantidad_disponible: Int
  ```

---

### **2. data/network/**

- **RetrofitClient.kt**  
  Configures and exposes the Retrofit instance:
  ```kotlin
  object RetrofitClient {
      private const val BASE_URL = "http://10.0.2.2:8000/"
      val api: ProductApi by lazy { ... }
  }
  ```

- **ProductoApi.kt**  
  Defines all HTTP methods connected to the FastAPI service:
  ```kotlin
    @GET("productos")
    fun getProducts(): Call<List<Product>>

    @POST("productos")
    fun createProduct(@Body producto: ProductCreate): Call<Product>

    @DELETE("productos/{id}")
    fun deleteProduct(@Path("id") id: Int): Call<Void>
  ```

---

### **3. data/repository/**

- **ProductRepository.kt**  
  Acts as a single source of truth between the ViewModel and API.  
  Uses Retrofit to perform operations and exposes LiveData to the ViewModel.

  ```kotlin
  fun getAllProducts()
  fun createNewProduct (product: ProductCreate)
  fun deleteProductByID(id: Int)
  ```

  Each function updates LiveData objects such as `products` and `message`.

---

### **4. ui/main/**

- **ProductoViewModel.kt**  
  Connects the Repository to the UI.  
  Exposes LiveData for product lists and messages.  
  Functions available:
  ```kotlin
  fun loadProducts()
  fun createProduct(product: ProductCreate)
  fun deleteProduct(id: Int)
  ```

- **MainActivity.kt**  
  Current version serves as a test interface.  
  It loads the list of products from the ViewModel and performs basic operations (create, delete).  
  This will later be replaced by a proper RecyclerView-based UI.

---

## API Integration

The backend API (FastAPI) provides the following endpoints:

| Method | Endpoint | Description |
|--------|-----------|-------------|
| GET | `/productos` | Retrieve all products |
| POST | `/productos` | Create a new product |
| DELETE | `/productos/{id}` | Delete a product by ID |


---

## Data Flow

1. **MainActivity** requests data from the **ViewModel**.
2. The **ViewModel** calls functions from **ProductoRepository**.
3. The **Repository** makes HTTP requests using **RetrofitClient** and **ProductoApi**.
4. The API responses are posted to LiveData objects.
5. The UI observes LiveData and updates automatically when data changes.

---

## How to Use the Existing Components

1. **To fetch products:**
   ```kotlin
   viewModel.cargarProductos()
   ```

2. **To create a product:**
   ```kotlin
   val newProduct = ProductCreate("Mouse", 249.99, 10)
   viewModel.createProduct(newProduct)
   ```

3. **To delete a product:**
   ```kotlin
   viewModel.deleteProduct(id)
   ```

4. **To observe changes:**
   ```kotlin
   viewModel.products.observe(this) { list -> ... }
   viewModel.message.observe(this) { msg -> ... }
   ```

---

## What Remains to Be Implemented (Frontend)

The backend integration is complete.  
Frontend tasks remaining:

1. **Product List Screen (RecyclerView)**
   - Display each product with name, price, and available quantity.
   - Include a checkbox next to each product for multi-selection.
   - Add a button “Delete Selected” that removes all checked items.

2. **Add Product Screen**
   - A new Activity to input product details:
     - Name (TextField)
     - Price (Numeric input)
     - Quantity (Numeric input)
   - Button “Save” that sends a POST request using `viewModel.createProduct()`.
   - After saving, return to the main list and refresh data.

3. **Confirmations & Feedback**
   - Show confirmation dialogs before deleting.
   - Show success/error messages from `viewModel.mensaje` using Toast.

4. **Navigation & Design**
   - Implement navigation between MainActivity and AddProductActivity.
   - Apply basic UI styling and consistent theme.

---

## Additional Notes

- The app uses **LiveData** for automatic updates between Repository → ViewModel → UI.
- Retrofit handles all HTTP requests asynchronously using `enqueue()`.
- The app supports development on Android 9+ with `network_security_config.xml` allowing cleartext HTTP for local testing (`10.0.2.2`).
