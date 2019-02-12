package study;

import java.sql.*;

public class JdbcTest {
	public static void main(String[] args) {
		test_jdbc_connect();
	} // main()의 끝
	
	static void test_jdbc_connect() {
			// DB이름인 book_ex를 적절히 변경해서 사용
		String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8";
			// DB의 user id & pw를 알맞게 변경해서 사용
		String DB_USER = "System";
		String DB_PASSWORD = "IriDesCence";
     
		Connection conn = null;
		Statement stmt = null;
		ResultSet rs = null;
		
		String query = "SELECT now()"; // 시스템의 날짜를 출력한다.
		try {
			// 드라이버를 로딩한다.
			Class.forName("com.mysql.jdbc.Driver");
			
			conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD); // 데이터베이스의 연결을 설정한다.
			stmt = conn.createStatement(); // Statement를 가져온다.
			rs = stmt.executeQuery(query); // SQL문을 실행한다.
      
			while (rs.next()) {
				String curDate = rs.getString(1);
				System.out.println(curDate);
			}
		} catch ( Exception e ) {
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				stmt.close();
				conn.close();
			} catch ( SQLException e ) {}
		}
    }
}