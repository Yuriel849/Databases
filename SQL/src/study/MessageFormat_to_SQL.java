package study;

import java.io.*;
import java.text.MessageFormat;

/*
 * 1. SQL Developer에서 주문 테이블 생성 (ORDER_M, ORDER_D)
 * 2. 엑셀 프로그램을 "data.csv"로 저장
 * 3. Java 프로그램 (eclipse)로 INSERT 생성 & SQL Developer에서 실행
 * 4. ORDER_M & ORDER_D를 join
 */

class MessageFormat_to_SQL {
	@SuppressWarnings("resource")
	public static void main(String[] args) throws IOException {
		String tableName = "ORDER_M";
		String msg = "INSERT INTO " + tableName + " VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}'');";
		
		FileReader fr = new FileReader("C:\\Users\\Yuriel\\eclipse-workspace\\SQL\\ORDER_M.csv");
		BufferedReader br = new BufferedReader(fr);
		
		Object[][] arguments = new Object[5][60];
		String line = "";
		for(int i = 1; (line = br.readLine()) != null; i++) {
			arguments[i-1] = line.split(",");
		}

		FileWriter fw = new FileWriter("C:\\Users\\Yuriel\\eclipse-workspace\\SQL\\INSERT_ORDER.sql");
		BufferedWriter bw = new BufferedWriter(fw);
		
		for(int i = 0; i < arguments.length; i++) {
			String result = MessageFormat.format(msg,  arguments[i]);
			System.out.println(result);
			bw.write(result + "\n");
		}
		
		
		tableName = "ORDER_D";
		msg = "INSERT INTO " + tableName + " VALUES (''{0}'', ''{1}'', ''{2}'', ''{3}'', ''{4}''"
				+ ", ''{5}'', ''{6}'', ''{7}'');";
		
		fr = new FileReader("C:\\Users\\Yuriel\\eclipse-workspace\\SQL\\ORDER_D.csv");
		br = new BufferedReader(fr);
		
		Object[][] arguments2 = new Object[6][100];
		line = "";
		for(int i = 1; (line = br.readLine()) != null; i++) {
			arguments2[i-1] = line.split(",");
		}

		for(int i = 0; i < arguments2.length; i++) {
			String result = MessageFormat.format(msg,  arguments2[i]);
			System.out.println(result);
			bw.write(result + "\n");
		}
		bw.close();
	}
}
