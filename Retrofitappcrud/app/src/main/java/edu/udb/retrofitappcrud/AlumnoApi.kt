package edu.udb.retrofitappcrud

import retrofit2.Call
import retrofit2.http.*

interface AlumnoApi {

    @GET("alumno.php")
    fun obtenerAlumnos(): Call<List<Alumno>>

    @GET("alumno/{id}")
    fun obtenerAlumnoPorId(@Path("id") id: Int): Call<Alumno>

    @POST("alumno.php")
    fun crearAlumno(@Body alumno: Alumno): Call<Alumno>

    @PUT("alumno.php")
    fun actualizarAlumno(@Query("id") id: Int, @Body alumno: Alumno): Call<Alumno>

    @DELETE("alumno/{id}")
    fun eliminarAlumno(@Path("id") id: Int): Call<Void>
}