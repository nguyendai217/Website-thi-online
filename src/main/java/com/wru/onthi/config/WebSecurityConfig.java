package com.wru.onthi.config;

import com.wru.onthi.oauth.CustomOAuth2UserService;
import com.wru.onthi.oauth.OAuth2LoginSuccessHandler;
import com.wru.onthi.services.serviceImpl.UserDetailsServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;


@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public UserDetailsService userDetailsService(){
        return new UserDetailsServiceImpl();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider= new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService());
        return authProvider;
    }

    @Autowired
    AuthenticationSuccessHandler authSuccessHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());
    }
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers("/","/login/**","/reset_password",
                        "/oauth2/**","/signin","/class/**","/tintuc/**","/lophoc/**"
                        ,"/monhoc/**","/baihoc/**","/noidungbaihoc/**", "/image/**","/search/**"
                        ,"/kiemtra/**","/contact/**","/lesson","/user/profile","/hoctap/**","/comment/**","/info/**").permitAll()
                .antMatchers("/home/**","/history/**","/question/**")
                .hasAnyAuthority("USER","ADMIN","MANAGER")
                .antMatchers("/admin/**","/profile/**","/lesson/**",
                        "/exam/**","/download/**","/thongke/**","/result/**","/comment-manager/**")
                .hasAnyAuthority("ADMIN","MANAGER")
                .anyRequest().authenticated()
                //login
                .and().formLogin().loginPage("/login").usernameParameter("email").passwordParameter("password")
                .successHandler(authSuccessHandler).and().oauth2Login().loginPage("/login")
                .userInfoEndpoint().userService(oAuth2UserService).and().successHandler(oAuth2LoginSuccessHandler)
                .and().logout().logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
                .logoutSuccessUrl("/?logout=true").invalidateHttpSession(true)
                .deleteCookies("JSESSIONID").permitAll()
                .and().exceptionHandling().accessDeniedPage("/403");

                //remember me configuration
                http.rememberMe().
                key("remember-key").
                rememberMeParameter("remember").
                rememberMeCookieName("my-remember-me").
                tokenValiditySeconds(1296000);

                //http.csrf().disable();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/resources/**", "/image/**"
                ,"/css/**","/js/**","/static/**","/templates/**");
    }

//    @Bean
//    public PersistentTokenRepository persistentTokenRepository() {
//        JdbcTokenRepositoryImpl tokenRepository = new JdbcTokenRepositoryImpl();
//        tokenRepository.setDataSource(dataSource);
//        return tokenRepository;
//    }

    @Autowired
    private CustomOAuth2UserService oAuth2UserService;

    @Autowired
    private OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
}
