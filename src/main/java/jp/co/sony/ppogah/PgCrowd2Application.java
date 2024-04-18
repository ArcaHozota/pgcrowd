package jp.co.sony.ppogah;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

import jp.co.sony.ppogah.common.PgCrowd2Constants;
import lombok.extern.log4j.Log4j2;

/**
 * PgCrowd2アプリケーション
 *
 * @author ArkamaHozota
 * @since 1.00beta
 */
@Log4j2
@SpringBootApplication
@ServletComponentScan
public class PgCrowd2Application {
	public static void main(final String[] args) {
		SpringApplication.run(PgCrowd2Application.class, args);
		log.info(PgCrowd2Constants.MESSAGE_SPRING_APPLICATION);
	}
}
