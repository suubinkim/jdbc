package hello.jdbc.connection;

import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class DBConnectionUtil {

    // 자바에서 제공하는 표준 인터페이스
    public static Connection getConnection() {
        try {
            //라이브러리에서 db 드라이버를 찾아서 해당 드라이버가 제공하는 커넥션 반환 (db연결)
            //반환된 커넥션은 JDBC가 제공하는 커넥션 인터페이스를 구현하고 있음
            Connection connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
            log.info("get connection={}, class={}", connection, connection.getClass());
            return connection;
        } catch (SQLException e) {
            throw new IllegalStateException(e);
        }
    }
}
