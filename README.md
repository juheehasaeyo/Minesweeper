# Minesweeper
2023-2 모바일프로그래밍 지뢰찾기 프로젝트  

🗓️ 개발 기간: 2023.10.27 ~ 2023.11.29  
🛠️ 기술 스택: Android studio, Java  
<br>
✔ UI Design
- 9 × 9 TableLayout
- TableRows 9개를 TableLayout 에 추가
  - 각 TableRow 에 BlockButton[9][9] 총 81개 추가
- 남은 지뢰 수 - Textview
- 깃발 꽂기/해제 - ToggleButton  

✔ BlockButton Class (지뢰밭의 각 블록 표현)
- 필드
  - 버튼의 좌표 (x, y 좌표) : int x, int y
  - 지뢰인지 아닌지 표시: boolean mine
  - 깃발이 꽂혔는지 표시: boolean flag
  - 블록 주변의 지뢰 수 : int neighborMines
  - 깃발이 꽂힌 블록 수 : static int flags
  - 남은 블록 수 : static int blocks
 
- 메소드
   - 깃발 꽂기/해제 toggleFlag()
   - 블록 열기 breakBlock()

🖥 실행 화면
---
<img width="200" alt="1_start" src="https://github.com/juheehasaeyo/Minesweeper/assets/118191954/1cf070fe-054a-4cbc-831d-ff43deaf7e04">
<img width="200" alt="2_click" src="https://github.com/juheehasaeyo/Minesweeper/assets/118191954/6593c0f2-e055-469d-8753-8f23516f73a6">
<img width="200" alt="3_flag_mode" src="https://github.com/juheehasaeyo/Minesweeper/assets/118191954/85ff8e57-2358-4117-9076-5fa07785194d">
<br>
<br>
<img width="200" alt="4_you_win!" src="https://github.com/juheehasaeyo/Minesweeper/assets/118191954/1cf0624b-2ba1-4203-9a70-62f8074805ca">
<img width="200" alt="5_you_lose!" src="https://github.com/juheehasaeyo/Minesweeper/assets/118191954/5445f2ad-f927-4b0d-a462-018989fbeb86">
