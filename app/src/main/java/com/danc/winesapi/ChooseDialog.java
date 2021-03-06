package com.danc.winesapi;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

public class ChooseDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "ChooseDialog";
    ConfirmDialogListener mListener;
    RelativeLayout close_dialog;
    ListView mListView;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.dialog_choose_quantity, null);
        setRetainInstance(true);
        builder.setView(view);

        close_dialog = view.findViewById(R.id.close_dialog);
        close_dialog.setOnClickListener(this);

        mListView = view.findViewById(R.id.list_view);
        mListView.setOnItemClickListener(mOnItemClickListener);

        return builder.create();
    }

    public AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            String positionItem = (String) parent.getItemAtPosition(position);
            Intent intent = new Intent("INTENT_NAME");
            intent.putExtra("Item Position", positionItem);
            LocalBroadcastManager.getInstance(getContext()).sendBroadcast(intent);
            getDialog().dismiss();
        }
    };

    public View.OnClickListener mCloseDialogListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getDialog().dismiss();
        }
    };

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            mListener = (ConfirmDialogListener) context;
        } catch (ClassCastException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.close_dialog:
                dismiss();
                break;
        }
    }

    public interface ConfirmDialogListener {
        void confirmSubmission(boolean isConfirmed);
    }
}
