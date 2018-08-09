package hornet.framework.entity;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "ROLE")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Role  implements Serializable {
	
    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 1L;
    
    @Id
    //@SequenceGenerator(name="role_id_generator", initialValue=1, allocationSize=1, sequenceName="role_id_sequence")
    //@GeneratedValue(strategy=GenerationType.SEQUENCE, generator="role_id_generator")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @NotNull
    @Column(name = "id_role")
    public Integer id;
    
    @NotNull
    @Column(name = "rol_nom")
    public String nom;
    
    @ManyToMany
    @JoinTable(name="role_utilisateur", joinColumns=@JoinColumn(name="id_role", referencedColumnName="id_role"),
    	inverseJoinColumns=@JoinColumn(name="id_utilisateur", referencedColumnName="id_utilisateur"))
    public Set<Utilisateur> roleUtilisateur;
}
