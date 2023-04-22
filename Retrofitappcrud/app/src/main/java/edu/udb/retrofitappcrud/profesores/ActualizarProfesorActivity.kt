package edu.udb.retrofitappcrud.profesores

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import edu.udb.retrofitappcrud.MainActivity
import edu.udb.retrofitappcrud.R
import okhttp3.Credentials
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ActualizarProfesorActivity : AppCompatActivity() {

    private lateinit var api: ProfesorApi
    private var profesor: Profesor? = null

    private lateinit var nombreEditText: EditText
    private lateinit var apellidoEditText: EditText
    private lateinit var edadEditText: EditText
    private lateinit var actualizarButton: Button

    // Obtener las credenciales de autenticación
    val auth_username = "admin"
    val auth_password = "admin123"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_actualizar_profesor)

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
        val api = retrofit.create(ProfesorApi::class.java)

        // Obtener el ID del alumno de la actividad anterior
        val profesorId = intent.getIntExtra("profesor_id", -1)
        Log.e("API", "profesorId : $profesorId")

        val nombre = intent.getStringExtra("nombre").toString()
        val apellido = intent.getStringExtra("apellido").toString()
        val edad = intent.getIntExtra("edad", 1)

        nombreEditText.setText(nombre)
        apellidoEditText.setText(apellido)
        edadEditText.setText(edad.toString())

        val profesor = Profesor(0,nombre, apellido, edad)

        // Configurar el botón de actualización
        actualizarButton.setOnClickListener {
            if (profesor != null) {
                // Crear un nuevo objeto Alumno con los datos actualizados
                val profesorActualizado = Profesor(
                    profesorId,
                    nombreEditText.text.toString(),
                    apellidoEditText.text.toString(),
                    edadEditText.text.toString().toInt()
                )
                Log.e("API", "profesorActualizado : $profesorActualizado")
                // Realizar una solicitud PUT para actualizar el objeto Alumno
                api.actualizarProfesor(profesorId, profesorActualizado).enqueue(object :
                    Callback<Profesor> {
                    override fun onResponse(call: Call<Profesor>, response: Response<Profesor>) {
                        if (response.isSuccessful && response.body() != null) {
                            // Si la solicitud es exitosa, mostrar un mensaje de éxito en un Toast
                            Toast.makeText(this@ActualizarProfesorActivity, "Profesor actualizado correctamente", Toast.LENGTH_SHORT).show()
                            // Finalizar la actividad
                            finish()
                        }
                        val error = response.errorBody()?.string()
                        Log.e("API", "Error al actualizar profesor: $error")
                    }

                    override fun onFailure(call: Call<Profesor>, t: Throwable) {
                        // Si la solicitud falla, mostrar un mensaje de error en un Toast
                        Log.e("API", "onFailure : $t")
                        Toast.makeText(this@ActualizarProfesorActivity, "Error al actualizar el profesor", Toast.LENGTH_SHORT).show()
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