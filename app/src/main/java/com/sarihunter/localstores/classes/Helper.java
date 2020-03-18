package com.sarihunter.localstores.classes;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class Helper {
    //Firebase
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseUser mUser;
    static String s;
    ProgressDialog pd;


    public Helper() {

        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

    }

    public String uploadFileToFirebase(Uri file, Context context,String itemID){

        uploadImage(file,context,itemID);
        return s;

    }

    private void uploadImage(Uri filePath, Context context,String itemID) {

        pd = new ProgressDialog(context);
        pd.setTitle("uploading Image");
        pd.setMessage("Wait a sec :) ...");



        if(filePath != null)
        {

            pd.show();

            StorageReference ref = storageReference.child("images/"+ mUser.getUid() +"/"+itemID);
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                            Toast.makeText(context, "Uploaded", Toast.LENGTH_SHORT).show();

                            Task<Uri> task = ref.getDownloadUrl();
                            task.addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    s = uri.toString();
                                    pd.dismiss();

                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            Toast.makeText(context, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }


    public void addItem(Context context,DatabaseReference items, String id, String name, double price, String description,
                        FirebaseUser currentUser, String date, String barcode, String category, int quantity){




        String owner = currentUser.getEmail();
        String ownerID = currentUser.getUid();


        Items c = new Items(id, name,description,owner,ownerID,date,s,barcode,price,category,quantity);

        items.setValue(c).addOnSuccessListener(aVoid -> {

        });

    }






    public void showError(String msg, Context c) {
        new AlertDialog.Builder(c)
                .setTitle("Error")
                .setMessage(msg)
                .setPositiveButton("OK", (dialog, which) -> {
                })
                .show();
    }

}
