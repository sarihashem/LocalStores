package com.sarihunter.localstores;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatDialogFragment;

import com.sarihunter.localstores.classes.Helper;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;

public class AddProductDialog extends AppCompatDialogFragment {

    private TextInputEditText name1, price1, description, etQuantity;
    private ImageButton ibItem;
    private Button barcodeButton;
    private EditText barcodeNum;
    private Spinner categorySpinner;
    private FirebaseAuth mAuth;

    private String itemImgURL;

    public static String id;

    public static final int PICK_IMAGE = 1;
    public static final int REQUEST_IMAGE_CAPTURE = 2;
    private static final int IMAGE_PICK_CAMERA_CODE = 100;


    LocationManager locationManager;


    Uri uriImage;
    private DatabaseReference items;
    Helper helper = new Helper();



    //TODO 1| implement the location of item


    @SuppressLint("MissingPermission")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();


//        locationManager = (LocationManager) view.getContext().getSystemService(Context.LOCATION_SERVICE);
//
//        if(Manifest.permission.ACCESS_FINE_LOCATION.equals( PackageManager.PERMISSION_GRANTED) || (Manifest.permission.ACCESS_COARSE_LOCATION).equals( PackageManager.PERMISSION_GRANTED)){
//
//
//
//            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new LocationListener() {
//                            @Override
//                            public void onLocationChanged(Location location) {
//
//
//                            }
//
//                            @Override
//                            public void onStatusChanged(String provider, int status, Bundle extras) {
//
//                            }
//
//                            @Override
//                            public void onProviderEnabled(String provider) {
//
//                            }
//
//                            @Override
//                            public void onProviderDisabled(String provider) {
//
//                            }
//                        }
//                );
//
//
//
//        }
//


        items = FirebaseDatabase.getInstance().getReference().child("Items").push();

        id = items.getKey();


        builder.setView(view)
                .setTitle("Add Item")
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })

                .setPositiveButton("Done", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                        if (currentUser == null) {
                            Toast.makeText(getActivity(), "please sign in to add items", Toast.LENGTH_LONG).show();
                            return;
                            //startActivity(new Intent(getActivity().getApplication(),MainActivity.class));
                        } else {

                            int quantity;
                            Double iPrice = 0.0;
                            String itemName = name1.getText().toString();
                            String itemPrice = price1.getText().toString();
                            String descritpion = description.getText().toString();
                            String barcode = "0000";
                            String quanttity = "1";
                            //String currentUserID = currentUser.getUid();

                            if (quanttity == null) {
                                quantity = 1;
                            } else {
                                quantity = Integer.parseInt(quanttity);
                            }


                            if(TextUtils.isEmpty(itemName.trim())){
                                return;
                            }

                            if (itemPrice != null) {

                                iPrice = Double.parseDouble(itemPrice);

                            }

                            String category = getCategories()[categorySpinner.getSelectedItemPosition()];

                            if (items != null && id != null && itemName != null && iPrice != null && currentUser != null) {

                                helper.addItem(view.getContext(),items, id, itemName, iPrice, descritpion, currentUser, dateCreated(), barcode, category, quantity);
                            } else {
                                Toast.makeText(getContext(), "no items added", Toast.LENGTH_LONG).show();
                            }


                        }

                        //startActivity(new Intent(getActivity(), Main3Activity.class));

                    }
                });

        // load default values for price and quantity:


        name1 = view.findViewById(R.id.etName);
        price1 = view.findViewById(R.id.etPrice);
        description = view.findViewById(R.id.desctiption);
        ibItem = view.findViewById(R.id.ibItem);
        categorySpinner = view.findViewById(R.id.sCategory);

        price1.setText("0.0");
        price1.setSelectAllOnFocus(true);

        barcodeButton = view.findViewById(R.id.bBarcode);
        barcodeNum = view.findViewById(R.id.etBarcode);

        etQuantity = view.findViewById(R.id.etQuantity);

        ibItem.setOnClickListener(v -> {

            String [] options = {"Camera","Gallery"};

            AlertDialog.Builder pickfrom = new AlertDialog.Builder(getActivity());
            pickfrom.setTitle("from where ...")
                    .setItems(options, (dialog,which)->{

                        if(which == 0){
                            pickFromCamera();

                        }
                        if(which == 1 ){

                            pickImg();
                        }


                    });

            pickfrom.create().show();
            //takeImg();
        });


        barcodeButton.setVisibility(View.INVISIBLE);
        barcodeNum.setVisibility(View.INVISIBLE);
        etQuantity.setVisibility(View.INVISIBLE);

        //load categories
        loadSpinner(view);

        return builder.create();
    }



    private void loadSpinner(View view) {
        // put categories into spinner to choose one for category of the item

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                view.getContext(), android.R.layout.simple_spinner_item, getCategories());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


    }

    private String dateCreated() {

        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date date = new Date();
        return formatter.format(date);

    }

    public String[] getCategories() {
        return new String[]{"Computers and Electronics", "Home and Garden", "Vehicales", "Hobbies", "Entertainment", "Classifieds", "Family"};
    }

    private void pickImg() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(getActivity(), "something wrong happened", Toast.LENGTH_LONG).show();
                return;
            }


            Uri uriItem = data.getData();
            try {

                //upload image to firebase adding item id to the end of the path.

                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), uriItem);

                ibItem.setImageBitmap(bitmap);

                itemImgURL = helper.uploadFileToFirebase(uriItem, getActivity(), id);


            } catch (IOException e) {
                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();

            }
        }

        if (requestCode == REQUEST_IMAGE_CAPTURE  && resultCode == RESULT_OK) {
            if (data == null) {
                //Display an error
                Toast.makeText(getActivity(), "something wrong happened", Toast.LENGTH_LONG).show();
                return;
            }

            Uri uriItem = data.getData();
//            Bundle extras = data.getExtras();
//            Bitmap bitmap = (Bitmap) extras.get("data");
//            ibItem.setImageBitmap(bitmap);

            try {
                Bundle extras = data.getExtras();
                Bitmap imageBitmap = (Bitmap) extras.get("data");
                //itemImgURL = helper.uploadFileToFirebase(uriItem, getActivity(), id);

                ibItem.setImageBitmap(imageBitmap);
            } catch (NullPointerException e) {

            }

            itemImgURL = helper.uploadFileToFirebase(uriItem, getActivity().getApplication(), id);

        }
    }

    private void pickFromCamera() {

        //Intent to start camera
        Intent startCamera = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startCamera.putExtra(MediaStore.EXTRA_OUTPUT, uriImage);
        startActivityForResult(startCamera, IMAGE_PICK_CAMERA_CODE);

    }

}
