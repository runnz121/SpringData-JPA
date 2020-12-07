package study.datajpa.entity;

import lombok.Getter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import java.time.LocalDateTime;


@Getter
@MappedSuperclass//속성을 상속하여 실제 테이블까지 구현가능하게함
public class JpaBaseEntity { //JPA audit

    @Column(updatable = false)
    private LocalDateTime createdDated;
    private LocalDateTime updatedDated;

    @PrePersist
    public void prePersist(){
        LocalDateTime now = LocalDateTime.now();
        createdDated = now;
        updatedDated = now;
    }

    @PreUpdate
    public void preUpdate() {
        updatedDated = LocalDateTime.now();
    }
}
