package lt.gallery.config;

import lt.gallery.services.UserSevice;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true) //I used preauthorize
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final UserSevice userSevice;

    public WebSecurityConfig(UserSevice userSevice) {
        this.userSevice = userSevice;
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/**/css/**", "/**/js/**", "/**/images/**").permitAll()
                .anyRequest().authenticated()
                .antMatchers("/deletePicture/**").hasAnyRole("ADMIN")
                .antMatchers("/updatePicture/**").hasAnyRole("ADMIN","USER")
                .antMatchers("/deletePicture/**").hasAnyAuthority("ADMIN")
                .antMatchers("/updatePicture/**").hasAnyAuthority("ADMIN","USER")

                .and()
                .formLogin()
                .loginPage("/login")
                .permitAll()
                .and()
                .logout()
                .permitAll();


        //Uncomment part to login in H2 database

        http
                .authorizeRequests().antMatchers("/").permitAll().and()
                .authorizeRequests().antMatchers("/console/**").permitAll();

        http
                .csrf().disable();
        http
                .headers().frameOptions().disable();

    }

    //In-memory login
//    @Autowired
//    public void configureGlobal(AuthenticationManagerBuilder auth)
//            throws Exception {
//        auth
//                .inMemoryAuthentication()
//                .withUser("u").password(passwordEncoder().encode("u")).roles("USER")
//                .and()
//                .withUser("a").password(passwordEncoder().encode("a")).roles("ADMIN").authorities("ADMIN");
//    }


//    Authorize:
    // U: test1 P:test
    // A: test2 P:test
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userSevice)
                .passwordEncoder(passwordEncoder() );
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
