package com.example.b9000application.Fragments;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;

import com.example.b9000application.Activities.MainActivity;
import com.example.b9000application.Adapters.MyApplication;
import com.example.b9000application.Adapters.UserAdapter;
import com.example.b9000application.Helper.SpacesItemDecoration;
import com.example.b9000application.Models.Entities.User;
import com.example.b9000application.Models.ViewModel.UserListViewModel;
import com.example.b9000application.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserListFragment extends Fragment {

    private RecyclerView userRecyclerView;
    private UserListViewModel userListViewModel;
    final UserAdapter adapter = new UserAdapter();
    public UserListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.search_menu, menu);
        final MenuItem search = menu.findItem(R.id.app_bar_search);
        final SearchView searchView = (SearchView) search.getActionView();
        searchView.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                adapter.getFilter().filter("");
                ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {

                    adapter.getFilter().filter(newText);

                return false;
            }
        });


    }
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        switch (id){
            case (R.id.app_bar_search):
                ((MainActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);



        }
        return true;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View v= inflater.inflate(R.layout.fragment_user_list, container, false);
        userListViewModel = ViewModelProviders.of(this).get(UserListViewModel.class);
        userRecyclerView = v.findViewById(R.id.user_list_rv);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MyApplication.getContext(),3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.DefaultSpanSizeLookup());
        gridLayoutManager.setMeasurementCacheEnabled(true);
        userRecyclerView.setLayoutManager(gridLayoutManager);
        int spacingInPixels = getResources().getDimensionPixelSize(R.dimen.spacing);
        userRecyclerView.addItemDecoration(new SpacesItemDecoration(spacingInPixels));
        userRecyclerView.setClipToPadding(true);
        userRecyclerView.setItemAnimator(new DefaultItemAnimator());
        userRecyclerView.setAdapter(adapter);
        userListViewModel.getAllUsers().observe(this, new Observer<List<User>>() {
            @Override
            public void onChanged(List<User> users) {
                adapter.setUsers(users);
            }
        });



       
       
       
       
       
       return v;
    }

}
