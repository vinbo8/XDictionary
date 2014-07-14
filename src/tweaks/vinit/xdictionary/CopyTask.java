package tweaks.vinit.xdictionary;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class CopyTask extends AsyncTask<Void, Void, Boolean> {

	Context c;
	ZipEntry entry;
	AssetManager am;
	String filename;
	InputStream in;
	OutputStream out;
	ZipInputStream zis;

	CopyTask(Context c, AssetManager am) {
		this.c = c;
		this.am = am;
	}

	@Override
	protected Boolean doInBackground(Void... params) {
		// TODO Auto-generated method stub

		filename = Environment.getExternalStorageDirectory().getAbsolutePath()
				+ "/xdictionary/dict/";
		File f = new File(filename);

		f.mkdirs();

		if (Integer.toString(f.list().length).equalsIgnoreCase("9")) {
			return false;
		}

		publishProgress();

		try {

			String i = am.list("dict")[0];
			Log.i("filename", i);

			in = am.open("dict/" + i);
			Log.d("filename", in.toString());
			zis = new ZipInputStream(in);

			while ((entry = zis.getNextEntry()) != null) {
				Log.w("filename", entry.getName().substring(5));
				f = new File(filename + entry.getName().substring(5));
				if (f.exists()) {
					continue;
				}
				f.createNewFile();
				out = new FileOutputStream(filename
						+ entry.getName().substring(5), false);
				byte[] buffer = new byte[1024];
				int read;
				while ((read = zis.read(buffer)) != -1) {
					out.write(buffer, 0, read);
				}
			}
			Log.w("filename", "entry");

		} catch (IOException e) {
			e.printStackTrace();
		}

		return true;
	}

	protected void onPostExecute(Boolean result) {
		if (result) {
			Toast.makeText(c, "Done!", Toast.LENGTH_SHORT).show();
		}
	}

	protected void onProgressUpdate(Void... values) {

		Toast.makeText(c, "Copying dictionary data", Toast.LENGTH_SHORT).show();

	}
}