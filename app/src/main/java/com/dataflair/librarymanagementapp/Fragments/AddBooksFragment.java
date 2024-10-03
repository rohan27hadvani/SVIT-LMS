package com.dataflair.librarymanagementapp.Fragments;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import com.dataflair.librarymanagementapp.Activities.AdminActivity;
import com.dataflair.librarymanagementapp.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class AddBooksFragment extends Fragment implements AdapterView.OnItemSelectedListener  {

    Button submitBtn;
    ImageView imageView;
    EditText bookNameEditTxt,booksCountEditTxt,booksLocationEditText;
    DatabaseReference databaseReference;
    StorageReference storageReference;
    Uri imageUri;

    Spinner spinner;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_add_books, container, false);

        //Assigning the address of the android materials
        imageView = (ImageView) view.findViewById(R.id.BookImage);
        bookNameEditTxt= (EditText) view.findViewById(R.id.BookNameEditTxt);
        booksCountEditTxt= (EditText) view.findViewById(R.id.TotalBooksEditTxt);
        booksLocationEditText = (EditText) view.findViewById(R.id.BookLocationEditTxt);

        // Spinner element
        spinner = (Spinner)view.findViewById(R.id.Spinner);

        submitBtn = (Button) view.findViewById(R.id.AddBookBtn);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("AllBooks");
        storageReference = FirebaseStorage.getInstance().getReference();


        // Spinner click listener
        spinner.setOnItemSelectedListener(this);

        // Spinner Drop down elements
        List<String> categories = new ArrayList<String>();

        categories.add("Technology");
        categories.add("Health");



        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, categories);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        spinner.setAdapter(dataAdapter);



        //Setting onClick Listener for the imageView To select image
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent galleryIntent = new Intent();
                //setting the intent action to get content
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                //setting the upload content type as image
                galleryIntent.setType("image/*");
                startActivityForResult(galleryIntent, 2);

            }
        });


        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //getting the data from the text view
               String bookName= bookNameEditTxt.getText().toString();
               String booksCount=booksCountEditTxt.getText().toString();
               String bookLocation=booksLocationEditText.getText().toString();
               String  category = spinner.getSelectedItem().toString();

                //checking all the fields are filled or not and performing the upload data action
                if (bookName.isEmpty() || booksCount.isEmpty() || bookLocation.isEmpty()) {
                    Toast.makeText(getContext(), "Please Enter Details", Toast.LENGTH_SHORT).show();
                } else if (imageUri == null) {
                    Toast.makeText(getContext(), "Please Upload Image", Toast.LENGTH_SHORT).show();
                } else {
                    //calling the method to add data to fireabase
                    uploadData(imageUri, bookName,booksCount,bookLocation,category);
                    Toast.makeText(getContext(),"Please,Wait uploading data...",Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private void uploadData(Uri imageUri, String bookName, String booksCount, String bookLocation,String  category) {

        //setting the file name as current time with milli Seconds to make the image name unique
        StorageReference fileRef = storageReference.child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
        //uploading the image to firebase
        fileRef.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<UploadTask.TaskSnapshot> task) {
                if (task.isSuccessful()) {
                    fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            //generating the unique key to add data under this node
                            String push = databaseReference.push().getKey().toString();

                            //Hash map to store values
                            HashMap bookDetails = new HashMap();

                            //adding the data to hashmap
                            bookDetails.put("bookName", bookName);
                            bookDetails.put("booksCount", booksCount);
                            bookDetails.put("bookLocation", bookLocation);
                            bookDetails.put("imageUrl", uri.toString());
                            bookDetails.put("category",category);
                            bookDetails.put("pushKey",push);
                            Log.e("bookdetailsssisssss", String.valueOf(bookDetails));
                            //uploading the data to the fireabase
                            databaseReference.child(category)
                                    .child(push).setValue(bookDetails)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {

                                    FirebaseDatabase.getInstance().getReference().child("TotalBooksCategories")
                                            .child(push).child("category").setValue(category)
                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                @Override
                                                public void onSuccess(Void unused) {

                                                    //Calling the same intent to reset all the current data
                                                    Intent intent = new Intent(getContext(), AdminActivity.class);
                                                    getActivity().startActivity(intent);
                                                    getActivity().finish();

                                                    //Showing the toast to user for confirmation
                                                    Toast.makeText(getContext(), "Data Uploaded Successfully", Toast.LENGTH_SHORT).show();


                                                }
                                            });
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull @NotNull Exception e) {

                            //Showing the toast message to the user to reUpload the data
                            Toast.makeText(getContext(), "Failed To Upload Please,Try Again", Toast.LENGTH_SHORT).show();
                        }
                    });

                }

            }
        });

    }

    private String getFileExtension(Uri imageUri) {

        //getting the image extension
        ContentResolver contentResolver = getActivity().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String extension = mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(imageUri));
        return extension;
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 2 && resultCode == RESULT_OK && data != null) {

            //Getting the image from the device and setting the image to imageView
            imageUri = data.getData();
            imageView.setImageURI(imageUri);
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

        // On selecting a spinner item
        String item = parent.getItemAtPosition(position).toString();

        // Showing selected spinner item
        Toast.makeText(parent.getContext(), "Selected: " + item, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        //Do nothing
    }

}