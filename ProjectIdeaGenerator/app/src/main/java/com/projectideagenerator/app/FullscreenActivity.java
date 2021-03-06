package com.projectideagenerator.app;

import com.projectideagenerator.app.util.SystemUiHider;

import android.content.pm.PackageManager;
import android.support.v7.app.ActionBarActivity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.Button;
import android.support.v7.widget.ShareActionProvider;
import android.widget.TextView;
import android.content.DialogInterface;
import android.content.Intent;

import java.util.ArrayList;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class FullscreenActivity extends ActionBarActivity {

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
    //share button control system...thanks Google!
    private ShareActionProvider shareActionProvider;

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
                    displayStandardDialog(null, "Copied to clipboard!");
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
                //what to share later (top of the stack)
                updateShareIntent(theStack.get(theStack.size()-1));
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
                        new ContextThemeWrapper(v.getContext(), R.style.CSHDialog));
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
                            //what to share later (the item that is currently on the screen)
                            updateShareIntent(listToShow[item]);
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
        getMenuInflater().inflate(R.menu.main_activity_actions, menu);
        MenuItem shareItem = menu.findItem(R.id.actionShare);
        //Share button
        shareActionProvider =
                (ShareActionProvider)MenuItemCompat.getActionProvider(shareItem);
        Intent shareIntent = getDefaultIntent("You have no ideas!");
        shareActionProvider.setShareIntent(shareIntent);
        return(super.onCreateOptionsMenu(menu));
    }

    /**
     * Passes the string along to the ActionProvider to give it to Facederp, G+, etc.
     * @return Intent
     */
    private Intent getDefaultIntent(String idea)
    {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TITLE,"CSH Project Idea Generator");
        //pull of the stack
        if(theStack.size() > 0)
        {
            //Stuff to share + tagline 
            intent.putExtra(Intent.EXTRA_TEXT, idea + "-CSH Project Idea Generator");
        }
        else
        {
            //default
            intent.putExtra(Intent.EXTRA_TEXT,"No ideas!");
        }
        return(intent);
    }

    /**
     * Update the share intent
     */
    private void updateShareIntent(String idea)
    {
        if (shareActionProvider != null)
        {
            shareActionProvider.setShareIntent(getDefaultIntent(idea));
        }
    }

    //Handles about button...much simpler than using an ActivityProvider
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.actionSettings)
        {
            //get the version of the app in the form of a string
            String version = "";
            try
            {
                version = "v" +
                        getPackageManager().getPackageInfo(getPackageName(),0).versionName + "\n";
            }
            catch(PackageManager.NameNotFoundException ex)
            {
                ex.printStackTrace();
            }
            String messageTxt =
                    version +
                    "Created by:\n" +
                    "Schuyler Martin\n\n" +
                    "CSHer and student at RIT\n" +
                    "GitHub: schuylermartin45\n\n" +
                    "Open source since 2014";
            displayStandardDialog("CSH Project Idea Generator", messageTxt);
            return(true);
        }
        if(item.getItemId() == R.id.actionTotalNum)
        {
            displayStandardDialog("Total Number of Ideas Possible:",
                    NumberFormat.getNumberInstance(getResources().getConfiguration().locale)
                    .format(IdeaGenerator.calculateIdeas()));
            return(true);
        }
        return(false);
    }

    /**
     * Clean up the process for generating the "standard" dialog/message
     * box for this program
     * These dialogs share a common style and disappear when the message gets tapped on
     * @param titleTxt Title of the dialog
     * @param messageTxt Body text of the dialog
     */
    private void displayStandardDialog(String titleTxt, String messageTxt)
    {
        //build a simple dialog block with some stuff
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(
                new ContextThemeWrapper(this, R.style.CSHDialog));
        alertBuilder.setTitle(titleTxt);
        alertBuilder.setMessage(messageTxt);

        final AlertDialog alert = alertBuilder.show();
        //formatting
        TextView message = (TextView)alert.findViewById(android.R.id.message);
        message.setGravity(Gravity.CENTER);
        //Hide on click
        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.cancel();
            }
        });
        alert.show();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
    }
}
