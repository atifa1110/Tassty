package com.example.core.ui.mapper

import com.example.core.domain.model.MenuBusinessInfo

fun MenuBusinessInfo.toUiModel() : MenuBusinessInfo{
    return MenuBusinessInfo(
        menu = menu,
        isWishlist = isWishlist,
        status = status
    )
}