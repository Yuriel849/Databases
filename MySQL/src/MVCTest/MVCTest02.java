package MVCTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

// User객체를 매개변수로 받아 DB의 user_info테이블에 insert하는 insertUser()메서드를 작성하라.
//	(실제로 데이터가 잘저장되었는지 DB에서 확인하라)

class MVCTest02 {
    public static void main(String[] args) {
    	User02 user = new User02("gg88s","1234","tester","ccc@eee.com");
    	// 한글 사용할 경우, 에러 발생!
    		// Incorrect string value: '\xEB\x82\xA8' for column 'password' at row 1
    		// user_info 테이블을 확인하면 "latin1"	문자를 받도록 설정되어 있다.
    	
    	System.out.println(user);
    	
    	System.out.println(insertUser(user)); // 객체 user에 저장된 값을 DB의 user_info테이블에 insert하는 메서드
    }

    static int insertUser(User02 user) {
    	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8"; // DB이름인 book_ex를 적절히 변경
    	String DB_USER = "System";  // DB의 userid와 pwd를 알맞게 변경
    	String DB_PASSWORD = "IriDesCence";
    	
    	String query = "insert into book_ex.user_info values (?, ?, ?, ?, now(), now());";
    		// user_info의 컬럼 개수와 자료형과 일치해야 한다!
    			// 	끝에 "now(), now()"가 필요하다 -> user_info에는 마지막 2개 컬럼이 날짜를 받는 것이기 때문이다.    	
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
    }
} // MVCTest02 클래스 끝.

class User02 {
	String userId;
	String password;
	String name;
	String email;
	
	public User02(String userId, String password, String name, String email) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return "User02 [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
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