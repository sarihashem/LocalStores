package com.sarihunter.localstores;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sarihunter.localstores.adapters.ProductAdapter;
import com.sarihunter.localstores.classes.Items;
import com.sarihunter.localstores.ui.gallery.GalleryFragment;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;

public class ProductDetailss extends AppCompatActivity {

    ImageView ivItem, ivDelete, ivEdit;
    TextView title, description, date, price;
    Button contact,buy,addToCart;

    RecyclerView rv;
    ProductAdapter productAdapter;
    Toolbar toolbar;
    private StorageReference firebaseStorage;
    //DatabaseReference databaseReference;

    private Uri uriImage;
    String imgUrl, titlename, descript, datte;
    int rateing; double pricee;

    String itemID;
    FirebaseUser cUser;

    private static final int CAMER_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String cameraPermession[];
    String storagePermession[];
    ProgressDialog pd;

    DatabaseReference myRef;

    private String fixerAPIKEY = "0a9f9a593cf4b2e83cac04113bfe1ff5";
    private String fixerAPIEndPoints = "http://data.fixer.io/api/latest?access_key=0a9f9a593cf4b2e83cac04113bfe1ff5";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_detailss);


        try {
           JSONObject object = readJsonFromUrl(fixerAPIEndPoints);
           Log.i("json object", object.toString());

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //TODO: 3| add currency changer : create special class to handle it
        //TODO: 2| insert map fragment

        //database firebase
        //account type (1 == admin, 0 == customer )

        toolbar = findViewById(R.id.toolbar_product);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().hide();
        //toolbar.setTitle("");

        pd = new ProgressDialog(this);
        cameraPermession = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermession = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        ivDelete = findViewById(R.id.ivDelete);
        ivEdit = findViewById(R.id.ivEditItem);

        //set delete action if the owner of the item is the current user and show toolbar
        ivItem = findViewById(R.id.imageView2);
        title = findViewById(R.id.tvNamDetail);
        description = findViewById(R.id.tvDesctiptionDetail);
        date = findViewById(R.id.tvDateDetailes);
        contact = findViewById(R.id.contactSeller);

        addToCart = findViewById(R.id.bAddToCart);

        price = findViewById(R.id.tvpriceDetails);
        rv = findViewById(R.id.rvItemsSameSeller);

        Intent result = getIntent();
        itemID = result.getExtras().getString("itemID");
        String owner = result.getExtras().getString("owner");
        String ownerID = result.getExtras().getString("OwnerID");

        cUser = FirebaseAuth.getInstance().getCurrentUser();

        String currentUser = cUser.getEmail();

        deleteEditOptions(owner, currentUser, itemID);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        firebaseStorage = FirebaseStorage.getInstance().getReference();




        //get a reference for the item
        myRef = database.getReference("Items").child(itemID);

        Query query = FirebaseDatabase.getInstance()
                .getReference()
                .child("Items").orderByChild("owner")
                .equalTo(owner);

        FirebaseRecyclerOptions<Items> options = new FirebaseRecyclerOptions.Builder<Items>().
                setQuery(query,Items.class).build();

        productAdapter = new ProductAdapter(options);


        // Read from the database
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                Items value = dataSnapshot.getValue(Items.class);

                imgUrl = value.getPhotoUrl();
                titlename = value.getName();
                descript = value.getDescription();
                datte = value.getDatePublished();
                rateing = value.getRate();
                pricee = value.getPrice();
                title.setText(titlename);
                description.setText(descript);
                date.setText(datte);

                //TODO: add addToCart later
                addToCart.setVisibility(View.INVISIBLE);
                price.setText(String.valueOf(value.getPrice()));

                try {

                    Picasso.with(getApplicationContext()).load(imgUrl).into(ivItem);
                }catch (Exception e) {
                }

//
//                Tags tags = new Tags();
//
//                MainAdapter adapter = new MainAdapter(tags.fillWith("more from the same seller"),productAdapter);
                rv.setAdapter(productAdapter);
                rv.setLayoutManager(new GridLayoutManager(getApplicationContext(),3));

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value

            }
        });

        contact.setOnClickListener(v->{
            startActivity(new Intent(this,ChattingActivity.class)
                    .putExtra("owner",owner)
                    .putExtra("OwnerID",ownerID));


        });

//        addToCart.setOnClickListener(v -> {
//            DatabaseReference addtocartRef = database.getReference("Cart").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push();
//            addtocartRef.getKey();
//
//            addtocartRef.setValue(itemID);
//
//            Toast.makeText(this,"added to cart successfuly :)", Toast.LENGTH_LONG).show();
//
//            //startActivity(new Intent(this,addToCart.class));
//        });



    }

    private void deleteEditOptions(String owner, String myRef, String itemID) {
        //if the owner and the current user are the same show toolbar and enable the delete action

        Log.i("Enter","Entered delete");
        Log.i("owner",owner);
        Log.i("myRef",myRef);

        if(owner.equalsIgnoreCase(myRef)){


            Log.i("Result","owner == cUser.email");
            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Items").child(itemID);
            getSupportActionBar().show();

            //toolbar.setVisibility(View.VISIBLE);
            //getSupportActionBar().show();

            ivDelete.setOnClickListener(v-> {

                dbRef.setValue(null).addOnSuccessListener(aVoid ->  {
                    Toast.makeText(this,"item removed successfully",Toast.LENGTH_LONG).show();
                    startActivity(new Intent(this, GalleryFragment.class));
                }).addOnFailureListener(aVoid ->{
                    Toast.makeText(this,"something wrong happened try later",Toast.LENGTH_SHORT).show();

                });

            });

            ivEdit.setOnClickListener(v -> {
                // create Dialog to Change Title, description, price, imageView.

                showEditDialog();


            });

        }
    }

    private void showEditDialog() {

        //show dialog to edit Either Img , name, ...etc
        String[] options = {"Image", "Title", "Description", "price"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose what do you want to Edit");
        builder.setItems(options, (dialog, which) -> {

            if (which == 0) {
                //edit  image
                pd.setMessage("updating image");
                showImgDialog();



            } else if (which == 1) {
                ////edit title
                pd.setMessage("updating Title");
                updateTitleDescriptionPrice("name");


            } else if (which == 2) {
                //edit description
                pd.setMessage("updating description");
                updateTitleDescriptionPrice("description");

            } else if (which == 3) {
                //edit price
                pd.setMessage("updating price");
                updateTitleDescriptionPrice("price");


            }

        });

        builder.create().show();
    }

    private void updateTitleDescriptionPrice(String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(ProductDetailss.this);
        builder.setTitle("update " + key);

        LinearLayout linearLayout = new LinearLayout(ProductDetailss.this);
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);
        EditText editText = new EditText(ProductDetailss.this);
        editText.setHint("please enter new" + key);

        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", (dialog, which) -> {


            String value = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(value)) {

                if(key.equals("price")){


                    pd.show();

                    try{
                        Double price = Double.parseDouble(value);
                        myRef.child(key).setValue(price)

                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {

                                        Toast.makeText(ProductDetailss.this, "updated successfully", Toast.LENGTH_LONG).show();

                                        pd.dismiss();


                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(ProductDetailss.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                                pd.dismiss();

                            }
                        });

                    }catch (Exception e){
                        pd.dismiss();
                        Toast.makeText(ProductDetailss.this, "please enter valid number" , Toast.LENGTH_LONG).show();

                    }

                }


                else{
                    pd.show();

                    myRef.child(key).setValue(value)

                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {

                                    Toast.makeText(ProductDetailss.this, "updated successfully", Toast.LENGTH_LONG).show();

                                    pd.dismiss();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ProductDetailss.this, "" + e.getMessage(), Toast.LENGTH_LONG).show();
                            pd.dismiss();

                        }
                    });
                }

            } else {
                Toast.makeText(ProductDetailss.this, "Enter" + key, Toast.LENGTH_SHORT).show();

            }

        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

            dialog.dismiss();

        });

        builder.create().show();

    }

    @Override
    protected void onStart() {
        super.onStart();
        productAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        productAdapter.stopListening();
    }

    public void showImgDialog() {
        //show gallery or talk photo
        //show dialog to edit Either Img , name, ...etc
        String[] options = {"Take image", "Choose Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pick Image");
        builder.setItems(options, (dialog, which) -> {

            if (which == 0) {
                //camera


                if (!checkCameraPermession()) {
                    //no access
                    requestCameraPermession();
                } else {
                    pickFromCamera();
                }
            } else if (which == 1) {
                ////gallery

                if (!checkStoragePermession()) {
                    requestStoragePermession();

                } else {
                    openGallery();
                }
            }

        });

        builder.create().show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        //handle permession if granted or denied

        switch (requestCode) {
            case CAMER_REQUEST_CODE: {
                //check if request granted for or not then open choose camera
                if (grantResults.length > 0) {
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean writeStorageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && writeStorageAccepted) {
                        //permession enabled;
                        pickFromCamera();
                    } else {
                        //permession denied
                        Toast.makeText(this, "please enable camera and storage permessions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;

            case STORAGE_REQUEST_CODE: {
                //check if request granted for or not then open choose camera
                if (grantResults.length > 0) {

                    boolean writeStorageAccepted = (grantResults[0] == PackageManager.PERMISSION_GRANTED);

                    if (writeStorageAccepted) {
                        //permession enabled;
                        openGallery();
                    } else {
                        //permession denied
                        Toast.makeText(this, "please enable storage permessions", Toast.LENGTH_LONG).show();
                    }
                }
            }
            break;
        }
    }

    private void pickFromCamera() {

        //intent to pick image from device gallery

        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.Images.Media.TITLE, "Temp Pic");
        contentValues.put(MediaStore.Images.Media.DESCRIPTION, "Temp Description");
        //put image uri
        uriImage = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
        //Intent to start camera
        Intent startCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(startCamera, IMAGE_PICK_CAMERA_CODE);

    }

    private void openGallery() {
        //intent to pick image from device camera

        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent, IMAGE_PICK_GALLERY_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {

            if (requestCode == IMAGE_PICK_GALLERY_CODE) {

                //get image uri

                uriImage = data.getData();
                uploadImage(uriImage);

            }

            if (requestCode == IMAGE_PICK_CAMERA_CODE) {
                uploadImage(uriImage);

            }


        }
    }

    int c = 0;

    private void uploadImage(Uri uriImage) {

        pd.show();



        String imagePath = "images/"+ cUser.getUid() +"/"+itemID + c++;

        StorageReference storageReference2 = firebaseStorage.child(imagePath);
        //DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("Users");

        storageReference2.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful()) ;

                Uri downloadUri = uriTask.getResult();

                Log.i("photo URL",uriImage.toString());


                if (uriTask.isSuccessful()) {
                    //imageUploaded
                    //update user image
                    //HashMap<String, Object> results = new HashMap();
                    //dbRef2.child(cUser.getUid()).child(profileORcover).setValue(downloadUri);
                    myRef.child("photoUrl").setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //image uri added to user dismiss pd;
                            Log.i("insideDBRef","the value updated");
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), " updated successfully", Toast.LENGTH_LONG).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getApplicationContext(), " couldn't update", Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pd.dismiss();

                    Toast.makeText(getApplicationContext(), "error occured", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    private Boolean checkStoragePermession() {
        //check if permession to storage is enabled

        return ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);
    }

    private void requestStoragePermession() {

        requestPermissions(storagePermession, STORAGE_REQUEST_CODE);

    }

    private Boolean checkCameraPermession() {
        //check if permession to storage is enabled

        Boolean result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        Boolean result1 = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermession() {

        requestPermissions(cameraPermession, CAMER_REQUEST_CODE);

    }

    public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
        InputStream is = new URL(url).openStream();
        Log.i("jsonFromURL entered", is.toString());
        try {
            BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
            String jsonText = readAll(rd);
            JSONObject json = new JSONObject(jsonText);
            Log.i("have the json", jsonText);
            return json;
        } finally {
            is.close();
        }
    }
    private static String readAll(Reader rd) throws IOException {
        StringBuilder sb = new StringBuilder();
        int cp;
        while ((cp = rd.read()) != -1) {
            sb.append((char) cp);
        }
        return sb.toString();
    }
}
