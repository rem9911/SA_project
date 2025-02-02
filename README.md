# 📚 Library Management System

## Overview
This system is a microservices-based **Library Management System** designed to handle books, loans, and notifications efficiently. It uses **Kafka** for event-driven communication and follows best software design principles, including:
- **Adapter Pattern** in Book Service
- **Strategy Pattern** in Loan Service 
- **Observer Pattern** in Notification Service
- **Singleton Pattern** for a common logging mechanism

## 🏗️ Architecture
The system consists of three microservices:

- **Book Service** 📚 - Manages books and availability
- **Loan Service** 🏦 - Handles borrowing and returning books
- **Notification Service** 🔔 - Sends email/SMS notifications

Each microservice runs independently and communicates via **Kafka events**, ensuring scalability and decoupling.

## 🏛️ Microservices Breakdown

### 1️⃣ Book Service (Adapter Pattern)
📌 **Purpose:**  
- Manage books (Create, Read, Update, Delete)
- Update book availability
- Publish book-related events via Kafka

🔹 **Key Components:**
- `BookController.java` (API Endpoints)
- `BookService.java` (Business Logic)
- `KafkaBookPublisher.java` (Adapter for Kafka communication)

  
  ![image](https://github.com/user-attachments/assets/3b8b1ad1-712f-44d1-ad88-19d009c1c26a)


### 2️⃣ Loan Service (Strategy Pattern)
📌 **Purpose:**  
- Handle book lending operations
- Select loan strategy based on loan type (Standard, Premium, Student)
- Publish loan-related events via Kafka

🔹 **Key Components:**
- `LoanController.java` (API Endpoints)
- `LoanService.java` (Business Logic)
- `StandardLoanStrategy.java`, `PremiumLoanStrategy.java`, `StudentLoanStrategy.java` (Loan Strategies)

  
![image](https://github.com/user-attachments/assets/dc6d0a44-400e-46ce-a0ea-669ac752d99c)

### 3️⃣ Notification Service (Observer Pattern)
📌 **Purpose:**  
- Listen for book and loan events via Kafka
- Notify users via Email and SMS

🔹 **Key Components:**
- `EventConsumer.java` (Kafka Event Listener)
- `NotificationService.java` (Observer Pattern Implementation)
- `EmailNotification.java`, `SMSNotification.java` (Concrete Observers)

  
![image](https://github.com/user-attachments/assets/86de6b93-5f7b-40de-90de-72859535561a)



## 🔄 Data Flow & Interactions
1️⃣ **BookService** receives book creation/update requests → Stores in **PostgreSQL** → Sends event to Kafka.  

2️⃣ **LoanService** processes loan requests → Validates book availability → Updates database → Sends event to Kafka.  

3️⃣ **NotificationService** consumes Kafka events → Notifies users via Email/SMS.  

📌 *Refer to the system diagram for a visual representation of interactions.*  

![Capturepng](https://github.com/user-attachments/assets/b07a43e1-6c29-41cf-9367-003a846f7581)


## 📖 API Documentation
Access **Swagger API Docs**:
- **Book Service:** `http://localhost:8080/swagger-ui.html`
- **Loan Service:** `http://localhost:8081/swagger-ui.html`
- **Notification Service:** `http://localhost:8082/swagger-ui.html`

## 🛠️ Deployment & Setup
run : docker run -d --name dev -e POSTGRES_USER=dev -e POSTGRES_PASSWORD=dev -e POSTGRES_DB=dev -p 5432:5432 postgres

docker compose up (or run the docker configuration file)

Run all 3 applications 
## 🎨 Frontend

A lightweight frontend is provided for seamless interaction with the system.

### 📌 Features:
- **Book Management**: 📚 View, add, update, and delete books.
- **Loan Management**: 🔄 Borrow and return books using different loan types.
- **Notifications**: 🔔 Displays real-time notifications when books or loans are updated.
### 🚀 Running the Frontend
1. **Ensure backend services are running** 
2. Open **`index.html`** and **`notifications.html`** in a browser.

## 🎯 Key Design Decisions

### ✅ **1. PostgreSQL Database**
- Ensures **data integrity** and **high availability**.
- Supports **complex transactions** required for library operations.
- ![image](https://github.com/user-attachments/assets/057d9196-f453-4257-9001-31c55fd5b4af)
![image](https://github.com/user-attachments/assets/a4895272-bb0a-45d0-a672-5dca659c6792)
![image](https://github.com/user-attachments/assets/ab7b70db-599d-43c6-b6c6-1feaf0555cb4)

### ✅ **2. Adapter Pattern (Book Service)**
- Decouples Kafka logic from core business processes.
- `KafkaBookPublisher.java` abstracts message sending.

### ✅ **3. Strategy Pattern (Loan Service)**
- Allows **dynamic selection** of loan types.
- Customizable loan durations based on **Standard, Premium, or Student** categories.

### ✅ **4. Observer Pattern (Notification Service)**
- Enables **extensible notifications** with multiple delivery channels.
- `NotificationService.java` dynamically manages observers.

### ✅ **5. Singleton Pattern (Logger Service)**
- Ensures **consistent logging** across services.
- Prevents unnecessary instantiation of logging components.

## 🚀 Future Enhancements
- **User Authentication & Role Management**
- **Admin Dashboard for Loan Insights**
- **Enhanced UI/UX for a Better Experience**

## 💡 Contributors
Birzaneanu Rares

Deara Bianca

Godin Rémy

Purcarus Raluca

Zaharia Diana

