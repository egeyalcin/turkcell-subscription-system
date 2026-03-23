# 🚀 Kurulum ve Çalıştırma Yönergeleri

Bu proje, ihtiyaca göre iki farklı ortamda ayağa kaldırılabilir:

- **Release Ortamı (Kubernetes / Minikube)**
- **Development Ortamı (Lokal geliştirme)**

Lütfen kullanmak istediğiniz ortama göre ilgili branch üzerinden ilerleyin.
Minikube/Kubernetes & Docker kurulu olduğundan emin olunuz.

---

## 📦 1. Release Ortamı (Kubernetes / Minikube)

Tüm sistemi Kubernetes üzerinde, mikroservis mimarisinin tüm bileşenleriyle birlikte ayağa kaldırmak için aşağıdaki adımları takip edin.

### 🔹 Branch
```
release
```

### 🔹 Kurulum Komutları
```bash
git checkout release
chmod +x build.sh
./build.sh
```

### 🔹 Erişim

Sistem ayağa kalktıktan sonra Gateway üzerinden aşağıdaki porttan erişilebilir:

```
http://localhost:8016
```

Gateway, `curl` isteklerini kabul etmeye hazır olacaktır.

---

## 🛠️ 2. Development Ortamı (Lokal)

Servisler üzerinde geliştirme yapmak ve debug edebilmek için Docker Compose destekli hibrit yapı kullanılmalıdır.

### 🔹 Branch
```
development
```

### 🔹 Altyapıyı Hazırlama

```bash
git checkout development
cd local-test-env
docker-compose up -d
```

---

### 🔹 Servisleri IDE Üzerinden Çalıştırma

Her servis, **config-server** üzerinde bulunan yerel konfigürasyon dosyasına göre çalıştırılmalıdır.

#### 📌 Örnek:

Eğer config dosyası şu şekildeyse:

```
subscription-service-local.yaml
```

IDE üzerinde aşağıdaki profile aktif edilmelidir:

```
subscription-service-local
```

---

## 📡 Test Etme

Sistem ayağa kalktıktan sonra aşağıdaki örnek POST isteği ile abonelik sürecini başlatabilirsiniz:

```bash
curl --location 'http://localhost:8016/api/v1/subscriptions' \
--header 'Content-Type: application/json' \
--data '{
  "userId": 12345,
  "planId": 1,
  "type": "GNC",
  "cardHolderName": "Ege Yalcin",
  "cardNumber": "1234567812345678",
  "expiryMonth": "12",
  "expiryYear": "26",
  "cvv": "321"
}'
```

---

## ✅ Notlar

- Docker ve Kubernetes ortamlarının sisteminizde kurulu olduğundan emin olun.
- Port çakışmalarını kontrol edin (özellikle `8016`).
- Config Server’ın doğru şekilde ayağa kalktığını kontrol edin.

---

## 👨‍💻 Katkı

Geliştirme yaparken `development` branch’i kullanmanız önerilir.  
Release süreci için `release` branch’i tercih edilmelidir.
