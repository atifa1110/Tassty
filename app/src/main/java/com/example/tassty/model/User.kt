package com.example.tassty.model

/**
 * Data class representing a complete address entry provided by the user.
 * This combines fetched geographical data and user-defined details.
 */
data class UserAddress(
    val id: String,
    /** The complete street address string obtained via reverse geocoding. */
    val fullStreetAddress: String, // e.g., "Hollywood street no. 5"
    /** The latitude coordinate of the selected location pin. */
    val latitude: Double,
    /** The longitude coordinate of the selected location pin. */
    val longitude: Double,

    // --- User Input Data (From Text Fields) ---
    /** A user-friendly name for the address (e.g., "Home," "Office"). */
    val addressName: String,       // e.g., "Rafiq's Home"
    /** Additional details to help locate the spot (e.g., "near the blue gate"). */
    val landmarkDetail: String,    // e.g., "The most corner house"

    // --- Selection Data (From Address Type Chips) ---
    /** The type of address selected by the user, providing type safety. */
    val addressType: AddressType,
    val isSelected: Boolean = false
)

/**
 * Enumeration defining the possible categories for an address, ensuring type safety.
 */
enum class AddressType(val displayName: String) {
    PERSONAL("Personal"),
    BUSINESS("Business")
    // Additional types can be added here if needed, e.g., OFFICE, WAREHOUSE
}