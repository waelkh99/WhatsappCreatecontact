package com.example.whatsappcreatecontact;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.FileUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Wallpapers extends AppCompatActivity {

    StorageReference storageReference;
    ArrayList<Bitmap> images;
    GridView gridView;
    GridAdapter gridAdapter;
    Button openConversation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallpapers);

        openConversation=(Button)findViewById(R.id.openConversationBtn);
        openConversation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        images=new ArrayList<Bitmap>();

        gridView=(GridView)findViewById(R.id.gridView);

        storageReference=FirebaseStorage.getInstance().getReference().child("wallpapers");

        
        storageReference.listAll().addOnSuccessListener(new OnSuccessListener<ListResult>() {
            @Override
            public void onSuccess(ListResult listResult) {
                for (StorageReference item : listResult.getItems()) {
                    try {
                        File temp=File.createTempFile("pic","jpg");
                        item.getFile(temp).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                                gridAdapter=new GridAdapter(getApplicationContext(),images);
                                gridView.setAdapter(gridAdapter);
                                Bitmap bitmap=BitmapFactory.decodeFile(temp.getAbsolutePath());
                                images.add(gridAdapter.getCount(),bitmap);
                                Log.d("counter",String.valueOf(gridAdapter.getCount()));



                            }
                        });
                    }
                    catch (Exception e){

                    }
                }

                
            }
        });

            gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int pos, long l) {
                    Log.d("selected",String.valueOf(pos+1));
                    new AlertDialog.Builder(Wallpapers.this).setTitle("Download this Image?").setMessage("This image is going to be installed on your device")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String root = Environment.getExternalStorageDirectory().toString();
                                    File myDir = new File(root + "/gift_cards");
                                    if (!myDir.exists()) {
                                        myDir.mkdirs();
                                    }
                                    Random generator = new Random();
                                    int n = 10000;
                                    n = generator.nextInt(n);
                                    String fname = "Image-"+ n +".jpg";
                                    File file = new File (myDir, fname);
                                    if (file.exists ())
                                        file.delete ();
                                    try {
                                        FileOutputStream out = new FileOutputStream(file);
                                        Bitmap finalBitmap;

                                            finalBitmap= (Bitmap) adapterView.getAdapter().getItem(pos);


                                        finalBitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                                        out.flush();
                                        out.close();
                                        Toast.makeText(Wallpapers.this, "File downloaded to "+root+"/gift_cards", Toast.LENGTH_LONG).show();

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }

                                }
                            }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                        }
                    }).create().show();

                }

            });



    }
}