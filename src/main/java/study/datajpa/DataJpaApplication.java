package study.datajpa;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.util.Optional;
import java.util.UUID;

@EnableJpaAuditing//메인 매소드에 옵션 넣어줘야 audit 작동
@SpringBootApplication
public class DataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataJpaApplication.class, args);
	}

	@Bean//BaseEntity의 createby, modifiedby를 이용하기 위해 설정
	public AuditorAware<String> auditorProvider(){
		return () -> Optional.of(UUID.randomUUID().toString());
	}

	/*람다 풀어쓰기
	@Bean
	public AuditorAware<String> auditorProvider(){
		return new AuditorAware<String>() { //인터페이스에서 메소드가 하나면 람다로 바꿀 수 있다.

		@Override
		public Optional<String> getCurrentAudtior() {
			return Optional.of(UUID.randomUUID().toString());
	 */


}
