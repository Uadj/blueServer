package com.example.demo.User;

import com.example.demo.Ranking.RankingService;
import com.example.demo.Ranking.UserRankingDto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserService userService;
    private final RankingService rankingService;

    public UserController(UserService userService, RankingService rankingService) {
        this.userService = userService;
        this.rankingService = rankingService;
    }
    @Autowired
    private UserRepository userRepository;
    @PersistenceContext
    private EntityManager entityManager;

    @GetMapping("/users/{id}")
    public User getUser(@PathVariable Long id) {
        return entityManager.find(User.class, id);
    }

    @PostMapping("/users")
    @Transactional
    public void createUser(@RequestBody User user) {
        entityManager.persist(user);
    }

    @PutMapping("/users/{id}")
    @Transactional
    public void updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        User existingUser = entityManager.find(User.class, id);
        if (existingUser != null) {
            existingUser.setName(updatedUser.getName());
            existingUser.setLevel(updatedUser.getLevel());
            entityManager.merge(existingUser);
        }
    }

    @DeleteMapping("/users/{id}")
    @Transactional
    public void deleteUser(@PathVariable Long id) {
        User user = entityManager.find(User.class, id);
        if (user != null) {
            entityManager.remove(user);
        }
    }

    @GetMapping("/users")
    public List<User> getAllUsers() {
        String query = "SELECT u FROM User u";
        return entityManager.createQuery(query, User.class).getResultList();
    }

    @GetMapping("/users/ranking/{username}")
    public int getUserRank(@PathVariable String username) {
        return rankingService.getUserRank(username);
    }
    @GetMapping("/users/ranking")
    public List<UserRankingDto> getTopRanking() {
        return rankingService.getTopRanking(15);
    }

}
