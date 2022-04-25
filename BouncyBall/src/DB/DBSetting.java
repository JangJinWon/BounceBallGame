package DB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.Statement;

public class DBSetting {
	public static void main(String[] args) {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			Connection con = DriverManager.getConnection("jdbc:mysql://localhost", "root", "root"); // 접속
			Statement stmt = con.createStatement();
			// user_information이 있다면 삭제 없다면 무시
			stmt.executeUpdate("drop database if exists `user_information`");

			// user_information 스키마 생성
			stmt.execute("CREATE SCHEMA `user_information` DEFAULT CHARACTER SET utf8 COLLATE utf8_unicode_ci ;");																					
			// userlist 테이블 생성
			stmt.execute("CREATE TABLE `user_information`.`userlist` (`Index` INT NOT NULL, `ID` VARCHAR(45) NOT NULL, `PW` VARCHAR(45) NULL, `NickName` VARCHAR(45) NULL, `question` VARCHAR(45) NULL, `answer` VARCHAR(45) NULL, PRIMARY KEY (`Index`, `ID`));");
			// ranking 테이블 생성
			stmt.execute("CREATE TABLE `user_information`.`ranking` ( `Index` INT NOT NULL, `user1` INT NULL, `user2` INT NULL, `grade` INT NULL, PRIMARY KEY (`Index`),  INDEX `user1_idx` (`user1` ASC), INDEX `user2_idx` (`user2` ASC),CONSTRAINT `user1` FOREIGN KEY (`user1`) REFERENCES `user_information`.`userlist` (`Index`) ON DELETE CASCADE ON UPDATE CASCADE, CONSTRAINT `user2` FOREIGN KEY (`user2`) REFERENCES `user_information`.`userlist` (`Index`) ON DELETE CASCADE ON UPDATE CASCADE);");

			// user 계정이 없다면 생성, 있다면 무시하고 다음 진행
			stmt.execute("create user if not exists 'user'@'%' identified by '1234'");
			// user 계정에 user_information에 대한 권한 부여
			stmt.execute("grant select, insert, delete, update on `user_information`.* to user@'%'");
			System.out.println("입력완료");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}