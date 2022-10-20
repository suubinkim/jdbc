package hello.jdbc.exception.basic;

import org.junit.jupiter.api.Test;

import java.net.ConnectException;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class UnCheckedAppTest {

    @Test
    void unchecked() {
        Controller controller = new Controller();
        assertThatThrownBy(controller::request)
                .isInstanceOf(RuntimeException.class);
    }

    static class Controller {
        Service service = new Service();

        public void request() {
            service.logic();
        }
    }

    static class Service {
        Repository repository = new Repository();
        NetWorkClient netWorkClient = new NetWorkClient();

        public void logic() {
            repository.call();
            netWorkClient.call();
        }
    }

    static class NetWorkClient {
        public void call() {
            throw new RunTimeConnectException("연결 실패");
        }
    }

    static class Repository {
        public void call() {
            try {
                runSQL();
            } catch (SQLException e) {
                throw new RunTimeSQLException(e);
            }
        }

        public void runSQL() throws SQLException {
            throw new SQLException("ex");
        }
    }

    static class RunTimeConnectException extends RuntimeException {
        public RunTimeConnectException(String message) {
            super(message);
        }
    }

    static class RunTimeSQLException extends RuntimeException {
        public RunTimeSQLException(Throwable cause) {
            super(cause);
        }
    }
}
