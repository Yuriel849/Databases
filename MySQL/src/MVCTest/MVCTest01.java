package MVCTest;

//user_info 테이블에 mapping하기 위한 User 클래스를 작성하고 테스트하라.
//	>> 	생성자를 적절히 작성하라 (Eclipse 기능 사용)
//	>> 	toString()을 적절히 오버라이딩하라 (Eclipse 기능 사용)
//	>> 	getter & setter를 추가하라 (Eclipse 기능 사용)


class MVCTest01 {
	public static void main(String[] args) {
		User user = new User("ff22f", "1234", "tester", "aaa@bbb.com");
		System.out.println(user);
	}
}

class User {
	String userId;
	String password;
	String name;
	String email;
	
	public User(String userId, String password, String name, String email) {
		super();
		this.userId = userId;
		this.password = password;
		this.name = name;
		this.email = email;
	}

	@Override
	public String toString() {
		return "User [userId=" + userId + ", password=" + password + ", name=" + name + ", email=" + email + "]";
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