package study.datajpa.dto;

import lombok.Data;
import study.datajpa.entity.Member;

@Data//no serializer 오류 해결 @data가 없어서 발생했음
public class MemberDto {

    private Long id; //전달인자 (argument)는 생성자(constructor)가 필요하다
    private String username;
    private String teamName;

    public MemberDto(Long id, String username, String teamName) {

        this.id = id;
        this.username = username;
        this.teamName = teamName;
    }

    public MemberDto(Member member){
        this.id = member.getId();
        this.username = member.getUsername();
    }
}
