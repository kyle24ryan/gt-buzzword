package edu.gatech.seclass.words6300.menu;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.ScrollView;

import edu.gatech.seclass.words6300.R;
import us.feras.mdv.MarkdownView;

public class ManualActivity extends AppCompatActivity {

  @SuppressLint("ClickableViewAccessibility")
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
    setContentView(R.layout.activity_manual);

    View blank = findViewById(R.id.manual);
    blank.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        onBackPressed();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
      }
    });

    DisplayMetrics displayMetrics = new DisplayMetrics();
    this.getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

    int deviceWidth = Math.round(displayMetrics.widthPixels * 0.85f);
    int deviceHeight = Math.round(displayMetrics.heightPixels * 0.85f);

    MarkdownView markdownView = findViewById(R.id.markdown);
    markdownView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
    markdownView.loadMarkdownFile("file:///android_asset/manual.md", "file:///android_asset/paperwhite.css");
    markdownView.bringToFront();
    markdownView.getLayoutParams().width = deviceWidth;
    markdownView.getLayoutParams().height = deviceHeight;

  }

  @Override
  public void onBackPressed() {
    super.onBackPressed();
    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
  }
}
