package com.example.alphakids.data.response

import com.google.gson.annotations.SerializedName

data class LoginResponsee(

	@field:SerializedName("data")
	val data: DataV? = null,

	@field:SerializedName("error")
	val error: Boolean? = null,

	@field:SerializedName("message")
	val message: String? = null
)

data class DataV(

	@field:SerializedName("date")
	val date: String? = null,

	@field:SerializedName("__v")
	val v: Int? = null,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("email")
	val email: String? = null,

	@field:SerializedName("username")
	val username: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)
