package com.example.contacts;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.example.contacts.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

//    Data Source
    private ContactDataBase contactDataBase;
    private ArrayList<Contacts> contactsArrayList = new ArrayList<>();
//    Adapter
    private MyAdapter myAdapter;

//    Binding
    private ActivityMainBinding mainBinding;
    private MainActivityClickHandlers handlers;

//    View model
    MyViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Data Binding
        mainBinding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        handlers = new MainActivityClickHandlers(this);
        mainBinding.setClickHandler(handlers);

//        RecyclerView
        RecyclerView recyclerView = mainBinding.recyclerView;
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);


//        Database
        contactDataBase = ContactDataBase.getDbInstance(this);

//       View Model
        viewModel = new ViewModelProvider(this).get(MyViewModel.class);

//        Inserting a new Contact (Just For Testing)
//        Contacts c1 = new Contacts("Jack","jack@gmail.com");
//        viewModel.addNewContacts(c1);

//        Loading the Data from ROOM DB
        viewModel.getAllContacts().observe(this,
                new Observer<List<Contacts>>() {
                    @Override
                    public void onChanged(List<Contacts> contacts) {
                        contactsArrayList.clear();
                        for(Contacts c: contacts){
//                            Log.v("TAGY",c.getName());
                            contactsArrayList.add(c);
                        }

                        myAdapter.notifyDataSetChanged();

                    }
                });

//        Adapter
        myAdapter = new MyAdapter(contactsArrayList);

//        Linking the RecyclerView with the Adapter
        recyclerView.setAdapter(myAdapter);

        // swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                // If you swipe the item to the left
                Contacts c = contactsArrayList.get(viewHolder.getAdapterPosition());

                viewModel.deleteContacts(c);
            }
        }).attachToRecyclerView(recyclerView);

    }
}