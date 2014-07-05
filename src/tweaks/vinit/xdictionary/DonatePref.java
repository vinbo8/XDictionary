package tweaks.vinit.xdictionary;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.preference.DialogPreference;
import android.util.AttributeSet;

public class DonatePref extends DialogPreference {

	public DonatePref(Context context, AttributeSet attrs) {
		super(context, attrs);
		setDialogLayoutResource(R.layout.donate_window);
	}

	@Override
	protected void onPrepareDialogBuilder(AlertDialog.Builder builder) {
		builder.setTitle("Donate");
		builder.setPositiveButton("Donate",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						Intent playIntent = new Intent(
								"android.intent.action.VIEW",
								Uri.parse("https://play.google.com/store/apps/details?id=tweaks.vinit.xdictionary"));
						getContext().startActivity(playIntent);
					}
				});

		builder.setNegativeButton("Maybe later",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						getDialog().dismiss();
					}
				});
		super.onPrepareDialogBuilder(builder);
	}
}
