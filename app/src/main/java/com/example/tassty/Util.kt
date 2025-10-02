package com.example.tassty

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import android.os.Looper
import androidx.annotation.RequiresApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Discount
import androidx.core.content.ContextCompat
import com.example.tassty.component.RadioFilterItem
import com.example.tassty.model.Category
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.FavoriteCollection
import com.example.tassty.model.Menu
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.model.OperationalDay
import com.example.tassty.model.RadioFilterOption
import com.example.tassty.model.Restaurant
import com.example.tassty.model.Review
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import com.google.android.gms.maps.model.LatLng
import java.text.NumberFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.Locale
import java.util.concurrent.TimeUnit

fun hashUrl(url: String): String {
    return url.hashCode().toString() // atau pakai MD5/SHA1 kalau mau unik banget
}
fun getSubtitle(min: Int, max: Int): String {
    return when {
        min == 1 && max == 1 -> "pick 1"
        min > 1 && max > min -> "pick $min to $max"
        min == 0 && max > 0 -> "pick up to $max"
        min == max && min > 1 -> "pick $min"
        else -> "select options"
    }
}
fun getCurrentLocation(
    context: Context,
    onLocation: (LatLng) -> Unit
) {
    // 1. Cek Izin Lokasi
    if (ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    ) {
        // Jika izin belum diberikan, Anda harus meminta izin terlebih dahulu.
        // Anda bisa menggunakan ActivityResultLauncher untuk menangani ini.
        return
    }

    val fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)

    // 2. Ambil Lokasi Terakhir
    fusedLocationClient.lastLocation.addOnSuccessListener { location ->
        if (location != null) {
            onLocation(LatLng(location.latitude, location.longitude))
        } else {
            // 3. Jika Lokasi Terakhir tidak tersedia, minta update baru
            val locationRequest = LocationRequest.Builder(
                Priority.PRIORITY_HIGH_ACCURACY,
                TimeUnit.SECONDS.toMillis(10)
            )
                .setMinUpdateIntervalMillis(TimeUnit.SECONDS.toMillis(5))
                .setMaxUpdates(1) // Minta hanya satu update
                .build()

            val locationCallback = object : LocationCallback() {
                override fun onLocationResult(locationResult: LocationResult) {
                    val newLocation = locationResult.lastLocation
                    if (newLocation != null) {
                        onLocation(LatLng(newLocation.latitude, newLocation.longitude))
                    }
                    // Hentikan update setelah mendapatkan lokasi
                    fusedLocationClient.removeLocationUpdates(this)
                }
            }

            fusedLocationClient.requestLocationUpdates(
                locationRequest,
                locationCallback,
                Looper.getMainLooper()
            )
        }
    }
}

fun handleSelectionChange(
    currentSection: MenuChoiceSection,
    toggledOption: MenuItemOption,
    onSectionUpdated: (MenuChoiceSection) -> Unit
) {
    val currentSelections = currentSection.selectedOptions.toMutableList()
    val isSingleChoice = currentSection.maxSelection == 1

    if (currentSelections.contains(toggledOption)) {
        // 1. Jika Pilihan Tunggal (RadioButton) dan Opsional (Min=0), Hapus
        if (isSingleChoice && currentSection.minSelection == 0) {
            currentSelections.clear()
        }
        // 2. Jika Pilihan Ganda (Checkbox), hanya hapus jika tidak melanggar Min
        else if (!isSingleChoice && currentSelections.size > currentSection.minSelection) {
            currentSelections.remove(toggledOption)
        }
        // Kasus lain (RadioButton Wajib) tidak melakukan apa-apa (tidak bisa di-uncheck)

    } else {
        // --- Logika Tambah (Check) ---

        if (isSingleChoice) {
            // Jika pilihan tunggal (RadioButton), ganti langsung
            currentSelections.clear()
            currentSelections.add(toggledOption)
        }
        else if (currentSelections.size < currentSection.maxSelection) {
            // Jika pilihan ganda (Checkbox), dan belum mencapai batas maksimum
            currentSelections.add(toggledOption)
        }
    }

    onSectionUpdated(currentSection.copy(selectedOptions = currentSelections))
}

fun Int.toCleanRupiahFormat(): String {
    val localeID = Locale.Builder().setLanguage("in").setRegion("ID").build()

    val formatRupiah = NumberFormat.getCurrencyInstance(localeID).apply {
        // Baris ini yang menghilangkan koma dan angka nol di belakang
        maximumFractionDigits = 0
    }

    // Perhatikan: Simbol mata uang default dari locale "in", "ID" adalah "Rp"
    return formatRupiah.format(this.toLong())
}

val restoRatingsOptions = listOf(
    ChipFilterOption("Rated 4.0+", R.drawable.star),
    ChipFilterOption("Rated 4.5+", R.drawable.star)
)
val discountOptions = listOf(
    ChipFilterOption("All type promo", R.drawable.promo),
    ChipFilterOption("Membership", R.drawable.users),
    ChipFilterOption("Credit card payments", R.drawable.tag)
)

val modesOptions = listOf(
    ChipFilterOption("Delivery", R.drawable.hand),
    ChipFilterOption("Pickup", R.drawable.hand)
)

val cuisineOptions = listOf(
    RadioFilterOption("Beverages", "cuisine_beverages"),
    RadioFilterOption("Snacks", "cuisine_snacks")
)

val rupiahPriceRanges = listOf(
    RadioFilterOption("Di bawah Rp20.000", "below_20"),
    RadioFilterOption("Rp20.000 ~ Rp50.000", "range_20_50"),
    RadioFilterOption("Rp50.000 ~ Rp200.000", "range_50_200"),
    RadioFilterOption("Di atas Rp200.000", "above_200")
)

var menuSections = listOf(
    MenuChoiceSection(
        title = "Choice of protein",
        isRequired = true,
        minSelection = 1,
        maxSelection = 1,
        options = listOf(
            MenuItemOption("Chicken Thigh", 2000),
            MenuItemOption("Chicken Breast", 2300),
            MenuItemOption("Eggs", 1000),
            MenuItemOption("Beef", 5000),
            MenuItemOption("Tofu", 3500)
        )
    ),
    MenuChoiceSection(
        title = "Choice of dressing",
        isRequired = true,
        minSelection = 1,
        maxSelection = 2,
        options = listOf(
            MenuItemOption("Ranch", 1200),
            MenuItemOption("Honey Mustard", 3000),
            MenuItemOption("Balsamic vinaigrette", 2800),
            MenuItemOption("Blue Cheese", 5000),
            MenuItemOption("Caesar", 1500)
        )
    )
)

// Data Dummy Jam Operasional
val operationalHours = listOf(
    OperationalDay("Monday", "08.00 - 22.00"),
    OperationalDay("Tuesday", "08.00 - 22.00"),
    OperationalDay("Wednesday", "08.00 - 22.00", isToday = true),
    OperationalDay("Thursday", "08.00 - 22.00"),
    OperationalDay("Friday", "08.00 - 22.00"),
    OperationalDay("Saturday", "08.00 - 22.00"),
    OperationalDay("Sunday", "Off Day")
)

val collection = FavoriteCollection(
    collectionId = "1",name = "Favorite Salad",
    itemCount = 2, thumbnailUrl = "",isSelected = false
)
val collections = listOf(
    FavoriteCollection(
        collectionId = "1",name = "Favorite Salad",
        itemCount = 2, thumbnailUrl = "",isSelected = false
    ),
    FavoriteCollection(
        collectionId = "2",name = "Daily Menus",
        itemCount = 0, thumbnailUrl = "",isSelected = true
    )
)

val reviews = listOf(
    Review(1,
        userName = "Andrew",
        date = "Sep 27, 2024",
        rating = 5,
        reviewText = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
        profilePicture = ""
    ),
    Review(2,
        userName = "Kiana",
        date = "Sep 27, 2024",
        rating = 4,
        reviewText = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
        profilePicture = ""
    )
)
val menus = listOf(
    Menu(id = 1,
        name = "Fresh Salad", description = "food short description",
        price = "28.000", rating= 4.9, distance = 190, sold = 154, stock = 10,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"
    ),
    Menu(id = 2, name= "Ramen Tomyum", description = "spicy noodle soup", price = "12.000",
        rating = 4.7, distance = 125,sold = 167, stock = 4, imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"),
    Menu(id = 3, name = "Pizza", description = "pepperoni pizza", price="25.000",
        rating = 4.8, distance = 100 ,sold = 100, stock =0,imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/c8c7b731-0ad1-47d7-90a2-8525239a4b5f_Combo-Jiwa-Toast.jpg?auto=format"),
    Menu(id  = 4, name = "Burger", description = "classic cheeseburger", price="20.000", rating = 4.6, distance = 250 ,
        sold = 154, stock = 8, imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"),
)

val menuItem =
    Menu(id = 1,
        name = "Fresh Salad", description = "food short description",
        price = "28.000", rating= 4.9, distance = 190, sold = 154, stock = 10,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"
    )

val restaurants = listOf(
    Restaurant(
        id = 1, name = "Indah Cafe", isVerified = true, rating = 4.9,
        deliveryTime = "10-20", deliveryCost = "Free", city = "Jakarta", distance = 20,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
        operationalHours = operationalHours,
        menus = menus
    ),
    Restaurant(
        id = 2, name = "Foodie Heaven", isVerified = false, rating = 4.7,
        deliveryTime = "25-30", deliveryCost = "Rp 15.000", city = "Jakarta", distance = 20,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
        operationalHours = operationalHours,
        menus = menus
    ),
    Restaurant(
        id = 3, name = "Sushi Corner", isVerified = true, rating = 4.8,
        deliveryTime = "15-25", deliveryCost = "Rp 10.000",city = "Jakarta", distance = 20,
        operationalHours = operationalHours,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/aa10c62a-b967-4890-a50d-9e906bfc0f53_brand-image_1758592956515.jpg?auto=format"
    ),
    Restaurant(
        id = 4, name = "Healthy Bites",
        isVerified = true, rating = 4.5,
        deliveryTime = "20", deliveryCost = "Free",city = "Jakarta", distance = 20,
        operationalHours = operationalHours,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format"
    ),
    Restaurant(
        id = 5, name = "Burger Factory",
        isVerified = false, rating = 4.6,
        deliveryTime = "30", deliveryCost = "Rp 5.000",city = "Jakarta", distance = 20,
        operationalHours = operationalHours,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format"
    )
)

@RequiresApi(Build.VERSION_CODES.O)
private fun getOpenStatus(today: OperationalDay?): Pair<Boolean, String> {
    if (today == null) return false to "No data"

    if (today.hours == "Off Day") return false to "Closed! Off Day"

    val parts = today.hours.split(" - ")
    val openTime = LocalTime.parse(parts[0], DateTimeFormatter.ofPattern("HH.mm"))
    val closeTime = LocalTime.parse(parts[1], DateTimeFormatter.ofPattern("HH.mm"))
    val now = LocalTime.now()

    return when {
        now.isBefore(openTime) -> {
            false to "Closed! Opens at ${parts[0]} today"
        }
        now.isAfter(closeTime) -> {
            false to "Closed! Opens tomorrow at ${parts[0]}"
        }
        else -> {
            true to "Open until ${parts[1]}"
        }
    }
}


val restaurantItem =
    Restaurant(
        id = 1,
        name = "Indah Cafe",
        isVerified = true,
        rating = 4.9,
        deliveryTime = "40",
        deliveryCost = "Free",city = "Jakarta", distance = 20,
        operationalHours = operationalHours,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format"
    )

val categories = listOf(
    Category(1,"Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format"),
    Category(2,"Bakso & Soto","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/6a7bbb72-962e-4dff-ba2e-caf3cdac39f7_cuisine-soto_bakso_sop-banner.png?auto=format"),
    Category(3,"Bakery","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/ea0db4c2-79b8-4a69-8b00-ea634e7ff3c9_cuisine-roti-banner.png?auto=format"),
    Category(4,"Chinese","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/e152cfa1-1813-4be7-aebb-62b84cd0d38f_cuisine-chinese-banner.png?auto=format"),
    Category(5,"Western","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/00847dea-ca14-4ecc-8cdf-f97a8d221d53_cuisine-burger_sandwich_steak-banner.png?auto=format"),
    Category(6,"Fast Food","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/292379c7-fcdd-44e9-8821-ede3a09765b1_fastfood.png?auto=format")
)

val categoriesItem = Category(1,"Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format")