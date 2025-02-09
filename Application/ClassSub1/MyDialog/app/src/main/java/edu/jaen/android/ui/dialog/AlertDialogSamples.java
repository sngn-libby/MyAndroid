package edu.jaen.android.ui.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class AlertDialogSamples extends Activity {
    private static final int DIALOG_YES_NO_MESSAGE = 1;
    private static final int DIALOG_YES_NO_LONG_MESSAGE = 2;
    private static final int DIALOG_LIST = 3;
    private static final int DIALOG_PROGRESS = 4;
    private static final int DIALOG_SINGLE_CHOICE = 5;
    private static final int DIALOG_MULTIPLE_CHOICE = 6;
    private static final int DIALOG_TEXT_ENTRY = 7;

    private static final int MAX_PROGRESS = 100;
    
    private ProgressDialog mProgressDialog;
    private int mProgress;
    private Handler mProgressHandler;

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_YES_NO_MESSAGE:
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.alert_dialog_two_buttons_title)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked OK so do some stuff */
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Cancel so do some stuff */
                    }
                })
                .create();
        case DIALOG_YES_NO_LONG_MESSAGE:
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.alert_dialog_two_buttons_msg)
                .setMessage(R.string.alert_dialog_two_buttons2_msg)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
    
                        /* User clicked OK so do some stuff */
                    }
                })
                .setNeutralButton(R.string.alert_dialog_something, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Something so do some stuff */
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Cancel so do some stuff */
                    }
                })
                .create();
        case DIALOG_LIST:
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setTitle(R.string.select_dialog)
                .setItems(R.array.select_dialog_items, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        /* User clicked so do some stuff */
                        String[] items = getResources().getStringArray(R.array.select_dialog_items);
                        new AlertDialog.Builder(AlertDialogSamples.this)
                                .setMessage("You selected: " + which + " , " + items[which])
                                .show();
                    }
                })
                .create();
        case DIALOG_PROGRESS:
            mProgressDialog = new ProgressDialog(AlertDialogSamples.this);
            mProgressDialog.setIcon(R.drawable.alert_dialog_icon);
            mProgressDialog.setTitle(R.string.select_dialog);
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(MAX_PROGRESS);
            mProgressDialog.setButton(getText(R.string.alert_dialog_hide), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked Yes so do some stuff */
                }
            });
            mProgressDialog.setButton2(getText(R.string.alert_dialog_cancel), new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {

                    /* User clicked No so do some stuff */
                }
            });
            return mProgressDialog;
        case DIALOG_SINGLE_CHOICE:
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.alert_dialog_single_choice)
                .setSingleChoiceItems(R.array.select_dialog_items2, 0, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked on a radio button do some stuff */
                    }
                })
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Yes so do some stuff */
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked No so do some stuff */
                    }
                })
               .create();
        case DIALOG_MULTIPLE_CHOICE:
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setIcon(R.drawable.ic_popup_reminder)
                .setTitle(R.string.alert_dialog_multi_choice)
                .setMultiChoiceItems(R.array.select_dialog_items3,
                        new boolean[]{false, true, false, true, false, false, false},
                        new DialogInterface.OnMultiChoiceClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton,
                                    boolean isChecked) {

                                /* User clicked on a check box do some stuff */
                            }
                        })
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked Yes so do some stuff */
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked No so do some stuff */
                    }
                })
               .create();
        case DIALOG_TEXT_ENTRY:
            // This example shows how to add a custom layout to an AlertDialog
            LayoutInflater factory = LayoutInflater.from(this);
            final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
            return new AlertDialog.Builder(AlertDialogSamples.this)
                .setIcon(R.drawable.alert_dialog_icon)
                .setTitle(R.string.alert_dialog_text_entry)
                .setView(textEntryView)
                .setPositiveButton(R.string.alert_dialog_ok, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
    
                        /* User clicked OK so do some stuff */
                    }
                })
                .setNegativeButton(R.string.alert_dialog_cancel, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        /* User clicked cancel so do some stuff */
                    }
                })
                .create();
        }
        return null;
    }

    /**
     * Initialization of the Activity after it is first created.  Must at least
     * call {@link android.app.Activity#setContentView(int)} to
     * describe what is to be displayed in the screen.
     */
    @Override
	protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.alert_dialog);
                
        /* Display a text message with yes/no buttons and handle each message as well as the cancel action */
        Button twoButtonsTitle = (Button) findViewById(R.id.two_buttons);
        twoButtonsTitle.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_YES_NO_MESSAGE);
            }
        });
        
        /* Display a long text message with yes/no buttons and handle each message as well as the cancel action */
        Button twoButtons2Title = (Button) findViewById(R.id.two_buttons2);
        twoButtons2Title.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_YES_NO_LONG_MESSAGE);
            }
        });
        
        
        /* Display a list of items */
        Button selectButton = (Button) findViewById(R.id.select_button);
        selectButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_LIST);
            }
        });
        
        /* Display a custom progress bar */
        Button progressButton = (Button) findViewById(R.id.progress_button);
        progressButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_PROGRESS);
                mProgress = 0;
                mProgressDialog.setProgress(0);
                mProgressHandler.sendEmptyMessage(0);
            }
        });
        
        /* Display a radio button group */
        Button radioButton = (Button) findViewById(R.id.radio_button);
        radioButton.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_SINGLE_CHOICE);
            }
        });
        
        /* Display a list of checkboxes */
        Button checkBox = (Button) findViewById(R.id.checkbox_button);
        checkBox.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_MULTIPLE_CHOICE);
            }
        });
        
        /* Display a text entry dialog */
        Button textEntry = (Button) findViewById(R.id.text_entry_button);
        textEntry.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                showDialog(DIALOG_TEXT_ENTRY);
            }
        });
        
        mProgressHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                if (mProgress >= MAX_PROGRESS) {
                    mProgressDialog.dismiss();
                } else {
                    mProgress++;
                    mProgressDialog.incrementProgressBy(1);
                    mProgressHandler.sendEmptyMessageDelayed(0, 100);
                }
            }
        };
    }
 
}
