package com.example.ssanusi.advert;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.internal.BottomNavigationItemView;
import android.support.design.internal.BottomNavigationMenuView;
import android.support.design.widget.BottomNavigationView;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import com.example.ssanusi.advert.fragment.HomeFragment;
import com.example.ssanusi.advert.fragment.Toview;
import com.example.ssanusi.advert.fragment.ProfileFragment;
import com.example.ssanusi.advert.fragment.publishFragment;
import com.example.ssanusi.advert.interfaces.Listener;

import java.lang.reflect.Field;

public class MainActivity extends AppCompatActivity implements Listener {
    BottomNavigationView bottomNavigationView;
    private Fragment selected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        removeShiftMode(bottomNavigationView);

        selected = new HomeFragment();
       // selected.getSupportFragmentManager().beginTransaction().add(R.id.frameLayoutForBottomNav,selected).commit();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frameLayoutForBottomNav, selected)
                .commit();

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        selected = new HomeFragment();
                        break;

                    case R.id.publish:
                        selected = new publishFragment();
                        break;


                    case R.id.toView:
                        selected = new Toview();
                        break;

                    case R.id.profile:
                        selected = new ProfileFragment();
                        break;
                }
                onClicked(selected);
                return true;
            }
        });
    }

    public void onClicked (@NonNull final Fragment fragment){

        FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction()
                .addToBackStack(null)
                .replace(R.id.frameLayoutForBottomNav,fragment)
                .commit();
    }

    @SuppressLint("RestrictedApi")
    public static void removeShiftMode(BottomNavigationView view) {
        BottomNavigationMenuView menuView = (BottomNavigationMenuView) view.getChildAt(0);
        try {
            Field shiftingMode = menuView.getClass().getDeclaredField("mShiftingMode");
            shiftingMode.setAccessible(true);
            shiftingMode.setBoolean(menuView, false);
            shiftingMode.setAccessible(false);
            for (int i = 0; i < menuView.getChildCount(); i++) {
                BottomNavigationItemView item = (BottomNavigationItemView) menuView.getChildAt(i);
                item.setShiftingMode(false);
                // set once again checked value, so view will be updated
                item.setChecked(item.getItemData().isChecked());
            }
        } catch (NoSuchFieldException e) {
            Log.e("ERROR NO SUCH FIELD", "Unable to get shift mode field");
        } catch (IllegalAccessException e) {
            Log.e("ERROR ILLEGAL ALG", "Unable to change value of shift mode");
        }
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
}
