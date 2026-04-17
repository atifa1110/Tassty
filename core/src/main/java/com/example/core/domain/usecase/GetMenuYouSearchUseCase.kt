package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Menu
import com.example.core.domain.repository.MenuRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetMenuYouSearchUseCase @Inject constructor(
    private val menuRepository: MenuRepository
){
    operator fun invoke() : Flow<TasstyResponse<List<Menu>>> {
        return menuRepository.getRecommendedMenus(false)
    }
}