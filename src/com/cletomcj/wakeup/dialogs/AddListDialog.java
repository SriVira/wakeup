package com.cletomcj.wakeup.dialogs;

import com.cletomcj.wakeup.R;
import com.cletomcj.wakeup.listeners.OnListAddListener;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.EditText;

public class AddListDialog extends DialogFragment{
	
	private Context context;
	private OnListAddListener addListener;
		
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState){
		context = getActivity();
		addListener = (OnListAddListener) context;
	    final EditText input = new EditText(context);
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(R.string.new_list)
			   .setMessage(R.string.write_title)
			   .setView(input);
		builder.setPositiveButton(R.string.accept, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
            	String title = input.getText().toString().trim();
            	//insert the title into database
            	addListener.onListAdd(title);
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
