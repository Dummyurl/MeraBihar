package app.zingo.merabihar.UI.ActivityScreen.Events;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import app.zingo.merabihar.R;

public class TouristMapImages extends AppCompatActivity {



    private static FrameLayout bhagalpur_map,darbhanga_map,gaya_map,muzaffarpur_map,
            nalanda_map,patna_map,sitamarhi_map,vaishali_map,west_champaran_map;

    private static final String AUTHORITY="app.zingo.merabihar.fileprovider";

    private FileOutputStream outStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try{

            setContentView(R.layout.activity_tourist_map_images);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
            setTitle("Tourist Map");

            bhagalpur_map = (FrameLayout)findViewById(R.id.bhagalpur_frame);
            darbhanga_map = (FrameLayout)findViewById(R.id.darbhanga_frame);
            gaya_map = (FrameLayout)findViewById(R.id.gaya_map_frame);
            muzaffarpur_map = (FrameLayout)findViewById(R.id.muzaffarpur_frame);
            nalanda_map = (FrameLayout)findViewById(R.id.nalanda_frame);
            patna_map = (FrameLayout)findViewById(R.id.patna_frame);
            sitamarhi_map = (FrameLayout)findViewById(R.id.sitamarhi_frame);
            vaishali_map = (FrameLayout)findViewById(R.id.vaishali_frame);
            west_champaran_map = (FrameLayout)findViewById(R.id.west_champaran_frame);


            bhagalpur_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.bhagalpur_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "bhagalpur_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/bhagalpur_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));


                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            darbhanga_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.darbhanga_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "darbhanga_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/darbhanga_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            gaya_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.gaya_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "gaya_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/gaya_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            muzaffarpur_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.muzaffarpur_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "muzaffarpur_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/muzaffarpur_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            nalanda_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.nalanda_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "nalanda_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/nalanda_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            patna_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.patna_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "patna_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/patna_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            sitamarhi_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.sitamarhi_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "sitamarhi_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/sitamarhi_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            vaishali_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.vaishali_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "vaishali_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/vaishali_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });

            west_champaran_map.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Bitmap bm = BitmapFactory.decodeResource( getResources(), R.drawable.west_champaran_map);
                    String extStorageDirectory = Environment.getExternalStorageDirectory().toString();
                    File file = new File(extStorageDirectory, "west_champaran_map.jpg");
                    try {
                        outStream = new FileOutputStream(file);

                        Toast.makeText(TouristMapImages.this, "Downloading...", Toast.LENGTH_SHORT).show();
                        bm.compress(Bitmap.CompressFormat.PNG, 100, outStream);
                        outStream.flush();
                        outStream.close();

                        File root = Environment.getExternalStorageDirectory();

                        String pathToMyAttachedFile = "/west_champaran_map.jpg";
                        // System.out.println("File Name="+root+pathToMyAttachedFile);
                        File files = new File(root, pathToMyAttachedFile);
                        if (!files.exists() || !files.canRead()) {


                            return;
                        }
                        //  Uri uri = Uri.fromFile(file);
                        // System.out.println("Uri==="+uri);
                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        Uri photoURI =  FileProvider.getUriForFile(TouristMapImages.this, AUTHORITY, files);
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        intent.setDataAndType(photoURI,"image/*");
                        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        startActivity(Intent.createChooser(intent, "View using"));

                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                }
            });



        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id)
        {
            case android.R.id.home:
                TouristMapImages.this.finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }
}
