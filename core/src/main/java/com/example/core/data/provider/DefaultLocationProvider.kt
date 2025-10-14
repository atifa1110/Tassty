package com.example.core.data.provider

import android.annotation.SuppressLint
import android.content.Context
import android.location.Geocoder
import com.example.core.domain.model.LocationDetails
import com.example.core.domain.provider.LocationProvider
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

class DefaultLocationProvider @Inject constructor(
    private val context: Context,
    private val geocoder: Geocoder // bawaan Android
) : LocationProvider {

    private val fusedClient = LocationServices.getFusedLocationProviderClient(context)

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): LocationDetails {
        return suspendCancellableCoroutine { cont ->
            fusedClient.getCurrentLocation(
                com.google.android.gms.location.Priority.PRIORITY_HIGH_ACCURACY,
                null
            ).addOnSuccessListener { androidLoc ->
                if (androidLoc != null) {
                    // convert lat-lng ke alamat readable
                    val addresses = geocoder.getFromLocation(androidLoc.latitude, androidLoc.longitude, 1)
                    val addr = addresses?.firstOrNull()

                    cont.resume(
                        LocationDetails(
                            fullAddress = addr?.getAddressLine(0) ?: "Unknown address",
                            latitude = androidLoc.latitude,
                            longitude = androidLoc.longitude,
                            city = addr?.locality ?: "Unknown city",
                            postcode = addr?.postalCode
                        )
                    )
                } else {
                    cont.resumeWithException(Exception("Location unavailable"))
                }
            }.addOnFailureListener { e ->
                cont.resumeWithException(e)
            }
        }
    }
}
