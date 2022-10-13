package hello.jdbc.repository;

import hello.jdbc.domain.Member;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.filter;
import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class MemberRepositoryV0Test {

    MemberRepositoryV0 repositoryV0 = new MemberRepositoryV0();

    @Test
    void crud() throws SQLException {
        //save
        Member member = new Member("memberV4", 10000);
        repositoryV0.save(member);

        //findById
        Member byId = repositoryV0.findById(member.getMemberId());
        log.info("findMember = {}", byId);
        assertThat(byId).isEqualTo(member);

        //update
        repositoryV0.update(byId.getMemberId(), 20000);
        Member updateM = repositoryV0.findById(member.getMemberId());
        assertThat(updateM.getMoney()).isEqualTo(20000);
    }
}