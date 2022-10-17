package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.NoSuchElementException;

import static org.springframework.jdbc.support.JdbcUtils.*;

/**
 * 트랜젝션 - 트랜젝션 매니저
 * DataSourceUtils.getConnection()
 * DataSourceUtils.releaseConnection()
 */
@Slf4j
public class MemberRepositoryV3 {

    private final DataSource dataSource;

    public MemberRepositoryV3(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void save(Member member) throws SQLException {
        String sql = "insert into member(member_id,money) values (?, ?)";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            // 트랜젝션 동기화를 위해서는 유틸 사용
            con = DataSourceUtils.getConnection(dataSource);
            log.info("dataSource={},class={}", con, con.getClass());
            pstmt = con.prepareStatement(sql);
            //바인딩
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();//쿼리 실행
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            closeStatement(pstmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }


    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("dataSource={},class={}", con, con.getClass());
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery(); //조회 결과 반환
            if (rs.next()) { //next 호출해줘야 실제 값 부터 나옴
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found memberId =" + memberId);
            }
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            closeResultSet(rs);
            closeStatement(pstmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public void update(String memberId, int money) throws SQLException {
        String sql = "update member set money = ? where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = DataSourceUtils.getConnection(dataSource);
            log.info("dataSource={},class={}", con, con.getClass());
            pstmt = con.prepareStatement(sql);
            pstmt.setString(2, memberId);
            pstmt.setInt(1, money);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error = ", e);
            throw e;
        } finally {
            closeStatement(pstmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }

    public void delete(String memberId) throws SQLException {
        String sql = "delete from member where member_id = ?";

        Connection con = null;
        PreparedStatement pstmt = null;

        try {
            con = dataSource.getConnection();
            log.info("dataSource={},class={}", con, con.getClass());
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);
            int resultSize = pstmt.executeUpdate();
            log.info("resultSize = {}", resultSize);
        } catch (SQLException e) {
            log.error("db error = ", e);
            throw e;
        } finally {
            closeStatement(pstmt);
            DataSourceUtils.releaseConnection(con, dataSource);
        }
    }
}
