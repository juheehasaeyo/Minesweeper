package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Context;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int flags;
    private static int blocks;
    ToggleButton toggleButton;
    public class BlockButton extends Button {
        private int x, y;
        private boolean mine;
        private boolean flag;
        private int neighborMines;
        public BlockButton(Context context, int x, int y) {
            super(context);
            this.x = x;
            this.y = y;
            this.mine = false;
            this.flag = false;
            this.neighborMines = 0;

            TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                    TableRow.LayoutParams.WRAP_CONTENT,
                    TableRow.LayoutParams.WRAP_CONTENT,
                    1.0f
            );
            this.setLayoutParams(layoutParams);
            // 클릭 리스너 설정
            setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // 토글 버튼이 체크된 경우 깃발 토글, 그렇지 않은 경우 블록 열기
                    if (toggleButton.isChecked()) {
                        toggleFlag();
                    } else {
                        breakBlock(view);
                    }
                }
            });
        }
        // 깃발 토글 메소드
        public void toggleFlag() {
            flag = !flag;
            if (flag) {
                setText("\uD83D\uDEA9"); // 깃발 표시
                flags++;
            } else {
                setText(""); // 깃발 해제
                flags--;
            }
        }

        // 블록 열기 메소드
        public boolean breakBlock(View view) {
            setClickable(false); // 블록을 클릭 안되는 상태로 변경

            if (mine) {
                // 지뢰인 경우
                setText("\uD83D\uDCA3"); // 지뢰 표시
                return true; // 지뢰를 클릭한 경우 true 반환
            } else {
                // 지뢰가 아닌 경우
                setText(String.valueOf(neighborMines)); // 주변 지뢰 수 표시
                setEnabled(false); // 블록 열린 상태로 표시
                blocks--; // 남은 블록 수 감소
                return false; // 지뢰가 아닌 경우 false 반환
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);

        TextView textView = findViewById(R.id.textView);
        int mines = 10;
        textView.setText("Mines : " + mines);

        TableLayout table = findViewById(R.id.tableLayout);
        BlockButton[][] buttons = new BlockButton[9][9];

        // 지뢰 배치
        ArrayList<Pair<Integer, Integer>> mineCoordinates = new ArrayList<>();
        Random random = new Random();

        for (int k = 0; k < 10; k++) {
            int x, y;
            do {
                x = random.nextInt(9);
                y = random.nextInt(9);
            } while (mineCoordinates.contains(new Pair<>(x, y)));

            mineCoordinates.add(new Pair<>(x, y));
            buttons[x][y] = new BlockButton(this, x, y);
            buttons[x][y].mine = true;
        }

        // 각 BlockButton의 이웃 지뢰 수 계산
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (buttons[i][j] == null) {
                    buttons[i][j] = new BlockButton(this, i, j);
                }
                BlockButton button = buttons[i][j];
                if (!button.mine) {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            int newX = i + dx;
                            int newY = j + dy;
                            if (newX >= 0 && newX < 9 && newY >= 0 && newY < 9 && buttons[newX][newY] != null && buttons[newX][newY].mine) {
                                button.neighborMines++;
                            }
                        }
                    }
                }
            }
        }
        // 버튼을 테이블에 추가
        for (int i = 0; i < 9; i++) {
            TableRow tableRow = new TableRow(this);
            table.addView(tableRow);
            for (int j = 0; j < 9; j++) {
                TableRow.LayoutParams layoutParams = new TableRow.LayoutParams(
                        TableRow.LayoutParams.WRAP_CONTENT,
                        TableRow.LayoutParams.WRAP_CONTENT,
                        1.0f);
                buttons[i][j].setLayoutParams(layoutParams);
                tableRow.addView(buttons[i][j]);

            }
        }
    }
}