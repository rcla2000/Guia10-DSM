package edu.udb.retrofitappcrud

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import edu.udb.retrofitappcrud.profesores.ProfesoresActivity
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarAlumnoActivity : AppCompatActivity() {

    private lateinit var api: AlumnoApi
    private var alumno: Alumno? = null

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var actualizarButton: Button

    // Obtener las credenciales de autenticación
    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_alumno)

        nombreEditText = findViewById(R.id.nombreEditText)
        apellidoEditText = findViewById(R.id.apellidoEditText)
        edadEditText = findViewById(R.id.edadEditText)
        actualizarButton = findViewById(R.id.actualizarButton)

        // Crea un cliente OkHttpClient con un interceptor que agrega las credenciales de autenticación
        val client = OkHttpClient.Builder()
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", Credentials.basic(auth_username, auth_password))
                    .build()
                chain.proceed(request)
            }
            .build()

        // Crea una instancia de Retrofit con el cliente OkHttpClient
        val retrofit = Retrofit.Builder()
            .baseUrl("http://192.168.14.130/guia-10-dsm/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()

        // Crea una instancia del servicio que utiliza la autenticación HTTP básica
        val api = retrofit.create(AlumnoApi::class.java)

        // Obtener el ID del alumno de la actividad anterior
        val alumnoId = intent.getIntExtra("alumno_id", -1)
        Log.e("API", "alumnoId : $alumnoId")

        val nombre = intent.getStringExtra("nombre").toString()
        val apellido = intent.getStringExtra("apellido").toString()
        val edad = intent.getIntExtra("edad", 1)

        nombreEditText.setText(nombre)
        apellidoEditText.setText(apellido)
        edadEditText.setText(edad.toString())

        val alumno = Alumno(0,nombre, apellido, edad)

        // Configurar el botón de actualización
        actualizarButton.setOnClickListener {
            if (alumno != null) {
                // Crear un nuevo objeto Alumno con los datos actualizados
                val alumnoActualizado = Alumno(
                    alumnoId,
                    nombreEditText.text.toString(),
                    apellidoEditText.text.toString(),
                    edadEditText.text.toString().toInt()
                )
                Log.e("API", "alumnoActualizado : $alumnoActualizado")
                // Realizar una solicitud PUT para actualizar el objeto Alumno
                api.actualizarAlumno(alumnoId, alumnoActualizado).enqueue(object : Callback<Alumno> {
                    override fun onResponse(call: Call<Alumno>, response: Response<Alumno>) {
                        if (response.isSuccessful && response.body() != null) {
                            // Si la solicitud es exitosa, mostrar un mensaje de éxito en un Toast
                            Toast.makeText(this@ActualizarAlumnoActivity, "Alumno actualizado correctamente", Toast.LENGTH_SHORT).show()
                            // Finalizar la actividad
                            finish()
                        }
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error actualizar alumno: $error")
                    }

                    override fun onFailure(call: Call<Alumno>, t: Throwable) {
                        // Si la solicitud falla, mostrar un mensaje de error en un Toast
                        Log.e("API", "onFailure : $t")
                        Toast.makeText(this@ActualizarAlumnoActivity, "Error al actualizar el alumno", Toast.LENGTH_SHORT).show()
                    }
                })
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_alumnos -> {
                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
            R.id.action_profesores -> {
                val intent = Intent(this, ProfesoresActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }
}