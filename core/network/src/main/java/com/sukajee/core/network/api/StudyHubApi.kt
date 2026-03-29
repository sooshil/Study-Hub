package com.sukajee.core.network.api

import com.sukajee.core.network.dto.BookDto
import com.sukajee.core.network.dto.LoginRequestDto
import com.sukajee.core.network.dto.LoginResponseDto
import com.sukajee.core.network.dto.RegisterRequestDto
import com.sukajee.core.network.dto.UserDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface StudyHubApi {

    @POST("auth/login")
    suspend fun login(@Body request: LoginRequestDto): LoginResponseDto

    @POST("auth/register")
    suspend fun register(@Body request: RegisterRequestDto): LoginResponseDto

    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("books/{id}")
    suspend fun getBook(@Path("id") id: String): BookDto

    @GET("users/me")
    suspend fun getCurrentUser(): UserDto
}
