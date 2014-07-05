package tweaks.vinit.xdictionary;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class ModeArrayAdapter extends ArrayAdapter<CharSequence> {

	static class ViewHolder {
		TextView prefText;
	}

	LayoutInflater mInflater;
	CharSequence[] mEntries;

	public ModeArrayAdapter(Context context, int resource,
			CharSequence[] objects) {
		super(context, resource, objects);

		mInflater = LayoutInflater.from(context);
		mEntries = objects;
		// TODO Auto-generated constructor stub
	}

	/*public boolean areAllItemsEnabled() {
		return false;
	}*/

	/*public boolean isEnabled(int position) {
		if (position >= 2)
			return false;
		else
			return true;
	}*/

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder mViewHolder;
		if (convertView == null) {
			convertView = mInflater.inflate(
					android.R.layout.select_dialog_singlechoice, null);
			mViewHolder = new ViewHolder();
			mViewHolder.prefText = (TextView) convertView
					.findViewById(android.R.id.text1);

			convertView.setTag(mViewHolder);

			/*if (position == 2) {
				mViewHolder.prefText.setBackgroundColor(Color.GRAY);
			}*/
		} else {
			mViewHolder = (ViewHolder) convertView.getTag();
		}

		mViewHolder.prefText.setText(mEntries[position]);

		return convertView;
	}

}
