package com.example.tassty

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.ComposeView
import androidx.core.view.drawToBitmap
import com.example.core.data.mapper.toDomain
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.OperationalDay
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantDetail
import com.example.core.domain.model.RestaurantStatus
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import com.example.core.ui.model.MenuUiModel
import com.example.core.ui.model.RestaurantDetailUiModel
import com.example.core.ui.model.RestaurantStatusResult
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.component.MarkerView
import com.example.tassty.model.AddressType
import com.example.tassty.model.Cart
import com.example.tassty.model.Category
import com.example.tassty.model.ChipFilterOption
import com.example.tassty.model.ChipOption
import com.example.tassty.model.CollectionUiItem
import com.example.tassty.model.MenuChoiceSection
import com.example.tassty.model.MenuItemOption
import com.example.tassty.model.RadioFilterOption
import com.example.tassty.model.Review
import com.example.tassty.model.UserAddress
import com.example.tassty.model.Voucher
import com.example.tassty.ui.theme.Blue500
import com.example.tassty.ui.theme.Neutral10
import com.example.tassty.ui.theme.Neutral100
import com.example.tassty.ui.theme.Orange50
import com.example.tassty.ui.theme.Orange500
import com.example.tassty.ui.theme.Pink500
import java.util.Locale
import org.threeten.bp.LocalDate
import org.threeten.bp.format.DateTimeFormatter
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar

fun hashUrl(url: String): String {
    return url.hashCode().toString()
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

val addresses = listOf(
    UserAddress(
        id = "address_1",
        fullStreetAddress = "Jl. Sudirman No. 12, Kebayoran Baru, Jakarta Selatan",
        latitude = -6.2235,
        longitude = 106.8119,
        addressName = "Apartment 12A",
        landmarkDetail = "Near the fountain, 3rd floor, unit 12A",
        addressType = AddressType.PERSONAL
    ),UserAddress(
        id = "address_2",
        fullStreetAddress = "Komplek Perkantoran Mega Kuningan Blok E, Jakarta Selatan",
        latitude = -6.2274,
        longitude = 106.8378,
        addressName = "Head Office - Sinar Raya",
        landmarkDetail = "Gedung Tower 3, lantai 15 (samping lift B)",
        addressType = AddressType.BUSINESS
    )
)

val baseChips = listOf(
    ChipOption(
        key = "sort",
        label = "Sort",
        icon = R.drawable.arrow_down,
        selectedColor = Orange50,
        selectedLabelColor = Neutral100,
        selectedIconColor = Orange500,
        selectedBorderColor = Orange500,
        isSelected = false // default aktif
    ),
    ChipOption(
        key = "rated",
        label = "Rating",
        icon = R.drawable.star,
        selectedColor = Orange500,
        selectedLabelColor = Neutral10,
        selectedIconColor = Neutral10,
        selectedBorderColor = Color.Transparent,
        isSelected = false
    ),
    ChipOption(
        key = "promo",
        label = "Promo available",
        icon = R.drawable.promo,
        selectedColor = Blue500,
        selectedLabelColor = Neutral10,
        selectedIconColor = Neutral10,
        selectedBorderColor = Color.Transparent,
        isSelected = false
    ),
    ChipOption(
        key = "delivery",
        label = "Delivery",
        icon = R.drawable.hand,
        selectedColor = Pink500,
        selectedLabelColor = Neutral10,
        selectedIconColor = Neutral10,
        selectedBorderColor = Color.Transparent,
        isSelected = false
    )
)

val restoRatingsOptions = listOf(
    ChipFilterOption("Rated 4.0+", R.drawable.star),
    ChipFilterOption("Rated 4.5+", R.drawable.star)
)
val discountOptions = listOf(
    ChipFilterOption("All type promo", R.drawable.promo),
    ChipFilterOption("Membership", R.drawable.users),
    ChipFilterOption("Credit card payments", R.drawable.tag)
)

val historyOptions = listOf(
    ChipFilterOption("Chicken", R.drawable.history),
    ChipFilterOption("KFC", R.drawable.history),
    ChipFilterOption("Falafel", R.drawable.history)
)

val popularOptions = listOf(
    ChipFilterOption("Chicken", R.drawable.history),
    ChipFilterOption("KFC", R.drawable.history),
    ChipFilterOption("Janji Jiwa", R.drawable.history),
    ChipFilterOption("Falafel", R.drawable.history),
    ChipFilterOption("Banana", R.drawable.history),
    ChipFilterOption("Flower", R.drawable.history)
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
        id="menu_choice_section_1",
        title = "Choice of protein",
        isRequired = true,
        minSelection = 1,
        maxSelection = 1,
        options = listOf(
            MenuItemOption("1","Chicken Thigh", 2000),
            MenuItemOption("2","Chicken Breast", 2300),
            MenuItemOption("3","Eggs", 1000),
            MenuItemOption("4","Beef", 5000),
            MenuItemOption("5","Tofu", 3500)
        )
    ),
    MenuChoiceSection(
        id="menu_choice_section_2",
        title = "Choice of dressing",
        isRequired = true,
        minSelection = 1,
        maxSelection = 2,
        options = listOf(
            MenuItemOption("1","Ranch", 1200),
            MenuItemOption("2","Honey Mustard", 3000),
            MenuItemOption("3","Balsamic vinaigrette", 2800),
            MenuItemOption("4","Blue Cheese", 5000),
            MenuItemOption("5","Caesar", 1500)
        )
    )
)

val operationalHours = listOf(
    OperationalDay("Monday", "08.00 - 22.00"),
    OperationalDay("Tuesday", "08.00 - 22.00"),
    OperationalDay("Wednesday", "08.00 - 22.00"),
    OperationalDay("Thursday", "08.00 - 22.00"),
    OperationalDay("Friday", "08.00 - 22.00"),
    OperationalDay("Saturday", "08.00 - 22.00"),
    OperationalDay("Sunday", "Off Day")
)

val collection = CollectionUiItem(
    collectionId = "1",name = "Favorite Salad",
    itemCount = 2, thumbnailUrl = "",isSelected = false
)

val collections = listOf(
    CollectionUiItem(
        collectionId = "1",name = "Favorite Salad",
        itemCount = 2, thumbnailUrl = "",isSelected = false
    ),
    CollectionUiItem(
        collectionId = "2",name = "Daily Menus",
        itemCount = 0, thumbnailUrl = "",isSelected = false
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
    MenuUiModel(
        menu = com.example.core.domain.model.Menu(
            id = "RES-001", // karena tidak ada "id" di JSON, tapi ada "restaurantId"
            name = "Shabu Premium Set",
            description = "",
            imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
            originalPrice = 150000,
            discountPrice = 130000,
            isAvailable = true,
            rating = 4.8,
            soldCount = 120,
            isBestSeller = true,
            isRecommended = true,
            rank = 1,
            distanceMeters = 750,
            maxOrderQuantity = 3,
            operationalHours = emptyList() // karena tidak ada datanya di JSON
        ),
        status = MenuStatus.CLOSED,
        isWishlist = false
    ),
    MenuUiModel(
        menu = com.example.core.domain.model.Menu(
            id = "RES-002", // karena tidak ada "id" di JSON, tapi ada "restaurantId"
            name = "Shabu Premium Set",
            description = "",
            imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
            originalPrice = 150000,
            discountPrice = 130000,
            isAvailable = true,
            rating = 4.8,
            soldCount = 120,
            isBestSeller = true,
            isRecommended = true,
            rank = 1,
            distanceMeters = 750,
            maxOrderQuantity = 3,
            operationalHours = emptyList() // karena tidak ada datanya di JSON
        ),
        status = MenuStatus.CLOSED,
        isWishlist = false
    )
)

val carts = listOf(
    Cart(id = "1", name = "Fresh Salad", price = 28000, quantity = 1, isChecked = false, stock=4, note = listOf("Notes: Add more cheese","Choice of protein: Beef", "Choice of dressing: Caesar, Balsamic vinaigrette"),
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"
    ),
    Cart(id = "2", name= "Ramen Tomyum",price = 12000 ,quantity = 1, isChecked = false, stock=1,
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/ad2ab90b-ecd0-46f7-b172-48be7b70f922_Combo-Asik-Berdua.jpg?auto=format"),
)

val menuItem =
    MenuUiModel(
        menu = com.example.core.domain.model.Menu(
            id = "RES-001", // karena tidak ada "id" di JSON, tapi ada "restaurantId"
            name = "Shabu Premium Set",
            description = "",
            imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
            originalPrice = 150000,
            discountPrice = 130000,
            isAvailable = true,
            rating = 4.8,
            soldCount = 120,
            isBestSeller = true,
            isRecommended = true,
            rank = 1,
            distanceMeters = 750,
            maxOrderQuantity = 3,
            operationalHours = emptyList() // karena tidak ada datanya di JSON
        ),
        status = MenuStatus.CLOSED,
        isWishlist = false
    )


val restaurantDetails = listOf(
    RestaurantDetail(
        restaurant = Restaurant(
            id = "1",
            name = "Indah Cafe",
            imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Bakery", "Martabak", "Western"),
            rating = 4.9,
            reviewCount = 1250, // Nilai reviewCount diisi
            deliveryTime = "10-20 min",
            locationDetails = LocationDetails( // city dan koordinat dipindahkan ke sini
                fullAddress = "Jl. Sudirman No. 10",
                latitude = -6.2088,
                longitude = 106.8456,
                city = "Jakarta"
            ),
            operationalHours = operationalHours
        ),
        isVerified = true,
        deliveryCost = "Free",
    ),
    RestaurantDetail(
        restaurant = Restaurant(
            id = "2",
            name = "Foodie Heaven",
            imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Fusion", "Nasi", "Seafood"),
            rating = 4.7,
            reviewCount = 450,
            deliveryTime = "25-30 min",
            locationDetails = LocationDetails(
                fullAddress = "Jl. Raya Bogor No. 5",
                latitude = -6.2500,
                longitude = 106.8000,
                city = "Jakarta"
            ),
            operationalHours = operationalHours
        ),
        isVerified = false,
        deliveryCost = "Rp 15.000",
    ),
    RestaurantDetail(
        restaurant = Restaurant(
            id = "3",
            name = "Sushi Corner",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Japanese", "Sushi", "Dessert"),
            rating = 4.8,
            reviewCount = 2890,
            deliveryTime = "15-25 min",
            locationDetails = LocationDetails(
                fullAddress = "Jl. Asia Afrika No. 1",
                latitude = -6.2100,
                longitude = 106.8200,
                city = "Jakarta"
            ),
            operationalHours = operationalHours
        ),
        isVerified = true,
        deliveryCost = "Rp 10.000",
    ),
    RestaurantDetail(
        restaurant = Restaurant(
            id = "4",
            name = "Healthy Bites",
            imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/1a3b8e1e-a1a3-4a85-abfa-2c2c0ef30996_restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Salad", "Vegan", "Minuman Sehat"),
            rating = 4.5,
            reviewCount = 980,
            deliveryTime = "20 min",
            locationDetails = LocationDetails(
                fullAddress = "Jl. HR Rasuna Said Kav. 1",
                latitude = -6.2200,
                longitude = 106.8300,
                city = "Jakarta"
            ),
            operationalHours = operationalHours
        ),
        isVerified = true,
        deliveryCost = "Free"
    ),
    RestaurantDetail(
        restaurant = Restaurant(
            id = "5",
            name = "Burger Factory",
            imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Burger", "American", "Fries"),
            rating = 4.6,
            reviewCount = 670,
            deliveryTime = "30 min",
            locationDetails = LocationDetails(
                fullAddress = "Jl. Kebon Jeruk IX No. 3",
                latitude = -6.2800,
                longitude = 106.7800,
                city = "Jakarta"
            ),
            operationalHours = operationalHours
        ),
        deliveryCost = "Rp 5.000",
        isVerified = false,
    )
)

val restaurantDetailItem = RestaurantDetailUiModel(
    detail = RestaurantDetail(
        restaurant = Restaurant(
            id = "1",
            name = "Indah Cafe",
            imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
            category = listOf("Bakery", "Martabak", "Western"),
            rating = 4.9,
            reviewCount = 1250, // Nilai reviewCount diisi
            deliveryTime = "10-20 min",
            locationDetails = LocationDetails( // city dan koordinat dipindahkan ke sini
                fullAddress = "Jl. Sudirman No. 10",
                latitude = -6.2088,
                longitude = 106.8456,
                city = "Jakarta"
            ),
            operationalHours = operationalHours,
        ),
        isVerified = true,
        deliveryCost = "Free",
    ),
    distance = 100,
    isWishlist = false,
    operationalStatus = RestaurantStatusResult(RestaurantStatus.CLOSED,"")
)

val userCurrentLocation = LocationDetails(
    fullAddress = "Lokasi Pengguna",
    latitude = -6.2150,
    longitude = 106.8400,
    city = "Jakarta"
)



/**
 * Maps Java Calendar's day of week to a standardized string (e.g., "MONDAY").
 */
private fun getDayOfWeekString(calendarDay: Int): String {
    return when (calendarDay) {
        Calendar.MONDAY -> "MONDAY"
        Calendar.TUESDAY -> "TUESDAY"
        Calendar.WEDNESDAY -> "WEDNESDAY"
        Calendar.THURSDAY -> "THURSDAY"
        Calendar.FRIDAY -> "FRIDAY"
        Calendar.SATURDAY -> "SATURDAY"
        Calendar.SUNDAY -> "SUNDAY"
        else -> ""
    }
}

/**
 * Calculates the current operational status of the restaurant using older Java/Android APIs.
 * This function is compatible with all Android API levels.
 *
 * @param operationalHours The list of daily operational hours.
 */

fun Restaurant.getTodayStatusResult(): RestaurantStatusResult {
    val calendar = Calendar.getInstance()
    val currentDayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)
    val currentDayOfWeekString = getDayOfWeekString(currentDayOfWeekInt)

    val timeFormat = SimpleDateFormat("HH.mm", Locale.getDefault())
    val currentTimeString = timeFormat.format(calendar.time)

    val todayHours = operationalHours.find {
        it.day.equals(currentDayOfWeekString, ignoreCase = true)
    }

    // Hari ini libur atau tidak ada data
    if (todayHours == null || todayHours.hours.isBlank() || todayHours.hours.equals("CLOSED", ignoreCase = true)) {
        return RestaurantStatusResult(
            status = RestaurantStatus.OFFDAY,
            message = "Closed today"
        )
    }

    val parts = todayHours.hours.split(" - ")
    if (parts.size != 2) {
        return RestaurantStatusResult(RestaurantStatus.CLOSED, "Invalid time format")
    }

    return try {
        val (openTimeString, closeTimeString) = parts.map { it.trim() }

        val currentTime = timeFormat.parse(currentTimeString)
        val openTime = timeFormat.parse(openTimeString)
        val closeTime = timeFormat.parse(closeTimeString)

        // Pastikan semua jam berhasil diparse
        if (currentTime == null || openTime == null || closeTime == null) {
            return RestaurantStatusResult(
                status = RestaurantStatus.CLOSED,
                message = "Error parsing time format"
            )
        }

        when {
            // ⏰ Restoran sedang buka
            !currentTime.before(openTime) && currentTime.before(closeTime) -> {
                RestaurantStatusResult(
                    status = RestaurantStatus.OPEN,
                    message = "Closes at $closeTimeString today"
                )
            }

            // ⏱ Belum buka, tapi akan buka hari ini
            currentTime.before(openTime) -> {
                RestaurantStatusResult(
                    status = RestaurantStatus.CLOSED,
                    message = "Opens at $openTimeString today"
                )
            }

            // 🚪 Sudah tutup untuk hari ini
            else -> {
                val nextOpenDayInfo = findNextOpenDayCompat(currentDayOfWeekInt, operationalHours)
                val message = if (nextOpenDayInfo != null) {
                    val nextOpenTime = nextOpenDayInfo.hours.split(" - ")[0]
                    "Closed. Opens next on ${nextOpenDayInfo.day} at $nextOpenTime"
                } else {
                    "Closed for the day"
                }

                RestaurantStatusResult(
                    status = RestaurantStatus.CLOSED,
                    message = message
                )
            }
        }
    } catch (e: ParseException) {
        RestaurantStatusResult(
            status = RestaurantStatus.CLOSED,
            message = "Error processing hours: ${e.message ?: "unknown"}"
        )
    } catch (e: Exception) {
        RestaurantStatusResult(
            status = RestaurantStatus.CLOSED,
            message = "An unknown error occurred"
        )
    }
}

fun calculateRestaurantStatus(
    operationalHours: List<OperationalDay>
): RestaurantStatusResult {

    val calendar = Calendar.getInstance()
    val currentDayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)
    val currentDayOfWeekString = getDayOfWeekString(currentDayOfWeekInt)

    // Format to extract time for comparison (e.g., "19:20")
    val timeFormat = SimpleDateFormat("HH.mm", Locale.getDefault())

    // Format to extract only the hour and minute from the current time
    val currentTimeString = timeFormat.format(calendar.time)

    val todayHours = operationalHours.find {
        it.day.uppercase(Locale.ROOT) == currentDayOfWeekString
    }

    if (todayHours == null || todayHours.hours.isBlank() || todayHours.hours.equals("CLOSED", ignoreCase = true)) {
        return RestaurantStatusResult(
            status = RestaurantStatus.OFFDAY,
            message = "Closed today"
        )
    }

    val parts = todayHours.hours.split(" - ")
    if (parts.size != 2) {
        return RestaurantStatusResult(RestaurantStatus.CLOSED, "Invalid time format")
    }

    try {
        val openTimeString = parts[0]
        val closeTimeString = parts[1]

        // Parse current, open, and close times into Date objects for comparison
        val currentTime = timeFormat.parse(currentTimeString)!!
        val openTime = timeFormat.parse(openTimeString)!!
        val closeTime = timeFormat.parse(closeTimeString)!!

        // 2. Check if the current time is within the operating window
        // Note: Date.after() and Date.before() are equivalent to > and < for time comparison
        val isOpen = !currentTime.before(openTime) && currentTime.before(closeTime)

        if (isOpen) {
            return RestaurantStatusResult(
                status = RestaurantStatus.OPEN,
                message = "Closes at $closeTimeString today"
            )
        } else if (currentTime.before(openTime)) {
            // Closed now but will open later today
            return RestaurantStatusResult(
                status = RestaurantStatus.CLOSED,
                message = "Opens at $openTimeString today"
            )
        } else {
            // Past closing time for today
            // Look for the next open day
            val nextOpenDayInfo = findNextOpenDayCompat(currentDayOfWeekInt, operationalHours)

            val message = if(nextOpenDayInfo != null) {
                val nextOpenTime = nextOpenDayInfo.hours.split(" - ")[0]
                "Closed. Opens next on ${nextOpenDayInfo.day} at $nextOpenTime"
            } else {
                "Closed for the day"
            }

            return RestaurantStatusResult(
                status = RestaurantStatus.CLOSED,
                message = message
            )
        }

    } catch (e: ParseException) {
        // Handle issues if "HH.mm" string format is incorrect
        return RestaurantStatusResult(RestaurantStatus.CLOSED, "Error processing hours: ${e.message}")
    } catch (e: Exception) {
        // Catch all other exceptions
        return RestaurantStatusResult(RestaurantStatus.CLOSED, "An unknown error occurred")
    }
}

// Helper to find the next open day (simulating DayOfWeek.plus(i))
private fun findNextOpenDayCompat(currentDayInt: Int, hours: List<OperationalDay>): OperationalDay? {
    for (i in 1..7) {
        // Calculate the next day in the cycle (1=Sunday, 7=Saturday)
        val nextDayInt = (currentDayInt + i - 1) % 7 + 1
        val nextDayString = getDayOfWeekString(nextDayInt)

        val nextDayInfo = hours.find {
            it.day.uppercase(Locale.ROOT) == nextDayString &&
                    it.hours.isNotBlank() &&
                    !it.hours.equals("CLOSED", ignoreCase = true)
        }

        if (nextDayInfo != null) {
            return nextDayInfo
        }
    }
    return null
}

/**
 * Mencari jam operasional untuk hari saat ini dalam daftar OperationalDay.
 *
 * @param operationalHours Daftar jam operasional restoran.
 * @return String jam operasional (e.g., "10.00 - 20.00") atau pesan default jika tutup.
 */
fun getTodayOperatingHoursString(
    operationalHours: List<OperationalDay>
): String {
    val calendar = Calendar.getInstance()
    val currentDayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)
    val currentDayOfWeekString = getDayOfWeekString(currentDayOfWeekInt)

    // Mencari entri hari ini (misalnya, mencari "WEDNESDAY")
    val todayHours = operationalHours.find {
        it.day.uppercase(Locale.ROOT) == currentDayOfWeekString
    }

    // Jika entri ditemukan dan jamnya ada, kembalikan jamnya.
    if (todayHours != null && todayHours.hours.isNotBlank()) {
        // Karena data Anda mungkin memiliki "Off Day", kita cek lagi agar lebih aman
        if (todayHours.hours.equals("Off Day", ignoreCase = true)) {
            return "Closed Today"
        }
        return todayHours.hours
    }

    // Default jika hari ini tidak terdaftar atau datanya kosong
    return "N/A"
}


/**
 * Memproses daftar OperationalDay untuk menandai hari mana yang isToday.
 */
fun markToday(operationalHours: List<OperationalDay>): List<OperationalDay> {
    val calendar = Calendar.getInstance()
    val currentDayOfWeekInt = calendar.get(Calendar.DAY_OF_WEEK)
    val currentDayOfWeekString = getDayOfWeekString(currentDayOfWeekInt) // e.g., "WEDNESDAY"

    return operationalHours.map { day ->
        val dayString = day.day.uppercase(Locale.ROOT)

        // Cek apakah nama hari cocok
        val isCurrentDay = dayString == currentDayOfWeekString

        // Kembalikan objek OperationalDay yang sudah di-copy dengan status isToday yang benar
        day.copy(isToday = isCurrentDay)
    }
}


val categories = listOf(
    Category(1,"Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format"),
    Category(2,"Bakso & Soto","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/6a7bbb72-962e-4dff-ba2e-caf3cdac39f7_cuisine-soto_bakso_sop-banner.png?auto=format"),
    Category(3,"Bakery","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/ea0db4c2-79b8-4a69-8b00-ea634e7ff3c9_cuisine-roti-banner.png?auto=format"),
    Category(4,"Chinese","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/e152cfa1-1813-4be7-aebb-62b84cd0d38f_cuisine-chinese-banner.png?auto=format"),
    Category(5,"Western","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/00847dea-ca14-4ecc-8cdf-f97a8d221d53_cuisine-burger_sandwich_steak-banner.png?auto=format"),
    Category(6,"Fast Food","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/292379c7-fcdd-44e9-8821-ede3a09765b1_fastfood.png?auto=format"),
    Category(7,"Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format"),
    Category(8,"Bakso & Soto","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/6a7bbb72-962e-4dff-ba2e-caf3cdac39f7_cuisine-soto_bakso_sop-banner.png?auto=format"),
    Category(9,"Bakery","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/ea0db4c2-79b8-4a69-8b00-ea634e7ff3c9_cuisine-roti-banner.png?auto=format"),
    Category(10,"Chinese","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/e152cfa1-1813-4be7-aebb-62b84cd0d38f_cuisine-chinese-banner.png?auto=format"),
    Category(11,"Western","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/00847dea-ca14-4ecc-8cdf-f97a8d221d53_cuisine-burger_sandwich_steak-banner.png?auto=format"),
    Category(12,"Fast Food","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/292379c7-fcdd-44e9-8821-ede3a09765b1_fastfood.png?auto=format")
)

val categoriesItem = Category(1,"Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format")

fun LocalDate.toUiDateString(): String {
    val formatter = DateTimeFormatter.ofPattern("dd MMM yy", Locale.ENGLISH)
    return this.format(formatter)
}

fun getSampleVouchers(): List<Voucher> {
    val todayApiString = "2025-10-06"
    val apiFormatter = DateTimeFormatter.ISO_DATE
    val today: LocalDate = LocalDate.parse(todayApiString, apiFormatter)

    return listOf(

        // 1. GLOBAL DISCOUNT (Percentage)
        Voucher(
            id = "VOU-PYPL-99",
            imageUrl = "https://assets.example.com/icons/paypal.png",
            title = "Diskon 20% Semua Restoran",
            description = "Use with GoPay or 7 other options, \nmax Rp15K discount.",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.GLOBAL,
            discountValue = 20,
            maxDiscount = 30000,
            minOrderValue = 75000,
            minOrderLabel = "Min. order Rp75.000",
            expiryDate = today.plusMonths(1).withDayOfMonth(28),
            isAvailable = true,
            isSelected = false,
        ),

        // 2. RESTAURANT-SPECIFIC DISCOUNT (Percentage)
        Voucher(
            id = "VOU-RST-456",
            imageUrl = "https://assets.example.com/icons/restaurant_exclusive.png",
            title = "Chef's Special 15% Off",
            description = "Valid only at 'The Spice Garden' \nrestaurant.",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.RESTAURANT,
            discountValue = 15,
            maxDiscount = 10000,
            minOrderValue = 40000,
            minOrderLabel = "Min. order Rp40.000",
            expiryDate = today.plusWeeks(2),
            isAvailable = true,
            isSelected = false,
            restaurantIds = listOf("4")
        ),

        // 3. GLOBAL SHIPPING (Free Delivery)
        Voucher(
            id = "VOU-DLVY-01",
            imageUrl = "https://assets.example.com/icons/delivery.png",
            title = "Gratis Ongkir Rp15.000",
            description = "Use with GoPay or 7 other options",
            type = VoucherType.SHIPPING,
            discountType = DiscountType.FIXED,
            scope = VoucherScope.GLOBAL,
            discountValue = 15000,
            maxDiscount = 15000,
            minOrderValue = 50000,
            minOrderLabel = "Min. order Rp50.000",
            expiryDate = today.plusDays(7),
            isAvailable = true,
            isSelected = false,
        ),

        // 4. RESTAURANT-SPECIFIC CASHBACK
        Voucher(
            id = "VOU-CB-77",
            imageUrl = "https://assets.example.com/icons/cashback.png",
            title = "Cashback 10% di Kopi Kenangan",
            description = "Dapatkan cashback ke Tassty Points",
            type = VoucherType.CASHBACK,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.RESTAURANT,
            discountValue = 10,
            maxDiscount = 20000,
            minOrderValue = 25000,
            minOrderLabel = "Min. order Rp25.000",
            expiryDate = today.plusMonths(2),
            isAvailable = true,
            isSelected = false,
            restaurantIds = listOf("4")
        ),

        // 5. GLOBAL CASHBACK
        Voucher(
            id = "VOU-CB-88",
            imageUrl = "https://assets.example.com/icons/global_cashback.png",
            title = "Cashback 5% Semua Restoran",
            description = "Use with GoPay or Tassty Points balance",
            type = VoucherType.CASHBACK,
            discountType = DiscountType.PERCENTAGE,
            scope = VoucherScope.GLOBAL,
            discountValue = 5,
            maxDiscount = 15000,
            minOrderValue = 20000,
            minOrderLabel = "Min. order Rp20.000",
            expiryDate = today.plusMonths(1),
            isAvailable = true,
            isSelected = false
        ),

        // 6. RESTAURANT FIXED DISCOUNT (Unique case)
        Voucher(
            id = "VOU-RST-789",
            imageUrl = "https://assets.example.com/icons/burger.png",
            title = "Potongan Rp25.000 di Burger Bliss",
            description = "Hanya berlaku untuk Burger Bliss",
            type = VoucherType.DISCOUNT,
            discountType = DiscountType.FIXED,
            scope = VoucherScope.RESTAURANT,
            discountValue = 25000,
            maxDiscount = 25000,
            minOrderValue = 70000,
            minOrderLabel = "Min. order Rp70.000",
            expiryDate = today.plusWeeks(3),
            isAvailable = true,
            isSelected = false,
            restaurantIds = listOf("4")
        ),

        // 7. SHIPPING - RESTAURANT ONLY (rare case)
        Voucher(
            id = "VOU-DLVY-LOCAL",
            imageUrl = "https://assets.example.com/icons/local_delivery.png",
            title = "Free Ongkir Rp10.000 (Khusus Nasi Goreng Pak Kumis)",
            description = "Berlaku hanya untuk resto tertentu.",
            type = VoucherType.SHIPPING,
            discountType = DiscountType.FIXED,
            scope = VoucherScope.RESTAURANT,
            discountValue = 10000,
            maxDiscount = 10000,
            minOrderValue = 30000,
            minOrderLabel = "Min. order Rp30.000",
            expiryDate = today.plusDays(10),
            isAvailable = true,
            isSelected = false,
            restaurantIds = listOf("4")
        )
    )

}

fun filterVouchersByRestaurant(targetRestaurantId: String): List<Voucher> {

    return getSampleVouchers().filter { voucher ->
        // Kriteria 1: Voucher GLOBAL selalu disertakan
        val isGlobal = voucher.scope == VoucherScope.GLOBAL

        // Kriteria 2: Voucher RESTAURANT disertakan HANYA jika ID target cocok
        val isRestaurantSpecificMatch = voucher.scope == VoucherScope.RESTAURANT &&
                voucher.restaurantIds.contains(targetRestaurantId)

        // Gabungkan kedua kriteria: (GLOBAL) ATAU (RESTAURANT dan ID cocok)
        isGlobal || isRestaurantSpecificMatch
    }
}

fun placeholder() = Restaurant(
    id = "", name = "", rating = 0.0,
    deliveryTime = "", locationDetails = LocationDetails("",0.0,0.0,"",""),
    imageUrl = "",
    operationalHours = emptyList(),
    category = listOf(),
    reviewCount = 0
)

val voucherItem = VoucherUiModel(
    voucher = com.example.core.domain.model.Voucher(
        id = "VCHR001",
        code =  "DISKON20",
        imageUrl = "https://example.com/images/voucher_discount_20.png",
        title =  "Diskon 20% Semua Menu",
        description= "Nikmati diskon 20% untuk semua menu tanpa minimum pembelian.",
        type= VoucherType.DISCOUNT,
        discountType= DiscountType.PERCENTAGE,
        scope = VoucherScope.RESTAURANT,
        discountValue = 20,
        maxDiscount= 50000,
        minOrderValue = 0,
        minOrderLabel ="Tanpa minimum pembelian",
        startDate= LocalDate.of(2024,10,24),
        expiryDate=LocalDate.of(2024,10,24),
        status= VoucherStatus.AVAILABLE,
        terms = "Tidak dapat digabung dengan promo lain. Berlaku untuk semua restoran yang berpartisipasi."
    ),
    isUsable = true,
    isSelected = false
)