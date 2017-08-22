package airbnb.configuration;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@ComponentScan
@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

//    @Autowired
  //  private MyDBAythenticationService myDBAythenticationService;


    /*@Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .inMemoryAuthentication()
                .withUser("user").password("password").roles("USER");
        auth.userDetailsService(myDBAythenticationService).passwordEncoder(bCryptPasswordEncoder);
    }*/

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.
			authorizeRequests()
                .antMatchers("/").permitAll()
				.antMatchers("/index").permitAll()
				.antMatchers("/register").anonymous()
				.antMatchers("/usernameCheck").anonymous()
                .antMatchers("/register_prov").anonymous()
                .antMatchers("/about").permitAll()
                .antMatchers("/error").permitAll()
                .antMatchers("/activity").permitAll()
                .antMatchers("/google_map").permitAll()
                .antMatchers("/search","/search2").permitAll()
                .antMatchers("/faq").permitAll()
                .antMatchers("/activity_reg").hasRole("2")//be a provider
                .antMatchers("/activityProvider").hasRole("2")
				.antMatchers("/categories_form").permitAll()//has to be admin
				.antMatchers("/category_submit").permitAll()//has t be admin
                .antMatchers("/profile").hasRole("1")//hasAuthority("1")//be a parent
                .antMatchers("/profileProvider").hasRole("2")//be a Provider
				.antMatchers("/admin").hasRole("0")
                .anyRequest()//be admin
				.authenticated().and().csrf().disable()
                .formLogin()
				.loginPage("/index")
                .loginProcessingUrl("/login")
                .usernameParameter("username")
                .passwordParameter("password")
                .failureUrl("/index?success=false")
                .defaultSuccessUrl("/index")
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/index").and().exceptionHandling();
				//.accessDeniedPage("/error");
        http.sessionManagement()//now added
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .maximumSessions(2)
                .expiredUrl("/index");
        http.sessionManagement()
                .invalidSessionUrl("/index")
                .sessionFixation().migrateSession();
	}

    @Bean//lately added
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }

  /*  @Bean
    public LettuceConnectionFactory connectionFactory() {
        return new LettuceConnectionFactory();
    }*/
	
	@Override
	public void configure(WebSecurity web) throws Exception {
	    web.ignoring()
	       .antMatchers("/fragments/**","/resources/**","/static/**", "/css/**", "/js/**", "/images/**","/scripts/**");
		web.ignoring().antMatchers("/the_js_path/**");
	}


   /* @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception{
//        auth.inMemoryAuthentication()
//                .withUser("admin").password("password").roles("USER","ADMIN")
//                .and()
//                .withUser("jaiprak").password("password").roles("USER","ADMIN");
        JdbcUserDetailsManager userDetailsService = new JdbcUserDetailsManager();
        userDetailsService.setDataSource(datasource);
        PasswordEncoder encoder = new BCryptPasswordEncoder();

        auth.userDetailsService(userDetailsService).passwordEncoder(encoder);
        auth.jdbcAuthentication().dataSource(datasource);


    }*/

    /*@Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests().anyRequest().fullyAuthenticated()
                .and()
                .httpBasic().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http
                .formLogin().failureUrl("/login?error")
                .defaultSuccessUrl("/")
                .loginPage("/login")
                .permitAll()
                .and()
                .logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout")).logoutSuccessUrl("/login")
                .permitAll();
    }*/
}