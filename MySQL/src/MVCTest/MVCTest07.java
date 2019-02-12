package MVCTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// 앞서 작성한 메서드를 포함하는 UserDAO클래스를 작성하라 (Singleton패턴 이용)

class MVCTest07 {
	public static void main(String[] args) {
		UserDAO udao = UserDAO.getInstance();
		
		List<User07> list = udao.selectAllUsers();
		System.out.println(list);
    }
} // MVCTest07 클래스 끝.

class UserDAO {
	private static UserDAO udao = new UserDAO();
	
	private UserDAO() {	}
	
	public static UserDAO getInstance() {
		if(udao == null) {
			udao = new UserDAO();
		}
		return udao;
	}
	
    List<User07> selectAllUsers() {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System";  // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "SELECT * FROM book_ex.user_info;";
    	
    	Connection conn = null;
    	Statement stmt = null;
    	ResultSet rs = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		stmt= conn.createStatement(); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)  		  		
    		rs = stmt.executeQuery(query); // SQL문을 실행한다.
    		
    		List<User07> list = new ArrayList<>();
    		while (rs.next()) {
    			String user_id = rs.getString(1);
    			String password = rs.getString(2);
    			String name = rs.getString(3);
    			String email = rs.getString(4);
    			String in_date = rs.getString(5);
    			String up_date = rs.getString(6);
    			list.add(new User07(user_id, password, name, email, in_date, up_date));
    		}
    		return list;
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
    } // selectAllUsers() 끝.

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

    static User07 selectUser(String uId) {
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
    			return new User07(user_id, password, name, email, in_date, up_date);
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
    
    int updateUser(User07 user) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System"; // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "UPDATE user_info SET password = ?, name = ?, email = ? WHERE user_id = 'ff44e';";
    	
    	Connection conn = null;
    	PreparedStatement pstmt = null;
    	
    	try {
    		Class.forName("com.mysql.jdbc.Driver");
    		conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		
    		pstmt.setString(1, user.password);
    		pstmt.setString(2, user.name);
    		pstmt.setString(3, user.email);
    		
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
    
    int insertUser(User07 user) {
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
    		
    		pstmt.setString(1, user.userId);
    		pstmt.setString(2, user.password);
    		pstmt.setString(3, user.name);
    		pstmt.setString(4, user.email);
    		
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
    } // insertUser() 끝.
} // UserDAO 클래스 끝.

class User07 {
	String userId;
	String password;
	String name;
	String email;
	String in_date;
	String up_date;
	
	public User07(String userId, String password, String name, String email, String in_date, String up_date) {
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
		return "User07 [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email
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