package com.projectideagenerator.app;

import com.projectideagenerator.app.util.SystemUiHider;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.graphics.Path;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.view.MenuInflater;
import android.widget.Button;
import android.widget.TextView;
import android.content.DialogInterface;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Stack;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends Activity {

    //Button objects that stem from the GUI
    private Button generateBtn;
    private Button stackBtn;
    //Output for the idea
    private TextView ideaDisp;
    //Tracks phrases
    //WAS actually a stack, but then I needed an array...
    private ArrayList<String> theStack;
    //Max size of the stack
    private final int MAXSTACK = 50;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // remove titlebar
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_fullscreen);
        theStack = new ArrayList<String>();

        //This is how android links the XML to Java...COOL!
        generateBtn = (Button)findViewById(R.id.genBtn);
        stackBtn = (Button)findViewById(R.id.stackBtn);
        ideaDisp = (TextView)findViewById(R.id.fullscreen_content);

        //Clicking on an idea will copy it to the clip-board
        ideaDisp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                //prevent copying of default text...
                if(theStack.size() != 0 )
                {
                    //Thank you IntelliJ for auto-completing camel-case vars
                    ClipboardManager clipboardManager =
                        (ClipboardManager)getSystemService(CLIPBOARD_SERVICE);
                    ClipData clipData = ClipData.newPlainText("CSHProjectText",ideaDisp.getText());
                    clipboardManager.setPrimaryClip(clipData);
                    //Tell the user!
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                            new ContextThemeWrapper(v.getContext(), R.style.AppTheme));
                    alertBuilder.setMessage("Copied to clipboard!");
                    final AlertDialog alert = alertBuilder.show();
                    //formatting
                    TextView message = (TextView)alert.findViewById(android.R.id.message);
                    message.setGravity(Gravity.CENTER);
                    message.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            alert.cancel();
                        }
                    });
                    //draw the dialog!
                    alert.show();
                }
            }
        });

        //Generation button click-event control
        generateBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                String idea = IdeaGenerator.generateIdea();
                ideaDisp.setText(idea);
                //idea is stored
                theStack.add(idea);
                //exceed the max, the remove from the end...
                if(theStack.size() > MAXSTACK)
                {
                    theStack.remove(0);
                }
            }
        });

        //Draw the stack to the screen in a scroll-able list
        stackBtn.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v)
            {
                //Dialog to put ListView in
                AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                        new ContextThemeWrapper(v.getContext(), R.style.AppTheme));
                alertBuilder.setTitle("Previous ideas:");
                //control the list visuals
                final String[] listToShow;
                if(theStack.size() == 0)
                {
                    listToShow = new String[]{ "<You have no ideas>" };
                }
                else
                {
                    listToShow = theStack.toArray(new String[theStack.size()]);
                    //b/c not actually a stack but I need to
                    //display it as a true stack...so reverse it!
                    for(int cntr=0; cntr<listToShow.length/2; cntr++)
                    {
                        String temp = listToShow[cntr];
                        listToShow[cntr] = listToShow[listToShow.length - cntr - 1];
                        listToShow[listToShow.length - cntr - 1] = temp;
                    }
                }
                alertBuilder.setItems(listToShow, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item) {
                        //Set the current text if non-zero
                        if(theStack.size() != 0)
                        {
                            //clicking an item in the list will draw it back on the screen
                            ideaDisp.setText(listToShow[item]);
                        }
                    }
                });
                AlertDialog alert = alertBuilder.create();
                alert.show();
            }
        });
    }

    //The part that handles the action bar buttons
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu items for use in the action bar
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main_activity_actions, menu);

        //Share button
        //MenuItem shareItem = (MenuItem)menu.findItem(R.id.actionShare);
        //shareItem.setActionProvider();
        return(super.onCreateOptionsMenu(menu));
    }

    //Handles about button...much simpler than using an ActivityProvider
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.actionSettings)
        {
            //build a simple dialog block with some stuff
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                    new ContextThemeWrapper(this, R.style.AppTheme));
            alertBuilder.setTitle("CSH Project Idea Generator");
            alertBuilder.setMessage(
                    "Created by:\n" +
                    "Schuyler Martin\n\n" +
                    "CSHer and student at RIT\n" +
                    "GitHub: schuylermartin45\n\n" +
                    "Open source since 2014\n"
            );
            AlertDialog alert = alertBuilder.create();
            alert.show();
            return(true);
        }
        return(false);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
