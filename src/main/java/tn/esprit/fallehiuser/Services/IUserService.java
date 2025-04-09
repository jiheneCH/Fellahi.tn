package tn.esprit.fallehiuser.Services;


import tn.esprit.fallehiuser.model.User;

import java.util.Optional;

public interface IUserService {
    Optional<User> findByEmail(String email);
    Optional<User> findByUsername(String username);
    User save(User user);
}
