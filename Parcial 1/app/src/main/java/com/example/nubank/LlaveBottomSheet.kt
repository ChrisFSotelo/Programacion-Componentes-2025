package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class LlaveBottomSheet : BottomSheetDialogFragment() {

    companion object {
        private const val ARG_TIPO = "tipo_llave"
        private const val ARG_VALOR = "valor_llave"

        fun newInstance(tipo: String, valor: String): LlaveBottomSheet {
            val fragment = LlaveBottomSheet()
            val args = Bundle()
            args.putString(ARG_TIPO, tipo)
            args.putString(ARG_VALOR, valor)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_key, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tipo = arguments?.getString(ARG_TIPO) ?: "Tipo desconocido"
        val valor = arguments?.getString(ARG_VALOR) ?: "Valor no disponible"

        view.findViewById<TextView>(R.id.txtTipo).text = tipo
        view.findViewById<TextView>(R.id.txtValor).text = valor

        view.findViewById<LinearLayout>(R.id.btnCompartir).setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Llave: $valor\nTipo: $tipo")
            }
            startActivity(Intent.createChooser(intent, "Compartir llave con"))
        }

        view.findViewById<LinearLayout>(R.id.btnEliminar).setOnClickListener {
            val tipo = arguments?.getString(ARG_TIPO) // ejemplo: "CELULAR"
            val valor = arguments?.getString(ARG_VALOR) // ejemplo: "3120000000"

            if (tipo != null && valor != null) {
                val prefs = requireContext().getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
                val setLlaves = prefs.getStringSet("listaLlaves", emptySet())?.toMutableSet() ?: mutableSetOf()

                val llaveAEliminar = "$tipo:$valor"

                if (setLlaves.remove(llaveAEliminar)) {
                    prefs.edit().putStringSet("listaLlaves", setLlaves).apply()
                    Toast.makeText(requireContext(), "Llave eliminada", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(requireContext(), "No se encontró la llave", Toast.LENGTH_SHORT).show()
                }

                dismiss()
            }
        }



        view.findViewById<ImageView>(R.id.btnCerrar)?.setOnClickListener {
            dismiss()
        }
    }
}
