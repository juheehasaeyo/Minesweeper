package com.example.minesweeper;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.content.Context;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private static int flags = 10; // 깃발이 꽂힌 블록 수
    private static int blocks = 81; // 남은 블록 수
    ToggleButton toggleButton;
    public static BlockButton[][] buttons;
    TextView minesTextView; // 남은 지뢰 수
    public class BlockButton extends Button {
        private int x, y; // 버튼의 x, y 좌표
        private boolean mine; // 지뢰인지 아닌지 표시
        private boolean flag; // 깃발이 꽂혔는지 표시
        private int neighborMines; // 블록 주변의 지뢰 수

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
                        breakBlock(buttons[x][y]);
                    }
                }
            });
        }
        // 블록 열기 메소드
        public boolean breakBlock(BlockButton button) {
            if (button.flag) {
                // 깃발이 꽂힌 블록을 클릭하면 아무 일도 일어나지 않도록
                return false;
            }
            button.setClickable(false); // 블록을 클릭 안되는 상태로 변경
            button.setBackgroundColor(Color.WHITE);

            if (button.mine) {
                // 블록이 지뢰라면
                button.setText("\uD83D\uDCA3"); // 지뢰 표시
                handleGameOver();
                setAllBlocksClickable(false);  // 게임 오버 시 모든 블록 비활성화
                return true; // 지뢰를 클릭한 경우 true 반환
            } else { // 블록이 지뢰가 아니라면
                if (button.neighborMines == 0) {
                    // 주변 지뢰 수가 0 → 주변의 모든 블록 열기
                    button.openNeighborBlocks(button.x, button.y, buttons);
                } else {
                    // 주변에 지뢰가 있는 경우 주변 지뢰 수 표시
                    button.setText(String.valueOf(button.neighborMines));
                }

                button.setEnabled(false); // 블록 열린 상태로 표시
                blocks--; // 남은 블록 수 감소

                if (blocks == 10) {
                    handleGameWin();
                    setAllBlocksClickable(false);  // 게임 오버 시 모든 블록 비활성화
                }
                return false; // 지뢰가 아닌 경우 false 반환
            }
        }
        // 깃발 토글 메소드
        public void toggleFlag() {
            // 깃발이 아니면
            if (!flag) {
                flag = true;
                setText("\uD83D\uDEA9");
                flags--;
            } else {
                flag = false;
                setText(""); // 깃발 해제
                flags++;
            }
            // Mines 텍스트 업데이트
            updateMinesText();
        }

        // 주변 블록 열기 (재귀 호출)
        private void openNeighborBlocks(int x, int y, BlockButton[][] buttons) {
            for (int dx = -1; dx <= 1; dx++) {
                for (int dy = -1; dy <= 1; dy++) {
                    int newX = x + dx;
                    int newY = y + dy;

                    // 주변에 있는 블록의 좌표가 유효한지 확인
                    if (newX >= 0 && newX < 9 && newY >= 0 && newY < 9) {
                        BlockButton neighborBlock = buttons[newX][newY];

                        // 주변 블록이 클릭되지 않았고, 깃발이 꽂힌 블록이 아니라면
                        if (neighborBlock != null && neighborBlock.isClickable() && !neighborBlock.flag) {
                            // 해당 블록을 열기
                            if (breakBlock(neighborBlock)) {
                                // 만약 열린 블록이 지뢰라면 재귀 호출하지 않음
                                continue;
                            }

                            // 주변에 지뢰가 없다면 해당 블록 주변의 블록들도 열기 (재귀 호출)
                            if (neighborBlock.neighborMines == 0) {
                                openNeighborBlocks(newX, newY, buttons);
                            }
                        }
                    }
                }
            }
        }
    }
    // 게임 종료 처리
    private void handleGameOver() {
        Toast.makeText(MainActivity.this, "Game Over!", Toast.LENGTH_LONG).show();
    }

    // 게임 승리 처리
    private void handleGameWin() {
        Toast.makeText(MainActivity.this, "You Win!", Toast.LENGTH_LONG).show();
    }
    // Mines 텍스트 업데이트 메소드
    private void updateMinesText() {
        minesTextView.setText("Mines : " + flags);
    }

    // 모든 블록을 클릭이 안되게 변경
    private void setAllBlocksClickable(boolean clickable) {
        TableLayout table = findViewById(R.id.tableLayout);
        for (int i = 0; i < 9; i++) {
            TableRow row = (TableRow) table.getChildAt(i);
            for (BlockButton blockButton : buttons[i]) {
                blockButton.setClickable(clickable);
            }
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        toggleButton = findViewById(R.id.toggleButton);
        minesTextView = findViewById(R.id.textView);

        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                if (isChecked) {
                    // On 상태: 깃발 모드
                    Toast.makeText(MainActivity.this, "깃발 모드", Toast.LENGTH_SHORT).show();
                } else {
                    // Off 상태: 블록 열기 모드
                    Toast.makeText(MainActivity.this, "블록 열기 모드", Toast.LENGTH_SHORT).show();
                }
            }
        });

        TableLayout table = findViewById(R.id.tableLayout);
        buttons = new BlockButton[9][9];

        // 지뢰 10개 랜덤 배치
        Random random = new Random();

        // x 좌표와 y 좌표를 각각 담을 ArrayList 생성
        List<Integer> xCoordinates = new ArrayList<>();
        List<Integer> yCoordinates = new ArrayList<>();

        for (int x = 0; x < 9; x++) {
            for (int y = 0; y < 9; y++) {
                xCoordinates.add(x);
                yCoordinates.add(y);
            }
        }
        Collections.shuffle(xCoordinates, random);
        Collections.shuffle(yCoordinates, random);

        for (int k = 0; k < 10; k++) {
            int x = xCoordinates.get(k);
            int y = yCoordinates.get(k);

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
        updateMinesText();
    }
}