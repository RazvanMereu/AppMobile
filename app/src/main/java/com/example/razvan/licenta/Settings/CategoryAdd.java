package com.example.razvan.licenta.Settings;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.razvan.licenta.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class CategoryAdd extends Activity {
    ListView listView;
    Button btnAdd;
    Button cancel;
    EditText inputLabel;
    ArrayList<Category> listViews = new ArrayList<>();
    Category_Adapter tab1_adapter;
    FirebaseDatabase mFirebaseDatabase;
    FirebaseAuth mAuth;
    FirebaseUser mUser;
    DatabaseReference myRef;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.tab1);

        cancel = findViewById(R.id.btnCancelAdd);
        listView = findViewById(R.id.listAddCategory);
        btnAdd = findViewById(R.id.btnAddCategory);
        inputLabel = findViewById(R.id.week_view_category);
        mAuth = FirebaseAuth.getInstance();
        mFirebaseDatabase = FirebaseDatabase.getInstance();
        mUser = mAuth.getCurrentUser();

        myRef = FirebaseDatabase.getInstance().getReference("Users").child(mUser.getUid()).child("Categories");


        Log.e("aaa", myRef.toString() + " " + myRef.getKey());

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listViews.clear();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                    Category category = new Category(ds.getKey(), ds.getValue().toString());
                    listViews.add(category);
                }
                loadListView();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.e("CategoryAdd", databaseError.getMessage());
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String category_name = inputLabel.getText().toString();

                DatabaseReference pushedPostRef = myRef.push();
                final String postId = pushedPostRef.getKey();

                if (category_name.trim().length() > 0) {
                    DatabaseReference current_user_db = mFirebaseDatabase.getReference().child("Users").child(mUser.getUid());
                    if (postId != null) {
                        myRef.child(postId).setValue(category_name);
                    }
                    inputLabel.setText("");
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    if (imm != null) {
                        imm.hideSoftInputFromWindow(inputLabel.getWindowToken(), 0);
                    }

                    Toast.makeText(getApplication(), "Successfully Added!", Toast.LENGTH_SHORT).show();
                    //  loadListView();
                } else {
                    Toast.makeText(getApplication(), "Please enter category name",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void loadListView() {
        tab1_adapter = new Category_Adapter(getApplicationContext(), listViews);

        listView.setAdapter(tab1_adapter);
        tab1_adapter.notifyDataSetChanged();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                showEditBox(listViews.get(position).getName(), position);
            }
        });
    }

    public void showEditBox(final String oldItem, final Integer id) {
        final Dialog dialog = new Dialog(CategoryAdd.this);
        dialog.setCancelable(true);
        dialog.setTitle("Update / Delete Category");

        dialog.setContentView(R.layout.edit_category);
        final EditText editText = dialog.findViewById(R.id.editCategory);
        editText.setText(oldItem);
        Button btn = dialog.findViewById(R.id.btnDone);
        Button delete = dialog.findViewById(R.id.btnCancelEdit);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //Update Category
                String ids = listViews.get(id).getId();
                tab1_adapter.notifyDataSetChanged();

                String Category_name = editText.getText().toString();
                editText.setText("");
                myRef.child(ids).setValue(Category_name);

                Toast.makeText(CategoryAdd.this, "Category name edited to " + Category_name, Toast.LENGTH_SHORT).show();
                loadListView();
                dialog.dismiss();

            }
        });
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                dialog.dismiss();
                final AlertDialog.Builder dialog1 = new AlertDialog.Builder(CategoryAdd.this)
                        .setTitle("Delete " + listViews.get(id).getName().toString() + "?")
                        .setMessage("Make sure this item is empty or transferred to other item. Otherwise all data related to this item will be deleted.")
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog1, int which) {
                                dialog1.dismiss();
                                final AlertDialog.Builder dialogDelete = new AlertDialog.Builder(CategoryAdd.this)
                                        .setMessage("Are you sure you want to delete this item?")
                                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogDel, int which) {
                                                String value = listViews.get(id).getName();
                                                String deleted = " is Deleted!";
                                                Log.e("de sters", myRef.child(value).toString());
                                                String ids = listViews.get(id).getId();
                                                myRef.child(ids).removeValue();
                                                tab1_adapter.notifyDataSetChanged();
                                                loadListView();

                                                Toast.makeText(CategoryAdd.this, "Category  " + value + deleted, Toast.LENGTH_SHORT).show();

                                                dialogDel.dismiss();

                                            }

                                        })

                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogdel1, int which) {
                                                dialogdel1.dismiss();
                                            }
                                        });
                                dialogDelete.show();
                            }
                        })

                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog2, int which) {
                                dialog2.dismiss();
                                dialog.dismiss();

                            }
                        });
                dialog1.show();

            }

        });
        dialog.show();

    }

}