package com.example.core.domain.usecase

import com.example.core.data.source.remote.network.ResultWrapper
import com.example.core.domain.model.Category
import com.example.core.domain.repository.CategoryRepository
import javax.inject.Inject

class GetAllCategoriesUseCase @Inject constructor(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): ResultWrapper<List<Category>> {
        return repository.getAllCategories()
    }
}