package edu.udb.retrofitappcrud.profesores

import retrofit2.Call
import retrofit2.http.*

interface ProfesorApi {
    @GET("profesor.php")
    fun obtenerProfesores(): Call<List<Profesor>>

    @GET("profesor/{id}")
    fun obtenerProfesorPorId(@Path("id") id: Int): Call<Profesor>

    @POST("profesor.php")
    fun crearProfesor(@Body profesor: Profesor): Call<Profesor>

    @PUT("profesor.php")
    fun actualizarProfesor(@Query("id") id: Int, @Body profesor: Profesor): Call<Profesor>

    @DELETE("profesor/{id}")
    fun eliminarProfesor(@Path("id") id: Int): Call<Void>
}