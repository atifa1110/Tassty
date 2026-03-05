package com.example.tassty

import androidx.compose.runtime.Composable
import com.example.core.domain.model.AddressType
import com.example.core.domain.model.Collection
import com.example.core.domain.model.DiscountType
import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuStatus
import com.example.core.domain.model.OperationalDay
import com.example.core.domain.model.Restaurant
import com.example.core.domain.model.RestaurantStatus
import com.example.core.domain.model.VoucherScope
import com.example.core.domain.model.VoucherStatus
import com.example.core.domain.model.VoucherType
import com.example.core.ui.mapper.FilterCategory
import com.example.core.ui.mapper.FilterIconKeys
import com.example.core.ui.mapper.OrderFilterCategory
import com.example.core.ui.mapper.toUiModel
import com.example.core.ui.model.CardUserUiModel
import com.example.core.ui.model.CartGroupUiModel
import com.example.core.ui.model.CartItemUiModel
import com.example.core.ui.model.CategoryUiModel
import com.example.core.ui.model.ChatUiModel
import com.example.core.ui.model.CollectionMenuUiModel
import com.example.core.ui.model.CollectionRestaurantUiModel
import com.example.core.ui.model.CollectionRestaurantWithMenuUiModel
import com.example.core.ui.model.DetailMenuUiModel
import com.example.core.ui.model.DetailOrderUiModel
import com.example.core.ui.model.DetailRestaurantUiModel
import com.example.core.ui.model.DriverUiModel
import com.example.core.ui.model.FilterOptionUi
import com.example.core.ui.model.NotificationType
import com.example.core.ui.model.NotificationUiModel
import com.example.core.ui.model.OptionGroupUiModel
import com.example.core.ui.model.OptionUiModel
import com.example.core.ui.model.OrderItemUiModel
import com.example.core.ui.model.OrderStatus
import com.example.core.ui.model.OrderUiModel
import com.example.core.ui.model.PaymentChannelUiModel
import com.example.core.ui.model.RestaurantMenuUiModel
import com.example.core.ui.model.RestaurantStatusResult
import com.example.core.ui.model.ReviewUiModel
import com.example.core.ui.model.SenderType
import com.example.core.ui.model.UserAddressUiModel
import com.example.core.ui.model.VoucherUiModel
import com.example.tassty.component.OrderItem
import com.example.tassty.model.ChipFilterOption
import org.threeten.bp.LocalDate

val restaurants = listOf(
    Restaurant(
        id = "RES-001",
        name = "Indah Cafe",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        fullAddress = "Jl. Sudirman No. 10",
        latitude = -6.2088,
        longitude = 106.8456,
        city = "Jakarta",
        categories = listOf("Bakery", "Martabak", "Western"),
        rank = 0,
        distance = 4,
        rating = 4.9,
        totalReviews = 1250,
        deliveryCost = 10000,
        deliveryTime = "10-20 min",
        isOpenFromApi = true,
        closingTimeServerFromApi = "2026-01-13T15:00:00.000Z"
    ),
    Restaurant(
        id = "RES-002",
        name = "Foodie Heaven",
        imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
        fullAddress = "Jl. Raya Bogor No. 5",
        latitude = -6.2500,
        longitude = 106.8000,
        city = "Jakarta",
        categories = listOf("Fusion", "Nasi", "Seafood"),
        rank = 1,
        distance = 4,
        rating = 4.7,
        totalReviews = 450,
        deliveryCost = 10000,
        deliveryTime = "25-30 min",
        isOpenFromApi = false,
        closingTimeServerFromApi = "2026-01-13T15:00:00.000Z"
    ),
)

val restaurantUiModel = restaurants.map { it.toUiModel() }

val menus = listOf(
    Menu(
        id = "MEN-011",
        name = "Shabu Premium",
        imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
        description = "Lorem ipsum dolor sit amet, consectetur adipiscing elit.",
        price = 150000,
        soldCount = 120,
        rank = 1,
        isAvailable = true,
        customizable = false,
        maxQuantity = 3,
        stockLabel = "",
        stockStatus = "AVAILABLE",
        restaurant = restaurants[0]
    ),
    Menu(
        id = "MEN-002",
        name = "Super Komplit 2",
        imageUrl = "https://i.gojekapi.com/darkroom/gofood-indonesia/v2/images/uploads/0d2eab58-d99a-41e4-af67-5cfc7bebc055_12670.jpg?auto=format",
        description = "1 Pc Chicken + 1 Rice + 1 Cream Soup + 1 Mocha Float",
        price = 50000,
        soldCount = 200,
        rank = 2,
        customizable = true,
        isAvailable = true,
        maxQuantity = 10,
        stockStatus = "AVAILABLE",
        stockLabel = "",
        restaurant = restaurants[1]
)
)
val menuItem = menus[0].toUiModel()
val menusItem = menus.map { it.toUiModel() }

val restaurantMenuUiModel = listOf(
    RestaurantMenuUiModel(
        restaurant = restaurantUiModel[0],
        menus = menusItem
    ),
    RestaurantMenuUiModel(
        restaurant = restaurantUiModel[1],
        menus = menusItem
    ),
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

val restaurantDetailItem = DetailRestaurantUiModel(
    id = "1",
    name = "Indah Cafe",
    imageUrl = "https://i.gojekapi.com/darkroom/.../restaurant-image_1756676534805.jpg?auto=format",
    fullAddress = "",
    city = "",
    categories = listOf("Bakery", "Martabak", "Western").joinToString(", "),
    rank = 0,
    distance = 4,
    rating = 4.9,
    totalReviews = 1250,
    deliveryTime = "10-20 min",
    operationalDay = operationalHours.map { it.toUiModel("Friday") },
    isVerified = true,
    deliveryCost = 10000,
    statusResult = RestaurantStatusResult(RestaurantStatus.CLOSED,"Open at 08.00 today"),
    isWishlist = false
)

val optionGroups  = listOf(
    OptionGroupUiModel(
        id = "GRP-001",
        title = "Pilih Varian Kopi",
        required = true,
        maxPick = 1,
        options = listOf(
            OptionUiModel(id="OPT-001", name="Kenangan Blend",
                extraPrice = 0, extraPriceText = "+Rp0", isAvailable = true, isSelected = true),
            OptionUiModel(id = "OPT-002", name = "Juwara Beans",
                extraPrice = 0, extraPriceText = "+Rp0", isAvailable = true, isSelected = false)
        )
    ),

    OptionGroupUiModel(
        id = "GRP-002",
        title = "Sugar Level",
        required = true,
        maxPick = 2,
        options = listOf(
            OptionUiModel(id = "OPT-003", name = "Normal Sugar", extraPrice = 0, extraPriceText = "+Rp0", isAvailable = true, isSelected = false),
            OptionUiModel(id = "OPT-004", name = "Less Sugar", extraPrice = 0, extraPriceText = "+Rp0", isAvailable = true, isSelected = false),
            OptionUiModel(id = "OPT-005", name = "No Sugar", extraPrice = 0, extraPriceText = "+Rp0", isAvailable = true, isSelected = false),
        )
    )
)

val menuDetailItem = DetailMenuUiModel(
    id = "RES-001",
    name = "Shabu Premium Set",
    imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
    description = "shabu shabu yang enak",
    promo = true,
    priceOriginal = 150000,
    priceDiscount = 90000,
    customizable = true,
    isAvailable = true,
    maxQuantity = 10,
    optionGroups = optionGroups,
    restaurant = restaurants[0].toUiModel(),
    menuStatus = MenuStatus.AVAILABLE,
    isWishlist = false
)

val categories = listOf(
    CategoryUiModel("CAT-001","Martabak","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/bf94423b-8781-440d-ad51-c37d4cd75add_cuisine-martabak-banner.png?auto=format",isSelected = false),
    CategoryUiModel("CAT-002","Bakso & Soto","https://i.gojekapi.com/darkroom/butler-id/v2/images/images/6a7bbb72-962e-4dff-ba2e-caf3cdac39f7_cuisine-soto_bakso_sop-banner.png?auto=format",isSelected = false),
)

val collections = listOf(
    Collection(
        id = "Collection-1",
        title = "Favorite Salad",
        imageUrl = "",
        menuCount = 2
    ),
    Collection(
        id = "Collection-1",
        title = "Daily Menus",
        imageUrl = "",
        menuCount = 1
    ),
)

val collectionUiModel = collections.map { it.toUiModel() }

val voucherUiModel = listOf(
    VoucherUiModel(
        id = "VOU-001",
        code = "Voucher1",
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
        startDate = LocalDate.of(2025,6,10),
        expiryDate = LocalDate.of(2025,10,10),
        status = VoucherStatus.AVAILABLE,
        isUsable = true,
        isSelected = false
    ),

    VoucherUiModel(
        id = "VOU-002",
        code = "Vocuher1",
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
        startDate = LocalDate.of(2025,6,10),
        expiryDate = LocalDate.of(2025,10,10),
        status = VoucherStatus.AVAILABLE,
        isUsable = false,
        isSelected = false
    )
)

val voucherUiModel1 = listOf(
    VoucherUiModel(
        id = "VOU-003",
        code = "Voucher1",
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
        startDate = LocalDate.of(2025,6,10),
        expiryDate = LocalDate.of(2025,10,10),
        status = VoucherStatus.AVAILABLE,
        isUsable = true,
        isSelected = false
    ),
)


val collectionMenuUiModel = listOf(
    CollectionMenuUiModel(
        id = "MEN-001",
        name = "Shabu Premium Set",
        imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
        description = "1 Pc Chicken + 1 Rice + 1 Cream Soup + 1 Mocha Float",
        priceText = "Rp150000",
    ),
    CollectionMenuUiModel(
        id = "MEN-002",
        name = "Shabu Premium Set",
        imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
        description = "shabu shabu yang enak",
        priceText = "Rp150000",
    )
)
val collectionRestaurantMenuUiModel = listOf(
    CollectionRestaurantWithMenuUiModel(
        restaurant = CollectionRestaurantUiModel(
            id = "RES-001",
            name = "Foodie Heaven",
            city = "Jakarta",
            ratingText = "4.7",
        ),
        menus = collectionMenuUiModel
    ),
    CollectionRestaurantWithMenuUiModel(
        restaurant = CollectionRestaurantUiModel(
            id = "RES-002",
            name = "Foodie Heaven",
            city = "Jakarta",
            ratingText = "4.7",
        ),
        menus = collectionMenuUiModel
    )
)

val reviews = listOf(
    ReviewUiModel(
        id = "REV-1",
        username = "Andrew",
        date = "Sep 27, 2024",
        rating = 5,
        comment = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
        profileImage = "",
        orderItems = "Hazelnut Latte, Kopi Kenangan Mantan"
    ),
    ReviewUiModel(
        id = "REV-2",
        username = "Kiana",
        date = "Sep 27, 2024",
        rating = 4,
        comment = "Indah Café offers a fantastic \ndining experience with a warm \nambiance and friendly staff.",
        profileImage = "",
        orderItems = "Hazelnut Latte, Kopi Kenangan Mantan"
    )
)

val cartUiModel =
    CartGroupUiModel(
        restaurant = restaurants[0].toUiModel(),
        menus = listOf(
            CartItemUiModel(
                cartId = "CART-001",
                menuId = "MEN-002",
                name = "Shabu Premium Set",
                imageUrl = "https://cdn.example.com/menu/shabu_premium.jpg",
                price =  150000,
                quantity = 1,
                summary = "",
                notes = "",
                isSelected = false,
                isSwipeActionVisible = false
            )
        )
)

val addresses = listOf(
    UserAddressUiModel(
        id = "address_1",
        fullAddress = "Jl. Sudirman No. 12, Kebayoran Baru, Jakarta Selatan",
        latitude = -6.2235,
        longitude = 106.8119,
        addressName = "Apartment 12A",
        landmarkDetail = "Near the fountain, 3rd floor, unit 12A",
        addressType = AddressType.PERSONAL,
        isPrimary = true,
        isSelected = false
    ),
    UserAddressUiModel(
        id = "address_2",
        fullAddress = "Komplek Perkantoran Mega Kuningan Blok E, Jakarta Selatan",
        latitude = -6.2274,
        longitude = 106.8378,
        addressName = "Head Office - Sinar Raya",
        landmarkDetail = "Gedung Tower 3, lantai 15 (samping lift B)",
        addressType = AddressType.PERSONAL,
        isPrimary = false,
        isSelected = false
    )
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

val defaultFilter = listOf(
    FilterOptionUi(
        key = "DEFAULT_SORT",
        label = "Sort",
        category = FilterCategory.SORT,
        iconRes = FilterIconKeys.SORT
    ),
    FilterOptionUi(
        key = "DEFAULT_RATING",
        label = "Rating",
        category = FilterCategory.RATING,
        iconRes = FilterIconKeys.STAR
    ),
    FilterOptionUi(
        key = "DEFAULT_MODE",
        label = "Mode",
        category = FilterCategory.MODE,
        iconRes = FilterIconKeys.DELIVERY
    ),
    FilterOptionUi(
        key = "DEFAULT_PRICE",
        label = "Price",
        category = FilterCategory.PRICE,
        iconRes = FilterIconKeys.PRICE
    ),
    FilterOptionUi(
        key = "DEFAULT_CUISINE",
        label = "Cuisine",
        category = FilterCategory.CUISINE
    )
)

val cardList = listOf(
    CardUserUiModel(
        id = "CARD-001",
        stripeId = "pm_123444243",
        maskedNumber = "**** **** **** 4242",
        cardBrand = "visa",
        expDate = "11/28",
        cardholderName = "Rafiq Daniel",
        themeColor = "pink",
        themeBackground = "pattern_1",
        isActive = true,
        isSelected = true
    ),
    CardUserUiModel(
        id = "CARD-002",
        stripeId = "pm_1323242",
        maskedNumber = "**** **** **** 4242",
        cardBrand = "mastercard",
        expDate = "11/28",
        cardholderName = "Rafiq Daniel",
        themeColor = "orange",
        themeBackground = "pattern_3",
        isActive = true,
        isSelected = false
    )
)

val paymentChannel = listOf(
    PaymentChannelUiModel(
        businessId = "PAY-001",
        channelCode = "BCA",
        name = "BCA virtual account",
        channelCategory = "Virtual Account",
        iconKey = "BCA",
        isEnabled = true,
        isSelected = false
    ),
    PaymentChannelUiModel(
        businessId = "PAY-002",
        channelCode = "BRI",
        name ="BRI virtual account",
        channelCategory = "Virtual Account",
        iconKey = "BRI",
        isEnabled = false,
        isSelected = false
    ),
    PaymentChannelUiModel(
        businessId = "PAY-003",
        channelCode = "OVO",
        name ="OVO e-wallet",
        channelCategory = "E-Wallet",
        iconKey = "OVO",
        isEnabled = true,
        isSelected = false
    ),
    PaymentChannelUiModel(
        businessId = "PAY-004",
        channelCode = "DANA",
        name ="Dana e-wallet",
        channelCategory =  "E-Wallet",
        iconKey = "DANA",
        isEnabled = true,
        isSelected = false
    )
)

val orderList= listOf(
    OrderUiModel(
        id="ORD-001",
        restaurantName = "Kopi Kenangan - Depok Town Square",
        restaurantImage = "",
        status = OrderStatus.PENDING,
        displayHeader = "Today",
        displayTime = "09:35 AM",
        orderDate = LocalDate.of(2026,2,28),
        orderNumber = "",
        finalAmount = "Rp150.000",
        queueNumber = "#001"
    ),
    OrderUiModel(
        id="ORD-002",
        restaurantName = "Indah Cafe",
        restaurantImage = "",
        status = OrderStatus.PLACED,
        displayHeader = "Today",
        orderDate = LocalDate.of(2026,2,28),
        displayTime = "09:35 AM",
        orderNumber = "",
        finalAmount = "Rp150.000",
        queueNumber = "#001"
    ),
    OrderUiModel(
        id="ORD-003",
        restaurantName = "Indah Cafe",
        restaurantImage = "",
        status = OrderStatus.COMPLETED,
        orderDate = LocalDate.of(2026,2,27),
        displayHeader = "Yesterday",
        displayTime = "08:00 AM",
        orderNumber = "",
        finalAmount = "Rp100.000",
        queueNumber = "#012"
    ),
    OrderUiModel(
        id="ORD-004",
        restaurantName = "Indah Cafe",
        restaurantImage = "",
        status = OrderStatus.COMPLETED,
        orderDate = LocalDate.of(2026,2,27),
        displayHeader = "Yesterday",
        displayTime = "08:00 AM",
        orderNumber = "",
        finalAmount = "Rp100.000",
        queueNumber = "#025"
    )
)

val orderFilters: List<FilterOptionUi<OrderFilterCategory>> = listOf(
    FilterOptionUi(
        key = "all",
        label = "All",
        category = OrderFilterCategory.ALL,
        isSelected = true
    ),
    FilterOptionUi(
        key = "ongoing",
        label = "Ongoing",
        category = OrderFilterCategory.ONGOING
    ),
    FilterOptionUi(
        key = "completed",
        label = "Completed",
        category = OrderFilterCategory.COMPLETED
    )
)

@Composable
fun getFilterIconRes(filter: FilterOptionUi<OrderFilterCategory>): Int {
    return when (filter.key) {
        "all" -> R.drawable.collection
        "ongoing" -> R.drawable.delivery
        "completed" -> R.drawable.category
        else -> R.drawable.flag
    }
}

val dummyNotifications = listOf(
    NotificationUiModel(
        id = "1",
        title = "Your order has been taken by the driver!",
        description = "Lorem ipsum dolor sit amet",
        displayTime = "15.00 PM",
        displayHeader = "Today",
        isRead = true,
        type = NotificationType.ORDER
    ),
    NotificationUiModel(
        id = "2",
        title = "45% Special Discount for Tassty Membership!",
        description = "Lorem ipsum dolor sit amet",
        displayTime = "14.25 PM",
        displayHeader = "Today",
        isRead = false,
        type = NotificationType.DISCOUNT
    ),
    NotificationUiModel(
        id = "3",
        title = "45% Special Discount for Tassty Membership!",
        description = "Lorem ipsum dolor sit amet",
        displayTime = "14.25 PM",
        displayHeader = "Yesterday",
        isRead = false,
        type = NotificationType.DISCOUNT
    )
)

val dummyChats = listOf(
    ChatUiModel(
        id = "1",
        senderName = "Lucas Nathan",
        lastMessage = "Your order on its way and should arrive shortly",
        senderProfileImage = "https://example.com/p1.jpg",
        displayTime = "27/09/24",
        unreadCount = 2,
        isOnline = false,
        senderType = SenderType.DRIVER
    ),
    ChatUiModel(
        id = "2",
        senderName = "Indah Café",
        lastMessage = "Your order will be prepared and delivered now",
        senderProfileImage = "https://example.com/p2.jpg",
        displayTime = "27/09/24",
        unreadCount = 0,
        isOnline = true,
        senderType = SenderType.RESTAURANT
    )
)

val listOrder = listOf(
    OrderItemUiModel(
        id = "ITEM-001",
        quantity = 1,
        menuName = menus[0].name,
        notesSummary = "Pilih Varian Kopi: Kenangan Blend\n" +
                "Sugar Level: Normal Sugar\n" +
                "Size: Iced Jumbo\n" +
                "Milk Option: Fresh Milk\n" +
                "Ice level: Less Ice\n" +
                "Syrup: Hazelnut Syrup"
    ),
    OrderItemUiModel(
        id = "ITEM-002",
        quantity = 2,
        menuName = menus[1].name,
        notesSummary = "Note : -"
    )
)
val dummyDetail = DetailOrderUiModel(
    id = "DET-001",
    orderNumber = "T-12340002",
    userAddress = addresses[0],
    restaurant = restaurantUiModel[0],
    userId = "",
    status = OrderStatus.PLACED,
    totalPrice = 15000,
    deliveryFee = 0,
    discount = 10000,
    finalAmount = 140000,
    paymentStatus = "",
    cardPayment = cardList[0],
    createdAt = "",
    queueNumber = "#001",
    driver = DriverUiModel(id = "DRI-001", name = "Luchas",4.7, profileImage = ""),
    orderItems = listOrder
)