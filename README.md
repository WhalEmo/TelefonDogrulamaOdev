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

İletişim
--------
Bu README'i proje köküne ekleyebilirim veya isterseniz backend ve frontend dizinlerinin içeriğini inceleyip Dockerfile/CI iyileştirmeleri için örnek PR hazırlayabilirim.