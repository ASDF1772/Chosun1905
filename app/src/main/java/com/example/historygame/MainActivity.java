package com.example.historygame;

import android.content.res.Resources;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.ConstraintSet;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity {

    private long backKeyPressedTime;    //앱종료 위한 백버튼 누른시간

    private boolean isSettingCreated = false;
    private boolean isAchieveCreated = false;

    private ChoiceItemAdapter choiceItemAdapter;
    private ContentWriteAnimator contentWriteAnimator;

    private DrawerLayout drawer;
    private GridView abilityGrid;
    private ListView choiceList;
    private View choiceListBottomDivider;

    private Button abilityOpenButton;
    private ImageButton abilityCloseButton;

    private TextView health;
    private TextView mental;
    private TextView money;

    //이벤트 내용
    private ScrollView storyScrollView;
    private ImageView contentImage;
    private TextView contents;
    private String contentText;
    private TextView pageNumText;

    private Toast lastToast;
    private View toastView;

    private TextView typeSpeedText;
    /////////////////////////////////////////////////////////////////////////////////////
    //
    //                 현재 상태
    //
    //  Player 클래스는 싱글톤으로 abilityList 체력, 정신력, 돈 가지고 있음
    //
    //  addtext 버튼 누르면 이벤트 시작과 ASDF 테스트 능력 추가
    //
    //  '뒤로가기' 버튼 누르면 ASDF 능력 삭제
    //
    //  '뒤로가기' 버튼 두 번 누르면 종료
    //
    //  선택지 누르면 다음으로 넘어감
    //
    //  스토리 추가예정
    //
    //  주석 열심히 달아야겠다.
    //
    //  시작하는 이벤트들은 enableEventList에 추가, 연계는 array.xml에 item 마지막 부분에 이름 넣으면 됨
    //
    //  함수 설명
    //
    //  onBackPressed       : '뒤로가기' 버튼 누를때 이벤트 설정
    //
    //  createDrawer        : 드로어 레이아웃에 스크롤 생기는걸 없애준다
    //
    //  createMenuBar       : 메뉴바에 있는 버튼의 이벤트 설정(체력, 정신력등 UI 추가 예정)
    //
    //  createAbilityLayout : 열고 닫는 능력창을 설정해준다
    //
    //  createSettingLayout : 설정창 여는 버튼 누를 때 호출/설정에 있는 레이아웃 초기 설정
    //
    //  createAchieveLayout : 도전과제창 여는 버튼 누를 때 호출/도전과제에 있는 레이아웃 초기 설정
    //
    //  createChoiceList    : 선택지 리스트를 생성함
    //
    //  selectEvent         : 다음 이벤트 선택, 예정된 이벤트가 없으면 enableEventList에서 무작위로 뽑아 선택
    //
    //  setListChoice       : 이벤트 선택 후에 밑에 선택지 창 설정
    //
    //  addAbility          : 플레이어에게 능력을 추가함
    //
    //  removeAbility       : 플레이어에게 능력을 제거함
    //
    //  onlyShowToast       : toast.show() 대신에 쓰자. 이미 떠 있는 Toast를 제거하고 toast를 띄운다.
    //
    //
    ///////////////////////////////////////////////////////////////////////////////////////


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        createDrawer();
        createMenuBar();
        createAbilityLayout();
        createContentView();
        createChoiceList();

        createContentAnimator();

        selectEvent();

        lastToast = null;
        toastView = getLayoutInflater().inflate(R.layout.toast_custom, null);
    }

    @Override
    public void onBackPressed() {
        //드로어 열려있으면 닫아줌
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (drawer.isDrawerOpen(GravityCompat.END)) {
            drawer.closeDrawer(GravityCompat.END);
        }
        //능력창 열려있으면 닫아줌
        else if (!abilityOpenButton.isEnabled()) {
            ConstraintLayout constraintLayout = findViewById(R.id.constraint);
            ConstraintSet set = new ConstraintSet();
            set.clone(constraintLayout);
            set.connect(R.id.layout_ability, ConstraintSet.TOP, R.id.text_page_num, ConstraintSet.BOTTOM);
            set.connect(R.id.layout_ability, ConstraintSet.BOTTOM, View.NO_ID, ConstraintSet.BOTTOM);
            set.applyTo(constraintLayout);

            abilityOpenButton.setEnabled(true);
            abilityGrid.setSelection(0);
        }
        //아무것도 안 열려있을때 두번 누르면 나감
        else {
            if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
                backKeyPressedTime = System.currentTimeMillis();
            } else {
                finish();
                System.exit(0);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }
    }

    private void createDrawer() {
        //드로어에 스크롤 생기는 걸 없애주는 코드
        NavigationView achieveNav = findViewById(R.id.nav_view_achieve);
        NavigationView settingNav = findViewById(R.id.nav_view_setting);

        achieveNav.setVerticalFadingEdgeEnabled(false);
        settingNav.setVerticalFadingEdgeEnabled(false);

        for (int i = 0; i < achieveNav.getChildCount(); i++) {
            achieveNav.getChildAt(i).setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
        for (int i = 0; i < settingNav.getChildCount(); i++) {
            settingNav.getChildAt(i).setOverScrollMode(View.OVER_SCROLL_NEVER);
        }
    }

    private void createMenuBar() {
        ImageButton settingOpenButton = findViewById(R.id.button_setting_open);
        ImageButton achieveOpenButton = findViewById(R.id.button_achieve_open);

        contentImage = findViewById(R.id.image_story);

        health = findViewById(R.id.health);
        mental = findViewById(R.id.mental);
        money = findViewById(R.id.money);

        drawer = findViewById(R.id.drawer_layout);
        drawer.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);

        achieveOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.END);

                //처음 열 때 레이아웃을 만들어야 한다
                if (!isAchieveCreated) {
                    createAchieveLayout();
                    isAchieveCreated = true;
                }
            }
        });

        settingOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.openDrawer(GravityCompat.START);

                //처음 열 때 레이아웃을 만들어야 한다
                if (!isSettingCreated) {
                    createSettingLayout();
                    isSettingCreated = true;
                }
            }
        });
    }

    private void createAbilityLayout() {
        AbilityGridAdapter gridAdapter = new AbilityGridAdapter(Player.getPlayer().getAbilities());
        abilityGrid = findViewById(R.id.grid_ability);
        abilityGrid.setAdapter(gridAdapter);

        abilityOpenButton = findViewById(R.id.button_ability_open);
        abilityOpenButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ConstraintLayout 바꿔주는 코드
                ConstraintLayout constraintLayout = findViewById(R.id.constraint);
                ConstraintSet set = new ConstraintSet();
                set.clone(constraintLayout);
                set.connect(R.id.layout_ability, ConstraintSet.TOP, View.NO_ID, ConstraintSet.BOTTOM);
                set.connect(R.id.layout_ability, ConstraintSet.BOTTOM, R.id.constraint, ConstraintSet.BOTTOM);
                set.applyTo(constraintLayout);

                //능력창 여는 버튼 비활성화
                abilityOpenButton.setEnabled(false);
            }
        });

        abilityCloseButton = findViewById(R.id.button_ability_close);
        abilityCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //ConstraintLayout 바꿔주는 코드
                ConstraintLayout constraintLayout = findViewById(R.id.constraint);
                ConstraintSet set = new ConstraintSet();
                set.clone(constraintLayout);
                set.connect(R.id.layout_ability, ConstraintSet.TOP, R.id.text_page_num, ConstraintSet.BOTTOM);
                set.connect(R.id.layout_ability, ConstraintSet.BOTTOM, View.NO_ID, ConstraintSet.BOTTOM);
                set.applyTo(constraintLayout);

                //능력창 여는 버튼 활성화
                abilityOpenButton.setEnabled(true);
                //능력창 스크롤 맨 위로 올려줌
                abilityGrid.setSelection(0);
            }
        });

        //다시 시작 버튼 클릭시
        Button restartButton = findViewById(R.id.button_restart);
        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //다시 시작
                restart();
            }
        });
    }

    private void createSettingLayout() {
        ImageButton settingCloseButton = findViewById(R.id.button_setting_close);
        settingCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
            }
        });

        typeSpeedText = findViewById(R.id.text_type_speed);
        SeekBar seekBar = findViewById(R.id.seek_bar_write_speed);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                typeSpeedText.setText(Integer.toString(progress + 10));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int delay = 60 - seekBar.getProgress();
                contentWriteAnimator.setWriteDelay(delay);
            }
        });
    }

    private void createAchieveLayout() {
        ImageButton achieveCloseButton = findViewById(R.id.button_achieve_close);
        achieveCloseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.END);
            }
        });

        ListView achieveList = findViewById(R.id.list_achieve);
        AchieveListAdapter adapter = new AchieveListAdapter();
        achieveList.setAdapter(adapter);

        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
        adapter.addItem("???", "???");
    }

    private void createContentView() {
        contents = findViewById(R.id.text_story);
        pageNumText = findViewById(R.id.text_page_num);
    }

    private void createChoiceList() {
        choiceItemAdapter = new ChoiceItemAdapter();

        choiceListBottomDivider = findViewById(R.id.divider_choice_list);
        choiceList = findViewById(R.id.list_choice);
        choiceList.setAdapter(choiceItemAdapter);

        choiceList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ChoiceItem choice = choiceItemAdapter.getItem(position);
                Player player = Player.getPlayer();

                //페이지 수 증가
                player.increasePageNum();
                pageNumText.setText("-" + player.getPageNum() + "-");

                String requireAbility = choice.getRequireAbility();
                //요구 능력이 없으면 토스트 띄우고 리턴
                if (!requireAbility.equals("")) {
                    if (!player.searchAbility(requireAbility)) {
                        Toast toast = Toast.makeText(MainActivity.this, requireAbility + " 없음!", Toast.LENGTH_SHORT);
                        onlyShowToast(toast);
                        return;
                    }

                    if (requireAbility.equals("체력")) {
                        decreaseHealth();
                    } else if (requireAbility.equals("정신력")) {
                        decreaseMental();
                    } else if (requireAbility.equals("돈")) {
                        decreaseMoney();
                    }
                }

                String acquireAbility = choice.getAcquireAbility();
                //능력 획득 or 상실
                if (!acquireAbility.equals("")) {
                    if (acquireAbility.startsWith("+")) {
                        addAbility(acquireAbility.substring(1));
                    } else {
                        removeAbility(acquireAbility.substring(1));
                    }
                }

                int choiceHealth = choice.getHealth();
                int choiceMental = choice.getMental();
                int choiceMoney = choice.getMoney();

                if (choiceHealth > 0 && player.getHealth() < 3) {
                    increaseHealth();
                } else if (choiceHealth < 0 && player.getHealth() > 0) {
                    decreaseHealth();
                }
                if (choiceMental > 0 && player.getMental() < 3) {
                    increaseMental();
                } else if (choiceMental < 0 && player.getMental() > 0) {
                    decreaseMental();
                }
                if (choiceMoney > 0 && player.getMoney() < 3) {
                    increaseMoney();
                } else if (choiceMoney < 0 && player.getMoney() > 0) {
                    decreaseMoney();
                }

                health.setText(Integer.toString(player.getHealth()));
                mental.setText(Integer.toString(player.getMental()));
                money.setText(Integer.toString(player.getMoney()));

                EventSelector.getInstance().nextEvent = choice.getNextEvent();

                selectEvent();
            }
        });
    }

    private void createContentAnimator(){
        storyScrollView = findViewById(R.id.scroll_view_story);
        contentWriteAnimator = new ContentWriteAnimator(choiceList, choiceListBottomDivider, contents);
        LinearLayout storyLayout = findViewById(R.id.scroll_layout_story);

        storyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                contentWriteAnimator.finishAnimation();
            }
        });
    }

    private void selectEvent() {
        if(EventSelector.getInstance().isEnd){
            restart();
            return;
        }

        String eventName = EventSelector.getInstance().selectEvent();

        //이름으로 array.xml에서 이벤트 String[] 가져오기
        Resources res = getResources();
        String packName = this.getPackageName();

        int arrayId = res.getIdentifier(eventName, "array", packName);
        String[] choices = res.getStringArray(arrayId);

        choiceItemAdapter.clear();

        try {
            int i = 0;

            //이미지가 있다면 변경
            if (choices[0].contains("image:")) {
                String resName = "@drawable/" + choices[0].substring(6);

                int drawableId = res.getIdentifier(resName, "drawable", packName);

                contentImage.setImageResource(drawableId);
                contentImage.setVisibility(View.VISIBLE);

                i++;
            }
            else {
                contentImage.setVisibility(View.GONE);
            }

            storyScrollView.fullScroll(ScrollView.FOCUS_UP);
            choiceList.setVisibility(View.GONE);
            choiceListBottomDivider.setVisibility(View.GONE);


            contentText = choices[i++];
            contentWriteAnimator.animateText(contentText);


            ChoiceItem choiceItem;
            String[] choiceStr = new String[7];

            for (; i < choices.length; i++) {
                String[] tmp = choices[i].split(":");

                for(int j = 0; j < 7; j++){
                    if(j < tmp.length){
                        choiceStr[j] = tmp[j];
                    }
                    else{
                        choiceStr[j] = "";
                    }
                }


                choiceItem = new ChoiceItem(choiceStr);
                choiceItemAdapter.addItem(choiceItem);
            }
        }
        catch (ArrayIndexOutOfBoundsException e) {
            Log.e("error", "문자열 인덱스에 이상있음");
            e.printStackTrace();
        }

        choiceList.setAdapter(choiceItemAdapter);
    }

    private void addAbility(String ability) {
        //추가되었다면 toast 출력, 중복이면 나감
        boolean result = Player.getPlayer().addAbility(ability);
        if (result) {
            //그리드뷰에 나타나게 해줌
            abilityGrid.setSelection(0);
            //토스트 출력
            makeToast("+ " + ability, true);
        }
    }

    private void removeAbility(String ability) {
        //addAbility와 똑같음
        boolean result = Player.getPlayer().removeAbility(ability);

        if (result) {
            abilityGrid.setSelection(0);

            makeToast("- " + ability, false);
        }
    }

    private void restart(){
        EventSelector.getInstance().initEventList();
        selectEvent();
        Player.getPlayer().init();
        pageNumText.setText("-" + Player.getPlayer().getPageNum() + "-");
        abilityCloseButton.callOnClick();

        health.setText(Integer.toString(Player.getPlayer().getHealth()));
        mental.setText(Integer.toString(Player.getPlayer().getMental()));
        money.setText(Integer.toString(Player.getPlayer().getMoney()));
    }

    private void makeToast(String text, boolean isGain){
        if(isGain){
            toastView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.toast_gain));
        }
        else{
            toastView.setBackground(ContextCompat.getDrawable(MainActivity.this, R.drawable.toast_lose));
        }

        TextView toastText = toastView.findViewById(R.id.text_toast);
        toastText.setText(text);

        Toast toast = new Toast(MainActivity.this);
        toast.setView(toastView);
        toast.setDuration(Toast.LENGTH_SHORT);
        onlyShowToast(toast);
    }

    private void onlyShowToast(Toast toast) {
        if(lastToast != null){
            lastToast.cancel();
        }

        toast.show();
        lastToast = toast;
    }

    private void decreaseHealth() {
        Player.getPlayer().decreaseHealth();

        makeToast("- 체력", false);
    }

    private void increaseHealth() {
        Player.getPlayer().increaseHealth();

        makeToast("+ 체력", true);
    }

    private void decreaseMental() {
        Player.getPlayer().decreaseMental();

        makeToast("- 정신력", false);
    }

    private void increaseMental() {
        Player.getPlayer().increaseMental();

        makeToast("+ 정신력", true);
    }

    private void decreaseMoney() {
        Player.getPlayer().decreaseMoney();

        makeToast("- 돈", false);
    }

    private void increaseMoney() {
        Player.getPlayer().increaseMoney();

        makeToast("+ 돈", true);
    }
}