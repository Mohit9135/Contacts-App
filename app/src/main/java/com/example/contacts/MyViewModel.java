package com.example.contacts;
import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.List;

public class MyViewModel extends AndroidViewModel{
    private LiveData<List<Contacts>> allContacts;
    private Repository myRepository;


    public MyViewModel(@NonNull Application application) {
        super(application);
        this.myRepository = new Repository(application);
    }

    public LiveData<List<Contacts>> getAllContacts(){
        allContacts = myRepository.getAllContacts();
        return allContacts;
    }

    public void addNewContacts(Contacts contact){
        myRepository.addContact(contact);
    }

    public void deleteContacts(Contacts contact){
        myRepository.deleteContact(contact);
    }

}
