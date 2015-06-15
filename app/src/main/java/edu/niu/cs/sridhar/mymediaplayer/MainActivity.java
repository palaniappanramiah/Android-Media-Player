package edu.niu.cs.sridhar.mymediaplayer;

import android.app.ListActivity;
import android.media.MediaPlayer;
import android.os.Environment;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ListActivity {
    Button stopBtn;
    private String sdPath = Environment.getExternalStorageDirectory().getAbsolutePath();
    List<String> songs = new ArrayList<>();
    MediaPlayer mPlayer = new MediaPlayer();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        upDatePlaylist();
        stopBtn = (Button)findViewById(R.id.buttonStop);
        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPlayer.stop();
            }
        });
    }

    public void onListItemClick(ListView List, View v, int pos, long id)
    {
        try
        {
            mPlayer.reset();
            mPlayer.setDataSource(sdPath + "/" +songs.get(pos));
            mPlayer.prepare();
            mPlayer.start();
        }
        catch (IOException e)
        {
            Log.d("MP3", e.getMessage());
        }
    }

    private void upDatePlaylist() {
        //This will use the mp3 filter class to find all the files
        // on the sd card that have .mp3 extension
        File home = new File(sdPath);
        if(home.listFiles(new MP3Filter()).length >0 )
        {
            for (File file:home.listFiles(new MP3Filter()))
            {
                songs.add(file.getName());
            }//end for
        }//end if

        ArrayAdapter<String> songList = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1,songs);
        setListAdapter(songList);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class MP3Filter implements FilenameFilter
    {

        @Override
        public boolean accept(File dir, String filename) {

            return (filename.endsWith(".mp3")) ;
        }
    }
}
