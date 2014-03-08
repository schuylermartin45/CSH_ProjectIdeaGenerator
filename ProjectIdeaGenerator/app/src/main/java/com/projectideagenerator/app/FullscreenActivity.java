package com.projectideagenerator.app;

import com.projectideagenerator.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    //Button object that stems from the GUI
    private Button generateBtn;
    //Output for the idea
    private TextView ideaDisp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove titlebar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fullscreen);


        //This is how android links the XML to Java...COOL!
        generateBtn = (Button)findViewById(R.id.genBtn);
        ideaDisp = (TextView)findViewById(R.id.fullscreen_content);
        //Generation button click-event control
        generateBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                String idea = IdeaGenerator.generateIdea();
                ideaDisp.setText(idea);
            }
        });

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
