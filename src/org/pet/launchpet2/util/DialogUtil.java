package org.pet.launchpet2.util;

import org.pet.launchpet2.R;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;

public class DialogUtil {
	
	public static final AlertDialog.Builder createSelectDialogItem(Context context, String[] items, DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builderSingle = new AlertDialog.Builder(context);
		final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(context, android.R.layout.select_dialog_item);
		for(String s : items) {
			arrayAdapter.add(s);
		}
		builderSingle.setNegativeButton(context.getString(R.string.button_close), null);
		builderSingle.setAdapter(arrayAdapter, onClickListener);
		return builderSingle;
	}

}
