package com.aylingunes.androidmaps;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener {

    private GoogleMap mMap;
    LocationManager locationManager;
    LocationListener locationListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {  // harita hazır olduktan sonra yapılması gerek harita işlemlerinin
        mMap = googleMap;
        mMap.setOnMapLongClickListener(this);



        //LogMan bir konum yöntecisidir. sistemin konumuna erişmeyi sağlar güncel konuma değişse de ulaşacak ve bazı izinleri almamız gerek
        //Manifeste COARSE LOC ve FINE LOC izinleri eklemelisin
         locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
       //LocMAn oluşturduktan sınra listener oluştur ki konum değişirse haberdar olabil
         locationListener = new LocationListener() { //konumdeğişme methodunu verir
            @Override
            public void onLocationChanged(@NonNull Location location) { // konum değişince ne yapacağını verir

                //System.out.println("location: " + location.toString()); // locationu logcat içinde görebilecek miyiz --> izinler alındıktan sonra bakılacak
                //kullanıcının konmunu marker olarak verelim

               /* mMap.clear(); // markerlar temizlenir konum güncellemesinde arkadaki konumun izi kalmaz
                LatLng userLocation = new LatLng(location.getLatitude(),location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(userLocation).title("user location"));
               mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation,15)); */


             /*   try {
                    List<Address> addressList = geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                if ( addressList != null && addressList.size()>0){
                    System.out.println("adress: " +addressList.get(0).toString());
                }

                } catch (IOException e) {
                    e.printStackTrace();
                }

*/


            }


        };
        //kullanıcının konumu için izin var mı yok mu şekkilnde kontrol et
        // izin verilmediyse --->
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED){
            //permission  işlemleri
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},1);
        }else {
            //location işlemleri--> hazırda izin verildiyse --> req update konumgüncellemesini ister
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener) ; // parametreleri zaman ve metreyi pil yanlısı planla
            Location lastLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER); //sonbilinen  konum yoksa null dönebilir bunu kontrol et

            if( lastLocation != null) {
                LatLng userLastLocation = new LatLng(lastLocation.getLatitude(),lastLocation.getLongitude());
mMap.addMarker(new MarkerOptions().position(userLastLocation).title("ure Location"));
mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLastLocation,15));
            }




        }



        // Add a marker in Sydney and move the camera
    //    LatLng eiffel = new LatLng(48.8573937, 2.2940337); // enlem ve boylam değişkeni oluşturuluyor. bu şekilde koordinat veriliyor


 //       mMap.addMarker(new MarkerOptions().position(eiffel).title("Eiffel Tower")); // işaretçi oluşturulmuş
        // mMap.moveCamera(CameraUpdateFactory.newLatLng(eiffel)); //kamerayı oynat anlamında nereye oynatacağını yine LATLANG ile veriyoruz
        // eiffele gidiyor ama oldukça uzak, bu nedenle newLatLngı zoom seçeceğiz
  //      mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(eiffel,15)); // LatLang,zoomlama miktarı olmalı
        // zoom 2ile 21 arasında bir değer alır. deneyerek beğeniye uygun bir oran belirlenir



    }
//onrequest yazınca hazır bir blok verir. burada izinleri aldığında yapacağın işlemleri bulundurmalısın
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
       //kontrol etmelisin
       if(grantResults.length>0 ){
           if (requestCode ==1){ // req koda kendimiz 1 atamaıştık. bunu izin alındığında döndürüyordu yani eğer 1se bu değer izinde sorun yoktur
               if(ContextCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                   //izin verildiyse yukarıdaki else altındaki durum geçerli olur
                   locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locationListener) ; // parametreleri zaman ve metreyi pil yanlısı planla

               }
           }
       }


        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onMapLongClick(LatLng latLng) { // en üstte MapsActivity yanında OnMapReadyCallBack vardı biz ayrıca OnMapLongClick ekledik ki kullanıcı haritaya uzun basında bir işaretleme yapabilsin istiyorduk. alt+enter ile importlayınca bu sınıfı verdi. içinde haritaya uzun basılınca ne olacağını yazacağız
        // fonksiyon bize default konum enlem boylamı verdi

        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.getDefault()); //kullanıcının cihaz diline göre adres gösteerimini düzenle
        mMap.clear(); // işlemi sürekli yapacağı için markerda karışıklık olabilir
        String address ="";

        try {
            List<Address> addressList = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);

        if ( addressList != null && addressList.size()>0){

            if (addressList.get(0).getThoroughfare() != null ) { //her konumda cadde bilgisi bulunamayabilir kontrolde tutmakta fayda olacaktır
                     address += addressList.get(0).getThoroughfare();
                     if (addressList.get(0).getSubThoroughfare() != null){
                         address += addressList.get(0).getSubThoroughfare();
                     }
            }
        }


        } catch (IOException e) {
            e.printStackTrace();
        }

        mMap.addMarker(new MarkerOptions().position(latLng).title(address));

    }
}