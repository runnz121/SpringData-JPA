package study.datajpa.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class Member {

        @Id
        @GeneratedValue
        private Long id;
        private String username;

        public Member() { //엔티티는 기본적으로 defatul 생성자가하나 있어야된다 (protected로 private은 안됨)

        }

        public Member(String username) {
            this.username = username;
        }
}
