package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.model.RestaurantBusinessInfo
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.utils.calculateHaversine
import com.example.core.domain.utils.getStatus
import com.example.core.domain.utils.getTodayStatus
import javax.inject.Inject

class GetRecommendedMenusUseCase @Inject constructor(
    private val repository: MenuRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<MenuBusinessInfo>> {
        val result = repository.getRecommendedMenus()

        return when (result) {
            is ResultWrapper.Success -> {
                val businessList = result.data
                    .map { menu ->
                        val restaurantStatus = menu.getTodayStatus()
                        val status = menu.getStatus(restaurantStatus)
                        MenuBusinessInfo(menu,false, status)
                    }

                ResultWrapper.Success(businessList, result.meta)
            }
            is ResultWrapper.Error -> ResultWrapper.Error(result.meta)
            is ResultWrapper.Loading -> ResultWrapper.Loading
        }
    }


    }