package main;
import java.io.File;

//설정@테이블 관리
public class Setting {	//싱글톤 연습
	
    private static Setting instance = null;
    //식당 설정 정보
	public static final String R_NAME = "수타게 수타한 제면소";		//식당이름
	
	//테이블 설정 정보
    public static final String TABLE_DIR = "./table";		//폴더 경로 
	public static final char ROW_START='A';					//행 시작
	public static final char ROW_END='D';					//행 끝
	public static final int COL_START=1;					//열 시작
	public static final int COL_END=5;						//열 끝
    private String[][] tableArray;							//테이블 어레이
    
    //서버 설정 정보
    //로컬"127.0.0.1";  
    //"192.168.20.254"; 발표용
    public static final String SERVER_IP = "127.0.0.1";			// 서버 IP 주소
	public static final int LOG_IN_PORT = 7913; 			// 서버 포트 번호 
	public static final int ADMIN_PORT = 1217; 				// 서버 포트 번호

    public static final int MAX_ATTEMPTS = 5;				//최대 시도횟수 5
	
    //생성자
    private Setting() {
        // 행 개수와 열 개수를 계산하여 배열 생성
        int numRows = ROW_END - ROW_START + 1;
        int numCols = COL_END - COL_START + 1;
        tableArray = new String[numRows][numCols];			//초기값 줘야 오류 안남
    }
    
    //생성자
	public static Setting getInstance() {
        if (instance == null) {
            instance = new Setting();
        }
        return instance;
    }
	//출력:전체 테이블 업데이트후 출력
		public void listAllTable() {
			System.out.println(Main.BAR);
			resetTableArray();
			printAllTable(tableArray);
		} 

		//업데이트:테이블 정보
		public String[][] resetTableArray() {		//자료 남는걸 방지하기 위해 배열 초기화
			for (int i = 0; i < tableArray.length; i++) {
				for (int j = 0; j < tableArray[i].length; j++) {
					tableArray[i][j] = null;
				}
			} 			
			File dir =new File(TABLE_DIR);        	//테이블 폴더
			File[]tables=dir.listFiles();        	//디렉토리내 모든 파일과 디렉토리 가져옴

			for(File file: tables) {
				if(file.isFile()) {
					String fileName = file.getName();
					if (fileName.endsWith(".txt")) { 		//. txt 파일인지 확인
						char row = fileName.charAt(0);  	//행정보로 사용할 첫 알파벳 추출
						int col = Integer.parseInt(fileName.substring(1,fileName.length()-4));//열정보 나중에 2자리수 이상 쓸것을 대비 .txt만 지우고 1~.txt 이전까지 지정
						if (row >= ROW_START && row <= ROW_END && col >= COL_START && col <= COL_END) {//범위에 있는지 확인
							// 해당 테이블을 2차원 배열에 추가
							tableArray[row - ROW_START][col - COL_START] = fileName.substring(0, fileName.length() - 4);
						}
					}
				}
			}
			return tableArray; //2차원 배열 리턴
		}//end of createTableArray

		//출력:전체 테이블
		public void printAllTable(String[][] tables) {
			String ANSI_RED = "\u001B[31m";  //빨간글씨
			String ANSI_RESET = "\u001B[0m"; //리셋

			for (String[] row : tables) {
				for (String cell : row) {
					System.out.print(cell != null ? cell+"\t" : ANSI_RED + "X0\t" + ANSI_RESET);
				}
				System.out.println(); // 다음 행으로 이동
			}//end of for
		} // end of printAllTable
}