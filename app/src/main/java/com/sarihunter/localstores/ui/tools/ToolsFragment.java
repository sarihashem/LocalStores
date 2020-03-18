package com.sarihunter.localstores.ui.tools;

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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.sarihunter.localstores.Main3Activity;
import com.sarihunter.localstores.MainActivity;
import com.sarihunter.localstores.R;
import com.sarihunter.localstores.classes.User;
import com.squareup.picasso.Picasso;

import static android.app.Activity.RESULT_OK;

//my profile fragment

public class ToolsFragment extends Fragment {

    private ToolsViewModel toolsViewModel;

    private TextView email;
    private TextView displayName, phoneTv;
    private ImageView userImgView, coverIV;
    private FloatingActionButton fabEdit;

    private ProgressDialog pd;

    //storage
    private StorageReference firebaseStorage;
    private String path = "Users_Profile_Cover_Imgs/";

    private FirebaseAuth mAuth;
    // uri of picked image
    private Uri uriImage;

    private FirebaseUser cUser;

    DatabaseReference databaseReference;

    //check if it is profile image or cover image
    String profileORcover;

    private static final int CAMER_REQUEST_CODE = 100;
    private static final int STORAGE_REQUEST_CODE = 200;
    private static final int IMAGE_PICK_CAMERA_CODE = 300;
    private static final int IMAGE_PICK_GALLERY_CODE = 400;

    String cameraPermession[];
    String storagePermession[];


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {



        setHasOptionsMenu(true);

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            startActivity(new Intent(getContext(), MainActivity.class));
            Toast.makeText(getContext(), "please sign in", Toast.LENGTH_LONG).show();
        }
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_tools, container, false);


        cameraPermession = new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermession = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        email = root.findViewById(R.id.emailTVProfile);
        displayName = root.findViewById(R.id.nameTVProfile);
        userImgView = root.findViewById(R.id.ivUserEditProfile);
        coverIV = root.findViewById(R.id.coverIV);


        phoneTv = root.findViewById(R.id.phoneTVProfile);
        fabEdit = root.findViewById(R.id.fabEdit);

        pd = new ProgressDialog(getActivity());
        mAuth = FirebaseAuth.getInstance();
        cUser = mAuth.getCurrentUser();

        databaseReference = FirebaseDatabase.getInstance().getReference("Users");
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        //Query query = databaseReference.child(cUser.getUid()).orderByChild("userID").equalTo(cUser.getUid());

        @NonNull
        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference("Users").child(cUser.getUid());

        if(mRef != null){

            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    Log.i("datasnapshot is ", "null");

                    if (dataSnapshot.getValue() != null) {


                        Log.i("entered snapshot", dataSnapshot.getValue().toString());
                        User user = dataSnapshot.getValue(User.class);
                        String coverURL = user.getCoverImg();
                        String name = user.getName();
                        String phone = user.getPhone();
                        String photoURL = user.getPhotoURL();

                        email.setText(cUser.getEmail());

                        try {
                            displayName.setText(name);
                            phoneTv.setText(phone);

                            try {
                                if (user.getPhotoURL() != null) {

                                    //Picasso.with(root.getContext()).load(photoURL).into(userImgView);
                                    Glide.with(root.getContext())
                                            .load(photoURL)
                                            .apply(RequestOptions.circleCropTransform())
                                            .into(userImgView);

                                }

                            } catch (Exception e) {
                            }

                            try {

                                if (user.getCoverImg() != null) {

                                    Picasso.with(root.getContext()).load(coverURL).into(coverIV);
//                                Glide.with(root.getContext())
//                                        .load(coverURL)
//                                        .apply(RequestOptions.circleCropTransform())
//                                        .into(coverIV);
                                }
                            } catch (Exception e) {
                            }

                        } catch (Exception e) {
                        }
                    }else{
                        Toast.makeText(getContext(), "please sign in to your account or register new user",Toast.LENGTH_LONG).show();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else {
            Toast.makeText(getActivity(), "can't connect to DB, Check your connection",Toast.LENGTH_SHORT).show();
        }

        fabEdit.setOnClickListener(v -> {
            showEditDialog();


        });



//
//        if(mUser != null){
//
//            email.setText(mUser.getEmail());
//            displayname.setText(mUser.getDisplayName());
//            phone.setText(mUser.getPhoneNumber());
//        }

        //Offline support
        //FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        return root;
    }

    private Boolean checkStoragePermession() {
        //check if permession to storage is enabled

        return ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_DENIED);
    }

    private void requestStoragePermession() {

        requestPermissions(storagePermession, STORAGE_REQUEST_CODE);

    }

    private Boolean checkCameraPermession() {
        //check if permession to storage is enabled

        Boolean result = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        Boolean result1 = ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result && result1;
    }

    private void requestCameraPermession() {

        requestPermissions(cameraPermession, CAMER_REQUEST_CODE);

    }

    private void showEditDialog() {

        //show dialog to edit Either Img , name, ...etc
        String[] options = {"Profile Image", "Cover Image", "Name", "phone"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose what do you want to Edit");
        builder.setItems(options, (dialog, which) -> {

            if (which == 0) {
                //edit profile image
                pd.setMessage("updating image");
                profileORcover = "photoURL";
                showImgDialog();
            } else if (which == 1) {
                ////edit cover
                pd.setMessage("updating cover");
                profileORcover = "coverImg";
                showImgDialog();
            } else if (which == 2) {
                //edit name
                pd.setMessage("updating name");
                updatePhoneName("name");
            } else if (which == 3) {
                //edit phone
                pd.setMessage("updating phone number");
                updatePhoneName("phone");

            }

        });

        builder.create().show();
    }

    private void updatePhoneName(String key) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("update " + key);

        LinearLayout linearLayout = new LinearLayout(getActivity());
        linearLayout.setOrientation(LinearLayout.VERTICAL);
        linearLayout.setPadding(5, 5, 5, 5);
        EditText editText = new EditText(getActivity());
        editText.setHint("please enter new" + key);

        linearLayout.addView(editText);

        builder.setView(linearLayout);

        builder.setPositiveButton("Update", (dialog, which) -> {

            DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference("Users");



            String value = editText.getText().toString().trim();
            if (!TextUtils.isEmpty(value)) {

                pd.show();
//                HashMap<String, Object> result = new HashMap<>();
//
//                result.put(key, value);

                if(key =="name"){

                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(value)
                            .build();

                    cUser.updateProfile(profileUpdates);

                }

                dbRef.child(cUser.getUid()).child(key).setValue(value)

                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(getActivity(), "updated successfully", Toast.LENGTH_LONG).show();

                                pd.dismiss();


                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getActivity(), "" + e.getMessage(), Toast.LENGTH_LONG).show();
                        pd.dismiss();

                    }
                });


            } else {
                Toast.makeText(getActivity(), "Enter" + key, Toast.LENGTH_SHORT).show();

            }

        });

        builder.setNegativeButton("cancel", (dialog, which) -> {

            dialog.dismiss();

        });

        builder.create().show();

    }

    public void showImgDialog() {
        //show gallery or talk photo
        //show dialog to edit Either Img , name, ...etc
        String[] options = {"Take image", "Choose Image"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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
                        Toast.makeText(getActivity(), "please enable camera and storage permessions", Toast.LENGTH_LONG).show();
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
                        Toast.makeText(getActivity(), "please enable storage permessions", Toast.LENGTH_LONG).show();
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
        uriImage = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
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

    private void uploadImage(Uri uriImage) {

        pd.show();
        firebaseStorage = FirebaseStorage.getInstance().getReference();


        String imagePath = path + "" + profileORcover+"_" + cUser.getUid();

        StorageReference storageReference2 = firebaseStorage.child(imagePath);
        //DatabaseReference dbRef2 = FirebaseDatabase.getInstance().getReference("Users");

        storageReference2.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();

                while (!uriTask.isSuccessful()) ;

                Uri downloadUri = uriTask.getResult();

                Log.i("photo URL",uriImage.toString());

                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                        .setPhotoUri(downloadUri)
                        .build();

                cUser.updateProfile(profileUpdates);





                if (uriTask.isSuccessful()) {
                    //imageUploaded
                    //update user image
                    //HashMap<String, Object> results = new HashMap();
                    //dbRef2.child(cUser.getUid()).child(profileORcover).setValue(downloadUri);
                    databaseReference.child(cUser.getUid()).child(profileORcover).setValue(downloadUri.toString()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            //image uri added to user dismiss pd;
                            Log.i("insideDBRef","the value updated");
                            pd.dismiss();
                            Toast.makeText(getActivity(), " updated successfully", Toast.LENGTH_LONG).show();


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            pd.dismiss();
                            Toast.makeText(getActivity(), " couldn't update", Toast.LENGTH_LONG).show();


                        }
                    });
                } else {
                    pd.dismiss();

                    Toast.makeText(getActivity(), "error occured", Toast.LENGTH_LONG).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                pd.dismiss();
                Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();

            }
        });


    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        MenuItem item=menu.findItem(R.id.search);
        if(item!=null)
            item.setVisible(false);
    }

    public void checkStatus(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user == null){
            //user isn't logged in go to main activity
            startActivity(new Intent(getContext(), MainActivity.class));
            getActivity().finish();

        }
    }

    @Override
    public void onStart() {
        super.onStart();
        checkStatus();
    }

    @Override
    public void onStop() {
        super.onStop();
        startActivity(new Intent(getContext(), Main3Activity.class));
    }
}