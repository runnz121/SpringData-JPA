package study.datajpa.entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of ={"id","name"})
public class Team {

    @Id
    @GeneratedValue
    @Column(name ="team_id")
    private Long id;
    private String name;


    //두 entity가 연관관계일때 mappedby로 이어줘야 하는데 이때 FK없는쪽에다가 mappedby 써서 연결해주는게 좋다
   @OneToMany(mappedBy ="team") //Team이 1개일때 N개의 Member를 받는다(Team 에서 Member로 가는것 1 ->N)

    private List<Member> members = new ArrayList<>();

   public Team(String name) {
       this.name = name;
   }


}
