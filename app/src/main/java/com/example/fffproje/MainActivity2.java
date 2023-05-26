    package com.example.fffproje;


    import android.annotation.SuppressLint;
    import android.os.Bundle;
    import android.view.View;
    import android.widget.AdapterView;
    import android.widget.ArrayAdapter;
    import android.widget.Button;
    import android.widget.Spinner;
    import android.widget.TextView;

    import androidx.appcompat.app.AppCompatActivity;

    import com.google.firebase.database.DataSnapshot;
    import com.google.firebase.database.DatabaseError;
    import com.google.firebase.database.FirebaseDatabase;
    import com.google.firebase.database.ValueEventListener;

    import java.util.ArrayList;
    import java.util.List;

    public class MainActivity2 extends AppCompatActivity {

        private Spinner spinnerOgunSecim;
        private Spinner spinnerYemekSecim;
        private Button btnHesapla;
        private TextView txtGecici;

        private List<String> ogunListesi;
        private List<String> yemekListesi;
        private List<Integer> yemekKaloriListesi;

        private String secilenOgun;
        private List<String> secilenYemekler;
        private int toplamKalori;

        @SuppressLint("MissingInflatedId")
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main2);

            spinnerOgunSecim = findViewById(R.id.spinnerOgunSecim);
            spinnerYemekSecim = findViewById(R.id.spinnerYemekSecim);
            btnHesapla = findViewById(R.id.btnHesapla);
            txtGecici = findViewById(R.id.txtGecici);

            ogunListesi = new ArrayList<>();
            ogunListesi.add("Sabah");
            ogunListesi.add("Öğlen");
            ogunListesi.add("Akşam");

            ArrayAdapter<String> ogunAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, ogunListesi);
            ogunAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerOgunSecim.setAdapter(ogunAdapter);

            yemekListesi = new ArrayList<>();
            yemekKaloriListesi = new ArrayList<>();

            // Firebase'den yemek listesi ve kalorilerini al
            FirebaseDatabase.getInstance().getReference().child("kaloriler")
                    .addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                String yemekAdi = snapshot.getKey();
                                int kalori = snapshot.getValue(Integer.class);

                                yemekListesi.add(yemekAdi);
                                yemekKaloriListesi.add(kalori);
                            }

                            ArrayAdapter<String> yemekAdapter = new ArrayAdapter<>(MainActivity2.this, android.R.layout.simple_spinner_item, yemekListesi);
                            yemekAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spinnerYemekSecim.setAdapter(yemekAdapter);
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            // Veritabanı hatası durumunda yapılacak işlemler
                        }
                    });

            secilenYemekler = new ArrayList<>();

            spinnerOgunSecim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    secilenOgun = ogunListesi.get(position);

                    // Öğün değiştiğinde seçilen yiyeceklerin ve toplam kalorinin sıfırlanması
                    secilenYemekler.clear();
                    toplamKalori = 0;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            spinnerYemekSecim.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                    String secilenYemek = yemekListesi.get(position);
                    int kalori = yemekKaloriListesi.get(position);

                    // Seçilen yemeği ve kalorisini ilgili öğünün listesine ekle
                    if (!secilenYemekler.contains(secilenYemek)) {
                        secilenYemekler.add(secilenYemek);
                        toplamKalori += kalori;
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {
                }
            });

            btnHesapla.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // Seçilen yemekleri ve toplam kaloriyi göster
                    StringBuilder sb = new StringBuilder();
                    for (String yemek : secilenYemekler) {
                        sb.append(yemek).append("\n");
                    }
                    sb.append("\nToplam Kalori: ").append(toplamKalori);

                    txtGecici.setText(sb.toString());
                }
            });
        }
    }

