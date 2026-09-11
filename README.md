# Printshop – Backend

Backend مشروع موقع طباعة كتب التلوين، مبني بـ Spring Boot.

## المتطلبات قبل التشغيل

1. **Java 17** مثبت على جهازك
2. **Maven** (أو استخدم أي IDE بيديره تلقائي زي IntelliJ)
3. **PostgreSQL** مثبت ومشغل، وعامل قاعدة بيانات اسمها `printshop_db`

## خطوات التشغيل

### 1. اعمل قاعدة البيانات
افتح psql أو أي أداة إدارة PostgreSQL وشغل:
```sql
CREATE DATABASE printshop_db;
```

### 2. عدّل إعدادات الاتصال
في ملف `src/main/resources/application.properties`، غيّر:
```
spring.datasource.username=postgres
spring.datasource.password=changeme
```
لبيانات الدخول الحقيقية بتاعتك في PostgreSQL.

### 3. افتح المشروع في IDE
- افتح IntelliJ IDEA (أو Eclipse/VS Code)
- File → Open → اختار فولدر المشروع
- الـ IDE هيتعرف على المشروع كـ Maven project تلقائي ويحمّل الـ dependencies

### 4. شغل المشروع
- دوس على زرار Run جنب الكلاس `PrintshopApplication`
- أو من التيرمينال: `./mvnw spring-boot:run` (لو عندك Maven wrapper) أو `mvn spring-boot:run`

### 5. تأكد إن السيرفر شغال
افتح المتصفح على: `http://localhost:8080`

## هيكل المشروع
```
src/main/java/com/coloringshop/printshop/
├── controller/   → REST endpoints
├── service/      → منطق العمل (Business logic)
├── repository/   → التعامل مع قاعدة البيانات
├── model/        → الـ Entities (Book, Order, OrderItem, Admin)
├── security/     → إعدادات حماية صفحات الأدمن
├── dto/          → كلاسات نقل البيانات بين الـ API والعميل
└── config/       → إعدادات عامة للمشروع
```

## الخطوة الجاية
بناء الـ Entities (Book, Order, OrderItem, Admin) وربطها بقاعدة البيانات عن طريق JPA.
