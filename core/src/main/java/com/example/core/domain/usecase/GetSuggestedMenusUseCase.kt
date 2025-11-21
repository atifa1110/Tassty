package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.MenuBusinessInfo
import com.example.core.domain.repository.MenuRepository
import com.example.core.domain.utils.getStatus
import com.example.core.domain.utils.getTodayStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetSuggestedMenusUseCase @Inject constructor(
    private val repository: MenuRepository
) {
    operator fun invoke(): Flow<TasstyResponse<List<MenuBusinessInfo>>> = flow {
        val result = repository.getSuggestedMenus()

        result.collect { result ->
            when (result) {
                is TasstyResponse.Success -> {
                    val businessList = result.data
                        ?.map { menu ->
                            val restaurantStatus = menu.getTodayStatus()
                            val status = menu.getStatus(restaurantStatus)
                            MenuBusinessInfo(menu, false, status)
                        }

                    emit(TasstyResponse.Success(businessList, result.meta))
                }

                is TasstyResponse.Error -> emit(result) // just pass error downstream
                is TasstyResponse.Loading -> emit(TasstyResponse.Loading)
            }
        }
    }
}