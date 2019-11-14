package edu.jaen.android.res.theme;

import edu.jaen.android.res.theme.R;
import android.app.*;
import android.content.res.AssetManager;
import android.os.*;

import java.io.IOException;
import java.io.InputStream;


public class ThemeTest extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        try {
            InputStream in = getAssets().open("fil명");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}