package com.ifsc.ctds.stinghen.recycle_it_api.models.user;

import com.ifsc.ctds.stinghen.recycle_it_api.security.models.UserCredentials;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public abstract class User implements IUser {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    private UserCredentials credential;

}
