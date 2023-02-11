package app.quantun.api.config;

import app.quantun.api.models.Role;
import app.quantun.api.models.User;
import app.quantun.api.services.AuthenticationService;

import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;


import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;


@Transactional
@Configuration
@Slf4j
public class DataLoader implements ApplicationRunner {


    @Autowired
    private AuthenticationService userServices;

    @Autowired
    private PasswordEncoder passwordEncoder;
    @Value("${app.data.loader.user.admin.password}")
    private String adminPassword;

    @Override
    public void run(ApplicationArguments args) throws Exception {
       try {
           if (this.userServices.count()==0)
           {
               this.createUser();
           }


       }    catch (Exception e) {
            log.error("Error while loading data", e.getMessage());
        }

        //loadCsvCompanies();
    }

    private void createUser() {
        //Role role=this.roleRepository.save(Role.builder().name("ROLE_ADMIN").build());
        //Role role2=this.roleRepository.save(Role.builder().name("ROLE_USER").build());
        //List<Role> roles=List.of(role,role2);

        User user= User.builder().lastname("Manager").username("admin").firstname("admin").email("admin@admin").enabled(true).password(this.passwordEncoder.encode(this.adminPassword)).role(Role.ADMIN).build();
        this.userServices.save(user);
    }



}
