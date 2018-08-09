package hornet.framework.repository;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import hornet.framework.entity.Utilisateur;

public class UtilisateurSpecification {

    public static Specification<Utilisateur> LoginAndPassword(final String login, final String password) {

        return new Specification<Utilisateur>() {

            /**
             * default serialVersionUID
             */
            private static final long serialVersionUID = 1L;

            @Override
            public Predicate toPredicate(final Root<Utilisateur> root, final CriteriaQuery<?> query,
                        final CriteriaBuilder builder) {

                final List<Predicate> p = new ArrayList<>();
                p.add(builder.like(root.get("login"), login));
                if (password != null) {
                    p.add(builder.like(root.get("password"), password));
                }
                final Predicate[] result = new Predicate[p.size()];
                return builder.and(p.toArray(result));
            }
        };
    }


}
