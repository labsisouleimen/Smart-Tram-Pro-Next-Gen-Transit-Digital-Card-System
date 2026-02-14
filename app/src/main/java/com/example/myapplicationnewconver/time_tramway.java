package com.example.myapplicationnewconver;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;


import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class time_tramway extends BottomSheetDialogFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog=(BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        View view=View.inflate(getContext(),R.layout.time_tramway,null);
        dialog.setContentView(view);
        return dialog;
    }
}
