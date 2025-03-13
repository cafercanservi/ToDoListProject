package com.example.yaplacaklarlistesiprojesi;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    EditText editText;
    ImageButton imageButton;
    ListView listView;
    ArrayAdapter adapter;
    ArrayList<String> Liste = new ArrayList<>();// Listeye arrayadapter yardımıyla veri ekleyecez sonra listviewe yansıtacaz

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        editText = findViewById(R.id.editTextText4);
        imageButton= findViewById(R.id.imageButton2);
        listView = findViewById(R.id.listview);

        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1,Liste);

        //adapterü listviewe ekleyecez
        listView.setAdapter(adapter);


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
               //uzun tıklama işlemi
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Bu ögeyi silmek isteğinizden emin misiniz?")
                        .setTitle("Yapılacaklar Listesi")
                        .setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                  Liste.remove(position);
                                  adapter.notifyDataSetChanged();
                                  //String listeverisi = Liste.get(position);   + listeverisi
                                Toast.makeText(getApplicationContext(),"Veri Silindi" ,Toast.LENGTH_LONG).show();
                            }
                        })
                        .setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                AlertDialog dialog =builder.create();
                dialog.show();


                return true;
            }
        });


        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String veri =editText.getText().toString();// veri buttona bastığında alınır
                if(!Liste.contains(veri.trim())){
                    Liste.add(veri.trim());
                    adapter.notifyDataSetChanged();;
                    Toast.makeText(getApplicationContext(),"Veri Eklendi",Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(getApplicationContext(),veri + " zaten eklenmiş",Toast.LENGTH_LONG).show();
                }
                editText.setText("");

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        verilerigerigetir();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        kayitet();
    }

    public void kayitet(){
        SharedPreferences sharedPreferences= getSharedPreferences("data",MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Set<String> veriSeti= new HashSet<>(Liste);
        editor.putStringSet("VeriListesi",veriSeti);
        //değişiklikleri kaydet
        editor.commit();
        Toast.makeText(getApplicationContext(),"Veriler Kaydedildi",Toast.LENGTH_LONG).show();

    }
    public void verilerigerigetir(){
        SharedPreferences sharedPreferences= getSharedPreferences("data",MODE_PRIVATE);
        Set<String> veriSeti= sharedPreferences.getStringSet("VeriListesi",null);
        Liste.clear();
        if(veriSeti!=null){
            Liste.addAll(veriSeti);
        }
        adapter.notifyDataSetChanged();//adaptoru güncellendi
        Toast.makeText(getApplicationContext(),"Yükleme İşlemi tamamlandı",Toast.LENGTH_LONG).show();




    }

}