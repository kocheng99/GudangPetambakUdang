package com.example.shrimpfeedmanagement.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shrimpfeedmanagement.Adapter.MainAdapter;
import com.example.shrimpfeedmanagement.Model.ModelBarang;
import com.example.shrimpfeedmanagement.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements MainAdapter.FirebaseDataListener{

    private ExtendedFloatingActionButton mFloatingActionButton;
    private EditText mEditNama;
    private EditText mEditMerk;
    private EditText mEditHarga;
    private EditText mEditJumlah;
    private EditText mEditDeskripsi;
    private RecyclerView mRecyclerView;
    private MainAdapter mAdapter;
    private ArrayList<ModelBarang> daftarBarang;
    private DatabaseReference mDatabaseReference;
    private FirebaseDatabase mFirebaseInstance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // inisialisasi bottom navigation
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

//        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
//        }
//        if (Build.VERSION.SDK_INT >= 19) {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                getWindow().getDecorView()
//                        .setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE
//                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                                | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
//            }
//        }

//        if (Build.VERSION.SDK_INT >= 21) {
//            setWindowFlag(this, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }

        // set home selected
        bottomNavigationView.setSelectedItemId(R.id.items);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.items:
                        return true;
                    case R.id.almanac:
                        startActivity(new Intent(getApplicationContext()
                                , AlmanacActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.report:
                        startActivity(new Intent(getApplicationContext()
                                , ReportActivity.class));
                        overridePendingTransition(0,0);
                        return true;
//                    case R.id.user:
//                        startActivity(new Intent(getApplicationContext()
//                                , UserActivity.class));
//                        overridePendingTransition(0,0);
//                        return true;
                }
                return false;
            }
        });


        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        FirebaseApp.initializeApp(this);
        mFirebaseInstance = FirebaseDatabase.getInstance();
        mDatabaseReference = mFirebaseInstance.getReference("barang");
        mDatabaseReference.child("data_barang").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                daftarBarang = new ArrayList<>();
                for (DataSnapshot mDataSnapshot : dataSnapshot.getChildren()) {
                    ModelBarang barang = mDataSnapshot.getValue(ModelBarang.class);
                    barang.setKey(mDataSnapshot.getKey());
                    daftarBarang.add(barang);
                }

                mAdapter = new MainAdapter(HomeActivity.this, daftarBarang);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(HomeActivity.this,
                        databaseError.getDetails() + " " + databaseError.getMessage(), Toast.LENGTH_LONG).show();
            }

        });

        mFloatingActionButton = findViewById(R.id.tambah_barang);
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialogTambahBarang();
            }
        });

    }

    // method untuk menampilkan option menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opsi, menu);
        return true;
    }
    // method untuk handle option menu
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profile:
                Intent toUserActivity = new Intent(HomeActivity.this, UserActivity.class);
                startActivity(toUserActivity);
                break;
            case R.id.about:
                dialogAbout();
                break;
            case R.id.sign_out:
                dialogSignOut();
        }
        return true;
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {

        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }

    @Override
    public void onDataClick(final ModelBarang barang, int position) {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Pilih Aksi");

        builder.setPositiveButton("UPDATE", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialogUpdateBarang(barang);
            }
        });

        builder.setNegativeButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                hapusDataBarang(barang);
            }
        });

        builder.setNeutralButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogSignOut() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("anda yakin ingin logout ?");

        builder.setPositiveButton("YAKIN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FirebaseAuth.getInstance().signOut();
                Intent LoginActivity = new Intent(HomeActivity.this, LoginActivity.class);
                startActivity(LoginActivity);
                finish();
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.show();

    }

    private void dialogAbout(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("about app");
        View view = getLayoutInflater().inflate(R.layout.dialog_about, null);
        builder.setView(view);

        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogTambahBarang() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tambah Item");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);

        mEditNama = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        mEditJumlah = view.findViewById(R.id.jumlah_barang);
        mEditDeskripsi = view.findViewById(R.id.deskripsi_barang);
        builder.setView(view);

        builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {

                String namaBarang = mEditNama.getText().toString();
                String merkBarang = mEditMerk.getText().toString();
                String hargaBarang = mEditHarga.getText().toString();
                String jumlahBarang = mEditJumlah.getText().toString();
                String deskripsiBarang = mEditDeskripsi.getText().toString();

                if (!namaBarang.isEmpty() && !merkBarang.isEmpty() && !hargaBarang.isEmpty()) {
                    submitDataBarang(new ModelBarang(namaBarang, merkBarang, hargaBarang, jumlahBarang, deskripsiBarang));
                } else {
                    Toast.makeText(HomeActivity.this, "Data harus di isi!", Toast.LENGTH_LONG).show();
                }
            }
        });

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();
    }

    private void dialogUpdateBarang(final ModelBarang barang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Item");
        View view = getLayoutInflater().inflate(R.layout.layout_edit_barang, null);

        mEditNama = view.findViewById(R.id.nama_barang);
        mEditMerk = view.findViewById(R.id.merk_barang);
        mEditHarga = view.findViewById(R.id.harga_barang);
        mEditJumlah = view.findViewById(R.id.jumlah_barang);
        mEditDeskripsi = view.findViewById(R.id.deskripsi_barang);

        mEditNama.setText(barang.getNama());
        mEditMerk.setText(barang.getMerk());
        mEditHarga.setText(barang.getHarga());
        mEditJumlah.setText(barang.getJumlah());
        mEditDeskripsi.setText(barang.getDeskripsi());

        builder.setView(view);

        if (barang != null) {
            builder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int id) {
                    barang.setNama(mEditNama.getText().toString());
                    barang.setMerk(mEditMerk.getText().toString());
                    barang.setHarga(mEditHarga.getText().toString());
                    barang.setJumlah(mEditJumlah.getText().toString());
                    barang.setDeskripsi(mEditDeskripsi.getText().toString());
                    updateDataBarang(barang);
                }
            });
        }

        builder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        Dialog dialog = builder.create();
        dialog.show();

    }

    private void submitDataBarang(ModelBarang barang) {
        mDatabaseReference.child("data_barang").push()
                .setValue(barang).addOnSuccessListener(this, new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(HomeActivity.this, "Data barang berhasil di simpan !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void updateDataBarang(ModelBarang barang) {
        mDatabaseReference.child("data_barang").child(barang.getKey())
                .setValue(barang).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void mVoid) {
                Toast.makeText(HomeActivity.this, "Data berhasil di update !", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void hapusDataBarang(ModelBarang barang) {
        if (mDatabaseReference != null) {
            mDatabaseReference.child("data_barang").child(barang.getKey())
                    .removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void mVoid) {
                    Toast.makeText(HomeActivity.this, "Data berhasil di hapus !", Toast.LENGTH_LONG).show();
                }
            });
        }
    }

}
