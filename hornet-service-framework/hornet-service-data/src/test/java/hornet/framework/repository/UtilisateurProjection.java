package hornet.framework.repository;

import java.util.Set;

import org.springframework.beans.factory.annotation.Value;

public interface UtilisateurProjection {

    public interface Detail {

        Long getId();

        @Value("#{target.login}")
        String getName();

        Set<RoleSummary> getRoles();

        public interface RoleSummary {

            @Value("#{target.nom}")
            String getName();

        }

    }

}
