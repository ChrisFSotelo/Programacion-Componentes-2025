package com.example.nubank

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SavingAccountBottomSheet : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Aquí se infla correctamente el layout del BottomSheet
        return inflater.inflate(R.layout.savings_account, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Ya puedes usar 'view' directamente
        view.findViewById<TextView>(R.id.paragraph1).text = getString(R.string.info_paragraph_1)
        view.findViewById<TextView>(R.id.paragraph2).text = getString(R.string.info_paragraph_2)
        view.findViewById<TextView>(R.id.paragraph3).text = getString(R.string.info_paragraph_3)

        view.findViewById<ImageButton>(R.id.btnBack).setOnClickListener {
            dismiss()
        }
    }
}
