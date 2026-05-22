package hrm.auth;

import hrm.auth.defaultSetup.DefaultSetupManager;
import hrm.auth.defaultSetup.DefaultUserAndRoleCreator;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class AuthServiceApplication implements CommandLineRunner {

    private final DefaultUserAndRoleCreator defaultUserAndRoleCreator;
    private final DefaultSetupManager defaultSetupManager;



    AuthServiceApplication(DefaultUserAndRoleCreator defaultUserAndRoleCreator,
                           DefaultSetupManager defaultSetupManager){
        this.defaultSetupManager=defaultSetupManager;
        this.defaultUserAndRoleCreator=defaultUserAndRoleCreator;

    }

    public static void main(String[] args) {
        SpringApplication.run(AuthServiceApplication.class,args);
    }

    @Override
    public void run(String... args) throws Exception {
        defaultUserAndRoleCreator.createDefaultUserAndRole();
        defaultSetupManager.retrieveAllEndPointsAndSaveToDB();
    }
}