package com.example.core.tasstymapper

import com.example.core.data.source.remote.network.Meta
import com.example.core.data.source.remote.network.TasstyResponse
import com.example.core.utils.mapToResource
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class TasstyMapperTest {

    @Test
    fun `mapToResource should return loading resource when response is Loading`() {
        val response = TasstyResponse.Loading()
        val result = response.mapToResource { }

        assertTrue(result.isLoading)
        assertNull(result.data)
    }

    @Test
    fun `mapToResource should return error message when response is Error`() {
        val mockMeta = Meta(code = 404, message = "Jaringan bermasalah", status = "Error")
        val response = TasstyResponse.Error(meta = mockMeta)

        val result = response.mapToResource {}

        assertFalse(result.isLoading)
        assertEquals("Jaringan bermasalah", result.errorMessage)
    }

    @Test
    fun `mapToResource should return mapped data when response is Success`() {
        //val response = TasstyResponse.Success(data = "Luna")

        //val result = response.mapToResource { it.length }

        //assertFalse(result.isLoading)
        //assertEquals(4, result.data) // "Luna".length == 4
    }
}