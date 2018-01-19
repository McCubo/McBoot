package org.cubias.config;

import java.util.Arrays;

import org.cubias.security.CustomActiveDirectoryLdapAuthenticationProvider;
import org.cubias.security.CustomAuthoritiesPopulator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AccessDeniedHandler accessDeniedHandler;

	@Value("${ad.domain}")
	private String AD_DOMAIN;

	@Value("${ad.url}")
	private String AD_URL;

	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/webjars/**");
		web.ignoring().antMatchers("/css/**", "/fonts/**", "/libs/**", "/js/**", "/font/**", "/fonts/**", "/img/**");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// http.authorizeRequests().anyRequest().authenticated().and().httpBasic();
		http.csrf().disable()
			.authorizeRequests()				
				.antMatchers("/admin/**")
					.hasAuthority("ADMIN")
					.anyRequest()
					.authenticated()
				.and()
				.formLogin()
					.loginPage("/login")
					.failureUrl("/login?expected_fatal_error")
					.permitAll()
				.and()
				.logout()
					.permitAll()
				.and()
					.exceptionHandling().accessDeniedHandler(accessDeniedHandler);
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(activeDirectoryLdapAuthenticationProvider())
				.userDetailsService(userDetailsService());
	}

	@Bean
	public AuthenticationProvider activeDirectoryLdapAuthenticationProvider() {
		CustomActiveDirectoryLdapAuthenticationProvider provider = new CustomActiveDirectoryLdapAuthenticationProvider(
				AD_DOMAIN, AD_URL, customAuthoritiesPopulator());
		provider.setConvertSubErrorCodesToExceptions(false);
		provider.setUseAuthenticationRequestCredentials(true);
		return provider;
	}

	@Bean
	public AuthenticationManager authenticationManager() {
		return new ProviderManager(Arrays.asList(activeDirectoryLdapAuthenticationProvider()));
	}

	@Bean
	public CustomAuthoritiesPopulator customAuthoritiesPopulator() {
		return new CustomAuthoritiesPopulator();
	}
}
