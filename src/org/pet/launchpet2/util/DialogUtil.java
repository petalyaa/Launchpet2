package org.pet.launchpet2.util;

import org.pet.launchpet2.R;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.ArrayAdapter;
import android.widget.EditText;

public class DialogUtil {

	public static interface DialogUtilCallback {
		public void onTextInputClickListener(String textInput);
	}

	public static final AlertDialog.Builder createMultiSelectDialogItem(Context context, String title, String[] items, boolean[] selected, DialogInterface.OnMultiChoiceClickListener onMultiChoiceClickListener, DialogInterface.OnClickListener onClickListener) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		if(!StringUtil.isNullEmptyString(title))
			builder.setTitle(title);
		builder.setMultiChoiceItems(items, selected, onMultiChoiceClickListener);
		builder.setPositiveButton(context.getString(R.string.button_ok), onClickListener);
		builder.setNegativeButton(context.getString(R.string.button_close), null);
		return builder;
	}

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

	public static final AlertDialog.Builder createInputTextDialog(Activity activity, String title, String message, final DialogUtil.DialogUtilCallback onClickListener) {
		AlertDialog.Builder alert = new AlertDialog.Builder(activity);
		if(!StringUtil.isNullEmptyString(title))
			alert.setTitle(title);
		if(!StringUtil.isNullEmptyString(message))
			alert.setMessage(message);
		final EditText input = new EditText(activity);
		alert.setView(input);
		alert.setPositiveButton(activity.getString(R.string.button_ok), new DialogInterface.OnClickListener() {
			@SuppressLint("DefaultLocale")
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				if(onClickListener != null)
					onClickListener.onTextInputClickListener(value);
			}
		});
		alert.setNegativeButton(activity.getString(R.string.button_close), null);
		return alert;
	}

}
