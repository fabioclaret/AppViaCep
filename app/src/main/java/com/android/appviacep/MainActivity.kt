package com.android.appviacep

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.android.appviacep.api.Api
import com.android.appviacep.databinding.ActivityMainBinding
import com.android.appviacep.model.Endereco
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        window.statusBarColor = Color.parseColor("#FF018786");
        actionBar?.setBackgroundDrawable(ColorDrawable(Color.parseColor("#FF018786")))

        val retrofit = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl("https://viacep.com.br/ws/")
            .build()
            .create(Api::class.java)

        binding.buscarCep.setOnClickListener {
            val cep = binding.editCep.text.toString()

            if(cep.isEmpty()){
                Toast.makeText(this,"Preencha o campo CEP",Toast.LENGTH_LONG).show()
            }else{
                retrofit.setEndereco(cep).enqueue(object: Callback<Endereco> {
                    override fun onResponse(call: Call<Endereco>, response: Response<Endereco>) {
                        if(response.code() == 200){
                            val logradouro = response.body()?.logradouro.toString()
                            val bairro     = response.body()?.bairro.toString()
                            val localidade = response.body()?.localidade.toString()
                            val uf         = response.body()?.uf.toString()

                            setFormularios(logradouro, bairro, localidade, uf)

                        }else{
                            Toast.makeText(applicationContext, "CEP inválido!" + response.code(), Toast.LENGTH_LONG).show()
                        }
                    }

                    override fun onFailure(call: Call<Endereco>, t: Throwable) {
                        Toast.makeText(applicationContext, "Erro inesperado", Toast.LENGTH_LONG).show()
                    }
                })
            }
        }
    }

    private fun setFormularios(logradouro: String, bairro: String, localidade: String, uf: String) {
        binding.editLogradouro.setText(logradouro)
        binding.editBairro.setText(bairro)
        binding.editCidade.setText(localidade)
        binding.editEstado.setText(uf)
    }
}

