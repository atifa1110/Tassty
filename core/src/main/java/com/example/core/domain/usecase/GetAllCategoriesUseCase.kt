package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.domain.model.Category
import com.example.core.domain.repository.CategoryRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    operator fun invoke(fetchFromRemote: Boolean = false): Flow<TasstyResponse<List<Category>>> {
        return repository.getAllCategories(fetchFromRemote)
    }
}