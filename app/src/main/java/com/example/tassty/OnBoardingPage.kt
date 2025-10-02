package com.example.tassty

import androidx.annotation.DrawableRes

sealed class OnBoardingPage(
    @DrawableRes
    val image: Int,
    val title: String,
    val subtitle: String
) {
    data object First : OnBoardingPage(
        image = R.drawable.eating,
        title = "Our unmatched delicacies.",
        subtitle = "The finest restaurants eagerly await your order, showcasing their top menus just for you!"
    )

    data object Second : OnBoardingPage(
        image = R.drawable.phone,
        title = "Ordering food in a simple way.",
        subtitle = "Effortlessly placing an uncomplicated food order to enjoy a hassle-free dining experience"
    )

    data object Third : OnBoardingPage(
        image = R.drawable.megaphone,
        title = "Get extra daily vouchers.",
        subtitle = "Be a member of Tassty and get more benefits such as extra daily discount!"
    )
}
