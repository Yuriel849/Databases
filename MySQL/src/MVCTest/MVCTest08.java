package MVCTest;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

// 앞서 작성한 메서드를 포함하는 UserDAO2클래스를 작성하라 (Singleton패턴 이용)

class MVCTest08 {
	public static void main(String[] args) {
		UserDAO2 udao = UserDAO2.getInstance();
		
        User08 user = udao.selectUser("ff22f");
        System.out.println(user);

        udao.deleteUser("ff22f"); // 객체 user에 저장된 user_id와 동일한 행의 정보를 delete하는 메서드
        user = udao.selectUser("ff22f");
        System.out.println(user);
        
        user = new User08("ff22f", "1234", "tester", "aaa@bbb.com", "", "");
		// 한글 사용할 경우, 에러 발생!
        	// Incorrect string value: '\xEB\x82\xA8' for column 'password' at row 1
        	// user_info 테이블을 확인하면 "latin1"	문자를 받도록 설정되어 있다.
	
        System.out.println(user);
        System.out.println(udao.insertUser(user));
        
        System.out.println(udao.selectAllUsers());
        
   		udao.close(udao.conn, udao.pstmt, udao.rs);
    }
} // MVCTest08 클래스 끝.

class Dao {
	// Connection을 만드는데 사용할 DB의 URL, user ID, password 명시
	String DB_URL = "jdbc:mysql://localhost:3306/book_ex?useUnicode=true&characterEncoding=utf8";
	String DB_USER = "System"; // DB의 user ID와 password를 알맞게 변경
	String DB_PASSWORD = "IriDesCence";

	// query마다 개별적인 conn(DB와의 연결)을 가지고 있어야 한다.
	Connection conn = null;
	PreparedStatement pstmt = null;
	ResultSet rs = null;
	String tableName = "";
	 
	Dao() {
		this(null, "");
	}
	 
	Dao(String tableName) {
		this(null, tableName);
	}
	 
	Dao(Connection conn, String tableName) {
		// 생성자 호출 시 conn == null이면 Connection이 필요하니까 getConnection() 호출
		if(conn != null) {
			this.conn = conn;
		} else {
			this.conn = getConnection();
		}
		this.tableName = tableName;
	}
	
	// 별도로 Connection이 제공되지 않는다면 여기서 Connection을 새로 만들어서 제공한다.
	Connection getConnection() {
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver");
			conn = DriverManager.getConnection(DB_URL,  DB_USER, DB_PASSWORD); // 데이터베이스와 연결을 하여 그 연결을 "conn" 참조변수로 다룬다.
		} catch(Exception e) {
			e.printStackTrace();
		}
		return conn;
	}
	 
	void rollback(){
		if(conn!=null) {
			try {
				conn.rollback();
			} catch(SQLException e){ }
		}
	}
 
	// 가변인자를 사용 -> AutoCloseable한 객체를 여럿 입력하면 하나씩 close() 호출하면서 끝낸다.
	void close(AutoCloseable... acs) {
		try {
			for(AutoCloseable auto : acs) {
				auto.close();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
} // Dao 클래스 끝.

class UserDAO2 extends Dao { // Singleton 패턴 사용
	private static UserDAO2 udao = new UserDAO2();

	// 생성자
	private UserDAO2() {
		super(null,"");
	}
	
	// 외부에서 생성자에 직접 접근 못하니까 getInstance()로 대신 객체를 반환받도록.
	public static UserDAO2 getInstance() {
		if(udao == null) { // Singleton 패턴 -> UserDAO2 객체가 단 하나만 만들어질 수 있도록 제어
			udao = new UserDAO2();
		}
		return udao;
	}
	
	// 테이블의 모든 행의 모든 컬럼을 반환 받는다.
    List<User08> selectAllUsers() {
    	String query = "SELECT * FROM book_ex.user_info;";
    	
    	try {
    		// conn을 null check 한다 --> Connection이 null이면 사용할 수 없으니까
    		if(conn == null) {
    			new Dao();
    		}
    		pstmt= conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		rs = pstmt.executeQuery(); // SQL문을 실행한다.
    		
    		List<User08> list = new ArrayList<>();
    		while (rs.next()) {
    			String user_id = rs.getString(1);
    			String password = rs.getString(2);
    			String name = rs.getString(3);
    			String email = rs.getString(4);
    			String in_date = rs.getString(5);
    			String up_date = rs.getString(6);
    			list.add(new User08(user_id, password, name, email, in_date, up_date));
    		}
    		return list;
    	} catch(Exception e) {
    		e.printStackTrace();
       	}
		return null;
    } // selectAllUsers() 끝.
    
    
	// 테이블의 특정 행을 삭제한다.
    int deleteUser(String uId) {
    	String query = "DELETE FROM book_ex.user_info WHERE user_id='" +uId+ "';";
    	
    	try {
    		// conn을 null check 한다 --> Connection이 null이면 사용할 수 없으니까
    		if(conn == null) {
    			new Dao();
    		}
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		pstmt.executeUpdate(); // SQL문을 실행한다.
    		
    		return 0;
    	} catch(Exception e) {
    		e.printStackTrace();
       	}
		return -1; // 에러가 났을 경우 실행된다 -> 제대로 실행되지 않았다는 의미 (제대로 실행된 경우 1이나 0을 반환하니까
    } // deleteUser() 끝.

	// 테이블의 특정 행을 반환 받는다.
    User08 selectUser(String uId) {
    	String query = "SELECT * FROM book_ex.user_info WHERE user_id='" +uId+ "';";
    	
    	try {
    		// conn을 null check 한다 --> Connection이 null이면 사용할 수 없으니까
    		if(conn == null) {
    			new Dao();
    		}
    		pstmt= conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		rs = pstmt.executeQuery(); // SQL문을 실행한다.
    		
    		while (rs.next()) { 
    			String user_id = rs.getString(1);
    			String password = rs.getString(2);
    			String name = rs.getString(3);
    			String email = rs.getString(4);
    			String in_date = rs.getString(5);
    			String up_date = rs.getString(6);
    			return new User08(user_id, password, name, email, in_date, up_date);
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
       	}
		return null;
    } // selectUser() 끝.
    
	// 테이블의 특정 행의 정보를 수정한다.
    int updateUser(User08 user) {
    	String query = "UPDATE user_info SET password = ?, name = ?, email = ? WHERE user_id = 'ff44e';";
    	
    	try {
    		// conn을 null check 한다 --> Connection이 null이면 사용할 수 없으니까
    		if(conn == null) {
    			new Dao();
    		}
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		
    		// 순서대로 "query"의 물음표에 하나씩 문자열을 더한다.
    		pstmt.setString(1, user.getPassword()); // "...password = user.password..."
    		pstmt.setString(2, user.getName()); // "...name = user.name..."
    		pstmt.setString(3, user.getEmail()); // "...email = user.email..."
    		
    		return pstmt.executeUpdate(); // 준비된 PreparedStatement 객체를 DB에 업데이트 한다.
    									  // int형 반환값 -> 작업 후 영향 받은 열의 개수 OR 아무것도 하지 않았을 경우 0 반환
    	} catch(Exception e) {
    		e.printStackTrace();
       	}
    	return -1; // 에러가 났을 경우 실행된다 -> 제대로 실행되지 않았다는 의미 (제대로 실행된 경우 1이나 0을 반환하니까
    } // updateUser() 끝.
    
	// 테이블의 새로운 행을 추가한다.
    int insertUser(User08 user) {
    	String query = "insert into book_ex.user_info values (?, ?, ?, ?, now(), now());";
    		// user_info의 컬럼 개수와 자료형과 일치해야 한다!
    			// 끝에 "now(), now()"가 필요하다 -> user_info에는 마지막 2개 컬럼이 날짜를 받는 것이기 때문이다.
    	
    	try {
    		// conn을 null check 한다 --> Connection이 null이면 사용할 수 없으니까
    		if(conn == null) {
    			new Dao();
    		}
    		pstmt = conn.prepareStatement(query); // "query" 문자열을 가지고 PreparedStatement 객체를 만든다 (DB에 보내려는 명령을 패키지에 담은 것)
    		
    		// 순서대로 "query"의 물음표에 하나씩 문자열을 더한다.
    		pstmt.setString(1, user.getUserId()); // "...userId = user.userId..."
    		pstmt.setString(2, user.getPassword()); // "...password = user.password..."
    		pstmt.setString(3, user.getName()); // "...name = user.name..."
    		pstmt.setString(4, user.getEmail()); // "...email = user.email..."
    		
    		return pstmt.executeUpdate(); // 준비된 PreparedStatement 객체를 DB에 업데이트 한다.
			  							  // int형 반환값 -> 작업 후 영향 받은 열의 개수 OR 아무것도 하지 않았을 경우 0 반환
    	} catch(Exception e) {
    		e.printStackTrace();
       	}
    	return -1; // 에러가 났을 경우 실행된다 -> 제대로 실행되지 않았다는 의미 (제대로 실행된 경우 1이나 0을 반환하니까
    } // insertUser() 끝.
} // UserDAO2 클래스 끝.

class User08 {
	private String userId;
	private String password;
	private String name;
	private String email;
	private String in_date = "";
	private String up_date = "";
	
	// 생성자
	public User08(String userId, String password, String name, String email, String in_date, String up_date) {
		super();
		this.setUserId(userId);
		this.setPassword(password);
		this.setName(name);
		this.setEmail(email);
		this.setIn_date(in_date);
		this.setUp_date(up_date);
	}

	@Override
	public String toString() {
		return "User08 [userId=" + getUserId() + ", password=" + getPassword() + ", name=" + getName()
				+ ", email=" + getEmail() + ", in_date=" + getIn_date() + ", up_date=" + getUp_date() + "]";
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

	public String getIn_date() {
		return in_date;
	}

	public void setIn_date(String in_date) {
		this.in_date = in_date;
	}

	public String getUp_date() {
		return up_date;
	}

	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
}