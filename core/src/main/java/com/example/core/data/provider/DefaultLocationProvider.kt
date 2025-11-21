package com.example.core.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.model.UserAddress
import com.example.core.domain.provider.LocationProvider
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DefaultLocationProvider @Inject constructor(
    private val context: Context,
    private val geocoder: Geocoder
) : LocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission") // Pastikan Permission sudah dicek di UI
    override suspend fun getCurrentLocation(): LocationDetails =
        suspendCancellableCoroutine { cont ->

            fusedClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { androidLoc ->
                if (androidLoc == null) {
                    cont.resumeWithException(Exception("Location unavailable"))
                    return@addOnSuccessListener
                }

                // Pindahkan geocoder ke IO thread
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        val addresses = geocoder.getFromLocation(androidLoc.latitude, androidLoc.longitude, 1)
                        val address = addresses?.firstOrNull()

                        val result = LocationDetails(
                            fullAddress = address?.getAddressLine(0) ?: "Unknown address",
                            latitude = androidLoc.latitude,
                            longitude = androidLoc.longitude,
                            city = address?.locality ?: "Unknown city",
                            postcode = address?.postalCode ?: ""
                        )

                        // Kembalikan ke main thread
                        withContext(Dispatchers.Main) {
                            if (cont.isActive) cont.resume(result)
                        }

                    } catch (e: Exception) {
                        withContext(Dispatchers.Main) {
                            if (cont.isActive) cont.resumeWithException(e)
                        }
                    }
                }
            }.addOnFailureListener { e ->
                if (cont.isActive) cont.resumeWithException(e)
            }
        }
}

