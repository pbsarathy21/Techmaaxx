package com.android.ninos.techmaaxx.Bluetooth;

import android.app.Dialog;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.ninos.techmaaxx.R;
import com.android.ninos.techmaaxx.Utils.CustomTextviewTitle;
import com.harrysoft.androidbluetoothserial.BluetoothManager;

import java.util.List;

/**
 * Created by Parthasarathy D on 1/3/2019.
 * Ninos IT Solution
 * parthasarathy.d@ninositsolution.com
 */
public class PairDialog {

    private Context context;
    private Dialog dialog;
    private CustomTextviewTitle textviewTitle;
    private ListView listView;
    private BluetoothManager bluetoothManager;
    private String[] mac;

    public PairDialog(Context context, String[] items) {
        this.context = context;

        dialog = new Dialog(context);
        dialog.setContentView(R.layout.list_paired_devices);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        listView = dialog.findViewById(R.id.list_devices);

        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, items);
        listView.setAdapter(arrayAdapter);

        listView.setOnItemClickListener((parent, view, position, id) -> {

            SharedPreferences preferences = context.getSharedPreferences("BT", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = preferences.edit();
            editor.putInt("MAC", position);
            editor.commit();

            dialog.dismiss();

        });
        dialog.create();

        dialog.show();

    }
        }
