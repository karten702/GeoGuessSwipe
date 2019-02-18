package com.example.geoguessswipe;

import android.content.ClipData;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerView.OnItemTouchListener {

    private List<GeoObject> geoObjects = new ArrayList<>();
    private GestureDetector gestureDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (int i = 0; i < GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES.length; i++){
            geoObjects.add(new GeoObject(GeoObject.PRE_DEFINED_GEO_OBJECT_NAMES[i],
                                            GeoObject.PRE_DEFINED_GEO_OBJECT_IMAGE_IDS[i],
                                            GeoObject.PRE_DEFINED_GEO_OBJECT_ANSWERS[i]));
        }

        final RecyclerView recyclerView = findViewById(R.id.recyclerView);
        RecyclerView.LayoutManager layoutManager = new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(false);
        final GeoObjectAdapter adapter = new GeoObjectAdapter(this, geoObjects);
        recyclerView.setAdapter(adapter);
        recyclerView.addOnItemTouchListener(this);

        gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener(){
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }
        });

        ItemTouchHelper.SimpleCallback callbackSwipeLeft = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = (viewHolder.getAdapterPosition());
                GeoObject thisObject = geoObjects.get(position);
                if(thisObject.getInEurope()){
                    Toast.makeText(getBaseContext(), getString(R.string.correctAnswer) + " " + thisObject.getGeoName() + " " + getString(R.string.isInEurope), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getBaseContext(), getString(R.string.wrongAnswer) + " " + thisObject.getGeoName() + " " + getString(R.string.notInEurope), Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper.SimpleCallback callbackSwipeRight = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                int position = (viewHolder.getAdapterPosition());
                GeoObject thisObject = geoObjects.get(position);
                if(!thisObject.getInEurope()){
                    Toast.makeText(getBaseContext(), getString(R.string.correctAnswer) + thisObject.getGeoName() + getString(R.string.notInEurope), Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(getBaseContext(), getString(R.string.wrongAnswer) + thisObject.getGeoName() + getString(R.string.isInEurope), Toast.LENGTH_SHORT).show();
            }
        };

        ItemTouchHelper touchHelper = new ItemTouchHelper(callbackSwipeLeft);
        ItemTouchHelper touchRight = new ItemTouchHelper(callbackSwipeRight);
        touchHelper.attachToRecyclerView(recyclerView);
        touchRight.attachToRecyclerView(recyclerView);
    }

    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
        View child = rv.findChildViewUnder(e.getX(), e.getY());
        int adapterPosition = rv.getChildAdapterPosition(child);

        if (child != null && gestureDetector.onTouchEvent(e)){
            Toast.makeText(this, geoObjects.get(adapterPosition).getGeoName(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    @Override
    public void onTouchEvent(RecyclerView recyclerView, MotionEvent motionEvent) {
    }

    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean b) {
    }
}
