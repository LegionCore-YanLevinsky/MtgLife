package core.legion.mtglife;

import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.Random;

import trikita.anvil.*;
import trikita.anvil.RenderableView;

import static trikita.anvil.DSL.*;

public class MainActivity extends AppCompatActivity {

    private int columns, count;

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private GridLayoutManager gridLayoutManager;
    private PlayerAdapter adapter;
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        initNavigationView();
        initRecyclerView();
    }

    @Override
    public boolean onCreatePanelMenu(int featureId, Menu menu) {
        drawerLayout.openDrawer(START);
        return false;
    }

    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START))
            drawerLayout.closeDrawer(GravityCompat.START);
        else super.onBackPressed();
    }

    private void initNavigationView() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.menu_reset:
                    changePlayerCount(count, columns);
                    break;
                case R.id.menu_roll_dice:
                    showRollDiceDialog();
                    break;
                case R.id.menu_flip_coin:
                    showFlipCoinDialog();
                    break;
            }
            drawerLayout.closeDrawer(START);
            return true;
        });
    }

    private void initRecyclerView() {
        gridLayoutManager = new GridLayoutManager(this, 1);
        adapter = new PlayerAdapter(this);

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setItemAnimator(null);
        recyclerView.setOverScrollMode(View.OVER_SCROLL_NEVER);

        changePlayerCount(2, 2);
    }

    public void playerCountSwitcher(View v) {
        switch (v.getId()) {
            case R.id.btn_one_player:
                changePlayerCount(1, 1);
                break;
            case R.id.btn_two_players:
                changePlayerCount(2, 2);
                break;
            case R.id.btn_three_players:
                changePlayerCount(3, 3);
                break;
            case R.id.btn_four_players:
                changePlayerCount(4, 2);
                break;
        }
        drawerLayout.closeDrawer(START);
    }

    private void changePlayerCount(int newCount, int newColumns) {
        count = newCount;
        columns = newColumns;

        gridLayoutManager.setSpanCount(columns);
        adapter.updatePlayers(count);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }

    private void showRollDiceDialog() {
        AlertDialog alertDialog = new AlertDialog.Builder(this).create();
        alertDialog.setView(new RenderableView(this) {
            @Override
            public void view() {
                linearLayout(() -> {
                    orientation(LinearLayout.VERTICAL);

                    linearLayout(() -> {
                        orientation(LinearLayout.HORIZONTAL);
                        gravity(CENTER);
                        padding(dip(8));

                        initDice(4);
                        initDice(6);
                        initDice(8);
//                        initDice(10);
                        initDice(12);
                        initDice(20);
                    });

                    linearLayout(() -> {
                        orientation(LinearLayout.HORIZONTAL);

                        initButton("SHUFFLE", v -> {
                            v.setEnabled(false);
                            Anvil.render();
                            new Handler().postDelayed(() -> v.setEnabled(true), 300);
                        });
                        initButton("OK!", v -> alertDialog.dismiss());
                    });
                });
            }
        });
        alertDialog.show();
    }

    private void initDice(int edges) {
        imageView(() -> {
            padding(dip(4));
            imageBitmap(DiceUtils.drawDice(edges, new Random().nextInt(edges) + 1));
            Anvil.currentView().animate().rotationBy(360).setDuration(300);
        });
    }

    private void initButton(String text, View.OnClickListener onClick) {
        button(() -> {
            size(WRAP, WRAP);
            weight(0.5f);
            backgroundColor(0);
            text(text);
            onClick(onClick);
        });
    }

    private void showFlipCoinDialog() {
        Toast.makeText(this, "to be continued", Toast.LENGTH_SHORT).show();
    }
}
