package com.example.core.ui.mapper

import com.example.core.domain.model.Menu
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.model.MenuStatus
import com.example.core.ui.model.MenuUiModel

fun MenuBusinessInfo.toUiModel() : MenuUiModel{
    return MenuUiModel(
        menu = menu,
        isWishlist = isWishlist,
        status = status
    )
}

fun Menu.toUiModel(menuStatus : MenuStatus): MenuBusinessInfo{
    return MenuBusinessInfo(
        menu = this,
        isWishlist = false,
        status = menuStatus
    )
}