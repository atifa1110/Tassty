package com.example.tassty.screen.onboarding

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.tassty.R

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    @StringRes val title: Int,
    @StringRes val subtitle: Int
) {
    data object First : OnBoardingPage(
        image = R.drawable.eating,
        title = R.string.our_unmatched_delicacies,
        subtitle = R.string.the_finest_restaurants
    )

    data object Second : OnBoardingPage(
        image = R.drawable.phone_boarding,
        title = R.string.ordering_food_in_a_simple_way,
        subtitle = R.string.effortlessly_placing
    )

    data object Third : OnBoardingPage(
        image = R.drawable.megaphone,
        title = R.string.get_extra_daily_vouchers,
        subtitle = R.string.be_a_member_of_tassty
    )
}
