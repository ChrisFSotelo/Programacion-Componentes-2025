package com.example.nubank

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.nubank.enums.TipoLlave
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

        val tipoStr = arguments?.getString(ARG_TIPO) ?: "TIPO"
        val valor = arguments?.getString(ARG_VALOR) ?: "Valor no disponible"
        val tipoEnum = runCatching { TipoLlave.valueOf(tipoStr) }.getOrNull()

        view.findViewById<TextView>(R.id.txtTipo).text = tipoEnum?.displayName ?: tipoStr
        view.findViewById<TextView>(R.id.txtValor).text = valor


        view.findViewById<LinearLayout>(R.id.btnCompartir).setOnClickListener {
            val tipoStr = arguments?.getString(ARG_TIPO)
            val valor = arguments?.getString(ARG_VALOR)

            val tipoEnum = runCatching { TipoLlave.valueOf(tipoStr ?: "") }.getOrNull()
            val tipoDisplayName = tipoEnum?.displayName ?: tipoStr

            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, "Tipo: $tipoDisplayName\nLlave: $valor")
            }
            startActivity(Intent.createChooser(intent, "Compartir llave con"))
        }


        view.findViewById<LinearLayout>(R.id.btnEliminar).setOnClickListener {
            val tipoStr = arguments?.getString(ARG_TIPO)
            val valor = arguments?.getString(ARG_VALOR)

            if (tipoStr != null && valor != null) {
                val tipoEnum = runCatching { TipoLlave.valueOf(tipoStr) }.getOrNull()

                if (tipoEnum != null) {
                    val prefs = requireContext().getSharedPreferences("MisLlaves", Context.MODE_PRIVATE)
                    val originalSet = prefs.getStringSet("listaLlaves", emptySet()) ?: emptySet()
                    val setLlaves = originalSet.toMutableSet()

                    val llaveAEliminar = "${tipoEnum.name}:$valor"

                    Log.d("LlavesDebug", "Contenido actual del set: $setLlaves")
                    Log.d("LlavesDebug", "Intentando eliminar: $llaveAEliminar")

                    if (setLlaves.remove(llaveAEliminar)) {
                        prefs.edit().putStringSet("listaLlaves", setLlaves).apply()
                        Toast.makeText(requireContext(), "Llave eliminada", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "No se encontró la llave", Toast.LENGTH_SHORT).show()
                    }

                    dismiss()
                } else {
                    Toast.makeText(requireContext(), "Tipo de llave inválido", Toast.LENGTH_SHORT).show()
                }
            }

        }



        view.findViewById<ImageView>(R.id.btnCerrar)?.setOnClickListener {
            dismiss()
        }
    }
}
