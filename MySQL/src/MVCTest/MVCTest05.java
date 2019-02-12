package MVCTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

// user_id를 매개변수로 받아 user_info테이블에서 해당 사용자의 정보를 삭제하는 deleteUser메서드를 작성하라.

class MVCTest05 {
    public static void main(String[] args) {
        User05 user = selectUser("ff22f");
        System.out.println(user);

        deleteUser("ff22f"); // 객체 user에 저장된 user_id와 동일한 행의 정보를 delete하는 메서드
        user = selectUser("ff22f");
        System.out.println(user);
    }
    
    static int deleteUser(String uId) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System";  // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "DELETE FROM book_ex.user_info WHERE user_id='" +uId+ "';";
    	
    	Connection conn = null;
    	Statement stmt = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		stmt= conn.createStatement(); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)  		
    		stmt.executeQuery(query); // SQL문을 실행한다.
    		
    		return 0;
    	} catch(Exception e) {
    		e.printStackTrace();
       	} finally {
    		try {
    			stmt.close();
    			conn.close();
    		} catch ( SQLException e ) {}
    	}
		return -1;    	
    } // deleteUser() 끝.

    static User05 selectUser(String uId) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System";  // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "SELECT * FROM book_ex.user_info WHERE user_id='" +uId+ "';";
    	
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		stmt= conn.createStatement(); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)  		
    		rs = stmt.executeQuery(query); // SQL문을 실행한다.
    		
    		while (rs.next()) { 
    			String user_id = rs.getString(1);
    			String password = rs.getString(2);
    			String name = rs.getString(3);
    			String email = rs.getString(4);
    			String in_date = rs.getString(5);
    			String up_date = rs.getString(6);
    			return new User05(user_id, password, name, email, in_date, up_date);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
       	} finally {
    		try {
    			rs.close();
    			stmt.close();
    			conn.close();
    		} catch ( SQLException e ) {}
    	}
		return null;
    } // selectUser() 끝.
    
    static int updateUser(User05 user) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System"; // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "UPDATE user_info SET password = ?, name = ?, email = ? WHERE user_id = ?;";
    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		
    		pstmt.setString(1, user.getPassword());
    		pstmt.setString(2, user.getName());
    		pstmt.setString(3, user.getEmail());
    		pstmt.setString(4, user.getUserId());
    		
    		return pstmt.executeUpdate(); // 준비된 PreparedStatement 객체를 DB에 업데이트 한다.
    									  // int형 반환값 -> 작업 후 영향 받은 열의 개수 OR 아무것도 하지 않았을 경우 0 반환
    	} catch(Exception e) {
    		e.printStackTrace();
       	} finally {
    		try {
    			pstmt.close();
    			conn.close();
    		} catch ( SQLException e ) {}
    	}
    	return -1;
    } // updateUser() 끝.
    
    static int insertUser(User05 user) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System"; // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "insert into book_ex.user_info values (?, ?, ?, ?, now(), now());";
			// user_info의 컬럼 개수와 자료형과 일치해야 한다!
				// 끝에 "now(), now()"가 필요하다 -> user_info에는 마지막 2개 컬럼이 날짜를 받는 것이기 때문이다.    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		
    		pstmt.setString(1, user.getUserId());
    		pstmt.setString(2, user.getPassword());
    		pstmt.setString(3, user.getName());
    		pstmt.setString(4, user.getEmail());
    		
    		return pstmt.executeUpdate();  // 준비된 PreparedStatement 객체를 DB에 업데이트 한다.
			  							   // int형 반환값 -> 작업 후 영향 받은 열의 개수 OR 아무것도 하지 않았을 경우 0 반환
    	} catch(Exception e) {
    		e.printStackTrace();
       	} finally {
    		try {
    			pstmt.close();
    			conn.close();
    		} catch ( SQLException e ) {}
    	}
    	return -1;
    } // insertUser() 끝.
} // MVCTest05 클래스 끝.

class User05 {
	String userId;
	String password;
	String name;
	String email;
	String in_date;
	String up_date;
	
	public User05(String userId, String password, String name, String email, String in_date, String up_date) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
		this.in_date = in_date;
		this.up_date = up_date;
	}

	@Override
	public String toString() {
		return "User05 [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email
				+ ", in_date=" + in_date + ", up_date=" + up_date + "]";
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}