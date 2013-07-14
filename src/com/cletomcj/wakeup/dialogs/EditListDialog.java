package com.cletomcj.wakeup.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

import com.cletomcj.wakeup.MusicListsActivity;
import com.cletomcj.wakeup.R;
import com.cletomcj.wakeup.listeners.OnListEditListener;

public class EditListDialog extends DialogFragment{
	private Context context;
	private OnListEditListener editListener;
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		context = getActivity();
		editListener = (OnListEditListener) context;
	    final EditText input = new EditText(context);
	    input.setText(MusicListsActivity.getItemSelected().getTitle());
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.edit_list)
			   .setMessage(R.string.edit_title)
			   .setView(input);
		builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	String nTitle = input.getText().toString().trim();
            	editListener.onListEdit(nTitle);
            }
        });
        builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // User cancelled the dialog
            }
        });

	    return builder.create();
	}

}
