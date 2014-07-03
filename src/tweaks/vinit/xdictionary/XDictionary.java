package tweaks.vinit.xdictionary;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Locale;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.XC_MethodHook;
import de.robv.android.xposed.XposedBridge;
import de.robv.android.xposed.XposedHelpers;
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam;

public class XDictionary implements IXposedHookLoadPackage {

	int POPUP_TEXT_LAYOUT;
	int MAX_URI_LENGTH = 200;
	int selStart, selEnd;
	int width, height;
	Object mEditor;
	TextView mTextView, mDictTextView;
	ViewGroup mContentView;
	PopupWindow mPopupWindow;
	LayoutInflater mPopupInflater;
	Intent browserIntent;
	StringBuffer url;
	LinearLayout layout;
	LayoutParams params;

	private class Definition extends AsyncTask<String, Void, String[]> {

		private TextView word_view, meaning_view;

		public Definition(TextView w, TextView m) {
			word_view = w;
			meaning_view = m;
		}

		@Override
		protected String[] doInBackground(String... params) {

			StringBuffer url = new StringBuffer(200);
			StringBuffer out = new StringBuffer(5000);
			StringBuffer json_buffer = new StringBuffer(5000);
			
			String[] outputResult = new String[2];
			
			String url_func = "d0f718dc083e4811b010c0d8c060940a914a7869fae4dee3e"; 
			
			
			url.append("http://api.wordnik.com/v4/word.json/");
			url.append(params[0].toLowerCase());
			url.append("/definitions?limit=50&includeRelated=false&sourceDictionaries=wiktionary&useCanonical=false&includeTags=false&api_key=");
			url.append(url_func);

			DefaultHttpClient httpClient = new DefaultHttpClient(
					new BasicHttpParams());

			HttpGet h = new HttpGet(url.toString());


			try {
				HttpResponse hres = httpClient.execute(h);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						hres.getEntity().getContent()));
				String line = new String();

				while ((line = in.readLine()) != null) {
					json_buffer.append(line + "\n");
				}
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}

			try {
				JSONArray json_array = new JSONArray(json_buffer.toString());
				
				if(json_array.length() == 0) {
					throw new JSONException("Failed");
				}
				for (int i = 0; i < json_array.length(); i++) {
					JSONObject json_obj = json_array.getJSONObject(i);
					String meaning = String.format("%d. (%s) %s\n", (i + 1),
							json_obj.getString("partOfSpeech"),
							json_obj.getString("text"));
					out.append(meaning);
				}

			} catch (JSONException e) {
				e.printStackTrace();
				outputResult[0] = "No definition found.";
				outputResult[1] = "";
				return outputResult;
			}

			outputResult[0] = params[0].toLowerCase() + ":";
			outputResult[1] = out.toString();
			return outputResult;
		}

		@Override
		protected void onPostExecute(String[] result) {
			word_view.setText(result[0]);
			meaning_view.setText(result[1]);
		}
	}

	public void handleLoadPackage(final LoadPackageParam lpparam)
			throws Throwable {

		findAndHookMethod("android.widget.Editor.ActionPopupWindow",
				lpparam.classLoader, "initContentView", new XC_MethodHook() {
					protected void afterHookedMethod(MethodHookParam param) {

						mEditor = XposedHelpers
								.getSurroundingThis(param.thisObject);
						mTextView = (TextView) XposedHelpers.getObjectField(
								mEditor, "mTextView");

						mContentView = (ViewGroup) XposedHelpers
								.getObjectField(param.thisObject,
										"mContentView");
						POPUP_TEXT_LAYOUT = XposedHelpers.getIntField(
								param.thisObject, "POPUP_TEXT_LAYOUT");

						LayoutInflater inflater = (LayoutInflater) mTextView
								.getContext().getSystemService(
										Context.LAYOUT_INFLATER_SERVICE);

						LayoutParams wrapContent = new LayoutParams(
								ViewGroup.LayoutParams.WRAP_CONTENT,
								ViewGroup.LayoutParams.WRAP_CONTENT);

						mDictTextView = (TextView) inflater.inflate(
								POPUP_TEXT_LAYOUT, null);
						mDictTextView.setLayoutParams(wrapContent);
						mContentView.addView(mDictTextView);
						mDictTextView.setText("Define");

						// CONSTRUCT POPUP

						mDictTextView.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {

								selStart = mTextView.getSelectionStart();
								selEnd = mTextView.getSelectionEnd();

								url = new StringBuffer(MAX_URI_LENGTH);
								url.append("http://dictionary.reference.com/browse/");
								String search_word = mTextView.getText()
										.subSequence(selStart, selEnd)
										.toString();
								
								// url.append(mTextView.getText().subSequence(
								// selStart, selEnd));

								/*
								 * browserIntent = new
								 * Intent(Intent.ACTION_VIEW,
								 * Uri.parse(url.toString()));
								 * v.getContext().startActivity(browserIntent);
								 */

								/*
								 * POPUP CODE
								 */

								width = mTextView.getContext().getResources()
										.getDisplayMetrics().widthPixels;
								height = mTextView.getContext().getResources()
										.getDisplayMetrics().heightPixels;

								LinearLayout layout = new LinearLayout(
										mTextView.getContext());
								TextView word = new TextView(mTextView
										.getContext());
								TextView meaning = new TextView(mTextView
										.getContext());

								LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								params.setMargins(40, 0, 40, 0);

					
								word.setTextSize(17);
								word.setLayoutParams(params);
								word.setTypeface(null, Typeface.BOLD);
								word.setPadding(0, 40, 0, 0);
								
								meaning.setTextSize(15);
								meaning.setSingleLine(false);
								meaning.setLayoutParams(params);
								
								Definition def = new Definition(word, meaning);
								def.execute(search_word);
								
								layout.setOrientation(1);
								layout.addView(word);
								layout.addView(meaning);

								Dialog d = new Dialog(v.getContext());
								d.addContentView(layout,
										new LinearLayout.LayoutParams(
												4 * width / 5,
												LayoutParams.WRAP_CONTENT));
								d.setCanceledOnTouchOutside(true);
								d.setTitle("XDictionary");
								d.show();
							}
						});
					}
				});
	}

}
