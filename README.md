# TelefonDogrulamaOdev

Kısa açıklama
-------------
Bu proje telefon doğrulama/mikroservis uygulamasının kaynak kodunu içerir. Proje üç ana bileşenden oluşur:
- db: MySQL veritabanı
- app: Backend (Spring Boot) — `./phoneNumber`
- frontend: Frontend — `./frontend`

Docker Compose ile hızlıca çalıştırılabilir şekilde yapılandırılmıştır.

Önkoşullar
---------
- Docker (ve Docker Compose)
- Alternatif olarak backend için: JDK 11+ ve Maven/Gradle
- Alternatif olarak frontend için: Node.js ve npm/yarn (lokal geliştirme için)

Klasör yapısı (özet)
-------------------
- docker-compose.yml
- phoneNumber/   → Backend (Spring Boot)
- frontend/      → Frontend

Çalıştırma — Docker Compose (önerilen)
--------------------------------------
1. Repo kökünde terminal açın.
2. (İsteğe bağlı) Ortam değişkenlerini özelleştirmek için bir `.env` dosyası oluşturun ve içerisine hassas bilgileri koyun. Örnek `.env`:
   ```
   MYSQL_ROOT_PASSWORD=rootpassword
   MYSQL_DATABASE=odevdb
   SPRING_DATASOURCE_USERNAME=root
   SPRING_DATASOURCE_PASSWORD=rootpassword
   ```
   docker-compose.yml içindeki sabit değerleri `.env` ile değiştirmek için compose dosyasını güncelleyin.
3. Hizmetleri başlatın:
   ```
   docker compose up --build -d
   ```
4. Servisleri kontrol edin:
   - Frontend: http://localhost:3000
   - Backend API: http://localhost:8080
   - MySQL: 3306 portundan bağlanabilir (root/rootpassword)

Logları görmek için:
```
docker compose logs -f app
docker compose logs -f frontend
docker compose logs -f db
```

Veritabanı ve JPA
-----------------
- docker-compose.yml içinde `SPRING_JPA_HIBERNATE_DDL_AUTO` değeri `update` olarak ayarlanmış. Bu, uygulama başlatıldığında şemayı otomatik güncelleyecektir. Geliştirme için uygundur; üretimde veri kayıplarını önlemek için migration aracı (Flyway veya Liquibase) kullanmanız önerilir.


Dockerfile önerileri (eğer eksikse)
-----------------------------------
- Backend (Spring Boot) örneği:
```dockerfile
# phoneNumber/Dockerfile
FROM maven:3.8.6-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn -B -DskipTests package

FROM openjdk:17-jdk-slim
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/app/app.jar"]
```

- Frontend (nginx ile servis edilen static build) örneği:
```dockerfile
# frontend/Dockerfile
FROM node:18 AS build
WORKDIR /app
COPY package*.json ./
RUN npm install
COPY . .
RUN npm run build

FROM nginx:stable-alpine
COPY --from=build /app/build /usr/share/nginx/html
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
```

Yardımcı Docker Komutları
-------------------------
- Tüm containerları durdurup kaldırmak:
  ```
  docker compose down
  ```
- Volumeleri de silmek (veri kaybına dikkat):
  ```
  docker compose down -v
  ```

Sorun Giderme
-------------
- "DB connection refused" → db container'ın çalışıp çalışmadığını ve healthcheck sonucunu kontrol edin.
- Port çakışması → hostta 8080/3000/3306 portlarının kullanımda olup olmadığını kontrol edin.
- Maven/Gradle build hataları → `phoneNumber` dizininde lokal olarak `mvn clean package` çalıştırın ve logları inceleyin.

Eksik veya doğrulanması gereken noktalar
----------------------------------------
- `phoneNumber` ve `frontend` dizinlerinin içinde uygun Dockerfile ve build konfigürasyonu olduğundan emin olun.
- Eğer frontend statik dosyalarla servis edilmiyorsa (ör. ayrı bir dev server kullanıyorsa), nginx/Dockerfile yapılandırmasını gözden geçirin.

---
# API Endpoint Dokümantasyonu
Aşağıda backend servisinde kullanılan API endpoint’lerinin örnek istek ve yanıtları bulunmaktadır.

## **POST /api/phone/validate**
Telefon numarasını doğrular.

### İstek
```json
{
  "number": "074173"
}
```

### Yanıt
```json
{
  "number": "074173",
  "rules": {
    "sumOddEqualsEven": true,
    "hasNonZeroDigit": true,
    "sumFirstEqualsLast": true
  },
  "valid": true
}
```

---
## **POST /api/registration**
Telefon doğrulaması geçerliyse kullanıcı kaydı oluşturur.

### İstek
```json
{
  "name": "Ali Veli",
  "email": "ali23@exampl.com",
  "phone": "684189"
}
```

### Yanıt
```json
{
  "status": "accepted",
  "message": "Telefon numarası geçerli, kayıt başarıyla oluşturuldu.",
  "data": {
    "id": 26,
    "name": "Ali Veli",
    "email": "ali23@exampl.com",
    "phone": "684189",
    "createdAt": "2025-12-07T16:18:52.977846524"
  },
  "valid": true
}
```

---
## **GET /api/phone/count**
Doğrulanan telefon numarası sayısını döner.

### Yanıt
```json
{
  "telefon sayısı": 8
}
```

---
## **GET /api/users?page=0&size=5**
Kayıtlı kullanıcıları sayfalı olarak listeler.

### Yanıt
```json
[
  {
    "id": 26,
    "name": "Ali Veli",
    "email": "ali23@exampl.com",
    "phone": "684189",
    "createdAt": "2025-12-07T16:18:52.977847"
  },
  {
    "id": 21,
    "name": "emrullah45",
    "email": "emrullah4uygun@gmail.com",
    "phone": "274175",
    "createdAt": "2025-11-20T19:22:37.392625"
  },
  {
    "id": 15,
    "name": "Ali Veli",
    "email": "ali@exampl.com",
    "phone": "664169",
    "createdAt": "2025-11-20T16:32:24.282532"
  },
  {
    "id": 13,
    "name": "Ali Veli",
    "email": "ali@example.com",
    "phone": "464167",
    "createdAt": "2025-11-20T13:15:18.532947"
  },
  {
    "id": 11,
    "name": "emrullah45",
    "email": "f231229051@ktun.edu.tr",
    "phone": "254155",
    "createdAt": "2025-11-20T12:39:23.763317"
  }
]
```

---
