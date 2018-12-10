package com.example.aloranking.journalapp;

import android.content.ClipData;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.SparseBooleanArray;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.aloranking.journalapp.adapter.AdapterClicklistener;
import com.example.aloranking.journalapp.adapter.DiaryAdapter;
import com.example.aloranking.journalapp.adapter.SwipeToDeleteCallback;
import com.example.aloranking.journalapp.model.Diaries;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DairyHome extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private List<Diaries> diariesList = new ArrayList<>();
    private  ArrayList<String> selection_list = new ArrayList<>();
    int counter= 0;

    private RecyclerView recyclerView;
    private DiaryAdapter diaryAdapter;
    private EditText feelingText, whatsOnUrMindText;
    private Button saveButton;
    DatabaseReference databaseDiary;
    CoordinatorLayout coordinatorLayout;
    FloatingActionButton fab;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private TextView mUserName;
    private TextView mUserEmail;
    private ImageView mUserImage;
    private  TextView counter_textview;
    Toolbar toolbar;
    private SparseBooleanArray selectedItems;


    private static final int EditJournal_REQUEST_CODE = 10;
    public boolean in_action_mode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dairy_home);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        selectedItems = new SparseBooleanArray();

        counter_textview = findViewById(R.id.counter_text);
        counter_textview.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();


        coordinatorLayout = findViewById(R.id.coordinatorLayout);



        diaryAdapter = new DiaryAdapter(diariesList);

        fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                Intent intent = new Intent(DairyHome.this, InputJournal.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer =  findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView =  findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View headerView = navigationView.getHeaderView(0);

        mUserImage = headerView.findViewById(R.id.userImage);
        mUserName = headerView.findViewById(R.id.userName);
        mUserEmail = headerView.findViewById(R.id.userEmail);


        databaseDiary = FirebaseDatabase.getInstance().getReference("dairies");
        databaseDiary.keepSynced(true);




        recyclerView =  findViewById(R.id.journal_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setItemAnimator(new DefaultItemAnimator());

          mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                if (firebaseAuth.getCurrentUser() == null){
                    startActivity(new Intent(DairyHome.this, GoogleSignin.class));
                    finish();
                }
            }
        };


          getUserSignInData();

        enableSwipeToDeleteAndUndo();


    }

    private void getUserSignInData(){
        Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }

        String username = extras.getString("name");
        String email = extras.getString("email");
        Uri userPhoto = Uri.parse(extras.getString("photo"));


        mUserImage.setImageURI(userPhoto);
        mUserEmail.setText(email);
        mUserName.setText(username);
    }

    private void enableSwipeToDeleteAndUndo() {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(this) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {


                final int position = viewHolder.getAdapterPosition();
                final  Diaries deletedItem = diariesList.get(viewHolder.getAdapterPosition());
                String name = diariesList.get(viewHolder.getAdapterPosition()).getDiaryFeeling();

               /* Diaries diaries = new Diaries();

                databaseDiary = FirebaseDatabase.getInstance().getReference("diaries")
                        .child(diaries.getDiaryId());
*/
               diaryAdapter.removeIteml(viewHolder.getAdapterPosition());

               fab.setVisibility(View.GONE);

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, name +" was removed from the list.", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        diaryAdapter.restoreItem(deletedItem, position);
                        recyclerView.scrollToPosition(position);
                    }
                });

                snackbar.setActionTextColor(Color.YELLOW);
                snackbar.show();

            }
        };



        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }


    @Override
    protected void onStart() {
        super.onStart();


        mAuth.addAuthStateListener(mAuthStateListener);


        databaseDiary.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                diariesList.clear();

                for (DataSnapshot diarySnapshot : dataSnapshot.getChildren()){
                    final Diaries diaries = diarySnapshot.getValue(Diaries.class);

                    diariesList.add(diaries);

                    final DiaryAdapter adapter = new DiaryAdapter(diariesList);
                    recyclerView.setAdapter(adapter);
                    adapter.setOnItemClickListener(new AdapterClicklistener() {
                        @Override
                        public void OnItemClick(View v, int position) {
                            Diaries diaries = diariesList.get(position);
                            if (!in_action_mode) {



                                Intent intent = new Intent(DairyHome.this, EditJournal.class);
                                intent.putExtra("feeling", diaries.getDiaryFeeling());
                                intent.putExtra("thoughts", diaries.getDairyEntries());
                                intent.putExtra("id", diaries.getDiaryId());
                                startActivityForResult(intent, EditJournal_REQUEST_CODE);
                            }else{
                                String Sid = diaries.getDiaryId();
                                toggleSelected(Sid);
                            }
                            /*else if(selectedItems.get(position, true)){
                                //Diaries diaries = diariesList.get(position);
                               // selectedItems.put(position, true);
                                selection_list.add(position);
                                counter=counter+1;
                                updateCounter(counter);
                            }else {
                                //selectedItems.delete(position);
                                selection_list.remove(diaries);
                                counter = counter-1;
                                updateCounter(counter);
                            }*/

                        }


                        @Override
                        public void OnItemLongClick(View v, int pos) {
                            toolbar.getMenu().clear();
                            toolbar.inflateMenu(R.menu.menu_action_mode);
                            counter_textview.setVisibility(View.VISIBLE);
                            in_action_mode = true;
                            adapter.notifyDataSetChanged();
                            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

                        }
                    });


                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    public void updateCounter(int counter){
        if (counter==0){
            counter_textview.setText("0 items selected");
        }else {
            counter_textview.setText(counter+ "items selected");
        }
    }



    public void toggleSelected(String e){
        final boolean newState = !selection_list.contains(e);
        if (newState){
           //String id= e.getDiaryId();
            selection_list.add(e);
            counter=counter+1;
            updateCounter(counter);
        }else {
            selection_list.remove(e);
            counter=counter-1;
            updateCounter(counter);
        }

    }

    public void clearActionMode(){
        in_action_mode = false;
        toolbar.getMenu().clear();
        toolbar.inflateMenu(R.menu.navigation_menu);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        counter_textview.setVisibility(View.GONE);
        counter_textview.setText("0 item selected");
        counter = 0;
        selection_list.clear();

    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        }
        if (in_action_mode){
            clearActionMode();
            diaryAdapter.notifyDataSetChanged();
        }else {
            super.onBackPressed();
        }
    }

    public void updateJournal(){

       /* Bundle extras = getIntent().getExtras();
        if (extras == null) {
            return;
        }
        // get data via the key
        String feeling = extras.getString("feeling");
        String thoughts = extras.getString("thoughts");
        String id = extras.getString("id");
        String time = extras.getString("time");
*/


        /*databaseDiary = FirebaseDatabase.getInstance().getReference("dairies").child("id");
        Diaries mDiaries = new Diaries(id,thoughts, feeling, time);
        databaseDiary.setValue(mDiaries);*/

        DiaryAdapter adapter = new DiaryAdapter(diariesList);
        adapter.notifyDataSetChanged();



        Toast.makeText(this, "Diary updated", Toast.LENGTH_SHORT).show();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == EditJournal_REQUEST_CODE && resultCode == RESULT_OK) {
            updateJournal();
        } else {


        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.logOut) {
            mAuth.signOut();
        }

        if (id == R.id.action_delete){
            in_action_mode = false;
            //DatabaseReference databaseReference =FirebaseDatabase.getInstance().getReference();
           // databaseDiary.
            DiaryAdapter diaryAdapter = new DiaryAdapter(diariesList);
            diaryAdapter.removeItem(selection_list);
            clearActionMode();
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {
            mAuth.signOut();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }



}
