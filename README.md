# 🍕 Tassty - Modern Food Delivery Ecosystem

**Tassty** adalah aplikasi pengiriman makanan *full-stack* yang dibangun dengan **Jetpack Compose**. Aplikasi ini mengusung konsep *High-Performance State Management* dengan menggabungkan **MVVM** dan **MVI**, serta sistem *Hybrid Data* antara **Supabase** (Cloud) dan **Room Database** (Local).

---

## 🚀 Fitur Utama (Technical Highlights)

* **Real-time Order Tracking:** Integrasi **Google Maps & Directions API** untuk pelacakan posisi driver secara presisi dari restoran ke lokasi user.
* **Instant Messaging System:** Fitur chat *real-time* antara user dan driver menggunakan **Stream Chat SDK**, terintegrasi dengan **Firebase Cloud Messaging (FCM)** untuk *Push Notifications*.
* **Hybrid Data Management:** * **Remote:** Menggunakan **Supabase (Auth, JWT, PostgreSQL)** untuk manajemen data global seperti Restoran, Menu, dan Order History.
    * **Local (Offline-first):** Menggunakan **Room Database** untuk mengelola *Cart*, *Personal Favorite & Collections*, dan Notifikasi guna meminimalkan latensi dan beban server.
* **Payment Gateway Integration:** Mendukung transaksi aman menggunakan **Stripe SDK** (Add Card & Payment) serta persiapan infrastruktur untuk **Xendit**.
* **Advanced Authentication:** Sistem login aman dengan Supabase Auth, fitur *Forgot Password*, dan *Multi-stage Account Setup* (pilihan kategori masakan & alamat utama).

---

## 🍱 Fitur Aplikasi (User Experience)

* **Smart Exploration:** Cari restoran dan menu berdasarkan kategori masakan dengan fitur *Searching* dan *Filtering* yang responsif.
* **Dynamic Cart & Customization:** Tambahkan menu ke keranjang dengan opsi kustomisasi detail sebelum melakukan checkout.
* **Live Order Dashboard:** Pantau status pesanan (Pending, Cooking, Delivery) secara *real-time* lengkap dengan estimasi waktu.
* **Personal Favorite & Collection:** Simpan restoran favorit dan buat koleksi menu pribadi yang tersimpan aman di database lokal.
* **Promo & Vouchers:** Sistem klaim diskon yang terintegrasi langsung pada halaman pembayaran.
* **Theme Support:** Antarmuka yang adaptif dengan dukungan **Dark Mode** dan **Light Mode**.

---

## 🏗️ Arsitektur & Modularisasi

Aplikasi ini menerapkan **Clean Architecture** dengan pemisahan modul (**Multi-module**) untuk meningkatkan *maintainability* dan *build speed*:

* **`:app` Module:** Berisi UI Layer utama, ViewModels, Navigasi, serta **Design System & Themes** (Color, Type, Shape) yang mengatur tampilan visual aplikasi.
* **`:core` Module:** *Core engine* aplikasi yang menyediakan fungsionalitas dasar bagi seluruh modul:
    * **`domain`:** Berisi *Business Logic* murni seperti Use Cases, Models, dan Repository Interfaces.
    * **`data`:** Implementasi Repository, integrasi Retrofit/Supabase, dan manajemen **Room Database** (Offline-first).
    * **`di`:** Konfigurasi **Dagger Hilt** untuk manajemen *dependency injection* lintas modul.
    * **`ui`:** Berisi **UI Models** (Data classes khusus untuk tampilan) dan service
  
**State Management:**
- **Hybrid MVI:** Digunakan pada fitur kompleks (seperti Tracking) untuk memastikan *Unidirectional Data Flow* (UDF).
- **MVVM:** Digunakan pada fitur sederhana untuk menjaga efisiensi kode.
- Menggunakan `StateFlow` untuk UI State dan `SharedFlow` untuk *one-time events*.

---

## 🛠️ Tech Stack Ecosystem

### 📱 Mobile (Frontend)
| Category | Technology |
| :--- | :--- |
| **Language** | Kotlin (100%) |
| **UI Framework** | Jetpack Compose |
| **Dependency Injection** | Hilt |
| **Local Database** | Room Database & DataStore |
| **Image Loading** | Coil |

### 🌐 Server (Backend)
| Category | Technology |
| :--- | :--- |
| **Runtime** | Node.js (Express.js) |
| **Deployment** | Vercel (Serverless) |
| **Database** | Supabase (PostgreSQL) |
| **Real-time Engine** | Stream Chat Server SDK & Firebase Admin |
| **Security** | JWT, Rate Limiting, RLS (Row Level Security) |

> **Backend Repository:** [Lihat Backend di GitHub](https://github.com/atifa1110/Tassty-Backend) 🚀

---

## ⚙️ Setup Project

1.  Clone repository ini:
    ```bash
    git clone [https://github.com/atifa1110/Tassty](https://github.com/atifa1110/Tassty)
    ```
2.  Tambahkan API Key di file `local.properties`:
    ```properties
    MAPS_API_KEY=your_google_maps_key
    STREAM_API_KEY=your_stream_key
    STRIPE_PUBLISHABLE_KEY=your_stripe_key
    ```
3.  Pastikan file `google-services.json` (Firebase) sudah berada di folder `/app`.
4.  Sync project dengan Gradle dan jalankan di Android Studio.

---

*Developed with ❤️ by Atifa*
