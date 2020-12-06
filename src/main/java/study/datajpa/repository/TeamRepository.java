package study.datajpa.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import study.datajpa.entity.Team;


//이렇게 적을 경우 @Respository 를 생략해도 된다         //Entity, entity의 pk로 맵핑된 type
public interface TeamRepository extends JpaRepository<Team, Long> { //JpaRepository가 알아서 구현체를 생성하여 injection한다
}
