package tweaks.vinit.xdictionary;

import static de.robv.android.xposed.XposedHelpers.findAndHookMethod;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
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
								/*
								 * BROWSER CODE
								 * 
								 * 
								 * selStart = mTextView.getSelectionStart();
								 * selEnd = mTextView.getSelectionEnd();
								 * 
								 * url = new StringBuffer(MAX_URI_LENGTH);
								 * url.append
								 * ("http://dictionary.reference.com/browse/");
								 * url
								 * .append(mTextView.getText().subSequence(selStart
								 * , selEnd));
								 * 
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

								LinearLayout.LayoutParams word_params = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								word_params.setMargins(40, 0, 40, 0);

								LinearLayout.LayoutParams meaning_params = new LinearLayout.LayoutParams(
										LayoutParams.MATCH_PARENT,
										LayoutParams.WRAP_CONTENT);
								meaning_params.setMargins(40, 0, 40, 0);

								word.setTextSize(15);
								word.setLayoutParams(word_params);
								word.setText("apple (n): ");
								word.setTypeface(null, Typeface.BOLD);
								meaning.setTextSize(15);
								meaning.setSingleLine(false);
								meaning.setText("1. Lorem ipsum dolor sit amet wololo gud1 m8\n2. A fukin froot m8 u want me 2 hook u in the gabber?\n3. oo you callin a fruit ill knock yer block off cunt");
								meaning.setLayoutParams(meaning_params);
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
