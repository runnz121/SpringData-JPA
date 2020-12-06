package study.datajpa.entity;


import lombok.*;

import javax.persistence.*;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id","username","age"}) //실행시 출력 연관관계 없는것들 선택
@NamedQuery(name = "Member.findByUsername",
            query ="select m from Member m where m.username =:username") //엔티티에 쿼리를 적어서 정의  // '=:' 뜻은 이미 정의된 username의 객체나 값의 조건에 따라 sql문 적용하야 비교하는것  https://developer.salesforce.com/forums/?id=9060G000000I7VAQA0
public class Member {

        @Id
        @GeneratedValue
        @Column(name = "member_id")
        private Long id;
        private String username;
        private int age;

        //ManyToOne은 기본 eager이라서 꼭 lazy타입으로 바꿔줘야한다.
        @ManyToOne(fetch = FetchType.LAZY)//N:1관계임 team 1개는 N개의 member을 받는다 (Member에서 Team으로 가는건 N ->1)
        @JoinColumn(name = "team_id")
        private Team team;


        /*protected Member() { //엔티티는 기본적으로 defatul 생성자가하나 있어야된다 (protected로 private은 안됨)

        }*/ //NoArgsConstructor가 이를 대체

        public Member(String username) {
            this.username = username;
        }


        public Member(String username, int age) {
                this.username = username;
                this.age = age;
        }


        public Member(String username, int age, Team team) {
                this.username = username;
                this.age = age;
                if(team !=null){
                        changeTeam(team);
                }
        }

        public void changeTeam(Team team) { //Member는 Team을 변경 할 수 있다.
                this.team = team;
                team.getMembers().add(this);
        }
}
