# 🖨️ Midad (مِداد) — Educational Color Print Shop

A Full-Stack E-commerce platform built for selling colorful educational books and printouts to students in Egypt. It features a **Guest Checkout System** secured via a secret PIN, a comprehensive Admin Dashboard, and direct-to-browser file uploads powered by Cloudflare R2.

![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.3.4-6DB33F)
![Java](https://img.shields.io/badge/Java-17-orange)
![React](https://img.shields.io/badge/React-18-61DAFB)
![Tailwind](https://img.shields.io/badge/Tailwind-3-38B2AC)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-Neon-336791)
![Cloudflare R2](https://img.shields.io/badge/Storage-Cloudflare%20R2-F6821F)
![Railway](https://img.shields.io/badge/Hosting-Railway-0B0D0E)

---

## ✨ Features (المميزات)

### 🛒 Storefront (Guest Checkout — بدون تسجيل حساب)
- **Book Catalog:** Browse educational books with covers, live prices, and instant API fetching.
- **Persistent Cart:** Shopping cart saved in `localStorage` (persists after page refresh).
- **Guest Checkout:** Place orders without creating an account; generate a **secret 4-digit PIN** at checkout.
- **Order Tracking:** Track order progress using Order ID + Mobile number with a visual status timeline.
- **Self-Service Order Management:** Edit items/quantities or cancel orders directly using your secret PIN.

### 🔐 Admin Dashboard (لوحة التحكم)
- **Secure Auth:** JWT-based authentication for admins.
- **Real-Time Analytics:** Total order counters, confirmed vs. pending revenue, and top-selling books.
- **Book Management:** Full CRUD operations with **direct browser-to-R2 file uploads** featuring live upload progress bars.
- **Order Processing:** Update order states, inspect full details, and perform deletions.

### 🛡️ Security & Privacy
- PIN verification is strictly required before any guest order modification or cancellation.
- **Privacy First:** The PIN is never returned in any API response payload.
- Admins operate exclusively via JWT roles (no PIN required).
- Comprehensive DTO validation with Arabic user-friendly error messages (رسائل خطأ عربية واضحة).
- Configured CORS policies for smooth frontend integration.

---

## 🛠️ Tech Stack (التقنيات المستخدمة)

| Layer | Technology |
|---|---|
| **Backend** | Spring Boot 3.3.4, Spring Security + JWT, Spring Data JPA, Bean Validation |
| **Database** | PostgreSQL (Hosted on Neon) |
| **Storage** | Cloudflare R2 (S3-compatible) + Presigned URLs |
| **Hosting** | Railway (Backend Deployment) |
| **Frontend** | React 18 + Vite, React Router v6, Tailwind CSS 3, Axios, Lucide Icons |
| **API Testing** | Bruno |

---

## 🏗️ System Architecture (معمارية النظام)

```

┌─────────────┐   JSON + JWT/PIN    ┌──────────────────┐      ┌─────────────┐

│  React App  │ ──────────────────► │  Spring Boot API │ ───► │  Neon (PG)  │

│ localhost   │                     │  (Railway)       │      └─────────────┘

│   :5173     │   Presigned PUT     ┌──────────────────┐

│             │ ──────────────────► │  Cloudflare R2   │ ──► روابط عامة (r2.dev)

└─────────────┘   (ملفات مباشرة)    └──────────────────┘

       ▲                                                          │

       └────────────────── الصور والـ PDFs ◄──────────────────────┘

```

## 📁 Project Structure (هيكل المشروع)
```
backend/
├── src/main/java/com/coloringshop/printshop/
│   ├── config/          → CorsConfig, R2Config, SecurityConfig
│   ├── security/        → JwtAuthenticationFilter, JwtUtils
│   ├── controller/      → AdminController, GuestOrderController, AuthController
│   ├── service/         → AdminBookService, GuestOrderService, R2Service
│   ├── dto/OrderDto/    → OrderRequest, OrderItemRequest, OrderResponse,
│   │                      OrderItemResponse, GuestOrderUpdateRequest
│   └── model/           → Book, Order, OrderItem, Admin, Status
└── src/main/resources/application.properties

frontend/ (midad-frontend)
├── src/
│   ├── components/      → Navbar, StoreLayout, admin/AdminLayout
│   ├── context/         → CartContext
│   ├── pages/           → Home, Cart, Checkout, OrderSuccess, TrackOrder
│   ├── pages/admin/     → AdminLogin, AdminDashboard, AdminBooks, AdminOrders
│   ├── services/        → api.js (Axios instance), r2.js (Direct R2 upload helper)
│   └── utils/           → orderStatus.js
└── .env                 → API Base URL & R2 Configs
```

---

## 🔌 API Reference

### Public Endpoints (عام)
| Method | Endpoint | Description |
|---|---|---|
| `GET` | `/api/v1/books` | Get all available books |
| `POST` | `/api/v1/orders` | Create a guest order (requires PIN) |
| `GET` | `/api/v1/orders/track?orderId=&phone=` | Track order status |
| `PUT` | `/api/v1/orders/guest/{id}` | Update order using PIN (`PENDING` state only) |
| `DELETE` | `/api/v1/orders/guest/{id}` | Cancel/delete order using PIN |

### Admin Endpoints (JWT Required)
| Method | Endpoint | Description |
|---|---|---|
| `POST` | `/api/v1/auth/login` | Admin authentication |
| `GET` | `/api/v1/admin/statistics` | Dashboard metrics and revenue summary |
| `GET` | `/api/v1/admin/upload-url?type=&contentType=&extension=` | Generate Presigned URL for upload |
| `POST/PUT/DELETE` | `/api/v1/admin/books[/{id}]` | Book management (CRUD) |
| `GET` | `/api/v1/admin/orders` | Fetch all system orders |
| `PUT` | `/api/v1/admin/orders/{id}/status` | Update order status |
| `DELETE` | `/api/v1/admin/orders/{id}` | Permanently delete an order |

---

## 📊 Data Models

- **Book:** `id`, `title`, `description`, `price`, `coverImageUrl`, `pdfFileUrl`
- **Order:** `id`, `orderCode`, `customerName`, `address`, `phone`, `pin`, `status`, `totalPrice`, `createdAt`
- **OrderItem:** `id`, `order`, `book`, `quantity`, `price` (historical snapshot)
- **Status:** `PENDING → PRINTING → READY → DELIVERED` (or `CANCELLED`)

---

## 🔐 Business Rules (قواعد البيزنس)

| Operation | Guest User (الضيف) | Admin (الأدمن) |
|---|---|---|
| **Track Order** | Order ID + Phone Number | ✅ Full View |
| **Edit Order** | Requires PIN + Status must be `PENDING` | ✅ Any time |
| **Cancel / Delete** | Requires PIN (Permanent DB deletion) | ✅ Any time |
| **Confirmed Revenue** | — | `DELIVERED` orders only |

---

## 🚀 Local Setup (التشغيل محلياً)

### Backend
Define required Environment Variables:
```bash
DB_URL=jdbc:postgresql://...
DB_USERNAME=...
DB_PASSWORD=...
APP_JWT_SECRET=...
R2_ACCESS_KEY=...
R2_SECRET_KEY=...
R2_ENDPOINT=https://<account-id>.r2.cloudflarestorage.com
R2_BUCKET_NAME=midad-books
R2_PUBLIC_URL=https://pub-<hash>.r2.dev
```
```
# Run Spring Boot application
mvn spring-boot:run
```

---

## ☁️ النشر (Deployment)



1. **Railway**: اربط الريبو → ضيف الـ Variables فوق → Deploy تلقائي مع كل push

2. **Neon**: أنشئ Database → انسخ الـ Connection string للمتغيرات

3. **Cloudflare R2**:

   - أنشئ Bucket + API Token (Object Read & Write)

   - فعّل **CORS Policy** على الـ Bucket (السماح بـ `PUT/GET/POST/HEAD` من دومين الـ Frontend)

   - فعّل **Public Access (r2.dev subdomain)** لعرض الصور والـ PDF


---
